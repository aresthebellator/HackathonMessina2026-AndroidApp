import React from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { X, Flame, Zap, CheckCircle2, ExternalLink, Calendar, Trash2 } from 'lucide-react';
import { useQuizStore } from '@/store/useQuizStore';
import { useTranslation } from '@/lib/i18n';
import { Button } from '@/components/ui/Button';

export const HistoryModal: React.FC = () => {
  const isHistoryOpen = useQuizStore((s) => s.isHistoryOpen);
  const closeHistory = useQuizStore((s) => s.closeHistory);
  const streak = useQuizStore((s) => s.streak);
  const xp = useQuizStore((s) => s.xp);
  const completedLessons = useQuizStore((s) => s.completedLessons);
  const topicHistory = useQuizStore((s) => s.topicHistory);
  const clearHistory = useQuizStore((s) => s.clearHistory);
  const { t } = useTranslation();

  if (!isHistoryOpen) return null;

  const formatDate = (timestamp: number) => {
    try {
      const d = new Date(timestamp);
      return d.toLocaleDateString(undefined, {
        day: '2-digit',
        month: 'short',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      });
    } catch {
      return '';
    }
  };

  return (
    <AnimatePresence>
      <div className="fixed inset-0 z-50 bg-black/50 backdrop-blur-xs flex items-center justify-center p-4">
        <motion.div
          initial={{ opacity: 0, scale: 0.95, y: 15 }}
          animate={{ opacity: 1, scale: 1, y: 0 }}
          exit={{ opacity: 0, scale: 0.95, y: 15 }}
          className="w-full max-w-lg bg-white dark:bg-[#1E2D34] rounded-3xl border-2 border-[#E5E5E5] dark:border-[#37464F] shadow-2xl flex flex-col max-h-[88vh] overflow-hidden"
        >
          {/* Header */}
          <div className="px-6 py-4 border-b-2 border-[#E5E5E5] dark:border-[#37464F] flex items-center justify-between">
            <h2 className="text-lg font-black text-[#3C3C3C] dark:text-white flex items-center gap-2">
              <span>{t('history.title')}</span>
            </h2>
            <button
              onClick={closeHistory}
              className="p-1.5 rounded-xl text-[#AFAFAF] hover:text-[#3C3C3C] dark:hover:text-white hover:bg-black/5 dark:hover:bg-white/5 transition-colors"
              aria-label={t('common.close')}
            >
              <X className="w-5 h-5" />
            </button>
          </div>

          {/* Modal Content */}
          <div className="p-6 overflow-y-auto space-y-6 flex-1">
            {/* User Stats Summary Card */}
            <div className="p-4 rounded-2xl bg-[#F7F7F7] dark:bg-[#131F24] border-2 border-[#E5E5E5] dark:border-[#37464F] grid grid-cols-3 gap-2 text-center">
              {/* Streak */}
              <div className="flex flex-col items-center">
                <div className="w-8 h-8 rounded-xl bg-[#FFF7ED] dark:bg-[#78350F]/30 flex items-center justify-center text-[#FF9600] mb-1">
                  <Flame className="w-5 h-5 fill-[#FF9600]" />
                </div>
                <span className="text-base font-black text-[#3C3C3C] dark:text-white">
                  {streak} {t('dashboard.streak_unit')}
                </span>
                <span className="text-[11px] font-bold text-[#777777] dark:text-[#9CA3AF]">
                  {t('history.current_streak')}
                </span>
              </div>

              {/* XP */}
              <div className="flex flex-col items-center">
                <div className="w-8 h-8 rounded-xl bg-[#F0F9FF] dark:bg-[#0C4A6E]/30 flex items-center justify-center text-[#1CB0F6] mb-1">
                  <Zap className="w-5 h-5 fill-[#1CB0F6]" />
                </div>
                <span className="text-base font-black text-[#3C3C3C] dark:text-white">
                  {xp} {t('dashboard.xp_suffix')}
                </span>
                <span className="text-[11px] font-bold text-[#777777] dark:text-[#9CA3AF]">
                  {t('history.experience_points')}
                </span>
              </div>

              {/* Lessons Completed */}
              <div className="flex flex-col items-center">
                <div className="w-8 h-8 rounded-xl bg-[#F2FCE8] dark:bg-[#14532D]/30 flex items-center justify-center text-[#58CC02] mb-1">
                  <CheckCircle2 className="w-5 h-5" />
                </div>
                <span className="text-base font-black text-[#3C3C3C] dark:text-white">
                  {completedLessons.length}
                </span>
                <span className="text-[11px] font-bold text-[#777777] dark:text-[#9CA3AF]">
                  {t('history.lessons_completed')}
                </span>
              </div>
            </div>

            {/* Topics Heading */}
            <div className="flex items-center justify-between">
              <h3 className="text-sm font-black text-[#3C3C3C] dark:text-white uppercase tracking-wider">
                {t('history.topics_heading')} ({topicHistory.length})
              </h3>
              {topicHistory.length > 0 && (
                <button
                  onClick={clearHistory}
                  className="text-xs font-bold text-gray-400 hover:text-[#FF4B4B] flex items-center gap-1 transition-colors"
                  title="Azzera cronologia"
                >
                  <Trash2 className="w-3.5 h-3.5" />
                  <span className="hidden sm:inline">Pulisci</span>
                </button>
              )}
            </div>

            {/* List or Empty State */}
            {topicHistory.length === 0 ? (
              <div className="py-12 flex flex-col items-center text-center space-y-3">
                <img
                  src="/ic_launcher_viking.png"
                  alt="Wikingo"
                  className="w-18 h-18 object-contain drop-shadow-sm select-none"
                  onError={(e) => {
                    (e.target as HTMLElement).style.display = 'none';
                  }}
                />
                <h4 className="text-base font-black text-[#3C3C3C] dark:text-white">
                  {t('history.empty_title')}
                </h4>
                <p className="text-xs text-[#777777] dark:text-[#9CA3AF] max-w-xs">
                  {t('history.empty_description')}
                </p>
              </div>
            ) : (
              <div className="space-y-3">
                {topicHistory.map((item) => (
                  <div
                    key={item.id}
                    className="p-4 rounded-2xl bg-white dark:bg-[#18262C] border-2 border-[#E5E5E5] dark:border-[#37464F] hover:border-[#1CB0F6] transition-all space-y-2.5"
                  >
                    <div className="flex items-start justify-between gap-3">
                      <div className="min-w-0 flex-1">
                        <div className="flex items-center gap-2">
                          <h4 className="text-base font-black text-[#3C3C3C] dark:text-white truncate">
                            {item.title}
                          </h4>
                          <span className="text-[11px] font-black px-2 py-0.5 rounded-lg bg-[#F7F7F7] dark:bg-[#1E2D34] text-[#3C3C3C] dark:text-[#E5E7EB] border border-[#E5E5E5] dark:border-[#37464F] shrink-0">
                            {item.score}/{item.totalQuestions}
                          </span>
                        </div>
                        {item.description && (
                          <p className="text-xs text-[#777777] dark:text-[#9CA3AF] truncate mt-0.5">
                            {item.description}
                          </p>
                        )}
                      </div>

                      <a
                        href={item.wikiUrl}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="p-2 rounded-xl text-[#1CB0F6] hover:bg-[#DDF4FF] dark:hover:bg-[#0C4A6E]/30 shrink-0 transition-colors"
                        title={t('history.open_topic')}
                      >
                        <ExternalLink className="w-4 h-4" />
                      </a>
                    </div>

                    {item.extract && (
                      <p className="text-xs text-[#4B4B4B] dark:text-[#D1D5DB] line-clamp-2 leading-relaxed bg-[#F7F7F7] dark:bg-[#131F24] p-2.5 rounded-xl border border-[#E5E5E5] dark:border-[#2A3B44]">
                        {item.extract}
                      </p>
                    )}

                    <div className="flex items-center justify-between text-[11px] font-bold text-[#AFAFAF] dark:text-[#9CA3AF] pt-1 border-t border-gray-100 dark:border-[#2A3B44]">
                      <span className="flex items-center gap-1">
                        <Calendar className="w-3.5 h-3.5" />
                        {formatDate(item.completedAt)}
                      </span>
                      <span className="text-[#58CC02] dark:text-[#86EFAC] font-black">
                        +{item.xpEarned} XP
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>

          {/* Footer */}
          <div className="p-4 border-t-2 border-[#E5E5E5] dark:border-[#37464F] bg-[#F7F7F7] dark:bg-[#131F24]">
            <Button
              variant="outline"
              size="md"
              fullWidth
              onClick={closeHistory}
              className="dark:bg-[#1E2D34] dark:text-white dark:border-[#37464F]"
            >
              {t('common.close')}
            </Button>
          </div>
        </motion.div>
      </div>
    </AnimatePresence>
  );
};
