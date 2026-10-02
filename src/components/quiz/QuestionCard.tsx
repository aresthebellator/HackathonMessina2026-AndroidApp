import React from 'react';
import { motion } from 'framer-motion';
import { Bookmark, ExternalLink, CheckCircle2, XCircle } from 'lucide-react';
import { Question } from '@/types';
import { useQuizStore } from '@/store/useQuizStore';
import { useTranslation } from '@/lib/i18n';
import { MascotReaction } from '@/components/quiz/MascotReaction';
import { cn } from '@/lib/utils';

interface QuestionCardProps {
  question: Question;
}

export const QuestionCard: React.FC<QuestionCardProps> = ({ question }) => {
  const currentRound = useQuizStore((s) => s.currentRound);
  const selectedOptionId = useQuizStore((s) => s.selectedOptionId);
  const feedbackStatus = useQuizStore((s) => s.feedbackStatus);
  const selectOption = useQuizStore((s) => s.selectOption);
  const toggleBookmark = useQuizStore((s) => s.toggleBookmark);
  const isArticleSaved = useQuizStore((s) => s.isArticleSaved(question.article.pageid));
  const { t } = useTranslation();

  const currentIndex = currentRound?.currentIndex ?? 0;
  const totalQuestions = currentRound?.questions.length ?? 5;

  const getQuestionTypeLabel = () => {
    switch (question.type) {
      case 'cloze':
        return t('quiz.type_cloze');
      case 'true_false':
        return t('quiz.type_true_false');
      case 'subject_recognition':
        return t('quiz.type_subject');
      case 'multiple_choice':
      default:
        return t('quiz.type_mc');
    }
  };

  return (
    <div className="w-full max-w-2xl mx-auto px-4 py-4 flex flex-col gap-5">
      {/* Mascot Comic Reaction Speech Bubble */}
      <MascotReaction
        message={t('trainer.question_header', { current: currentIndex + 1, total: totalQuestions })}
      />

      {/* Question metadata badge & bookmark */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-2">
          <span className="text-xs font-black uppercase tracking-wider px-3 py-1 rounded-xl bg-[#E5E5E5]/60 dark:bg-[#37464F] text-[#777777] dark:text-[#9CA3AF]">
            {getQuestionTypeLabel()}
          </span>
          {question.categoryHint && (
            <span className="text-xs font-bold text-[#AFAFAF] dark:text-[#64748B] hidden sm:inline">
              • {question.categoryHint}
            </span>
          )}
        </div>

        <button
          onClick={() => toggleBookmark(question.article)}
          className={cn(
            'p-2 rounded-xl border-2 transition-all flex items-center gap-1.5 text-xs font-bold',
            isArticleSaved
              ? 'bg-[#FFF7ED] dark:bg-[#78350F]/30 border-[#FED7AA] dark:border-[#92400E] text-[#FF9600]'
              : 'bg-white dark:bg-[#1E2D34] border-[#E5E5E5] dark:border-[#37464F] text-[#777777] dark:text-[#9CA3AF] hover:border-[#D5D5D5]'
          )}
          title={isArticleSaved ? t('saved.remove') : t('trainer.save_article')}
        >
          <Bookmark className={cn('w-4 h-4', isArticleSaved && 'fill-[#FF9600]')} />
          <span className="hidden sm:inline">
            {isArticleSaved ? t('saved.saved_label') : t('common.save')}
          </span>
        </button>
      </div>

      {/* Main Question Prompt */}
      <motion.div
        key={question.id + '_prompt'}
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        className="space-y-4"
      >
        <h2 className="text-xl sm:text-2xl font-extrabold text-[#3C3C3C] dark:text-white leading-snug">
          {question.prompt}
        </h2>

        {/* Optional Article Image Card */}
        {question.article.thumbnail && (
          <div className="relative w-full max-h-48 overflow-hidden rounded-2xl border-2 border-[#E5E5E5] dark:border-[#37464F] bg-[#F7F7F7] dark:bg-[#1E2D34] flex items-center justify-center">
            <img
              src={question.article.thumbnail.source}
              alt={question.article.title}
              className="max-h-48 w-auto object-contain hover:scale-105 transition-transform duration-300"
              loading="lazy"
            />
            <a
              href={question.sourceUrl}
              target="_blank"
              rel="noopener noreferrer"
              className="absolute bottom-2 right-2 bg-black/60 hover:bg-black/80 text-white text-[11px] font-bold px-2.5 py-1 rounded-lg flex items-center gap-1 backdrop-blur-sm transition-all"
            >
              Wikipedia <ExternalLink className="w-3 h-3" />
            </a>
          </div>
        )}

        {/* Cloze Sentence Context Box */}
        {question.type === 'cloze' && question.clozeContext && (
          <div className="p-4 rounded-2xl bg-white dark:bg-[#1E2D34] border-2 border-[#E5E5E5] dark:border-[#37464F] shadow-sm text-base sm:text-lg font-medium text-[#4B4B4B] dark:text-[#E5E7EB] leading-relaxed">
            <span>{question.clozeContext.before} </span>
            <span className="inline-block px-3 py-1 bg-[#DDF4FF] dark:bg-[#1CB0F6]/20 border-2 border-[#1CB0F6] border-dashed rounded-xl font-bold text-[#0C70A2] dark:text-[#38BDF8]">
              {selectedOptionId
                ? question.options.find((o) => o.id === selectedOptionId)?.text
                : '_______'}
            </span>
            <span> {question.clozeContext.after}</span>
          </div>
        )}

        {/* Subject recognition snippet */}
        {question.type === 'subject_recognition' && question.clozeContext?.before && (
          <div className="p-4 rounded-2xl bg-white dark:bg-[#1E2D34] border-2 border-[#E5E5E5] dark:border-[#37464F] text-base font-medium text-[#4B4B4B] dark:text-[#E5E7EB] italic border-l-4 border-l-[#1CB0F6]">
            "{question.clozeContext.before}"
          </div>
        )}
      </motion.div>

      {/* Answer Options Grid */}
      <motion.div
        key={question.id + '_options'}
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 0.1 }}
        className="grid grid-cols-1 sm:grid-cols-2 gap-3"
      >
        {question.options.map((option, idx) => {
          const isSelected = selectedOptionId === option.id;
          const isChecked = feedbackStatus !== 'idle';
          const isCorrect = option.id === question.correctOptionId;

          let optionStyle = 'bg-white dark:bg-[#1E2D34] border-[#E5E5E5] dark:border-[#37464F] text-[#3C3C3C] dark:text-white hover:bg-[#F9F9F9] dark:hover:bg-[#25363F] hover:border-[#D5D5D5] dark:hover:border-[#4B5E6B]';

          if (isSelected && !isChecked) {
            optionStyle = 'bg-[#DDF4FF] dark:bg-[#1CB0F6]/20 border-[#1CB0F6] text-[#0C70A2] dark:text-[#38BDF8] shadow-[0_4px_0_#1899D6]';
          } else if (isChecked) {
            if (isCorrect) {
              optionStyle = 'bg-[#D7FFB8] dark:bg-[#052E16] border-[#58CC02] text-[#2A7000] dark:text-[#86EFAC] shadow-[0_4px_0_#46A302]';
            } else if (isSelected && !isCorrect) {
              optionStyle = 'bg-[#FFDFE0] dark:bg-[#4C0519] border-[#FF4B4B] text-[#B91C1C] dark:text-[#FECDD3] shadow-[0_4px_0_#EA2B2B]';
            } else {
              optionStyle = 'bg-white/50 dark:bg-[#1E2D34]/50 border-[#E5E5E5] dark:border-[#37464F] text-[#AFAFAF] dark:text-gray-600 opacity-50';
            }
          }

          return (
            <button
              key={option.id}
              onClick={() => selectOption(option.id)}
              disabled={isChecked}
              className={cn(
                'relative flex items-center justify-between p-4 rounded-2xl border-2 border-b-4 text-left font-bold transition-all min-h-[64px]',
                'active:translate-y-[2px] active:border-b-2 focus:outline-none focus-visible:ring-2 focus-visible:ring-[#1CB0F6]',
                optionStyle
              )}
            >
              <div className="flex items-center gap-3 pr-2">
                {/* Option letter/number badge */}
                <span
                  className={cn(
                    'w-7 h-7 flex items-center justify-center text-xs font-black rounded-lg border-2 shrink-0 transition-colors',
                    isSelected && !isChecked
                      ? 'bg-[#1CB0F6] text-white border-[#1899D6]'
                      : isChecked && isCorrect
                      ? 'bg-[#58CC02] text-white border-[#46A302]'
                      : isChecked && isSelected && !isCorrect
                      ? 'bg-[#FF4B4B] text-white border-[#EA2B2B]'
                      : 'bg-[#F7F7F7] text-[#777777] border-[#E5E5E5]'
                  )}
                >
                  {idx + 1}
                </span>
                <span className="text-sm sm:text-base leading-snug">
                  {option.text}
                </span>
              </div>

              {/* Status icon once checked */}
              {isChecked && isCorrect && (
                <CheckCircle2 className="w-6 h-6 text-[#58CC02] shrink-0 fill-[#58CC02]/20" />
              )}
              {isChecked && isSelected && !isCorrect && (
                <XCircle className="w-6 h-6 text-[#FF4B4B] shrink-0 fill-[#FF4B4B]/20" />
              )}
            </button>
          );
        })}
      </motion.div>
    </div>
  );
};
