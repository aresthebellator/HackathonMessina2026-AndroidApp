package com.exertia.wikingo.presentation.trainer

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.exertia.wikingo.R
import com.exertia.wikingo.core.designsystem.DuoBackground
import com.exertia.wikingo.core.designsystem.DuoBlue
import com.exertia.wikingo.core.designsystem.DuoBlueDark
import com.exertia.wikingo.core.designsystem.DuoBlueLight
import com.exertia.wikingo.core.designsystem.DuoBorder
import com.exertia.wikingo.core.designsystem.DuoGreen
import com.exertia.wikingo.core.designsystem.DuoGreenDark
import com.exertia.wikingo.core.designsystem.DuoInk
import com.exertia.wikingo.core.designsystem.DuoInkSecondary
import com.exertia.wikingo.core.designsystem.DuoRed
import com.exertia.wikingo.core.designsystem.DuoSurface
import com.exertia.wikingo.core.designsystem.DuoYellowDark
import com.exertia.wikingo.core.designsystem.DuoYellowLight
import com.exertia.wikingo.core.designsystem.components.DuolingoButton
import com.exertia.wikingo.core.designsystem.components.DuolingoButtonStyle
import com.exertia.wikingo.core.designsystem.components.DuolingoProgressBar
import com.exertia.wikingo.core.designsystem.components.FeedbackSheet
import com.exertia.wikingo.core.designsystem.components.MascotReaction
import com.exertia.wikingo.core.designsystem.components.OptionCard
import com.exertia.wikingo.core.designsystem.components.OptionCardState
import com.exertia.wikingo.core.designsystem.components.StreakHeader
import com.exertia.wikingo.core.designsystem.LocalReduceMotion
import com.exertia.wikingo.core.i18n.i18n
import com.exertia.wikingo.data.local.SavedArticle

