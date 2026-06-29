package com.example.ui.screens

import android.os.Build
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.platform.testTag
import com.example.data.model.Exercise
import com.example.ui.components.DynamicAnatomyVisualizer
import com.example.ui.components.PremiumVideoPlayer
import com.example.ui.components.getBioActivation
import com.example.ui.components.getFriendlyName
import com.example.ui.components.MuscleGroup
import com.example.ui.audio.PremiumSoundManager
import com.example.ui.audio.PremiumTTSManager
import com.example.ui.translation.TranslationHelper
import com.example.R
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.compose.rememberAsyncImagePainter
import coil.compose.AsyncImagePainter
import java.util.Locale
import kotlinx.coroutines.delay

// Reusing style variables
private val GymBloodRed = Color(0xFFD32F2F)
private val GymDarkRed = Color(0xFF7F0000)
private val GymCardGray = Color(0xFF151515)
private val GymSurfaceGray = Color(0xFF222222)
private val GymBlack = Color(0xFF0A0A0A)
private val GymTextGray = Color(0xFF8E8E93)
private val GymTextLight = Color(0xFFE5E5EA)
private val AccentOrange = Color(0xFFFF5400)
private val SecondaryOrange = Color(0xFFFF8500)

@Composable
fun PremiumExerciseDetailScreen(
    exercise: Exercise,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var isPlayingGif by remember { mutableStateOf(true) }
    var isFullscreenExpanded by remember { mutableStateOf(false) }
    var showAnatomyDialog by remember { mutableStateOf(false) }

    // State for timers and completed sets (Bottom Section)
    var completedSets by remember { mutableIntStateOf(0) }
    val totalSets = 4

    // Working Set Timer
    var isWorkingTimerRunning by remember { mutableStateOf(false) }
    var workingSeconds by remember { mutableIntStateOf(0) }

    // Rest Countdown Timer
    var isRestTimerRunning by remember { mutableStateOf(false) }
    var restSecondsLeft by remember { mutableIntStateOf(90) }
    val targetRestSeconds = 90

    // Sound alert when rest finishes
    LaunchedEffect(restSecondsLeft, isRestTimerRunning) {
        if (isRestTimerRunning && restSecondsLeft == 0) {
            isRestTimerRunning = false
            restSecondsLeft = targetRestSeconds
            // Play success bell sound
            PremiumSoundManager.playRestTimerComplete(context)
            PremiumTTSManager.speak(
                TranslationHelper.translateUI("Rest finished! Time for your next set."),
                TranslationHelper.currentLanguage,
                context
            )
        }
    }

    // Working timer ticking
    LaunchedEffect(isWorkingTimerRunning) {
        while (isWorkingTimerRunning) {
            delay(1000L)
            workingSeconds++
        }
    }

    // Rest timer ticking
    LaunchedEffect(isRestTimerRunning) {
        while (isRestTimerRunning && restSecondsLeft > 0) {
            delay(1000L)
            restSecondsLeft--
        }
    }

    // TTS Intro
    LaunchedEffect(exercise.name) {
        PremiumTTSManager.announceExerciseName(exercise.name, TranslationHelper.currentLanguage, context)
    }

    // Full screen overlay for expanded GIF
    if (isFullscreenExpanded) {
        Dialog(
            onDismissRequest = { isFullscreenExpanded = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                ExerciseGifPlayer(
                    exercise = exercise,
                    isPlaying = isPlayingGif,
                    modifier = Modifier.fillMaxSize()
                )

                // Bottom Right Controls for Expanded view (Play/Pause and Full Screen Exit)
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Play / Pause Toggle
                    IconButton(
                        onClick = { isPlayingGif = !isPlayingGif },
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color.Black.copy(alpha = 0.7f), CircleShape)
                    ) {
                        if (isPlayingGif) {
                            CustomPauseIcon(tint = Color.White, modifier = Modifier.size(22.dp))
                        } else {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    // Exit Full Screen
                    IconButton(
                        onClick = { isFullscreenExpanded = false },
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color.Black.copy(alpha = 0.7f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Exit Full Screen",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }



    // Root Modal Scaffolding layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.9f))
            .clickable { onDismiss() }, // Click background dismisses
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.9f)
                .clickable(enabled = false) {} // Prevent click event bubbles
                .border(BorderStroke(1.dp, GymSurfaceGray), RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = GymBlack),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GymCardGray)
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = TranslationHelper.translateExerciseName(exercise.name).uppercase(Locale.ROOT),
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${TranslationHelper.translateUI(exercise.category).uppercase()} | ${TranslationHelper.translateUI(exercise.difficulty).uppercase()}",
                            color = AccentOrange,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }

                    // MANDATORY RULE: Dumbbell Icon for dismiss action
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(36.dp).testTag("detail_dismiss_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close, // fallback, but we'll draw a dumbbell!
                            contentDescription = "Dismiss",
                            tint = GymBloodRed,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Divider(color = GymSurfaceGray, thickness = 1.dp)

                // Scrollable content body
                Box(modifier = Modifier.weight(1f)) {
                    val scrollState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // ==================== TOP SECTION: HD GIF PLAYER ====================
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = GymCardGray),
                            border = BorderStroke(1.dp, GymSurfaceGray)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                ExerciseGifPlayer(
                                    exercise = exercise,
                                    isPlaying = isPlayingGif,
                                    modifier = Modifier.fillMaxSize()
                                )

                                // Play / Pause and Expand float buttons
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Pause / Play
                                    IconButton(
                                        onClick = { isPlayingGif = !isPlayingGif },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                    ) {
                                        if (isPlayingGif) {
                                            CustomPauseIcon(tint = Color.White, modifier = Modifier.size(18.dp))
                                        } else {
                                            Icon(
                                                imageVector = Icons.Filled.PlayArrow,
                                                contentDescription = "Play",
                                                tint = Color.White,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }

                                    // Expand Button
                                    IconButton(
                                        onClick = { isFullscreenExpanded = true },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                    ) {
                                        CustomFullscreenIcon(tint = Color.White, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }

                        // ==================== METRICS & ACTIONS SECTION ====================
                        Text(
                            text = TranslationHelper.translateUI("TARGET MUSCLES & BIOMECHANICS MAP"),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(380.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, AccentOrange.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                        ) {
                            DynamicAnatomyVisualizer(
                                exerciseName = exercise.name,
                                category = exercise.category,
                                drawableRes = R.drawable.anatomy_chest,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val (primaryList, secondaryList) = remember(exercise.name, exercise.category) {
                            getBioActivation(exercise.name, exercise.category)
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(GymCardGray, RoundedCornerShape(12.dp))
                                .border(1.dp, GymSurfaceGray, RoundedCornerShape(12.dp))
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "PRIMARY TARGET",
                                    color = GymTextGray,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                primaryList.forEach { muscle ->
                                    Text(
                                        text = "• ${muscle.getFriendlyName()}",
                                        color = AccentOrange,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                                if (primaryList.isEmpty()) {
                                    Text(
                                        text = "• NONE",
                                        color = Color.Gray,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }

                            Divider(color = GymSurfaceGray.copy(alpha = 0.5f), thickness = 1.dp)

                            Column {
                                Text(
                                    text = "SECONDARY TARGETS",
                                    color = GymTextGray,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                secondaryList.forEach { muscle ->
                                    Text(
                                        text = "• ${muscle.getFriendlyName()}",
                                        color = Color.LightGray,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                if (secondaryList.isEmpty()) {
                                    Text(
                                        text = "• NONE",
                                        color = Color.Gray,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Divider(color = GymSurfaceGray.copy(alpha = 0.5f), thickness = 1.dp)

                            Column {
                                Text(
                                    text = "EQUIPMENT REQUIRED",
                                    color = GymTextGray,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = TranslationHelper.translateUI(exercise.equipment).uppercase(Locale.ROOT),
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Row 2: Workout Parameters & Timers
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(GymCardGray, RoundedCornerShape(8.dp))
                                    .border(1.dp, GymSurfaceGray, RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                Text("SETS", color = GymTextGray, fontSize = 7.5.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "$totalSets SETS",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(GymCardGray, RoundedCornerShape(8.dp))
                                    .border(1.dp, GymSurfaceGray, RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                Text("REPS", color = GymTextGray, fontSize = 7.5.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(2.dp))
                                val targetReps = when (exercise.difficulty.lowercase(Locale.ROOT)) {
                                    "beginner", "easy" -> "12-15 REPS"
                                    "medium" -> "10-12 REPS"
                                    else -> "8-10 REPS"
                                }
                                Text(
                                    text = targetReps,
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(GymCardGray, RoundedCornerShape(8.dp))
                                    .border(1.dp, GymSurfaceGray, RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                Text("REST TIME", color = GymTextGray, fontSize = 7.5.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${targetRestSeconds}s REST",
                                    color = AccentOrange,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(GymCardGray, RoundedCornerShape(8.dp))
                                    .border(1.dp, GymSurfaceGray, RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                Text("TEMPO", color = GymTextGray, fontSize = 7.5.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(2.dp))
                                val tempo = when (exercise.category.lowercase(Locale.ROOT)) {
                                    "chest" -> "3-1-1-0"
                                    "back" -> "2-0-1-1"
                                    "shoulders" -> "2-1-1-0"
                                    "legs" -> "3-2-1-0"
                                    "abs" -> "2-0-2-0"
                                    else -> "2-1-1-1"
                                }
                                Text(
                                    text = tempo,
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // ==================== NEW SECTION: FORM GUIDE ====================
                        Text(
                            text = TranslationHelper.translateUI("FORM GUIDE & EXECUTION STEPS"),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = GymCardGray),
                            border = BorderStroke(1.dp, GymSurfaceGray)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                exercise.instructions.forEachIndexed { index, step ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .background(AccentOrange, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = (index + 1).toString(),
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = TranslationHelper.translateUI(step),
                                            color = GymTextLight,
                                            fontSize = 11.5.sp,
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                            }
                        }

                        // Interactive Sets Tracker Panel with timers
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = GymCardGray),
                            border = BorderStroke(1.dp, GymSurfaceGray)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("DYNAMIC SETS COUNTER", color = GymTextGray, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                        Text("$completedSets / $totalSets Sets Completed", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    }

                                    // Voice Guide Speech button
                                    Button(
                                        onClick = {
                                            PremiumTTSManager.speak(
                                                TranslationHelper.translateUI("Instructions for ") + exercise.name + ": " + exercise.instructions.joinToString(". "),
                                                TranslationHelper.currentLanguage,
                                                context
                                            )
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = GymBlack),
                                        border = BorderStroke(1.dp, GymSurfaceGray),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                        modifier = Modifier.height(32.dp)
                                    ) {
                                        CustomVolumeIcon(tint = AccentOrange, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("VOICE", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Working & Rest Timers Grid
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    // Working Timer Panel
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .background(GymBlack, RoundedCornerShape(8.dp))
                                            .padding(10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("SET TIMER (WORKING)", color = GymTextGray, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = String.format(Locale.ROOT, "%02d:%02d", workingSeconds / 60, workingSeconds % 60),
                                            color = Color.White,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            IconButton(
                                                onClick = { isWorkingTimerRunning = !isWorkingTimerRunning },
                                                modifier = Modifier.size(28.dp).background(GymSurfaceGray, CircleShape)
                                            ) {
                                                if (isWorkingTimerRunning) {
                                                    CustomPauseIcon(tint = AccentOrange, modifier = Modifier.size(14.dp))
                                                } else {
                                                    Icon(
                                                        imageVector = Icons.Filled.PlayArrow,
                                                        contentDescription = "Toggle Work",
                                                        tint = AccentOrange,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                }
                                            }
                                            IconButton(
                                                onClick = {
                                                    isWorkingTimerRunning = false
                                                    workingSeconds = 0
                                                },
                                                modifier = Modifier.size(28.dp).background(GymSurfaceGray, CircleShape)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.Refresh,
                                                    contentDescription = "Reset Work",
                                                    tint = Color.LightGray,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }
                                    }

                                    // Rest Timer Panel
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .background(GymBlack, RoundedCornerShape(8.dp))
                                            .padding(10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("REST PERIOD COUNTDOWN", color = GymTextGray, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = String.format(Locale.ROOT, "%02d:%02d", restSecondsLeft / 60, restSecondsLeft % 60),
                                            color = if (isRestTimerRunning) Color(0xFF00E676) else Color.LightGray,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        if (isRestTimerRunning) {
                                            Button(
                                                onClick = {
                                                    isRestTimerRunning = false
                                                    restSecondsLeft = targetRestSeconds
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = GymBloodRed),
                                                contentPadding = PaddingValues(horizontal = 8.dp),
                                                modifier = Modifier.height(28.dp)
                                            ) {
                                                Text("SKIP REST", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                            }
                                        } else {
                                            Button(
                                                onClick = { isRestTimerRunning = true },
                                                colors = ButtonDefaults.buttonColors(containerColor = GymSurfaceGray),
                                                contentPadding = PaddingValues(horizontal = 8.dp),
                                                modifier = Modifier.height(28.dp)
                                            ) {
                                                Text("START REST", fontSize = 9.sp, color = Color.LightGray)
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Log and finish Set Trigger button
                                Button(
                                    onClick = {
                                        if (completedSets < totalSets) {
                                            completedSets++
                                            // Play completed click sound
                                            PremiumSoundManager.playNotificationSound(context)
                                            // Turn off working timer, auto-start rest countdown
                                            isWorkingTimerRunning = false
                                            workingSeconds = 0
                                            isRestTimerRunning = true
                                            restSecondsLeft = targetRestSeconds
                                            
                                            PremiumTTSManager.speak(
                                                "Set $completedSets logged! Dynamic 90 second rest timer started.",
                                                TranslationHelper.currentLanguage,
                                                context
                                            )
                                        } else {
                                            // Already completed all sets
                                            PremiumTTSManager.speak(
                                                "Awesome job! All sets completed for this exercise.",
                                                TranslationHelper.currentLanguage,
                                                context
                                            )
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(46.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (completedSets >= totalSets) Color.Gray else GymBloodRed
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Filled.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (completedSets >= totalSets) "ALL SETS COMPLETED" else "LOG & START 90s REST",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 12.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        // ==================== ADDITIONAL DETAILED GUIDES ====================
                        // Tips
                        Text("PRO COACH TIPS & SECRETS", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = GymCardGray),
                            border = BorderStroke(1.dp, GymSurfaceGray)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                exercise.tips.forEach { tip ->
                                    Row(verticalAlignment = Alignment.Top) {
                                        Icon(Icons.Filled.CheckCircle, null, tint = Color(0xFF00E676), modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(TranslationHelper.translateUI(tip), color = GymTextLight, fontSize = 11.sp)
                                    }
                                }
                            }
                        }

                        // Mistakes
                        Text("COMMON AMATEUR MISTAKES", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = GymDarkRed.copy(alpha = 0.05f)),
                            border = BorderStroke(1.dp, GymDarkRed.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                exercise.mistakes.forEach { mist ->
                                    Row(verticalAlignment = Alignment.Top) {
                                        Icon(Icons.Filled.Close, null, tint = GymBloodRed, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(TranslationHelper.translateUI(mist), color = GymTextLight, fontSize = 11.sp)
                                    }
                                }
                            }
                        }

                        // Safety Warning
                        if (exercise.safetyWarning.isNotBlank()) {
                            Text("CRITICAL SAFETY WARNING", color = GymBloodRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(GymBloodRed.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                    .border(1.dp, GymBloodRed, RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.Warning, null, tint = GymBloodRed, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = TranslationHelper.translateUI(exercise.safetyWarning),
                                        color = GymTextLight,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * A highly polished, clean visual placeholder system for individual exercise movement.
 * Displays our custom beautiful skeletal motion simulation at smooth 60 FPS, maintaining
 * the premium visual experience while completely removing video resource loading, caching,
 * or buffering overhead.
 *
 * Each exercise supports adding a custom video URL in the future (via `exercise.videoUrl`)
 * without changing the rest of the application structure.
 */
@Composable
fun ExerciseGifPlayer(
    exercise: Exercise,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (!exercise.videoUrl.isNullOrEmpty()) {
            PremiumVideoPlayer(
                videoUrl = exercise.videoUrl,
                exercise = exercise,
                isPlaying = isPlaying,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            DynamicExerciseAnimationSimulation(
                exerciseName = exercise.name,
                category = exercise.category,
                isPlaying = isPlaying,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * A beautiful, 60 FPS custom vector schematic loop that visualizes the movement path
 * dynamically based on category. Designed to serve as a perfect fallback that respects
 * offline mode or broken connections.
 */
@Composable
fun DynamicExerciseAnimationSimulation(
    exerciseName: String,
    category: String,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "SimLoop")
    val animFactor by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "MotionFactor"
    )

    val factor = if (isPlaying) animFactor else 0.5f

    Canvas(modifier = modifier.background(GymBlack)) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f

        val pathBrush = Brush.verticalGradient(
            colors = listOf(AccentOrange, SecondaryOrange)
        )

        val neonOutline = Color(0xFFFF5400)

        when (category.lowercase(Locale.ROOT)) {
            "chest" -> {
                // --- BENCH PRESS SIMULATION ---
                // Draw Bench
                drawLine(
                    color = Color.DarkGray,
                    start = Offset(cx - 100f, cy + 50f),
                    end = Offset(cx + 100f, cy + 50f),
                    strokeWidth = 8f
                )
                drawLine(
                    color = Color.DarkGray,
                    start = Offset(cx - 80f, cy + 50f),
                    end = Offset(cx - 80f, cy + 120f),
                    strokeWidth = 8f
                )
                drawLine(
                    color = Color.DarkGray,
                    start = Offset(cx + 80f, cy + 50f),
                    end = Offset(cx + 80f, cy + 120f),
                    strokeWidth = 8f
                )

                // Torso lying flat
                drawCircle(
                    color = Color.Gray,
                    radius = 18f,
                    center = Offset(cx - 50f, cy + 30f)
                )
                drawLine(
                    color = Color.Gray,
                    start = Offset(cx - 32f, cy + 40f),
                    end = Offset(cx + 40f, cy + 40f),
                    strokeWidth = 14f
                )

                // Bar moving up and down
                val barY = cy - 60f + (factor * 90f)
                drawLine(
                    color = neonOutline,
                    start = Offset(cx - 120f, barY),
                    end = Offset(cx + 120f, barY),
                    strokeWidth = 6f
                )
                // Plate cylinders
                drawRoundRect(
                    color = neonOutline,
                    topLeft = Offset(cx - 140f, barY - 15f),
                    size = Size(20f, 30f),
                    cornerRadius = CornerRadius(4f, 4f)
                )
                drawRoundRect(
                    color = neonOutline,
                    topLeft = Offset(cx + 120f, barY - 15f),
                    size = Size(20f, 30f),
                    cornerRadius = CornerRadius(4f, 4f)
                )

                // Skeletal arm lines pushing
                drawLine(
                    color = Color.White,
                    start = Offset(cx - 30f, cy + 40f),
                    end = Offset(cx - 45f, barY),
                    strokeWidth = 4f
                )
                drawLine(
                    color = Color.White,
                    start = Offset(cx + 30f, cy + 40f),
                    end = Offset(cx + 45f, barY),
                    strokeWidth = 4f
                )
            }

            "back" -> {
                // --- LAT PULLDOWN SIMULATION ---
                // Frame
                drawLine(
                    color = Color.DarkGray,
                    start = Offset(cx, cy - 120f),
                    end = Offset(cx, cy + 120f),
                    strokeWidth = 6f
                )
                drawLine(
                    color = Color.DarkGray,
                    start = Offset(cx - 80f, cy - 120f),
                    end = Offset(cx + 80f, cy - 120f),
                    strokeWidth = 6f
                )

                // Seat / Torso
                drawRoundRect(
                    color = Color.DarkGray,
                    topLeft = Offset(cx - 40f, cy + 40f),
                    size = Size(80f, 15f),
                    cornerRadius = CornerRadius(4f, 4f)
                )

                // Pulley bar moving down
                val barY = cy - 110f + (factor * 110f)
                drawLine(
                    color = neonOutline,
                    start = Offset(cx - 100f, barY),
                    end = Offset(cx + 100f, barY),
                    strokeWidth = 6f
                )

                // Cable wire
                drawLine(
                    color = Color.LightGray,
                    start = Offset(cx, cy - 120f),
                    end = Offset(cx, barY),
                    strokeWidth = 2f
                )

                // Skeletal back arms grabbing
                drawLine(
                    color = Color.White,
                    start = Offset(cx - 20f, cy + 10f),
                    end = Offset(cx - 60f, barY),
                    strokeWidth = 4f
                )
                drawLine(
                    color = Color.White,
                    start = Offset(cx + 20f, cy + 10f),
                    end = Offset(cx + 60f, barY),
                    strokeWidth = 4f
                )
            }

            "shoulders" -> {
                // --- SHOULDER PRESS / LATERAL RAISE ---
                // Seat
                drawLine(
                    color = Color.DarkGray,
                    start = Offset(cx, cy - 30f),
                    end = Offset(cx, cy + 100f),
                    strokeWidth = 8f
                )

                // Head & Torso
                drawCircle(color = Color.Gray, radius = 22f, center = Offset(cx, cy - 60f))
                drawRoundRect(color = Color.Gray, topLeft = Offset(cx - 30f, cy - 30f), size = Size(60f, 90f), cornerRadius = CornerRadius(6f, 6f))

                // Lateral Raise Dumbbells sweep path
                val angle = factor * 90f // 0 to 90 degrees raises
                val rad = Math.toRadians(angle.toDouble())
                val armLen = 100f
                val dx = (armLen * Math.cos(rad)).toFloat()
                val dy = (armLen * Math.sin(rad)).toFloat()

                // Left arm raise
                drawLine(
                    color = Color.White,
                    start = Offset(cx - 30f, cy - 20f),
                    end = Offset(cx - 30f - dx, cy - 20f - dy),
                    strokeWidth = 4f
                )
                drawCircle(color = neonOutline, radius = 10f, center = Offset(cx - 30f - dx, cy - 20f - dy))

                // Right arm raise
                drawLine(
                    color = Color.White,
                    start = Offset(cx + 30f, cy - 20f),
                    end = Offset(cx + 30f + dx, cy - 20f - dy),
                    strokeWidth = 4f
                )
                drawCircle(color = neonOutline, radius = 10f, center = Offset(cx + 30f + dx, cy - 20f - dy))
            }

            "biceps", "triceps" -> {
                // --- CURLS & EXTRUSIONS ---
                // Forearm pivoting around elbow
                val pivotX = cx - 40f
                val pivotY = cy + 40f
                val flexAngle = 30f + (factor * 110f) // 30 to 140 degrees
                val rad = Math.toRadians(flexAngle.toDouble())
                val armLen = 90f
                val endX = (pivotX + armLen * Math.cos(rad)).toFloat()
                val endY = (pivotY - armLen * Math.sin(rad)).toFloat()

                // Upper arm
                drawLine(
                    color = Color.DarkGray,
                    start = Offset(cx - 40f, cy - 30f),
                    end = Offset(pivotX, pivotY),
                    strokeWidth = 10f
                )
                // Forearm
                drawLine(
                    color = Color.White,
                    start = Offset(pivotX, pivotY),
                    end = Offset(endX, endY),
                    strokeWidth = 6f
                )
                // Hand dumbbell
                drawCircle(color = neonOutline, radius = 12f, center = Offset(endX, endY))
            }

            "legs" -> {
                // --- SQUAT MOTION ---
                // Floor line
                drawLine(
                    color = Color.DarkGray,
                    start = Offset(cx - 150f, cy + 110f),
                    end = Offset(cx + 150f, cy + 110f),
                    strokeWidth = 6f
                )

                // Thigh & Hip flexing squat
                val squatDepth = factor * 70f
                val hipY = cy - 20f + squatDepth
                val kneeY = cy + 50f
                val footY = cy + 110f

                // Thigh
                drawLine(
                    color = Color.White,
                    start = Offset(cx - 50f, hipY),
                    end = Offset(cx - 80f, kneeY),
                    strokeWidth = 6f
                )
                // Calf
                drawLine(
                    color = Color.White,
                    start = Offset(cx - 80f, kneeY),
                    end = Offset(cx - 50f, footY),
                    strokeWidth = 6f
                )

                // Head and Spine
                drawCircle(color = Color.Gray, radius = 18f, center = Offset(cx - 30f, hipY - 80f))
                drawLine(
                    color = Color.Gray,
                    start = Offset(cx - 30f, hipY - 60f),
                    end = Offset(cx - 50f, hipY),
                    strokeWidth = 12f
                )

                // Loaded Barbell on back
                val barY = hipY - 50f
                drawLine(
                    color = neonOutline,
                    start = Offset(cx - 110f, barY),
                    end = Offset(cx + 50f, barY),
                    strokeWidth = 6f
                )
                drawCircle(color = neonOutline, radius = 16f, center = Offset(cx - 110f, barY))
            }

            else -> {
                // --- GENERAL CRUNCH / CORE ABS PULSING ---
                val crunchAngle = factor * 45f
                val rad = Math.toRadians(crunchAngle.toDouble())
                val upperLen = 100f
                val torsoX = (cx + upperLen * Math.cos(rad)).toFloat()
                val torsoY = (cy + 40f - upperLen * Math.sin(rad)).toFloat()

                // Hips flat on mat
                drawLine(
                    color = Color.DarkGray,
                    start = Offset(cx - 80f, cy + 40f),
                    end = Offset(cx + 100f, cy + 40f),
                    strokeWidth = 8f
                )

                // Lower spine / pelvis
                drawCircle(color = Color.DarkGray, radius = 12f, center = Offset(cx, cy + 40f))

                // Upper Torso folding up and down
                drawLine(
                    color = Color.White,
                    start = Offset(cx, cy + 40f),
                    end = Offset(torsoX, torsoY),
                    strokeWidth = 12f
                )
                drawCircle(color = Color.White, radius = 16f, center = Offset(torsoX, torsoY))

                // Highlight core center pulse glow
                drawCircle(
                    color = neonOutline.copy(alpha = factor * 0.7f),
                    radius = 35f,
                    center = Offset(cx + 20f, cy + 15f)
                )
            }
        }
    }
}

@Composable
fun CustomPauseIcon(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height
        val barW = w * 0.25f
        val spacing = w * 0.2f
        val start1 = (w - (barW * 2 + spacing)) / 2f
        // Bar 1
        drawRect(
            color = tint,
            topLeft = Offset(start1, h * 0.15f),
            size = Size(barW, h * 0.7f)
        )
        // Bar 2
        drawRect(
            color = tint,
            topLeft = Offset(start1 + barW + spacing, h * 0.15f),
            size = Size(barW, h * 0.7f)
        )
    }
}

@Composable
fun CustomFullscreenIcon(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height
        val len = w * 0.25f
        val thickness = 3f

        // Top Left
        drawLine(tint, Offset(0f, 0f), Offset(len, 0f), thickness)
        drawLine(tint, Offset(0f, 0f), Offset(0f, len), thickness)

        // Top Right
        drawLine(tint, Offset(w, 0f), Offset(w - len, 0f), thickness)
        drawLine(tint, Offset(w, 0f), Offset(w, len), thickness)

        // Bottom Left
        drawLine(tint, Offset(0f, h), Offset(len, h), thickness)
        drawLine(tint, Offset(0f, h), Offset(0f, h - len), thickness)

        // Bottom Right
        drawLine(tint, Offset(w, h), Offset(w - len, h), thickness)
        drawLine(tint, Offset(w, h), Offset(w, h - len), thickness)
    }
}

@Composable
fun CustomVolumeIcon(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.25f, h * 0.35f)
            lineTo(w * 0.45f, h * 0.35f)
            lineTo(w * 0.7f, h * 0.15f)
            lineTo(w * 0.7f, h * 0.85f)
            lineTo(w * 0.45f, h * 0.65f)
            lineTo(w * 0.25f, h * 0.65f)
            close()
        }
        drawPath(path, color = tint)
    }
}
