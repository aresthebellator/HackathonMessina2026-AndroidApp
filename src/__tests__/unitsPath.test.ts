import { describe, it, expect, beforeEach } from 'vitest';
import { UNITS_DATA, getUnitForLesson, getSerpentineOffset, LESSON_TITLES } from '@/lib/unitsData';
import { useQuizStore } from '@/store/useQuizStore';
import { generateQuizQuestions } from '@/services/quizGenerator';
import { OFFLINE_ARTICLES_IT } from '@/lib/offlinePool';

describe('Duolingo Path & 10-Lesson Units Configuration', () => {
  it('should have 10 distinct units with 10 lessons each (100 lessons total)', () => {
    expect(UNITS_DATA).toHaveLength(10);

    UNITS_DATA.forEach((unit, idx) => {
      const expectedStart = idx * 10 + 1;
      const expectedEnd = (idx + 1) * 10;
      expect(unit.startLesson).toBe(expectedStart);
      expect(unit.endLesson).toBe(expectedEnd);
      expect(unit.endLesson - unit.startLesson + 1).toBe(10);
      expect(unit.topic).toBeDefined();
      expect(unit.keywords.length).toBeGreaterThan(0);
      expect(unit.theme.primary).toMatch(/^#[0-9A-Fa-f]{6}$/);
    });
  });

  it('should switch unit topic accurately across all 10 units', () => {
    // Unit 1: Storia (1 - 10)
    expect(getUnitForLesson(1).topic).toContain('Storia');
    expect(getUnitForLesson(10).topic).toContain('Storia');

    // Unit 2: Scienza (11 - 20)
    expect(getUnitForLesson(11).topic).toContain('Scienza');
    expect(getUnitForLesson(20).topic).toContain('Scienza');

    // Unit 3: Arte (21 - 30)
    expect(getUnitForLesson(21).topic).toContain('Arte');
    expect(getUnitForLesson(30).topic).toContain('Arte');

    // Unit 4: Geografia (31 - 40)
    expect(getUnitForLesson(31).topic).toContain('Geografia');
    expect(getUnitForLesson(40).topic).toContain('Geografia');

    // Unit 5: Filosofia (41 - 50)
    expect(getUnitForLesson(41).topic).toContain('Filosofia');
    expect(getUnitForLesson(50).topic).toContain('Filosofia');

    // Unit 6: Letteratura (51 - 60)
    expect(getUnitForLesson(51).topic).toContain('Letteratura');
    expect(getUnitForLesson(60).topic).toContain('Letteratura');

    // Unit 7: Musica (61 - 70)
    expect(getUnitForLesson(61).topic).toContain('Musica');
    expect(getUnitForLesson(70).topic).toContain('Musica');

    // Unit 8: Natura / Tecnologia (71 - 80)
    expect(getUnitForLesson(71).topic).toContain('Natura');
    expect(getUnitForLesson(80).topic).toContain('Natura');

    // Unit 9: Sport (81 - 90)
    expect(getUnitForLesson(81).topic).toContain('Sport');
    expect(getUnitForLesson(90).topic).toContain('Sport');

    // Unit 10: Informatica (91 - 100)
    expect(getUnitForLesson(91).topic).toContain('Informatica');
    expect(getUnitForLesson(100).topic).toContain('Informatica');
  });

  it('should have custom titles for all 100 lessons and checkpoint trophies on multiples of 10', () => {
    for (let i = 1; i <= 100; i++) {
      expect(LESSON_TITLES[i]).toBeDefined();
      if (i % 10 === 0) {
        expect(LESSON_TITLES[i]).toContain('🏆');
      }
    }
  });

  it('should generate wave-like serpentine horizontal offsets', () => {
    const offsets = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map(getSerpentineOffset);
    // Verifies alternating negative (left), zero (center), and positive (right) offsets
    expect(offsets[0]).toBe(0);
    expect(offsets[1]).toBeLessThan(0); // swings left
    expect(offsets[2]).toBeLessThan(0); // swings far left
    expect(offsets[4]).toBe(0);         // returns center
    expect(offsets[5]).toBeGreaterThan(0); // swings right
    expect(offsets[6]).toBeGreaterThan(0); // swings far right
    expect(offsets[8]).toBe(0);         // returns center
  });
});

describe('Duolingo Lesson Progression Store', () => {
  beforeEach(() => {
    const store = useQuizStore.getState();
    store.quitQuiz();
    store.restoreLives();
    useQuizStore.setState({
      currentLessonIndex: 1,
      completedLessons: [],
      lessonStars: {},
    });
  });

  it('should advance from Lesson 1 to Lesson 2 upon completion', () => {
    const questions = generateQuizQuestions(OFFLINE_ARTICLES_IT.slice(0, 1));
    useQuizStore.getState().startRound(questions, 1);

    expect(useQuizStore.getState().currentRound?.lessonNumber).toBe(1);

    // Answer question correctly
    const q = questions[0];
    useQuizStore.getState().selectOption(q.correctOptionId);
    useQuizStore.getState().checkAnswer();

    // Finish round
    useQuizStore.getState().nextQuestion();

    const state = useQuizStore.getState();
    expect(state.completedLessons).toContain(1);
    expect(state.currentLessonIndex).toBe(2);
    expect(state.lessonStars[1]).toBeGreaterThanOrEqual(1);
  });

  it('should cross unit boundary from Lesson 10 (Unit 1) to Lesson 11 (Unit 2)', () => {
    useQuizStore.setState({ currentLessonIndex: 10 });
    const questions = generateQuizQuestions(OFFLINE_ARTICLES_IT.slice(0, 1));
    useQuizStore.getState().startRound(questions, 10);

    const q = questions[0];
    useQuizStore.getState().selectOption(q.correctOptionId);
    useQuizStore.getState().checkAnswer();
    useQuizStore.getState().nextQuestion();

    const state = useQuizStore.getState();
    expect(state.completedLessons).toContain(10);
    expect(state.currentLessonIndex).toBe(11);

    const unitBefore = getUnitForLesson(10);
    const unitAfter = getUnitForLesson(state.currentLessonIndex);

    expect(unitBefore.id).toBe(1);
    expect(unitAfter.id).toBe(2);
    expect(unitAfter.topic).toContain('Scienza');
  });
});
