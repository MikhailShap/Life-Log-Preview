package com.lifelog.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifelog.core.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.settings_title)) })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(stringResource(id = R.string.language), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(onClick = { viewModel.setLanguage("en") }, enabled = uiState.language != "en") {
                    Text(stringResource(id = R.string.english))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { viewModel.setLanguage("ru") }, enabled = uiState.language != "ru") {
                    Text(stringResource(id = R.string.russian))
                }
            }
        }
    }
}
