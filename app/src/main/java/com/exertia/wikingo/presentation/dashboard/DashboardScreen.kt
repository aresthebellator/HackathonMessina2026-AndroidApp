package com.exertia.wikingo.presentation.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.exertia.wikingo.R
import com.exertia.wikingo.core.designsystem.DuoBackground
import com.exertia.wikingo.core.designsystem.DuoBlue
import com.exertia.wikingo.core.designsystem.DuoBlueLight
import com.exertia.wikingo.core.designsystem.DuoBorder
import com.exertia.wikingo.core.designsystem.DuoBorderDark
import com.exertia.wikingo.core.designsystem.DuoGreen
import com.exertia.wikingo.core.designsystem.DuoGreenDark
import com.exertia.wikingo.core.designsystem.DuoGreenLight
import com.exertia.wikingo.core.designsystem.DuoInk
import com.exertia.wikingo.core.designsystem.DuoInkSecondary
import com.exertia.wikingo.core.designsystem.DuoRed
import com.exertia.wikingo.core.designsystem.DuoSurface
import com.exertia.wikingo.core.designsystem.LocalReduceMotion
import com.exertia.wikingo.core.i18n.i18n
import com.exertia.wikingo.domain.model.UserStats
import com.exertia.wikingo.domain.model.MAX_LESSON_NUMBER
import com.exertia.wikingo.domain.model.getLessonTitle
import com.exertia.wikingo.domain.model.getUnitForLesson
import com.exertia.wikingo.domain.model.getLessonTopic
import com.exertia.wikingo.presentation.dashboard.components.DuolingoPath
import kotlin.math.roundToInt

