import { describe, it, expect, beforeEach } from 'vitest';
import { useQuizStore } from '@/store/useQuizStore';

describe('Settings & Theme Store State', () => {
  beforeEach(() => {
    useQuizStore.setState({
      isDarkMode: false,
      fontSize: 'normal',
      reducedMotion: false,
      highContrast: false,
      isSettingsOpen: false,
      isLoadingRound: false,
      loadingLessonNumber: null,
    });
  });

  it('should toggle dark mode state correctly', () => {
    expect(useQuizStore.getState().isDarkMode).toBe(false);

    useQuizStore.getState().toggleDarkMode();
    expect(useQuizStore.getState().isDarkMode).toBe(true);

    useQuizStore.getState().toggleDarkMode();
    expect(useQuizStore.getState().isDarkMode).toBe(false);
  });

  it('should update font size properly', () => {
    expect(useQuizStore.getState().fontSize).toBe('normal');

    useQuizStore.getState().setFontSize('large');
    expect(useQuizStore.getState().fontSize).toBe('large');

    useQuizStore.getState().setFontSize('extra');
    expect(useQuizStore.getState().fontSize).toBe('extra');
  });

  it('should toggle reduced motion and high contrast', () => {
    expect(useQuizStore.getState().reducedMotion).toBe(false);
    useQuizStore.getState().toggleReducedMotion();
    expect(useQuizStore.getState().reducedMotion).toBe(true);

    expect(useQuizStore.getState().highContrast).toBe(false);
    useQuizStore.getState().toggleHighContrast();
    expect(useQuizStore.getState().highContrast).toBe(true);
  });

  it('should open and close the settings modal', () => {
    expect(useQuizStore.getState().isSettingsOpen).toBe(false);

    useQuizStore.getState().openSettings();
    expect(useQuizStore.getState().isSettingsOpen).toBe(true);

    useQuizStore.getState().closeSettings();
    expect(useQuizStore.getState().isSettingsOpen).toBe(false);
  });

  it('should record loadingLessonNumber when setLoadingRound is triggered', () => {
    expect(useQuizStore.getState().isLoadingRound).toBe(false);
    expect(useQuizStore.getState().loadingLessonNumber).toBeNull();

    useQuizStore.getState().setLoadingRound(true, 15);
    expect(useQuizStore.getState().isLoadingRound).toBe(true);
    expect(useQuizStore.getState().loadingLessonNumber).toBe(15);

    useQuizStore.getState().setLoadingRound(false);
    expect(useQuizStore.getState().isLoadingRound).toBe(false);
    expect(useQuizStore.getState().loadingLessonNumber).toBeNull();
  });
});
