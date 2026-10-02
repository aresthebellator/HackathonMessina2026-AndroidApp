package com.exertia.wikingo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.exertia.wikingo.core.designsystem.DuoBackground
import com.exertia.wikingo.core.designsystem.DuoBorder
import com.exertia.wikingo.core.designsystem.DuoInk
import com.exertia.wikingo.core.designsystem.DuoInkSecondary
import com.exertia.wikingo.core.designsystem.DuoSurface
import com.exertia.wikingo.core.designsystem.WikiTrainerTheme
import com.exertia.wikingo.data.local.SavedArticleStore
import com.exertia.wikingo.presentation.dashboard.DashboardScreen
import com.exertia.wikingo.presentation.history.HistoryScreen
import com.exertia.wikingo.presentation.history.HistoryViewModel
import com.exertia.wikingo.presentation.saved.SavedArticlesScreen
import com.exertia.wikingo.presentation.trainer.TrainerScreen
import com.exertia.wikingo.presentation.trainer.TrainerViewModel
import org.koin.androidx.compose.koinViewModel

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
                        com.exertia.wikingo.core.i18n.LocalI18nLanguage provides language
                    ) {
                        Crossfade(
                            targetState = currentDestination,
                            animationSpec = tween(if (reducedMotion) 0 else 220),
                            label = "mainNavCrossfade"
                        ) { destination ->
                            when (destination) {
                                AppDestination.DASHBOARD -> {
                                    val stats = (historyState as? com.exertia.wikingo.presentation.history.HistoryUiState.Success)?.stats
                                        ?: com.exertia.wikingo.domain.model.UserStats(0, 0, 0, 0, "")
                                    DashboardScreen(
                                        stats = stats,
                                        language = language,
                                        darkMode = darkMode,
                                        onLanguageToggle = {
                                            language = if (language == "it") "en" else "it"
                                            settings.edit().putString("language", language).apply()
                                        },
                                        onThemeToggle = {
                                            darkMode = !darkMode
                                            settings.edit().putBoolean("dark_mode", darkMode).apply()
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
                            SettingsSheet(
                                language = language,
                                darkMode = darkMode,
                                soundEnabled = soundEnabled,
                                reducedMotion = reducedMotion,
                                largeText = largeText,
                                highContrast = highContrast,
                                onDismiss = { settingsOpen = false },
                                onDarkModeChange = {
                                    darkMode = it
                                    settings.edit().putBoolean("dark_mode", it).apply()
                                },
                                onSoundChange = {
                                    soundEnabled = it
                                    settings.edit().putBoolean("sound_enabled", it).apply()
                                    trainerViewModel.setSoundEnabled(it)
                                },
                                onReducedMotionChange = {
                                    reducedMotion = it
                                    settings.edit().putBoolean("reduced_motion", it).apply()
                                },
                                onLargeTextChange = {
                                    largeText = it
                                    settings.edit().putBoolean("large_text", it).apply()
                                },
                                onHighContrastChange = {
                                    highContrast = it
                                    settings.edit().putBoolean("high_contrast", it).apply()
                                },
                                onLanguageChange = {
                                    language = it
                                    settings.edit().putString("language", it).apply()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@androidx.compose.runtime.Composable
private fun SettingsSheet(
    language: String,
    darkMode: Boolean,
    soundEnabled: Boolean,
    reducedMotion: Boolean,
    largeText: Boolean,
    highContrast: Boolean,
    onDismiss: () -> Unit,
    onDarkModeChange: (Boolean) -> Unit,
    onSoundChange: (Boolean) -> Unit,
    onReducedMotionChange: (Boolean) -> Unit,
    onLargeTextChange: (Boolean) -> Unit,
    onHighContrastChange: (Boolean) -> Unit,
    onLanguageChange: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = DuoBackground,
        dragHandle = {
            Surface(
                modifier = Modifier.padding(vertical = 8.dp).size(width = 42.dp, height = 5.dp),
                shape = RoundedCornerShape(50),
                color = DuoBorder
            ) {}
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = com.exertia.wikingo.core.i18n.i18n("settings.title"),
                style = MaterialTheme.typography.headlineSmall,
                color = DuoInk
            )
            Text(
                text = com.exertia.wikingo.core.i18n.i18n("settings.subtitle"),
                style = MaterialTheme.typography.bodyMedium,
                color = DuoInkSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))

            SettingItem(
                icon = Icons.Default.DarkMode,
                title = com.exertia.wikingo.core.i18n.i18n("settings.dark_theme"),
                description = com.exertia.wikingo.core.i18n.i18n("settings.dark_theme_description"),
                checked = darkMode,
                onCheckedChange = onDarkModeChange
            )
            SettingItem(
                icon = Icons.Default.VolumeUp,
                title = com.exertia.wikingo.core.i18n.i18n("settings.sound_effects"),
                description = com.exertia.wikingo.core.i18n.i18n("settings.sound_effects_description"),
                checked = soundEnabled,
                onCheckedChange = onSoundChange
            )
            SettingItem(
                icon = Icons.Default.AccessibilityNew,
                title = com.exertia.wikingo.core.i18n.i18n("settings.reduce_motion"),
                description = com.exertia.wikingo.core.i18n.i18n("settings.reduce_motion_description"),
                checked = reducedMotion,
                onCheckedChange = onReducedMotionChange
            )
            SettingItem(
                icon = Icons.Default.TextFields,
                title = com.exertia.wikingo.core.i18n.i18n("settings.large_text"),
                description = com.exertia.wikingo.core.i18n.i18n("settings.large_text_description"),
                checked = largeText,
                onCheckedChange = onLargeTextChange
            )
            SettingItem(
                icon = Icons.Default.AccessibilityNew,
                title = com.exertia.wikingo.core.i18n.i18n("settings.high_contrast"),
                description = com.exertia.wikingo.core.i18n.i18n("settings.high_contrast_description"),
                checked = highContrast,
                onCheckedChange = onHighContrastChange
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = DuoBorder)
            Text(
                text = com.exertia.wikingo.core.i18n.i18n("settings.language_prefix"),
                style = MaterialTheme.typography.labelLarge,
                color = DuoInkSecondary
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf("it" to "Italiano", "en" to "English").forEach { (code, label) ->
                    Button(
                        onClick = { onLanguageChange(code) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (language == code) MaterialTheme.colorScheme.primary else DuoSurface,
                            contentColor = if (language == code) MaterialTheme.colorScheme.onPrimary else DuoInk
                        )
                    ) {
                        Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(label)
                    }
                }
            }
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(com.exertia.wikingo.core.i18n.i18n("settings.save_and_close"))
            }
        }
    }
}

@androidx.compose.runtime.Composable
private fun SettingItem(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = DuoSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, DuoBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(10.dp)
                )
            }
            Spacer(modifier = Modifier.size(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, color = DuoInk)
                Text(description, style = MaterialTheme.typography.bodySmall, color = DuoInkSecondary)
            }
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}
