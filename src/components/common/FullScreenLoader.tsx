import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Lightbulb, Compass, ArrowRight } from 'lucide-react';
import { useQuizStore } from '@/store/useQuizStore';
import { getUnitForLesson, getLessonTitle, getUnitLocalizedTitle, getUnitLocalizedTopic } from '@/lib/unitsData';
import { useTranslation } from '@/lib/i18n';

const WIKIPEDIA_FUN_FACTS_IT = [
  {
    fact: "Wikipedia in lingua italiana conta oltre 1,8 milioni di voci, scritte e curate quotidianamente da migliaia di volontari!",
    topic: "Enciclopedia Libera"
  },
  {
    fact: "Il Colosseo a Roma poteva essere allagato per ospitare le 'naumachie', spettacolari simulazioni di battaglie navali.",
    topic: "Roma Antica"
  },
  {
    fact: "Un giorno sul pianeta Venere dura più di un intero anno venusiano: ruota su se stesso molto lentamente!",
    topic: "Astronomia"
  },
  {
    fact: "La prima persona a scrivere un algoritmo per un computer fu Ada Lovelace nel 1843 per la Macchina Analitica.",
    topic: "Storia dell'Informatica"
  },
  {
    fact: "I polpi possiedono tre cuori, sangue blu ricco di emocianina a base di rame e neuroni distribuiti su tutti i tentacoli.",
    topic: "Biologia Marina"
  },
  {
    fact: "Il miele puro commestibile può durare millenni: vasi rinvenuti intatti nelle tombe dei faraoni egizi erano ancora intatti.",
    topic: "Archeologia"
  },
  {
    fact: "La luce del Sole impiega circa 8 minuti e 20 secondi per compiere i 150 milioni di chilometri fino alla Terra.",
    topic: "Fisica dello Spazio"
  },
  {
    fact: "Leonardo da Vinci redigeva quasi tutti i suoi taccuini personali scrivendo a specchio da destra verso sinistra.",
    topic: "Rinascimento"
  },
  {
    fact: "La Grande Barriera Corallina in Australia è la più estesa struttura formata da organismi viventi, visibile anche dallo spazio.",
    topic: "Geografia & Natura"
  },
  {
    fact: "La Biblioteca di Alessandria d'Egitto conservava oltre mezzo milione di rotoli di papiro con tutto il sapere dell'antichità.",
    topic: "Grandi Civiltà"
  }
];

const WIKIPEDIA_FUN_FACTS_EN = [
  {
    fact: "English Wikipedia has over 6.9 million articles curated daily by tens of thousands of active volunteers around the world!",
    topic: "Free Encyclopedia"
  },
  {
    fact: "The Roman Colosseum could be flooded to stage 'naumachiae'—spectacular simulated naval battles.",
    topic: "Ancient Rome"
  },
  {
    fact: "A single day on Venus lasts longer than an entire Venusian year because it rotates so slowly!",
    topic: "Astronomy"
  },
  {
    fact: "Ada Lovelace wrote the world's first computer algorithm in 1843 for Charles Babbage's Analytical Engine.",
    topic: "Computer History"
  },
  {
    fact: "Octopuses have three hearts, copper-based blue blood, and two-thirds of their neurons are in their arms.",
    topic: "Marine Biology"
  },
  {
    fact: "Pure honey never spoils: edible pots of honey found in ancient Egyptian tombs are thousands of years old.",
    topic: "Archaeology"
  },
  {
    fact: "Sunlight takes about 8 minutes and 20 seconds to travel 150 million kilometers to reach the Earth.",
    topic: "Space Physics"
  },
  {
    fact: "Leonardo da Vinci wrote almost all of his personal notebooks in mirror writing from right to left.",
    topic: "Renaissance"
  },
  {
    fact: "Australia's Great Barrier Reef is the largest living structure on Earth, visible even from outer space.",
    topic: "Geography & Nature"
  },
  {
    fact: "The Library of Alexandria housed over half a million papyrus scrolls preserving the ancient world's knowledge.",
    topic: "Great Civilizations"
  }
];

