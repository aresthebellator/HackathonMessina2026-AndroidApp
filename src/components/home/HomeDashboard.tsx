import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { Zap, Bookmark, Globe, WifiOff, Shuffle, Play, Settings, History, User } from 'lucide-react';
import { HeartLives } from '@/components/ui/HeartLives';
import { StreakBadge } from '@/components/ui/StreakBadge';
import { SavedArticlesModal } from './SavedArticlesModal';
import { DuolingoPath } from '@/components/path/DuolingoPath';
import { useQuizStore } from '@/store/useQuizStore';
import { useWikipediaQuiz } from '@/hooks/useWikipediaQuiz';
import { getUnitForLesson, getLessonTitle, getUnitLocalizedTitle, getUnitLocalizedTopic } from '@/lib/unitsData';
import { useTranslation } from '@/lib/i18n';

export const HomeDashboard: React.FC = () => {
  const xp = useQuizStore((s) => s.xp);
  const streak = useQuizStore((s) => s.streak);
  const lives = useQuizStore((s) => s.lives);
  const language = useQuizStore((s) => s.language);
  const setLanguage = useQuizStore((s) => s.setLanguage);
  const savedArticles = useQuizStore((s) => s.savedArticles);
  const currentLessonIndex = useQuizStore((s) => s.currentLessonIndex);
  const completedLessons = useQuizStore((s) => s.completedLessons);
  const openSettings = useQuizStore((s) => s.openSettings);
  const openHistory = useQuizStore((s) => s.openHistory);
  const openEasterEgg = useQuizStore((s) => s.openEasterEgg);
  const openAuth = useQuizStore((s) => s.openAuth);
  const user = useQuizStore((s) => s.user);

  const { t } = useTranslation();
  const { loadLesson, loadNewRound, isLoading, isOnline } = useWikipediaQuiz();
  const [isLibraryOpen, setIsLibraryOpen] = useState(false);
  const [logoTaps, setLogoTaps] = useState(0);

  const currentUnit = getUnitForLesson(currentLessonIndex);
  const currentLessonTitle = getLessonTitle(currentLessonIndex, language);

  const handleStartCurrentLesson = () => {
    loadLesson(currentLessonIndex, 5);
  };

  const handleLogoTap = () => {
    const next = logoTaps + 1;
    if (next >= 10) {
      setLogoTaps(0);
      openEasterEgg();
    } else {
      setLogoTaps(next);
      setTimeout(() => setLogoTaps(0), 4000);
    }
  };

  const completedInUnit = completedLessons.filter(
    (l) => l >= currentUnit.startLesson && l <= currentUnit.endLesson
  ).length;

  return (
    <div className="min-h-screen bg-[#F7F7F7] dark:bg-[#131F24] flex flex-col items-center transition-colors">
      {/* Top Sticky App Bar */}
      <header className="w-full bg-white dark:bg-[#1E2D34] border-b-2 border-[#E5E5E5] dark:border-[#37464F] sticky top-0 z-30 shadow-xs">
        <div className="max-w-4xl mx-auto px-4 py-3 flex items-center justify-between">
          {/* Brand with 10-tap Easter Egg trigger */}
          <div
            onClick={handleLogoTap}
            className="flex items-center gap-2.5 cursor-pointer select-none active:scale-95 transition-transform"
            title={logoTaps > 3 ? `${10 - logoTaps} tocchi all'Easter Egg!` : 'Wikingo'}
          >
            <div className="w-10 h-10 rounded-2xl bg-[#58CC02] border-b-4 border-[#46A302] flex items-center justify-center text-white text-xl font-black shadow-sm overflow-hidden">
              <img
                src="/ic_launcher_viking.png"
                alt="Wikingo Mascot"
                className="w-full h-full object-cover"
                onError={(e) => {
                  e.currentTarget.style.display = 'none';
                }}
              />
            </div>
            <div>
              <span className="text-xl font-black tracking-tight text-[#58CC02]">
                Wikingo
              </span>
              <span className="hidden sm:inline-block ml-1.5 text-[10px] font-extrabold uppercase px-2 py-0.5 rounded-md bg-[#D7FFB8] dark:bg-[#134E24] text-[#2A7000] dark:text-[#86EFAC]">
                {language === 'it' ? 'Percorso' : 'Path'}
              </span>
            </div>
          </div>

          {/* Gamification Stats & Navigation Bar */}
          <div className="flex items-center gap-1.5 sm:gap-2.5">
            {/* Language Selector */}
            <button
              onClick={() => setLanguage(language === 'it' ? 'en' : 'it')}
              className="flex items-center gap-1 text-xs font-black px-2.5 py-1.5 rounded-xl border-2 border-[#E5E5E5] dark:border-[#37464F] bg-white dark:bg-[#1E2D34] hover:bg-gray-50 dark:hover:bg-[#2A3B44] text-[#4B4B4B] dark:text-[#E5E7EB] transition-colors"
              title="Cambia lingua / Change language"
            >
              <Globe className="w-3.5 h-3.5 text-[#1CB0F6]" />
              <span>{language === 'it' ? 'IT 🇮🇹' : 'EN 🇬🇧'}</span>
            </button>

            <StreakBadge streak={streak} />

            {/* XP Pill */}
            <div className="flex items-center gap-1 font-extrabold text-[#1CB0F6] bg-[#F0F9FF] dark:bg-[#0C4A6E]/30 px-3 py-1.5 rounded-2xl border-2 border-[#BAE6FD] dark:border-[#0284C7]">
              <Zap className="w-4 h-4 fill-[#1CB0F6]" />
              <span className="text-xs sm:text-sm">{xp} XP</span>
            </div>

            <HeartLives lives={lives} />

            {/* History & Statistics Button */}
            <button
              onClick={openHistory}
              className="p-2 rounded-2xl border-2 border-[#E5E5E5] dark:border-[#37464F] bg-white dark:bg-[#1E2D34] hover:bg-gray-50 dark:hover:bg-[#2A3B44] text-[#777777] dark:text-[#E5E7EB] transition-colors"
              title={t('history_title')}
            >
              <History className="w-4 h-4 text-[#FF9600]" />
            </button>

            {/* Saved Articles Button */}
            <button
              onClick={() => setIsLibraryOpen(true)}
              className="p-2 rounded-2xl border-2 border-[#E5E5E5] dark:border-[#37464F] bg-white dark:bg-[#1E2D34] hover:bg-gray-50 dark:hover:bg-[#2A3B44] text-[#777777] dark:text-[#E5E7EB] transition-colors relative"
              title={t('saved_title')}
            >
              <Bookmark className="w-4 h-4" />
              {savedArticles.length > 0 && (
                <span className="absolute -top-1 -right-1 w-4 h-4 rounded-full bg-[#FF9600] text-white text-[9px] font-black flex items-center justify-center">
                  {savedArticles.length}
                </span>
              )}
            </button>

            {/* Auth / Account Profile Button */}
            <button
              onClick={openAuth}
              className="p-2 rounded-2xl border-2 border-[#E5E5E5] dark:border-[#37464F] bg-white dark:bg-[#1E2D34] hover:bg-gray-50 dark:hover:bg-[#2A3B44] text-[#777777] dark:text-[#E5E7EB] transition-colors"
              title={user?.isAnonymous ? t('profile_guest') : (user?.displayName || t('profile_title'))}
            >
              <User className="w-4 h-4 text-[#1CB0F6]" />
            </button>

            {/* Settings & Accessibility Button */}
            <button
              onClick={openSettings}
              className="p-2 rounded-2xl border-2 border-[#E5E5E5] dark:border-[#37464F] bg-white dark:bg-[#1E2D34] hover:bg-gray-50 dark:hover:bg-[#2A3B44] text-[#777777] dark:text-[#E5E7EB] transition-colors"
              title={t('settings_title')}
            >
              <Settings className="w-4 h-4" />
            </button>
          </div>
        </div>

        {/* Section Sub-Bar */}
        <div className="bg-[#F7F9FA] dark:bg-[#18262C] border-t border-[#E5E5E5] dark:border-[#37464F] px-4 py-2">
          <div className="max-w-4xl mx-auto flex items-center justify-between text-xs font-bold">
            <div className="flex items-center gap-2">
              <span
                className="w-2.5 h-2.5 rounded-full"
                style={{ backgroundColor: currentUnit.theme.primary }}
              />
              <span className="text-[#3C3C3C] dark:text-[#F3F4F6]">
                {getUnitLocalizedTitle(currentUnit, language)}: <strong className="font-extrabold">{getUnitLocalizedTopic(currentUnit, language)}</strong>
              </span>
              <span className="text-[#AFAFAF] dark:text-[#9CA3AF] hidden sm:inline">
                ({completedInUnit}/10 {language === 'it' ? 'completate' : 'completed'})
              </span>
            </div>

            <button
              onClick={() => loadNewRound(5)}
              disabled={isLoading}
              className="flex items-center gap-1 text-[#1CB0F6] hover:text-[#0C70A2] transition-colors"
              title={language === 'it' ? 'Gioca un round di voci casuali' : 'Play a round of random articles'}
            >
              <Shuffle className="w-3.5 h-3.5" />
              <span className="hidden sm:inline">{language === 'it' ? 'Quiz Rapido' : 'Quick Quiz'}</span>
            </button>
          </div>
        </div>
      </header>

      {/* Main Path Container */}
      <main className="w-full flex-1 flex flex-col items-center">
        {/* Offline Warning Banner */}
        {!isOnline && (
          <div className="w-full max-w-lg mt-4 px-4">
            <div className="flex items-center gap-2 p-3 bg-amber-50 border-2 border-amber-200 rounded-2xl text-amber-800 text-xs font-bold">
              <WifiOff className="w-4 h-4 text-amber-600 shrink-0" />
              <span>{t('offline_warning')}</span>
            </div>
          </div>
        )}

        {/* Winding Duolingo Path */}
        <DuolingoPath />
      </main>

      {/* Sticky Bottom Floating Bar to Continue Current Lesson */}
      <div className="fixed bottom-4 left-4 right-4 max-w-md mx-auto z-20">
        <motion.div
          initial={{ y: 20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          className="bg-white/95 dark:bg-[#1E2D34]/95 backdrop-blur-md p-3.5 rounded-3xl border-2 border-[#E5E5E5] dark:border-[#37464F] shadow-xl flex items-center justify-between gap-3"
        >
          <div className="min-w-0 flex-1 pl-1">
            <span className="text-[10px] font-black uppercase tracking-wider text-[#AFAFAF] dark:text-[#9CA3AF] block truncate">
              {language === 'it' ? 'PROSSIMA TAPPA' : 'NEXT STEP'} • {language === 'it' ? 'LEZIONE' : 'LESSON'} {currentLessonIndex}
            </span>
            <h4 className="text-sm font-black text-[#3C3C3C] dark:text-white truncate">
              {currentLessonTitle}
            </h4>
          </div>

          <button
            onClick={handleStartCurrentLesson}
            disabled={isLoading}
            className="px-5 py-3 rounded-2xl font-black text-sm uppercase tracking-wider text-white shadow-md active:translate-y-[2px] transition-all flex items-center gap-1.5 shrink-0"
            style={{
              backgroundColor: currentUnit.theme.primary,
              borderBottom: `4px solid ${currentUnit.theme.dark}`,
            }}
          >
            {isLoading ? (
              <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin" />
            ) : (
              <>
                <Play className="w-4 h-4 fill-white" />
                <span>{t('lesson_continue')}</span>
              </>
            )}
          </button>
        </motion.div>
      </div>

      {/* Saved Articles Modal */}
      <SavedArticlesModal
        isOpen={isLibraryOpen}
        onClose={() => setIsLibraryOpen(false)}
      />
    </div>
  );
};
