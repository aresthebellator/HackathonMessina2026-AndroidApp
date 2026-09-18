import { useEffect } from 'react';
import { useQuizStore } from '@/store/useQuizStore';
import { HomeDashboard } from '@/components/home/HomeDashboard';
import { QuizInteractive } from '@/components/quiz/QuizInteractive';
import { FullScreenLoader } from '@/components/common/FullScreenLoader';
import { SettingsModal } from '@/components/common/SettingsModal';

export function App() {
  const currentRound = useQuizStore((s) => s.currentRound);
  const isDarkMode = useQuizStore((s) => s.isDarkMode);
  const fontSize = useQuizStore((s) => s.fontSize);
  const highContrast = useQuizStore((s) => s.highContrast);

  // Sync dark mode class with HTML root
  useEffect(() => {
    if (typeof document !== 'undefined') {
      document.documentElement.classList.toggle('dark', isDarkMode);
    }
  }, [isDarkMode]);

  const fontSizeClass =
    fontSize === 'large'
      ? 'text-lg'
      : fontSize === 'extra'
      ? 'text-xl'
      : 'text-base';

  return (
    <div
      className={`min-h-screen bg-[#F7F7F7] dark:bg-[#131F24] text-[#3C3C3C] dark:text-[#F7F7F7] font-sans antialiased transition-colors duration-200 ${fontSizeClass} ${
        highContrast ? 'contrast-125' : ''
      }`}
    >
      {/* Dynamic Screen (Dashboard vs Interactive Quiz) */}
      {currentRound ? <QuizInteractive /> : <HomeDashboard />}

      {/* Full-screen Loading Screen with Rotating Wikipedia Fun Facts */}
      <FullScreenLoader />

      {/* Accessibility & Theme Settings Modal */}
      <SettingsModal />
    </div>
  );
}

export default App;
