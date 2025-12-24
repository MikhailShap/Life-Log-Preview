package com.lifelog.feature.videonotes

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifelog.core.domain.model.VideoNote
import com.lifelog.core.ui.R
import com.lifelog.core.ui.components.ScreenHeader
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoNotesScreen(
    viewModel: VideoNotesViewModel = hiltViewModel(),
    onNavigateToRecord: () -> Unit,
    onMenuClick: () -> Unit,
    selectedDate: Long,
    onDateClick: () -> Unit
) {
    val notes by viewModel.videoNotes.collectAsState()
    var selectedVideo by remember { mutableStateOf<VideoNote?>(null) }

    // Filter notes by selected date
    val filteredNotes = remember(notes, selectedDate) {
        val selectedCal = Calendar.getInstance().apply { timeInMillis = selectedDate }
        notes.filter { note ->
            val noteCal = Calendar.getInstance().apply { timeInMillis = note.createdAt.time }
            noteCal.get(Calendar.YEAR) == selectedCal.get(Calendar.YEAR) &&
            noteCal.get(Calendar.DAY_OF_YEAR) == selectedCal.get(Calendar.DAY_OF_YEAR)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ScreenHeader(
            title = stringResource(id = R.string.menu_video_note),
            onMenuClick = onMenuClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val dateText = remember(selectedDate, Locale.getDefault()) {
                try {
                    SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()).format(Date(selectedDate))
                } catch (e: Exception) {
                    ""
                }
            }

            Text(
                text = dateText.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { onDateClick() }
            )

            if (filteredNotes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stringResource(id = R.string.no_video_notes), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onNavigateToRecord) {
                            Text(stringResource(id = R.string.record_first_note))
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredNotes) { note ->
                        VideoNoteGridItem(
                            note = note, 
                            onClick = { selectedVideo = note }
                        )
                    }
                }
            }
        }

        if (selectedVideo != null) {
            VideoPlayerDialog(
                note = selectedVideo!!,
                onDismiss = { selectedVideo = null }
            )
        }
    }
    
    // FAB position for video notes
    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = onNavigateToRecord,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 88.dp, end = 16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Record New")
        }
    }
}

@Composable
fun VideoNoteGridItem(note: VideoNote, onClick: () -> Unit) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.8f) 
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF23202E)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.PlayArrow, 
                    contentDescription = null, 
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = timeFormat.format(note.createdAt),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${note.duration / 1000} sec",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun VideoPlayerDialog(note: VideoNote, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Оставляем непрозрачным
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                val uri = try {
                    if (note.uri.startsWith("content://") || note.uri.startsWith("file://")) {
                        Uri.parse(note.uri)
                    } else {
                        Uri.fromFile(File(note.uri))
                    }
                } catch (e: Exception) {
                    Uri.EMPTY
                }

                Box(
                    modifier = Modifier
                        .size(300.dp) 
                        .clip(CircleShape)
                        .background(Color.Black)
                ) {
                    VideoPlayer(
                        uri = uri,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(onClick = onDismiss) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        }
    }
}
