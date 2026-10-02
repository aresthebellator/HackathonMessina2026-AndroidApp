import React from 'react';
import { motion } from 'framer-motion';

interface MascotReactionProps {
  message: string;
  className?: string;
}

/**
 * Friendly Viking Mascot Companion with Comic Speech Bubble.
 * Ported from android-app/core/designsystem/components/MascotReaction.kt
 */
export const MascotReaction: React.FC<MascotReactionProps> = ({ message, className = '' }) => {
  return (
    <motion.div
      initial={{ opacity: 0, y: -6 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.25 }}
      className={`flex items-center gap-3 w-full py-1 ${className}`}
    >
      {/* Viking Avatar */}
      <div className="relative shrink-0">
        <img
          src="/ic_launcher_viking.png"
          alt="Wikingo Viking Mascot"
          className="w-14 h-14 sm:w-16 sm:h-16 object-contain select-none drop-shadow-sm transition-transform hover:scale-105"
          onError={(e) => {
            // Fallback avatar if image isn't loaded
            const target = e.currentTarget;
            target.style.display = 'none';
            if (target.nextElementSibling) {
              (target.nextElementSibling as HTMLElement).style.display = 'flex';
            }
          }}
        />
        <div className="hidden w-14 h-14 rounded-2xl bg-[#58CC02] border-b-4 border-[#46A302] items-center justify-center text-white text-2xl font-black">
          🛡️
        </div>
      </div>

      {/* Comic Speech Bubble */}
      <div className="relative flex-1 bg-white dark:bg-[#1E2D34] rounded-2xl border-2 border-b-4 border-[#E5E5E5] dark:border-[#37464F] px-4 py-2.5 shadow-xs">
        {/* Left pointing arrow tip */}
        <div className="absolute top-1/2 -left-2 -translate-y-1/2 w-0 h-0 border-y-[6px] border-y-transparent border-r-[8px] border-r-white dark:border-r-[#1E2D34] z-10" />
        <div className="absolute top-1/2 -left-[10px] -translate-y-1/2 w-0 h-0 border-y-[7px] border-y-transparent border-r-[9px] border-r-[#E5E5E5] dark:border-r-[#37464F]" />

        <p className="text-xs sm:text-sm font-extrabold text-[#3C3C3C] dark:text-[#E5E7EB] leading-snug">
          {message}
        </p>
      </div>
    </motion.div>
  );
};
