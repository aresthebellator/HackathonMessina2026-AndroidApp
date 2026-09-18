package com.hackaton.wikitrainer.data.generator

import com.hackaton.wikitrainer.data.remote.dto.WikiSummaryDto
import com.hackaton.wikitrainer.domain.model.Question
import com.hackaton.wikitrainer.domain.model.QuestionType
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

    private val biographicalDistractors = listOf(
        "Un matematico e astronomo dell'antica Grecia",
        "Un generale dell'impero persiano durante le guerre mediche",
        "Un pittore fiammingo del periodo barocco",
        "Un esploratore portoghese che circumnavigò l'Africa",
        "Un compositore classico austriaco del XVIII secolo",
        "Un filosofo illuminista francese autore di saggi politici",
        "Un pioniere dell'aviazione e ingegnere meccanico",
        "Un medico e biologo scopritore di vaccini moderni"
    )

    private val geographicalDistractors = listOf(
        "Un'isola vulcanica situata nell'arcipelago polinesiano",
        "Una catena montuosa che separa due continenti",
        "Un antico porto fluviale della Mesopotamia",
        "Una regione desertica dell'Africa subsahariana",
        "Una città costiera fondata dai coloni fenici",
        "Un ghiacciaio perenne situato nelle Alpi scandinave",
        "Un parco nazionale protetto nell'America centrale"
    )

    private val scienceDistractors = listOf(
        "Un principio fondamentale della termodinamica quantistica",
        "Un elemento chimico sintetizzato in laboratorio nel 1974",
        "Una cometa periodica visibile a occhio nudo ogni 76 anni",
        "Un processo biologico di fotosintesi anaerobica",
        "Una missione spaziale robotica inviata verso Giove",
        "Una teoria geologica sulla tettonica delle placche continentali"
    )

    private val generalDistractors = listOf(
        "Un trattato diplomatico firmato al termine della guerra dei trent'anni",
        "Un movimento artistico d'avanguardia nato a inizio Novecento",
        "Un manoscritto medievale conservato nella biblioteca vaticana",
        "Uno strumento musicale tradizionale a fiato",
        "Un'opera teatrale in versi scritta durante il Rinascimento"
    )

    fun generateQuestions(summary: WikiSummaryDto): List<Question> {
        val title = summary.title.ifBlank { "Soggetto sconosciuto" }
        val description = summary.description ?: ""
        val extract = summary.extract ?: ""

        val sentences = extract
            .split(Regex("(?<=[.!?])\\s+"))
            .map { it.trim() }
            .filter { it.length > 25 }

        val questions = mutableListOf<Question>()

        // 1. Definition / Identity Question (Multiple Choice 4 Options)
        questions.add(createDefinitionQuestion(title, description, sentences))

        // 2. True / False Fact Question (Answer: True)
        if (sentences.isNotEmpty()) {
            questions.add(createTrueFactQuestion(title, sentences.first()))
        }

        // 3. Extract Specific Attribute Question (Multiple Choice 4 Options)
        if (sentences.size > 1) {
            questions.add(createSentenceDetailQuestion(title, sentences[1]))
        } else {
            questions.add(createFallbackDetailQuestion(title, description, extract))
        }

        // 4. True / False Negative / Distractor Question (Answer: False)
        val candidateSentence = sentences.getOrNull(2) ?: sentences.getOrNull(0) ?: extract
        questions.add(createFalseFactQuestion(title, candidateSentence))

        // 5. Date / Location or Context Question (Multiple Choice 4 Options)
        questions.add(createContextQuestion(title, extract, sentences))

        // 6. Synthesis / Summary Fact Question
        questions.add(createSynthesisQuestion(title, description, sentences))

        // Ensure we always provide between 5 and 7 questions
        return questions.take(7)
    }

    private fun createDefinitionQuestion(
        title: String,
        description: String,
        sentences: List<String>
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
                    description.contains("isola", true) || description.contains("stato", true) -> geographicalDistractors
            description.contains("persona", true) || description.contains("scrittore", true) ||
                    description.contains("pittore", true) || description.contains("calciatore", true) -> biographicalDistractors
            else -> scienceDistractors + generalDistractors
        }

        val distractors = distractorPool.shuffled().take(3)
        val allOptions = (distractors + correctDef).shuffled()
        val correctIndex = allOptions.indexOf(correctDef)

        val explanation = sentences.firstOrNull() ?: "$title: $correctDef"

        return Question(
            id = UUID.randomUUID().toString(),
            text = "Quale delle seguenti definizioni descrive correttamente '$title'?",
            type = QuestionType.MULTIPLE_CHOICE,
            options = allOptions,
            correctOptionIndex = correctIndex,
            explanation = explanation,
            wikiQuote = explanation
        )
    }

    private fun createTrueFactQuestion(title: String, sentence: String): Question {
        val trimmed = sentence.removeSuffix(".")
        val options = listOf("Vero", "Falso")
        val correctIndex = 0 // "Vero"

        return Question(
            id = UUID.randomUUID().toString(),
            text = "Vero o Falso:\nSecondo Wikipedia, riguardo a '$title': \"$trimmed\".",
            type = QuestionType.TRUE_FALSE,
            options = options,
            correctOptionIndex = correctIndex,
            explanation = "Esatto! Come riportato dalla voce di Wikipedia: \"$sentence\"",
            wikiQuote = sentence
        )
    }

    private fun createSentenceDetailQuestion(title: String, sentence: String): Question {
        val correctOption = sentence.take(80).trim().removeSuffix(".")
        val distractors = generalDistractors.shuffled().take(3)
        val allOptions = (distractors + correctOption).shuffled()
        val correctIndex = allOptions.indexOf(correctOption)

        return Question(
            id = UUID.randomUUID().toString(),
            text = "Quale delle seguenti informazioni è confermata nel testo su '$title'?",
            type = QuestionType.MULTIPLE_CHOICE,
            options = allOptions,
            correctOptionIndex = correctIndex,
            explanation = "La voce specifica: \"$sentence\"",
            wikiQuote = sentence
        )
    }

    private fun createFallbackDetailQuestion(title: String, description: String, extract: String): Question {
        val correctOption = if (description.isNotBlank()) description else title
        val distractors = biographicalDistractors.shuffled().take(3)
        val allOptions = (distractors + correctOption).shuffled()

        return Question(
            id = UUID.randomUUID().toString(),
            text = "Cosa caratterizza principalmente '$title'?",
            type = QuestionType.MULTIPLE_CHOICE,
            options = allOptions,
            correctOptionIndex = allOptions.indexOf(correctOption),
            explanation = extract.ifBlank { "Informazione verificata sul riassunto di Wikipedia." }
        )
    }

    private fun createFalseFactQuestion(title: String, sentence: String): Question {
        val fakeClaim = "è stato scoperto nel 2024 da una spedizione sottomarina alle Isole Figi"
        val statement = "'$title' $fakeClaim."
        val options = listOf("Vero", "Falso")
        val correctIndex = 1 // "Falso"

        return Question(
            id = UUID.randomUUID().toString(),
            text = "Vero o Falso:\n\"$statement\"",
            type = QuestionType.TRUE_FALSE,
            options = options,
            correctOptionIndex = correctIndex,
            explanation = "Falso! In realtà: \"$sentence\"",
            wikiQuote = sentence
        )
    }

    private fun createContextQuestion(
        title: String,
        extract: String,
        sentences: List<String>
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
            val options = (distractors + correctYear.toString()).shuffled()

            val quote = sentences.find { it.contains(yearMatch.value) } ?: extract

            return Question(
                id = UUID.randomUUID().toString(),
                text = "In quale anno o periodo si colloca l'evento o la menzione storica di '$title'?",
                type = QuestionType.MULTIPLE_CHOICE,
                options = options,
                correctOptionIndex = options.indexOf(correctYear.toString()),
                explanation = "Nel testo viene riportato: \"$quote\"",
                wikiQuote = quote
            )
        }

        // Alternative context question
        val options = listOf(
            "Ambito culturale ed enciclopedico documentato",
            "Mito popolare senza riscontro storico",
            "Personaggio fittizio creato nel 2021",
            "Brevetto commerciale non riconosciuto"
        ).shuffled()

        val correctIndex = options.indexOf("Ambito culturale ed enciclopedico documentato")

        return Question(
            id = UUID.randomUUID().toString(),
            text = "Qual è la natura generale del contenuto di '$title'?",
            type = QuestionType.MULTIPLE_CHOICE,
            options = options,
            correctOptionIndex = correctIndex,
            explanation = sentences.firstOrNull() ?: extract
        )
    }

    private fun createSynthesisQuestion(
        title: String,
        description: String,
        sentences: List<String>
    ): Question {
        val snippet = sentences.lastOrNull() ?: sentences.firstOrNull() ?: description
        val options = listOf("Vero", "Falso")

        return Question(
            id = UUID.randomUUID().toString(),
            text = "Vero o Falso:\nLa voce su '$title' conclude o approfondisce specificando: \"${snippet.take(110)}...\".",
            type = QuestionType.TRUE_FALSE,
            options = options,
            correctOptionIndex = 0,
            explanation = "Esatto! Dal riassunto di Wikipedia: \"$snippet\"",
            wikiQuote = snippet
        )
    }

    /**
     * Curated offline fallback lessons guaranteeing 100% functionality without internet.
     */
    fun getCuratedOfflineSummary(): WikiSummaryDto {
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
            )
        )
        return fallbacks.random()
    }
}
