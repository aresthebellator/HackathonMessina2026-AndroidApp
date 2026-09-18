package com.hackaton.wikitrainer

import androidx.compose.foundation.layout.Column
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.hackaton.wikitrainer.core.designsystem.DuoBackground
import com.hackaton.wikitrainer.core.designsystem.WikiTrainerTheme
import com.hackaton.wikitrainer.presentation.history.HistoryScreen
import com.hackaton.wikitrainer.presentation.history.HistoryViewModel
import com.hackaton.wikitrainer.presentation.dashboard.DashboardScreen
import com.hackaton.wikitrainer.presentation.trainer.TrainerScreen
import com.hackaton.wikitrainer.presentation.trainer.TrainerViewModel
import com.hackaton.wikitrainer.presentation.saved.SavedArticlesScreen
import com.hackaton.wikitrainer.data.local.SavedArticleStore
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color

enum class AppDestination {
    DASHBOARD,
    TRAINER,
    HISTORY,
    SAVED
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var darkMode by remember { mutableStateOf(false) }
            WikiTrainerTheme(darkTheme = darkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DuoBackground
                ) {
                    var currentDestination by remember { mutableStateOf(AppDestination.DASHBOARD) }
                    var language by remember { mutableStateOf("it") }
                    var settingsOpen by remember { mutableStateOf(false) }
                    var soundEnabled by remember { mutableStateOf(true) }
                    var highContrast by remember { mutableStateOf(false) }
                    var reducedMotion by remember { mutableStateOf(false) }
                    var largeText by remember { mutableStateOf(false) }
                    val historyViewModel: HistoryViewModel = koinViewModel()
                    val historyState by historyViewModel.uiState.collectAsState()
                    val trainerViewModel: TrainerViewModel = koinViewModel()
                    val savedArticleStore = androidx.compose.runtime.remember {
                        SavedArticleStore(this@MainActivity)
                    }

                    Crossfade(
                        targetState = currentDestination,
                        label = "mainNavCrossfade"
                    ) { destination ->
                        when (destination) {
                            AppDestination.DASHBOARD -> {
                                val stats = (historyState as? com.hackaton.wikitrainer.presentation.history.HistoryUiState.Success)?.stats
                                    ?: com.hackaton.wikitrainer.domain.model.UserStats(0, 0, 0, 0, "")
                                DashboardScreen(
                                    stats = stats,
                                    language = language,
                                    onLanguageToggle = { language = if (language == "it") "en" else "it" },
                                    onStartLesson = {
                                        trainerViewModel.loadNewLesson(language)
                                        currentDestination = AppDestination.TRAINER
                                    },
                                    onQuickQuiz = {
                                        trainerViewModel.loadNewLesson(language)
                                        currentDestination = AppDestination.TRAINER
                                    },
                                    onHistory = { currentDestination = AppDestination.HISTORY },
                                    onSettings = { settingsOpen = true },
                                    onSavedArticles = { currentDestination = AppDestination.SAVED }
                                )
                            }
                            AppDestination.TRAINER -> {
                                TrainerScreen(
                                    viewModel = trainerViewModel,
                                    onNavigateToHistory = {
                                        currentDestination = AppDestination.DASHBOARD
                                    },
                                    onSaveArticle = { savedArticleStore.toggle(it) }
                                )
                            }
                            AppDestination.HISTORY -> {
                                HistoryScreen(
                                    viewModel = historyViewModel,
                                    onBackClick = {
                                        currentDestination = AppDestination.DASHBOARD
                                    }
                                )
                            }
                            AppDestination.SAVED -> {
                                SavedArticlesScreen(
                                    onBackClick = { currentDestination = AppDestination.DASHBOARD }
                                )
                            }
                        }
                    }

                    if (settingsOpen) {
                        AlertDialog(
                            onDismissRequest = { settingsOpen = false },
                            title = { Text("Impostazioni & Accessibilità") },
                            text = {
                                Column {
                                    PreferenceRow("Tema scuro", darkMode) { darkMode = !darkMode }
                                    PreferenceRow("Effetti sonori", soundEnabled) {
                                        soundEnabled = !soundEnabled
                                        trainerViewModel.setSoundEnabled(soundEnabled)
                                    }
                                    PreferenceRow("Riduci animazioni", reducedMotion) { reducedMotion = !reducedMotion }
                                    PreferenceRow("Testo grande", largeText) { largeText = !largeText }
                                    PreferenceRow("Alto contrasto", highContrast) { highContrast = !highContrast }
                                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                                    Text("Lingua: ${language.uppercase()}", style = MaterialTheme.typography.bodyMedium)
                                    OutlinedButton(
                                        onClick = { language = if (language == "it") "en" else "it" },
                                        modifier = Modifier.padding(top = 8.dp)
                                    ) { Text("Cambia lingua") }
                                }
                            },
                            confirmButton = {
                                TextButton(onClick = { settingsOpen = false }) { Text("Salva e chiudi") }
                            }
                        )
                    }
                }
            }
        }
    }
}

@androidx.compose.runtime.Composable
private fun PreferenceRow(label: String, checked: Boolean, onCheckedChange: () -> Unit) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Text(label, modifier = Modifier.weight(1f))
        Checkbox(checked = checked, onCheckedChange = { onCheckedChange() })
    }
}
