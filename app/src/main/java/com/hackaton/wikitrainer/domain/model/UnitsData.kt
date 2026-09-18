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
        textColor = WikiWhite
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
        textColor = WikiWhite
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
        textColor = WikiWhite
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
        textColor = WikiWhite
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
        textColor = WikiWhite
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
        textColor = WikiWhite
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
    60 to "🏆 Sfida Finale: Sommo Sapiente di Wikingo"
)

fun getUnitForLesson(lessonNumber: Int): PathUnit {
    return UNITS_DATA.find { lessonNumber in it.startLesson..it.endLesson } ?: UNITS_DATA.first()
}

fun getLessonTitle(lessonNumber: Int): String {
    return LESSON_TITLES[lessonNumber] ?: "Lezione $lessonNumber"
}

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
