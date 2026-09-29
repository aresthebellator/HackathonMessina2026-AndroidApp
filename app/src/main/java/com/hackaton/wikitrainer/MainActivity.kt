package com.hackaton.wikitrainer

import androidx.compose.foundation.layout.Column
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
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
            val settings = remember {
                getSharedPreferences("wikingo_settings", MODE_PRIVATE)
            }
            var darkMode by remember { mutableStateOf(settings.getBoolean("dark_mode", false)) }
            var highContrast by remember { mutableStateOf(settings.getBoolean("high_contrast", false)) }
            var reducedMotion by remember { mutableStateOf(settings.getBoolean("reduced_motion", false)) }
            var largeText by remember { mutableStateOf(settings.getBoolean("large_text", false)) }
            var soundEnabled by remember { mutableStateOf(settings.getBoolean("sound_enabled", true)) }
            WikiTrainerTheme(
                darkTheme = darkMode,
                highContrast = highContrast,
                largeText = largeText,
                reduceMotion = reducedMotion
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DuoBackground
                ) {
                    var currentDestination by remember { mutableStateOf(AppDestination.DASHBOARD) }
                    var language by remember { mutableStateOf(settings.getString("language", "it") ?: "it") }
                    var settingsOpen by remember { mutableStateOf(false) }
                    val historyViewModel: HistoryViewModel = koinViewModel()
                    val historyState by historyViewModel.uiState.collectAsState()
                    val trainerViewModel: TrainerViewModel = koinViewModel()
                    androidx.compose.runtime.LaunchedEffect(soundEnabled) {
                        trainerViewModel.setSoundEnabled(soundEnabled)
                    }
                    val savedArticleStore = androidx.compose.runtime.remember {
                        SavedArticleStore(this@MainActivity)
                    }

                    androidx.compose.runtime.CompositionLocalProvider(
                        com.hackaton.wikitrainer.core.i18n.LocalI18nLanguage provides language
                    ) {
                        Crossfade(
                            targetState = currentDestination,
                            animationSpec = tween(if (reducedMotion) 0 else 220),
                            label = "mainNavCrossfade"
                        ) { destination ->
                            when (destination) {
                                AppDestination.DASHBOARD -> {
                                    val stats = (historyState as? com.hackaton.wikitrainer.presentation.history.HistoryUiState.Success)?.stats
                                        ?: com.hackaton.wikitrainer.domain.model.UserStats(0, 0, 0, 0, "")
                                    DashboardScreen(
                                        stats = stats,
                                        language = language,
                                        onLanguageToggle = {
                                            language = if (language == "it") "en" else "it"
                                            settings.edit().putString("language", language).apply()
                                        },
                                        onStartLesson = { topic, lessonNumber ->
                                            trainerViewModel.loadLesson(language = language, topic = topic, lessonNumber = lessonNumber)
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
                                title = { Text(com.hackaton.wikitrainer.core.i18n.i18n("settings.title")) },
                                text = {
                                    Column {
                                        PreferenceRow(com.hackaton.wikitrainer.core.i18n.i18n("settings.dark_theme"), darkMode) {
                                            darkMode = !darkMode
                                            settings.edit().putBoolean("dark_mode", darkMode).apply()
                                        }
                                        PreferenceRow(com.hackaton.wikitrainer.core.i18n.i18n("settings.sound_effects"), soundEnabled) {
                                            soundEnabled = !soundEnabled
                                            settings.edit().putBoolean("sound_enabled", soundEnabled).apply()
                                            trainerViewModel.setSoundEnabled(soundEnabled)
                                        }
                                        PreferenceRow(com.hackaton.wikitrainer.core.i18n.i18n("settings.reduce_motion"), reducedMotion) {
                                            reducedMotion = !reducedMotion
                                            settings.edit().putBoolean("reduced_motion", reducedMotion).apply()
                                        }
                                        PreferenceRow(com.hackaton.wikitrainer.core.i18n.i18n("settings.large_text"), largeText) {
                                            largeText = !largeText
                                            settings.edit().putBoolean("large_text", largeText).apply()
                                        }
                                        PreferenceRow(com.hackaton.wikitrainer.core.i18n.i18n("settings.high_contrast"), highContrast) {
                                            highContrast = !highContrast
                                            settings.edit().putBoolean("high_contrast", highContrast).apply()
                                        }
                                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                                        Text("${com.hackaton.wikitrainer.core.i18n.i18n("settings.language_prefix")} ${language.uppercase()}", style = MaterialTheme.typography.bodyMedium)
                                        OutlinedButton(
                                            onClick = {
                                                language = if (language == "it") "en" else "it"
                                                settings.edit().putString("language", language).apply()
                                            },
                                            modifier = Modifier.padding(top = 8.dp)
                                        ) { Text(com.hackaton.wikitrainer.core.i18n.i18n("settings.change_language")) }
                                    }
                                },
                                confirmButton = {
                                    TextButton(onClick = { settingsOpen = false }) { Text(com.hackaton.wikitrainer.core.i18n.i18n("settings.save_and_close")) }
                                }
                            )
                        }
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
