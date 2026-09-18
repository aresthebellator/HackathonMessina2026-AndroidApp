import React from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { X, Play, Trophy, Star, Zap, Lock, Sparkles } from 'lucide-react';
import { Button } from '@/components/ui/Button';
import { Unit } from '@/types';
import { LESSON_TITLES } from '@/lib/unitsData';

interface LessonPreviewModalProps {
  lessonNumber: number | null;
  unit: Unit | null;
  status: 'completed' | 'current' | 'locked';
  stars?: number;
  isLoading: boolean;
  onClose: () => void;
  onStart: (lessonNumber: number) => void;
}

export const LessonPreviewModal: React.FC<LessonPreviewModalProps> = ({
  lessonNumber,
  unit,
  status,
  stars = 0,
  isLoading,
  onClose,
  onStart,
}) => {
  if (!lessonNumber || !unit) return null;

  const title = LESSON_TITLES[lessonNumber] || `Lezione ${lessonNumber}`;
  const isCheckpoint = lessonNumber % 10 === 0;
  const isLocked = status === 'locked';
  const isCompleted = status === 'completed';

  return (
    <AnimatePresence>
      <div className="fixed inset-0 z-50 bg-black/50 backdrop-blur-xs flex items-center justify-center p-4">
        <motion.div
          initial={{ opacity: 0, scale: 0.9, y: 20 }}
          animate={{ opacity: 1, scale: 1, y: 0 }}
          exit={{ opacity: 0, scale: 0.9, y: 20 }}
          className="w-full max-w-sm bg-white dark:bg-[#1E2D34] rounded-3xl border-2 border-[#E5E5E5] dark:border-[#37464F] p-6 text-center space-y-5 shadow-2xl overflow-hidden relative"
        >
          {/* Close button */}
          <button
            onClick={onClose}
            className="absolute top-4 right-4 p-1.5 rounded-xl text-[#AFAFAF] hover:text-[#3C3C3C] dark:hover:text-white hover:bg-black/5 dark:hover:bg-white/5 transition-colors"
          >
            <X className="w-5 h-5" />
          </button>

          {/* Unit Tag */}
          <div className="pt-2">
            <span
              className="text-[11px] font-black uppercase tracking-wider px-3 py-1 rounded-xl text-white inline-block"
              style={{ backgroundColor: unit.theme.primary }}
            >
              {unit.title} • {unit.topic}
            </span>
          </div>

          {/* Lesson Icon badge */}
          <div className="flex justify-center">
            <div
              className="w-20 h-20 rounded-3xl flex items-center justify-center border-4 shadow-sm"
              style={{
                backgroundColor: isLocked ? '#F7F7F7' : unit.theme.light,
                borderColor: isLocked ? '#E5E5E5' : unit.theme.primary,
              }}
            >
              {isCheckpoint ? (
                <Trophy
                  className="w-10 h-10 stroke-[2.2]"
                  style={{ color: isLocked ? '#AFAFAF' : unit.theme.primary }}
                />
              ) : isLocked ? (
                <Lock className="w-8 h-8 text-[#AFAFAF]" />
              ) : (
                <Play
                  className="w-8 h-8 ml-1 fill-current"
                  style={{ color: unit.theme.primary }}
                />
              )}
            </div>
          </div>

          {/* Title & Description */}
          <div className="space-y-1">
            <h3 className="text-xl font-black text-[#3C3C3C] dark:text-white leading-snug">
              {title}
            </h3>
            <p className="text-xs font-bold text-[#777777] dark:text-[#93A5AF]">
              {isCheckpoint
                ? 'Sfida finale della sezione: dimostra la tua padronanza!'
                : '5 quesiti dinamici estratti in tempo reale da Wikipedia'}
            </p>
          </div>

          {/* Stars (if completed) */}
          {isCompleted && (
            <div className="flex justify-center items-center gap-1 py-1">
              {[1, 2, 3].map((starIdx) => (
                <Star
                  key={starIdx}
                  className={`w-6 h-6 ${
                    starIdx <= stars
                      ? 'text-[#FFC800] fill-[#FFC800]'
                      : 'text-gray-300 fill-gray-200'
                  }`}
                />
              ))}
            </div>
          )}

          {/* Rewards preview */}
          <div className="grid grid-cols-2 gap-2 p-3 rounded-2xl bg-[#F7F7F7] dark:bg-[#131F24] border border-[#E5E5E5] dark:border-[#37464F] text-xs font-black">
            <div className="flex items-center justify-center gap-1 text-[#1CB0F6]">
              <Zap className="w-4 h-4 fill-[#1CB0F6]" />
              <span>+{isCheckpoint ? '150' : '90'} XP</span>
            </div>
            <div className="flex items-center justify-center gap-1 text-[#FF9600]">
              <Sparkles className="w-4 h-4 fill-[#FF9600]" />
              <span>+{isCheckpoint ? '25' : '10'} Gemme</span>
            </div>
          </div>

          {/* Action Button */}
          <div className="pt-2">
            {isLocked ? (
              <Button variant="outline" size="lg" fullWidth disabled>
                Lezione Bloccata
              </Button>
            ) : (
              <Button
                variant="green"
                size="lg"
                fullWidth
                onClick={() => onStart(lessonNumber)}
                disabled={isLoading}
                className="flex items-center justify-center gap-2"
              >
                {isLoading ? (
                  <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin" />
                ) : (
                  <>
                    <Play className="w-5 h-5 fill-white" />
                    <span>{isCompleted ? 'Ripassa (+50 XP)' : 'Inizia'}</span>
                  </>
                )}
              </Button>
            )}
          </div>
        </motion.div>
      </div>
    </AnimatePresence>
  );
};