@Composable
fun DashboardScreen(
    stats: UserStats,
    language: String,
    darkMode: Boolean,
    onLanguageToggle: () -> Unit,
    onThemeToggle: () -> Unit,
    onStartLesson: (topic: String?, lessonNumber: Int?) -> Unit,
    onQuickQuiz: () -> Unit,
    onHistory: () -> Unit,
    onSettings: () -> Unit,
    onSavedArticles: () -> Unit,
    modifier: Modifier = Modifier
) {
    var logoTapCount by remember { mutableStateOf(0) }
    var showHackathonScreen by remember { mutableStateOf(false) }
    var isStartingLesson by remember { mutableStateOf(false) }
    var pendingLesson by remember { mutableStateOf<Pair<String?, Int?>?>(null) }
    var lessonLaunchCenter by remember { mutableStateOf<Offset?>(null) }
    val lessonLaunchProgress = remember { Animatable(0f) }
    val reduceMotion = LocalReduceMotion.current
    val logoScale by animateFloatAsState(
        targetValue = if (logoTapCount in 1..9) 1.12f else 1f,
        animationSpec = tween(if (reduceMotion) 0 else 180),
        label = "logoTapScale"
    )

    LaunchedEffect(logoTapCount) {
        if (logoTapCount == 10) {
            kotlinx.coroutines.delay(if (reduceMotion) 0 else 320)
            showHackathonScreen = true
        }
    }

    LaunchedEffect(isStartingLesson) {
        if (isStartingLesson) {
            lessonLaunchProgress.snapTo(if (reduceMotion) 1f else 0f)
            if (!reduceMotion) {
                kotlinx.coroutines.delay(140)
                lessonLaunchProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 680,
                        easing = FastOutSlowInEasing
                    )
                )
            }
            val lesson = pendingLesson
            if (lesson != null) {
                onStartLesson(lesson.first, lesson.second)
            }
        }
    }

    fun startLessonWithTransition(
        topic: String?,
        lessonNumber: Int?,
        sourceCenter: Offset?
    ) {
        if (!isStartingLesson) {
            pendingLesson = topic to lessonNumber
            lessonLaunchCenter = sourceCenter
            isStartingLesson = true
        }
    }

    val currentLesson = (stats.totalLessonsCompleted + 1).coerceAtMost(MAX_LESSON_NUMBER)
    val currentUnit = getUnitForLesson(currentLesson)
    val currentLessonTitle = getLessonTitle(currentLesson, language)
    val launchingLessonNumber = pendingLesson?.second ?: currentLesson
    val launchingUnit = getUnitForLesson(launchingLessonNumber)
    val unitCompletedCount = (currentUnit.startLesson..currentUnit.endLesson)
        .count { it <= stats.totalLessonsCompleted }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DuoSurface)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Sticky Top Header (Brand + Stats + Actions)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, DuoBorder)
            ) {
                Column(modifier = Modifier.statusBarsPadding()) {
                    // Top App Bar row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Viking Logo Box
                        Box(
                            modifier = Modifier
                                .size(42.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_launcher_viking),
                                contentDescription = "Logo Wikingo",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(42.dp)
                                    .scale(logoScale)
                                    .clickable {
                                        if (!showHackathonScreen) {
                                            logoTapCount = (logoTapCount + 1).coerceAtMost(10)
                                        }
                                    }
                            )
                        }

                        Spacer(Modifier.width(10.dp))

                        // Title
                        Text(
                            text = "Wikingo",
                            color = DuoInk,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.weight(1f)
                        )

                        // Language Toggle Pill
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoBorder),
                            modifier = Modifier.clickable(onClick = onLanguageToggle)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Language,
                                    contentDescription = i18n("common.language"),
                                    tint = com.exertia.wikingo.core.designsystem.WikiShuttleGray,
                                    modifier = Modifier.size(14.dp)
                                 )
                                 Spacer(Modifier.width(4.dp))
                                 Text(
                                     text = if (language == "it") i18n("dashboard.language_toggle_it") else i18n("dashboard.language_toggle_en"),
                                     fontSize = 11.sp,
                                     fontWeight = FontWeight.Black,
                                     color = DuoInk
                                 )
                             }
                         }

                         Spacer(Modifier.width(6.dp))

                         // Light/dark theme toggle
                         IconButton(
                             onClick = onThemeToggle,
                             modifier = Modifier.size(36.dp)
                         ) {
                             Icon(
                                 imageVector = if (darkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                                 contentDescription = i18n("settings.dark_theme"),
                                 tint = DuoInkSecondary,
                                 modifier = Modifier.size(20.dp)
                             )
                         }

                         // Saved Articles
                         IconButton(
                             onClick = onSavedArticles,
                             modifier = Modifier.size(36.dp)
                         ) {
                             Icon(
                                 Icons.Default.Bookmark,
                                 contentDescription = i18n("dashboard.saved_articles_tooltip"),
                                 tint = DuoInkSecondary,
                                 modifier = Modifier.size(20.dp)
                             )
                         }

                         // Settings & Accessibility
                         IconButton(
                             onClick = onSettings,
                             modifier = Modifier.size(36.dp)
                         ) {
                             Icon(
                                 Icons.Default.Settings,
                                 contentDescription = i18n("dashboard.settings_tooltip"),
                                 tint = DuoInkSecondary,
                                 modifier = Modifier.size(20.dp)
                             )
                         }
                     }

                     // Gamification Stats Row
                     Row(
                         modifier = Modifier
                             .fillMaxWidth()
                             .padding(horizontal = 16.dp, vertical = 4.dp),
                         horizontalArrangement = Arrangement.SpaceEvenly,
                         verticalAlignment = Alignment.CenterVertically
                     ) {
                         StatPill("🔥", "${stats.currentStreak} ${i18n("dashboard.streak_unit")}", i18n("dashboard.streak_label"), DuoInk)
                         StatPill("⚡", "${stats.totalXp} ${i18n("dashboard.xp_suffix")}", i18n("dashboard.xp_label"), DuoInk)
                         StatPill("✓", "${stats.totalLessonsCompleted}/$MAX_LESSON_NUMBER", i18n("dashboard.lessons_label"), DuoInk)
                         IconButton(onClick = onHistory, modifier = Modifier.size(36.dp)) {
                             Icon(
                                 Icons.Default.History,
                                 contentDescription = i18n("dashboard.history_tooltip"),
                                 tint = DuoInkSecondary,
                                 modifier = Modifier.size(20.dp)
                             )
                         }
                     }

                     HorizontalDivider(color = DuoBorder, thickness = 1.dp)

                     // Sub-Bar: Current Unit Info & Quick Quiz Button
                     Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(com.exertia.wikingo.core.designsystem.WikiSurfaceAlt)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                         Row(
                             verticalAlignment = Alignment.CenterVertically,
                             modifier = Modifier.weight(1f)
                         ) {
                             Box(
                                 modifier = Modifier
                                     .size(10.dp)
                                     .clip(CircleShape)
                                     .background(currentUnit.primaryColor)
                             )
                             Spacer(Modifier.width(8.dp))
                             Text(
                                 text = "${currentUnit.localizedTitle(language)}: ${currentUnit.localizedTopic(language)} ($unitCompletedCount/${currentUnit.endLesson - currentUnit.startLesson + 1})",
                                 fontSize = 12.sp,
                                 fontWeight = FontWeight.Bold,
                                 color = DuoInk,
                                 maxLines = 1,
                                 overflow = TextOverflow.Ellipsis
                             )
                         }

                         Spacer(Modifier.width(8.dp))

                         // Quick Quiz Button
                         Surface(
                             shape = RoundedCornerShape(10.dp),
                             color = com.exertia.wikingo.core.designsystem.WikiSurfaceAlt,
                             border = androidx.compose.foundation.BorderStroke(1.dp, com.exertia.wikingo.core.designsystem.WikiSilverSand),
                             modifier = Modifier.clickable(onClick = onQuickQuiz)
                         ) {
                             Row(
                                 modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                 verticalAlignment = Alignment.CenterVertically
                             ) {
                                 Icon(
                                     imageVector = Icons.Default.Shuffle,
                                     contentDescription = null,
                                     tint = DuoInk,
                                     modifier = Modifier.size(13.dp)
                                 )
                                 Spacer(Modifier.width(4.dp))
                                 Text(
                                     text = i18n("dashboard.quick_quiz"),
                                     fontSize = 11.sp,
                                     fontWeight = FontWeight.Black,
                                     color = DuoInk
                                 )
                             }
                         }
                     }
                }
            }

            // Main Winding Duolingo Path
            DuolingoPath(
                currentLessonIndex = currentLesson,
                completedLessonsCount = stats.totalLessonsCompleted,
                onStartLesson = { lessonNum, _, sourceCenter ->
                    startLessonWithTransition(
                        topic = getLessonTopic(lessonNum, language),
                        lessonNumber = lessonNum,
                        sourceCenter = sourceCenter
                    )
                },
                modifier = Modifier.weight(1f)
            )
        }

        // Floating Sticky Bottom Bar (Continue Current Lesson)
        FloatingContinueBar(
            currentLesson = currentLesson,
            lessonTitle = currentLessonTitle,
            unit = currentUnit,
            onContinue = {
                val topic = getLessonTopic(currentLesson, language)
                startLessonWithTransition(
                    topic = topic,
                    lessonNumber = currentLesson,
                    sourceCenter = lessonLaunchCenter
                )
            },
            isLaunching = isStartingLesson,
            onClickPosition = { lessonLaunchCenter = it },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        )

        if (isStartingLesson) {
            LessonLaunchOverlay(
                unit = launchingUnit,
                expansion = lessonLaunchProgress.value,
                sourceCenter = lessonLaunchCenter
            )
        }

        AnimatedVisibility(
            visible = showHackathonScreen,
            enter = if (reduceMotion) fadeIn() else fadeIn(tween(260)) + scaleIn(tween(320)),
            exit = if (reduceMotion) fadeOut() else fadeOut(tween(180)) + scaleOut(tween(220)),
            modifier = Modifier.fillMaxSize()
        ) {
            HackathonScreen(
                onClose = {
                    showHackathonScreen = false
                    logoTapCount = 0
                }
            )
        }
    }
}

