import { useState, useCallback, useEffect } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { useQuizStore } from '@/store/useQuizStore';
import { fetchArticleBatch, fetchArticlesForTopic } from '@/services/wikipedia';
import { generateQuizQuestions } from '@/services/quizGenerator';
import { getUnitForLesson } from '@/lib/unitsData';
import { Question } from '@/types';

export function useWikipediaQuiz() {
  const queryClient = useQueryClient();
  const language = useQuizStore((s) => s.language);
  const startRound = useQuizStore((s) => s.startRound);
  const setLoadingRound = useQuizStore((s) => s.setLoadingRound);
  const isLoading = useQuizStore((s) => s.isLoadingRound);

  const [isOnline, setIsOnline] = useState<boolean>(
    typeof navigator !== 'undefined' ? navigator.onLine : true
  );

  useEffect(() => {
    const handleOnline = () => setIsOnline(true);
    const handleOffline = () => setIsOnline(false);

    window.addEventListener('online', handleOnline);
    window.addEventListener('offline', handleOffline);

    return () => {
      window.removeEventListener('online', handleOnline);
      window.removeEventListener('offline', handleOffline);
    };
  }, []);

  /**
   * Fetch a fresh batch for a specific numbered lesson according to its unit topic
   */
  const loadLesson = useCallback(
    async (lessonNumber: number, count: number = 5): Promise<Question[]> => {
      setLoadingRound(true, lessonNumber);
      try {
        const unit = getUnitForLesson(lessonNumber);
        const cacheKey = ['wikipedia-lesson', lessonNumber, language, Date.now()];

        const articles = await queryClient.fetchQuery({
          queryKey: cacheKey,
          queryFn: () => fetchArticlesForTopic(unit.keywords, count, language),
          staleTime: 1000 * 60 * 10,
        });

        const questions = generateQuizQuestions(articles);
        startRound(questions, lessonNumber);
        return questions;
      } catch (err) {
        console.error('Failed to load lesson:', err);
        const fallbackArticles = await fetchArticleBatch(count, language);
        const fallbackQuestions = generateQuizQuestions(fallbackArticles);
        startRound(fallbackQuestions, lessonNumber);
        return fallbackQuestions;
      } finally {
        setLoadingRound(false);
      }
    },
    [language, queryClient, setLoadingRound, startRound]
  );

  /**
   * Fetch a fresh batch and generate questions, utilizing React Query cache
   */
  const loadNewRound = useCallback(
    async (count: number = 5): Promise<Question[]> => {
      setLoadingRound(true);
      try {
        const cacheKey = ['wikipedia-batch', language, Date.now()];
        
        const articles = await queryClient.fetchQuery({
          queryKey: cacheKey,
          queryFn: () => fetchArticleBatch(count, language),
          staleTime: 1000 * 60 * 10, // 10 minutes cache
        });

        const questions = generateQuizQuestions(articles);
        startRound(questions);
        return questions;
      } catch (err) {
        console.error('Failed to load Wikipedia quiz round:', err);
        const fallbackArticles = await fetchArticleBatch(count, language);
        const fallbackQuestions = generateQuizQuestions(fallbackArticles);
        startRound(fallbackQuestions);
        return fallbackQuestions;
      } finally {
        setLoadingRound(false);
      }
    },
    [language, queryClient, setLoadingRound, startRound]
  );

  /**
   * Pre-fetches the subsequent round in the background for zero-latency continuation
   */
  const prefetchNextRound = useCallback(
    async (count: number = 5) => {
      try {
        await queryClient.prefetchQuery({
          queryKey: ['wikipedia-prefetch', language],
          queryFn: () => fetchArticleBatch(count, language),
          staleTime: 1000 * 60 * 5,
        });
      } catch {}
    },
    [language, queryClient]
  );

  return {
    loadLesson,
    loadNewRound,
    prefetchNextRound,
    isLoading,
    isOnline,
  };
}
