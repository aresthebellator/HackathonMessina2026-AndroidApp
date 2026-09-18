package com.hackaton.wikitrainer.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hackaton.wikitrainer.core.designsystem.DuoBackground
import com.hackaton.wikitrainer.core.designsystem.DuoBlue
import com.hackaton.wikitrainer.core.designsystem.DuoBlueLight
import com.hackaton.wikitrainer.core.designsystem.DuoBorder
import com.hackaton.wikitrainer.core.designsystem.DuoGreen
import com.hackaton.wikitrainer.core.designsystem.DuoGreenDark
import com.hackaton.wikitrainer.core.designsystem.DuoGreenLight
import com.hackaton.wikitrainer.core.designsystem.DuoInk
import com.hackaton.wikitrainer.core.designsystem.DuoInkSecondary
import com.hackaton.wikitrainer.core.designsystem.DuoPurple
import com.hackaton.wikitrainer.core.designsystem.DuoRed
import com.hackaton.wikitrainer.core.designsystem.DuoYellow
import com.hackaton.wikitrainer.domain.model.UserStats

private data class UnitInfo(
    val title: String,
    val topic: String,
    val color: Color,
    val start: Int
)

private val units = listOf(
    UnitInfo("Sezione 1", "Storia & Grandi Civiltà", DuoGreen, 1),
    UnitInfo("Sezione 2", "Scienza, Spazio & Cosmo", DuoBlue, 11),
    UnitInfo("Sezione 3", "Arte, Scultura & Capolavori", DuoPurple, 21),
    UnitInfo("Sezione 4", "Geografia & Meraviglie Naturali", Color(0xFFFF9600), 31),
    UnitInfo("Sezione 5", "Filosofia, Idee & Invenzioni", Color(0xFF00A37B), 41),
    UnitInfo("Sezione 6", "Letteratura, Miti & Poemi", DuoRed, 51)
)

@Composable
fun DashboardScreen(
    stats: UserStats,
    language: String,
    onLanguageToggle: () -> Unit,
    onStartLesson: () -> Unit,
    onQuickQuiz: () -> Unit,
    onHistory: () -> Unit,
    onSettings: () -> Unit,
    onSavedArticles: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentLesson = (stats.totalLessonsCompleted + 1).coerceAtMost(60)

    Column(modifier = modifier.fillMaxSize().background(DuoBackground)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .border(1.dp, DuoBorder)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(DuoGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🦉", fontSize = 22.sp)
                }
                Spacer(Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Wikingo", color = DuoGreenDark, fontSize = 21.sp, fontWeight = FontWeight.Black)
                    Text("Percorso Wikipedia", color = DuoInkSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                IconButton(onClick = onLanguageToggle) {
                    Icon(Icons.Default.Language, "Cambia lingua", tint = DuoBlue)
                    Text(language.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                IconButton(onClick = onSavedArticles) {
                    Icon(Icons.Default.Bookmark, "Voci salvate", tint = DuoInkSecondary)
                }
                IconButton(onClick = onSettings) {
                    Icon(Icons.Default.Settings, "Impostazioni", tint = DuoInkSecondary)
                }
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatPill("🔥", "${stats.currentStreak} gg", "Streak", DuoRed)
                StatPill("⚡", "${stats.totalXp} XP", "Esperienza", DuoBlue)
                StatPill("✓", "${stats.totalLessonsCompleted}", "Lezioni", DuoGreenDark)
                IconButton(onClick = onHistory) {
                    Icon(Icons.Default.History, "Cronologia", tint = DuoInkSecondary)
                }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 110.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Percorso di apprendimento",
                        modifier = Modifier.weight(1f),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = DuoInk
                    )
                    Button(
                        onClick = onQuickQuiz,
                        colors = ButtonDefaults.buttonColors(containerColor = DuoBlueLight, contentColor = DuoBlue),
                        contentPadding = PaddingValues(horizontal = 10.dp)
                    ) {
                        Icon(Icons.Default.Shuffle, null, Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Quiz rapido", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            items(units) { unit ->
                UnitCard(
                    unit = unit,
                    currentLesson = currentLesson,
                    completedLessons = stats.totalLessonsCompleted,
                    onLessonClick = onStartLesson
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .border(1.dp, DuoBorder)
                .padding(12.dp)
        ) {
            Button(
                onClick = onStartLesson,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = DuoGreen),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.PlayArrow, null)
                Spacer(Modifier.width(6.dp))
                Text("CONTINUA • LEZIONE $currentLesson", fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
private fun StatPill(icon: String, value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("$icon $value", color = color, fontWeight = FontWeight.Black, fontSize = 14.sp)
        Text(label, color = DuoInkSecondary, fontSize = 10.sp)
    }
}

@Composable
private fun UnitCard(
    unit: UnitInfo,
    currentLesson: Int,
    completedLessons: Int,
    onLessonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, unit.color.copy(alpha = .35f), RoundedCornerShape(20.dp))
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth().background(unit.color).padding(14.dp)) {
            Text(unit.title, color = Color.White, fontWeight = FontWeight.Black, fontSize = 17.sp)
            Text(unit.topic, color = Color.White.copy(alpha = .9f), fontSize = 12.sp)
        }
        Column(modifier = Modifier.padding(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                (0 until 10).forEach { index ->
                    val lesson = unit.start + index
                    val unlocked = lesson <= currentLesson
                    val completed = lesson <= completedLessons
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .size(26.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                when {
                                    completed -> DuoGreenLight
                                    lesson == currentLesson -> unit.color
                                    else -> DuoBackground
                                }
                            )
                            .border(1.dp, if (unlocked) unit.color else DuoBorder, RoundedCornerShape(8.dp))
                            .clickable(enabled = unlocked) { onLessonClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (completed) "✓" else "${index + 1}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = if (lesson == currentLesson) Color.White else DuoInkSecondary
                        )
                    }
                }
            }
        }
    }
}