export const FullScreenLoader: React.FC = () => {
  const isLoadingRound = useQuizStore((s) => s.isLoadingRound);
  const loadingLessonNumber = useQuizStore((s) => s.loadingLessonNumber);
  const language = useQuizStore((s) => s.language);
  const { t } = useTranslation();
  const [factIndex, setFactIndex] = useState(0);

  const facts = language === 'en' ? WIKIPEDIA_FUN_FACTS_EN : WIKIPEDIA_FUN_FACTS_IT;

  // Rotate fun facts smoothly every 3.2 seconds
  useEffect(() => {
    if (!isLoadingRound) return;
    const interval = setInterval(() => {
      setFactIndex((prev) => (prev + 1) % facts.length);
    }, 3200);
    return () => clearInterval(interval);
  }, [isLoadingRound, facts.length]);

  const currentFact = facts[factIndex % facts.length];
  const lessonTitle = loadingLessonNumber ? getLessonTitle(loadingLessonNumber, language) : null;
  const unit = loadingLessonNumber ? getUnitForLesson(loadingLessonNumber) : null;

  return (
    <AnimatePresence>
      {isLoadingRound && (
        <motion.div
          key="full-screen-loader"
          initial={{ opacity: 0, scale: 0.96 }}
          animate={{ opacity: 1, scale: 1 }}
          exit={{ opacity: 0, scale: 0.98 }}
          transition={{ duration: 0.32, ease: [0.16, 1, 0.3, 1] }}
          className="fixed inset-0 z-50 bg-[#F7F7F7] dark:bg-[#131F24] flex flex-col items-center justify-between p-6 select-none overflow-hidden"
        >
          {/* Top Unit Tag */}
          <motion.div
            initial={{ opacity: 0, y: -10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.3, delay: 0.05 }}
            className="w-full flex justify-center pt-6"
          >
            {unit && (
              <span
                className="text-xs font-black uppercase tracking-wider px-3.5 py-1.5 rounded-2xl text-white shadow-xs"
                style={{ backgroundColor: unit.theme.primary }}
              >
                {getUnitLocalizedTitle(unit, language)} • {getUnitLocalizedTopic(unit, language)}
              </span>
            )}
          </motion.div>

          {/* Center Loader Spinner & Loading Text */}
          <div className="w-full max-w-sm flex flex-col items-center space-y-6">
            {/* Elegant Circular Loader Spinner */}
            <motion.div
              initial={{ scale: 0.7, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              transition={{ delay: 0.1, duration: 0.35, ease: 'easeOut' }}
              className="relative flex items-center justify-center"
            >
              {/* Soft ambient aura */}
              <div className="absolute w-24 h-24 rounded-full bg-[#58CC02]/20 dark:bg-[#58CC02]/25 blur-xl animate-pulse" />

              {/* Smooth Dual-Ring Circular Spinner */}
              <svg
                className="w-16 h-16 sm:w-20 sm:h-20 animate-spin text-[#58CC02]"
                viewBox="0 0 50 50"
                fill="none"
              >
                <circle
                  className="stroke-[#E5E5E5] dark:stroke-[#2B3940]"
                  cx="25"
                  cy="25"
                  r="20"
                  strokeWidth="4.5"
                />
                <circle
                  className="stroke-current"
                  cx="25"
                  cy="25"
                  r="20"
                  strokeWidth="4.5"
                  strokeDasharray="95 150"
                  strokeLinecap="round"
                />
              </svg>
            </motion.div>

            {/* Loading text with animated ellipsis */}
            <motion.div
              initial={{ opacity: 0, y: 10 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.15, duration: 0.3 }}
              className="space-y-1.5 text-center"
            >
              <h2 className="text-xl sm:text-2xl font-black text-[#3C3C3C] dark:text-white flex items-center justify-center gap-1.5">
                <span>{t('loader.loading_lesson')}</span>
                <span className="inline-flex tracking-widest text-[#58CC02]">
                  <motion.span
                    animate={{ opacity: [0, 1, 0] }}
                    transition={{ duration: 1.2, repeat: Infinity, delay: 0 }}
                  >
                    .
                  </motion.span>
                  <motion.span
                    animate={{ opacity: [0, 1, 0] }}
                    transition={{ duration: 1.2, repeat: Infinity, delay: 0.2 }}
                  >
                    .
                  </motion.span>
                  <motion.span
                    animate={{ opacity: [0, 1, 0] }}
                    transition={{ duration: 1.2, repeat: Infinity, delay: 0.4 }}
                  >
                    .
                  </motion.span>
                </span>
              </h2>
              {lessonTitle ? (
                <p className="text-sm font-extrabold text-[#58CC02] dark:text-[#61E002]">
                  "{lessonTitle}"
                </p>
              ) : (
                <p className="text-xs font-bold text-[#777777] dark:text-[#93A5AF]">
                  {t('loader.extracting_questions')}
                </p>
              )}
            </motion.div>
          </div>

          {/* Bottom: Rotating Wikipedia Fun Facts Box */}
          <motion.div
            initial={{ opacity: 0, y: 16 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.2, duration: 0.35 }}
            className="w-full max-w-md pb-6"
          >
            <div className="bg-white dark:bg-[#1E2D34] rounded-3xl border-2 border-b-4 border-[#E5E5E5] dark:border-[#37464F] p-4 sm:p-5 shadow-lg relative">
              <div className="flex items-center justify-between mb-2">
                <div className="flex items-center gap-1.5 text-[#FF9600] font-black text-xs uppercase tracking-wider">
                  <Lightbulb className="w-4 h-4 fill-[#FF9600]" />
                  <span>{t('loader.did_you_know')}</span>
                </div>
                <span className="text-[10px] font-bold text-[#AFAFAF] dark:text-[#777777] flex items-center gap-1">
                  <Compass className="w-3 h-3" /> {currentFact.topic}
                </span>
              </div>

              <div className="min-h-[64px] flex items-center">
                <AnimatePresence mode="wait">
                  <motion.p
                    key={factIndex}
                    initial={{ opacity: 0, y: 8 }}
                    animate={{ opacity: 1, y: 0 }}
                    exit={{ opacity: 0, y: -8 }}
                    transition={{ duration: 0.3 }}
                    className="text-xs sm:text-sm font-bold text-[#4B4B4B] dark:text-[#D1D5DB] leading-relaxed"
                  >
                    "{currentFact.fact}"
                  </motion.p>
                </AnimatePresence>
              </div>

              {/* Dots progression */}
              <div className="flex items-center justify-between pt-3 border-t border-gray-100 dark:border-[#37464F]/60 mt-2">
                <div className="flex items-center gap-1">
                  {facts.slice(0, 5).map((_, idx) => (
                    <div
                      key={idx}
                      className={`w-2 h-2 rounded-full transition-all ${
                        idx === factIndex % 5
                          ? 'bg-[#58CC02] w-4'
                          : 'bg-gray-200 dark:bg-gray-600'
                      }`}
                    />
                  ))}
                </div>

                <button
                  onClick={() => setFactIndex((prev) => (prev + 1) % facts.length)}
                  className="text-[11px] font-black text-[#1CB0F6] hover:text-[#0C70A2] flex items-center gap-1 transition-colors"
                >
                  <span>{t('loader.next_fact')}</span>
                  <ArrowRight className="w-3 h-3" />
                </button>
              </div>
            </div>
          </motion.div>
        </motion.div>
      )}
    </AnimatePresence>
  );
};
