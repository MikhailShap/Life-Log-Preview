package com.lifelog.feature.videonotes

import android.Manifest
import android.content.ContentValues
import android.provider.MediaStore
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
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
import androidx.core.util.Consumer
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RecordVideoScreen(
    onVideoSaved: (String, Long) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var recording: Recording? by remember { mutableStateOf(null) }
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }
    val videoCapture: MutableState<VideoCapture<Recorder>?> = remember { mutableStateOf(null) }

    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    )

    if (permissionsState.allPermissionsGranted) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val recorder = Recorder.Builder()
                        .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                        .build()
                    videoCapture.value = VideoCapture.withOutput(recorder)

                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner, cameraSelector, preview, videoCapture.value
                            )
                        } catch (exc: Exception) {
                            // Handle exceptions
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            Button(
                onClick = {
                    val currentRecording = recording
                    if (currentRecording != null) {
                        currentRecording.stop()
                        recording = null
                        return@Button
                    }

                    val name = "lifelog-video-" + SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
                        .format(System.currentTimeMillis()) + ".mp4"
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                        put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
                    }
                    val mediaStoreOutputOptions = MediaStoreOutputOptions
                        .Builder(context.contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                        .setContentValues(contentValues)
                        .build()

                    recording = videoCapture.value?.output?.prepareRecording(context, mediaStoreOutputOptions)
                        ?.withAudioEnabled()
                        ?.start(ContextCompat.getMainExecutor(context), object : Consumer<VideoRecordEvent> {
                            override fun accept(t: VideoRecordEvent) {
                                when (t) {
                                    is VideoRecordEvent.Finalize -> {
                                        if (!t.hasError()) {
                                            val uri = t.outputResults.outputUri
                                            // You'll need a way to get the duration. This is a simplified example.
                                            onVideoSaved(uri.toString(), 0L)
                                        } else {
                                            recording?.close()
                                            recording = null
                                        }
                                    }
                                }
                            }
                        })
                },
                modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Record")
            }
        }
    } else {
        Column {
            Text("Camera and microphone permissions are required to record video notes.")
            Button(onClick = { permissionsState.launchMultiplePermissionRequest() }) {
                Text("Request Permissions")
            }
        }
    }
}
