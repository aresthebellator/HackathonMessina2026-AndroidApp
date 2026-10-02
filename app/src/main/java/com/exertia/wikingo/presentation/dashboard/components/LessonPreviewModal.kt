package com.exertia.wikingo.presentation.dashboard.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.exertia.wikingo.core.designsystem.WikiSurfaceAlt
import com.exertia.wikingo.core.designsystem.LocalReduceMotion
import com.exertia.wikingo.core.designsystem.components.DuolingoButton
import com.exertia.wikingo.core.designsystem.components.DuolingoButtonStyle
import com.exertia.wikingo.core.i18n.i18n
import com.exertia.wikingo.domain.model.PathUnit
import com.exertia.wikingo.domain.model.getLessonTitle
import com.exertia.wikingo.domain.model.isCheckpointLesson
import com.exertia.wikingo.core.i18n.LocalI18nLanguage

/**
 * Lesson Preview Modal styled with Wikipedia Brand and Logo Colors.
 * Uses MaterialTheme tokens throughout so it renders correctly in both
 * light and dark themes.
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
    val reduceMotion = LocalReduceMotion.current

    val xpReward = i18n("path_modal.xp_reward_format", "xp" to if (isCheckpoint) 150 else 90)
    val gemReward = i18n("path_modal.gems_reward_format", "gems" to if (isCheckpoint) 25 else 10)

    // Drive a spring-based enter animation for the modal content
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Dialog(onDismissRequest = onDismiss) {
        AnimatedVisibility(
            visible = visible,
            enter = if (reduceMotion) fadeIn() else scaleIn(
                initialScale = 0.88f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
            ) + fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)),
            exit = if (reduceMotion) fadeOut() else scaleOut(targetScale = 0.92f) + fadeOut()
        ) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant),
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
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Unit Tag pill — always white text on unit color (legible regardless of theme)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(unit.primaryColor)
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "${unit.localizedTitle(language).uppercase()} • ${unit.localizedTopic(language).uppercase()}",
                                color = unit.textColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                        }

                        // Central Icon in tactile frame — uses theme tokens for lock/surface states
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(if (isLocked) MaterialTheme.colorScheme.surfaceVariant else unit.lightColor)
                                .border(
                                    width = 3.dp,
                                    color = if (isLocked) MaterialTheme.colorScheme.outline else unit.primaryColor,
                                    shape = RoundedCornerShape(24.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            when {
                                isCheckpoint -> {
                                    Icon(
                                        imageVector = Icons.Default.EmojiEvents,
                                        contentDescription = null,
                                        tint = if (isLocked) MaterialTheme.colorScheme.onSurfaceVariant else unit.primaryColor,
                                        modifier = Modifier.size(42.dp)
                                    )
                                }
                                isLocked -> {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
                                color = MaterialTheme.colorScheme.onSurface,
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
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }

                        // Completed Stars — colored with unit accent or theme star color
                        if (isCompleted) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(3) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = unit.primaryColor,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }

                        // Rewards Preview — use theme surfaceVariant for the tiles
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Bolt,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = xpReward,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 13.sp
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = gemReward,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
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
}
