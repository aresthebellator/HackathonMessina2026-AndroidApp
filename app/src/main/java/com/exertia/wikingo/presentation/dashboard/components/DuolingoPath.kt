package com.exertia.wikingo.presentation.dashboard.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import com.exertia.wikingo.domain.model.PathUnit
import com.exertia.wikingo.domain.model.UNITS_DATA
import com.exertia.wikingo.domain.model.getSerpentineOffset
import com.exertia.wikingo.domain.model.isCheckpointLesson
import com.exertia.wikingo.domain.model.getLessonTopic
import com.exertia.wikingo.core.i18n.LocalI18nLanguage

@Composable
fun DuolingoPath(
    currentLessonIndex: Int,
    completedLessonsCount: Int,
    onStartLesson: (lessonNumber: Int, topic: String, sourceCenter: Offset?) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState()
) {
    val language = LocalI18nLanguage.current

    // Auto-scroll gently to the current active lesson node on first load
    LaunchedEffect(currentLessonIndex) {
        // Find rough index in list (unit banners + nodes)
        // Each unit has 1 banner + 10 nodes = 11 items per unit
        val unitIndex = ((currentLessonIndex - 1) / 10).coerceIn(0, UNITS_DATA.lastIndex)
        val lessonInUnit = (currentLessonIndex - 1) % 10
        val targetIndex = (unitIndex * 11 + lessonInUnit).coerceAtLeast(0)
        listState.animateScrollToItem((targetIndex - 1).coerceAtLeast(0))
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item(key = "topSpacer") {
            Spacer(modifier = Modifier.height(10.dp))
        }

        UNITS_DATA.forEach { unit ->
            // Unit Section Header Banner
            val unitCompletedCount = (unit.startLesson..unit.endLesson)
                .count { it <= completedLessonsCount }

            item(key = "unit_banner_${unit.id}") {
                UnitBanner(
                    unit = unit,
                    completedCount = unitCompletedCount
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            // Path Nodes for this unit
            items(
                count = (unit.endLesson - unit.startLesson + 1),
                key = { idx -> "lesson_${unit.startLesson + idx}" }
            ) { idx ->
                val lessonNumber = unit.startLesson + idx
                val lessonIndexInUnit = idx + 1
                val offsetDp = getSerpentineOffset(lessonIndexInUnit)
                val isCompleted = lessonNumber <= completedLessonsCount
                val isCurrent = lessonNumber == currentLessonIndex
                val status = when {
                    isCompleted -> PathNodeStatus.COMPLETED
                    isCurrent -> PathNodeStatus.CURRENT
                    else -> PathNodeStatus.LOCKED
                }
                val connectorColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)

                val prevOffsetDp = if (idx > 0) getSerpentineOffset(lessonIndexInUnit - 1) else 0.dp

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    // Dashed serpentine connecting curve from previous node
                    if (idx > 0) {
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .align(Alignment.TopCenter)
                        ) {
                            val startX = size.width / 2f + prevOffsetDp.toPx()
                            val endX = size.width / 2f + offsetDp.toPx()
                            val curvePath = Path().apply {
                                moveTo(startX, -10.dp.toPx())
                                cubicTo(
                                    startX, size.height * 0.4f,
                                    endX, size.height * 0.6f,
                                    endX, size.height
                                )
                            }
                            drawPath(
                                path = curvePath,
                                color = if (isCompleted) unit.lightColor else connectorColor,
                                style = Stroke(
                                    width = 6.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 14f), 0f)
                                )
                            )
                        }
                    }

                    PathNode(
                        lessonNumber = lessonNumber,
                        status = status,
                        unit = unit,
                        offsetDp = offsetDp,
                        stars = if (isCompleted) 3 else 0,
                        isCheckpoint = isCheckpointLesson(lessonNumber),
                        onClick = { sourceCenter ->
                            if (status != PathNodeStatus.LOCKED) {
                                onStartLesson(
                                    lessonNumber,
                                    getLessonTopic(lessonNumber, language),
                                    sourceCenter
                                )
                            }
                        }
                    )
                }
            }

            item(key = "unit_bottom_spacer_${unit.id}") {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Bottom spacer to avoid being covered by sticky bottom bar
        item(key = "bottomPaddingSpacer") {
            Spacer(modifier = Modifier.height(110.dp))
        }
    }

}
