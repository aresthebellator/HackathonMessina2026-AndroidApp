import React from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { BookOpen, Sparkles, X } from 'lucide-react';
import { useQuizStore } from '@/store/useQuizStore';
import { useTranslation } from '@/lib/i18n';
import { Button } from '@/components/ui/Button';

/**
 * Onboarding Welcome Modal ported from android-app/presentation/onboarding/WelcomeScreen.kt
 */
export const WelcomeModal: React.FC = () => {
  const isWelcomeOpen = useQuizStore((s) => s.isWelcomeOpen);
  const closeWelcome = useQuizStore((s) => s.closeWelcome);
  const { t } = useTranslation();

  if (!isWelcomeOpen) return null;

  return (
    <AnimatePresence>
      <div className="fixed inset-0 z-50 bg-black/60 backdrop-blur-xs flex items-center justify-center p-4">
        <motion.div
          initial={{ opacity: 0, scale: 0.9, y: 15 }}
          animate={{ opacity: 1, scale: 1, y: 0 }}
          exit={{ opacity: 0, scale: 0.9, y: 15 }}
          className="w-full max-w-sm bg-white dark:bg-[#1E2D34] rounded-3xl border-2 border-[#E5E5E5] dark:border-[#37464F] p-6 text-center space-y-6 shadow-2xl relative"
        >
          {/* Close button */}
          <button
            onClick={closeWelcome}
            className="absolute top-4 right-4 p-1.5 rounded-xl text-[#AFAFAF] hover:text-[#3C3C3C] dark:hover:text-white hover:bg-black/5 dark:hover:bg-white/5 transition-colors"
            aria-label={t('common.close')}
          >
            <X className="w-5 h-5" />
          </button>

          {/* Viking Mascot */}
          <div className="flex justify-center pt-2">
            <div className="relative">
              <div className="absolute -inset-2 rounded-full bg-[#58CC02]/20 blur-xl animate-pulse" />
              <img
                src="/ic_launcher_viking.png"
                alt="Wikingo Mascot"
                className="w-28 h-28 object-contain relative drop-shadow-md select-none"
              />
            </div>
          </div>

          {/* Header */}
          <div className="space-y-1.5">
            <h2 className="text-2xl font-black text-[#3C3C3C] dark:text-white">
              {t('welcome.title')}
            </h2>
            <p className="text-xs sm:text-sm font-bold text-[#777777] dark:text-[#9CA3AF]">
              {t('welcome.subtitle')}
            </p>
          </div>

          {/* Benefits Box */}
          <div className="p-4 rounded-2xl bg-[#F7F7F7] dark:bg-[#131F24] border-2 border-[#E5E5E5] dark:border-[#37464F] space-y-3 text-left">
            <div className="flex items-center gap-3">
              <div className="w-9 h-9 rounded-xl bg-[#E0F2FE] dark:bg-[#0369A1]/30 flex items-center justify-center text-[#0284C7] dark:text-[#38BDF8] shrink-0">
                <BookOpen className="w-5 h-5" />
              </div>
              <span className="text-xs font-bold text-[#3C3C3C] dark:text-white">
                {t('welcome.benefit_lessons')}
              </span>
            </div>

            <div className="flex items-center gap-3">
              <div className="w-9 h-9 rounded-xl bg-[#FEF3C7] dark:bg-[#B45309]/30 flex items-center justify-center text-[#D97706] dark:text-[#FCD34D] shrink-0">
                <Sparkles className="w-5 h-5" />
              </div>
              <span className="text-xs font-bold text-[#3C3C3C] dark:text-white">
                {t('welcome.benefit_wikipedia')}
              </span>
            </div>
          </div>

          {/* CTA */}
          <Button
            variant="green"
            size="lg"
            fullWidth
            onClick={closeWelcome}
          >
            {t('welcome.start')}
          </Button>
        </motion.div>
      </div>
    </AnimatePresence>
  );
};
