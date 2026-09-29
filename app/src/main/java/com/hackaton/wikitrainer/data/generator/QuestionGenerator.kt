package com.hackaton.wikitrainer.data.generator

import com.hackaton.wikitrainer.data.remote.dto.WikiSummaryDto
import com.hackaton.wikitrainer.domain.model.Question
import com.hackaton.wikitrainer.domain.model.QuestionType
import kotlin.math.absoluteValue
import kotlin.random.Random
import java.util.UUID

/**
 * QuestionGenerator:
 * Analyzes Wikipedia summary data (title, extract, description) and automatically
 * formulates 5-7 engaging, Duolingo-styled micro-questions:
 * - 4-option multiple choice questions with plausible distractors
 * - True / False questions
 * - Explanatory cards extracted directly from Wikipedia text
 */
class QuestionGenerator {

    private val italianBiographicalDistractors = listOf(
        "Un matematico e astronomo dell'antica Grecia",
        "Un generale dell'impero persiano durante le guerre mediche",
        "Un pittore fiammingo del periodo barocco",
        "Un esploratore portoghese che circumnavigò l'Africa",
        "Un compositore classico austriaco del XVIII secolo",
        "Un filosofo illuminista francese autore di saggi politici",
        "Un pioniere dell'aviazione e ingegnere meccanico",
        "Un medico e biologo scopritore di vaccini moderni"
    )

    private val italianGeographicalDistractors = listOf(
        "Un'isola vulcanica situata nell'arcipelago polinesiano",
        "Una catena montuosa che separa due continenti",
        "Un antico porto fluviale della Mesopotamia",
        "Una regione desertica dell'Africa subsahariana",
        "Una città costiera fondata dai coloni fenici",
        "Un ghiacciaio perenne situato nelle Alpi scandinave",
        "Un parco nazionale protetto nell'America centrale"
    )

    private val italianScienceDistractors = listOf(
        "Un principio fondamentale della termodinamica quantistica",
        "Un elemento chimico sintetizzato in laboratorio nel 1974",
        "Una cometa periodica visibile a occhio nudo ogni 76 anni",
        "Un processo biologico di fotosintesi anaerobica",
        "Una missione spaziale robotica inviata verso Giove",
        "Una teoria geologica sulla tettonica delle placche continentali"
    )

    private val italianGeneralDistractors = listOf(
        "Un trattato diplomatico firmato al termine della guerra dei trent'anni",
        "Un movimento artistico d'avanguardia nato a inizio Novecento",
        "Un manoscritto medievale conservato nella biblioteca vaticana",
        "Uno strumento musicale tradizionale a fiato",
        "Un'opera teatrale in versi scritta durante il Rinascimento"
    )

    private val englishBiographicalDistractors = listOf(
        "A Greek mathematician and astronomer",
        "A Persian general during the Greco-Persian Wars",
        "A Flemish painter from the Baroque period",
        "A Portuguese explorer who sailed around Africa",
        "An Austrian classical composer from the 18th century",
        "A French Enlightenment philosopher and political essayist",
        "An aviation pioneer and mechanical engineer",
        "A physician and biologist who pioneered modern vaccines"
    )

    private val englishGeographicalDistractors = listOf(
        "A volcanic island in the Polynesian archipelago",
        "A mountain range separating two continents",
        "An ancient river port in Mesopotamia",
        "A desert region in sub-Saharan Africa",
        "A coastal city founded by Phoenician settlers",
        "A permanent glacier in the Scandinavian Alps",
        "A protected national park in Central America"
    )

    private val englishScienceDistractors = listOf(
        "A fundamental principle of quantum thermodynamics",
        "A chemical element synthesized in a laboratory in 1974",
        "A periodic comet visible every 76 years",
        "An anaerobic photosynthesis process",
        "A robotic space mission sent toward Jupiter",
        "A geological theory about continental plate tectonics"
    )

    private val englishGeneralDistractors = listOf(
        "A diplomatic treaty signed at the end of the Thirty Years' War",
        "An avant-garde art movement born in the early 20th century",
        "A medieval manuscript kept in the Vatican Library",
        "A traditional wind instrument",
        "A Renaissance verse play"
    )

