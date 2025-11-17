package com.lifelog.feature.meds

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifelog.core.domain.model.Med

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedsScreen(
    viewModel: MedsViewModel = hiltViewModel()
) {
    val meds by viewModel.meds.collectAsState()
    var showAddMedDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Medications") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddMedDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Medication")
            }
        }
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            items(meds) { med ->
                MedListItem(med = med, onDelete = { viewModel.deleteMed(med) })
            }
        }
    }

    if (showAddMedDialog) {
        AddMedDialog(
            onDismiss = { showAddMedDialog = false },
            onConfirm = { name, dosage, time ->
                viewModel.saveMed(name, dosage, time)
                showAddMedDialog = false
            }
        )
    }
}

@Composable
fun MedListItem(med: Med, onDelete: () -> Unit) {
    ListItem(
        headlineContent = { Text(med.name) },
        supportingContent = { Text("${med.dosage} - ${med.timeOfDay}") },
        trailingContent = {
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Medication")
            }
        }
    )
}

@Composable
fun AddMedDialog(onDismiss: () -> Unit, onConfirm: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var timeOfDay by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Medication") },
        text = {
            Column {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                TextField(value = dosage, onValueChange = { dosage = it }, label = { Text("Dosage") })
                TextField(value = timeOfDay, onValueChange = { timeOfDay = it }, label = { Text("Time of Day") })
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(name, dosage, timeOfDay) }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
