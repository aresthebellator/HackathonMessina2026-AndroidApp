import { describe, it, expect, beforeEach } from 'vitest';
import { i18n } from '@/lib/i18n';
import {
  ALL_UNITS_DATA,
  UNITS_DATA,
  getLessonTitle,
  getLessonTopic,
  isCheckpointLesson,
  getUnitForLesson,
  getUnitLocalizedTitle,
  getUnitLocalizedSubtitle,
  getUnitLocalizedTopic,
  getUnitLocalizedDescription,
  getUnitLocalizedKeywords,
} from '@/lib/unitsData';
import { generateSubjectRecognitionQuestion } from '@/services/quizGenerator';
import { Article } from '@/types';
import { useQuizStore } from '@/store/useQuizStore';

describe('Functional Alignment: i18n Localization', () => {
  it('should provide translations for both IT and EN for all core keys', () => {
    const keys = [
      'history_title',
      'streak_title',
      'best_streak',
      'welcome_title',
      'welcome_subtitle',
      'auth_title',
      'auth_guest',
      'offline_warning',
      'mascot_correct_1',
      'mascot_wrong_1',
    ];

    keys.forEach((key) => {
      const itVal = i18n(key, 'it');
      const enVal = i18n(key, 'en');
      expect(itVal).not.toBe(key);
      expect(enVal).not.toBe(key);
      expect(itVal.length).toBeGreaterThan(0);
      expect(enVal.length).toBeGreaterThan(0);
    });
  });

  it('should interpolate placeholders correctly', () => {
    const translated = i18n('streak_count', 'it', { count: 5 });
    expect(translated).toBe('5 Giorni');

    const translatedEn = i18n('streak_count', 'en', { count: 12 });
    expect(translatedEn).toBe('12 Days');
  });

  it('should fallback to Italian if key is missing in requested language or fallback to key if missing everywhere', () => {
    expect(i18n('non_existent_key_xyz', 'it')).toBe('non_existent_key_xyz');
  });
});

describe('Functional Alignment: 100-Lesson Curriculum & Checkpoints', () => {
  it('should support full 100 lessons across 10 units in ALL_UNITS_DATA', () => {
    expect(ALL_UNITS_DATA).toHaveLength(10);
    expect(ALL_UNITS_DATA[0].startLesson).toBe(1);
    expect(ALL_UNITS_DATA[9].endLesson).toBe(100);
  });

  it('should return correct lesson title and topic in both IT and EN', () => {
    const itTitle1 = getLessonTitle(1, 'it');
    const enTitle1 = getLessonTitle(1, 'en');
    expect(itTitle1).toContain('Alba delle Civiltà');
    expect(enTitle1).toContain('Dawn of Civilizations');

    const itTopic1 = getLessonTopic(1, 'it');
    const enTopic1 = getLessonTopic(1, 'en');
    expect(itTopic1).toContain('Alba delle Civiltà');
    expect(enTopic1).toContain('Dawn of Civilizations');

    // Lesson 65 in Unit 7 (Cinema, Musica & Cultura Pop)
    const itTitle65 = getLessonTitle(65, 'it');
    const enTitle65 = getLessonTitle(65, 'en');
    expect(itTitle65).toContain('Regia Mondiale');
    expect(enTitle65).toContain('World Directing');

    const unit65 = getUnitForLesson(65);
    expect(unit65.id).toBe(7);
  });

  it('should accurately identify checkpoint trophy lessons on every 10th lesson up to 100', () => {
    for (let l = 1; l <= 100; l++) {
      if (l % 10 === 0) {
        expect(isCheckpointLesson(l)).toBe(true);
      } else {
        expect(isCheckpointLesson(l)).toBe(false);
      }
    }
  });
});

describe('Functional Alignment: Timed Hearts Recharge (HeartsManager port)', () => {
  beforeEach(() => {
    useQuizStore.setState({
      lives: 2,
      lastLifeLostAt: Date.now() - 4 * 60 * 60 * 1000, // 4 hours ago = 2 lives recovered
    });
  });

  it('should recharge 1 life every 2 hours when lives < 5', () => {
    const store = useQuizStore.getState();
    expect(store.lives).toBe(2);

    store.checkLifeRecharge();

    const updated = useQuizStore.getState();
    expect(updated.lives).toBe(4); // 2 + 2 = 4
  });

  it('should cap recharge at MAX_LIVES (5)', () => {
    useQuizStore.setState({
      lives: 1,
      lastLifeLostAt: Date.now() - 20 * 60 * 60 * 1000, // 20 hours ago
    });

    useQuizStore.getState().checkLifeRecharge();
    expect(useQuizStore.getState().lives).toBe(5);
  });
});

