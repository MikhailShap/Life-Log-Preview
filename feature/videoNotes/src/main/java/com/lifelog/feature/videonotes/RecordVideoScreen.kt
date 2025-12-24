@file:OptIn(com.google.accompanist.permissions.ExperimentalPermissionsApi::class)

package com.lifelog.feature.videonotes

import android.Manifest
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMuxer
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.ByteBuffer

@Composable
fun RecordVideoScreen(
    viewModel: VideoNotesViewModel,
    onVideoSaved: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    var isRecording by remember { mutableStateOf(false) }
    var previewUri by remember { mutableStateOf<Uri?>(null) }
    var recordedDuration by remember { mutableLongStateOf(0L) }
    var currentRecording by remember { mutableStateOf<Recording?>(null) }
    var isSwitchingCamera by remember { mutableStateOf(false) }
    var isMerging by remember { mutableStateOf(false) }

    // Store video segments for merging when camera is switched during recording
    val videoSegments = remember { mutableStateListOf<File>() }
    var currentSegmentFile by remember { mutableStateOf<File?>(null) }
    var totalRecordedDuration by remember { mutableLongStateOf(0L) }

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

    // Function to start recording a new segment
    fun startNewSegment() {
        val mainExecutor = ContextCompat.getMainExecutor(context)
        val file = File(context.filesDir, "videos/segment_${System.currentTimeMillis()}.mp4").apply { parentFile?.mkdirs() }
        currentSegmentFile = file

        currentRecording = cameraController.startRecording(
            FileOutputOptions.Builder(file).build(),
            AudioConfig.create(true),
            mainExecutor
        ) { event ->
            when (event) {
                is VideoRecordEvent.Start -> {
                    isRecording = true
                    isSwitchingCamera = false
                }
                is VideoRecordEvent.Finalize -> {
                    if (!event.hasError() && file.exists() && file.length() > 0) {
                        totalRecordedDuration += event.recordingStats.recordedDurationNanos / 1_000_000
                        videoSegments.add(file)
                    }

                    // If we're switching camera, start a new segment after camera switch completes
                    if (isSwitchingCamera) {
                        // Camera switch is handled in the onClick, new segment starts there
                    } else {
                        // Normal stop - merge segments if needed
                        isRecording = false
                        if (videoSegments.isNotEmpty()) {
                            isMerging = true
                            coroutineScope.launch {
                                val finalFile = File(context.filesDir, "videos/video_${System.currentTimeMillis()}.mp4")
                                val mergedFile = if (videoSegments.size > 1) {
                                    mergeVideoSegments(videoSegments.toList(), finalFile)
                                } else {
                                    // Only one segment, just rename it
                                    videoSegments.first().renameTo(finalFile)
                                    finalFile
                                }

                                // Clean up segments
                                videoSegments.forEach { it.delete() }
                                videoSegments.clear()

                                recordedDuration = totalRecordedDuration
                                totalRecordedDuration = 0L
                                previewUri = Uri.fromFile(mergedFile)
                                isMerging = false
                            }
                        }
                    }
                }
            }
        }
    }

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
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0.dp) // Disable default insets padding
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {

            // Circular Camera Preview or Player
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.85f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(Color.DarkGray)
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

            // Show merging indicator
            if (isMerging) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color.White)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Processing video...", color = Color.White)
                    }
                }
            }

            // Controls
            Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(bottom = 64.dp)) {
                if (previewUri == null && !isMerging) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        
                        // Center: Record Button
                        FloatingActionButton(
                            onClick = {
                                if (isRecording) {
                                    currentRecording?.stop()
                                } else {
                                    startNewSegment()
                                }
                            },
                            containerColor = if (isRecording) Color.Red else Color(0xFF9575CD),
                            shape = CircleShape,
                            modifier = Modifier.align(Alignment.Center).size(84.dp),
                            elevation = FloatingActionButtonDefaults.elevation(0.dp)
                        ) {
                            Icon(if (isRecording) Icons.Default.Stop else Icons.Default.FiberManualRecord, null, modifier = Modifier.size(42.dp), tint = Color.White)
                        }

                        // Right: Switch Camera
                        IconButton(
                            onClick = {
                                if (isRecording && !isSwitchingCamera) {
                                    // Mark that we're switching camera
                                    isSwitchingCamera = true
                                    // Stop current recording to save segment
                                    currentRecording?.stop()

                                    // Switch camera and start new segment after a short delay
                                    coroutineScope.launch {
                                        // Wait a bit for the recording to finalize
                                        kotlinx.coroutines.delay(300)

                                        // Switch camera
                                        cameraController.cameraSelector = if (cameraController.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                                            CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA

                                        // Wait for camera to switch
                                        kotlinx.coroutines.delay(500)

                                        // Start new segment
                                        startNewSegment()
                                    }
                                } else if (!isRecording) {
                                    // Not recording - just switch camera
                                    cameraController.cameraSelector = if (cameraController.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                                        CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
                                }
                            },
                            enabled = !isSwitchingCamera,
                            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 48.dp)
                        ) {
                            Box(modifier = Modifier.size(48.dp).background(Color(0xFF2E2A4A), CircleShape), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.FlipCameraAndroid, contentDescription = "Switch", tint = if (isSwitchingCamera) Color.Gray else Color.White)
                            }
                        }

                        // Left: Back Button (Only if not recording)
                        if (!isRecording) {
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier.align(Alignment.CenterStart).padding(start = 48.dp)
                            ) {
                                Box(modifier = Modifier.size(48.dp).background(Color(0xFF2E2A4A), CircleShape), contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                                        contentDescription = stringResource(id = R.string.back),
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                } else if (previewUri != null) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(onClick = {
                            previewUri = null
                            recordedDuration = 0L
                        }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E2A4A))) {
                            Text(stringResource(id = R.string.retake), color = Color.White)
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

/**
 * Merges multiple video segments into a single video file using MediaMuxer.
 * This is used when the camera is switched during recording.
 */
private suspend fun mergeVideoSegments(segments: List<File>, outputFile: File): File = withContext(Dispatchers.IO) {
    if (segments.isEmpty()) return@withContext outputFile
    if (segments.size == 1) {
        segments.first().renameTo(outputFile)
        return@withContext outputFile
    }

    outputFile.parentFile?.mkdirs()

    var muxer: MediaMuxer? = null
    var videoTrackIndex = -1
    var audioTrackIndex = -1
    var videoFormat: MediaFormat? = null
    var audioFormat: MediaFormat? = null

    try {
        muxer = MediaMuxer(outputFile.absolutePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)

        // First pass: get format from first valid segment
        for (segment in segments) {
            if (!segment.exists()) continue

            val extractor = MediaExtractor()
            try {
                extractor.setDataSource(segment.absolutePath)

                for (i in 0 until extractor.trackCount) {
                    val format = extractor.getTrackFormat(i)
                    val mime = format.getString(MediaFormat.KEY_MIME) ?: continue

                    if (mime.startsWith("video/") && videoFormat == null) {
                        videoFormat = format
                        videoTrackIndex = muxer.addTrack(format)
                    } else if (mime.startsWith("audio/") && audioFormat == null) {
                        audioFormat = format
                        audioTrackIndex = muxer.addTrack(format)
                    }
                }

                if (videoFormat != null) break
            } finally {
                extractor.release()
            }
        }

        if (videoTrackIndex < 0) {
            // No valid video found, return empty file
            return@withContext outputFile
        }

        muxer.start()

        var currentTimeOffsetUs = 0L
        val bufferSize = 1024 * 1024 // 1MB buffer
        val buffer = ByteBuffer.allocate(bufferSize)
        val bufferInfo = MediaCodec.BufferInfo()

        for (segment in segments) {
            if (!segment.exists()) continue

            val extractor = MediaExtractor()
            try {
                extractor.setDataSource(segment.absolutePath)

                var segmentVideoTrack = -1
                var segmentAudioTrack = -1

                for (i in 0 until extractor.trackCount) {
                    val format = extractor.getTrackFormat(i)
                    val mime = format.getString(MediaFormat.KEY_MIME) ?: continue

                    if (mime.startsWith("video/")) {
                        segmentVideoTrack = i
                    } else if (mime.startsWith("audio/")) {
                        segmentAudioTrack = i
                    }
                }

                var maxPresentationTimeUs = 0L

                // Write video track
                if (segmentVideoTrack >= 0 && videoTrackIndex >= 0) {
                    extractor.selectTrack(segmentVideoTrack)

                    while (true) {
                        buffer.clear()
                        val sampleSize = extractor.readSampleData(buffer, 0)
                        if (sampleSize < 0) break

                        val presentationTimeUs = extractor.sampleTime + currentTimeOffsetUs
                        maxPresentationTimeUs = maxOf(maxPresentationTimeUs, presentationTimeUs)

                        bufferInfo.offset = 0
                        bufferInfo.size = sampleSize
                        bufferInfo.presentationTimeUs = presentationTimeUs
                        bufferInfo.flags = extractor.sampleFlags

                        muxer.writeSampleData(videoTrackIndex, buffer, bufferInfo)
                        extractor.advance()
                    }

                    extractor.unselectTrack(segmentVideoTrack)
                }

                // Write audio track
                if (segmentAudioTrack >= 0 && audioTrackIndex >= 0) {
                    extractor.selectTrack(segmentAudioTrack)

                    while (true) {
                        buffer.clear()
                        val sampleSize = extractor.readSampleData(buffer, 0)
                        if (sampleSize < 0) break

                        val presentationTimeUs = extractor.sampleTime + currentTimeOffsetUs
                        maxPresentationTimeUs = maxOf(maxPresentationTimeUs, presentationTimeUs)

                        bufferInfo.offset = 0
                        bufferInfo.size = sampleSize
                        bufferInfo.presentationTimeUs = presentationTimeUs
                        bufferInfo.flags = extractor.sampleFlags

                        muxer.writeSampleData(audioTrackIndex, buffer, bufferInfo)
                        extractor.advance()
                    }

                    extractor.unselectTrack(segmentAudioTrack)
                }

                currentTimeOffsetUs = maxPresentationTimeUs + 33333 // Add ~1 frame duration (30fps)

            } finally {
                extractor.release()
            }
        }

    } finally {
        try {
            muxer?.stop()
            muxer?.release()
        } catch (e: Exception) {
            // Ignore errors during cleanup
        }
    }

    outputFile
}
