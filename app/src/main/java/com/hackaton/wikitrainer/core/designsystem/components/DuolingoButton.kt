package com.hackaton.wikitrainer.core.designsystem.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hackaton.wikitrainer.core.designsystem.DuoBackground
import com.hackaton.wikitrainer.core.designsystem.DuoBorder
import com.hackaton.wikitrainer.core.designsystem.DuoBorderDark
import com.hackaton.wikitrainer.core.designsystem.DuoGreen
import com.hackaton.wikitrainer.core.designsystem.DuoGreenDark
import com.hackaton.wikitrainer.core.designsystem.DuoInkSecondary
import com.hackaton.wikitrainer.core.designsystem.DuoRed
import com.hackaton.wikitrainer.core.designsystem.DuoRedDark

enum class DuolingoButtonStyle {
    PRIMARY,
    SECONDARY,
    DANGER,
    OUTLINE
}

/**
 * Custom Duolingo 3D Tactile Button.
 * Replicates the signature Duolingo chunky button with a physical bevel bottom edge.
 * On press, the top face shifts down, giving immediate tactile visual satisfaction.
 */
@Composable
fun DuolingoButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: DuolingoButtonStyle = DuolingoButtonStyle.PRIMARY,
    height: Dp = 52.dp
) {
    val (faceColor, baseColor, textColor) = when {
        !enabled -> Triple(DuoBorder, DuoBorderDark, DuoInkSecondary)
        style == DuolingoButtonStyle.PRIMARY -> Triple(DuoGreen, DuoGreenDark, DuoBackground)
        style == DuolingoButtonStyle.DANGER -> Triple(DuoRed, DuoRedDark, DuoBackground)
        style == DuolingoButtonStyle.OUTLINE -> Triple(DuoBackground, DuoBorder, DuoGreenDark)
        else -> Triple(DuoGreen, DuoGreenDark, DuoBackground)
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val cornerRadius = 16.dp
    val bevelHeight = 4.dp
    val pressOffset by animateDpAsState(
        targetValue = if (isPressed && enabled) bevelHeight else 0.dp,
        label = "pressOffset"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height + bevelHeight)
            .clip(RoundedCornerShape(cornerRadius))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        // Bottom 3D bevel / base layer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height + bevelHeight)
                .clip(RoundedCornerShape(cornerRadius))
                .background(baseColor)
        )

        // Top button face that depresses when pressed
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .offset(y = pressOffset)
                .clip(RoundedCornerShape(cornerRadius))
                .background(faceColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text.uppercase(),
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.8.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
