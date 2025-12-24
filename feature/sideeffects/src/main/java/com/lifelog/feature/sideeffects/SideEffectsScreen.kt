package com.lifelog.feature.sideeffects

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifelog.core.domain.model.SideEffect
import com.lifelog.core.ui.R
import com.lifelog.core.ui.components.ScreenHeader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideEffectsScreen(
    viewModel: SideEffectsViewModel = hiltViewModel(),
    onMenuClick: () -> Unit,
    selectedDate: Long,
    onDateClick: () -> Unit
) {
    val sideEffects by viewModel.sideEffects.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    val dateText = remember(selectedDate, Locale.getDefault()) {
        try {
            SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()).format(Date(selectedDate))
        } catch (e: Exception) {
            ""
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ScreenHeader(
            title = stringResource(id = R.string.side_effects_title),
            onMenuClick = onMenuClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = dateText.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { onDateClick() }
            )
            
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(sideEffects) { sideEffect ->
                    SideEffectCard(sideEffect = sideEffect, onDelete = {
                        viewModel.deleteSideEffect(sideEffect)
                    })
                }
            }
        }

        if (showDialog) {
            AddSideEffectDialog(
                onDismiss = { showDialog = false },
                onConfirm = {
                    val effectWithDate = it.copy(date = selectedDate)
                    viewModel.addSideEffect(effectWithDate)
                    showDialog = false
                }
            )
        }
    }

    // FAB for adding side effects
    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = { showDialog = true },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 88.dp, end = 16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.add_side_effect))
        }
    }
}

@Composable
fun SideEffectCard(sideEffect: SideEffect, onDelete: () -> Unit) {
    var isChecked by remember { mutableStateOf(true) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF23202E)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked, 
                onCheckedChange = { isChecked = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = sideEffect.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(id = R.string.frequency_label, sideEffect.frequency),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun AddSideEffectDialog(onDismiss: () -> Unit, onConfirm: (SideEffect) -> Unit) {
    var name by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("ONCE") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        title = { Text(stringResource(id = R.string.add_side_effect), style = MaterialTheme.typography.titleLarge) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(id = R.string.symptom_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(id = R.string.how_often), style = MaterialTheme.typography.bodyMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = frequency == "ONCE",
                        onClick = { frequency = "ONCE" }
                    )
                    Text(stringResource(id = R.string.once))
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(
                        selected = frequency == "ALL_DAY",
                        onClick = { frequency = "ALL_DAY" }
                    )
                    Text(stringResource(id = R.string.several_times))
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val newSideEffect = SideEffect(
                            name = name,
                            frequency = frequency,
                            date = System.currentTimeMillis()
                        )
                        onConfirm(newSideEffect)
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text(stringResource(id = R.string.add))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.textButtonColors()) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    )
}
