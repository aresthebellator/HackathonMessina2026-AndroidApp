import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { Zap, Bookmark, Globe, WifiOff, Shuffle, Play, Settings } from 'lucide-react';
import { HeartLives } from '@/components/ui/HeartLives';
import { StreakBadge } from '@/components/ui/StreakBadge';
import { SavedArticlesModal } from './SavedArticlesModal';
import { DuolingoPath } from '@/components/path/DuolingoPath';
import { useQuizStore } from '@/store/useQuizStore';
import { useWikipediaQuiz } from '@/hooks/useWikipediaQuiz';
import { getUnitForLesson, LESSON_TITLES } from '@/lib/unitsData';

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

  const { loadLesson, loadNewRound, isLoading, isOnline } = useWikipediaQuiz();
  const [isLibraryOpen, setIsLibraryOpen] = useState(false);

  const currentUnit = getUnitForLesson(currentLessonIndex);
  const currentLessonTitle = LESSON_TITLES[currentLessonIndex] || `Lezione ${currentLessonIndex}`;

  const handleStartCurrentLesson = () => {
    loadLesson(currentLessonIndex, 5);
  };

  return (
    <div className="min-h-screen bg-[#F7F7F7] dark:bg-[#131F24] flex flex-col items-center transition-colors">
      {/* Top Sticky App Bar */}
      <header className="w-full bg-white dark:bg-[#1E2D34] border-b-2 border-[#E5E5E5] dark:border-[#37464F] sticky top-0 z-30 shadow-xs">
        <div className="max-w-4xl mx-auto px-4 py-3 flex items-center justify-between">
          {/* Brand */}
          <div className="flex items-center gap-2.5">
            <div className="w-10 h-10 rounded-2xl bg-[#58CC02] border-b-4 border-[#46A302] flex items-center justify-center text-white text-xl font-black shadow-sm select-none">
              🦉
            </div>
            <div>
              <span className="text-xl font-black tracking-tight text-[#58CC02]">
                Wikingo
              </span>
              <span className="hidden sm:inline-block ml-1.5 text-[10px] font-extrabold uppercase px-2 py-0.5 rounded-md bg-[#D7FFB8] dark:bg-[#134E24] text-[#2A7000] dark:text-[#86EFAC]">
                Percorso
              </span>
            </div>
          </div>

          {/* Gamification Stats */}
          <div className="flex items-center gap-1.5 sm:gap-3">
            {/* Language Selector */}
            <button
              onClick={() => setLanguage(language === 'it' ? 'en' : 'it')}
              className="flex items-center gap-1 text-xs font-black px-2.5 py-1.5 rounded-xl border-2 border-[#E5E5E5] dark:border-[#37464F] bg-white dark:bg-[#1E2D34] hover:bg-gray-50 dark:hover:bg-[#2A3B44] text-[#4B4B4B] dark:text-[#E5E7EB] transition-colors"
              title="Cambia lingua"
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

            {/* Saved Articles Button */}
            <button
              onClick={() => setIsLibraryOpen(true)}
              className="p-2 rounded-2xl border-2 border-[#E5E5E5] dark:border-[#37464F] bg-white dark:bg-[#1E2D34] hover:bg-gray-50 dark:hover:bg-[#2A3B44] text-[#777777] dark:text-[#E5E7EB] transition-colors relative"
              title="Voci enciclopediche salvate"
            >
              <Bookmark className="w-4 h-4" />
              {savedArticles.length > 0 && (
                <span className="absolute -top-1 -right-1 w-4 h-4 rounded-full bg-[#FF9600] text-white text-[9px] font-black flex items-center justify-center">
                  {savedArticles.length}
                </span>
              )}
            </button>

            {/* Settings & Accessibility Button */}
            <button
              onClick={openSettings}
              className="p-2 rounded-2xl border-2 border-[#E5E5E5] dark:border-[#37464F] bg-white dark:bg-[#1E2D34] hover:bg-gray-50 dark:hover:bg-[#2A3B44] text-[#777777] dark:text-[#E5E7EB] transition-colors"
              title="Impostazioni e Accessibilità"
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
                {currentUnit.title}: <strong className="font-extrabold">{currentUnit.topic}</strong>
              </span>
              <span className="text-[#AFAFAF] dark:text-[#9CA3AF] hidden sm:inline">
                ({completedLessons.filter(l => l >= currentUnit.startLesson && l <= currentUnit.endLesson).length}/10 completate)
              </span>
            </div>

            <button
              onClick={() => loadNewRound(5)}
              disabled={isLoading}
              className="flex items-center gap-1 text-[#1CB0F6] hover:text-[#0C70A2] transition-colors"
              title="Gioca un round di voci casuali"
            >
              <Shuffle className="w-3.5 h-3.5" />
              <span className="hidden sm:inline">Quiz Rapido</span>
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
              <span>Modalità offline attiva. Il percorso utilizzerà il catalogo integrato.</span>
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
              PROSSIMA TAPPA • LEZIONE {currentLessonIndex}
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
                <span>Continua</span>
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
