package com.hackaton.wikitrainer.core.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hackaton.wikitrainer.R
import com.hackaton.wikitrainer.core.designsystem.WikiBlack
import com.hackaton.wikitrainer.core.designsystem.WikiShuttleGray

/**
 * Top Stat Header styled with Wikipedia Brand and Logo Colors:
 * Displays Streak Flame, XP Bolt, and Hearts in crisp Wikipedia monochrome styling.
 */
@Composable
fun StreakHeader(
    streakDays: Int,
    totalXp: Int,
    hearts: Int = 10,
    onCloseClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onCloseClick != null) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Esci",
                tint = WikiShuttleGray,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onCloseClick() }
            )
        } else {
            Spacer(modifier = Modifier.width(24.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Streak Flame Item
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_flame),
                    contentDescription = "Streak",
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$streakDays",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = WikiBlack
                )
            }

            // XP Bolt Item
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_bolt),
                    contentDescription = "XP",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$totalXp",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = WikiBlack
                )
            }

            // Hearts Item
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_heart),
                    contentDescription = "Cuori",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$hearts",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = WikiBlack
                )
            }
        }
    }
}
