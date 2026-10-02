import React from 'react';
import { X, Volume2, VolumeX, Settings } from 'lucide-react';
import { ProgressBar } from '@/components/ui/ProgressBar';
import { HeartLives } from '@/components/ui/HeartLives';
import { StreakBadge } from '@/components/ui/StreakBadge';
import { useQuizStore } from '@/store/useQuizStore';
import { useTranslation } from '@/lib/i18n';

interface TopHeaderProps {
  onQuit: () => void;
}

export const TopHeader: React.FC<TopHeaderProps> = ({ onQuit }) => {
  const currentRound = useQuizStore((s) => s.currentRound);
  const lives = useQuizStore((s) => s.lives);
  const streak = useQuizStore((s) => s.streak);
  const isSoundEnabled = useQuizStore((s) => s.isSoundEnabled);
  const toggleSound = useQuizStore((s) => s.toggleSound);
  const openSettings = useQuizStore((s) => s.openSettings);
  const { t } = useTranslation();

  if (!currentRound) return null;

  return (
    <header className="w-full max-w-2xl mx-auto px-4 py-3 flex items-center gap-4">
      {/* Exit Button */}
      <button
        onClick={onQuit}
        className="text-[#AFAFAF] hover:text-[#3C3C3C] dark:hover:text-white p-2 rounded-xl hover:bg-black/5 dark:hover:bg-white/5 transition-all focus:outline-none focus-visible:ring-2 focus-visible:ring-gray-300"
        aria-label={t('common.close')}
      >
        <X className="w-6 h-6 stroke-[2.5]" />
      </button>

      {/* Progress Bar */}
      <ProgressBar
        current={currentRound.currentIndex}
        total={currentRound.questions.length}
      />

      {/* Stats pills */}
      <div className="flex items-center gap-2">
        <StreakBadge streak={streak} />
        <HeartLives lives={lives} />
        
        {/* Sound toggle */}
        <button
          onClick={toggleSound}
          className="p-2 text-[#AFAFAF] hover:text-[#3C3C3C] dark:hover:text-white rounded-xl hover:bg-black/5 dark:hover:bg-white/5 transition-all focus:outline-none"
          title={isSoundEnabled ? `${t('common.sound')}: On` : `${t('common.sound')}: Off`}
          aria-label={t('common.sound')}
        >
          {isSoundEnabled ? (
            <Volume2 className="w-5 h-5" />
          ) : (
            <VolumeX className="w-5 h-5 text-[#FF4B4B]" />
          )}
        </button>

        {/* Settings button */}
        <button
          onClick={openSettings}
          className="p-2 text-[#AFAFAF] hover:text-[#3C3C3C] dark:hover:text-white rounded-xl hover:bg-black/5 dark:hover:bg-white/5 transition-all focus:outline-none"
          title={t('dashboard.settings_tooltip')}
        >
          <Settings className="w-5 h-5" />
        </button>
      </div>
    </header>
  );
};
