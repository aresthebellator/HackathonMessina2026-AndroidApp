import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { X, Mail, Lock, UserCheck, AlertCircle, LogOut } from 'lucide-react';
import { useQuizStore } from '@/store/useQuizStore';
import { useTranslation } from '@/lib/i18n';
import { Button } from '@/components/ui/Button';

/**
 * Authentication and Sync Modal ported from android-app/presentation/auth/AuthScreen.kt
 */
export const AuthModal: React.FC = () => {
  const isAuthOpen = useQuizStore((s) => s.isAuthOpen);
  const closeAuth = useQuizStore((s) => s.closeAuth);
  const userEmail = useQuizStore((s) => s.userEmail);
  const isAuthenticated = useQuizStore((s) => s.isAuthenticated);
  const setAuthenticatedUser = useQuizStore((s) => s.setAuthenticatedUser);
  const { t } = useTranslation();

  const [isRegistering, setIsRegistering] = useState(false);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  if (!isAuthOpen) return null;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!email || !email.includes('@') || password.length < 6) {
      setErrorMessage(t('auth.invalid_credentials'));
      return;
    }

    setIsLoading(true);
    setErrorMessage(null);

    // Simulate authentication / cloud sync
    setTimeout(() => {
      setIsLoading(false);
      setAuthenticatedUser(email.trim());
      closeAuth();
    }, 700);
  };

  const handleSignOut = () => {
    setAuthenticatedUser(null);
    setEmail('');
    setPassword('');
  };

  return (
    <AnimatePresence>
      <div className="fixed inset-0 z-50 bg-black/60 backdrop-blur-xs flex items-center justify-center p-4">
        <motion.div
          initial={{ opacity: 0, scale: 0.9, y: 15 }}
          animate={{ opacity: 1, scale: 1, y: 0 }}
          exit={{ opacity: 0, scale: 0.9, y: 15 }}
          className="w-full max-w-sm bg-white dark:bg-[#1E2D34] rounded-3xl border-2 border-[#E5E5E5] dark:border-[#37464F] p-6 text-center space-y-5 shadow-2xl relative"
        >
          {/* Close button */}
          <button
            onClick={closeAuth}
            className="absolute top-4 right-4 p-1.5 rounded-xl text-[#AFAFAF] hover:text-[#3C3C3C] dark:hover:text-white hover:bg-black/5 dark:hover:bg-white/5 transition-colors"
            aria-label={t('common.close')}
          >
            <X className="w-5 h-5" />
          </button>

          {/* Viking Avatar */}
          <div className="flex justify-center pt-2">
            <div className="relative">
              <img
                src="/ic_launcher_viking.png"
                alt="Wikingo"
                className="w-20 h-20 object-contain drop-shadow-md select-none"
              />
            </div>
          </div>

          {isAuthenticated ? (
            /* Signed-in profile view */
            <div className="space-y-4 py-2">
              <div className="w-12 h-12 mx-auto rounded-full bg-[#D7FFB8] dark:bg-[#14532D] flex items-center justify-center text-[#2A7000] dark:text-[#86EFAC]">
                <UserCheck className="w-6 h-6" />
              </div>
              <div className="space-y-1">
                <h3 className="text-lg font-black text-[#3C3C3C] dark:text-white">
                  {t('auth.logged_in_as')}
                </h3>
                <p className="text-sm font-extrabold text-[#58CC02] break-all">
                  {userEmail}
                </p>
                <p className="text-xs text-[#777777] dark:text-[#9CA3AF] pt-1">
                  I tuoi progressi e le tue voci salvate sono sincronizzati.
                </p>
              </div>

              <div className="space-y-2 pt-2">
                <Button
                  variant="outline"
                  size="md"
                  fullWidth
                  onClick={handleSignOut}
                  className="flex items-center justify-center gap-2 text-[#FF4B4B] hover:bg-[#FFF1F2] dark:hover:bg-[#4C0519]"
                >
                  <LogOut className="w-4 h-4" />
                  <span>Esci dall'account</span>
                </Button>
                <Button variant="green" size="md" fullWidth onClick={closeAuth}>
                  {t('common.continue')}
                </Button>
              </div>
            </div>
          ) : (
            /* Login / Registration Form */
            <form onSubmit={handleSubmit} className="space-y-4">
              <div className="space-y-1">
                <h3 className="text-xl font-black text-[#3C3C3C] dark:text-white">
                  {isRegistering ? t('auth.register_title') : t('auth.login_title')}
                </h3>
                <p className="text-xs text-[#777777] dark:text-[#9CA3AF]">
                  {t('auth.subtitle')}
                </p>
              </div>

              {/* Mode hint */}
              <div className="text-xs font-bold text-[#1CB0F6] bg-[#F0F9FF] dark:bg-[#0C4A6E]/30 p-2 rounded-xl border border-[#BAE6FD] dark:border-[#0284C7]">
                {isRegistering ? t('auth.register_hint') : t('auth.login_hint')}
              </div>

              {/* Inputs */}
              <div className="space-y-2.5 text-left">
                <div>
                  <label className="block text-[11px] font-black uppercase tracking-wider text-[#777777] dark:text-[#9CA3AF] mb-1">
                    {t('auth.email')}
                  </label>
                  <div className="relative">
                    <Mail className="w-4 h-4 text-gray-400 absolute left-3 top-1/2 -translate-y-1/2" />
                    <input
                      type="email"
                      value={email}
                      onChange={(e) => {
                        setEmail(e.target.value);
                        setErrorMessage(null);
                      }}
                      placeholder="es. nome@email.com"
                      className="w-full pl-9 pr-3 py-2.5 rounded-xl border-2 border-[#E5E5E5] dark:border-[#37464F] bg-white dark:bg-[#131F24] text-sm font-bold text-[#3C3C3C] dark:text-white focus:outline-none focus:border-[#1CB0F6]"
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-[11px] font-black uppercase tracking-wider text-[#777777] dark:text-[#9CA3AF] mb-1">
                    {t('auth.password')}
                  </label>
                  <div className="relative">
                    <Lock className="w-4 h-4 text-gray-400 absolute left-3 top-1/2 -translate-y-1/2" />
                    <input
                      type="password"
                      value={password}
                      onChange={(e) => {
                        setPassword(e.target.value);
                        setErrorMessage(null);
                      }}
                      placeholder="Almeno 6 caratteri"
                      className="w-full pl-9 pr-3 py-2.5 rounded-xl border-2 border-[#E5E5E5] dark:border-[#37464F] bg-white dark:bg-[#131F24] text-sm font-bold text-[#3C3C3C] dark:text-white focus:outline-none focus:border-[#1CB0F6]"
                    />
                  </div>
                </div>
              </div>

              {/* Error message */}
              {errorMessage && (
                <div className="flex items-center gap-1.5 text-xs font-bold text-[#FF4B4B] bg-[#FFF1F2] dark:bg-[#4C0519]/30 p-2.5 rounded-xl border border-[#FFDFE0] dark:border-[#881337] text-left">
                  <AlertCircle className="w-4 h-4 shrink-0" />
                  <span>{errorMessage}</span>
                </div>
              )}

              {/* Submit CTA */}
              <Button
                variant="green"
                size="lg"
                fullWidth
                type="submit"
                disabled={isLoading}
              >
                {isLoading ? (
                  <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin mx-auto" />
                ) : isRegistering ? (
                  t('auth.register_action')
                ) : (
                  t('auth.login_action')
                )}
              </Button>

              {/* Mode switch */}
              <div className="pt-2 text-xs font-bold text-[#777777] dark:text-[#9CA3AF] flex items-center justify-center gap-1">
                <span>{isRegistering ? t('auth.have_account') : t('auth.no_account')}</span>
                <button
                  type="button"
                  onClick={() => {
                    setIsRegistering(!isRegistering);
                    setErrorMessage(null);
                  }}
                  className="text-[#1CB0F6] hover:underline font-black"
                >
                  {isRegistering ? t('auth.login_action') : t('auth.register_action')}
                </button>
              </div>
            </form>
          )}
        </motion.div>
      </div>
    </AnimatePresence>
  );
};
