import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { TopHeader } from './TopHeader';
import { QuestionCard } from './QuestionCard';
import { BottomDrawer } from './BottomDrawer';
import { RoundComplete } from './RoundComplete';
import { useQuizStore } from '@/store/useQuizStore';
import { useWikipediaQuiz } from '@/hooks/useWikipediaQuiz';
import { useKeyboardShortcuts } from '@/hooks/useKeyboardShortcuts';
import { useTranslation } from '@/lib/i18n';
import { Button } from '@/components/ui/Button';
import { Heart, RefreshCw, AlertCircle } from 'lucide-react';

export const QuizInteractive: React.FC = () => {
  const currentRound = useQuizStore((s) => s.currentRound);
  const lives = useQuizStore((s) => s.lives);
  const restoreLives = useQuizStore((s) => s.restoreLives);
  const quitQuiz = useQuizStore((s) => s.quitQuiz);
  const { loadNewRound, loadLesson } = useWikipediaQuiz();
  const { t } = useTranslation();
  const [showExitDialog, setShowExitDialog] = useState(false);

  // Enable fast keyboard shortcuts (1-4 and Enter)
  useKeyboardShortcuts();

  if (!currentRound) return null;

  // Round completed state
  if (currentRound.status === 'completed') {
    return (
      <div className="min-h-screen bg-[#F7F7F7] dark:bg-[#131F24] flex flex-col justify-center py-6">
        <RoundComplete
          onPlayAgain={() => {
            if (currentRound.lessonNumber) {
              loadLesson(currentRound.lessonNumber + 1, 5);
            } else {
              loadNewRound(5);
            }
          }}
          onGoHome={quitQuiz}
        />
      </div>
    );
  }

  // Game Over (out of lives)
  if (lives <= 0) {
    return (
      <div className="min-h-screen bg-[#F7F7F7] dark:bg-[#131F24] flex items-center justify-center p-4">
        <motion.div
          initial={{ opacity: 0, scale: 0.95 }}
          animate={{ opacity: 1, scale: 1 }}
          className="w-full max-w-md bg-white dark:bg-[#1E2D34] rounded-3xl border-2 border-[#E5E5E5] dark:border-[#37464F] p-6 text-center space-y-6 shadow-md"
        >
          <div className="w-20 h-20 mx-auto rounded-3xl bg-[#FFF1F2] dark:bg-[#4C0519]/40 border-4 border-[#FF4B4B] flex items-center justify-center animate-pulse">
            <Heart className="w-10 h-10 text-[#FF4B4B] fill-[#FF4B4B]" />
          </div>

          <div className="space-y-2">
            <h2 className="text-2xl font-black text-[#3C3C3C] dark:text-white">
              {t('quiz.game_over_title')}
            </h2>
            <p className="text-sm font-bold text-[#777777] dark:text-[#9CA3AF]">
              {t('quiz.game_over_subtitle')}
            </p>
          </div>

          <div className="space-y-3">
            <Button
              variant="coral"
              size="lg"
              fullWidth
              onClick={restoreLives}
              className="flex items-center justify-center gap-2"
            >
              <RefreshCw className="w-5 h-5" /> {t('quiz.recharge_lives')}
            </Button>
            <Button
              variant="outline"
              size="md"
              fullWidth
              onClick={quitQuiz}
              className="dark:bg-[#1E2D34] dark:border-[#37464F] dark:text-white"
            >
              {t('quiz.return_home')}
            </Button>
          </div>
        </motion.div>
      </div>
    );
  }

  const currentQuestion = currentRound.questions[currentRound.currentIndex];

  return (
    <div className="min-h-screen bg-[#F7F7F7] dark:bg-[#131F24] flex flex-col justify-between pb-32">
      {/* Top Header with Progress and Status */}
      <TopHeader onQuit={() => setShowExitDialog(true)} />

      {/* Main Animated Question Container */}
      <main className="flex-1 flex flex-col justify-center py-4">
        <AnimatePresence mode="wait">
          {currentQuestion && (
            <motion.div
              key={currentQuestion.id}
              initial={{ opacity: 0, x: 25 }}
              animate={{ opacity: 1, x: 0 }}
              exit={{ opacity: 0, x: -25 }}
              transition={{ duration: 0.25, ease: 'easeInOut' }}
              className="w-full"
            >
              <QuestionCard question={currentQuestion} />
            </motion.div>
          )}
        </AnimatePresence>
      </main>

      {/* Persistent Bottom Feedback & Action Drawer */}
      <BottomDrawer />

      {/* Exit Confirmation Modal */}
      <AnimatePresence>
        {showExitDialog && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            className="fixed inset-0 z-50 bg-black/50 backdrop-blur-xs flex items-center justify-center p-4"
          >
            <motion.div
              initial={{ scale: 0.9, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              exit={{ scale: 0.9, opacity: 0 }}
              className="w-full max-w-sm bg-white dark:bg-[#1E2D34] rounded-3xl border-2 border-[#E5E5E5] dark:border-[#37464F] p-6 text-center space-y-5 shadow-2xl"
            >
              <div className="w-14 h-14 mx-auto rounded-2xl bg-[#FFF7ED] dark:bg-[#78350F]/40 border-2 border-[#FED7AA] dark:border-[#92400E] flex items-center justify-center text-[#FF9600]">
                <AlertCircle className="w-8 h-8" />
              </div>
              <div className="space-y-1">
                <h3 className="text-xl font-black text-[#3C3C3C] dark:text-white">
                  {t('quiz.exit_title')}
                </h3>
                <p className="text-xs font-bold text-[#777777] dark:text-[#9CA3AF]">
                  {t('quiz.exit_message')}
                </p>
              </div>
              <div className="space-y-2 pt-2">
                <Button
                  variant="coral"
                  size="md"
                  fullWidth
                  onClick={quitQuiz}
                >
                  {t('quiz.exit_confirm')}
                </Button>
                <Button
                  variant="outline"
                  size="md"
                  fullWidth
                  onClick={() => setShowExitDialog(false)}
                  className="dark:bg-[#1E2D34] dark:border-[#37464F] dark:text-white"
                >
                  {t('quiz.exit_cancel')}
                </Button>
              </div>
            </motion.div>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
};
