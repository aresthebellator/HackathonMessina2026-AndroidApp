package com.hackaton.wikitrainer.core.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hackaton.wikitrainer.core.designsystem.DuoBackground
import com.hackaton.wikitrainer.core.designsystem.DuoBlue
import com.hackaton.wikitrainer.core.designsystem.DuoBlueDark
import com.hackaton.wikitrainer.core.designsystem.DuoBorder
import com.hackaton.wikitrainer.core.designsystem.DuoBorderDark
import com.hackaton.wikitrainer.core.designsystem.DuoCardSelectedBg
import com.hackaton.wikitrainer.core.designsystem.DuoCardSelectedBorder
import com.hackaton.wikitrainer.core.designsystem.DuoGreen
import com.hackaton.wikitrainer.core.designsystem.DuoGreenDark
import com.hackaton.wikitrainer.core.designsystem.DuoGreenLight
import com.hackaton.wikitrainer.core.designsystem.DuoInk
import com.hackaton.wikitrainer.core.designsystem.DuoInkSecondary
import com.hackaton.wikitrainer.core.designsystem.DuoRed
import com.hackaton.wikitrainer.core.designsystem.DuoRedDark
import com.hackaton.wikitrainer.core.designsystem.DuoRedLight

enum class OptionCardState {
    IDLE,
    SELECTED,
    CORRECT,
    WRONG
}

/**
 * Duolingo-inspired interactive Option Card for micro-questions:
 * Features tactile bottom border bevel, animated state transition,
 * and clear keyboard/touch visual feedback.
 */
@Composable
fun OptionCard(
    text: String,
    indexLabel: String,
    state: OptionCardState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val targetBgColor = when (state) {
        OptionCardState.IDLE -> DuoBackground
        OptionCardState.SELECTED -> DuoCardSelectedBg
        OptionCardState.CORRECT -> DuoGreenLight
        OptionCardState.WRONG -> DuoRedLight
    }

    val targetBorderColor = when (state) {
        OptionCardState.IDLE -> DuoBorder
        OptionCardState.SELECTED -> DuoCardSelectedBorder
        OptionCardState.CORRECT -> DuoGreen
        OptionCardState.WRONG -> DuoRed
    }

    val targetBottomBevelColor = when (state) {
        OptionCardState.IDLE -> DuoBorderDark
        OptionCardState.SELECTED -> DuoBlueDark
        OptionCardState.CORRECT -> DuoGreenDark
        OptionCardState.WRONG -> DuoRedDark
    }

    val targetTextColor = when (state) {
        OptionCardState.CORRECT -> DuoGreenDark
        OptionCardState.WRONG -> DuoRedDark
        OptionCardState.SELECTED -> DuoBlueDark
        OptionCardState.IDLE -> DuoInk
    }

    val animatedBg by animateColorAsState(targetBgColor, animationSpec = tween(200), label = "optionBg")
    val animatedBorder by animateColorAsState(targetBorderColor, animationSpec = tween(200), label = "optionBorder")

    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(enabled = enabled, onClick = onClick)
    ) {
        // Base bottom 3D bevel layer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .matchParentSize()
                .clip(shape)
                .background(targetBottomBevelColor)
        )

        // Raised face container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 3.dp) // Exposes bottom 3dp bevel
                .clip(shape)
                .background(animatedBg)
                .border(width = 2.dp, color = animatedBorder, shape = shape)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Key badge: e.g. "1", "2", "3", "4" or "A", "B", "C", "D"
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = 1.5.dp,
                            color = if (state == OptionCardState.SELECTED) DuoBlue else DuoBorderDark,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(if (state == OptionCardState.SELECTED) DuoBlue.copy(alpha = 0.15f) else DuoBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = indexLabel,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (state == OptionCardState.SELECTED) DuoBlueDark else DuoInkSecondary
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = targetTextColor,
                    lineHeight = 22.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
