import { Unit } from '@/types';

export const UNITS_DATA: Unit[] = [
  {
    id: 1,
    title: 'Sezione 1',
    subtitle: 'Lezioni 1 - 10',
    topic: 'Storia & Grandi Civiltà',
    description: 'Dall\'antico Egitto e Roma classica al Rinascimento: esplora i personaggi e gli imperi del passato.',
    startLesson: 1,
    endLesson: 10,
    iconName: 'Landmark',
    keywords: ['Storia', 'Roma antica', 'Rinascimento', 'Impero', 'Medioevo', 'Antica Grecia', 'Egitto'],
    theme: {
      primary: '#58CC02',
      dark: '#46A302',
      light: '#D7FFB8',
      subtle: '#F2FCE8',
      text: '#2A7000',
      bannerBg: 'bg-[#58CC02]',
    },
  },
  {
    id: 2,
    title: 'Sezione 2',
    subtitle: 'Lezioni 11 - 20',
    topic: 'Scienza, Spazio & Cosmo',
    description: 'Astrofisica, pianeti, teorie della materia e la biologia della vita sulla Terra.',
    startLesson: 11,
    endLesson: 20,
    iconName: 'Rocket',
    keywords: ['Astronomia', 'Spazio', 'Fisica', 'Pianeta', 'Biologia', 'Galassia', 'Telescopio'],
    theme: {
      primary: '#1CB0F6',
      dark: '#1899D6',
      light: '#DDF4FF',
      subtle: '#F0F9FF',
      text: '#0C70A2',
      bannerBg: 'bg-[#1CB0F6]',
    },
  },
  {
    id: 3,
    title: 'Sezione 3',
    subtitle: 'Lezioni 21 - 30',
    topic: 'Arte, Scultura & Capolavori',
    description: 'Dai grandi maestri del Rinascimento all\'impressionismo e alle avanguardie mondiali.',
    startLesson: 21,
    endLesson: 30,
    iconName: 'Palette',
    keywords: ['Arte', 'Pittura', 'Scultura', 'Museo', 'Architettura', 'Impressionismo'],
    theme: {
      primary: '#CE82FF',
      dark: '#A855F7',
      light: '#F3E8FF',
      subtle: '#FAF5FF',
      text: '#7E22CE',
      bannerBg: 'bg-[#CE82FF]',
    },
  },
  {
    id: 4,
    title: 'Sezione 4',
    subtitle: 'Lezioni 31 - 40',
    topic: 'Geografia & Meraviglie Naturali',
    description: 'Le cime più alte, gli abissi marini, i grandi fiumi e gli ecosistemi del nostro pianeta.',
    startLesson: 31,
    endLesson: 40,
    iconName: 'Compass',
    keywords: ['Geografia', 'Continente', 'Montagna', 'Oceano', 'Parco nazionale', 'Vulcano'],
    theme: {
      primary: '#FF9600',
      dark: '#D97706',
      light: '#FFEDD5',
      subtle: '#FFF7ED',
      text: '#B45309',
      bannerBg: 'bg-[#FF9600]',
    },
  },
  {
    id: 5,
    title: 'Sezione 5',
    subtitle: 'Lezioni 41 - 50',
    topic: 'Filosofia, Idee & Grandi Invenzioni',
    description: 'I pensatori che hanno rivoluzionato la civiltà e le invenzioni che hanno cambiato la storia.',
    startLesson: 41,
    endLesson: 50,
    iconName: 'Lightbulb',
    keywords: ['Filosofia', 'Invenzione', 'Illuminismo', 'Tecnologia', 'Stampa', 'Elettricità'],
    theme: {
      primary: '#00CD9C',
      dark: '#00A37B',
      light: '#CCFBF1',
      subtle: '#F0FDFA',
      text: '#0F766E',
      bannerBg: 'bg-[#00CD9C]',
    },
  },
  {
    id: 6,
    title: 'Sezione 6',
    subtitle: 'Lezioni 51 - 60',
    topic: 'Letteratura, Miti & Poemi Epici',
    description: 'Le opere letterarie e i miti immortali che hanno ispirato la cultura universale.',
    startLesson: 51,
    endLesson: 60,
    iconName: 'BookOpen',
    keywords: ['Letteratura', 'Poesia', 'Mito', 'Teatro', 'Romanzo', 'Tragedia'],
    theme: {
      primary: '#FF4B4B',
      dark: '#EA2B2B',
      light: '#FFDFE0',
      subtle: '#FFF1F2',
      text: '#B91C1C',
      bannerBg: 'bg-[#FF4B4B]',
    },
  },
];

