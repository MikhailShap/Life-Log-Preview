package com.lifelog.feature.today

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

private const val TAG = "TodayScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
    viewModel: TodayViewModel = hiltViewModel()
) {
    Log.d(TAG, "TodayScreen composable executing")
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Запись сна") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        OutlinedTextField(
                            value = uiState.sleepStartTime,
                            onValueChange = { viewModel.onSleepStartTimeChange(it) },
                            label = { Text("Время отхода ко сну") }
                        )
                        OutlinedTextField(
                            value = uiState.sleepEndTime,
                            onValueChange = { viewModel.onSleepEndTimeChange(it) },
                            label = { Text("Время пробуждения") }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Качество сна")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val qualityIcons = listOf(
                            Icons.Filled.SentimentVeryDissatisfied,
                            Icons.Filled.SentimentDissatisfied,
                            Icons.Filled.SentimentNeutral,
                            Icons.Filled.SentimentSatisfied,
                            Icons.Filled.SentimentVerySatisfied
                        )
                        qualityIcons.forEachIndexed { index, icon ->
                            IconButton(onClick = { viewModel.onSleepQualityChange(index + 1) }) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (uiState.sleepQuality == index + 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.saveEntry() },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Сохранить", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
    Log.d(TAG, "TodayScreen composable finished")
}
