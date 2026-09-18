import React from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { X, Moon, Sun, Type, Eye, Volume2, VolumeX, Keyboard, RefreshCw, Sparkles } from 'lucide-react';
import { useQuizStore } from '@/store/useQuizStore';
import { Button } from '@/components/ui/Button';

export const SettingsModal: React.FC = () => {
  const isSettingsOpen = useQuizStore((s) => s.isSettingsOpen);
  const closeSettings = useQuizStore((s) => s.closeSettings);
  const isDarkMode = useQuizStore((s) => s.isDarkMode);
  const toggleDarkMode = useQuizStore((s) => s.toggleDarkMode);
  const fontSize = useQuizStore((s) => s.fontSize);
  const setFontSize = useQuizStore((s) => s.setFontSize);
  const reducedMotion = useQuizStore((s) => s.reducedMotion);
  const toggleReducedMotion = useQuizStore((s) => s.toggleReducedMotion);
  const highContrast = useQuizStore((s) => s.highContrast);
  const toggleHighContrast = useQuizStore((s) => s.toggleHighContrast);
  const isSoundEnabled = useQuizStore((s) => s.isSoundEnabled);
  const toggleSound = useQuizStore((s) => s.toggleSound);
  const lives = useQuizStore((s) => s.lives);
  const restoreLives = useQuizStore((s) => s.restoreLives);

  if (!isSettingsOpen) return null;

  return (
    <AnimatePresence>
      <div className="fixed inset-0 z-50 bg-black/50 backdrop-blur-xs flex items-center justify-center p-4">
        <motion.div
          initial={{ opacity: 0, scale: 0.95, y: 15 }}
          animate={{ opacity: 1, scale: 1, y: 0 }}
          exit={{ opacity: 0, scale: 0.95, y: 15 }}
          className="w-full max-w-md bg-white dark:bg-[#1E2D34] rounded-3xl border-2 border-[#E5E5E5] dark:border-[#37464F] shadow-2xl overflow-hidden flex flex-col max-h-[90vh]"
        >
          {/* Header */}
          <div className="px-6 py-4 border-b-2 border-[#E5E5E5] dark:border-[#37464F] flex items-center justify-between">
            <h2 className="text-lg font-black text-[#3C3C3C] dark:text-white flex items-center gap-2">
              <span>Impostazioni & Accessibilità</span>
            </h2>
            <button
              onClick={closeSettings}
              className="p-1.5 rounded-xl text-[#AFAFAF] hover:text-[#3C3C3C] dark:hover:text-white hover:bg-black/5 dark:hover:bg-white/5 transition-colors"
            >
              <X className="w-5 h-5" />
            </button>
          </div>

          {/* Settings List */}
          <div className="p-6 overflow-y-auto space-y-6 flex-1 divide-y divide-gray-100 dark:divide-[#37464F]">
            {/* Dark Mode Toggle */}
            <div className="flex items-center justify-between pt-1">
              <div className="space-y-0.5">
                <div className="flex items-center gap-2 font-black text-sm text-[#3C3C3C] dark:text-white">
                  {isDarkMode ? <Moon className="w-4 h-4 text-[#CE82FF]" /> : <Sun className="w-4 h-4 text-[#FFC800]" />}
                  <span>Tema Scuro (Dark Mode)</span>
                </div>
                <p className="text-xs text-[#777777] dark:text-[#93A5AF]">
                  Palette ad alto contrasto per riposare la vista
                </p>
              </div>

              <button
                onClick={toggleDarkMode}
                className={`w-14 h-8 flex items-center rounded-full p-1 transition-colors border-2 ${
                  isDarkMode
                    ? 'bg-[#58CC02] border-[#46A302] justify-end'
                    : 'bg-[#E5E5E5] border-[#CECECE] justify-start'
                }`}
                role="switch"
                aria-checked={isDarkMode}
              >
                <div className="bg-white w-5 h-5 rounded-full shadow-md" />
              </button>
            </div>

            {/* Font Size Selector */}
            <div className="pt-4 space-y-2">
              <div className="flex items-center gap-2 font-black text-sm text-[#3C3C3C] dark:text-white">
                <Type className="w-4 h-4 text-[#1CB0F6]" />
                <span>Dimensione del Testo</span>
              </div>
              <div className="grid grid-cols-3 gap-2">
                {(['normal', 'large', 'extra'] as const).map((size) => (
                  <button
                    key={size}
                    onClick={() => setFontSize(size)}
                    className={`py-2 rounded-xl text-xs font-black border-2 transition-all ${
                      fontSize === size
                        ? 'bg-[#DDF4FF] dark:bg-[#1CB0F6]/20 border-[#1CB0F6] text-[#0C70A2] dark:text-[#38BDF8]'
                        : 'bg-[#F7F7F7] dark:bg-[#131F24] border-[#E5E5E5] dark:border-[#37464F] text-[#777777] dark:text-[#93A5AF]'
                    }`}
                  >
                    {size === 'normal' ? 'Normale' : size === 'large' ? 'Grande' : 'Molto Grande'}
                  </button>
                ))}
              </div>
            </div>

            {/* Sound Effects */}
            <div className="flex items-center justify-between pt-4">
              <div className="space-y-0.5">
                <div className="flex items-center gap-2 font-black text-sm text-[#3C3C3C] dark:text-white">
                  {isSoundEnabled ? <Volume2 className="w-4 h-4 text-[#58CC02]" /> : <VolumeX className="w-4 h-4 text-[#FF4B4B]" />}
                  <span>Effetti Sonori Tattili</span>
                </div>
                <p className="text-xs text-[#777777] dark:text-[#93A5AF]">
                  Suoni sintetizzati Web Audio API a latenza zero
                </p>
              </div>

              <button
                onClick={toggleSound}
                className={`w-14 h-8 flex items-center rounded-full p-1 transition-colors border-2 ${
                  isSoundEnabled
                    ? 'bg-[#58CC02] border-[#46A302] justify-end'
                    : 'bg-[#E5E5E5] border-[#CECECE] justify-start'
                }`}
                role="switch"
                aria-checked={isSoundEnabled}
              >
                <div className="bg-white w-5 h-5 rounded-full shadow-md" />
              </button>
            </div>

            {/* Reduced Motion Toggle */}
            <div className="flex items-center justify-between pt-4">
              <div className="space-y-0.5">
                <div className="flex items-center gap-2 font-black text-sm text-[#3C3C3C] dark:text-white">
                  <Sparkles className="w-4 h-4 text-[#FF9600]" />
                  <span>Riduci Animazioni</span>
                </div>
                <p className="text-xs text-[#777777] dark:text-[#93A5AF]">
                  Minimizza i movimenti veloci e transizioni
                </p>
              </div>

              <button
                onClick={toggleReducedMotion}
                className={`w-14 h-8 flex items-center rounded-full p-1 transition-colors border-2 ${
                  reducedMotion
                    ? 'bg-[#58CC02] border-[#46A302] justify-end'
                    : 'bg-[#E5E5E5] border-[#CECECE] justify-start'
                }`}
                role="switch"
                aria-checked={reducedMotion}
              >
                <div className="bg-white w-5 h-5 rounded-full shadow-md" />
              </button>
            </div>

            {/* High Contrast Mode */}
            <div className="flex items-center justify-between pt-4">
              <div className="space-y-0.5">
                <div className="flex items-center gap-2 font-black text-sm text-[#3C3C3C] dark:text-white">
                  <Eye className="w-4 h-4 text-[#00CD9C]" />
                  <span>Alto Contrasto Visivo</span>
                </div>
                <p className="text-xs text-[#777777] dark:text-[#93A5AF]">
                  Bordi rinforzati e contrasti conformi WCAG AAA
                </p>
              </div>

              <button
                onClick={toggleHighContrast}
                className={`w-14 h-8 flex items-center rounded-full p-1 transition-colors border-2 ${
                  highContrast
                    ? 'bg-[#58CC02] border-[#46A302] justify-end'
                    : 'bg-[#E5E5E5] border-[#CECECE] justify-start'
                }`}
                role="switch"
                aria-checked={highContrast}
              >
                <div className="bg-white w-5 h-5 rounded-full shadow-md" />
              </button>
            </div>

            {/* Keyboard Shortcuts Reference */}
            <div className="pt-4 space-y-2">
              <div className="flex items-center gap-2 font-black text-sm text-[#3C3C3C] dark:text-white">
                <Keyboard className="w-4 h-4 text-[#777777]" />
                <span>Scorciatoie Rapide da Tastiera</span>
              </div>
              <div className="bg-[#F7F7F7] dark:bg-[#131F24] p-3 rounded-2xl border border-[#E5E5E5] dark:border-[#37464F] text-xs space-y-1 text-[#4B4B4B] dark:text-[#93A5AF]">
                <div className="flex items-center justify-between">
                  <span>Seleziona opzione:</span>
                  <span className="font-mono font-bold bg-white dark:bg-[#1E2D34] px-2 py-0.5 rounded-md border border-gray-200 dark:border-gray-700">1, 2, 3, 4</span>
                </div>
                <div className="flex items-center justify-between">
                  <span>Verifica risposta / Avanza:</span>
                  <span className="font-mono font-bold bg-white dark:bg-[#1E2D34] px-2 py-0.5 rounded-md border border-gray-200 dark:border-gray-700">Invio (Enter)</span>
                </div>
              </div>
            </div>

            {/* Heart Recovery */}
            {lives < 5 && (
              <div className="pt-4 flex items-center justify-between">
                <div className="space-y-0.5">
                  <span className="font-black text-sm text-[#FF4B4B]">
                    Hai {lives} vite rimaste
                  </span>
                  <p className="text-xs text-[#777777] dark:text-[#93A5AF]">
                    Ricarica istantaneamente il serbatoio cuori
                  </p>
                </div>
                <Button
                  variant="coral"
                  size="sm"
                  onClick={restoreLives}
                  className="flex items-center gap-1.5"
                >
                  <RefreshCw className="w-3.5 h-3.5" />
                  <span>Ricarica a 5</span>
                </Button>
              </div>
            )}
          </div>

          {/* Footer */}
          <div className="p-4 border-t-2 border-[#E5E5E5] dark:border-[#37464F] bg-[#F7F7F7] dark:bg-[#131F24]">
            <Button
              variant="outline"
              size="md"
              fullWidth
              onClick={closeSettings}
              className="dark:bg-[#1E2D34] dark:text-white dark:border-[#37464F]"
            >
              Salva e Chiudi
            </Button>
          </div>
        </motion.div>
      </div>
    </AnimatePresence>
  );
};
