import { Article } from '@/types';
import { OFFLINE_ARTICLES_EN, OFFLINE_ARTICLES_IT } from '@/lib/offlinePool';

const WIKI_API_TIMEOUT = 7000;

interface FetchOptions {
  lang?: 'it' | 'en';
  retries?: number;
}

/**
 * Clean and validate a Wikipedia summary object
 */
function isValidArticle(article: Partial<Article>): article is Article {
  if (!article || !article.title || !article.extract) return false;
  if (article.type === 'disambiguation' || article.type === 'no-extract') return false;
  // Summary should have enough text to build meaningful questions
  if (article.extract.trim().length < 60) return false;
  // Discard list / index pages
  if (article.title.toLowerCase().startsWith('lista di') || article.title.toLowerCase().startsWith('list of')) return false;
  return true;
}

/**
 * Fetch a single random article summary from Wikipedia
 */
export async function fetchRandomSummary(options: FetchOptions = {}): Promise<Article> {
  const lang = options.lang || 'it';
  const url = `https://${lang}.wikipedia.org/api/rest_v1/page/random/summary`;

  const controller = new AbortController();
  const timeoutId = setTimeout(() => controller.abort(), WIKI_API_TIMEOUT);

  try {
    const response = await fetch(url, {
      signal: controller.signal,
      headers: {
        'Accept': 'application/json',
        'Api-User-Agent': 'WikingoApp/1.0 (gamified microlearning)'
      }
    });

    clearTimeout(timeoutId);

    if (!response.ok) {
      throw new Error(`Wikipedia HTTP error: ${response.status}`);
    }

    const data: Article = await response.json();
    data.lang = lang;

    if (!isValidArticle(data)) {
      // Retry once if invalid article
      return fetchRandomSummary({ ...options, retries: (options.retries || 0) + 1 });
    }

    return data;
  } catch (error) {
    clearTimeout(timeoutId);
    // If network fails or offline, draw from offline pool
    const pool = lang === 'it' ? OFFLINE_ARTICLES_IT : OFFLINE_ARTICLES_EN;
    const randomIndex = Math.floor(Math.random() * pool.length);
    return pool[randomIndex];
  }
}

/**
 * Fetch a summary for a specific Wikipedia title
 */
export async function fetchSummaryByTitle(title: string, lang: 'it' | 'en' = 'it'): Promise<Article | null> {
  const url = `https://${lang}.wikipedia.org/api/rest_v1/page/summary/${encodeURIComponent(title)}`;
  const controller = new AbortController();
  const timeoutId = setTimeout(() => controller.abort(), WIKI_API_TIMEOUT);

  try {
    const response = await fetch(url, {
      signal: controller.signal,
      headers: {
        'Accept': 'application/json',
        'Api-User-Agent': 'WikingoApp/1.0 (gamified microlearning)'
      }
    });
    clearTimeout(timeoutId);

    if (!response.ok) return null;
    const data: Article = await response.json();
    data.lang = lang;
    return isValidArticle(data) ? data : null;
  } catch {
    clearTimeout(timeoutId);
    return null;
  }
}

/**
 * Fetch a batch of articles specifically relevant to a topic/keywords (e.g. for Unit progression)
 */
export async function fetchArticlesForTopic(
  keywords: string[],
  count: number = 5,
  lang: 'it' | 'en' = 'it'
): Promise<Article[]> {
  try {
    const query = keywords[Math.floor(Math.random() * keywords.length)] || keywords[0] || 'Storia';
    const searchUrl = `https://${lang}.wikipedia.org/w/api.php?action=query&list=search&srsearch=${encodeURIComponent(query)}&format=json&origin=*&utf8=1&srlimit=8`;

    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), WIKI_API_TIMEOUT);

    const searchRes = await fetch(searchUrl, {
      signal: controller.signal,
      headers: {
        'Accept': 'application/json',
        'Api-User-Agent': 'WikingoApp/1.0 (gamified microlearning)'
      }
    });
    clearTimeout(timeoutId);

    if (searchRes.ok) {
      const searchData = await searchRes.json();
      const searchHits: { title: string }[] = searchData?.query?.search || [];

      if (searchHits.length > 0) {
        const articlePromises = searchHits.slice(0, count + 2).map((hit) =>
          fetchSummaryByTitle(hit.title, lang)
        );
        const resolved = await Promise.allSettled(articlePromises);
        const topicArticles: Article[] = [];

        for (const r of resolved) {
          if (r.status === 'fulfilled' && r.value) {
            topicArticles.push(r.value);
            if (topicArticles.length >= count) break;
          }
        }

        if (topicArticles.length >= count) {
          return topicArticles.slice(0, count);
        }
      }
    }
  } catch (err) {
    console.warn('Topic search fallback to random batch:', err);
  }

  // Fallback to random batch if topic search did not yield enough results
  return fetchArticleBatch(count, lang);
}

/**
 * Fetch a batch of unique random Wikipedia articles for a full round
 */
export async function fetchArticleBatch(count: number = 5, lang: 'it' | 'en' = 'it'): Promise<Article[]> {
  const articles: Article[] = [];
  const seenIds = new Set<number>();
  const pool = lang === 'it' ? OFFLINE_ARTICLES_IT : OFFLINE_ARTICLES_EN;

  // Attempt parallel fetches with safety limit
  const fetchPromises = Array.from({ length: Math.min(count + 2, 8) }).map(() => 
    fetchRandomSummary({ lang })
  );

  const results = await Promise.allSettled(fetchPromises);

  for (const res of results) {
    if (res.status === 'fulfilled' && res.value && !seenIds.has(res.value.pageid)) {
      articles.push(res.value);
      seenIds.add(res.value.pageid);
      if (articles.length >= count) break;
    }
  }

  // If online fetched fewer than needed, backfill with offline pool
  if (articles.length < count) {
    for (const offlineArt of pool) {
      if (!seenIds.has(offlineArt.pageid)) {
        articles.push(offlineArt);
        seenIds.add(offlineArt.pageid);
        if (articles.length >= count) break;
      }
    }
  }

  return articles.slice(0, count);
}
