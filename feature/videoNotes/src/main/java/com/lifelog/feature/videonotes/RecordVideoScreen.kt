@file:OptIn(com.google.accompanist.permissions.ExperimentalPermissionsApi::class)

package com.lifelog.feature.videonotes

import android.Manifest
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.video.*
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.camera.view.video.AudioConfig
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.lifelog.core.ui.R
import java.io.File
import java.util.concurrent.Executor

@Composable
fun RecordVideoScreen(
    viewModel: VideoNotesViewModel,
    onVideoSaved: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isRecording by remember { mutableStateOf(false) }
    var previewUri by remember { mutableStateOf<Uri?>(null) }
    var recordedDuration by remember { mutableLongStateOf(0L) }
    var currentRecording by remember { mutableStateOf<Recording?>(null) }

    val cameraController = remember { 
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.VIDEO_CAPTURE)
            videoCaptureQualitySelector = QualitySelector.from(Quality.HD)
        }
    }

    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    )

    LaunchedEffect(Unit) {
        permissionsState.launchMultiplePermissionRequest()
    }

    if (!permissionsState.allPermissionsGranted) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(id = R.string.video_permissions_required), color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { permissionsState.launchMultiplePermissionRequest() }) {
                    Text(stringResource(id = R.string.grant_permissions))
                }
            }
        }
        return
    }

    Scaffold(containerColor = Color.Black) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Circular Camera Preview or Player
            Box(
                modifier = Modifier.align(Alignment.Center).size(350.dp).clip(CircleShape).background(Color.DarkGray)
            ) {
                if (previewUri == null) {
                    AndroidView(
                        factory = { ctx ->
                            PreviewView(ctx).apply {
                                controller = cameraController
                                cameraController.bindToLifecycle(lifecycleOwner)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    VideoPlayer(modifier = Modifier.fillMaxSize(), uri = previewUri!!)
                }
            }

            // Controls
            Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(bottom = 64.dp)) {
                if (previewUri == null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Switch Camera - Allowed during recording
                        IconButton(
                            onClick = {
                                cameraController.cameraSelector = if (cameraController.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) 
                                    CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
                            }
                        ) {
                            Box(modifier = Modifier.size(48.dp).background(Color.DarkGray.copy(alpha = 0.5f), CircleShape), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.FlipCameraAndroid, contentDescription = "Switch", tint = Color.White)
                            }
                        }

                        FloatingActionButton(
                            onClick = {
                                if (isRecording) {
                                    currentRecording?.stop()
                                } else {
                                    val mainExecutor = ContextCompat.getMainExecutor(context)
                                    val file = File(context.filesDir, "videos/video_${System.currentTimeMillis()}.mp4").apply { parentFile?.mkdirs() }
                                    
                                    currentRecording = cameraController.startRecording(
                                        FileOutputOptions.Builder(file).build(),
                                        AudioConfig.create(true),
                                        mainExecutor
                                    ) { event ->
                                        when (event) {
                                            is VideoRecordEvent.Start -> isRecording = true
                                            is VideoRecordEvent.Finalize -> {
                                                // Check if it's a real end or just a camera switch pause (not supported by CameraX yet for gapless)
                                                // If the recording stops, we reset the UI state.
                                                isRecording = false
                                                if (!event.hasError()) {
                                                    previewUri = Uri.fromFile(file)
                                                    recordedDuration = event.recordingStats.recordedDurationNanos / 1_000_000
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                            containerColor = if (isRecording) Color.Red else Color(0xFF9575CD),
                            shape = CircleShape,
                            modifier = Modifier.size(84.dp)
                        ) {
                            Icon(if (isRecording) Icons.Default.Stop else Icons.Default.FiberManualRecord, null, modifier = Modifier.size(42.dp), tint = Color.White)
                        }
                        
                        Spacer(Modifier.width(48.dp))
                    }
                } else {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(onClick = { previewUri = null }, colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)) {
                            Text(stringResource(id = R.string.retake))
                        }
                        FloatingActionButton(onClick = {
                            previewUri?.let { viewModel.saveVideoNote(it, recordedDuration) }
                            onVideoSaved()
                        }, containerColor = Color(0xFF4CAF50), shape = CircleShape) {
                            Icon(Icons.Default.Check, null, tint = Color.White)
                        }
                    }
                }
            }
        }
    }
}
