import React from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { X, Bookmark, ExternalLink, Trash2, BookOpen } from 'lucide-react';
import { useQuizStore } from '@/store/useQuizStore';
import { Button } from '@/components/ui/Button';
import { useTranslation } from '@/lib/i18n';

interface SavedArticlesModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export const SavedArticlesModal: React.FC<SavedArticlesModalProps> = ({ isOpen, onClose }) => {
  const savedArticles = useQuizStore((s) => s.savedArticles);
  const toggleBookmark = useQuizStore((s) => s.toggleBookmark);
  const language = useQuizStore((s) => s.language);
  const { t } = useTranslation();

  if (!isOpen) return null;

  return (
    <AnimatePresence>
      <div className="fixed inset-0 z-50 bg-black/50 backdrop-blur-xs flex items-center justify-center p-4">
        <motion.div
          initial={{ opacity: 0, scale: 0.95 }}
          animate={{ opacity: 1, scale: 1 }}
          exit={{ opacity: 0, scale: 0.95 }}
          className="w-full max-w-lg bg-white dark:bg-[#1E2D34] rounded-3xl border-2 border-[#E5E5E5] dark:border-[#37464F] flex flex-col max-h-[85vh] shadow-2xl overflow-hidden"
        >
          {/* Modal Header */}
          <div className="px-6 py-4 border-b-2 border-[#E5E5E5] dark:border-[#37464F] flex items-center justify-between">
            <div className="flex items-center gap-2">
              <div className="w-8 h-8 rounded-xl bg-[#FFF7ED] dark:bg-[#78350F]/40 border border-[#FED7AA] dark:border-[#92400E] flex items-center justify-center text-[#FF9600]">
                <Bookmark className="w-4 h-4 fill-[#FF9600]" />
              </div>
              <h2 className="text-lg font-black text-[#3C3C3C] dark:text-white">
                {t('saved_title')} ({savedArticles.length})
              </h2>
            </div>
            <button
              onClick={onClose}
              className="p-1.5 rounded-xl text-[#AFAFAF] hover:text-[#3C3C3C] dark:hover:text-white hover:bg-black/5 dark:hover:bg-white/5 transition-colors"
            >
              <X className="w-5 h-5" />
            </button>
          </div>

          {/* List of articles */}
          <div className="p-6 overflow-y-auto space-y-4 flex-1 divide-y divide-gray-100 dark:divide-[#37464F]">
            {savedArticles.length === 0 ? (
              <div className="text-center py-12 space-y-3">
                <BookOpen className="w-12 h-12 text-[#AFAFAF] mx-auto" />
                <p className="text-sm font-bold text-[#777777] dark:text-[#93A5AF]">
                  {t('saved_empty')}
                </p>
                <p className="text-xs text-[#AFAFAF] dark:text-gray-400">
                  {t('saved.empty_description')}
                </p>
              </div>
            ) : (
              savedArticles.map((item) => (
                <div key={item.pageid} className="pt-3 first:pt-0 space-y-2">
                  <div className="flex items-start justify-between gap-3">
                    <div className="flex items-center gap-3">
                      {item.thumbnailUrl && (
                        <img
                          src={item.thumbnailUrl}
                          alt={item.title}
                          className="w-12 h-12 rounded-xl object-cover border border-gray-200 dark:border-[#37464F] shrink-0"
                        />
                      )}
                      <div>
                        <h3 className="text-base font-extrabold text-[#3C3C3C] dark:text-white">
                          {item.title}
                        </h3>
                        {item.description && (
                          <p className="text-xs text-[#777777] dark:text-[#93A5AF] font-medium">
                            {item.description}
                          </p>
                        )}
                      </div>
                    </div>

                    <div className="flex items-center gap-1">
                      <a
                        href={item.url}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="p-2 text-[#1CB0F6] hover:bg-[#DDF4FF] dark:hover:bg-[#0C4A6E]/30 rounded-xl transition-colors"
                        title={t('read_wikipedia')}
                      >
                        <ExternalLink className="w-4 h-4" />
                      </a>
                      <button
                        onClick={() =>
                          toggleBookmark({
                            pageid: item.pageid,
                            title: item.title,
                            extract: item.extract,
                            content_urls: { desktop: { page: item.url } },
                            lang: item.lang,
                            type: 'standard',
                          })
                        }
                        className="p-2 text-[#FF4B4B] hover:bg-[#FFF1F2] dark:hover:bg-red-900/30 rounded-xl transition-colors"
                        title={t('saved_remove')}
                      >
                        <Trash2 className="w-4 h-4" />
                      </button>
                    </div>
                  </div>

                  <p className="text-xs text-[#4B4B4B] dark:text-[#E5E7EB] line-clamp-2 leading-relaxed bg-[#F7F7F7] dark:bg-[#131F24] p-2.5 rounded-xl border border-[#E5E5E5] dark:border-[#37464F]">
                    {item.extract}
                  </p>
                </div>
              ))
            )}
          </div>

          {/* Modal Footer */}
          <div className="p-4 border-t-2 border-[#E5E5E5] dark:border-[#37464F] bg-gray-50 dark:bg-[#131F24]">
            <Button
              variant="outline"
              size="md"
              fullWidth
              onClick={onClose}
              className="dark:bg-[#1E2D34] dark:text-white dark:border-[#37464F]"
            >
              {language === 'it' ? 'Chiudi' : 'Close'}
            </Button>
          </div>
        </motion.div>
      </div>
    </AnimatePresence>
  );
};
