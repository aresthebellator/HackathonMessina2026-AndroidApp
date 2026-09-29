package com.hackaton.wikitrainer.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hackaton.wikitrainer.core.designsystem.WikiBlack
import com.hackaton.wikitrainer.core.designsystem.WikiShuttleGray
import com.hackaton.wikitrainer.core.designsystem.WikiSilverSand
import com.hackaton.wikitrainer.core.designsystem.WikiWhite

val UNITS_DATA: List<PathUnit> = listOf(
    PathUnit(
        id = 1,
        title = "Sezione 1",
        subtitle = "Lezioni 1 - 10",
        topic = "Storia & Grandi Civiltà",
        description = "Dall'antico Egitto e Roma classica al Rinascimento: esplora i personaggi e gli imperi del passato.",
        startLesson = 1,
        endLesson = 10,
        iconName = "Landmark",
        keywords = listOf("Storia", "Roma antica", "Rinascimento", "Impero", "Medioevo", "Antica Grecia", "Egitto"),
        primaryColor = WikiBlack,
        darkColor = WikiShuttleGray,
        lightColor = WikiSilverSand,
        textColor = WikiWhite,
        englishTitle = "Section 1",
        englishSubtitle = "Lessons 1 - 10",
        englishTopic = "History & Great Civilizations",
        englishDescription = "From ancient Egypt and classical Rome to the Renaissance: explore the people and empires that shaped the past.",
        englishKeywords = listOf("History", "Ancient Rome", "Renaissance", "Empire", "Middle Ages", "Ancient Greece", "Egypt")
    ),
    PathUnit(
        id = 2,
        title = "Sezione 2",
        subtitle = "Lezioni 11 - 20",
        topic = "Scienza, Spazio & Cosmo",
        description = "Astrofisica, pianeti, teorie della materia e la biologia della vita sulla Terra.",
        startLesson = 11,
        endLesson = 20,
        iconName = "Rocket",
        keywords = listOf("Astronomia", "Spazio", "Fisica", "Pianeta", "Biologia", "Galassia", "Telescopio"),
        primaryColor = WikiShuttleGray,
        darkColor = WikiBlack,
        lightColor = WikiSilverSand,
        textColor = WikiWhite,
        englishTitle = "Section 2",
        englishSubtitle = "Lessons 11 - 20",
        englishTopic = "Science, Space & Cosmos",
        englishDescription = "Astrophysics, planets, theories of matter and the biology of life on Earth.",
        englishKeywords = listOf("Astronomy", "Space", "Physics", "Planet", "Biology", "Galaxy", "Telescope")
    ),
    PathUnit(
        id = 3,
        title = "Sezione 3",
        subtitle = "Lezioni 21 - 30",
        topic = "Arte, Scultura & Capolavori",
        description = "Dai grandi maestri del Rinascimento all'impressionismo e alle avanguardie mondiali.",
        startLesson = 21,
        endLesson = 30,
        iconName = "Palette",
        keywords = listOf("Arte", "Pittura", "Scultura", "Museo", "Architettura", "Impressionismo"),
        primaryColor = WikiBlack,
        darkColor = WikiShuttleGray,
        lightColor = WikiSilverSand,
        textColor = WikiWhite,
        englishTitle = "Section 3",
        englishSubtitle = "Lessons 21 - 30",
        englishTopic = "Art, Sculpture & Masterpieces",
        englishDescription = "From Renaissance masters to Impressionism and the world's artistic avant-garde.",
        englishKeywords = listOf("Art", "Painting", "Sculpture", "Museum", "Architecture", "Impressionism")
    ),
    PathUnit(
        id = 4,
        title = "Sezione 4",
        subtitle = "Lezioni 31 - 40",
        topic = "Geografia & Meraviglie Naturali",
        description = "Le cime più alte, gli abissi marini, i grandi fiumi e gli ecosistemi del nostro pianeta.",
        startLesson = 31,
        endLesson = 40,
        iconName = "Compass",
        keywords = listOf("Geografia", "Continente", "Montagna", "Oceano", "Parco nazionale", "Vulcano"),
        primaryColor = WikiShuttleGray,
        darkColor = WikiBlack,
        lightColor = WikiSilverSand,
        textColor = WikiWhite,
        englishTitle = "Section 4",
        englishSubtitle = "Lessons 31 - 40",
        englishTopic = "Geography & Natural Wonders",
        englishDescription = "The highest peaks, deepest seas, great rivers and ecosystems of our planet.",
        englishKeywords = listOf("Geography", "Continent", "Mountain", "Ocean", "National park", "Volcano")
    ),
    PathUnit(
        id = 5,
        title = "Sezione 5",
        subtitle = "Lezioni 41 - 50",
        topic = "Filosofia, Idee & Invenzioni",
        description = "I pensatori che hanno rivoluzionato la civiltà e le invenzioni che hanno cambiato la storia.",
        startLesson = 41,
        endLesson = 50,
        iconName = "Lightbulb",
        keywords = listOf("Filosofia", "Invenzione", "Illuminismo", "Tecnologia", "Stampa", "Elettricità"),
        primaryColor = WikiBlack,
        darkColor = WikiShuttleGray,
        lightColor = WikiSilverSand,
        textColor = WikiWhite,
        englishTitle = "Section 5",
        englishSubtitle = "Lessons 41 - 50",
        englishTopic = "Philosophy, Ideas & Inventions",
        englishDescription = "The thinkers who changed civilization and the inventions that transformed history.",
        englishKeywords = listOf("Philosophy", "Invention", "Enlightenment", "Technology", "Printing", "Electricity")
    ),
    PathUnit(
        id = 6,
        title = "Sezione 6",
        subtitle = "Lezioni 51 - 60",
        topic = "Letteratura, Miti & Poemi",
        description = "Le opere letterarie e i miti immortali che hanno ispirato la cultura universale.",
        startLesson = 51,
        endLesson = 60,
        iconName = "BookOpen",
        keywords = listOf("Letteratura", "Poesia", "Mito", "Teatro", "Romanzo", "Tragedia"),
        primaryColor = WikiShuttleGray,
        darkColor = WikiBlack,
        lightColor = WikiSilverSand,
        textColor = WikiWhite,
        englishTitle = "Section 6",
        englishSubtitle = "Lessons 51 - 60",
        englishTopic = "Literature, Myths & Poems",
        englishDescription = "Literary works and timeless myths that have inspired world culture.",
        englishKeywords = listOf("Literature", "Poetry", "Myth", "Theatre", "Novel", "Tragedy")
    ),
    PathUnit(
        id = 7,
        title = "Sezione 7",
        subtitle = "Lezioni 61 - 70",
        topic = "Musica, Cinema & Cultura Pop",
        description = "Dalla musica classica al cinema moderno: scopri gli artisti, le opere e le idee che hanno segnato l'immaginario collettivo.",
        startLesson = 61,
        endLesson = 70,
        iconName = "Music",
        keywords = listOf("Musica", "Cinema", "Compositore", "Regista", "Jazz", "Fotografia", "Cultura pop"),
        primaryColor = WikiBlack,
        darkColor = WikiShuttleGray,
        lightColor = WikiSilverSand,
        textColor = WikiWhite,
        englishTitle = "Section 7",
        englishSubtitle = "Lessons 61 - 70",
        englishTopic = "Music, Cinema & Pop Culture",
        englishDescription = "From classical music to modern cinema: discover the artists and ideas that shaped our shared imagination.",
        englishKeywords = listOf("Music", "Cinema", "Composer", "Director", "Jazz", "Photography", "Pop culture")
    ),
    PathUnit(
        id = 8,
        title = "Sezione 8",
        subtitle = "Lezioni 71 - 80",
        topic = "Natura, Tecnologia & Futuro",
        description = "Il rapporto tra esseri umani, ambiente e innovazione: ecosistemi, invenzioni e sfide del futuro.",
        startLesson = 71,
        endLesson = 80,
        iconName = "Science",
        keywords = listOf("Ecologia", "Tecnologia", "Robotica", "Clima", "Energia", "Medicina", "Innovazione"),
        primaryColor = WikiShuttleGray,
        darkColor = WikiBlack,
        lightColor = WikiSilverSand,
        textColor = WikiWhite,
        englishTitle = "Section 8",
        englishSubtitle = "Lessons 71 - 80",
        englishTopic = "Nature, Technology & the Future",
        englishDescription = "The relationship between people, the environment and innovation: ecosystems, inventions and future challenges.",
        englishKeywords = listOf("Ecology", "Technology", "Robotics", "Climate", "Energy", "Medicine", "Innovation")
    ),
    PathUnit(
        id = 9,
        title = "Sezione 9",
        subtitle = "Lezioni 81 - 90",
        topic = "Sport, Atleti & Strategie",
        description = "Dalle regole dei grandi sport alle storie degli atleti e alle strategie che trasformano una gara.",
        startLesson = 81,
        endLesson = 90,
        iconName = "Sports",
        keywords = listOf("Calcio", "Olimpiadi", "Atletica", "Tennis", "Ciclismo", "Basket", "Sport"),
        primaryColor = WikiBlack,
        darkColor = WikiShuttleGray,
        lightColor = WikiSilverSand,
        textColor = WikiWhite,
        englishTitle = "Section 9",
        englishSubtitle = "Lessons 81 - 90",
        englishTopic = "Sports, Athletes & Strategy",
        englishDescription = "From the rules of major sports to athlete stories and the strategies that decide a competition.",
        englishKeywords = listOf("Football", "Olympics", "Athletics", "Tennis", "Cycling", "Basketball", "Sports")
    ),
    PathUnit(
        id = 10,
        title = "Sezione 10",
        subtitle = "Lezioni 91 - 100",
        topic = "Informatica, Codice & Reti",
        description = "Scopri come funzionano algoritmi, linguaggi, computer, internet e intelligenza artificiale.",
        startLesson = 91,
        endLesson = 100,
        iconName = "Computer",
        keywords = listOf("Informatica", "Programmazione", "Algoritmo", "Internet", "Sicurezza informatica", "Database", "Intelligenza artificiale"),
        primaryColor = WikiShuttleGray,
        darkColor = WikiBlack,
        lightColor = WikiSilverSand,
        textColor = WikiWhite,
        englishTitle = "Section 10",
        englishSubtitle = "Lessons 91 - 100",
        englishTopic = "Computer Science, Code & Networks",
        englishDescription = "Discover how algorithms, programming languages, computers, the internet and AI work.",
        englishKeywords = listOf("Computer science", "Programming", "Algorithm", "Internet", "Cybersecurity", "Database", "Artificial intelligence")
    )
)

