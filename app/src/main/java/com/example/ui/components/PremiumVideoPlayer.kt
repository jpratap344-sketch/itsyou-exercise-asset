package com.example.ui.components

import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.PlayerView
import com.example.data.model.Exercise
import com.example.ui.screens.DynamicExerciseAnimationSimulation

/**
 * A beautiful, fully transparent vector-based ITS YOU brand logo drawn on a Canvas.
 * It features a stylized "IY" crest inside a sleek glowing athletic track circle.
 */
@Composable
fun ItsYouLogo(
    modifier: Modifier = Modifier,
    glowColor: Color = Color(0xFF8A0303),
    accentColor: Color = Color(0xFFFF5400)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val minDim = minOf(w, h)
        val strokeWidth = minDim * 0.08f

        // Draw outer athletic circular rim with gaps
        drawArc(
            color = glowColor,
            startAngle = 45f,
            sweepAngle = 270f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )

        // Draw inner stylized athletic letter 'Y'
        val pathY = Path().apply {
            moveTo(w * 0.35f, h * 0.3f)
            lineTo(w * 0.5f, h * 0.48f)
            lineTo(w * 0.65f, h * 0.3f)
        }
        drawPath(
            path = pathY,
            color = accentColor,
            style = Stroke(width = strokeWidth * 1.2f, cap = androidx.compose.ui.graphics.StrokeCap.Round)
        )

        // Draw inner stylized athletic letter 'I' vertical bar
        val pathI = Path().apply {
            moveTo(w * 0.5f, h * 0.48f)
            lineTo(w * 0.5f, h * 0.72f)
        }
        drawPath(
            path = pathI,
            color = accentColor,
            style = Stroke(width = strokeWidth * 1.2f, cap = androidx.compose.ui.graphics.StrokeCap.Round)
        )

        // Draw a premium center core indicator
        drawCircle(
            color = accentColor,
            radius = strokeWidth * 0.6f,
            center = Offset(w * 0.5f, h * 0.38f)
        )
    }
}

/**
 * A beautiful 2-second premium intro video overlay for ITS YOU branding.
 */
