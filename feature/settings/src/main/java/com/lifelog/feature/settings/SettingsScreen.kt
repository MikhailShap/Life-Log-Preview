package com.lifelog.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifelog.core.domain.model.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            Text("Theme", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
            ThemeMode.values().forEach { themeMode ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (themeMode == uiState.themeMode),
                            onClick = { viewModel.setThemeMode(themeMode) }
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (themeMode == uiState.themeMode),
                        onClick = { viewModel.setThemeMode(themeMode) }
                    )
                    Text(
                        text = themeMode.name,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Language", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
            Row {
                Button(onClick = { viewModel.setLanguage("en") }, enabled = uiState.language != "en") {
                    Text("English")
                }
                Button(onClick = { viewModel.setLanguage("ru") }, enabled = uiState.language != "ru") {
                    Text("Русский")
                }
            }
        }
    }
}
