/**
 * Centralized Internationalization (i18n) module for Wikingo.
 * Synchronized with android-app/app/src/main/assets/translations.json
 */
import { useQuizStore } from '@/store/useQuizStore';

export const TRANSLATIONS = {
  it: {
    common: {
      app_name: 'Wikingo',
      tagline: 'Impara ogni giorno con Wikipedia',
      continue: 'Continua',
      check: 'Verifica',
      got_it: 'Ho capito',
      retry: 'Riprova',
      close: 'Chiudi',
      back: 'Indietro',
      save: 'Salva',
      delete: 'Rimuovi',
      loading: 'Caricamento...',
      error: 'Errore',
      sound: 'Suono',
      language: 'Lingua'
    },
    welcome: {
      title: 'Wikingo',
      subtitle: 'Impara ogni giorno con Wikipedia, una sfida alla volta.',
      benefit_lessons: 'Lezioni brevi e coinvolgenti',
      benefit_wikipedia: 'Curiosità direttamente da Wikipedia',
      start: 'INIZIA A IMPARARE',
      mascot_description: 'Il vichingo Wikingo'
    },
    auth: {
      login_title: 'Bentornato su Wikingo',
      register_title: 'Crea il tuo account',
      subtitle: 'Salva i tuoi progressi e continua da qualsiasi dispositivo.',
      login_hint: 'Accedi per riprendere il tuo percorso',
      register_hint: 'Registrati per sincronizzare il tuo percorso',
      email: 'Email',
      password: 'Password',
      login_action: 'ACCEDI',
      register_action: 'REGISTRATI',
      no_account: 'Non hai ancora un account?',
      have_account: 'Hai già un account?',
      invalid_credentials: "Inserisci un'email valida e una password di almeno 6 caratteri.",
      generic_error: 'Impossibile completare l\'operazione. Riprova.',
      firebase_not_configured: 'Configura Firebase per sincronizzare i dati su cloud.',
      mascot_description: 'Il vichingo Wikingo',
      guest_mode: 'Continua come ospite',
      logged_in_as: 'Accesso eseguito come'
    },
    dashboard: {
      language_toggle_it: 'IT 🇮🇹',
      language_toggle_en: 'EN 🇬🇧',
      saved_articles_tooltip: 'Voci salvate',
      settings_tooltip: 'Impostazioni',
      history_tooltip: 'Cronologia & Streak',
      auth_tooltip: 'Account',
      streak_unit: 'gg',
      streak_label: 'Streak',
      xp_suffix: 'XP',
      xp_label: 'Esperienza',
      lessons_label: 'Lezioni',
      topics_heading: 'Argomenti affrontati',
      quick_quiz: 'Quiz rapido',
      next_step: 'PROSSIMA TAPPA',
      lesson_prefix: 'LEZIONE',
      continue_button: 'CONTINUA',
      start_bubble: 'INIZIA'
    },
    path_modal: {
      close: 'Chiudi',
      locked_title: 'LEZIONE BLOCCATA',
      start_now: 'INIZIA ORA',
      review_button: 'RIPASSA (+50 XP)',
      locked_desc: 'Completa le tappe precedenti per sbloccare questa sfida enciclopedica!',
      checkpoint_desc: 'Sfida epica di fine sezione: dimostra la tua padronanza su Wikipedia!',
      default_desc: '5 quesiti dinamici estratti in tempo reale da Wikipedia',
      xp_reward_format: '+{xp} XP',
      gems_reward_format: '+{gems} Gemme'
    },
    trainer: {
      question_header: 'Domanda {current} di {total}: Metti alla prova la tua cultura!',
      verify_button: 'VERIFICA',
      correct_title: 'Fantastico!',
      wrong_title: 'Risposta errata',
      correct_answer_label: 'Risposta corretta:',
      wiki_explanation_label: '📖 Dal riassunto di Wikipedia:',
      loading_subtitle: 'Preparo le micro-domande della sessione...',
      error_title: 'Ops! Qualcosa è andato storto',
      lesson_complete_title: 'Lezione Completata!',
      lesson_complete_subtitle: 'Hai ampliato le tue conoscenze con Wikipedia!',
      stat_total_xp: 'XP TOTALI',
      stat_accuracy: 'PRECISIONE',
      stat_streak: 'STREAK',
      new_lesson_button: 'NUOVA LEZIONE',
      read_on_wiki: 'APPROFONDISCI SU WIKIPEDIA',
      save_article: 'SALVA QUESTA VOCE',
      view_history_stats: 'Vedi Cronologia e Statistiche',
      all_topics_label: 'Tutte le voci del round:',
      back_to_dashboard: 'Torna al Percorso'
    },
    quiz: {
      type_cloze: 'COMPLETA LA FRASE',
      type_true_false: 'VERO O FALSO',
      type_subject: 'CHI O COSA È?',
      type_mc: 'SCELTA MULTIPLA',
      game_over_title: 'Vite terminate!',
      game_over_subtitle: 'Hai esaurito i cuori per questa sessione. Ricarica le tue vite per continuare a imparare!',
      recharge_lives: 'Ricarica 5 Vite',
      return_home: 'Torna alla Home',
      exit_title: 'Vuoi uscire?',
      exit_message: 'Se esci adesso perderai i progressi di questo round.',
      exit_confirm: 'Esci dalla sessione',
      exit_cancel: 'Continua la sessione'
    },
    loader: {
      loading_lesson: 'Caricamento della lezione in corso',
      extracting_questions: 'Estrazione e generazione quesiti da Wikipedia...',
      did_you_know: 'Lo sapevi che...?',
      next_fact: 'Altra curiosità'
    },
    settings: {
      title: 'Impostazioni & Accessibilità',
      subtitle: 'Personalizza il modo in cui impari con Wikingo.',
      dark_theme: 'Tema scuro',
      dark_theme_description: 'Riduce la luce e migliora la lettura al buio.',
      sound_effects: 'Effetti sonori',
      sound_effects_description: 'Riproduce un feedback audio durante le risposte.',
      reduce_motion: 'Riduci animazioni',
      reduce_motion_description: 'Limita movimenti e transizioni per un\'esperienza più confortevole.',
      large_text: 'Testo grande',
      large_text_description: 'Aumenta le dimensioni del testo nell\'intera app.',
      high_contrast: 'Alto contrasto',
      high_contrast_description: 'Rende più marcati bordi e colori per una lettura più chiara.',
      language_prefix: 'Lingua:',
      change_language: 'Cambia lingua',
      save_and_close: 'Salva e chiudi',
      welcome_guide: 'Guida di Benvenuto',
      welcome_guide_desc: 'Riapri la schermata introduttiva con i vantaggi di Wikingo.',
      open_welcome: 'Mostra Guida',
      keyboard_shortcuts: 'Scorciatoie da Tastiera',
      shortcut_select: 'Seleziona opzione:',
      shortcut_check: 'Verifica / Avanza:'
    },
    saved: {
      title: 'Voci salvate',
      saved_label: 'Salvato',
      empty_title: 'Nessuna voce salvata',
      empty_description: 'Usa il segnalibro al termine di un quiz per creare la tua biblioteca.',
      open_in_browser: 'Apri su Wikipedia',
      remove: 'Rimuovi'
    },
    history: {
      title: 'Cronologia & Streak',
      current_streak: 'Streak Attuale',
      experience_points: 'Punti Esperienza',
      lessons_completed: 'Lezioni Fatte',
      topics_heading: 'Argomenti affrontati',
      empty_title: 'Nessuna lezione ancora completata',
      empty_description: 'Avvia la tua prima sessione con Wikipedia per iniziare la streak!',
      accuracy_label: 'Precisione',
      open_topic: 'Apri voce'
    }
  },
  en: {
    common: {
      app_name: 'Wikingo',
      tagline: 'Learn new things every day with Wikipedia',
      continue: 'Continue',
      check: 'Check',
      got_it: 'Got it',
      retry: 'Retry',
      close: 'Close',
      back: 'Back',
      save: 'Save',
      delete: 'Delete',
      loading: 'Loading...',
      error: 'An unexpected error happened',
      sound: 'Sound',
      language: 'Language'
    },
    welcome: {
      title: 'Wikingo',
      subtitle: 'Learn every day with Wikipedia, one challenge at a time.',
      benefit_lessons: 'Short and engaging lessons',
      benefit_wikipedia: 'Facts straight from Wikipedia',
      start: 'START LEARNING',
      mascot_description: 'The Wikingo mascot'
    },
    auth: {
      login_title: 'Welcome back to Wikingo',
      register_title: 'Create your account',
      subtitle: 'Save your progress and continue on any device.',
      login_hint: 'Sign in to resume your journey',
      register_hint: 'Register to sync your journey',
      email: 'Email',
      password: 'Password',
      login_action: 'SIGN IN',
      register_action: 'CREATE ACCOUNT',
      no_account: "Don't have an account yet?",
      have_account: 'Already have an account?',
      invalid_credentials: 'Enter a valid email and a password with at least 6 characters.',
      generic_error: 'The operation could not be completed. Try again.',
      firebase_not_configured: 'Configure Firebase to enable cloud synchronization.',
      mascot_description: 'The Wikingo mascot',
      guest_mode: 'Continue as guest',
      logged_in_as: 'Signed in as'
    },
    dashboard: {
      language_toggle_it: 'IT 🇮🇹',
      language_toggle_en: 'EN 🇬🇧',
      saved_articles_tooltip: 'Saved articles',
      settings_tooltip: 'Settings',
      history_tooltip: 'History & Streak',
      auth_tooltip: 'Account',
      streak_unit: 'days',
      streak_label: 'Streak',
      xp_suffix: 'XP',
      xp_label: 'Experience',
      lessons_label: 'Lessons',
      topics_heading: 'Topics explored',
      quick_quiz: 'Quick spin?',
      next_step: 'NEXT STEP',
      lesson_prefix: 'LESSON',
      continue_button: 'CONTINUE',
      start_bubble: 'BEGIN'
    },
    path_modal: {
      close: 'Close',
      locked_title: 'Lesson not yet unlocked',
      start_now: 'Start now',
      review_button: 'Review (+50 XP)',
      locked_desc: 'Complete previous lessons to unlock this challenge',
      default_desc: '5 dynamic questions fetched in real time from Wikipedia',
      checkpoint_desc: 'Epic end-of-section challenge: prove your Wikipedia mastery!',
      xp_reward_format: '+{xp} XP',
      gems_reward_format: '+{gems} Gems'
    },
    trainer: {
      question_header: 'Question {current} of {total}: Put your knowledge to the test!',
      verify_button: 'CHECK',
      correct_title: 'Awesome!',
      wrong_title: 'Incorrect answer',
      correct_answer_label: 'Correct answer:',
      wiki_explanation_label: "📖 From Wikipedia's summary:",
      loading_subtitle: "Getting this session's micro-questions ready...",
      error_title: 'Oops! Something went wrong',
      lesson_complete_title: 'Lesson Completed!',
      lesson_complete_subtitle: 'You expanded your knowledge with Wikipedia!',
      stat_total_xp: 'TOTAL XP',
      stat_accuracy: 'ACCURACY',
      stat_streak: 'STREAK',
      new_lesson_button: 'NEW LESSON',
      read_on_wiki: 'READ MORE ON WIKIPEDIA',
      save_article: 'SAVE THIS ARTICLE',
      view_history_stats: 'View History & Stats',
      all_topics_label: 'All topics in this round:',
      back_to_dashboard: 'Back to Path'
    },
    quiz: {
      type_cloze: 'FILL IN THE BLANK',
      type_true_false: 'TRUE OR FALSE',
      type_subject: 'WHO OR WHAT IS IT?',
      type_mc: 'MULTIPLE CHOICE',
      game_over_title: 'Out of hearts!',
      game_over_subtitle: 'You ran out of hearts for this session. Refill your lives to keep learning!',
      recharge_lives: 'Refill 5 Hearts',
      return_home: 'Back to Home',
      exit_title: 'Leave quiz?',
      exit_message: 'If you leave now, you will lose your progress for this round.',
      exit_confirm: 'Leave session',
      exit_cancel: 'Keep playing'
    },
    loader: {
      loading_lesson: 'Loading lesson in progress',
      extracting_questions: 'Extracting and generating questions from Wikipedia...',
      did_you_know: 'Did you know...?',
      next_fact: 'Next fun fact'
    },
    settings: {
      title: 'Settings & Accessibility',
      subtitle: 'Personalize the way you learn with Wikingo.',
      dark_theme: 'Dark theme',
      dark_theme_description: 'Reduces glare and improves reading in low light.',
      sound_effects: 'Sound effects',
      sound_effects_description: 'Plays audio feedback when you answer.',
      reduce_motion: 'Reduce animations',
      reduce_motion_description: 'Limits movement and transitions for a more comfortable experience.',
      large_text: 'Large text',
      large_text_description: 'Increases text size throughout the app.',
      high_contrast: 'High contrast',
      high_contrast_description: 'Strengthens borders and colors for clearer reading.',
      language_prefix: 'Language:',
      change_language: 'Switch language',
      save_and_close: 'Save & close',
      welcome_guide: 'Welcome Guide',
      welcome_guide_desc: 'Reopen the introductory screen explaining Wikingo features.',
      open_welcome: 'Show Guide',
      keyboard_shortcuts: 'Keyboard Shortcuts',
      shortcut_select: 'Select option:',
      shortcut_check: 'Check / Advance:'
    },
    saved: {
      title: 'Saved articles',
      saved_label: 'Saved',
      empty_title: 'No saved articles yet',
      empty_description: 'Bookmark articles at the end of a quiz to build your library.',
      open_in_browser: 'Open on Wikipedia',
      remove: 'Remove'
    },
    history: {
      title: 'History & Streak',
      current_streak: 'Current Streak',
      experience_points: 'Experience Points',
      lessons_completed: 'Completed Lessons',
      topics_heading: 'Topics explored',
      empty_title: 'No lessons completed yet',
      empty_description: 'Start your first session with Wikipedia to get your streak going!',
      accuracy_label: 'Accuracy',
      open_topic: 'Open article'
    }
  }
} as const;

