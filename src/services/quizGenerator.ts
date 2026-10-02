import { Article, Question, QuestionOption, QuestionType } from '@/types';
import { shuffleArray, truncateWords } from '@/lib/utils';

// Knowledge pool of plausible generic distractors for multiple choice fallback
const FALLBACK_DISTRACTORS_IT = [
  'Scrittore e filosofo dell\'età classica',
  'Composto chimico sintetizzato in laboratorio',
  'Città medievale situata sulle rive del fiume Danubio',
  'Costellazione visibile nell\'emisfero boreale',
  'Trattato internazionale stipulato nel XIX secolo',
  'Specie di felino endemica delle foreste pluviali',
  'Dipinto a olio su tela del periodo barocco',
  'Stazione spaziale orbitante in orbita terrestre bassa',
  'Fiume navigated che attraversa l\'Europa centrale',
  'Parco nazionale noto per i suoi geyser vulcanici'
];

const FALLBACK_DISTRACTORS_EN = [
  'Classical philosopher and historian',
  'Chemical compound synthesized in laboratory',
  'Medieval town situated on the Danube river',
  'Constellation visible in the northern hemisphere',
  'International treaty signed in the 19th century',
  'Endangered feline species of the rainforest',
  'Baroque oil painting on canvas',
  'Space station in low Earth orbit',
  'Navigable river flowing across Central Europe',
  'National park famed for geothermal geysers'
];

/**
 * Remove the article's title from the extract to create a clue for subject recognition
 */
function maskTitleInText(text: string, title: string, lang: 'it' | 'en'): string {
  const cleanTitle = title.replace(/\s*\(.*?\)\s*/g, '').trim();
  const escaped = cleanTitle.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
  const regex = new RegExp(`\\b${escaped}\\b`, 'gi');
  const replacement = lang === 'it' ? '«Questo soggetto»' : '«This subject»';
  let masked = text.replace(regex, replacement);

  // If title was at start of sentence
  masked = masked.replace(new RegExp(`^${escaped}`, 'gi'), replacement);
  return masked;
}

/**
 * Generate a Subject Recognition Question:
 * "A quale voce corrisponde questa descrizione?"
 */
export function generateSubjectRecognitionQuestion(
  article: Article,
  allArticles: Article[],
  index: number = 0
): Question {
  const isIt = article.lang === 'it';
  const maskedExtract = maskTitleInText(article.extract, article.title, article.lang);
  const snippet = truncateWords(maskedExtract, 180);

  const prompt = isIt
    ? `A quale voce o entità di Wikipedia corrisponde questa descrizione?`
    : `Which Wikipedia subject corresponds to this description?`;

  const correctOption: QuestionOption = {
    id: `opt_${index}_correct`,
    text: article.title,
    isCorrect: true,
  };

  // Get distractors from other articles in the batch, backfilled by curated titles
  const fallbackTitlesIt = [
    'Leonardo da Vinci',
    'Colosseo',
    'Margherita Hack',
    'Sistema Solare',
    'Dante Alighieri',
    'Wolfgang Amadeus Mozart',
    'Galileo Galilei',
    'Stretto di Messina',
    'Alessandro Volta',
    'Guglielmo Marconi'
  ];
  const fallbackTitlesEn = [
    'Isaac Newton',
    'Ancient Rome',
    'James Webb Space Telescope',
    'Albert Einstein',
    'William Shakespeare',
    'Ludwig van Beethoven',
    'Milky Way',
    'Amazon Rainforest',
    'Marie Curie',
    'Charles Darwin'
  ];
  const otherTitles = allArticles
    .filter(a => a.pageid !== article.pageid && a.title.toLowerCase() !== article.title.toLowerCase())
    .map(a => a.title);

  const fallbackPool = isIt ? fallbackTitlesIt : fallbackTitlesEn;
  const distractorCandidates = Array.from(new Set([
    ...otherTitles,
    ...fallbackPool.filter(t => t.toLowerCase() !== article.title.toLowerCase())
  ]));

  const chosenDistractors = shuffleArray(distractorCandidates).slice(0, 3);

  const distractorOptions: QuestionOption[] = chosenDistractors.map((text, idx) => ({
    id: `opt_${index}_dist_${idx}`,
    text,
    isCorrect: false,
  }));

  const options = shuffleArray([correctOption, ...distractorOptions]);

  const explanation = isIt
    ? `Si tratta di "${article.title}". ${article.extract}`
    : `This refers to "${article.title}". ${article.extract}`;

  return {
    id: `q_${article.pageid}_subject`,
    type: 'subject_recognition',
    article,
    prompt,
    clozeContext: {
      before: snippet,
      blank: article.title,
      after: '',
      fullSentence: article.extract
    },
    options,
    correctOptionId: correctOption.id,
    explanation,
    sourceUrl: article.content_urls.desktop.page,
    categoryHint: article.description || (isIt ? 'Cultura Generale' : 'General Knowledge')
  };
}

