package com.lifelog.feature.videonotes

import android.Manifest
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.view.PreviewView
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RecordVideoScreen(
    viewModel: VideoNotesViewModel,
    onVideoSaved: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var recording: Recording? by remember { mutableStateOf(null) }
    var isRecording by remember { mutableStateOf(false) }
    var previewUri by remember { mutableStateOf<Uri?>(null) }
    var recordedDuration by remember { mutableLongStateOf(0L) }
    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_FRONT_CAMERA) }

    val videoCapture: VideoCapture<Recorder> = remember {
        val recorder = Recorder.Builder()
            .setQualitySelector(QualitySelector.from(Quality.HD))
            .build()
        VideoCapture.withOutput(recorder)
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

    Scaffold(
        containerColor = Color.Black
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Circular Camera Preview or Video Player
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(350.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray)
            ) {
                if (previewUri == null) {
                    CameraPreview(
                        modifier = Modifier.fillMaxSize(),
                        cameraSelector = cameraSelector,
                        videoCapture = videoCapture
                    )
                } else {
                    VideoPlayer(
                        modifier = Modifier.fillMaxSize(),
                        uri = previewUri!!
                    )
                }
            }

            // Controls
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 64.dp)
            ) {
                if (previewUri == null) {
                    // Recording Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Camera Switch Button
                        IconButton(
                            onClick = {
                                cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                    CameraSelector.DEFAULT_FRONT_CAMERA
                                } else {
                                    CameraSelector.DEFAULT_BACK_CAMERA
                                }
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Color.DarkGray.copy(alpha = 0.5f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.FlipCameraAndroid, contentDescription = "Switch Camera", tint = Color.White)
                            }
                        }

                        FloatingActionButton(
                            onClick = {
                                if (isRecording) {
                                    recording?.stop()
                                    recording = null
                                    isRecording = false
                                } else {
                                    val mainExecutor: Executor = ContextCompat.getMainExecutor(context)
                                    
                                    val outputDir = File(context.filesDir, "videos").apply { mkdirs() }
                                    val fileName = "video_note_${System.currentTimeMillis()}.mp4"
                                    val file = File(outputDir, fileName)
                                    
                                    val fileOutputOptions = FileOutputOptions.Builder(file).build()

                                    recording = videoCapture.output
                                        .prepareRecording(context, fileOutputOptions)
                                        .withAudioEnabled()
                                        .start(mainExecutor) { recordEvent ->
                                            when (recordEvent) {
                                                is VideoRecordEvent.Start -> {
                                                    isRecording = true
                                                }
                                                is VideoRecordEvent.Finalize -> {
                                                    isRecording = false
                                                    if (!recordEvent.hasError()) {
                                                        previewUri = Uri.fromFile(file)
                                                        recordedDuration = recordEvent.recordingStats.recordedDurationNanos / 1_000_000
                                                    } else {
                                                        file.delete()
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
                            Icon(
                                if (isRecording) Icons.Default.Stop else Icons.Default.FiberManualRecord,
                                contentDescription = if (isRecording) "Stop" else "Record",
                                modifier = Modifier.size(42.dp),
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(48.dp))
                    }
                } else {
                    // Preview Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { 
                                previewUri?.path?.let { File(it).delete() }
                                previewUri = null 
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Retake")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(id = R.string.retake))
                        }

                        FloatingActionButton(
                            onClick = {
                                previewUri?.let { uri ->
                                    viewModel.saveVideoNote(uri, recordedDuration)
                                    onVideoSaved()
                                }
                            },
                            containerColor = Color(0xFF4CAF50), // Green
                            shape = CircleShape
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "Done", tint = Color.White)
                        }
                    }
                }
            }
            
            if (isRecording) {
                 Text(
                    text = stringResource(id = R.string.recording_label),
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 32.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    cameraSelector: CameraSelector,
    videoCapture: VideoCapture<Recorder>
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }

    LaunchedEffect(cameraSelector) {
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                videoCapture
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    AndroidView(
        factory = { previewView },
        modifier = modifier
    )
}
