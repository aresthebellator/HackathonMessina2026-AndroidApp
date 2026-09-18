package com.hackaton.wikitrainer.core.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hackaton.wikitrainer.core.designsystem.DuoBackground
import com.hackaton.wikitrainer.core.designsystem.DuoBorder
import com.hackaton.wikitrainer.core.designsystem.DuoGreen
import com.hackaton.wikitrainer.core.designsystem.DuoGreenDark
import com.hackaton.wikitrainer.core.designsystem.DuoGreenLight
import com.hackaton.wikitrainer.core.designsystem.DuoInk
import com.hackaton.wikitrainer.core.designsystem.DuoRed
import com.hackaton.wikitrainer.core.designsystem.DuoRedDark
import com.hackaton.wikitrainer.core.designsystem.DuoRedLight

/**
 * Duolingo-styled Animated Feedback Bottom Banner:
 * Slides up immediately when the user validates their answer.
 * Displays vibrant positive validation or informative error correction with Wikipedia context.
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
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        modifier = modifier
    ) {
        val sheetBg = if (isCorrect) DuoGreenLight else DuoRedLight
        val sheetTitle = if (isCorrect) "Fantastico!" else "Risposta errata"
        val titleColor = if (isCorrect) DuoGreenDark else DuoRedDark
        val icon = if (isCorrect) Icons.Default.Check else Icons.Default.Close
        val iconBg = if (isCorrect) DuoGreen else DuoRed
        val buttonStyle = if (isCorrect) DuolingoButtonStyle.PRIMARY else DuolingoButtonStyle.DANGER
        val buttonText = if (isCorrect) "CONTINUA" else "HO CAPITO"

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
                            .background(DuoBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconBg,
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
                        text = "Risposta corretta:",
                        color = DuoRedDark,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = correctAnswerText,
                        color = DuoInk,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    // Detailed Wikipedia explanation card
                    if (explanation.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(DuoBackground)
                                .border(1.dp, DuoBorder, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "📖 Dal riassunto di Wikipedia:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DuoGreenDark
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = explanation,
                                    fontSize = 13.sp,
                                    color = DuoInk,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                DuolingoButton(
                    text = buttonText,
                    onClick = onContinue,
                    style = buttonStyle,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
