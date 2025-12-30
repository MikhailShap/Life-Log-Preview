package com.lifelog.feature.log

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifelog.core.ui.R
import com.lifelog.core.ui.components.ModernDatePicker
import com.lifelog.core.ui.components.ScreenHeader
import kotlin.math.roundToInt

@Composable
fun LogScreen(
    viewModel: LogViewModel = hiltViewModel(),
    onNavigateToRecord: () -> Unit,
    onMenuClick: () -> Unit,
    selectedDate: Long,
    onDateClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Shared ScreenHeader
        ScreenHeader(
            title = stringResource(id = R.string.mood_title),
            onMenuClick = onMenuClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Modern date picker
            ModernDatePicker(
                selectedDate = selectedDate,
                onClick = onDateClick
            )

            // Mood Section
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = stringResource(id = R.string.my_mood),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MoodItem(icon = Icons.Default.SentimentVeryDissatisfied, label = stringResource(id = R.string.mood_awful), isSelected = uiState.mood == 1) { viewModel.onMoodChange(1) }
                    MoodItem(icon = Icons.Default.SentimentDissatisfied, label = stringResource(id = R.string.mood_bad), isSelected = uiState.mood == 2) { viewModel.onMoodChange(2) }
                    MoodItem(icon = Icons.Default.SentimentNeutral, label = stringResource(id = R.string.mood_meh), isSelected = uiState.mood == 3) { viewModel.onMoodChange(3) }
                    MoodItem(icon = Icons.Default.SentimentSatisfied, label = stringResource(id = R.string.mood_good), isSelected = uiState.mood == 4) { viewModel.onMoodChange(4) }
                    MoodItem(icon = Icons.Default.SentimentVerySatisfied, label = stringResource(id = R.string.mood_rad), isSelected = uiState.mood == 5) { viewModel.onMoodChange(5) }
                }
            }

            // Indicators Section
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = stringResource(id = R.string.my_indicators),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                SliderGroup(
                    label = stringResource(id = R.string.energy),
                    value = uiState.energy,
                    onValueChange = { viewModel.onEnergyChange(it) }
                )
                
                SliderGroup(
                    label = stringResource(id = R.string.stress),
                    value = uiState.stress,
                    onValueChange = { viewModel.onStressChange(it) }
                )

                SliderGroup(
                    label = stringResource(id = R.string.libido),
                    value = uiState.libido,
                    onValueChange = { viewModel.onLibidoChange(it) }
                )
            }

            // Notes Section
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = stringResource(id = R.string.my_notes),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                OutlinedTextField(
                    value = uiState.notes,
                    onValueChange = { viewModel.onNotesChange(it) },
                    placeholder = { Text(stringResource(id = R.string.notes_placeholder)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF23202E), // Opaque dark purple-grey
                        unfocusedContainerColor = Color(0xFF23202E),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Save Button
            Button(
                onClick = { viewModel.saveEntry(selectedDate) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(id = R.string.save_entry),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun MoodItem(icon: ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.2f else 1.0f,
        animationSpec = tween(durationMillis = 300),
        label = "scale"
    )

    val containerColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
        animationSpec = tween(durationMillis = 300),
        label = "containerColor"
    )
    
    val gradientColors = listOf(Color(0xFF42A5F5), Color(0xFF26C6DA))
    val unselectedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .graphicsLayer(alpha = 0.99f)
                        .drawWithCache {
                            onDrawWithContent {
                                drawContent()
                                drawRect(
                                    brush = Brush.linearGradient(gradientColors),
                                    blendMode = BlendMode.SrcIn
                                )
                            }
                        }
                )
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = unselectedColor,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SliderGroup(label: String, value: Float, onValueChange: (Float) -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${(value * 10).roundToInt()}",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        }
        Slider(
            value = value * 10,
            onValueChange = { newValue ->
                onValueChange(newValue / 10f)
            },
            valueRange = 0f..10f,
            steps = 9, 
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        )
    }
}
