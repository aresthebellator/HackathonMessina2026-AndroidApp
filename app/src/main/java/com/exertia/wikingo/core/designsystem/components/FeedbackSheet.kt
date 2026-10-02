package com.exertia.wikingo.core.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.exertia.wikingo.core.designsystem.LocalReduceMotion
import com.exertia.wikingo.core.i18n.i18n

/**
 * Animated Feedback Bottom Banner styled with Wikipedia Brand and Logo Colors:
 * Slides up immediately when the user validates their answer.
 * Displays high-contrast Wikipedia brand styling for validation and Wikipedia context.
 *
 * Fix: The continue button now always uses a white background + colored text so it is
 * visible on both the green (correct) and red (wrong) sheet backgrounds in both light
 * and dark themes.
 */
@Composable
fun FeedbackSheet(
    visible: Boolean,
    isCorrect: Boolean,
    correctAnswerText: String,
    explanation: String,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    val reduceMotion = LocalReduceMotion.current
    AnimatedVisibility(
        visible = visible,
        enter = if (reduceMotion) EnterTransition.None else slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = if (reduceMotion) ExitTransition.None else slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        val colors = MaterialTheme.colorScheme
        val sheetBg = if (isCorrect) colors.primary else colors.error
        val sheetTitle = if (isCorrect) i18n("trainer.correct_title") else i18n("trainer.wrong_title")
        val titleColor = if (isCorrect) colors.onPrimary else colors.onError
        val icon = if (isCorrect) Icons.Default.Check else Icons.Default.Close
        val iconTint = if (isCorrect) colors.primary else colors.error
        val buttonText = if (isCorrect) i18n("dashboard.continue_button") else i18n("common.got_it").uppercase()

        // Always white button face + sheet-colored text so the button is visible on
        // any sheet background regardless of light/dark theme mode.
        val buttonFaceColor = Color.White
        val buttonTextColor = if (isCorrect) colors.primary else colors.error
        val buttonBaseColor = Color.Black.copy(alpha = 0.20f)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(sheetBg)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isCorrect) colors.onPrimary else colors.onError),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = sheetTitle,
                        color = titleColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                if (!isCorrect) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = i18n("trainer.correct_answer_label"),
                        color = colors.onError.copy(alpha = 0.80f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = correctAnswerText,
                        color = colors.onError,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    // Detailed Wikipedia explanation card — semi-transparent white on coloured bg
                    if (explanation.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.15f))
                                .border(1.dp, Color.White.copy(alpha = 0.30f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = i18n("trainer.wiki_explanation_label"),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.onError.copy(alpha = 0.85f)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = explanation,
                                    fontSize = 13.sp,
                                    color = colors.onError,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                FeedbackActionButton(
                    text = buttonText,
                    faceColor = buttonFaceColor,
                    textColor = buttonTextColor,
                    baseColor = buttonBaseColor,
                    onClick = onContinue,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * Tactile 3D action button for use inside the FeedbackSheet.
 * Always uses an explicit white face with colored text so it is visible on
 * any colored sheet background in both light and dark themes.
 */
@Composable
private fun FeedbackActionButton(
    text: String,
    faceColor: Color,
    textColor: Color,
    baseColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val reduceMotion = LocalReduceMotion.current

    val pressOffset by animateDpAsState(
        targetValue = if (isPressed) 4.dp else 0.dp,
        animationSpec = tween(if (reduceMotion) 0 else 100),
        label = "feedbackBtnPress"
    )

    Box(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        contentAlignment = Alignment.TopCenter
    ) {
        // Base bevel layer — slightly darker so the face "lifts" above it
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(16.dp))
                .background(baseColor)
        )

        // Face layer that shifts down on press giving 3D tactile effect
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .offset(y = pressOffset)
                .clip(RoundedCornerShape(16.dp))
                .background(faceColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text.uppercase(),
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.8.sp
            )
        }
    }
}
