import React from 'react';
import { motion } from 'framer-motion';
import { Heart } from 'lucide-react';

interface HeartLivesProps {
  lives: number;
}

export const HeartLives: React.FC<HeartLivesProps> = ({ lives }) => {
  return (
    <div className="flex items-center gap-1.5 font-extrabold text-[#FF4B4B] bg-[#FFF1F2] px-3 py-1.5 rounded-2xl border-2 border-[#FFDFE0]">
      <motion.div
        key={lives}
        animate={{ scale: [1, 1.35, 1] }}
        transition={{ duration: 0.3 }}
      >
        <Heart className="w-5 h-5 fill-[#FF4B4B] text-[#FF4B4B]" />
      </motion.div>
      <span className="text-sm tracking-wide">{lives}</span>
    </div>
  );
};
