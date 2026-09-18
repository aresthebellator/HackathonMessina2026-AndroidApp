import { create } from 'zustand';
import { persist, createJSONStorage, StateStorage } from 'zustand/middleware';
import { Article, FeedbackStatus, Question, QuizRound, SavedArticleItem } from '@/types';
import { soundManager } from '@/lib/sound';
import { getTodayDateString, isYesterday } from '@/lib/utils';

interface QuizStoreState {
  // Gamification & Profile
  xp: number;
  streak: number;
  lastActiveDate: string;
  completedRounds: number;
  totalCorrect: number;
  totalQuestions: number;
  gems: number;
  savedArticles: SavedArticleItem[];
  lives: number;
  maxLives: number;
  isSoundEnabled: boolean;
  language: 'it' | 'en';

  // Active Quiz Round
  currentRound: QuizRound | null;
  selectedOptionId: string | null;
  feedbackStatus: FeedbackStatus;
  isDrawerOpen: boolean;
  isLoadingRound: boolean;
  loadingLessonNumber: number | null;

  // Duolingo Path Progression
  currentLessonIndex: number;
  completedLessons: number[];
  lessonStars: Record<number, number>;

  // Accessibility & Theme
  isDarkMode: boolean;
  fontSize: 'normal' | 'large' | 'extra';
  reducedMotion: boolean;
  highContrast: boolean;
  isSettingsOpen: boolean;

  // Actions
  startRound: (questions: Question[], lessonNumber?: number) => void;
  selectOption: (optionId: string) => void;
  checkAnswer: () => void;
  nextQuestion: () => void;
  quitQuiz: () => void;
  toggleBookmark: (article: Article) => void;
  isArticleSaved: (pageid: number) => boolean;
  toggleSound: () => void;
  setLanguage: (lang: 'it' | 'en') => void;
  restoreLives: () => void;
  setLoadingRound: (loading: boolean, lessonNumber?: number) => void;
  toggleDarkMode: () => void;
  setFontSize: (size: 'normal' | 'large' | 'extra') => void;
  toggleReducedMotion: () => void;
  toggleHighContrast: () => void;
  openSettings: () => void;
  closeSettings: () => void;
}

