package com.lifelog.feature.today

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifelog.core.ui.theme.LifeLogAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
    viewModel: TodayViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("How are you feeling?") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("My mood", style = MaterialTheme.typography.titleMedium)
            // Placeholder for emoji selector
            Spacer(modifier = Modifier.height(16.dp))

            Text("My indicators", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Energy")
            Slider(value = uiState.energy, onValueChange = viewModel::onEnergyChange)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Stress")
            Slider(value = uiState.anxiety, onValueChange = viewModel::onAnxietyChange)

            Spacer(modifier = Modifier.height(16.dp))

            Text("My notes", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = uiState.notes,
                onValueChange = viewModel::onNotesChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("What's on your mind?") }
            )

            Spacer(modifier = Modifier.height(16.dp))
            // Placeholder for video note button

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = viewModel::saveEntry,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Entry")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodayScreenPreview() {
    LifeLogAppTheme {
        // This preview won't work with the ViewModel out of the box.
        // For a working preview, you'd typically pass a mock/fake ViewModel
        // or hoist the state out of the screen Composable.
        // For now, we'll rely on running in an emulator.
    }
}