export type TranslationKey = string;

// Alias map for direct or flattened key resolution
const FLAT_KEY_ALIASES: Record<string, string> = {
  history_title: 'history.title',
  streak_title: 'dashboard.streak_label',
  best_streak: 'history.current_streak',
  welcome_title: 'welcome.title',
  welcome_subtitle: 'welcome.subtitle',
  auth_title: 'auth.login_title',
  auth_guest: 'auth.guest_mode',
  settings_title: 'settings.title',
  settings_dark_mode: 'settings.dark_theme',
  settings_sound: 'settings.sound_effects',
  saved_title: 'saved.title',
  saved_empty: 'saved.empty_title',
  saved_remove: 'saved.remove',
  read_wikipedia: 'saved.open_in_browser',
  lesson_locked: 'path_modal.locked_title',
  lesson_continue: 'dashboard.continue_button',
  mascot_correct_1: 'mascot.correct_1',
  mascot_wrong_1: 'mascot.wrong_1',
};

const EXTRA_DYNAMIC_KEYS: Record<'it' | 'en', Record<string, string>> = {
  it: {
    streak_count: '{count} Giorni',
    offline_warning: 'Modalità offline attiva. Il percorso utilizzerà il catalogo integrato.',
    mascot_correct_1: 'Grande colpo! Continua così!',
    mascot_correct_2: 'Risposta esatta! Sei una forza!',
    mascot_wrong_1: 'Non mollare! Si impara sbagliando!',
    mascot_wrong_2: 'Nessun problema, la prossima andrà meglio!',
  },
  en: {
    streak_count: '{count} Days',
    offline_warning: 'Offline mode active. The path will use the offline catalog.',
    mascot_correct_1: 'Great hit! Keep it up!',
    mascot_correct_2: 'Spot on! You are on fire!',
    mascot_wrong_1: "Don't give up! Mistakes help you learn!",
    mascot_wrong_2: 'No worries, you will get the next one!',
  },
};