val LESSON_TITLES: Map<Int, String> = mapOf(
    1 to "L'Alba delle Civiltà",
    2 to "I Misteri dell'Antico Egitto",
    3 to "La Democrazia nell'Antica Grecia",
    4 to "La Repubblica e l'Impero di Roma",
    5 to "I Grandi Condottieri della Storia",
    6 to "Il Mondo Medievale e i Feudi",
    7 to "L'Età dei Cavalieri e delle Crociate",
    8 to "La Rinascita Culturale e Umanistica",
    9 to "Le Rotte delle Grandi Esplorazioni",
    10 to "🏆 Sfida Epica: Maestro della Storia",

    11 to "Il Nostro Sistema Solare",
    12 to "La Vita Segreta delle Stelle",
    13 to "I Misteri dei Buchi Neri",
    14 to "La Relatività dello Spaziotempo",
    15 to "L'Atomo e il Mondo Quantistico",
    16 to "Il Codice della Vita: Il DNA",
    17 to "L'Origine e l'Evoluzione della Vita",
    18 to "L'Epopea dell'Esplorazione Spaziale",
    19 to "I Giganti Telescopi dell'Universo",
    20 to "🏆 Sfida Epica: Maestro del Cosmo",

    21 to "I Maestri del Rinascimento Italiano",
    22 to "L'Invenzione della Prospettiva",
    23 to "Il Chiaroscuro e l'Arte Barocca",
    24 to "L'Impeto del Romanticismo",
    25 to "La Rivoluzione della Luce Impressionista",
    26 to "La Scultura e il Marmo Immortale",
    27 to "Le Avanguardie Artistiche del '900",
    28 to "I Musei più Celebri del Mondo",
    29 to "Le Grandi Architetture dell'Umanità",
    30 to "🏆 Sfida Epica: Custode dell'Arte",

    31 to "Le Sette Meraviglie della Terra",
    32 to "I Tetti del Mondo: Le Grandi Vette",
    33 to "Abissi Oceanici e Barriere Coralline",
    34 to "I Grandi Fiumi delle Civiltà",
    35 to "Il Cuore Caldo della Terra: I Vulcani",
    36 to "La Foresta Amazzonica e i Biomi",
    37 to "I Ghiacci Polari e le Aurore",
    38 to "I Deserti e le Oasi della Terra",
    39 to "Isole Selvagge e Terre Remote",
    40 to "🏆 Sfida Epica: Esploratore del Globo",

    41 to "La Nascita della Filosofia Greca",
    42 to "La Rivoluzione della Stampa a Caratteri Mobili",
    43 to "L'Illuminismo e la Ragione",
    44 to "La Macchina a Vapore e le Fabbriche",
    45 to "La Conquista dell'Elettricità",
    46 to "Il Metodo Scientifico di Galileo",
    47 to "La Macchina di Turing e i Computer",
    48 to "La Scoperta dei Vaccini e della Penicillina",
    49 to "La Rete Mondiale e le Comunicazioni",
    50 to "🏆 Sfida Epica: Maestro delle Idee",

    51 to "I Miti della Creazione e gli Dei",
    52 to "I Poemi Omerici: Iliade e Odissea",
    53 to "Il Viaggio di Dante negli Inferi",
    54 to "Le Tragedie Eterne di Shakespeare",
    55 to "I Grandi Romanzieri dell'Ottocento",
    56 to "Il Romanzo Storico e l'Epica Moderna",
    57 to "La Poesia Moderna e i Versi Liberi",
    58 to "Il Fascino del Realismo Magico",
    59 to "I Capolavori del Premio Nobel",
    60 to "🏆 Sfida Finale: Sommo Sapiente di Wikingo",

    61 to "Le Origini della Musica e degli Strumenti",
    62 to "I Grandi Compositori della Musica Classica",
    63 to "Jazz, Blues e la Rivoluzione del Ritmo",
    64 to "La Nascita del Cinema",
    65 to "I Maestri della Regia Mondiale",
    66 to "La Fotografia tra Arte e Memoria",
    67 to "Le Colonne Sonore più Celebri",
    68 to "La Cultura Pop e i Nuovi Linguaggi",
    69 to "Festival, Premi e Opere Indimenticabili",
    70 to "🏆 Sfida Epica: Maestro della Cultura",

    71 to "Gli Ecosistemi e la Biodiversità",
    72 to "Il Cambiamento Climatico",
    73 to "Energie Rinnovabili e Sostenibilità",
    74 to "Robotica e Intelligenza Artificiale",
    75 to "L'Esplorazione degli Abissi",
    76 to "La Medicina del Futuro",
    77 to "Materiali, Nanotecnologie e Nuove Idee",
    78 to "Le Città del Futuro",
    79 to "Le Grandi Sfide dell'Umanità",
    80 to "🏆 Sfida Finale: Visionario di Wikingo"
    ,81 to "Le Regole Invisibili del Calcio"
    ,82 to "Le Olimpiadi: Dalla Tradizione alla Tecnologia"
    ,83 to "Atletica e Biomeccanica del Movimento"
    ,84 to "Il Tennis tra Servizio e Strategia"
    ,85 to "Le Tappe Epiche del Ciclismo"
    ,86 to "Basket: Spazio, Ritmo e Squadra"
    ,87 to "Sport e Scienza dell'Allenamento"
    ,88 to "Le Sfide dello Sport Paralimpico"
    ,89 to "Fair Play, Regole e Decisioni al Limite"
    ,90 to "🏆 Sfida Epica: Campione dello Sport"
    ,91 to "Che Cos'è un Algoritmo?"
    ,92 to "Dai Primi Computer ai Processori Moderni"
    ,93 to "Linguaggi di Programmazione e Paradigmi"
    ,94 to "Internet: Pacchetti, Server e Web"
    ,95 to "Database e Organizzazione dei Dati"
    ,96 to "Crittografia e Sicurezza Informatica"
    ,97 to "Sistemi Operativi e Risorse"
    ,98 to "Intelligenza Artificiale e Apprendimento Automatico"
    ,99 to "Software Libero, Open Source e Comunità"
    ,100 to "🏆 Sfida Finale: Architetto del Codice"
)