@Composable
fun PremiumIntroOverlay(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    var startAnim by remember { mutableStateOf(false) }

    val alphaAnim by animateFloatAsState(
        targetValue = if (!startAnim) 0f else 1f,
        animationSpec = keyframes {
            durationMillis = 2000
            0f at 0 with LinearOutSlowInEasing
            1f at 400 with FastOutSlowInEasing
            1f at 1500 with LinearEasing
            0f at 2000 with FastOutLinearInEasing
        },
        label = "IntroAlpha"
    )

    val scaleAnim by animateFloatAsState(
        targetValue = if (!startAnim) 0.85f else 1.05f,
        animationSpec = tween(durationMillis = 2000, easing = LinearEasing),
        label = "IntroScale"
    )

    LaunchedEffect(Unit) {
        startAnim = true
        kotlinx.coroutines.delay(2000)
        onFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .alpha(alphaAnim)
                .scale(scaleAnim)
        ) {
            ItsYouLogo(
                modifier = Modifier
                    .size(90.dp)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "IT'S YOU",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 6.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "FITNESS EVOLUTION",
                color = Color(0xFFFF5400),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Premium video player with offline caching, auto-play, looping, error fallback, custom controls, and watermark.
 */
@OptIn(UnstableApi::class)
@Composable
fun PremiumVideoPlayer(
    videoUrl: String?,
    exercise: Exercise,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    var showIntro by remember(videoUrl) { mutableStateOf(!videoUrl.isNullOrEmpty()) }
    var isVideoPlaying by remember(isPlaying) { mutableStateOf(isPlaying) }
    var isFullScreen by remember { mutableStateOf(false) }

    val content = @Composable {
        PremiumVideoPlayerContent(
            videoUrl = videoUrl,
            exercise = exercise,
            isPlaying = isVideoPlaying,
            onPlayingChanged = { isVideoPlaying = it },
            isFullScreen = isFullScreen,
            onFullScreenToggle = { isFullScreen = !isFullScreen },
            modifier = Modifier.fillMaxSize()
        )
    }

    Box(modifier = modifier) {
        if (showIntro) {
            PremiumIntroOverlay(
                onFinished = { showIntro = false },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            if (isFullScreen) {
                Popup(
                    onDismissRequest = { isFullScreen = false },
                    properties = PopupProperties(
                        focusable = true,
                        excludeFromSystemGesture = true
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                    ) {
                        content()
                    }
                }
            } else {
                content()
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun PremiumVideoPlayerContent(
    videoUrl: String?,
    exercise: Exercise,
    isPlaying: Boolean,
    onPlayingChanged: (Boolean) -> Unit,
    isFullScreen: Boolean,
    onFullScreenToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var showControls by remember { mutableStateOf(true) }

    // Auto-hide controls after 3.5 seconds
    LaunchedEffect(showControls, isPlaying) {
        if (showControls && isPlaying) {
            kotlinx.coroutines.delay(3500)
            showControls = false
        }
    }

    val exoPlayer = remember(videoUrl) {
        if (!videoUrl.isNullOrEmpty()) {
            try {
                ExoPlayer.Builder(context)
                    .setMediaSourceFactory(
                        DefaultMediaSourceFactory(context)
                            .setDataSourceFactory(VideoCacheManager.getCacheDataSourceFactory(context))
                    )
                    .build().apply {
                        repeatMode = Player.REPEAT_MODE_ALL
                        volume = 0f // Muted by default as requested
                        playWhenReady = isPlaying
                    }
            } catch (e: Exception) {
                hasError = true
                null
            }
        } else {
            null
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer?.release()
        }
    }

    LaunchedEffect(exoPlayer, videoUrl, isPlaying) {
        exoPlayer?.let { player ->
            player.playWhenReady = isPlaying
            if (!videoUrl.isNullOrEmpty() && !hasError) {
                try {
                    val mediaItem = MediaItem.fromUri(videoUrl)
                    player.setMediaItem(mediaItem)
                    player.prepare()
                } catch (e: Exception) {
                    hasError = true
                }
            }
        }
    }

    LaunchedEffect(exoPlayer) {
        exoPlayer?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                isLoading = (playbackState == Player.STATE_BUFFERING)
            }

            override fun onPlayerError(error: PlaybackException) {
                hasError = true
                isLoading = false
            }
        })
    }

    Box(
        modifier = modifier
            .background(Color.Black)
            .clickable { showControls = !showControls }
    ) {
        if (videoUrl.isNullOrEmpty() || hasError) {
            // Elegant, polished local 60 FPS skeletal motion simulation placeholder
            DynamicExerciseAnimationSimulation(
                exerciseName = exercise.name,
                category = exercise.category,
                isPlaying = isPlaying,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        useController = false // Custom Controls built below
                        player = exoPlayer
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Loading / Buffering Spinner
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color(0xFFFF5400),
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center)
                )
            }
        }

        // Custom Overlay Watermark at bottom-right corner (10-15% transparent opacity)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (showControls) 60.dp else 16.dp, end = 16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            ItsYouLogo(
                modifier = Modifier
                    .size(45.dp)
                    .alpha(0.12f)
            )
        }

        // Custom Controls overlay
        if (showControls) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.25f))
            ) {
                // Play / Pause central button
                IconButton(
                    onClick = { onPlayingChanged(!isPlaying) },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .size(56.dp)
                ) {
                    if (isPlaying) {
                        // Custom Pause Bars drawn safely on Canvas
                        Canvas(modifier = Modifier.size(24.dp)) {
                            val barWidth = size.width * 0.18f
                            drawRect(
                                color = Color.White,
                                topLeft = Offset(size.width * 0.28f, size.height * 0.2f),
                                size = Size(barWidth, size.height * 0.6f)
                            )
                            drawRect(
                                color = Color.White,
                                topLeft = Offset(size.width * 0.54f, size.height * 0.2f),
                                size = Size(barWidth, size.height * 0.6f)
                            )
                        }
                    } else {
                        // Custom Play Triangle drawn safely on Canvas
                        Canvas(modifier = Modifier.size(24.dp)) {
                            val path = Path().apply {
                                moveTo(size.width * 0.32f, size.height * 0.2f)
                                lineTo(size.width * 0.82f, size.height * 0.5f)
                                lineTo(size.width * 0.32f, size.height * 0.8f)
                                close()
                            }
                            drawPath(path, color = Color.White)
                        }
                    }
                }

                // Fullscreen corners toggle button at bottom-right corner
                IconButton(
                    onClick = onFullScreenToggle,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .size(42.dp)
                ) {
                    // Corner framing marks drawn on Canvas
                    Canvas(modifier = Modifier.size(20.dp)) {
                        val len = size.width * 0.3f
                        val strokeWidth = size.width * 0.1f
                        if (isFullScreen) {
                            // Draws minimize/close overlay icon
                            drawLine(Color.White, Offset(0f, size.height), Offset(size.width, 0f), strokeWidth)
                            drawLine(Color.White, Offset(0f, 0f), Offset(size.width, size.height), strokeWidth)
                        } else {
                            // Top Left Corner
                            drawLine(Color.White, Offset(0f, 0f), Offset(len, 0f), strokeWidth)
                            drawLine(Color.White, Offset(0f, 0f), Offset(0f, len), strokeWidth)
                            // Top Right Corner
                            drawLine(Color.White, Offset(size.width, 0f), Offset(size.width - len, 0f), strokeWidth)
                            drawLine(Color.White, Offset(size.width, 0f), Offset(size.width, len), strokeWidth)
                            // Bottom Left Corner
                            drawLine(Color.White, Offset(0f, size.height), Offset(len, size.height), strokeWidth)
                            drawLine(Color.White, Offset(0f, size.height), Offset(0f, size.height - len), strokeWidth)
                            // Bottom Right Corner
                            drawLine(Color.White, Offset(size.width, size.height), Offset(size.width - len, size.height), strokeWidth)
                            drawLine(Color.White, Offset(size.width, size.height), Offset(size.width, size.height - len), strokeWidth)
                        }
                    }
                }
            }
        }
    }
}