    fun generateQuestions(summary: WikiSummaryDto, language: String = summary.lang ?: "it"): List<Question> {
        val title = summary.title.ifBlank { "Soggetto sconosciuto" }
        val description = summary.description ?: ""
        val extract = summary.extract ?: ""

        val sentences = extract
            .split(Regex("(?<=[.!?])\\s+"))
            .map { it.trim() }
            .filter { it.length > 25 }

        val facts = (sentences + description.split(Regex("[,;]")))
            .map { it.trim().removeSuffix(".") }
            .filter { it.length >= 24 }
            .distinct()
        val random = Random(title.hashCode() * 31 + extract.hashCode() + language.hashCode())
        val first = facts.getOrElse(0) { description.ifBlank { title } }
        val second = facts.getOrElse(1) { first }
        val third = facts.getOrElse(2) { second }
        val questions = listOf(
            createIdentityQuestion(title, description, facts, language, random),
            createEvidenceQuestion(title, first, facts, language, random),
            createRelationshipQuestion(title, second, third, language, random),
            createTrueFactQuestion(title, first, language),
            createFalseFactQuestion(title, third, language),
            createContextQuestion(title, extract, facts, language, random)
        )
        return questions.distinctBy { it.text }.take(7)
    }

    private fun createIdentityQuestion(
        title: String,
        description: String,
        facts: List<String>,
        language: String,
        random: Random
    ): Question {
        val correct = description.ifBlank { facts.firstOrNull() ?: "una voce enciclopedica" }
        val pool = (biographicalDistractors(language) + geographicalDistractors(language) +
                scienceDistractors(language) + generalDistractors(language))
            .filterNot { it.equals(correct, true) }
            .distinct()
            .shuffled(random)
        return multipleChoice(
            title = title,
            text = phrase(language, "Quale descrizione identifica meglio «$title»?", "Which description best identifies “$title”?"),
            correct = correct.replaceFirstChar { it.uppercase() },
            distractors = pool,
            explanation = facts.firstOrNull() ?: correct,
            language = language,
            random = random
        )
    }

    private fun createEvidenceQuestion(
        title: String,
        fact: String,
        facts: List<String>,
        language: String,
        random: Random
    ): Question {
        val alternatives = facts.drop(1) + generalDistractors(language)
        return multipleChoice(
            title = title,
            text = phrase(language, "Quale informazione trova conferma nel riassunto di «$title»?", "Which detail is supported by the summary of “$title”?"),
            correct = fact,
            distractors = alternatives + generalDistractors(language),
            explanation = fact,
            language = language,
            random = random
        )
    }

    private fun createRelationshipQuestion(
        title: String,
        firstFact: String,
        secondFact: String,
        language: String,
        random: Random
    ): Question {
        val correct = secondFact.take(110)
        return multipleChoice(
            title = title,
            text = phrase(language, "Quale conseguenza o caratteristica è collegata a «$title»?", "Which consequence or characteristic is connected to “$title”?"),
            correct = correct,
            distractors = if (language == "en") {
                listOf(firstFact, "A detail not present in the article", "An unsupported interpretation")
            } else {
                listOf(firstFact, "Un dettaglio non presente nella voce", "Un'interpretazione senza fonte")
            },
            explanation = secondFact,
            language = language,
            random = random
        )
    }

    private fun multipleChoice(
        title: String,
        text: String,
        correct: String,
        distractors: List<String>,
        explanation: String,
        language: String,
        random: Random
    ): Question {
        val options = (listOf(correct) + distractors.filter { it.isNotBlank() && it != correct }.distinct())
            .take(4)
            .shuffled(random)
        return Question(
            id = UUID.randomUUID().toString(),
            text = text,
            type = QuestionType.MULTIPLE_CHOICE,
            options = options,
            correctOptionIndex = options.indexOf(correct),
            explanation = explanation,
            wikiQuote = explanation
        )
    }

    private fun biographicalDistractors(language: String) =
        if (language == "en") englishBiographicalDistractors else italianBiographicalDistractors

    private fun geographicalDistractors(language: String) =
        if (language == "en") englishGeographicalDistractors else italianGeographicalDistractors

    private fun scienceDistractors(language: String) =
        if (language == "en") englishScienceDistractors else italianScienceDistractors

    private fun generalDistractors(language: String) =
        if (language == "en") englishGeneralDistractors else italianGeneralDistractors

