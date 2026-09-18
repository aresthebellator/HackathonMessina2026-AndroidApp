import React from 'react';
import { motion } from 'framer-motion';
import { CheckCircle2, XCircle, ExternalLink, Sparkles, BookOpen } from 'lucide-react';
import { Button } from '@/components/ui/Button';
import { useQuizStore } from '@/store/useQuizStore';
import { cn } from '@/lib/utils';

export const BottomDrawer: React.FC = () => {
  const currentRound = useQuizStore((s) => s.currentRound);
  const selectedOptionId = useQuizStore((s) => s.selectedOptionId);
  const feedbackStatus = useQuizStore((s) => s.feedbackStatus);
  const checkAnswer = useQuizStore((s) => s.checkAnswer);
  const nextQuestion = useQuizStore((s) => s.nextQuestion);

  if (!currentRound) return null;

  const currentQuestion = currentRound.questions[currentRound.currentIndex];
  if (!currentQuestion) return null;

  const isCorrect = feedbackStatus === 'correct';
  const correctOption = currentQuestion.options.find(
    (o) => o.id === currentQuestion.correctOptionId
  );

  return (
    <div
      className={cn(
        'fixed bottom-0 left-0 right-0 z-40 border-t-2 transition-colors duration-200',
        feedbackStatus === 'idle' && 'bg-white dark:bg-[#1E2D34] border-[#E5E5E5] dark:border-[#37464F] py-4',
        feedbackStatus === 'correct' && 'bg-[#D7FFB8] dark:bg-[#064E3B] border-[#B0EC77] dark:border-[#047857] py-6 sm:py-7',
        feedbackStatus === 'incorrect' && 'bg-[#FFDFE0] dark:bg-[#4C0519] border-[#FFC1C1] dark:border-[#881337] py-6 sm:py-7'
      )}
    >
      <div className="w-full max-w-2xl mx-auto px-4 flex flex-col sm:flex-row items-center justify-between gap-4">
        {/* Status & Feedback information */}
        <div className="w-full sm:w-auto flex-1">
          {feedbackStatus === 'idle' ? (
            <div className="hidden sm:block text-xs font-bold text-[#AFAFAF] dark:text-[#9CA3AF]">
              Seleziona un'opzione o premi i tasti 1-4
            </div>
          ) : (
            <motion.div
              initial={{ opacity: 0, y: 15 }}
              animate={{ opacity: 1, y: 0 }}
              className="space-y-2"
            >
              {/* Correct Feedback Header */}
              {isCorrect && (
                <div className="flex items-center gap-3">
                  <div className="w-9 h-9 rounded-full bg-white dark:bg-[#047857] flex items-center justify-center shrink-0 shadow-sm">
                    <CheckCircle2 className="w-7 h-7 text-[#58CC02] dark:text-[#86EFAC]" />
                  </div>
                  <div>
                    <div className="flex items-center gap-2">
                      <h3 className="text-xl font-black text-[#2A7000] dark:text-[#86EFAC]">
                        Ottimo lavoro!
                      </h3>
                      <span className="flex items-center gap-1 text-xs font-black text-[#58CC02] bg-white dark:bg-[#047857] px-2 py-0.5 rounded-lg border border-[#B0EC77] dark:border-[#059669]">
                        <Sparkles className="w-3 h-3" /> +15 XP
                      </span>
                    </div>
                    <p className="text-xs font-bold text-[#3B8200] dark:text-[#A7F3D0] mt-0.5">
                      Risposta esatta!
                    </p>
                  </div>
                </div>
              )}

              {/* Incorrect Feedback Header & Context Box */}
              {!isCorrect && (
                <div className="space-y-3">
                  <div className="flex items-center gap-3">
                    <div className="w-9 h-9 rounded-full bg-white dark:bg-[#881337] flex items-center justify-center shrink-0 shadow-sm">
                      <XCircle className="w-7 h-7 text-[#FF4B4B] dark:text-[#FDA4AF]" />
                    </div>
                    <div>
                      <h3 className="text-lg font-black text-[#B91C1C] dark:text-[#FDA4AF]">
                        Risposta corretta:
                      </h3>
                      <p className="text-sm font-black text-[#FF4B4B] dark:text-[#FECDD3]">
                        {correctOption?.text}
                      </p>
                    </div>
                  </div>

                  {/* Contextual Wikipedia Explanation Box */}
                  <div className="bg-white/95 dark:bg-[#1E2D34]/95 backdrop-blur-sm p-3.5 rounded-2xl border-2 border-[#FFC1C1] dark:border-[#881337] text-xs sm:text-sm text-[#4B4B4B] dark:text-[#E5E7EB] space-y-1.5 shadow-sm">
                    <div className="flex items-center justify-between text-[#B91C1C] dark:text-[#FDA4AF] font-extrabold text-xs">
                      <span className="flex items-center gap-1">
                        <BookOpen className="w-3.5 h-3.5" /> Approfondimento su "{currentQuestion.article.title}"
                      </span>
                      <a
                        href={currentQuestion.sourceUrl}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="flex items-center gap-1 text-[11px] underline hover:text-[#7F1D1D] dark:hover:text-white"
                      >
                        Leggi su Wikipedia <ExternalLink className="w-3 h-3" />
                      </a>
                    </div>
                    <p className="leading-relaxed text-[#3C3C3C] dark:text-[#F3F4F6]">
                      {currentQuestion.explanation}
                    </p>
                  </div>
                </div>
              )}
            </motion.div>
          )}
        </div>

        {/* Action Button */}
        <div className="w-full sm:w-auto shrink-0">
          {feedbackStatus === 'idle' ? (
            <Button
              variant="green"
              size="lg"
              fullWidth
              disabled={!selectedOptionId}
              onClick={checkAnswer}
              className="sm:w-44"
            >
              Verifica
            </Button>
          ) : (
            <Button
              variant={isCorrect ? 'green' : 'coral'}
              size="lg"
              fullWidth
              onClick={nextQuestion}
              className="sm:w-44"
            >
              Continua
            </Button>
          )}
        </div>
      </div>
    </div>
  );
};
