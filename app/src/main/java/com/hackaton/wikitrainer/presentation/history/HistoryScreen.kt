package com.hackaton.wikitrainer.presentation.history

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hackaton.wikitrainer.R
import com.hackaton.wikitrainer.core.designsystem.DuoBackground
import com.hackaton.wikitrainer.core.designsystem.DuoBlue
import com.hackaton.wikitrainer.core.designsystem.DuoBlueDark
import com.hackaton.wikitrainer.core.designsystem.DuoBorder
import com.hackaton.wikitrainer.core.designsystem.DuoGreen
import com.hackaton.wikitrainer.core.designsystem.DuoGreenDark
import com.hackaton.wikitrainer.core.designsystem.DuoGreenLight
import com.hackaton.wikitrainer.core.designsystem.DuoInk
import com.hackaton.wikitrainer.core.designsystem.DuoInkSecondary
import com.hackaton.wikitrainer.core.designsystem.DuoRed
import com.hackaton.wikitrainer.core.designsystem.DuoSurface
import com.hackaton.wikitrainer.core.designsystem.DuoYellowDark
import com.hackaton.wikitrainer.core.i18n.i18n
import com.hackaton.wikitrainer.domain.model.TopicHistory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = i18n("history.title"),
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black,
                        color = DuoInk
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = i18n("common.back"),
                            tint = DuoInk
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DuoBackground
                )
            )
        },
        containerColor = DuoBackground,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is HistoryUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = com.hackaton.wikitrainer.core.designsystem.WikiBlack)
                    }
                }
                is HistoryUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // User Stats Summary Card
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(DuoSurface)
                                    .border(2.dp, DuoBorder, RoundedCornerShape(18.dp))
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    StatSummaryItem(
                                        iconRes = R.drawable.ic_flame,
                                        value = "${state.stats.currentStreak} ${i18n("dashboard.streak_unit")}",
                                        label = i18n("history.current_streak"),
                                        color = com.hackaton.wikitrainer.core.designsystem.WikiBlack
                                    )
                                    StatSummaryItem(
                                        iconRes = R.drawable.ic_bolt,
                                        value = "${state.stats.totalXp} ${i18n("dashboard.xp_suffix")}",
                                        label = i18n("history.experience_points"),
                                        color = com.hackaton.wikitrainer.core.designsystem.WikiBlack
                                    )
                                    StatSummaryItem(
                                        iconRes = R.drawable.ic_check,
                                        value = "${state.stats.totalLessonsCompleted}",
                                        label = i18n("history.lessons_completed"),
                                        color = com.hackaton.wikitrainer.core.designsystem.WikiBlack
                                    )
                                }
                            }
                        }

                        // Topics List Header
                        item {
                            Text(
                                text = "${i18n("history.topics_heading")} (${state.topics.size})",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = DuoInk,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        if (state.topics.isEmpty()) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 40.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_launcher_image),
                                        contentDescription = null,
                                        modifier = Modifier.size(70.dp)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = i18n("history.empty_title"),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DuoInk
                                    )
                                    Text(
                                        text = i18n("history.empty_description"),
                                        fontSize = 13.sp,
                                        color = DuoInkSecondary,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp)
                                    )
                                }
                            }
                        } else {
                            items(state.topics, key = { it.id }) { topic ->
                                TopicHistoryItem(
                                    topic = topic,
                                    onItemClick = {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(topic.wikiUrl))
                                        context.startActivity(intent)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TopicHistoryItem(
    topic: TopicHistory,
    onItemClick: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy • HH:mm", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(topic.completedAt))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DuoBackground)
            .border(2.dp, DuoBorder, RoundedCornerShape(16.dp))
            .clickable { onItemClick() }
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = topic.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DuoInk,
                    modifier = Modifier.weight(1f)
                )

                // Score Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(com.hackaton.wikitrainer.core.designsystem.WikiSilverSand)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${topic.score}/${topic.totalQuestions}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = com.hackaton.wikitrainer.core.designsystem.WikiBlack
                    )
                }
            }

            if (topic.description.isNotBlank()) {
                Text(
                    text = topic.description,
                    fontSize = 13.sp,
                    color = DuoInkSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formattedDate,
                    fontSize = 11.sp,
                    color = DuoInkSecondary
                )

                Text(
                    text = "+${topic.xpEarned} XP",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = com.hackaton.wikitrainer.core.designsystem.WikiBlack
                )
            }
        }
    }
}

@Composable
private fun StatSummaryItem(
    iconRes: Int,
    value: String,
    label: String,
    color: androidx.compose.ui.graphics.Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            color = color
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = DuoInkSecondary
        )
    }
}
