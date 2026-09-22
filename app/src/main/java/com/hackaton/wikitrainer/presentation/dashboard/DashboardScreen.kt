package com.hackaton.wikitrainer.presentation.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hackaton.wikitrainer.R
import com.hackaton.wikitrainer.core.designsystem.DuoBackground
import com.hackaton.wikitrainer.core.designsystem.DuoBlue
import com.hackaton.wikitrainer.core.designsystem.DuoBlueLight
import com.hackaton.wikitrainer.core.designsystem.DuoBorder
import com.hackaton.wikitrainer.core.designsystem.DuoBorderDark
import com.hackaton.wikitrainer.core.designsystem.DuoGreen
import com.hackaton.wikitrainer.core.designsystem.DuoGreenDark
import com.hackaton.wikitrainer.core.designsystem.DuoGreenLight
import com.hackaton.wikitrainer.core.designsystem.DuoInk
import com.hackaton.wikitrainer.core.designsystem.DuoInkSecondary
import com.hackaton.wikitrainer.core.designsystem.DuoRed
import com.hackaton.wikitrainer.core.designsystem.DuoSurface
import com.hackaton.wikitrainer.core.i18n.i18n
import com.hackaton.wikitrainer.domain.model.UserStats
import com.hackaton.wikitrainer.domain.model.MAX_LESSON_NUMBER
import com.hackaton.wikitrainer.domain.model.getLessonTitle
import com.hackaton.wikitrainer.domain.model.getUnitForLesson
import com.hackaton.wikitrainer.presentation.dashboard.components.DuolingoPath

@Composable
fun DashboardScreen(
    stats: UserStats,
    language: String,
    onLanguageToggle: () -> Unit,
    onStartLesson: (topic: String?, lessonNumber: Int?) -> Unit,
    onQuickQuiz: () -> Unit,
    onHistory: () -> Unit,
    onSettings: () -> Unit,
    onSavedArticles: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentLesson = (stats.totalLessonsCompleted + 1).coerceAtMost(MAX_LESSON_NUMBER)
    val currentUnit = getUnitForLesson(currentLesson)
    val currentLessonTitle = getLessonTitle(currentLesson)
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
                color = Color.White,
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
                                .size(42.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White)
                                .border(width = 1.dp, color = DuoBorder, shape = RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_launcher_image),
                                contentDescription = "Logo Wikingo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(11.dp))
                            )
                        }

                        Spacer(Modifier.width(10.dp))

                        // Title
                        Text(
                            text = "Wikingo",
                            color = com.hackaton.wikitrainer.core.designsystem.WikiBlack,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.weight(1f)
                        )

                        // Language Toggle Pill
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
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
                                    tint = com.hackaton.wikitrainer.core.designsystem.WikiShuttleGray,
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
                         StatPill("🔥", "${stats.currentStreak} ${i18n("dashboard.streak_unit")}", i18n("dashboard.streak_label"), com.hackaton.wikitrainer.core.designsystem.WikiBlack)
                         StatPill("⚡", "${stats.totalXp} ${i18n("dashboard.xp_suffix")}", i18n("dashboard.xp_label"), com.hackaton.wikitrainer.core.designsystem.WikiBlack)
                         StatPill("✓", "${stats.totalLessonsCompleted}/$MAX_LESSON_NUMBER", i18n("dashboard.lessons_label"), com.hackaton.wikitrainer.core.designsystem.WikiBlack)
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
                            .background(com.hackaton.wikitrainer.core.designsystem.WikiSurfaceAlt)
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
                                 text = "${currentUnit.title}: ${currentUnit.topic} ($unitCompletedCount/${currentUnit.endLesson - currentUnit.startLesson + 1})",
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
                             color = com.hackaton.wikitrainer.core.designsystem.WikiSurfaceAlt,
                             border = androidx.compose.foundation.BorderStroke(1.dp, com.hackaton.wikitrainer.core.designsystem.WikiSilverSand),
                             modifier = Modifier.clickable(onClick = onQuickQuiz)
                         ) {
                             Row(
                                 modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                 verticalAlignment = Alignment.CenterVertically
                             ) {
                                 Icon(
                                     imageVector = Icons.Default.Shuffle,
                                     contentDescription = null,
                                     tint = com.hackaton.wikitrainer.core.designsystem.WikiBlack,
                                     modifier = Modifier.size(13.dp)
                                 )
                                 Spacer(Modifier.width(4.dp))
                                 Text(
                                     text = i18n("dashboard.quick_quiz"),
                                     fontSize = 11.sp,
                                     fontWeight = FontWeight.Black,
                                     color = com.hackaton.wikitrainer.core.designsystem.WikiBlack
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
                onStartLesson = { lessonNum, topic ->
                    onStartLesson(topic, lessonNum)
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
                val topic = currentUnit.keywords.firstOrNull() ?: currentUnit.topic
                onStartLesson(topic, currentLesson)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        )
    }
}

@Composable
private fun FloatingContinueBar(
    currentLesson: Int,
    lessonTitle: String,
    unit: com.hackaton.wikitrainer.domain.model.PathUnit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(2.dp, DuoBorder),
        modifier = modifier.fillMaxWidth()
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
                    .size(width = 130.dp, height = 48.dp)
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
                        .clip(RoundedCornerShape(16.dp))
                        .background(unit.primaryColor)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onContinue
                        ),
                    contentAlignment = Alignment.Center
                ) {
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

@Composable
private fun StatPill(icon: String, value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("$icon $value", color = color, fontWeight = FontWeight.Black, fontSize = 14.sp)
        Text(label, color = DuoInkSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}
