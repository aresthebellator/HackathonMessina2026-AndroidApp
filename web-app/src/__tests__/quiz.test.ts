import { describe, it, expect, beforeEach } from 'vitest';
import { generateQuizQuestions } from '@/services/quizGenerator';
import { OFFLINE_ARTICLES_IT } from '@/lib/offlinePool';
import { useQuizStore } from '@/store/useQuizStore';

describe('Quiz Generator Service', () => {
  it('should generate the exact number of questions as input articles', () => {
    const articles = OFFLINE_ARTICLES_IT.slice(0, 5);
    const questions = generateQuizQuestions(articles);

    expect(questions).toHaveLength(5);
  });

  it('should ensure each generated question has a valid correctOptionId', () => {
    const articles = OFFLINE_ARTICLES_IT.slice(0, 5);
    const questions = generateQuizQuestions(articles);

    questions.forEach((q) => {
      expect(q.options.length).toBeGreaterThanOrEqual(2);
      const correctOption = q.options.find((opt) => opt.id === q.correctOptionId);
      expect(correctOption).toBeDefined();
      expect(correctOption?.isCorrect).toBe(true);

      // Verify only 1 option is marked correct
      const correctCount = q.options.filter((opt) => opt.isCorrect).length;
      expect(correctCount).toBe(1);
    });
  });

  it('should generate valid prompts and explanations', () => {
    const articles = OFFLINE_ARTICLES_IT.slice(0, 4);
    const questions = generateQuizQuestions(articles);

    questions.forEach((q) => {
      expect(q.prompt.length).toBeGreaterThan(5);
      expect(q.explanation.length).toBeGreaterThan(10);
      expect(q.sourceUrl).toContain('wikipedia.org');
    });
  });
});

describe('Quiz Store (Zustand)', () => {
  beforeEach(() => {
    const store = useQuizStore.getState();
    store.quitQuiz();
    store.restoreLives();
  });

  it('should initialize with default gamification values', () => {
    const state = useQuizStore.getState();
    expect(state.lives).toBe(5);
    expect(state.streak).toBeGreaterThanOrEqual(1);
    expect(state.feedbackStatus).toBe('idle');
  });

  it('should start a round properly', () => {
    const articles = OFFLINE_ARTICLES_IT.slice(0, 3);
    const questions = generateQuizQuestions(articles);

    useQuizStore.getState().startRound(questions);
    const state = useQuizStore.getState();

    expect(state.currentRound).toBeDefined();
    expect(state.currentRound?.questions).toHaveLength(3);
    expect(state.currentRound?.currentIndex).toBe(0);
    expect(state.selectedOptionId).toBeNull();
  });

  it('should record correct answer and increase score/XP', () => {
    const articles = OFFLINE_ARTICLES_IT.slice(0, 2);
    const questions = generateQuizQuestions(articles);
    const initialXp = useQuizStore.getState().xp;

    useQuizStore.getState().startRound(questions);
    const currentQ = questions[0];

    // Select correct option
    useQuizStore.getState().selectOption(currentQ.correctOptionId);
    expect(useQuizStore.getState().selectedOptionId).toBe(currentQ.correctOptionId);

    // Check answer
    useQuizStore.getState().checkAnswer();
    const state = useQuizStore.getState();

    expect(state.feedbackStatus).toBe('correct');
    expect(state.currentRound?.score).toBe(1);
    expect(state.xp).toBe(initialXp + 15);
    expect(state.lives).toBe(5);
  });

  it('should record wrong answer and decrement lives', () => {
    const articles = OFFLINE_ARTICLES_IT.slice(0, 2);
    const questions = generateQuizQuestions(articles);

    useQuizStore.getState().startRound(questions);
    const currentQ = questions[0];

    // Find wrong option
    const wrongOption = currentQ.options.find((opt) => opt.id !== currentQ.correctOptionId);
    expect(wrongOption).toBeDefined();

    useQuizStore.getState().selectOption(wrongOption!.id);
    useQuizStore.getState().checkAnswer();
    const state = useQuizStore.getState();

    expect(state.feedbackStatus).toBe('incorrect');
    expect(state.lives).toBe(4);
  });

  it('should toggle bookmarks for articles', () => {
    const article = OFFLINE_ARTICLES_IT[0];

    expect(useQuizStore.getState().isArticleSaved(article.pageid)).toBe(false);

    useQuizStore.getState().toggleBookmark(article);
    expect(useQuizStore.getState().isArticleSaved(article.pageid)).toBe(true);

    useQuizStore.getState().toggleBookmark(article);
    expect(useQuizStore.getState().isArticleSaved(article.pageid)).toBe(false);
  });
});
