package com.example.ui.components

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Exercise
import com.example.ui.audio.PremiumSoundManager
import com.example.ui.audio.PremiumTTSManager
import com.example.ui.translation.TranslationHelper
import com.example.R
import java.util.Locale
import kotlinx.coroutines.delay

/**
 * Solid, custom color variables representing the premium commercial Fitness style.
 * Strictly Black + Tangy Vibrant Orange scheme.
 */
val TangyOrangeGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFFFF5400), Color(0xFFFF8500))
)
val AccentOrange = Color(0xFFFF5400)
val DarkBackground = Color(0xFF000000)
val SurfaceDarkCard = Color(0xFF0B0B0B)
val BorderOrangeMuted = Color(0xFFFF5400).copy(alpha = 0.25f)
val GrayBorder = Color(0xFF1C1C1E)

/**
 * Data representation of exercise anatomy specification and custom illustration mapping.
 */
data class MuscleTargetDetails(
    val primary: String,
    val secondary: String,
    val drawableRes: Int
)

/**
 * Returns exact primary/secondary target muscles as specified in premium visual catalogs and sports science references.
 */
fun getExerciseMuscleDetails(exerciseName: String, category: String, primaryMuscles: List<String> = emptyList(), secondaryMuscles: List<String> = emptyList()): MuscleTargetDetails {
    val nameLower = exerciseName.lowercase(Locale.ROOT)
    val catLower = category.lowercase(Locale.ROOT)

    return when {
        // --- 1. CHEST ---
        nameLower.contains("incline bench") || nameLower.contains("incline press") || nameLower.contains("incline dumbbell press") || nameLower.contains("incline dumbbell fly") -> {
            MuscleTargetDetails(
                primary = "Upper Chest (Clavicular Head)",
                secondary = "Anterior Deltoids, Triceps Brachii",
                drawableRes = R.drawable.anatomy_chest
            )
        }
        nameLower.contains("bench press") && !nameLower.contains("incline") && !nameLower.contains("decline") && !nameLower.contains("close") -> {
            MuscleTargetDetails(
                primary = "Pectoralis Major, Front Delts, Triceps",
                secondary = "Serratus Anterior, Coracobrachialis",
                drawableRes = R.drawable.anatomy_chest
            )
        }
        nameLower.contains("cable crossover") || nameLower.contains("crossover") -> {
            MuscleTargetDetails(
                primary = "Chest (Sternal Head)",
                secondary = "Anterior Deltoids, Clavicular Head",
                drawableRes = R.drawable.anatomy_chest
            )
        }
        nameLower.contains("pec deck") || nameLower.contains("fly") || nameLower.contains("pullover") || nameLower.contains("push-up") || nameLower.contains("pushup") || nameLower.contains("chest press") || catLower.contains("chest") -> {
            MuscleTargetDetails(
                primary = "Pectoralis Major",
                secondary = "Anterior Deltoids, Triceps Brachii",
                drawableRes = R.drawable.anatomy_chest
            )
        }

        // --- 2. BACK ---
        nameLower.contains("shrug") -> {
            MuscleTargetDetails(
                primary = "Upper Trapezius",
                secondary = "Levator Scapulae, Rhomboids",
                drawableRes = R.drawable.anatomy_back_traps
            )
        }
        nameLower.contains("single arm lat pulldown") || nameLower.contains("single-arm lat pulldown") -> {
            MuscleTargetDetails(
                primary = "Latissimus Dorsi",
                secondary = "Teres Major, Biceps Brachii",
                drawableRes = R.drawable.anatomy_back_lats
            )
        }
        nameLower.contains("lat pulldown") || nameLower.contains("pull up") || nameLower.contains("pull-up") || nameLower.contains("pullup") || nameLower.contains("chin-up") || nameLower.contains("chinup") || nameLower.contains("scapular pull") || nameLower.contains("straight-arm cable pulldown") -> {
            MuscleTargetDetails(
                primary = "Latissimus Dorsi (Lats)",
                secondary = "Biceps Brachii, Teres Major, Lower Trapezius",
                drawableRes = R.drawable.anatomy_back_lats
            )
        }
        nameLower.contains("face pull") -> {
            MuscleTargetDetails(
                primary = "Rear Deltoids",
                secondary = "Trapezius, Rhomboids",
                drawableRes = R.drawable.anatomy_shoulders
            )
        }
        nameLower.contains("barbell row") -> {
            MuscleTargetDetails(
                primary = "Lats, Rhomboids",
                secondary = "Rear Delts, Traps",
                drawableRes = R.drawable.anatomy_back
            )
        }
        nameLower.contains("seated cable row") || nameLower.contains("cable row") || nameLower.contains("t-bar row") || nameLower.contains("dumbbell row") || nameLower.contains("meadows row") || nameLower.contains("row") -> {
            MuscleTargetDetails(
                primary = "Mid Back (Rhomboids, Middle Trapezius)",
                secondary = "Latissimus Dorsi, Brachioradialis",
                drawableRes = R.drawable.anatomy_back
            )
        }
        nameLower.contains("deadlift") || nameLower.contains("rack pull") || nameLower.contains("hyper-extension") || nameLower.contains("erector") || catLower.contains("back") -> {
            MuscleTargetDetails(
                primary = "Latissimus Dorsi, Erector Spinae, Rhomboids",
                secondary = "Gluteus Maximus, Hamstrings, Trapezius",
                drawableRes = R.drawable.anatomy_back
            )
        }

        // --- 3. SHOULDERS ---
        nameLower.contains("lateral raise") || nameLower.contains("side delt") || nameLower.contains("y-raise") -> {
            MuscleTargetDetails(
                primary = "Side Deltoids (Lateral Delts)",
                secondary = "Anterior Deltoids, Trapezius",
                drawableRes = R.drawable.anatomy_shoulders
            )
        }
        nameLower.contains("shoulder press") || nameLower.contains("overhead press") || nameLower.contains("arnold press") || nameLower.contains("front raise") || catLower.contains("shoulder") || nameLower.contains("upright row") -> {
            MuscleTargetDetails(
                primary = "Front and Side Delts",
                secondary = "Triceps Brachii, Upper Trapezius, Upper Chest",
                drawableRes = R.drawable.anatomy_shoulders
            )
        }

        // --- 4. BICEPS ---
        nameLower.contains("hammer curl") || nameLower.contains("zottman") -> {
            MuscleTargetDetails(
                primary = "Brachialis, Brachioradialis",
                secondary = "Biceps Brachii",
                drawableRes = R.drawable.anatomy_biceps
            )
        }
        nameLower.contains("bicep") || nameLower.contains("curl") || nameLower.contains("preacher") || catLower.contains("bicep") -> {
            MuscleTargetDetails(
                primary = "Biceps Brachii",
                secondary = "Brachialis, Brachioradialis, Forearm Flexors",
                drawableRes = R.drawable.anatomy_biceps
            )
        }

        // --- 5. TRICEPS ---
        nameLower.contains("tricep pushdown") || nameLower.contains("pushdown") || nameLower.contains("tricep") || nameLower.contains("skull crusher") || nameLower.contains("kickback") || nameLower.contains("extension") || nameLower.contains("close-grip bench") || nameLower.contains("bench dip") || nameLower.contains("dip") || catLower.contains("tricep") -> {
            MuscleTargetDetails(
                primary = "Triceps Brachii (All Heads)",
                secondary = "Anterior Deltoids, Pectoralis Major",
                drawableRes = R.drawable.anatomy_triceps
            )
        }

        // --- 6. LEGS ---
        nameLower.contains("calf raise") || nameLower.contains("calf press") -> {
            MuscleTargetDetails(
                primary = "Gastrocnemius, Soleus (Calves)",
                secondary = "Achilles Tendon Linkages",
                drawableRes = R.drawable.anatomy_legs_calves
            )
        }
        nameLower.contains("romanian deadlift") || nameLower.contains("rdl") || nameLower.contains("leg curl") || nameLower.contains("hamstring") -> {
            MuscleTargetDetails(
                primary = "Hamstrings, Gluteus Maximus",
                secondary = "Erector Spinae, Adductores",
                drawableRes = R.drawable.anatomy_legs_hamstrings
            )
        }
        nameLower.contains("leg extension") -> {
            MuscleTargetDetails(
                primary = "Quadriceps (Isolated)",
                secondary = "Patellar Tendon",
                drawableRes = R.drawable.anatomy_legs_quads
            )
        }
        nameLower.contains("squat") || nameLower.contains("leg press") || nameLower.contains("lunge") || nameLower.contains("hack squat") || nameLower.contains("goblet") || catLower.contains("leg") || catLower.contains("glute") || catLower.contains("thigh") -> {
            MuscleTargetDetails(
                primary = "Quadriceps, Glutes",
                secondary = "Hamstrings, Calves, Core Stabilizers",
                drawableRes = R.drawable.anatomy_legs_quads
            )
        }

        // --- 7. ABS ---
        nameLower.contains("crunch") -> {
            MuscleTargetDetails(
                primary = "Rectus Abdominis",
                secondary = "Obliques, Transverse Abdominis",
                drawableRes = R.drawable.anatomy_abs
            )
        }
        nameLower.contains("plank") || nameLower.contains("raise") || nameLower.contains("twist") || nameLower.contains("woodchopper") || nameLower.contains("wiper") || catLower.contains("abs") || catLower.contains("core") || catLower.contains("oblique") -> {
            MuscleTargetDetails(
                primary = "Core (Rectus Abdominis, Transverse Abdominis)",
                secondary = "Obliques, Hip Flexors, Gluteals",
                drawableRes = R.drawable.anatomy_abs
            )
        }

        else -> {
            MuscleTargetDetails(
                primary = if (primaryMuscles.isNotEmpty()) primaryMuscles.joinToString(", ") else category,
                secondary = if (secondaryMuscles.isNotEmpty()) secondaryMuscles.joinToString(", ") else "Synergists",
                drawableRes = R.drawable.anatomy_chest
            )
        }
    }
}

