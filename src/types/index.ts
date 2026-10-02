/**
 * Core domain types for Wikingo Micro-learning Quiz application
 */

export interface ArticleThumbnail {
  source: string;
  width: number;
  height: number;
}

export interface ArticleUrls {
  desktop: {
    page: string;
    revisions?: string;
    edit?: string;
    talk?: string;
  };
  mobile?: {
    page: string;
    revisions?: string;
    edit?: string;
    talk?: string;
  };
}

export interface Article {
  pageid: number;
  title: string;
  displaytitle?: string;
  extract: string;
  extract_html?: string;
  description?: string;
  thumbnail?: ArticleThumbnail;
  originalimage?: ArticleThumbnail;
  content_urls: ArticleUrls;
  lang: 'it' | 'en';
  type: string;
  timestamp?: string;
}

export type QuestionType = 
  | 'multiple_choice' 
  | 'cloze' 
  | 'true_false' 
  | 'subject_recognition';

export interface QuestionOption {
  id: string;
  text: string;
  isCorrect: boolean;
}

export interface ClozeContext {
  before: string;
  blank: string;
  after: string;
  fullSentence: string;
}

export interface Question {
  id: string;
  type: QuestionType;
  article: Article;
  prompt: string;
  clozeContext?: ClozeContext;
  options: QuestionOption[];
  correctOptionId: string;
  explanation: string;
  sourceUrl: string;
  categoryHint?: string;
}

export interface UserAnswer {
  questionId: string;
  selectedOptionId: string;
  isCorrect: boolean;
  answeredAt: number;
}

export interface QuizRound {
  id: string;
  questions: Question[];
  currentIndex: number;
  answers: UserAnswer[];
  status: 'active' | 'reviewing' | 'completed' | 'game_over';
  score: number;
  totalXp: number;
  startedAt: number;
  completedAt?: number;
  lessonNumber?: number;
}

export type FeedbackStatus = 'idle' | 'correct' | 'incorrect';

export interface QuizState {
  currentRound: QuizRound | null;
  selectedOptionId: string | null;
  feedbackStatus: FeedbackStatus;
  isDrawerOpen: boolean;
  lives: number;
  maxLives: number;
  isSoundEnabled: boolean;
  language: 'it' | 'en';
}

export interface SavedArticleItem {
  pageid: number;
  title: string;
  description?: string;
  extract: string;
  thumbnailUrl?: string;
  url: string;
  savedAt: number;
  lang: 'it' | 'en';
}

export interface UnitTheme {
  primary: string;
  dark: string;
  light: string;
  subtle: string;
  text: string;
  bannerBg: string;
}

export interface Unit {
  id: number;
  title: string;
  subtitle: string;
  topic: string;
  description: string;
  startLesson: number;
  endLesson: number;
  theme: UnitTheme;
  iconName: string;
  keywords: string[];
  englishTitle?: string;
  englishSubtitle?: string;
  englishTopic?: string;
  englishDescription?: string;
  englishKeywords?: string[];
}

export type LessonStatus = 'completed' | 'current' | 'locked';

export interface Lesson {
  number: number;
  unitId: number;
  title: string;
  isCheckpoint?: boolean;
  status: LessonStatus;
  stars?: number;
  xpReward: number;
}

export interface TopicHistory {
  id: number;
  pageId: number;
  topicId?: string;
  title: string;
  topicTitle?: string;
  description: string;
  extract: string;
  thumbnailUrl?: string;
  wikiUrl: string;
  pageUrl?: string;
  score: number;
  totalQuestions: number;
  completedAt: number;
  xpEarned: number;
}

export interface UserStats {
  currentStreak: number;
  bestStreak: number;
  totalXp: number;
  totalLessonsCompleted: number;
  lastActiveDate: string;
}

export interface UserProfile {
  xp: number;
  streak: number;
  bestStreak: number;
  lastActiveDate: string; // ISO date 'YYYY-MM-DD'
  completedRounds: number;
  totalCorrect: number;
  totalQuestions: number;
  gems: number;
  savedArticles: SavedArticleItem[];
  currentLessonIndex: number;
  completedLessons: number[];
  lessonStars: Record<number, number>;
  topicHistory: TopicHistory[];
}