val MAX_LESSON_NUMBER: Int = UNITS_DATA.maxOf { it.endLesson }

fun getUnitForLesson(lessonNumber: Int): PathUnit {
    return UNITS_DATA.find { lessonNumber in it.startLesson..it.endLesson } ?: UNITS_DATA.first()
}

fun getLessonTitle(lessonNumber: Int): String {
    return LESSON_TITLES[lessonNumber] ?: "Lezione $lessonNumber"
}

private val ENGLISH_LESSON_TITLES: Map<Int, String> = LESSON_TITLES.mapValues { (_, title) ->
    title
        .replace("Lezione", "Lesson")
        .replace("Sfida Epica", "Epic Challenge")
        .replace("Sfida Finale", "Final Challenge")
        .replace("Maestro", "Master")
        .replace("Custode", "Guardian")
        .replace("Esploratore", "Explorer")
        .replace("Visionario", "Visionary")
        .replace("Sommo Sapiente", "Grand Sage")
        .replace("della Storia", "of History")
        .replace("del Cosmo", "of the Cosmos")
        .replace("dell'Arte", "of Art")
        .replace("del Globo", "of the Globe")
        .replace("delle Idee", "of Ideas")
        .replace("della Cultura", "of Culture")
        .replace("di Wikingo", "of Wikingo")
        .replace("Civiltà", "Civilizations")
        .replace("Antico Egitto", "Ancient Egypt")
        .replace("Antica Grecia", "Ancient Greece")
        .replace("Roma", "Rome")
        .replace("Rinascimento", "Renaissance")
        .replace("Medievale", "Medieval")
        .replace("Medioevo", "Middle Ages")
        .replace("Condottieri", "Commanders")
        .replace("Cavalieri", "Knights")
        .replace("Crociate", "Crusades")
        .replace("Esplorazioni", "Exploration")
        .replace("Sistema Solare", "Solar System")
        .replace("Stelle", "Stars")
        .replace("Buchi Neri", "Black Holes")
        .replace("Relatività", "Relativity")
        .replace("Spaziotempo", "Spacetime")
        .replace("Atomo", "Atom")
        .replace("Mondo Quantistico", "Quantum World")
        .replace("Origine", "Origin")
        .replace("Evoluzione", "Evolution")
        .replace("Esplorazione Spaziale", "Space Exploration")
        .replace("Telescopi", "Telescopes")
        .replace("Maestri", "Masters")
        .replace("Italiano", "Italian")
        .replace("Invenzione", "Invention")
        .replace("Prospettiva", "Perspective")
        .replace("Chiaroscuro", "Chiaroscuro")
        .replace("Arte Barocca", "Baroque Art")
        .replace("Romanticismo", "Romanticism")
        .replace("Luce Impressionista", "Impressionist Light")
        .replace("Scultura", "Sculpture")
        .replace("Marmo Immortale", "Immortal Marble")
        .replace("Avanguardie Artistiche", "Artistic Avant-Garde")
        .replace("Musei", "Museums")
        .replace("Architetture", "Architecture")
        .replace("Meraviglie", "Wonders")
        .replace("Grandi Vette", "Great Peaks")
        .replace("Abissi Oceanici", "Ocean Depths")
        .replace("Barriere Coralline", "Coral Reefs")
        .replace("Grandi Fiumi", "Great Rivers")
        .replace("Vulcani", "Volcanoes")
        .replace("Foresta Amazzonica", "Amazon Rainforest")
        .replace("Ghiacci Polari", "Polar Ice")
        .replace("Deserti", "Deserts")
        .replace("Isole Selvagge", "Wild Islands")
        .replace("Nascita", "Birth")
        .replace("Filosofia Greca", "Greek Philosophy")
        .replace("Stampa", "Printing")
        .replace("Ragione", "Reason")
        .replace("Macchina a Vapore", "Steam Engine")
        .replace("Fabbriche", "Factories")
        .replace("Elettricità", "Electricity")
        .replace("Metodo Scientifico", "Scientific Method")
        .replace("Scoperta", "Discovery")
        .replace("Vaccini", "Vaccines")
        .replace("Penicillina", "Penicillin")
        .replace("Rete Mondiale", "World Wide Web")
        .replace("Comunicazioni", "Communications")
        .replace("Miti", "Myths")
        .replace("Dei", "Gods")
        .replace("Poemi Omerici", "Homeric Poems")
        .replace("Iliade", "Iliad")
        .replace("Odissea", "Odyssey")
        .replace("Viaggio", "Journey")
        .replace("Inferi", "Underworld")
        .replace("Tragedie Eterne", "Timeless Tragedies")
        .replace("Romanzieri", "Novelists")
        .replace("Poesia Moderna", "Modern Poetry")
        .replace("Versi Liberi", "Free Verse")
        .replace("Realismo Magico", "Magical Realism")
        .replace("Capolavori", "Masterpieces")
        .replace("Origini", "Origins")
        .replace("Strumenti", "Instruments")
        .replace("Compositori", "Composers")
        .replace("Musica Classica", "Classical Music")
        .replace("Rivoluzione del Ritmo", "Rhythm Revolution")
        .replace("Nascita del Cinema", "Birth of Cinema")
        .replace("Regia Mondiale", "World Directing")
        .replace("Colonne Sonore", "Film Scores")
        .replace("Linguaggi", "Languages")
        .replace("Ecosistemi", "Ecosystems")
        .replace("Biodiversità", "Biodiversity")
        .replace("Cambiamento Climatico", "Climate Change")
        .replace("Energie Rinnovabili", "Renewable Energy")
        .replace("Sostenibilità", "Sustainability")
        .replace("Intelligenza Artificiale", "Artificial Intelligence")
        .replace("Esplorazione degli Abissi", "Deep-Sea Exploration")
        .replace("Medicina del Futuro", "Medicine of the Future")
        .replace("Città del Futuro", "Cities of the Future")
        .replace("Sfide dell'Umanità", "Challenges for Humanity")
        .replace("La ", "The ")
        .replace("Il ", "The ")
        .replace("Le ", "The ")
        .replace("I ", "The ")
        .replace("L'Alba", "The Dawn")
        .replace("I Misteri", "The Mysteries")
        .replace("La Democrazia", "Democracy")
        .replace("La Repubblica e l'Impero", "The Republic and Empire")
        .replace("I Grandi", "The Great")
        .replace(" della ", " of ")
        .replace("Il ", "The ")
        .replace("La ", "The ")
        .replace("Le ", "The ")
        .replace("L'", "The ")
        .replace(" e ", " and ")
        .replace(" & ", " & ")
} + mapOf(
    81 to "The Hidden Rules of Football",
    82 to "The Olympics: From Tradition to Technology",
    83 to "Athletics and the Biomechanics of Movement",
    84 to "Tennis: Serving and Strategy",
    85 to "Epic Cycling Stages",
    86 to "Basketball: Space, Rhythm and Teamwork",
    87 to "Sports Science and Training",
    88 to "The Challenges of Paralympic Sport",
    89 to "Fair Play, Rules and Close Calls",
    90 to "🏆 Epic Challenge: Sports Champion",
    91 to "What Is an Algorithm?",
    92 to "From Early Computers to Modern Processors",
    93 to "Programming Languages and Paradigms",
    94 to "The Internet: Packets, Servers and the Web",
    95 to "Databases and Data Organization",
    96 to "Cryptography and Cybersecurity",
    97 to "Operating Systems and Resources",
    98 to "Artificial Intelligence and Machine Learning",
    99 to "Free Software, Open Source and Communities",
    100 to "🏆 Final Challenge: Code Architect"
)

fun getLessonTitle(lessonNumber: Int, language: String): String {
    return if (language == "en") {
        ENGLISH_LESSON_TITLES[lessonNumber] ?: "Lesson $lessonNumber"
    } else {
        getLessonTitle(lessonNumber)
    }

}

/**
 * Stable topic assigned to each lesson. Lesson titles are curated in the
 * curriculum, so the app never has to guess a topic from a unit keyword.
 */
fun getLessonTopic(lessonNumber: Int, language: String): String =
    getLessonTitle(lessonNumber, language)

fun isCheckpointLesson(lessonNumber: Int): Boolean {
    return lessonNumber % 10 == 0
}

/**
 * Returns horizontal offset in Dp for the serpentine wave (0, -45, -70, -45, 0, 45, 70, 45, 0, 0).
 */
fun getSerpentineOffset(lessonIndexInUnit: Int): Dp {
    val offsets = listOf(0.dp, (-45).dp, (-70).dp, (-45).dp, 0.dp, 45.dp, 70.dp, 45.dp, 0.dp, 0.dp)
    val index = (lessonIndexInUnit - 1).coerceAtLeast(0) % offsets.size
    return offsets[index]
}
