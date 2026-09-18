import React, { useEffect } from 'react';
import { motion } from 'framer-motion';
import confetti from 'canvas-confetti';
import { Trophy, Zap, Flame, ExternalLink, Bookmark, ArrowRight, Star } from 'lucide-react';
import { Button } from '@/components/ui/Button';
import { useQuizStore } from '@/store/useQuizStore';
import { useWikipediaQuiz } from '@/hooks/useWikipediaQuiz';
import { LESSON_TITLES } from '@/lib/unitsData';

interface RoundCompleteProps {
  onPlayAgain: () => void;
  onGoHome: () => void;
}

export const RoundComplete: React.FC<RoundCompleteProps> = ({ onPlayAgain, onGoHome }) => {
  const currentRound = useQuizStore((s) => s.currentRound);
  const streak = useQuizStore((s) => s.streak);
  const toggleBookmark = useQuizStore((s) => s.toggleBookmark);
  const isArticleSaved = useQuizStore((s) => s.isArticleSaved);
  const { isLoading } = useWikipediaQuiz();

  useEffect(() => {
    // Launch celebratory confetti
    try {
      confetti({
        particleCount: 80,
        spread: 70,
        origin: { y: 0.6 },
        colors: ['#58CC02', '#1CB0F6', '#FFC800', '#FF4B4B', '#CE82FF'],
      });
    } catch {}
  }, []);

  if (!currentRound) return null;

  const totalQuestions = currentRound.questions.length;
  const score = currentRound.score;
  const accuracyPercent = Math.round((score / totalQuestions) * 100);
  const totalXp = currentRound.totalXp + 25; // includes 25 XP round completion bonus
  const lessonNumber = currentRound.lessonNumber;
  const lessonTitle = lessonNumber ? LESSON_TITLES[lessonNumber] : null;
  const starsEarned = score >= 5 ? 3 : score >= 4 ? 2 : 1;

  return (
    <motion.div
      initial={{ opacity: 0, scale: 0.95 }}
      animate={{ opacity: 1, scale: 1 }}
      className="w-full max-w-lg mx-auto px-4 py-8 flex flex-col items-center text-center space-y-6"
    >
      {/* Trophy / Victory Badge */}
      <div className="relative">
        <div className="w-24 h-24 rounded-3xl bg-[#FFF5C2] border-4 border-[#FFC800] flex items-center justify-center shadow-lg animate-bounce">
          <Trophy className="w-12 h-12 text-[#FFC800] stroke-[2.2]" />
        </div>
      </div>

      {/* Title & Lesson Context */}
      <div className="space-y-1">
        <h1 className="text-2xl sm:text-3xl font-black text-[#3C3C3C]">
          {lessonNumber ? `Lezione ${lessonNumber} Completata!` : accuracyPercent >= 80 ? 'Lezione Completata!' : 'Buon Allenamento!'}
        </h1>
        {lessonTitle && (
          <p className="text-base font-extrabold text-[#58CC02]">
            "{lessonTitle}"
          </p>
        )}
        <p className="text-xs sm:text-sm font-bold text-[#777777]">
          Hai esplorato 5 nuove voci dal patrimonio di Wikipedia!
        </p>

        {/* Stars Rating */}
        <div className="flex justify-center items-center gap-1.5 pt-2">
          {[1, 2, 3].map((s) => (
            <Star
              key={s}
              className={`w-7 h-7 ${
                s <= starsEarned
                  ? 'text-[#FFC800] fill-[#FFC800]'
                  : 'text-gray-300 fill-gray-200'
              }`}
            />
          ))}
        </div>
      </div>

      {/* Gamification Stats Cards */}
      <div className="grid grid-cols-3 gap-3 w-full">
        {/* XP Card */}
        <div className="p-3.5 rounded-2xl border-2 border-[#FED7AA] bg-[#FFF7ED] flex flex-col items-center">
          <div className="flex items-center gap-1 text-[#FF9600] font-black text-xs uppercase">
            <Zap className="w-4 h-4 fill-[#FF9600]" /> Punti XP
          </div>
          <span className="text-2xl font-black text-[#D97706] mt-1">+{totalXp}</span>
        </div>

        {/* Accuracy Card */}
        <div className="p-3.5 rounded-2xl border-2 border-[#B0EC77] bg-[#F2FCE8] flex flex-col items-center">
          <div className="flex items-center gap-1 text-[#58CC02] font-black text-xs uppercase">
            Precisione
          </div>
          <span className="text-2xl font-black text-[#2A7000] mt-1">{accuracyPercent}%</span>
        </div>

        {/* Streak Card */}
        <div className="p-3.5 rounded-2xl border-2 border-[#FED7AA] bg-[#FFF7ED] flex flex-col items-center">
          <div className="flex items-center gap-1 text-[#FF9600] font-black text-xs uppercase">
            <Flame className="w-4 h-4 fill-[#FF9600]" /> Streak
          </div>
          <span className="text-2xl font-black text-[#D97706] mt-1">{streak} {streak === 1 ? 'giorno' : 'giorni'}</span>
        </div>
      </div>

      {/* Discovered Articles Review Box */}
      <div className="w-full bg-white dark:bg-[#1E2D34] rounded-3xl border-2 border-[#E5E5E5] dark:border-[#37464F] p-4 text-left space-y-3">
        <h3 className="text-xs font-black text-[#777777] dark:text-[#93A5AF] uppercase tracking-wider">
          Voci esplorate in questo round:
        </h3>
        <div className="divide-y divide-gray-100 dark:divide-[#37464F]">
          {currentRound.questions.map((q) => {
            const isSaved = isArticleSaved(q.article.pageid);
            return (
              <div key={q.id} className="py-2.5 flex items-center justify-between gap-2">
                <div className="flex items-center gap-2.5 min-w-0">
                  {q.article.thumbnail ? (
                    <img
                      src={q.article.thumbnail.source}
                      alt={q.article.title}
                      className="w-9 h-9 rounded-lg object-cover shrink-0 border border-gray-200 dark:border-gray-700"
                    />
                  ) : (
                    <div className="w-9 h-9 rounded-lg bg-gray-100 dark:bg-gray-800 flex items-center justify-center shrink-0 text-xs font-bold text-gray-500">
                      Wiki
                    </div>
                  )}
                  <div className="min-w-0">
                    <h4 className="text-sm font-bold text-[#3C3C3C] dark:text-white truncate">
                      {q.article.title}
                    </h4>
                    <p className="text-[11px] text-[#777777] dark:text-[#93A5AF] truncate">
                      {q.article.description || 'Voce enciclopedica'}
                    </p>
                  </div>
                </div>

                <div className="flex items-center gap-1.5 shrink-0">
                  <button
                    onClick={() => toggleBookmark(q.article)}
                    className="p-1.5 rounded-lg text-gray-400 hover:text-[#FF9600] hover:bg-gray-50 dark:hover:bg-gray-800 transition-colors"
                    title={isSaved ? 'Rimuovi dai salvati' : 'Salva articolo'}
                  >
                    <Bookmark className={`w-4 h-4 ${isSaved ? 'fill-[#FF9600] text-[#FF9600]' : ''}`} />
                  </button>
                  <a
                    href={q.sourceUrl}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="p-1.5 rounded-lg text-gray-400 hover:text-[#1CB0F6] hover:bg-gray-50 dark:hover:bg-gray-800 transition-colors"
                    title="Apri su Wikipedia"
                  >
                    <ExternalLink className="w-4 h-4" />
                  </a>
                </div>
              </div>
            );
          })}
        </div>
      </div>

      {/* Action Buttons */}
      <div className="w-full space-y-3 pt-2">
        <Button
          variant="green"
          size="lg"
          fullWidth
          onClick={onPlayAgain}
          disabled={isLoading}
          className="flex items-center justify-center gap-2"
        >
          <span>
            {isLoading
              ? 'Caricamento...'
              : lessonNumber
              ? `Prossima Lezione (${lessonNumber + 1})`
              : 'Prossimo Round'}
          </span>
          <ArrowRight className="w-5 h-5" />
        </Button>
        <Button
          variant="outline"
          size="md"
          fullWidth
          onClick={onGoHome}
          className="dark:bg-[#1E2D34] dark:border-[#37464F] dark:text-white"
        >
          Torna al Percorso
        </Button>
      </div>
    </motion.div>
  );
};
