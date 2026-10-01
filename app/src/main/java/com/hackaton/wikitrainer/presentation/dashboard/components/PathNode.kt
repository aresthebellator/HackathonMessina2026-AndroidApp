package com.hackaton.wikitrainer.presentation.dashboard.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hackaton.wikitrainer.core.designsystem.WikiBlack
import com.hackaton.wikitrainer.core.designsystem.WikiOsloGray
import com.hackaton.wikitrainer.core.designsystem.WikiShuttleGray
import com.hackaton.wikitrainer.core.designsystem.WikiSilverSand
import com.hackaton.wikitrainer.core.designsystem.WikiWhite
import com.hackaton.wikitrainer.core.designsystem.LocalReduceMotion
import com.hackaton.wikitrainer.core.i18n.i18n
import com.hackaton.wikitrainer.domain.model.PathUnit

enum class PathNodeStatus {
    COMPLETED,
    CURRENT,
    LOCKED
}

/**
 * Path Node component styled with Wikipedia Brand and Logo Colors:
 * - Black: #000000
 * - Shuttle Gray: #636466
 * - Oslo Gray: #939598
 * - Silver Sand: #C7C8CA
 * - White: #FFFFFF
 */
@Composable
fun PathNode(
    lessonNumber: Int,
    status: PathNodeStatus,
    unit: PathUnit,
    offsetDp: Dp,
    stars: Int = 3,
    isCheckpoint: Boolean = false,
    onClick: (Offset?) -> Unit,
    modifier: Modifier = Modifier
) {
    val isCurrent = status == PathNodeStatus.CURRENT
    val isCompleted = status == PathNodeStatus.COMPLETED
    val isLocked = status == PathNodeStatus.LOCKED
    val reduceMotion = LocalReduceMotion.current
    val colors = MaterialTheme.colorScheme

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    var clickCenter by remember { mutableStateOf<Offset?>(null) }

    // Floating speech bubble bobbing animation for current node
    val infiniteTransition = rememberInfiniteTransition(label = "currentNodeBobbing")
    val bobbingOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (reduceMotion) 0f else -6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (reduceMotion) 0 else 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bobbingY"
    )

    // Pulsing halo scale/alpha for current node
    val haloScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (reduceMotion) 1f else 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (reduceMotion) 0 else 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "haloScale"
    )
    val haloAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = if (reduceMotion) 0f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (reduceMotion) 0 else 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "haloAlpha"
    )

    val nodeBg = when {
        isLocked -> colors.surfaceVariant
        else -> unit.primaryColor
    }
    val nodeBottomBorder = when {
        isLocked -> colors.outline
        else -> unit.darkColor
    }
    val targetScale = when {
        isCurrent -> 1.08f
        isCompleted -> 1.02f
        else -> 1f
    }
    val nodeScale by animateFloatAsState(
        targetValue = if (reduceMotion) 1f else targetScale,
        animationSpec = tween(if (reduceMotion) 0 else 450, easing = FastOutSlowInEasing),
        label = "nodeScale"
    )

    Column(
        modifier = modifier
            .offset(x = offsetDp)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Floating "INIZIA" Speech Bubble for Active/Current Node
        Box(
            modifier = Modifier.height(34.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            if (isCurrent) {
                Column(
                    modifier = Modifier.offset(y = bobbingOffset.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(unit.primaryColor)
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = unit.textColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = i18n("dashboard.start_bubble"),
                            color = unit.textColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }
                    // Downward pointing triangle arrow in Wikipedia Black
                    Canvas(modifier = Modifier.size(width = 10.dp, height = 5.dp)) {
                        val path = Path().apply {
                            moveTo(0f, 0f)
                            lineTo(size.width, 0f)
                            lineTo(size.width / 2f, size.height)
                            close()
                        }
                        drawPath(path, color = unit.primaryColor)
                    }
                }
            }
        }

        // Tactile 3D Node Container
        Box(
            modifier = Modifier.size(76.dp),
            contentAlignment = Alignment.Center
        ) {
            // Pulsing highlight halo for current node
            if (isCurrent) {
                Box(
                    modifier = Modifier
                        .size((76 * haloScale).dp)
                        .clip(CircleShape)
                        .background(unit.primaryColor.copy(alpha = haloAlpha))
                )
            }

            // 3D tactile button: bottom shadow base circle
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .offset(y = 6.dp)
                    .clip(CircleShape)
                    .background(nodeBottomBorder)
            )

            // Front circle button that presses down
            val pressOffset = if (isPressed) 4.dp else 0.dp
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .scale(nodeScale)
                    .offset(y = pressOffset)
                    .clip(CircleShape)
                    .background(nodeBg)
                    .onGloballyPositioned { coordinates: LayoutCoordinates ->
                        clickCenter = coordinates.positionInRoot() +
                            Offset(coordinates.size.width / 2f, coordinates.size.height / 2f)
                    }
                    .then(
                        if (isCurrent) {
                            Modifier.border(3.dp, unit.textColor, CircleShape)
                        } else {
                            Modifier
                        }
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { onClick(clickCenter) }
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Node Center Icon
                when {
                    isCheckpoint -> {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Sfida checkpoint",
                            tint = if (isLocked) colors.onSurfaceVariant else unit.textColor,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                    isCompleted -> {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Completata",
                            tint = unit.textColor,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    isCurrent -> {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "In corso",
                            tint = unit.textColor,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    else -> {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Bloccata",
                            tint = colors.onSurfaceVariant,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            // Number badge on bottom right
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-2).dp, y = 2.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isLocked) colors.surfaceVariant else colors.surface)
                    .border(
                        width = 2.dp,
                        color = if (isLocked) colors.outline else colors.outlineVariant,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$lessonNumber",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isLocked) colors.onSurfaceVariant else colors.onSurface
                )
            }
        }

        // Stars row for completed nodes
        if (isCompleted && stars > 0) {
            Row(
                modifier = Modifier.padding(top = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (index < stars) unit.primaryColor else colors.outlineVariant,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.height(19.dp))
        }
    }
}
