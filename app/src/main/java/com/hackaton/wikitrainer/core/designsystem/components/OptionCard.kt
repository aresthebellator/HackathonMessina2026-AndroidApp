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
import com.hackaton.wikitrainer.core.designsystem.WikiBlack
import com.hackaton.wikitrainer.core.designsystem.WikiOsloGray
import com.hackaton.wikitrainer.core.designsystem.WikiShuttleGray
import com.hackaton.wikitrainer.core.designsystem.WikiSilverSand
import com.hackaton.wikitrainer.core.designsystem.WikiSurfaceAlt
import com.hackaton.wikitrainer.core.designsystem.WikiWhite
import com.hackaton.wikitrainer.core.designsystem.LocalReduceMotion

enum class OptionCardState {
    IDLE,
    SELECTED,
    CORRECT,
    WRONG
}

/**
 * Interactive Option Card styled with Wikipedia Brand and Logo Colors:
 * Features tactile bottom border bevel, animated state transition,
 * and high-contrast Wikipedia monochromatic visual feedback.
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
    val reduceMotion = LocalReduceMotion.current
    val targetBgColor = when (state) {
        OptionCardState.IDLE -> WikiWhite
        OptionCardState.SELECTED -> WikiSurfaceAlt
        OptionCardState.CORRECT -> WikiBlack
        OptionCardState.WRONG -> WikiShuttleGray
    }

    val targetBorderColor = when (state) {
        OptionCardState.IDLE -> WikiSilverSand
        OptionCardState.SELECTED -> WikiBlack
        OptionCardState.CORRECT -> WikiBlack
        OptionCardState.WRONG -> WikiBlack
    }

    val targetBottomBevelColor = when (state) {
        OptionCardState.IDLE -> WikiSilverSand
        OptionCardState.SELECTED -> WikiShuttleGray
        OptionCardState.CORRECT -> WikiShuttleGray
        OptionCardState.WRONG -> WikiBlack
    }

    val targetTextColor = when (state) {
        OptionCardState.CORRECT -> WikiWhite
        OptionCardState.WRONG -> WikiWhite
        OptionCardState.SELECTED -> WikiBlack
        OptionCardState.IDLE -> WikiBlack
    }

    val badgeBg = when (state) {
        OptionCardState.CORRECT -> WikiWhite
        OptionCardState.WRONG -> WikiSilverSand
        OptionCardState.SELECTED -> WikiBlack
        OptionCardState.IDLE -> WikiWhite
    }

    val badgeBorder = when (state) {
        OptionCardState.CORRECT -> WikiWhite
        OptionCardState.WRONG -> WikiSilverSand
        OptionCardState.SELECTED -> WikiBlack
        OptionCardState.IDLE -> WikiSilverSand
    }

    val badgeTextColor = when (state) {
        OptionCardState.CORRECT -> WikiBlack
        OptionCardState.WRONG -> WikiBlack
        OptionCardState.SELECTED -> WikiWhite
        OptionCardState.IDLE -> WikiShuttleGray
    }

    val animationDuration = if (reduceMotion) 0 else 200
    val animatedBg by animateColorAsState(targetBgColor, animationSpec = tween(animationDuration), label = "optionBg")
    val animatedBorder by animateColorAsState(targetBorderColor, animationSpec = tween(animationDuration), label = "optionBorder")

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
                            color = badgeBorder,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(badgeBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = indexLabel,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = badgeTextColor
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