@Composable
private fun HackathonScreen(onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .clickable(onClick = onClose),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_viking),
                contentDescription = "Vichingo Wikingo",
                modifier = Modifier.size(180.dp)
            )
            Spacer(modifier = Modifier.height(26.dp))
            Text(
                text = "Wikingo",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Black,
                color = DuoInk
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Hackaton Messina 2026",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Un piccolo easter egg per chi esplora ogni angolo di Wikingo.",
                style = MaterialTheme.typography.bodyMedium,
                color = DuoInkSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Tocca per tornare indietro",
                style = MaterialTheme.typography.labelLarge,
                color = DuoInkSecondary
            )
        }
    }
}

@Composable
private fun FloatingContinueBar(
    currentLesson: Int,
    lessonTitle: String,
    unit: com.exertia.wikingo.domain.model.PathUnit,
    onContinue: () -> Unit,
    isLaunching: Boolean,
    onClickPosition: (Offset) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(2.dp, DuoBorder),
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(if (isLaunching) 0 else 240))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                Text(
                    text = "${i18n("dashboard.next_step")} • ${i18n("dashboard.lesson_prefix")} $currentLesson",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = DuoInkSecondary,
                    letterSpacing = 0.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = lessonTitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = DuoInk,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // 3D Tactile "CONTINUA" Button
            Box(
                modifier = Modifier
                    .size(
                        width = if (isLaunching) 52.dp else 130.dp,
                        height = 48.dp
                    )
                    .animateContentSize(animationSpec = tween(180))
            ) {
                // Bottom dark shadow base
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .offset(y = 4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(unit.darkColor)
                )

                // Front button layer
                val pressOffset = if (isPressed) 3.dp else 0.dp
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .offset(y = pressOffset)
                        .clip(if (isLaunching) CircleShape else RoundedCornerShape(16.dp))
                        .onGloballyPositioned { coordinates ->
                            val position = coordinates.positionInRoot()
                            onClickPosition(
                                position + Offset(
                                    coordinates.size.width / 2f,
                                    coordinates.size.height / 2f
                                )
                            )
                        }
                        .background(unit.primaryColor)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onContinue
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLaunching) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.5.dp
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = i18n("dashboard.continue_button"),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonLaunchOverlay(
    unit: com.exertia.wikingo.domain.model.PathUnit,
    expansion: Float,
    sourceCenter: Offset?
) {
    val density = LocalDensity.current
    val revealProgress = expansion.coerceIn(0f, 1f)
    val circleSize = 70.dp

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val maxDimension = with(density) {
            kotlin.math.hypot(maxWidth.toPx(), maxHeight.toPx())
        }
        val finalScale = maxDimension / with(density) { circleSize.toPx() }
        val circleScale = 1f + (finalScale - 1f) * revealProgress
        val center = sourceCenter ?: Offset(
            x = with(density) { maxWidth.toPx() / 2f },
            y = with(density) { maxHeight.toPx() / 2f }
        )

        Box(
            modifier = Modifier
                .size(circleSize)
                .offset {
                    val radius = with(density) { circleSize.toPx() / 2f }
                    IntOffset(
                        x = (center.x - radius).roundToInt(),
                        y = (center.y - radius).roundToInt()
                    )
                }
                .clip(CircleShape)
                .graphicsLayer {
                    scaleX = circleScale
                    scaleY = circleScale
                    transformOrigin = TransformOrigin.Center
                }
                .background(unit.primaryColor)
        )
    }
}

@Composable
private fun StatPill(icon: String, value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("$icon $value", color = color, fontWeight = FontWeight.Black, fontSize = 14.sp)
        Text(label, color = DuoInkSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}