/**
 * Generate a Multiple Choice Question:
 * "Cosa definisce o chi è: [Titolo]?"
 */
function generateMultipleChoiceQuestion(
  article: Article,
  allArticles: Article[],
  index: number
): Question {
  const isIt = article.lang === 'it';
  const prompt = isIt
    ? `Cosa definisce o chi è "${article.title}"?`
    : `What defines or who is "${article.title}"?`;

  // Best definition: description if available, else first sentence of extract
  let correctText = article.description;
  if (!correctText || correctText.length < 10) {
    const firstSentence = article.extract.split('.')[0];
    // Remove title from start
    const cleanTitle = article.title.replace(/\s*\(.*?\)\s*/g, '').trim();
    correctText = firstSentence.replace(new RegExp(`^${cleanTitle}\\s*(è|era|stato|stata|is|was)?\\s*`, 'i'), '').trim();
    if (correctText.length > 75) correctText = truncateWords(correctText, 75);
  }

  // Capitalize first letter
  correctText = correctText.charAt(0).toUpperCase() + correctText.slice(1);

  const correctOption: QuestionOption = {
    id: `opt_${index}_correct`,
    text: correctText,
    isCorrect: true,
  };

  // Find distractors from other articles
  const otherDescriptions = allArticles
    .filter(a => a.pageid !== article.pageid && a.description)
    .map(a => a.description!.charAt(0).toUpperCase() + a.description!.slice(1));

  const fallbackPool = isIt ? FALLBACK_DISTRACTORS_IT : FALLBACK_DISTRACTORS_EN;
  const distractorCandidates = Array.from(new Set([...otherDescriptions, ...fallbackPool]));
  const chosenDistractors = shuffleArray(distractorCandidates).slice(0, 3);

  const distractorOptions: QuestionOption[] = chosenDistractors.map((text, idx) => ({
    id: `opt_${index}_dist_${idx}`,
    text,
    isCorrect: false,
  }));

  const options = shuffleArray([correctOption, ...distractorOptions]);

  const explanation = isIt
    ? `"${article.title}": ${article.extract}`
    : `"${article.title}": ${article.extract}`;

  return {
    id: `q_${article.pageid}_mc`,
    type: 'multiple_choice',
    article,
    prompt,
    options,
    correctOptionId: correctOption.id,
    explanation,
    sourceUrl: article.content_urls.desktop.page,
    categoryHint: isIt ? 'Definizioni' : 'Definitions'
  };
}

/**
 * Generate a Cloze / Fill-in-the-blank Question
 */
function generateClozeQuestion(
  article: Article,
  allArticles: Article[],
  index: number
): Question {
  const isIt = article.lang === 'it';
  const sentences = article.extract.split(/(?<=[.!?])\s+/);
  const sentence = sentences[0] || article.extract;

  // Words that are good candidates for cloze (length >= 5, not common stop words)
  const stopWordsIt = new Set(['questo', 'questa', 'quello', 'quella', 'essere', 'stato', 'stata', 'della', 'delle', 'degli', 'anche', 'hanno', 'nella', 'nelle']);
  const stopWordsEn = new Set(['which', 'there', 'their', 'about', 'would', 'could', 'after', 'where', 'being', 'under']);
  const stopWords = isIt ? stopWordsIt : stopWordsEn;

  const words = sentence.split(/\s+/);
  const candidates = words
    .map((w, i) => ({ word: w.replace(/[^\w\u00C0-\u017F]/g, ''), raw: w, index: i }))
    .filter(c => c.word.length >= 5 && !stopWords.has(c.word.toLowerCase()));

  // Pick a candidate word near the middle
  const chosen = candidates[Math.floor(candidates.length / 2)] || candidates[0] || {
    word: article.title.split(' ')[0],
    raw: article.title.split(' ')[0],
    index: 0
  };

  const before = words.slice(0, chosen.index).join(' ');
  const after = words.slice(chosen.index + 1).join(' ');

  const correctOption: QuestionOption = {
    id: `opt_${index}_correct`,
    text: chosen.word,
    isCorrect: true,
  };

  // Generate plausible distractors
  const distractorsWordsIt = ['secolo', 'città', 'regione', 'sviluppo', 'periodo', 'scoperta', 'sistema', 'capitale', 'cultura', 'struttura'];
  const distractorsWordsEn = ['century', 'region', 'development', 'period', 'discovery', 'system', 'capital', 'culture', 'structure', 'science'];
  const pool = isIt ? distractorsWordsIt : distractorsWordsEn;

  // Add words from other articles if available
  const otherWords = allArticles
    .filter(a => a.pageid !== article.pageid)
    .flatMap(a => a.title.split(' '))
    .filter(w => w.length >= 4 && w.toLowerCase() !== chosen.word.toLowerCase());

  const distractorCandidates = Array.from(new Set([...pool, ...otherWords]))
    .filter(w => w.toLowerCase() !== chosen.word.toLowerCase());

  const chosenDistractors = shuffleArray(distractorCandidates).slice(0, 3);

  const distractorOptions: QuestionOption[] = chosenDistractors.map((text, idx) => ({
    id: `opt_${index}_dist_${idx}`,
    text,
    isCorrect: false,
  }));

  const options = shuffleArray([correctOption, ...distractorOptions]);

  const prompt = isIt
    ? `Completa la frase mancante tratta dalla voce "${article.title}":`
    : `Complete the missing word from the article "${article.title}":`;

  return {
    id: `q_${article.pageid}_cloze`,
    type: 'cloze',
    article,
    prompt,
    clozeContext: {
      before,
      blank: chosen.word,
      after,
      fullSentence: sentence
    },
    options,
    correctOptionId: correctOption.id,
    explanation: `Frase corretta: "${sentence}"`,
    sourceUrl: article.content_urls.desktop.page,
    categoryHint: isIt ? 'Completamento' : 'Fill in the blank'
  };
}