    private fun createDefinitionQuestion(
        title: String,
        description: String,
        sentences: List<String>,
        language: String
    ): Question {
        val correctDef = if (description.isNotBlank()) {
            description.replaceFirstChar { it.uppercase() }
        } else if (sentences.isNotEmpty()) {
            sentences.first().take(90).trim()
        } else {
            "Voce enciclopedica di rilievo"
        }

        val distractorPool = when {
            description.contains("comune", true) || description.contains("città", true) ||
                    description.contains("isola", true) || description.contains("stato", true) -> geographicalDistractors(language)
            description.contains("persona", true) || description.contains("scrittore", true) ||
                    description.contains("pittore", true) || description.contains("calciatore", true) -> biographicalDistractors(language)
            else -> scienceDistractors(language) + generalDistractors(language)
        }

        val distractors = distractorPool.shuffled().take(3)
        val allOptions = (distractors + correctDef).shuffled()
        val correctIndex = allOptions.indexOf(correctDef)

        val explanation = sentences.firstOrNull() ?: "$title: $correctDef"

        return Question(
            id = UUID.randomUUID().toString(),
            text = phrase(language, "Quale delle seguenti definizioni descrive correttamente '$title'?", "Which definition best describes '$title'?"),
            type = QuestionType.MULTIPLE_CHOICE,
            options = allOptions,
            correctOptionIndex = correctIndex,
            explanation = explanation,
            wikiQuote = explanation
        )
    }

    private fun createTrueFactQuestion(title: String, sentence: String, language: String): Question {
        val trimmed = sentence.removeSuffix(".")
        val options = if (language == "en") listOf("True", "False") else listOf("Vero", "Falso")
        val correctIndex = 0 // "Vero"

        return Question(
            id = UUID.randomUUID().toString(),
            text = phrase(language, "Vero o Falso:\nSecondo Wikipedia, riguardo a '$title': \"$trimmed\".", "True or False:\nAccording to Wikipedia, about '$title': \"$trimmed\"."),
            type = QuestionType.TRUE_FALSE,
            options = options,
            correctOptionIndex = correctIndex,
            explanation = phrase(language, "Esatto! Come riportato dalla voce di Wikipedia: \"$sentence\"", "Correct! Wikipedia's article says: \"$sentence\""),
            wikiQuote = sentence
        )
    }

    private fun createSentenceDetailQuestion(title: String, sentence: String, language: String): Question {
        val correctOption = sentence.take(80).trim().removeSuffix(".")
        val distractors = generalDistractors(language).shuffled().take(3)
        val allOptions = (distractors + correctOption).shuffled()
        val correctIndex = allOptions.indexOf(correctOption)

        return Question(
            id = UUID.randomUUID().toString(),
            text = phrase(language, "Quale delle seguenti informazioni è confermata nel testo su '$title'?", "Which statement is confirmed by the article about '$title'?"),
            type = QuestionType.MULTIPLE_CHOICE,
            options = allOptions,
            correctOptionIndex = correctIndex,
            explanation = phrase(language, "La voce specifica: \"$sentence\"", "The article states: \"$sentence\""),
            wikiQuote = sentence
        )
    }

    private fun createFallbackDetailQuestion(title: String, description: String, extract: String, language: String): Question {
        val correctOption = if (description.isNotBlank()) description else title
        val distractors = biographicalDistractors(language).shuffled().take(3)
        val allOptions = (distractors + correctOption).shuffled()

        return Question(
            id = UUID.randomUUID().toString(),
            text = phrase(language, "Cosa caratterizza principalmente '$title'?", "What mainly characterizes '$title'?"),
            type = QuestionType.MULTIPLE_CHOICE,
            options = allOptions,
            correctOptionIndex = allOptions.indexOf(correctOption),
            explanation = extract.ifBlank {
                phrase(language, "Informazione verificata sul riassunto di Wikipedia.", "Verified information from Wikipedia's summary.")
            }
        )
    }

    private fun createFalseFactQuestion(title: String, sentence: String, language: String): Question {
        val fakeClaim = phrase(language, "è stato scoperto nel 2024 da una spedizione sottomarina alle Isole Figi", "was discovered in 2024 by an underwater expedition near Fiji")
        val statement = "'$title' $fakeClaim."
        val options = if (language == "en") listOf("True", "False") else listOf("Vero", "Falso")
        val correctIndex = 1 // "Falso"

        return Question(
            id = UUID.randomUUID().toString(),
            text = phrase(language, "Vero o Falso:\n\"$statement\"", "True or False:\n\"$statement\""),
            type = QuestionType.TRUE_FALSE,
            options = options,
            correctOptionIndex = correctIndex,
            explanation = phrase(language, "Falso! In realtà: \"$sentence\"", "False! In reality: \"$sentence\""),
            wikiQuote = sentence
        )
    }

