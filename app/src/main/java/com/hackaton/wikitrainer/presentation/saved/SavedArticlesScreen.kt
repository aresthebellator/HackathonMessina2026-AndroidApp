package com.hackaton.wikitrainer.presentation.saved

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hackaton.wikitrainer.core.designsystem.DuoBackground
import com.hackaton.wikitrainer.core.designsystem.DuoBorder
import com.hackaton.wikitrainer.core.designsystem.DuoInk
import com.hackaton.wikitrainer.core.designsystem.DuoInkSecondary
import com.hackaton.wikitrainer.data.local.SavedArticle
import com.hackaton.wikitrainer.data.local.SavedArticleStore
import coil.compose.AsyncImage

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun SavedArticlesScreen(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val store = remember { SavedArticleStore(context) }
    var articles by remember { mutableStateOf(store.getAll()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Voci salvate", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Indietro")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DuoBackground)
            )
        },
        containerColor = DuoBackground,
        modifier = modifier.fillMaxSize()
    ) { padding ->
        if (articles.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Nessuna voce salvata", fontSize = 18.sp, fontWeight = FontWeight.Black, color = DuoInk)
                Spacer(Modifier.height(8.dp))
                Text("Usa il segnalibro al termine di un quiz per creare la tua biblioteca.", color = DuoInkSecondary)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(articles, key = { it.pageId }) { article ->
                    SavedArticleCard(
                        article = article,
                        onOpen = {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(article.wikiUrl)))
                        },
                        onDelete = {
                            store.remove(article.pageId)
                            articles = store.getAll()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SavedArticleCard(article: SavedArticle, onOpen: () -> Unit, onDelete: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().background(DuoBackground)
            .border(2.dp, DuoBorder, RoundedCornerShape(16.dp)).padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (article.thumbnailUrl != null) {
                AsyncImage(article.thumbnailUrl, article.title, modifier = Modifier.size(52.dp))
                Spacer(Modifier.padding(4.dp))
            }
            Text(article.title, modifier = Modifier.weight(1f), fontWeight = FontWeight.Black, color = DuoInk)
            IconButton(onClick = onOpen) { Icon(Icons.Default.OpenInBrowser, "Apri su Wikipedia") }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, "Rimuovi") }
        }
        if (article.description.isNotBlank()) {
            Text(article.description, fontSize = 12.sp, color = DuoInkSecondary)
        }
        Text(article.extract.take(220), fontSize = 12.sp, color = DuoInk, modifier = Modifier.padding(top = 6.dp))
    }
}