/**
 * Generate a True / False Question
 */
function generateTrueFalseQuestion(
  article: Article,
  allArticles: Article[],
  index: number
): Question {
  const isIt = article.lang === 'it';
  const isActuallyTrue = Math.random() >= 0.45; // ~55% true, 45% false for good balance
  const firstSentence = article.extract.split('.')[0];

  let statement = firstSentence;
  let explanation = '';

  if (isActuallyTrue) {
    explanation = isIt 
      ? `Esatto! "${article.title}" corrisponde a: ${article.extract}`
      : `Correct! "${article.title}" is indeed: ${article.extract}`;
  } else {
    // Modify statement by swapping subject with another article
    const otherArticle = allArticles.find(a => a.pageid !== article.pageid);
    if (otherArticle) {
      statement = statement.replace(article.title, otherArticle.title);
      explanation = isIt
        ? `Falso! Questa descrizione non si riferisce a "${otherArticle.title}", bensì a "${article.title}". ${article.extract}`
        : `False! This description does not describe "${otherArticle.title}", but rather "${article.title}". ${article.extract}`;
    } else {
      statement = isIt ? `${firstSentence} (non appartiene al pianeta Terra)` : `${firstSentence} (located on Mars)`;
      explanation = isIt
        ? `Falso! ${article.extract}`
        : `False! ${article.extract}`;
    }
  }

  const prompt = isIt
    ? `Vero o Falso: valuta questa affermazione su "${isActuallyTrue ? article.title : 'questo argomento'}":`
    : `True or False: evaluate this statement:`;

  const trueOptionId = `opt_${index}_true`;
  const falseOptionId = `opt_${index}_false`;

  const options: QuestionOption[] = [
    {
      id: trueOptionId,
      text: isIt ? 'Vero' : 'True',
      isCorrect: isActuallyTrue
    },
    {
      id: falseOptionId,
      text: isIt ? 'Falso' : 'False',
      isCorrect: !isActuallyTrue
    }
  ];

  return {
    id: `q_${article.pageid}_tf`,
    type: 'true_false',
    article,
    prompt,
    clozeContext: {
      before: statement,
      blank: '',
      after: '',
      fullSentence: statement
    },
    options,
    correctOptionId: isActuallyTrue ? trueOptionId : falseOptionId,
    explanation,
    sourceUrl: article.content_urls.desktop.page,
    categoryHint: isIt ? 'Vero o Falso' : 'True or False'
  };
}

/**
 * Orchestrate the generation of a full 5-question round with diverse question types
 */
export function generateQuizQuestions(articles: Article[]): Question[] {
  const types: QuestionType[] = ['subject_recognition', 'multiple_choice', 'cloze', 'true_false', 'multiple_choice'];
  const shuffledTypes = shuffleArray(types);

  return articles.map((article, index) => {
    const qType = shuffledTypes[index % shuffledTypes.length];
    switch (qType) {
      case 'subject_recognition':
        return generateSubjectRecognitionQuestion(article, articles, index);
      case 'cloze':
        return generateClozeQuestion(article, articles, index);
      case 'true_false':
        return generateTrueFalseQuestion(article, articles, index);
      case 'multiple_choice':
      default:
        return generateMultipleChoiceQuestion(article, articles, index);
    }
  });
}
