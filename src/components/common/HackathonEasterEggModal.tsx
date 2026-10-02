import React from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { useQuizStore } from '@/store/useQuizStore';

/**
 * Hackathon Messina 2026 Easter Egg Screen.
 * Ported from android-app/presentation/dashboard/DashboardScreen.kt (HackathonScreen)
 * Triggered by 10 taps on the Wikingo brand mascot logo.
 */
export const HackathonEasterEggModal: React.FC = () => {
  const isEasterEggOpen = useQuizStore((s) => s.isEasterEggOpen);
  const closeEasterEgg = useQuizStore((s) => s.closeEasterEgg);

  if (!isEasterEggOpen) return null;

  return (
    <AnimatePresence>
      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        exit={{ opacity: 0 }}
        onClick={closeEasterEgg}
        className="fixed inset-0 z-50 bg-[#F7F7F7]/98 dark:bg-[#131F24]/98 backdrop-blur-md flex flex-col items-center justify-center p-6 cursor-pointer select-none"
      >
        <motion.div
          initial={{ scale: 0.8, y: 20 }}
          animate={{ scale: 1, y: 0 }}
          exit={{ scale: 0.8, y: 20 }}
          transition={{ type: 'spring', damping: 20, stiffness: 300 }}
          className="max-w-md w-full flex flex-col items-center text-center space-y-5"
        >
          {/* Viking Logo */}
          <div className="relative">
            <div className="absolute -inset-4 rounded-full bg-[#58CC02]/20 blur-2xl animate-pulse" />
            <img
              src="/ic_launcher_viking.png"
              alt="Vichingo Wikingo"
              className="w-44 h-44 object-contain relative drop-shadow-xl animate-bounce"
            />
          </div>

          <div className="space-y-2">
            <h1 className="text-4xl font-black text-[#3C3C3C] dark:text-white tracking-tight">
              Wikingo
            </h1>
            <h2 className="text-2xl font-black text-[#58CC02]">
              Hackaton Messina 2026
            </h2>
            <div className="inline-block px-3 py-1 rounded-full bg-[#D7FFB8] dark:bg-[#14532D] text-[#2A7000] dark:text-[#86EFAC] text-xs font-black uppercase tracking-wider">
              Wikimedia Foundation • Micro-learning
            </div>
          </div>

          <p className="text-sm font-bold text-[#777777] dark:text-[#9CA3AF] max-w-sm leading-relaxed">
            Un piccolo easter egg per chi esplora ogni angolo di Wikingo. Creato con passione durante l'Hackathon di Messina per portare la conoscenza libera a portata di quiz.
          </p>

          <div className="pt-6">
            <span className="text-xs font-black uppercase tracking-wider text-[#AFAFAF] dark:text-[#64748B] border border-[#E5E5E5] dark:border-[#37464F] px-4 py-2 rounded-xl bg-white dark:bg-[#1E2D34] shadow-xs">
              Tocca ovunque per tornare indietro
            </span>
          </div>
        </motion.div>
      </motion.div>
    </AnimatePresence>
  );
};