describe('Functional Alignment: Topic History & Streaks', () => {
  beforeEach(() => {
    useQuizStore.setState({
      topicHistory: [],
      streak: 3,
      bestStreak: 5,
      xp: 100,
    });
  });

  it('should record completed topic history and update best streak', () => {
    useQuizStore.getState().recordTopicHistory({
      topicId: 'geologia-1',
      topicTitle: 'Origine della Terra',
      score: 5,
      totalQuestions: 5,
      xpEarned: 90,
      pageUrl: 'https://it.wikipedia.org/wiki/Origine_della_Terra',
    });

    const state = useQuizStore.getState();
    expect(state.topicHistory).toHaveLength(1);
    expect(state.topicHistory[0].topicTitle).toBe('Origine della Terra');
    expect(state.topicHistory[0].score).toBe(5);
    expect(state.topicHistory[0].xpEarned).toBe(90);
  });
});

describe('Functional Alignment: Ported Modals State & Guest Auth', () => {
  beforeEach(() => {
    useQuizStore.setState({
      isHistoryOpen: false,
      isEasterEggOpen: false,
      isAuthOpen: false,
      isWelcomeOpen: false,
      user: {
        uid: 'guest_123',
        displayName: 'Guest Explorer',
        email: null,
        isAnonymous: true,
      },
    });
  });

  it('should open and close HistoryModal correctly', () => {
    expect(useQuizStore.getState().isHistoryOpen).toBe(false);
    useQuizStore.getState().openHistory();
    expect(useQuizStore.getState().isHistoryOpen).toBe(true);
    useQuizStore.getState().closeHistory();
    expect(useQuizStore.getState().isHistoryOpen).toBe(false);
  });

  it('should open and close EasterEggModal correctly', () => {
    expect(useQuizStore.getState().isEasterEggOpen).toBe(false);
    useQuizStore.getState().openEasterEgg();
    expect(useQuizStore.getState().isEasterEggOpen).toBe(true);
    useQuizStore.getState().closeEasterEgg();
    expect(useQuizStore.getState().isEasterEggOpen).toBe(false);
  });

  it('should open and close AuthModal correctly', () => {
    expect(useQuizStore.getState().isAuthOpen).toBe(false);
    useQuizStore.getState().openAuth();
    expect(useQuizStore.getState().isAuthOpen).toBe(true);
    useQuizStore.getState().closeAuth();
    expect(useQuizStore.getState().isAuthOpen).toBe(false);
  });

  it('should update user profile upon login / guest sign-in', () => {
    useQuizStore.getState().setUser({
      uid: 'user_456',
      displayName: 'Viking Explorer',
      email: 'viking@wikimedia.org',
      isAnonymous: false,
    });

    expect(useQuizStore.getState().user?.displayName).toBe('Viking Explorer');
    expect(useQuizStore.getState().user?.isAnonymous).toBe(false);
  });
});

describe('Functional Alignment: Clock Rollback & Future Timestamp Recovery', () => {
  it('should safely clamp future lastLifeLostAt and not freeze recharge forever', () => {
    const futureTime = Date.now() + 500000;
    useQuizStore.setState({
      lives: 3,
      lastLifeLostAt: futureTime,
    });

    useQuizStore.getState().checkLifeRecharge();

    const state = useQuizStore.getState();
    // lastLifeLostAt should be clamped to current time or past, not remaining in the future
    expect(state.lastLifeLostAt).toBeLessThanOrEqual(Date.now());
  });

  it('should restore lives correctly after past time lapse', () => {
    const sixHoursAgo = Date.now() - 6 * 60 * 60 * 1000;
    useQuizStore.setState({
      lives: 1,
      lastLifeLostAt: sixHoursAgo,
    });

    useQuizStore.getState().checkLifeRecharge();

    // 6 hours / 2 hours per life = 3 lives gained -> 1 + 3 = 4
    expect(useQuizStore.getState().lives).toBe(4);
  });
});

