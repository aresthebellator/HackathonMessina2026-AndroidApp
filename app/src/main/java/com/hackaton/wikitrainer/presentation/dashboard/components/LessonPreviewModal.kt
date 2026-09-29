package com.hackaton.wikitrainer.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.hackaton.wikitrainer.core.designsystem.WikiBlack
import com.hackaton.wikitrainer.core.designsystem.WikiOsloGray
import com.hackaton.wikitrainer.core.designsystem.WikiShuttleGray
import com.hackaton.wikitrainer.core.designsystem.WikiSilverSand
import com.hackaton.wikitrainer.core.designsystem.WikiSurfaceAlt
import com.hackaton.wikitrainer.core.designsystem.WikiWhite
import com.hackaton.wikitrainer.core.designsystem.components.DuolingoButton
import com.hackaton.wikitrainer.core.designsystem.components.DuolingoButtonStyle
import com.hackaton.wikitrainer.core.i18n.i18n
import com.hackaton.wikitrainer.domain.model.PathUnit
import com.hackaton.wikitrainer.domain.model.getLessonTitle
import com.hackaton.wikitrainer.domain.model.isCheckpointLesson
import com.hackaton.wikitrainer.core.i18n.LocalI18nLanguage

/**
 * Lesson Preview Modal styled with Wikipedia Brand and Logo Colors
 */
@Composable
fun LessonPreviewModal(
    lessonNumber: Int,
    unit: PathUnit,
    status: PathNodeStatus,
    onStartLesson: () -> Unit,
    onDismiss: () -> Unit
) {
    val isCheckpoint = isCheckpointLesson(lessonNumber)
    val isLocked = status == PathNodeStatus.LOCKED
    val isCompleted = status == PathNodeStatus.COMPLETED
    val language = LocalI18nLanguage.current
    val title = getLessonTitle(lessonNumber, language)

    val xpReward = i18n("path_modal.xp_reward_format", "xp" to if (isCheckpoint) 150 else 90)
    val gemReward = i18n("path_modal.gems_reward_format", "gems" to if (isCheckpoint) 25 else 10)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = WikiWhite,
            border = androidx.compose.foundation.BorderStroke(2.dp, WikiSilverSand),
            shadowElevation = 16.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.padding(20.dp)) {
                // Close button top-right
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = i18n("path_modal.close"),
                        tint = WikiShuttleGray
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Unit Tag pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(unit.primaryColor)
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "${unit.localizedTitle(language).uppercase()} • ${unit.localizedTopic(language).uppercase()}",
                            color = WikiWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    }

                    // Central Icon in tactile frame
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(if (isLocked) WikiSurfaceAlt else unit.lightColor)
                            .border(
                                width = 3.dp,
                                color = if (isLocked) WikiSilverSand else unit.primaryColor,
                                shape = RoundedCornerShape(24.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            isCheckpoint -> {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = null,
                                    tint = if (isLocked) WikiShuttleGray else unit.primaryColor,
                                    modifier = Modifier.size(42.dp)
                                )
                            }
                            isLocked -> {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = WikiShuttleGray,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            else -> {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = unit.primaryColor,
                                    modifier = Modifier.size(44.dp)
                                )
                            }
                        }
                    }

                    // Lesson Title & Description
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = WikiBlack,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = when {
                                isLocked -> i18n("path_modal.locked_desc")
                                isCheckpoint -> i18n("path_modal.checkpoint_desc")
                                else -> i18n("path_modal.default_desc")
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = WikiShuttleGray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }

                    // Completed Stars
                    if (isCompleted) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(3) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = WikiBlack,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    // Rewards Preview
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(WikiSurfaceAlt)
                                .border(1.dp, WikiSilverSand, RoundedCornerShape(16.dp))
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = WikiBlack,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = xpReward,
                                    fontWeight = FontWeight.Black,
                                    color = WikiBlack,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(WikiSurfaceAlt)
                                .border(1.dp, WikiSilverSand, RoundedCornerShape(16.dp))
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = WikiShuttleGray,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = gemReward,
                                    fontWeight = FontWeight.Black,
                                    color = WikiShuttleGray,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Action Button
                    if (isLocked) {
                        DuolingoButton(
                            text = i18n("path_modal.locked_title"),
                            onClick = {},
                            enabled = false,
                            style = DuolingoButtonStyle.SECONDARY,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        DuolingoButton(
                            text = if (isCompleted) i18n("path_modal.review_button") else i18n("path_modal.start_now"),
                            onClick = onStartLesson,
                            style = DuolingoButtonStyle.PRIMARY,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