export const LESSON_TITLES: Record<number, string> = {
  1: "L'Alba delle Civiltà",
  2: "I Misteri dell'Antico Egitto",
  3: "La Democrazia nell'Antica Grecia",
  4: "La Repubblica e l'Impero di Roma",
  5: "I Grandi Condottieri della Storia",
  6: "Il Mondo Medievale e i Feudi",
  7: "L'Età dei Cavalieri e delle Crociate",
  8: "La Rinascita Culturale e Umanistica",
  9: "Le Rotte delle Grandi Esplorazioni",
  10: "🏆 Sfida Epica: Maestro della Storia",

  11: "Il Nostro Sistema Solare",
  12: "La Vita Segreta delle Stelle",
  13: "I Misteri dei Buchi Neri",
  14: "La Relatività dello Spaziotempo",
  15: "L'Atomo e il Mondo Quantistico",
  16: "Il Codice della Vita: Il DNA",
  17: "L'Origine e l'Evoluzione della Vita",
  18: "L'Epopea dell'Esplorazione Spaziale",
  19: "I Giganti Telescopi dell'Universo",
  20: "🏆 Sfida Epica: Maestro del Cosmo",

  21: "I Maestri del Rinascimento Italiano",
  22: "L'Invenzione della Prospettiva",
  23: "Il Chiaroscuro e l'Arte Barocca",
  24: "L'Impeto del Romanticismo",
  25: "La Rivoluzione della Luce Impressionista",
  26: "La Scultura e il Marmo Immortale",
  27: "Le Avanguardie Artistiche del '900",
  28: "I Musei più Celebri del Mondo",
  29: "Le Grandi Architetture dell'Umanità",
  30: "🏆 Sfida Epica: Custode dell'Arte",

  31: "Le Sette Meraviglie della Terra",
  32: "I Tetti del Mondo: Le Grandi Vette",
  33: "Abissi Oceanici e Barriere Coralline",
  34: "I Grandi Fiumi delle Civiltà",
  35: "Il Cuore Caldo della Terra: I Vulcani",
  36: "La Foresta Amazzonica e i Biomi",
  37: "I Ghiacci Polari e le Aurore",
  38: "I Deserti e le Oasi della Terra",
  39: "Isole Selvagge e Terre Remote",
  40: "🏆 Sfida Epica: Esploratore del Globo",

  41: "La Nascita della Filosofia Greca",
  42: "La Rivoluzione della Stampa a Caratteri Mobili",
  43: "L'Illuminismo e la Ragione",
  44: "La Macchina a Vapore e le Fabbriche",
  45: "La Conquista dell'Elettricità",
  46: "Il Metodo Scientifico di Galileo",
  47: "La Macchina di Turing e i Computer",
  48: "La Scoperta dei Vaccini e della Penicillina",
  49: "La Rete Mondiale e le Comunicazioni",
  50: "🏆 Sfida Epica: Maestro delle Idee",

  51: "I Miti della Creazione e gli Dei",
  52: "I Poemi Omerici: Iliade e Odissea",
  53: "Il Viaggio di Dante negli Inferi",
  54: "Le Tragedie Eterne di Shakespeare",
  55: "I Grandi Romanzieri dell'Ottocento",
  56: "Il Romanzo Storico e l'Epica Moderna",
  57: "La Poesia Moderna e i Versi Liberi",
  58: "Il Fascino del Realismo Magico",
  59: "I Capolavori del Premio Nobel",
  60: "🏆 Sfida Finale: Sommo Sapiente di Wikingo"
};

/**
 * Get unit for a given lesson number
 */
export function getUnitForLesson(lessonNumber: number): Unit {
  const unit = UNITS_DATA.find(
    (u) => lessonNumber >= u.startLesson && lessonNumber <= u.endLesson
  );
  return unit || UNITS_DATA[0];
}

/**
 * Calculate the horizontal serpentine offset percentage for Duolingo path wave
 */
export function getSerpentineOffset(lessonIndexInUnit: number): number {
  // Returns offset in pixels for a dynamic wave: 0, -45, -70, -45, 0, 45, 70, 45, 0
  const offsets = [0, -45, -70, -45, 0, 45, 70, 45, 0, 0];
  return offsets[(lessonIndexInUnit - 1) % offsets.length] || 0;
}
