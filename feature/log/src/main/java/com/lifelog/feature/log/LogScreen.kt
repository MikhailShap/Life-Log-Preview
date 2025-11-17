package com.lifelog.feature.log

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifelog.core.domain.model.Entry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogScreen(
    viewModel: LogViewModel = hiltViewModel()
) {
    val entries by viewModel.entries.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Log") })
        }
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            items(entries) { entry ->
                EntryListItem(entry)
            }
        }
    }
}

@Composable
fun EntryListItem(entry: Entry) {
    // For now, just display the date and notes
    Text(text = "Date: ${entry.date}")
    Text(text = "Notes: ${entry.notes.orEmpty()}")
}