/**
 * Kept for reverse-compatibility with standard dynamic templates.
 */
fun getExerciseImageUrl(category: String, exerciseName: String): String {
    return "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?fm=webp&fit=crop&w=480&h=480&q=65"
}

@Composable
fun PremiumExercisePlayer(
    exercise: Exercise,
    currentStepSeconds: Int = 0,
    currentSet: Int = 1,
    totalSets: Int = 4,
    repsRange: String = "8-12 Reps",
    restTimeSeconds: Int = 90,
    onPrevExercise: (() -> Unit)? = null,
    onNextExercise: (() -> Unit)? = null,
    isWorkoutResting: Boolean = false,
    restSecondsLeft: Int = 0,
    isWorkoutPaused: Boolean = false,
    onTogglePause: (() -> Unit)? = null,
    
    // New parameters for advanced player engine
    setTimerSeconds: Int = 0,
    isSetTimerRunning: Boolean = false,
    targetSetDuration: Int = 30,
    onStartSetTimer: (() -> Unit)? = null,
    onPauseSetTimer: (() -> Unit)? = null,
    onResumeSetTimer: (() -> Unit)? = null,
    onResetSetTimer: (() -> Unit)? = null,
    onUpdateTargetSetDuration: ((Int) -> Unit)? = null,
    onCompleteSet: (() -> Unit)? = null,
    onSkipRest: (() -> Unit)? = null
) {
    val context = LocalContext.current

    // Interactive announcement parameters
    LaunchedEffect(exercise.name) {
        PremiumSoundManager.playStandardClick(context)
        PremiumTTSManager.announceExerciseName(
            exerciseName = exercise.name,
            lang = TranslationHelper.currentLanguage,
            context = context
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkBackground),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        
        // Query our precise sports science muscle target details & illustration mapping
        val contributions = remember(exercise.name, exercise.category) {
            getBioContributions(exercise.name, exercise.category)
        }
        val primaryText = remember(contributions) {
            contributions.filter { it.isPrimary }.joinToString(", ") { it.name }
        }
        val secondaryText = remember(contributions) {
            val list = contributions.filter { !it.isPrimary }
            if (list.isEmpty()) "None" else list.joinToString(", ") { it.name }
        }

        // ==================== 1. HEADER ====================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Exercise Name - full width, wraps to avoid any truncation or ellipsis
            Text(
                text = TranslationHelper.translateExerciseName(exercise.name).uppercase(Locale.ROOT),
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                style = TextStyle(
                    letterSpacing = 0.5.sp,
                    lineHeight = 28.sp
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Primary Target muscle line
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(AccentOrange, CircleShape)
                )
                Text(
                    text = "PRIMARY MUSCLE: " + primaryText.uppercase(Locale.ROOT),
                    color = AccentOrange,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            }

            // Secondary Target muscle line
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color.Gray, CircleShape)
                )
                Text(
                    text = "SECONDARY MUSCLES: " + secondaryText.uppercase(Locale.ROOT),
                    color = Color.Gray,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // ==================== 2. CIRCULAR COUNTDOWN GAUGES ====================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            val totalDuration = if (isWorkoutResting) 90f else targetSetDuration.toFloat()
            val elapsed = if (isWorkoutResting) restSecondsLeft.toFloat() else setTimerSeconds.toFloat()
            val progress = if (totalDuration > 0) (elapsed / totalDuration).coerceIn(0f, 1f) else 0f
            
            // Jetpack Compose smooth animations for 60 FPS
            val animatedProgress by animateFloatAsState(
                targetValue = progress,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                label = "SmoothCircleProgress"
            )

            Box(
                modifier = Modifier.size(190.dp),
                contentAlignment = Alignment.Center
            ) {
                // Background Track circle
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        color = Color.DarkGray.copy(alpha = 0.2f),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 10.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                    )
                }
                
                // Active circle
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        color = if (isWorkoutResting) Color(0xFF00E676) else Color(0xFFFF5400),
                        startAngle = -90f,
                        sweepAngle = animatedProgress * 360f,
                        useCenter = false,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 12.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                    )
                }

                // Inner content
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isWorkoutResting) "REST PERIOD" else "SET TIMER",
                        color = Color.LightGray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = String.format(Locale.ROOT, "%02d:%02d", elapsed.toInt() / 60, elapsed.toInt() % 60),
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isWorkoutResting) "Remaining" else "Target: ${targetSetDuration}s",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                    
                    // Small Reset Button inside set timer
                    if (!isWorkoutResting && setTimerSeconds > 0) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "RESET",
                            color = Color(0xFFFF5400),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .clickable { onResetSetTimer?.invoke() }
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // ==================== 3. EXERCISE SET DURATION CHOICE ====================
        if (!isWorkoutResting) {
            var showCustomDialog by remember { mutableStateOf(false) }
            var customSecondsText by remember { mutableStateOf("") }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "CHOOSE SET DURATION",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val durations = listOf(20, 30, 45, 60)
                    durations.forEach { duration ->
                        val isSelected = targetSetDuration == duration
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .background(
                                    color = if (isSelected) Color(0xFFFF5400) else SurfaceDarkCard,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) Color(0xFFFF5400) else Color.DarkGray,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { onUpdateTargetSetDuration?.invoke(duration) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${duration}s",
                                color = if (isSelected) Color.White else Color.LightGray,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Custom selection chip
                    val isCustomSelected = targetSetDuration !in durations
                    Box(
                        modifier = Modifier
                            .weight(1.2f)
                            .height(38.dp)
                            .background(
                                color = if (isCustomSelected) Color(0xFFFF5400) else SurfaceDarkCard,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isCustomSelected) Color(0xFFFF5400) else Color.DarkGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { showCustomDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isCustomSelected) "Custom: ${targetSetDuration}s" else "Custom...",
                            color = if (isCustomSelected) Color.White else Color.LightGray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Custom duration input Dialog
                if (showCustomDialog) {
                    AlertDialog(
                        onDismissRequest = { showCustomDialog = false },
                        containerColor = SurfaceDarkCard,
                        title = { Text("Enter Custom Duration (seconds)", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                        text = {
                            OutlinedTextField(
                                value = customSecondsText,
                                onValueChange = { customSecondsText = it.filter { char -> char.isDigit() } },
                                label = { Text("Duration in seconds", color = Color.Gray) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFFF5400),
                                    unfocusedBorderColor = Color.Gray,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                singleLine = true
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    val secs = customSecondsText.toIntOrNull()
                                    if (secs != null && secs > 0) {
                                        onUpdateTargetSetDuration?.invoke(secs)
                                    }
                                    showCustomDialog = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5400))
                            ) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showCustomDialog = false }) {
                                Text("Cancel", color = Color.Gray)
                            }
                        }
                    )
                }
            }
        }

        // ==================== 4. WORKOUT INFO CARD ====================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val gridItems = listOf(
                "SETS" to "$totalSets SETS",
                "REPS" to repsRange.uppercase(Locale.ROOT),
                "REST TIME" to "${restTimeSeconds}S REST",
                "SYSTEM" to "${exercise.difficulty.uppercase(Locale.ROOT)} MODE"
            )

            gridItems.forEach { (label, value) ->
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDarkCard),
                    border = BorderStroke(1.dp, BorderOrangeMuted),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = label,
                            color = Color.Gray,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = value,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.2.sp,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        // ==================== 5. 7 KEPT BUTTONS ONLY ====================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Primary actions row (Start, Pause, Resume, Complete Set, Skip Rest)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isWorkoutResting) {
                    // Skip Rest Button (takes full width during rest period for clarity)
                    Button(
                        onClick = { onSkipRest?.invoke() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text(
                            text = "SKIP REST",
                            color = Color.Black,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    if (!isSetTimerRunning) {
                        // Start button
                        Button(
                            onClick = { onStartSetTimer?.invoke() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5400)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Start Set", tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("START", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    } else {
                        if (isWorkoutPaused) {
                            // Resume button
                            Button(
                                onClick = { onResumeSetTimer?.invoke() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = "Resume Set", tint = Color.Black)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("RESUME", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        } else {
                            // Pause button
                            Button(
                                onClick = { onPauseSetTimer?.invoke() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "Pause Set", tint = Color.Black)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("PAUSE", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Complete Set button
                        Button(
                            onClick = { onCompleteSet?.invoke() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1.2f)
                                .height(50.dp)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "Complete Set", tint = Color.Black)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("COMPLETE SET", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }

            // Exercise Navigation Row (Previous Exercise, Next Exercise)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Previous Exercise
                Button(
                    onClick = { onPrevExercise?.invoke() },
                    enabled = onPrevExercise != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceDarkCard,
                        disabledContainerColor = SurfaceDarkCard.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, if (onPrevExercise != null) Color(0xFFFF5400) else Color.DarkGray),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Previous Exercise",
                        tint = if (onPrevExercise != null) Color.White else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "PREV EXERCISE",
                        color = if (onPrevExercise != null) Color.White else Color.Gray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Next Exercise
                Button(
                    onClick = { onNextExercise?.invoke() },
                    enabled = onNextExercise != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceDarkCard,
                        disabledContainerColor = SurfaceDarkCard.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, if (onNextExercise != null) Color(0xFFFF5400) else Color.DarkGray),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                ) {
                    Text(
                        text = "NEXT EXERCISE",
                        color = if (onNextExercise != null) Color.White else Color.Gray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next Exercise",
                        tint = if (onNextExercise != null) Color.White else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // ==================== 6. HOW TO PERFORM SECTION ====================
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDarkCard),
            border = BorderStroke(1.dp, GrayBorder),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "📋 HOW TO PERFORM",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )

                val executionSteps = listOf(
                    "Start Position" to "Set machine baseline. ${exercise.handPlacement}. Secure ${exercise.footPlacement}.",
                    "Movement path" to (exercise.instructions.firstOrNull() ?: "Drive weight dynamically through natural mechanical arc of alignment."),
                    "Peak Contraction" to "Squeeze muscles inside maximum contraction limits. Hold peak pressure load for 2 full seconds.",
                    "Controlled Return" to "Slowly return to baseline under perfect eccentric resistance release parameters."
                )

                executionSteps.forEachIndexed { sIdx, (title, detail) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .background(AccentOrange, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${sIdx + 1}",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = title.uppercase(Locale.ROOT),
                                color = AccentOrange,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = detail,
                                color = Color.White.copy(alpha = 0.82f),
                                fontSize = 11.sp,
                                lineHeight = 15.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // ==================== 7. COMMON MISTAKES SECTION ====================
        if (exercise.mistakes.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceDarkCard),
                border = BorderStroke(1.dp, BorderOrangeMuted),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "⚠️ COMMON CRITICAL MISTAKES",
                        color = AccentOrange,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )

                    exercise.mistakes.forEach { errorText ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(AccentOrange, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = TranslationHelper.translateMistake(errorText, exercise.category),
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
