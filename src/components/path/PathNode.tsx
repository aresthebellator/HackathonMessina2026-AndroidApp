import React from 'react';
import { motion } from 'framer-motion';
import { Check, Lock, Star, Trophy, Sparkles, Play } from 'lucide-react';
import { Unit } from '@/types';
import { soundManager } from '@/lib/sound';
import { useQuizStore } from '@/store/useQuizStore';

interface PathNodeProps {
  lessonNumber: number;
  status: 'completed' | 'current' | 'locked';
  stars?: number;
  isCheckpoint?: boolean;
  offsetPx: number;
  unit: Unit;
  onClick: () => void;
}

export const PathNode: React.FC<PathNodeProps> = ({
  lessonNumber,
  status,
  stars = 0,
  isCheckpoint = false,
  offsetPx,
  unit,
  onClick,
}) => {
  const isDarkMode = useQuizStore((s) => s.isDarkMode);
  const isCurrent = status === 'current';
  const isCompleted = status === 'completed';
  const isLocked = status === 'locked';

  const handleClick = () => {
    soundManager.playClick();
    onClick();
  };

  const nodeBg = isLocked
    ? isDarkMode
      ? '#26343C'
      : '#E5E5E5'
    : unit.theme.primary;

  const nodeBorderBottom = isLocked
    ? isDarkMode
      ? '#37464F'
      : '#CECECE'
    : unit.theme.dark;

  return (
    <div
      className="relative flex flex-col items-center my-3 transition-transform duration-300"
      style={{
        transform: `translateX(${offsetPx}px)`,
      }}
    >
      {/* Floating "INIZIA" Speech Bubble for the Active/Current Node */}
      {isCurrent && (
        <motion.div
          initial={{ y: -8, opacity: 0 }}
          animate={{ y: [0, -6, 0], opacity: 1 }}
          transition={{
            y: { duration: 1.8, repeat: Infinity, ease: 'easeInOut' },
            opacity: { duration: 0.3 },
          }}
          className="absolute -top-11 z-20 pointer-events-none"
        >
          <div
            className="px-3 py-1 rounded-xl text-white font-black text-xs uppercase tracking-wider shadow-md flex items-center gap-1"
            style={{ backgroundColor: unit.theme.primary }}
          >
            <Sparkles className="w-3.5 h-3.5" />
            <span>Inizia</span>
          </div>
          {/* Triangle pointer arrow */}
          <div
            className="w-0 h-0 border-x-6 border-x-transparent border-t-6 mx-auto"
            style={{ borderTopColor: unit.theme.primary }}
          />
        </motion.div>
      )}

      {/* Pulsing ring for current node */}
      {isCurrent && (
        <div
          className="absolute -inset-2 rounded-full opacity-30 animate-ping pointer-events-none"
          style={{ backgroundColor: unit.theme.primary }}
        />
      )}

      {/* Main Circular Tactile Node Button */}
      <button
        onClick={handleClick}
        className={`relative w-[68px] h-[68px] sm:w-[74px] sm:h-[74px] rounded-full flex items-center justify-center transition-all cursor-pointer select-none focus:outline-none focus-visible:ring-4 ${
          isCurrent
            ? 'active:translate-y-[4px] active:border-b-2 shadow-lg ring-4 ring-white dark:ring-[#37464F]'
            : isCompleted
            ? 'active:translate-y-[3px] active:border-b-2 hover:brightness-105'
            : 'cursor-default opacity-85'
        }`}
        style={{
          backgroundColor: nodeBg,
          borderBottomWidth: isLocked ? '5px' : '7px',
          borderBottomColor: nodeBorderBottom,
        }}
        title={`Lezione ${lessonNumber}`}
      >
        {/* Inner Icon */}
        {isCheckpoint ? (
          <Trophy
            className={`w-7 h-7 sm:w-8 sm:h-8 stroke-[2.2] ${
              isLocked ? 'text-[#AFAFAF] dark:text-[#64748B]' : 'text-white fill-white/20'
            }`}
          />
        ) : isCompleted ? (
          <Check className="w-8 h-8 text-white stroke-[3.5]" />
        ) : isCurrent ? (
          <Play className="w-7 h-7 text-white fill-white ml-0.5" />
        ) : (
          <Lock className="w-6 h-6 text-[#AFAFAF] dark:text-[#64748B] stroke-[2.5]" />
        )}

        {/* Number badge on the bottom-right */}
        <span
          className={`absolute -bottom-1 -right-1 w-6 h-6 rounded-full border-2 text-[11px] font-black flex items-center justify-center shadow-xs ${
            isLocked
              ? 'bg-[#AFAFAF] dark:bg-[#37464F] text-white dark:text-[#93A5AF] border-white dark:border-[#1E2D34]'
              : 'bg-white dark:bg-[#1E2D34] text-[#3C3C3C] dark:text-white border-white dark:border-[#37464F]'
          }`}
        >
          {lessonNumber}
        </span>
      </button>

      {/* Stars indicator under completed node */}
      {isCompleted && stars > 0 && (
        <div className="flex items-center gap-0.5 mt-1.5">
          {[1, 2, 3].map((starIdx) => (
            <Star
              key={starIdx}
              className={`w-3.5 h-3.5 ${
                starIdx <= stars
                  ? 'text-[#FFC800] fill-[#FFC800]'
                  : 'text-gray-300 fill-gray-200'
              }`}
            />
          ))}
        </div>
      )}
    </div>
  );
};
