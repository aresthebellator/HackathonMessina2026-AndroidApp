import React from 'react';
import { motion } from 'framer-motion';

interface ProgressBarProps {
  current: number;
  total: number;
}

export const ProgressBar: React.FC<ProgressBarProps> = ({ current, total }) => {
  const percentage = Math.min(100, Math.max(0, (current / total) * 100));

  return (
    <div className="relative w-full h-4 bg-[#E5E5E5] rounded-full overflow-hidden flex-1 shadow-inner">
      <motion.div
        className="relative h-full bg-[#58CC02] rounded-full"
        initial={{ width: 0 }}
        animate={{ width: `${percentage}%` }}
        transition={{ type: 'spring', stiffness: 260, damping: 25 }}
      >
        {/* Gloss highlight reflection */}
        <div className="absolute top-0.5 left-2 right-2 h-1 bg-white/40 rounded-full" />
      </motion.div>
    </div>
  );
};