/**
 * Look up a translated string by dot notation (e.g. 'history.title') or flat alias (e.g. 'history_title').
 * Interpolates params such as {current} or {count}.
 * Supports both signatures:
 *   i18n(key, language)
 *   i18n(key, language, params)
 *   i18n(key, params, language)
 */
export function i18n(
  key: string,
  arg2?: 'it' | 'en' | Record<string, string | number>,
  arg3?: 'it' | 'en' | Record<string, string | number>
): string {
  let language: 'it' | 'en' = 'it';
  let params: Record<string, string | number> | undefined;

  if (typeof arg2 === 'string') {
    language = arg2 as 'it' | 'en';
    if (typeof arg3 === 'object' && arg3 !== null) {
      params = arg3 as Record<string, string | number>;
    }
  } else if (typeof arg2 === 'object' && arg2 !== null) {
    params = arg2 as Record<string, string | number>;
    if (typeof arg3 === 'string') {
      language = arg3 as 'it' | 'en';
    }
  }

  // Check extra dynamic keys first
  if (EXTRA_DYNAMIC_KEYS[language]?.[key] || EXTRA_DYNAMIC_KEYS.it?.[key]) {
    const raw = EXTRA_DYNAMIC_KEYS[language]?.[key] || EXTRA_DYNAMIC_KEYS.it[key];
    if (!params) return raw;
    return Object.entries(params).reduce((str, [pKey, val]) => {
      return str.replace(new RegExp(`\\{${pKey}\\}`, 'g'), String(val));
    }, raw);
  }

  // Resolve alias if present
  const resolvedKey = FLAT_KEY_ALIASES[key] || (key.includes('.') ? key : key.replace('_', '.'));

  const parts = resolvedKey.split('.');
  let current: any = TRANSLATIONS[language] || TRANSLATIONS.it;

  for (const part of parts) {
    if (current && typeof current === 'object' && part in current) {
      current = current[part];
    } else {
      // Fallback to Italian if not found in current language
      let fallbackCurrent: any = TRANSLATIONS.it;
      for (const fbPart of parts) {
        if (fallbackCurrent && typeof fallbackCurrent === 'object' && fbPart in fallbackCurrent) {
          fallbackCurrent = fallbackCurrent[fbPart];
        } else {
          return key;
        }
      }
      current = fallbackCurrent;
      break;
    }
  }

  if (typeof current !== 'string') {
    return key;
  }

  if (!params) {
    return current;
  }

  return Object.entries(params).reduce((str, [paramKey, val]) => {
    return str.replace(new RegExp(`\\{${paramKey}\\}`, 'g'), String(val));
  }, current);
}

/**
 * React hook that accesses the current language from the store and returns a t() function.
 */
export function useTranslation() {
  const language = useQuizStore((s) => s.language);
  const t = (key: string, params?: Record<string, string | number>) => i18n(key, params, language);
  return { t, language };
}
