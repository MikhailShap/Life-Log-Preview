package com.lifelog.feature.videonotes

import android.Manifest
import android.content.ContentValues
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.text.SimpleDateFormat
import java.util.*
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
    val videoCapture: VideoCapture<Recorder> = remember {
        val recorder = Recorder.Builder()
            .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
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

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                val previewView = PreviewView(context)
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            videoCapture
                        )
                    } catch (exc: Exception) {
                        // Handle exceptions
                    }
                }, ContextCompat.getMainExecutor(context))
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        Button(
            onClick = {
                if (recording == null) {
                    val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

                    val name = "video_${SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(System.currentTimeMillis())}.mp4"
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                        put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
                        put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/VideoNotes")
                    }

                    val mediaStoreOutputOptions = MediaStoreOutputOptions
                        .Builder(context.contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                        .setContentValues(contentValues)
                        .build()
                    recording = videoCapture.output
                        .prepareRecording(context, mediaStoreOutputOptions)
                        .start(mainExecutor) { recordEvent ->
                            when (recordEvent) {
                                is VideoRecordEvent.Start -> {
                                    // Recording started
                                }
                                is VideoRecordEvent.Finalize -> {
                                    if (!recordEvent.hasError()) {
                                        viewModel.saveVideoNote(recordEvent.outputResults.outputUri, recordEvent.recordingStats.recordedDurationNanos)
                                        onVideoSaved()
                                    }
                                }
                            }
                        }
                } else {
                    recording?.stop()
                    recording = null
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Record")
        }
    }

    if (!permissionsState.allPermissionsGranted) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Permissions not granted.")
            Button(onClick = { permissionsState.launchMultiplePermissionRequest() }) {
                Text("Request permissions")
            }
        }
    }
}