describe('Functional Alignment: Subject Recognition 4-Option Backfill', () => {
  it('should always generate exactly 4 options even when article batch has fewer than 4 items', () => {
    const singleArticle: Article = {
      pageid: 99999,
      title: 'Galileo Galilei',
      extract: 'Galileo Galilei è stato un fisico, astronomo, filosofo e matematico italiano.',
      lang: 'it',
      type: 'standard',
      content_urls: { desktop: { page: 'https://it.wikipedia.org/wiki/Galileo_Galilei' } },
    };

    const question = generateSubjectRecognitionQuestion(singleArticle, [singleArticle], 0);
    expect(question).not.toBeNull();
    if (question) {
      expect(question.options).toHaveLength(4);
      expect(question.options.some((o) => o.id === question.correctOptionId)).toBe(true);
      const correctOption = question.options.find((o) => o.id === question.correctOptionId);
      expect(correctOption?.text).toBe('Galileo Galilei');
    }
  });
});

describe('Functional Alignment: First-Time Onboarding & Welcome Flow', () => {
  beforeEach(() => {
    useQuizStore.setState({
      hasSeenWelcome: false,
      isWelcomeOpen: false,
      completedRounds: 0,
      completedLessons: [],
    });
  });

  it('should handle openWelcome and closeWelcome transitions', () => {
    expect(useQuizStore.getState().hasSeenWelcome).toBe(false);
    expect(useQuizStore.getState().isWelcomeOpen).toBe(false);

    useQuizStore.getState().openWelcome();
    expect(useQuizStore.getState().isWelcomeOpen).toBe(true);

    useQuizStore.getState().closeWelcome();
    expect(useQuizStore.getState().isWelcomeOpen).toBe(false);
    expect(useQuizStore.getState().hasSeenWelcome).toBe(true);
  });
});

describe('Functional Alignment: Complete Bilingual Units Verification', () => {
  it('should have all 10 units fully populated with valid Italian and English metadata', () => {
    expect(UNITS_DATA).toHaveLength(10);

    UNITS_DATA.forEach((unit) => {
      expect(unit.title.length).toBeGreaterThan(0);
      expect(unit.subtitle.length).toBeGreaterThan(0);
      expect(unit.topic.length).toBeGreaterThan(0);
      expect(unit.description.length).toBeGreaterThan(0);
      expect(unit.keywords.length).toBeGreaterThan(0);

      // Verify English fields ported from android-app UnitsData.kt
      expect(unit.englishTitle).toBeDefined();
      expect(unit.englishTitle!.length).toBeGreaterThan(0);
      expect(unit.englishSubtitle).toBeDefined();
      expect(unit.englishSubtitle!.length).toBeGreaterThan(0);
      expect(unit.englishTopic).toBeDefined();
      expect(unit.englishTopic!.length).toBeGreaterThan(0);
      expect(unit.englishDescription).toBeDefined();
      expect(unit.englishDescription!.length).toBeGreaterThan(0);
      expect(unit.englishKeywords).toBeDefined();
      expect(unit.englishKeywords!.length).toBeGreaterThan(0);

      // Localization helpers should return correct localized strings
      expect(getUnitLocalizedTitle(unit, 'it')).toBe(unit.title);
      expect(getUnitLocalizedTitle(unit, 'en')).toBe(unit.englishTitle);
      expect(getUnitLocalizedTopic(unit, 'it')).toBe(unit.topic);
      expect(getUnitLocalizedTopic(unit, 'en')).toBe(unit.englishTopic);
      expect(getUnitLocalizedSubtitle(unit, 'it')).toBe(unit.subtitle);
      expect(getUnitLocalizedSubtitle(unit, 'en')).toBe(unit.englishSubtitle);
      expect(getUnitLocalizedDescription(unit, 'it')).toBe(unit.description);
      expect(getUnitLocalizedDescription(unit, 'en')).toBe(unit.englishDescription);
      expect(getUnitLocalizedKeywords(unit, 'it')).toEqual(unit.keywords);
      expect(getUnitLocalizedKeywords(unit, 'en')).toEqual(unit.englishKeywords);
    });
  });
});
