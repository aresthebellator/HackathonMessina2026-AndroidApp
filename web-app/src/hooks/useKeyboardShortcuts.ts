import { useEffect } from 'react';
import { useQuizStore } from '@/store/useQuizStore';

export function useKeyboardShortcuts() {
  const currentRound = useQuizStore((s) => s.currentRound);
  const selectedOptionId = useQuizStore((s) => s.selectedOptionId);
  const feedbackStatus = useQuizStore((s) => s.feedbackStatus);
  const selectOption = useQuizStore((s) => s.selectOption);
  const checkAnswer = useQuizStore((s) => s.checkAnswer);
  const nextQuestion = useQuizStore((s) => s.nextQuestion);

  useEffect(() => {
    if (!currentRound || currentRound.status !== 'active') return;

    const currentQuestion = currentRound.questions[currentRound.currentIndex];
    if (!currentQuestion) return;

    const handleKeyDown = (e: KeyboardEvent) => {
      // Don't trigger if user is typing in an input
      if (['INPUT', 'TEXTAREA'].includes((e.target as HTMLElement)?.tagName)) return;

      // Number keys 1-4 for options
      if (feedbackStatus === 'idle') {
        const keyNum = parseInt(e.key, 10);
        if (keyNum >= 1 && keyNum <= currentQuestion.options.length) {
          e.preventDefault();
          selectOption(currentQuestion.options[keyNum - 1].id);
        }
      }

      // Enter key for Check or Continue
      if (e.key === 'Enter') {
        e.preventDefault();
        if (feedbackStatus === 'idle' && selectedOptionId) {
          checkAnswer();
        } else if (feedbackStatus !== 'idle') {
          nextQuestion();
        }
      }
    };

    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [currentRound, feedbackStatus, selectedOptionId, selectOption, checkAnswer, nextQuestion]);
}