    private fun createContextQuestion(
        title: String,
        extract: String,
        sentences: List<String>,
        language: String,
        random: Random
    ): Question {
        // Search for years / dates in the extract
        val yearMatch = Regex("\\b(1[0-9]{3}|20[0-2][0-9])\\b").find(extract)

        if (yearMatch != null) {
            val correctYear = yearMatch.value.toInt()
            val distractors = listOf(
                (correctYear - 45).toString(),
                (correctYear + 32).toString(),
                (correctYear - 110).toString()
            )
            val options = (distractors + correctYear.toString()).shuffled(random)

            val quote = sentences.find { it.contains(yearMatch.value) } ?: extract

            return Question(
                id = UUID.randomUUID().toString(),
                text = phrase(language, "In quale anno o periodo si colloca l'evento o la menzione storica di '$title'?", "Which year or period is associated with the event or historical reference to '$title'?"),
                type = QuestionType.MULTIPLE_CHOICE,
                options = options,
                correctOptionIndex = options.indexOf(correctYear.toString()),
                explanation = phrase(language, "Nel testo viene riportato: \"$quote\"", "The article reports: \"$quote\""),
                wikiQuote = quote
            )
        }

        // When no year is available, ask about a concrete fact rather than the
        // generic nature of the subject.
        val correctFact = sentences.firstOrNull()
            ?.take(110)
            ?: extract.take(110).ifBlank { title }
        val options = (listOf(correctFact) + (sentences.drop(1) + generalDistractors(language))
            .filter { it.isNotBlank() && it != correctFact }
            .distinct()
            .take(3))
            .shuffled(random)

        return Question(
            id = UUID.randomUUID().toString(),
            text = phrase(language, "Quale fatto concreto è riportato nella voce su '$title'?", "Which concrete fact is reported in the article about '$title'?"),
            type = QuestionType.MULTIPLE_CHOICE,
            options = options,
            correctOptionIndex = options.indexOf(correctFact),
            explanation = correctFact,
            wikiQuote = correctFact
        )
    }

    private fun createSynthesisQuestion(
        title: String,
        description: String,
        sentences: List<String>,
        language: String
    ): Question {
        val snippet = sentences.lastOrNull() ?: sentences.firstOrNull() ?: description
        val options = if (language == "en") listOf("True", "False") else listOf("Vero", "Falso")

        return Question(
            id = UUID.randomUUID().toString(),
            text = phrase(language, "Vero o Falso:\nLa voce su '$title' conclude o approfondisce specificando: \"${snippet.take(110)}...\".", "True or False:\nThe article about '$title' concludes or adds detail by stating: \"${snippet.take(110)}...\"."),
            type = QuestionType.TRUE_FALSE,
            options = options,
            correctOptionIndex = 0,
            explanation = phrase(language, "Esatto! Dal riassunto di Wikipedia: \"$snippet\"", "Correct! From Wikipedia's summary: \"$snippet\""),
            wikiQuote = snippet
        )
    }

    private fun phrase(language: String, italian: String, english: String): String =
        if (language == "en") english else italian