export const useQuizStore = create<QuizStoreState>()(
  persist(
    (set, get) => ({
      // Defaults
      xp: 40,
      streak: 1,
      lastActiveDate: getTodayDateString(),
      completedRounds: 0,
      totalCorrect: 0,
      totalQuestions: 0,
      gems: 100,
      savedArticles: [],
      lives: 5,
      maxLives: 5,
      isSoundEnabled: true,
      language: 'it',

      // Duolingo Path defaults
      currentLessonIndex: 1,
      completedLessons: [],
      lessonStars: {},

      // Accessibility & Theme defaults
      isDarkMode: false,
      fontSize: 'normal',
      reducedMotion: false,
      highContrast: false,
      isSettingsOpen: false,
      loadingLessonNumber: null,

      // Session state
      currentRound: null,
      selectedOptionId: null,
      feedbackStatus: 'idle',
      isDrawerOpen: false,
      isLoadingRound: false,

      setLoadingRound: (loading: boolean, lessonNumber?: number) => {
        set({ isLoadingRound: loading, loadingLessonNumber: lessonNumber ?? null });
      },

      startRound: (questions: Question[], lessonNumber?: number) => {
        const newRound: QuizRound = {
          id: `round_${Date.now()}`,
          questions,
          currentIndex: 0,
          answers: [],
          status: 'active',
          score: 0,
          totalXp: 0,
          startedAt: Date.now(),
          lessonNumber,
        };

        set({
          currentRound: newRound,
          selectedOptionId: null,
          feedbackStatus: 'idle',
          isDrawerOpen: false,
          isLoadingRound: false,
        });
      },

      selectOption: (optionId: string) => {
        const { feedbackStatus, isSoundEnabled } = get();
        // Prevent changing option once submitted
        if (feedbackStatus !== 'idle') return;

        if (isSoundEnabled) {
          soundManager.playClick();
        }

        set({ selectedOptionId: optionId });
      },

      checkAnswer: () => {
        const { currentRound, selectedOptionId, isSoundEnabled, lives, xp, totalCorrect, totalQuestions } = get();
        if (!currentRound || !selectedOptionId) return;

        const currentQuestion = currentRound.questions[currentRound.currentIndex];
        if (!currentQuestion) return;

        const isCorrect = selectedOptionId === currentQuestion.correctOptionId;

        if (isCorrect) {
          if (isSoundEnabled) soundManager.playSuccess();
          const earnedXp = 15;
          set({
            feedbackStatus: 'correct',
            isDrawerOpen: true,
            xp: xp + earnedXp,
            totalCorrect: totalCorrect + 1,
            totalQuestions: totalQuestions + 1,
            currentRound: {
              ...currentRound,
              score: currentRound.score + 1,
              totalXp: currentRound.totalXp + earnedXp,
              answers: [
                ...currentRound.answers,
                {
                  questionId: currentQuestion.id,
                  selectedOptionId,
                  isCorrect: true,
                  answeredAt: Date.now(),
                }
              ]
            }
          });
        } else {
          if (isSoundEnabled) soundManager.playError();
          const newLives = Math.max(0, lives - 1);
          set({
            feedbackStatus: 'incorrect',
            isDrawerOpen: true,
            lives: newLives,
            totalQuestions: totalQuestions + 1,
            currentRound: {
              ...currentRound,
              answers: [
                ...currentRound.answers,
                {
                  questionId: currentQuestion.id,
                  selectedOptionId,
                  isCorrect: false,
                  answeredAt: Date.now(),
                }
              ]
            }
          });
        }
      },

      nextQuestion: () => {
        const { currentRound, isSoundEnabled, streak, lastActiveDate, completedRounds, gems, currentLessonIndex, completedLessons, lessonStars } = get();
        if (!currentRound) return;

        const nextIndex = currentRound.currentIndex + 1;

        // Check if round is finished
        if (nextIndex >= currentRound.questions.length) {
          if (isSoundEnabled) soundManager.playVictory();

          // Calculate streak update
          const today = getTodayDateString();
          let newStreak = streak;

          if (lastActiveDate !== today) {
            if (isYesterday(lastActiveDate)) {
              newStreak += 1;
            } else if (lastActiveDate === '') {
              newStreak = 1;
            } else {
              // Streak broken if gap > 1 day
              newStreak = 1;
            }
          }

          // Duolingo Lesson Progression
          let nextLessonIndex = currentLessonIndex;
          const updatedCompleted = [...completedLessons];
          const updatedStars = { ...lessonStars };

          if (currentRound.lessonNumber !== undefined) {
            const lNum = currentRound.lessonNumber;
            if (!updatedCompleted.includes(lNum)) {
              updatedCompleted.push(lNum);
            }
            // 3 stars if perfect (5/5), 2 stars if 4/5, 1 star if >= 3/5
            const starsEarned = currentRound.score >= 5 ? 3 : currentRound.score >= 4 ? 2 : 1;
            updatedStars[lNum] = Math.max(updatedStars[lNum] || 0, starsEarned);

            if (lNum >= nextLessonIndex) {
              nextLessonIndex = lNum + 1;
            }
          }

          set({
            streak: newStreak,
            lastActiveDate: today,
            completedRounds: completedRounds + 1,
            gems: gems + 10,
            currentLessonIndex: nextLessonIndex,
            completedLessons: updatedCompleted,
            lessonStars: updatedStars,
            currentRound: {
              ...currentRound,
              status: 'completed',
              completedAt: Date.now(),
            },
            isDrawerOpen: false,
            feedbackStatus: 'idle',
            selectedOptionId: null,
          });
          return;
        }

        // Advance to next question
        set({
          currentRound: {
            ...currentRound,
            currentIndex: nextIndex,
          },
          selectedOptionId: null,
          feedbackStatus: 'idle',
          isDrawerOpen: false,
        });
      },

      quitQuiz: () => {
        set({
          currentRound: null,
          selectedOptionId: null,
          feedbackStatus: 'idle',
          isDrawerOpen: false,
          isLoadingRound: false,
        });
      },

      toggleBookmark: (article: Article) => {
        const { savedArticles } = get();
        const existingIndex = savedArticles.findIndex(a => a.pageid === article.pageid);

        if (existingIndex >= 0) {
          set({
            savedArticles: savedArticles.filter(a => a.pageid !== article.pageid)
          });
        } else {
          const item: SavedArticleItem = {
            pageid: article.pageid,
            title: article.title,
            description: article.description,
            extract: article.extract,
            thumbnailUrl: article.thumbnail?.source,
            url: article.content_urls.desktop.page,
            savedAt: Date.now(),
            lang: article.lang,
          };
          set({
            savedArticles: [item, ...savedArticles]
          });
        }
      },

      isArticleSaved: (pageid: number) => {
        return get().savedArticles.some(a => a.pageid === pageid);
      },

      toggleSound: () => {
        set((s) => ({ isSoundEnabled: !s.isSoundEnabled }));
      },

      setLanguage: (lang: 'it' | 'en') => {
        set({ language: lang });
      },

      restoreLives: () => {
        set({ lives: 5 });
      },

      toggleDarkMode: () => {
        const next = !get().isDarkMode;
        set({ isDarkMode: next });
        if (typeof document !== 'undefined') {
          document.documentElement.classList.toggle('dark', next);
        }
      },

      setFontSize: (size: 'normal' | 'large' | 'extra') => {
        set({ fontSize: size });
      },

      toggleReducedMotion: () => {
        set((s) => ({ reducedMotion: !s.reducedMotion }));
      },

      toggleHighContrast: () => {
        set((s) => ({ highContrast: !s.highContrast }));
      },

      openSettings: () => set({ isSettingsOpen: true }),
      closeSettings: () => set({ isSettingsOpen: false }),
    }),
    {
      name: 'wikingo-storage-v1',
      storage: createJSONStorage(() => {
        const memoryStorage: Record<string, string> = {};
        const isClient = typeof window !== 'undefined' && window.localStorage;
        return {
          getItem: (key: string) => (isClient ? localStorage.getItem(key) : (memoryStorage[key] ?? null)),
          setItem: (key: string, value: string) => {
            if (isClient) localStorage.setItem(key, value);
            else memoryStorage[key] = value;
          },
          removeItem: (key: string) => {
            if (isClient) localStorage.removeItem(key);
            else delete memoryStorage[key];
          },
        } as StateStorage;
      }),
      onRehydrateStorage: () => (state) => {
        if (state && typeof document !== 'undefined') {
          document.documentElement.classList.toggle('dark', Boolean(state.isDarkMode));
        }
      },
      partialize: (state) => ({
        xp: state.xp,
        streak: state.streak,
        lastActiveDate: state.lastActiveDate,
        completedRounds: state.completedRounds,
        totalCorrect: state.totalCorrect,
        totalQuestions: state.totalQuestions,
        gems: state.gems,
        savedArticles: state.savedArticles,
        isSoundEnabled: state.isSoundEnabled,
        language: state.language,
        currentLessonIndex: state.currentLessonIndex,
        completedLessons: state.completedLessons,
        lessonStars: state.lessonStars,
        isDarkMode: state.isDarkMode,
        fontSize: state.fontSize,
        reducedMotion: state.reducedMotion,
        highContrast: state.highContrast,
      }),
    }
  )
);