@Composable
fun TrainerScreen(
    viewModel: TrainerViewModel,
    onNavigateToHistory: () -> Unit,
    onNavigateHome: () -> Unit = onNavigateToHistory,
    onSaveArticle: (SavedArticle) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val reduceMotion = LocalReduceMotion.current

    // Drive a smooth slide-up + fade-in entrance when the screen first appears
    var screenVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { screenVisible = true }

    AnimatedVisibility(
        visible = screenVisible,
        enter = if (reduceMotion) fadeIn() else {
            slideInVertically(
                initialOffsetY = { fullHeight -> (fullHeight * 0.08f).toInt() },
                animationSpec = androidx.compose.animation.core.spring(
                    dampingRatio = androidx.compose.animation.core.Spring.DampingRatioLowBouncy,
                    stiffness = androidx.compose.animation.core.Spring.StiffnessMediumLow
                )
            ) + fadeIn(animationSpec = tween(durationMillis = 280))
        },
        exit = fadeOut(animationSpec = tween(if (reduceMotion) 0 else 180)),
        modifier = modifier.fillMaxSize()
    ) {
        Scaffold(
            containerColor = DuoBackground,
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                AnimatedContent(
                    targetState = uiState,
                    transitionSpec = {
                        // Loading→Question: slide up (sense of the lesson "arriving")
                        // Any other transition: fade
                        if (initialState is TrainerUiState.Loading && targetState is TrainerUiState.QuestionState) {
                            (slideInVertically(
                                initialOffsetY = { (it * 0.12f).toInt() },
                                animationSpec = tween(if (reduceMotion) 0 else 340)
                            ) + fadeIn(tween(if (reduceMotion) 0 else 300))) togetherWith
                                fadeOut(tween(if (reduceMotion) 0 else 150))
                        } else {
                            fadeIn(animationSpec = tween(if (reduceMotion) 0 else 250)) togetherWith
                                fadeOut(animationSpec = tween(if (reduceMotion) 0 else 200))
                        }
                    },
                    label = "screenTransition"
                ) { state ->
                    when (state) {
                        is TrainerUiState.Loading -> LoadingView(state.message)
                        is TrainerUiState.QuestionState -> QuestionView(
                            state = state,
                            onSelectOption = { viewModel.onEvent(TrainerUiEvent.SelectOption(it)) },
                            onCheckAnswer = { viewModel.onEvent(TrainerUiEvent.CheckAnswer) },
                            onNextQuestion = { viewModel.onEvent(TrainerUiEvent.NextQuestion) },
                            onToggleSound = { viewModel.onEvent(TrainerUiEvent.ToggleSound) },
                            onOpenHistory = onNavigateToHistory
                        )
                        is TrainerUiState.CompleteState -> CompleteView(
                            state = state,
                            onNewLesson = { viewModel.onEvent(TrainerUiEvent.StartNewLesson) },
                            onNavigateHome = onNavigateHome,
                            onOpenHistory = onNavigateToHistory,
                            onSaveArticle = onSaveArticle
                        )
                        is TrainerUiState.Error -> ErrorView(
                            message = state.message,
                            onRetry = { viewModel.onEvent(TrainerUiEvent.Retry) }
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun QuestionView(
    state: TrainerUiState.QuestionState,
    onSelectOption: (Int) -> Unit,
    onCheckAnswer: () -> Unit,
    onNextQuestion: () -> Unit,
    onToggleSound: () -> Unit,
    onOpenHistory: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var isTopicHelpVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 140.dp) // Leave space for bottom button/sheet
        ) {
            // Top Bar: Streak, XP, Hearts
            StreakHeader(
                streakDays = state.userStats.currentStreak,
                totalXp = state.userStats.totalXp,
                hearts = state.hearts,
                onCloseClick = onOpenHistory
            )

            // Step Progress Bar and sound toggle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            ) {
                DuolingoProgressBar(
                    progress = state.progress,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Icon(
                    imageVector = if (state.isSoundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                    contentDescription = i18n("common.sound"),
                    tint = DuoInkSecondary,
                    modifier = Modifier
                        .size(26.dp)
                        .clickable { onToggleSound() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Topic Pill Badge
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(DuoBlueLight)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "WIKIPEDIA • ${state.session.topicTitle.uppercase()}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = DuoBlueDark,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Companion tip or prompt
            MascotReaction(
                message = i18n("trainer.question_header", "current" to state.questionNumber, "total" to state.totalQuestions),
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Question Text
            Text(
                text = state.currentQuestion.text,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DuoInk,
                lineHeight = 26.sp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            DuolingoButton(
                text = i18n("trainer.explain_topic"),
                style = DuolingoButtonStyle.OUTLINE,
                onClick = { isTopicHelpVisible = true },
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Options List
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                state.currentQuestion.options.forEachIndexed { index, optionText ->
                    val optionLabel = when (index) {
                        0 -> "A"
                        1 -> "B"
                        2 -> "C"
                        3 -> "D"
                        else -> "${index + 1}"
                    }

                    val cardState = when {
                        state.feedback != null -> {
                            val isCorrectOption = index == state.feedback.correctOptionIndex
                            val isSelectedOption = index == state.feedback.selectedOptionIndex
                            when {
                                isCorrectOption -> OptionCardState.CORRECT
                                isSelectedOption && !state.feedback.isCorrect -> OptionCardState.WRONG
                                else -> OptionCardState.IDLE
                            }
                        }
                        index == state.selectedOptionIndex -> OptionCardState.SELECTED
                        else -> OptionCardState.IDLE
                    }

                    OptionCard(
                        text = optionText,
                        indexLabel = optionLabel,
                        state = cardState,
                        enabled = !state.isAnswerChecked,
                        onClick = { onSelectOption(index) }
                    )
                }
            }

            if (isTopicHelpVisible) {
                AlertDialog(
                    onDismissRequest = { isTopicHelpVisible = false },
                    title = {
                        Text(
                            text = state.session.topicTitle,
                            fontWeight = FontWeight.ExtraBold
                        )
                    },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (state.session.topicDescription.isNotBlank()) {
                                Text(
                                    text = state.session.topicDescription,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = state.session.topicExtract
                                    .takeIf { it.isNotBlank() }
                                    ?.let { extract -> extract.take(500) + if (extract.length > 500) "..." else "" }
                                    ?: i18n("trainer.topic_help_unavailable")
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { isTopicHelpVisible = false }) {
                            Text(i18n("common.got_it"))
                        }
                    }
                )
            }
        }

        // Bottom Action Bar: "VERIFICA" or Feedback Banner Sheet
        if (!state.isAnswerChecked) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(DuoBackground)
                    .border(width = 1.dp, color = DuoBorder)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                DuolingoButton(
                    text = i18n("trainer.verify_button"),
                    onClick = onCheckAnswer,
                    enabled = state.canCheckAnswer,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            val feedback = state.feedback
            if (feedback != null) {
                FeedbackSheet(
                    visible = true,
                    isCorrect = feedback.isCorrect,
                    correctAnswerText = feedback.correctAnswerText,
                    explanation = feedback.explanation,
                    showDeepeningAction = state.currentQuestion.shouldOfferDeepening,
                    onDeepen = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(state.session.wikiUrl))
                        context.startActivity(intent)
                    },
                    onContinue = onNextQuestion,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Composable
private fun CompleteView(
    state: TrainerUiState.CompleteState,
    onNewLesson: () -> Unit,
    onNavigateHome: () -> Unit,
    onOpenHistory: () -> Unit,
    onSaveArticle: (SavedArticle) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val reduceMotion = LocalReduceMotion.current

    // Celebratory entrance animations
    var mascotVisible by remember { mutableStateOf(false) }
    var contentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        mascotVisible = true
        kotlinx.coroutines.delay(if (reduceMotion) 0L else 180L)
        contentVisible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onNavigateHome) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = i18n("common.back"),
                    tint = DuoInk
                )
            }
        }

        // Mascot Trophy — bounces in with a spring
        AnimatedVisibility(
            visible = mascotVisible,
            enter = if (reduceMotion) fadeIn() else scaleIn(
                initialScale = 0.4f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ) + fadeIn(animationSpec = tween(220))
        ) {
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_viking),
                    contentDescription = "Completato",
                    modifier = Modifier.size(100.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Title + stats slide up with a slight delay after mascot
        AnimatedVisibility(
            visible = contentVisible,
            enter = if (reduceMotion) fadeIn() else slideInVertically(
                initialOffsetY = { (it * 0.3f).toInt() },
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
            ) + fadeIn(animationSpec = tween(280))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = i18n("trainer.lesson_complete_title"),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    color = DuoInk,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = i18n("trainer.lesson_complete_subtitle"),
                    fontSize = 15.sp,
                    color = DuoInkSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))


        // Wikipedia Article Summary Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(DuoSurface)
                .border(2.dp, DuoBorder, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column {
                if (state.thumbnailUrl != null) {
                    AsyncImage(
                        model = state.thumbnailUrl,
                        contentDescription = state.topicTitle,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Text(
                    text = state.topicTitle,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DuoInk
                )
                if (state.topicDescription.isNotBlank()) {
                    Text(
                        text = state.topicDescription,
                        fontSize = 13.sp,
                        color = DuoInkSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                if (state.topicExtract.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.topicExtract.take(220) + "...",
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = DuoInk
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3 Gamification Stat Tiles
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // XP Tile
            StatTile(
                iconRes = R.drawable.ic_bolt,
                label = i18n("trainer.stat_total_xp"),
                value = "+${state.xpEarned}",
                color = DuoInk,
                modifier = Modifier.weight(1f)
            )

            // Accuracy Tile
            StatTile(
                iconRes = R.drawable.ic_check,
                label = i18n("trainer.stat_accuracy"),
                value = "${state.accuracy}%",
                color = DuoInk,
                modifier = Modifier.weight(1f)
            )

            // Streak Tile
            StatTile(
                iconRes = R.drawable.ic_flame,
                label = i18n("trainer.stat_streak"),
                value = "${state.userStats.currentStreak} ${i18n("dashboard.streak_unit")}",
                color = DuoInk,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Action Buttons
        DuolingoButton(
            text = i18n("trainer.new_lesson_button"),
            style = DuolingoButtonStyle.PRIMARY,
            onClick = onNewLesson,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        DuolingoButton(
            text = i18n("trainer.read_on_wiki"),
            style = DuolingoButtonStyle.OUTLINE,
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(state.wikiUrl))
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        DuolingoButton(
            text = i18n("trainer.save_article"),
            style = DuolingoButtonStyle.OUTLINE,
            onClick = {
                onSaveArticle(
                    SavedArticle(
                        pageId = state.session.pageId,
                        title = state.topicTitle,
                        description = state.topicDescription,
                        extract = state.topicExtract,
                        thumbnailUrl = state.thumbnailUrl,
                        wikiUrl = state.wikiUrl
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = i18n("trainer.view_history_stats"),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = com.exertia.wikingo.core.designsystem.WikiShuttleGray,
            modifier = Modifier
                .clickable { onOpenHistory() }
                .padding(8.dp)
        )
    }
}

@Composable
private fun StatTile(
    iconRes: Int,
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(DuoSurface)
            .border(2.dp, DuoBorder, RoundedCornerShape(14.dp))
            .padding(vertical = 12.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = DuoInkSecondary
            )
        }
    }
}

@Composable
private fun LoadingView(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.viking_loading),
            contentDescription = "Caricamento della nuova lezione",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        CircularProgressIndicator(
            color = DuoInk,
            strokeWidth = 4.dp,
            modifier = Modifier.size(42.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = message,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = DuoInk,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = i18n("trainer.loading_subtitle"),
            fontSize = 13.sp,
            color = DuoInkSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_viking),
            contentDescription = i18n("common.error"),
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = i18n("trainer.error_title"),
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = DuoInk,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            fontSize = 14.sp,
            color = DuoInkSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        DuolingoButton(
            text = i18n("common.retry").uppercase(),
            onClick = onRetry,
            style = DuolingoButtonStyle.PRIMARY,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
