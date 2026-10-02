import React, { useEffect } from 'react';
import { motion } from 'framer-motion';
import confetti from 'canvas-confetti';
import { Trophy, Zap, Flame, ExternalLink, Bookmark, ArrowRight, Star, History, BookOpen } from 'lucide-react';
import { Button } from '@/components/ui/Button';
import { useQuizStore } from '@/store/useQuizStore';
import { useTranslation } from '@/lib/i18n';
import { useWikipediaQuiz } from '@/hooks/useWikipediaQuiz';
import { getLessonTitle } from '@/lib/unitsData';

interface RoundCompleteProps {
  onPlayAgain: () => void;
  onGoHome: () => void;
}

export const RoundComplete: React.FC<RoundCompleteProps> = ({ onPlayAgain, onGoHome }) => {
  const currentRound = useQuizStore((s) => s.currentRound);
  const streak = useQuizStore((s) => s.streak);
  const toggleBookmark = useQuizStore((s) => s.toggleBookmark);
  const isArticleSaved = useQuizStore((s) => s.isArticleSaved);
  const openHistory = useQuizStore((s) => s.openHistory);
  const language = useQuizStore((s) => s.language);
  const { isLoading } = useWikipediaQuiz();
  const { t } = useTranslation();

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
  const lessonTitle = lessonNumber ? getLessonTitle(lessonNumber, language) : null;
  const starsEarned = score >= 5 ? 3 : score >= 4 ? 2 : 1;

  // Primary topic explored in this round
  const mainArticle = currentRound.questions[0]?.article;
  const isMainSaved = mainArticle ? isArticleSaved(mainArticle.pageid) : false;

  return (
    <motion.div
      initial={{ opacity: 0, scale: 0.95 }}
      animate={{ opacity: 1, scale: 1 }}
      className="w-full max-w-lg mx-auto px-4 py-8 flex flex-col items-center text-center space-y-6"
    >
      {/* Viking Trophy Mascot */}
      <div className="relative">
        <div className="w-24 h-24 rounded-3xl bg-[#FFF5C2] dark:bg-[#78350F]/30 border-4 border-[#FFC800] flex items-center justify-center shadow-lg animate-bounce">
          <img
            src="/ic_launcher_viking.png"
            alt={t('welcome.mascot_description')}
            className="w-16 h-16 object-contain"
            onError={(e) => {
              (e.target as HTMLElement).style.display = 'none';
            }}
          />
          <Trophy className="w-10 h-10 text-[#FFC800] stroke-[2.2] hidden" />
        </div>
      </div>

      {/* Title & Lesson Context */}
      <div className="space-y-1">
        <h1 className="text-2xl sm:text-3xl font-black text-[#3C3C3C] dark:text-white">
          {lessonNumber
            ? `${t('trainer.lesson_complete_title')} (${lessonTitle})`
            : t('trainer.lesson_complete_title')}
        </h1>
        <p className="text-xs sm:text-sm font-bold text-[#777777] dark:text-[#9CA3AF]">
          {t('trainer.lesson_complete_subtitle')}
        </p>

        {/* Stars Rating */}
        <div className="flex justify-center items-center gap-1.5 pt-2">
          {[1, 2, 3].map((s) => (
            <Star
              key={s}
              className={`w-7 h-7 ${
                s <= starsEarned
                  ? 'text-[#FFC800] fill-[#FFC800]'
                  : 'text-gray-300 dark:text-gray-600 fill-gray-200 dark:fill-gray-700'
              }`}
            />
          ))}
        </div>
      </div>

      {/* Gamification Stats Cards (XP, Accuracy, Streak) */}
      <div className="grid grid-cols-3 gap-3 w-full">
        {/* XP Card */}
        <div className="p-3.5 rounded-2xl border-2 border-[#FED7AA] dark:border-[#92400E] bg-[#FFF7ED] dark:bg-[#78350F]/20 flex flex-col items-center">
          <div className="flex items-center gap-1 text-[#FF9600] font-black text-xs uppercase">
            <Zap className="w-4 h-4 fill-[#FF9600]" /> {t('trainer.stat_total_xp')}
          </div>
          <span className="text-2xl font-black text-[#D97706] dark:text-[#FCD34D] mt-1">
            +{totalXp}
          </span>
        </div>

        {/* Accuracy Card */}
        <div className="p-3.5 rounded-2xl border-2 border-[#B0EC77] dark:border-[#047857] bg-[#F2FCE8] dark:bg-[#064E3B]/20 flex flex-col items-center">
          <div className="flex items-center gap-1 text-[#58CC02] dark:text-[#86EFAC] font-black text-xs uppercase">
            {t('trainer.stat_accuracy')}
          </div>
          <span className="text-2xl font-black text-[#2A7000] dark:text-[#86EFAC] mt-1">
            {accuracyPercent}%
          </span>
        </div>

        {/* Streak Card */}
        <div className="p-3.5 rounded-2xl border-2 border-[#FED7AA] dark:border-[#92400E] bg-[#FFF7ED] dark:bg-[#78350F]/20 flex flex-col items-center">
          <div className="flex items-center gap-1 text-[#FF9600] font-black text-xs uppercase">
            <Flame className="w-4 h-4 fill-[#FF9600]" /> {t('trainer.stat_streak')}
          </div>
          <span className="text-2xl font-black text-[#D97706] dark:text-[#FCD34D] mt-1">
            {streak} {t('dashboard.streak_unit')}
          </span>
        </div>
      </div>

      {/* Wikipedia Article Summary Card (matches android-app CompleteView) */}
      {mainArticle && (
        <div className="w-full bg-white dark:bg-[#1E2D34] rounded-3xl border-2 border-[#E5E5E5] dark:border-[#37464F] p-4 sm:p-5 text-left space-y-3 shadow-xs">
          {mainArticle.thumbnail && (
            <div className="w-full h-36 rounded-2xl overflow-hidden border border-gray-200 dark:border-gray-700 bg-gray-50 dark:bg-gray-800">
              <img
                src={mainArticle.thumbnail.source}
                alt={mainArticle.title}
                className="w-full h-full object-cover"
              />
            </div>
          )}

          <div>
            <div className="flex items-center justify-between">
              <h3 className="text-base sm:text-lg font-black text-[#3C3C3C] dark:text-white">
                {mainArticle.title}
              </h3>
              <span className="text-[10px] font-extrabold uppercase px-2 py-0.5 rounded-md bg-[#DDF4FF] dark:bg-[#0C4A6E] text-[#0C70A2] dark:text-[#38BDF8]">
                Wikipedia
              </span>
            </div>
            {mainArticle.description && (
              <p className="text-xs text-[#777777] dark:text-[#9CA3AF] mt-0.5">
                {mainArticle.description}
              </p>
            )}
          </div>

          {mainArticle.extract && (
            <p className="text-xs text-[#4B4B4B] dark:text-[#D1D5DB] leading-relaxed line-clamp-3 bg-[#F7F7F7] dark:bg-[#131F24] p-3 rounded-xl border border-[#E5E5E5] dark:border-[#2A3B44]">
              {mainArticle.extract}
            </p>
          )}

          <div className="flex items-center gap-2 pt-1">
            <a
              href={mainArticle.content_urls.desktop.page}
              target="_blank"
              rel="noopener noreferrer"
              className="flex-1 py-2 rounded-xl border-2 border-[#E5E5E5] dark:border-[#37464F] text-xs font-black text-[#1CB0F6] hover:bg-[#F0F9FF] dark:hover:bg-[#0C4A6E]/30 flex items-center justify-center gap-1.5 transition-colors"
            >
              <ExternalLink className="w-3.5 h-3.5" />
              <span>{t('trainer.read_on_wiki')}</span>
            </a>

            <button
              onClick={() => toggleBookmark(mainArticle)}
              className="py-2 px-3 rounded-xl border-2 border-[#E5E5E5] dark:border-[#37464F] text-xs font-black text-[#777777] dark:text-[#9CA3AF] hover:text-[#FF9600] flex items-center gap-1.5 transition-colors"
              title={isMainSaved ? t('saved.remove') : t('trainer.save_article')}
            >
              <Bookmark className={`w-3.5 h-3.5 ${isMainSaved ? 'fill-[#FF9600] text-[#FF9600]' : ''}`} />
              <span className="hidden sm:inline">{isMainSaved ? t('common.delete') : t('common.save')}</span>
            </button>
          </div>
        </div>
      )}

      {/* Discovered Articles Review List (if > 1 question) */}
      {currentRound.questions.length > 1 && (
        <div className="w-full bg-white dark:bg-[#1E2D34] rounded-3xl border-2 border-[#E5E5E5] dark:border-[#37464F] p-4 text-left space-y-3">
          <h3 className="text-xs font-black text-[#777777] dark:text-[#93A5AF] uppercase tracking-wider flex items-center gap-1.5">
            <BookOpen className="w-3.5 h-3.5" />
            <span>{t('trainer.all_topics_label')}</span>
          </h3>
          <div className="divide-y divide-gray-100 dark:divide-[#37464F]">
            {currentRound.questions.map((q) => {
              const isSaved = isArticleSaved(q.article.pageid);
              return (
                <div key={q.id} className="py-2 flex items-center justify-between gap-2">
                  <div className="flex items-center gap-2.5 min-w-0">
                    {q.article.thumbnail ? (
                      <img
                        src={q.article.thumbnail.source}
                        alt={q.article.title}
                        className="w-8 h-8 rounded-lg object-cover shrink-0 border border-gray-200 dark:border-gray-700"
                      />
                    ) : (
                      <div className="w-8 h-8 rounded-lg bg-gray-100 dark:bg-gray-800 flex items-center justify-center shrink-0 text-[10px] font-bold text-gray-500">
                        Wiki
                      </div>
                    )}
                    <div className="min-w-0">
                      <h4 className="text-xs font-bold text-[#3C3C3C] dark:text-white truncate">
                        {q.article.title}
                      </h4>
                    </div>
                  </div>

                  <div className="flex items-center gap-1 shrink-0">
                    <button
                      onClick={() => toggleBookmark(q.article)}
                      className="p-1 rounded-lg text-gray-400 hover:text-[#FF9600] transition-colors"
                      title={isSaved ? t('saved.remove') : t('trainer.save_article')}
                    >
                      <Bookmark className={`w-3.5 h-3.5 ${isSaved ? 'fill-[#FF9600] text-[#FF9600]' : ''}`} />
                    </button>
                    <a
                      href={q.sourceUrl}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="p-1 rounded-lg text-gray-400 hover:text-[#1CB0F6] transition-colors"
                      title={t('saved.open_in_browser')}
                    >
                      <ExternalLink className="w-3.5 h-3.5" />
                    </a>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      )}

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
              ? t('common.loading')
              : lessonNumber
              ? `${t('trainer.new_lesson_button')} (${lessonNumber + 1})`
              : t('trainer.new_lesson_button')}
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
          {t('trainer.back_to_dashboard')}
        </Button>

        {/* View History & Stats Link */}
        <button
          onClick={openHistory}
          className="text-xs font-black text-[#777777] dark:text-[#9CA3AF] hover:text-[#1CB0F6] dark:hover:text-[#38BDF8] flex items-center justify-center gap-1.5 pt-1 transition-colors mx-auto"
        >
          <History className="w-3.5 h-3.5" />
          <span>{t('trainer.view_history_stats')}</span>
        </button>
      </div>
    </motion.div>
  );
};