    /**
     * Curated offline fallback lessons guaranteeing 100% functionality without internet.
     */
    fun getCuratedOfflineSummary(topic: String? = null, language: String = "it"): WikiSummaryDto {
        if (language == "en") {
            val englishTopic = topic?.removePrefix("🏆 ")?.substringBefore(":")
                ?.ifBlank { "General knowledge" } ?: "General knowledge"
            return WikiSummaryDto(
                title = englishTopic,
                pageId = 900000L + englishTopic.hashCode().toLong().absoluteValue,
                description = "an encyclopedia topic selected for this lesson",
                extract = "$englishTopic is a documented subject explored through Wikipedia. " +
                    "This lesson highlights its history, key ideas and real-world impact.",
                lang = "en"
            )
        }
        val fallbacks = listOf(
            WikiSummaryDto(
                title = "Leonardo da Vinci",
                pageId = 1542,
                description = "scienziato, inventore e artista italiano",
                extract = "Leonardo da Vinci è stato uno dei più grandi geni dell'umanità. Attivo nel Rinascimento come pittore, scienziato, ingegnere e scultore, ha realizzato capolavori immortali come la Gioconda e l'Ultima Cena, oltre a pionieristici studi di anatomia.",
                lang = "it"
            ),
            WikiSummaryDto(
                title = "Colosseo",
                pageId = 3280,
                description = "anfiteatro romano situato nel centro di Roma",
                extract = "Il Colosseo, originariamente noto come Anfiteatro Flavio, è il più grande anfiteatro del mondo. Situato nel centro storico di Roma, è stato inserito nel 1980 nella lista dei Patrimoni dell'umanità dell'UNESCO e fa parte delle nuove sette meraviglie del mondo.",
                lang = "it"
            ),
            WikiSummaryDto(
                title = "Stretto di Messina",
                pageId = 8472,
                description = "stretto marittimo che separa la Sicilia dalla Calabria",
                extract = "Lo Stretto di Messina è uno stretto che unisce il mar Tirreno con il mar Ionio e separa la Sicilia dalla penisola italiana. Caratterizzato da intense correnti di marea e da un ricco patrimonio mitologico legato ai mostri marini Scilla e Cariddi.",
                lang = "it"
            ),
            WikiSummaryDto(
                title = "Telescopio Spaziale James Webb",
                pageId = 91823,
                description = "grande telescopio spaziale a infrarossi per l'astronomia",
                extract = "Il telescopio spaziale James Webb è un osservatorio spaziale sviluppato dalla NASA in collaborazione con l'ESA e la CSA. Lanciato nel dicembre 2021, opera in orbita attorno al punto di Lagrange L2 per osservare le prime galassie dell'universo.",
                lang = "it"
            ),
            WikiSummaryDto(
                title = "Acropoli di Atene",
                pageId = 125,
                description = "cittadella monumentale dell'antica Atene",
                extract = "L'Acropoli di Atene è una cittadella rocciosa che domina la capitale greca. Il Partenone, costruito nel V secolo avanti Cristo, è il suo monumento più celebre e uno dei simboli dell'architettura classica.",
                lang = "it"
            ),
            WikiSummaryDto(
                title = "Galileo Galilei",
                pageId = 1354,
                description = "astronomo, fisico e matematico italiano",
                extract = "Galileo Galilei è stato un astronomo, fisico e matematico italiano. Con le sue osservazioni telescopiche sostenne l'astronomia eliocentrica e contribuì allo sviluppo del metodo sperimentale.",
                lang = "it"
            ),
            WikiSummaryDto(
                title = "Alpi",
                pageId = 2361,
                description = "sistema montuoso dell'Europa centrale",
                extract = "Le Alpi sono una catena montuosa dell'Europa centrale che attraversa diversi Paesi. Il Monte Bianco è la vetta più alta e la regione alpina ospita ambienti, culture e paesaggi molto diversi.",
                lang = "it"
            ),
            WikiSummaryDto(
                title = "Dante Alighieri",
                pageId = 816,
                description = "poeta e scrittore italiano del Medioevo",
                extract = "Dante Alighieri è stato un poeta e scrittore italiano. La Divina Commedia, composta nel Medioevo, racconta il viaggio immaginario del poeta attraverso Inferno, Purgatorio e Paradiso.",
                lang = "it"
            ),
            WikiSummaryDto(
                title = "Wolfgang Amadeus Mozart",
                pageId = 206,
                description = "compositore e musicista austriaco",
                extract = "Wolfgang Amadeus Mozart è stato un compositore e musicista austriaco del XVIII secolo. La sua produzione comprende opere, sinfonie, concerti e musica da camera, ancora oggi eseguiti in tutto il mondo.",
                lang = "it"
            ),
            WikiSummaryDto(
                title = "Cinema",
                pageId = 5842,
                description = "arte e industria delle immagini in movimento",
                extract = "Il cinema è l'arte di rappresentare storie e idee attraverso immagini in movimento. Nato tra la fine dell'Ottocento e l'inizio del Novecento, è diventato una delle forme culturali più diffuse al mondo.",
                lang = "it"
            ),
            WikiSummaryDto(
                title = "Biodiversità",
                pageId = 532,
                description = "varietà della vita sulla Terra",
                extract = "La biodiversità indica la varietà degli organismi viventi, degli ecosistemi e dei patrimoni genetici. La sua conservazione è importante per l'equilibrio naturale e per il benessere delle società umane.",
                lang = "it"
            ),
            WikiSummaryDto(
                title = "Intelligenza artificiale",
                pageId = 198,
                description = "disciplina che studia sistemi capaci di svolgere compiti intelligenti",
                extract = "L'intelligenza artificiale è la disciplina che studia metodi e sistemi capaci di svolgere compiti associati all'intelligenza umana. Comprende apprendimento automatico, elaborazione del linguaggio e visione artificiale.",
                lang = "it"
            )
        )
        val normalizedTopic = topic.orEmpty().lowercase()
        return fallbacks.firstOrNull { summary ->
            val searchable = "${summary.title} ${summary.description} ${summary.extract}".lowercase()
            normalizedTopic.isNotBlank() &&
                    normalizedTopic.split(Regex("\\s+")).any { word ->
                        word.length >= 5 && searchable.contains(word)
                    }
        } ?: fallbacks.random()
    }
}
