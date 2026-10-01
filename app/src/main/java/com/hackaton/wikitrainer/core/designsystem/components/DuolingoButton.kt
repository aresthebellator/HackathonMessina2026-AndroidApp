package com.hackaton.wikitrainer.core.designsystem.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hackaton.wikitrainer.core.designsystem.LocalReduceMotion

enum class DuolingoButtonStyle {
    PRIMARY,
    SECONDARY,
    DANGER,
    OUTLINE,
    INVERTED
}

/**
 * 3D Tactile Button styled with Wikipedia Brand and Logo Colors:
 * Replicates the signature chunky button with a physical bevel bottom edge.
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
    val colors = MaterialTheme.colorScheme
    val (faceColor, baseColor, textColor) = when {
        !enabled -> Triple(colors.surfaceVariant, colors.outline, colors.onSurfaceVariant)
        style == DuolingoButtonStyle.PRIMARY -> Triple(colors.primary, colors.onPrimary.copy(alpha = 0.7f), colors.onPrimary)
        style == DuolingoButtonStyle.SECONDARY -> Triple(colors.secondary, colors.onSecondary.copy(alpha = 0.7f), colors.onSecondary)
        style == DuolingoButtonStyle.DANGER -> Triple(colors.error, colors.onError.copy(alpha = 0.7f), colors.onError)
        style == DuolingoButtonStyle.OUTLINE -> Triple(colors.surface, colors.outline, colors.onSurface)
        style == DuolingoButtonStyle.INVERTED -> Triple(colors.surface, colors.outline, colors.onSurface)
        else -> Triple(colors.primary, colors.onPrimary.copy(alpha = 0.7f), colors.onPrimary)
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val reduceMotion = LocalReduceMotion.current

    val cornerRadius = 16.dp
    val bevelHeight = 4.dp
    val pressOffset by animateDpAsState(
        targetValue = if (isPressed && enabled) bevelHeight else 0.dp,
        animationSpec = tween(if (reduceMotion) 0 else 120),
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
                .background(faceColor)
                .then(
                    if (style == DuolingoButtonStyle.OUTLINE) {
                        Modifier.border(2.dp, colors.outline, RoundedCornerShape(cornerRadius))
                    } else Modifier
                ),
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
