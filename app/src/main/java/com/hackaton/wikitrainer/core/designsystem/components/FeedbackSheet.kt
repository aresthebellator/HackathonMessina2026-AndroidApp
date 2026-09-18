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
import com.hackaton.wikitrainer.core.designsystem.WikiBlack
import com.hackaton.wikitrainer.core.designsystem.WikiOsloGray
import com.hackaton.wikitrainer.core.designsystem.WikiShuttleGray
import com.hackaton.wikitrainer.core.designsystem.WikiSilverSand
import com.hackaton.wikitrainer.core.designsystem.WikiWhite
import com.hackaton.wikitrainer.core.i18n.i18n

/**
 * Animated Feedback Bottom Banner styled with Wikipedia Brand and Logo Colors:
 * Slides up immediately when the user validates their answer.
 * Displays high-contrast Wikipedia brand styling for validation and Wikipedia context.
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
        val sheetBg = if (isCorrect) WikiBlack else WikiShuttleGray
        val sheetTitle = if (isCorrect) i18n("trainer.correct_title") else i18n("trainer.wrong_title")
        val titleColor = WikiWhite
        val icon = if (isCorrect) Icons.Default.Check else Icons.Default.Close
        val iconTint = if (isCorrect) WikiBlack else WikiShuttleGray
        val buttonText = if (isCorrect) i18n("dashboard.continue_button") else i18n("common.got_it").uppercase()

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
                            .background(WikiWhite),
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
                        color = WikiSilverSand,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = correctAnswerText,
                        color = WikiWhite,
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
                                .background(WikiWhite)
                                .border(1.dp, WikiSilverSand, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = i18n("trainer.wiki_explanation_label"),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = WikiShuttleGray
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = explanation,
                                    fontSize = 13.sp,
                                    color = WikiBlack,
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
                    style = DuolingoButtonStyle.INVERTED,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
