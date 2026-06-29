package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.util.Locale

enum class MuscleGroup {
    // Chest
    UPPER_PEC,
    MID_PEC,
    LOWER_PEC,
    
    // Back
    UPPER_TRAPS,
    MID_TRAPS_RHOMBOIDS,
    UPPER_LATS,
    LOWER_LATS,
    ERECTOR_SPINAE,
    
    // Shoulders
    FRONT_DELT,
    SIDE_DELT,
    REAR_DELT,
    
    // Arms
    BICEPS,
    BICEPS_PEAK,
    BRACHIALIS_FOREARMS,
    LATERAL_TRICEPS,
    LONG_TRICEPS,
    
    // Legs
    QUADS,
    HAMSTRINGS,
    GLUTES,
    CALVES_GASTROC,
    CALVES_SOLEUS,
    
    // Abs
    UPPER_ABS,
    LOWER_ABS,
    OBLIQUES,
    CORE_TRANSVERSE
}

data class MuscleActivation(
    val name: String,
    val percentage: Int,
    val isPrimary: Boolean
)

/**
 * Custom-tailored biological highlights for any and every exercise name.
 */
fun MuscleGroup.getFriendlyName(): String {
    return when (this) {
        MuscleGroup.UPPER_PEC -> "Upper Chest (Clavicular Head)"
        MuscleGroup.MID_PEC -> "Middle Chest (Pectoralis Major)"
        MuscleGroup.LOWER_PEC -> "Lower Chest (Sternal Head)"
        MuscleGroup.UPPER_TRAPS -> "Upper Trapezius"
        MuscleGroup.MID_TRAPS_RHOMBOIDS -> "Mid Trapezius & Rhomboids"
        MuscleGroup.UPPER_LATS -> "Upper Latissimus Dorsi"
        MuscleGroup.LOWER_LATS -> "Lower Latissimus Dorsi"
        MuscleGroup.ERECTOR_SPINAE -> "Erector Spinae (Lower Back)"
        MuscleGroup.FRONT_DELT -> "Front Deltoid (Anterior)"
        MuscleGroup.SIDE_DELT -> "Side Deltoid (Lateral)"
        MuscleGroup.REAR_DELT -> "Rear Deltoid (Posterior)"
        MuscleGroup.BICEPS -> "Biceps Brachii"
        MuscleGroup.BICEPS_PEAK -> "Biceps Brachii (Short Head)"
        MuscleGroup.BRACHIALIS_FOREARMS -> "Brachialis & Forearms"
        MuscleGroup.LATERAL_TRICEPS -> "Triceps (Lateral Head)"
        MuscleGroup.LONG_TRICEPS -> "Triceps (Long Head)"
        MuscleGroup.QUADS -> "Quadriceps Femoris"
        MuscleGroup.HAMSTRINGS -> "Hamstrings"
        MuscleGroup.GLUTES -> "Gluteus Maximus"
        MuscleGroup.CALVES_GASTROC -> "Gastrocnemius (Calves)"
        MuscleGroup.CALVES_SOLEUS -> "Soleus (Lower Calves)"
        MuscleGroup.UPPER_ABS -> "Upper Rectus Abdominis"
        MuscleGroup.LOWER_ABS -> "Lower Rectus Abdominis"
        MuscleGroup.OBLIQUES -> "External Obliques"
        MuscleGroup.CORE_TRANSVERSE -> "Transversus Abdominis (Core)"
    }
}

private val exerciseSpecificMappings: Map<String, Pair<List<MuscleGroup>, List<MuscleGroup>>> = mapOf(
    "Bench Press" to Pair(listOf(MuscleGroup.MID_PEC), listOf(MuscleGroup.UPPER_PEC, MuscleGroup.FRONT_DELT, MuscleGroup.LATERAL_TRICEPS, MuscleGroup.LONG_TRICEPS)),
    "Incline Bench Press" to Pair(listOf(MuscleGroup.UPPER_PEC), listOf(MuscleGroup.MID_PEC, MuscleGroup.FRONT_DELT, MuscleGroup.LATERAL_TRICEPS, MuscleGroup.LONG_TRICEPS)),
    "Dumbbell Bench Press" to Pair(listOf(MuscleGroup.MID_PEC), listOf(MuscleGroup.UPPER_PEC, MuscleGroup.FRONT_DELT, MuscleGroup.LATERAL_TRICEPS)),
    "Incline Dumbbell Press" to Pair(listOf(MuscleGroup.UPPER_PEC), listOf(MuscleGroup.MID_PEC, MuscleGroup.FRONT_DELT, MuscleGroup.LATERAL_TRICEPS)),
    "Dumbbell Fly" to Pair(listOf(MuscleGroup.MID_PEC), listOf(MuscleGroup.UPPER_PEC, MuscleGroup.FRONT_DELT)),
    "Push-Up" to Pair(listOf(MuscleGroup.MID_PEC), listOf(MuscleGroup.UPPER_PEC, MuscleGroup.LOWER_PEC, MuscleGroup.FRONT_DELT, MuscleGroup.LATERAL_TRICEPS)),
    "Chest Dips" to Pair(listOf(MuscleGroup.LOWER_PEC), listOf(MuscleGroup.MID_PEC, MuscleGroup.FRONT_DELT, MuscleGroup.LATERAL_TRICEPS)),
    "Cable Crossover (High-to-Low)" to Pair(listOf(MuscleGroup.LOWER_PEC), listOf(MuscleGroup.MID_PEC, MuscleGroup.FRONT_DELT)),
    "Cable Crossover (Low-to-High)" to Pair(listOf(MuscleGroup.UPPER_PEC), listOf(MuscleGroup.MID_PEC, MuscleGroup.FRONT_DELT)),
    "Pec Deck Fly" to Pair(listOf(MuscleGroup.MID_PEC), listOf(MuscleGroup.FRONT_DELT)),

    // Back (15 UNIQUE)
    "Lat Pulldown" to Pair(listOf(MuscleGroup.UPPER_LATS, MuscleGroup.LOWER_LATS), listOf(MuscleGroup.BICEPS, MuscleGroup.MID_TRAPS_RHOMBOIDS)),
    "Wide Grip Lat Pulldown" to Pair(listOf(MuscleGroup.UPPER_LATS), listOf(MuscleGroup.BICEPS, MuscleGroup.MID_TRAPS_RHOMBOIDS)),
    "Close Grip Lat Pulldown" to Pair(listOf(MuscleGroup.UPPER_LATS, MuscleGroup.LOWER_LATS), listOf(MuscleGroup.BICEPS, MuscleGroup.MID_TRAPS_RHOMBOIDS)),
    "Single-Arm Lat Pulldown" to Pair(listOf(MuscleGroup.UPPER_LATS, MuscleGroup.LOWER_LATS), emptyList()),
    "Single Arm Lat Pulldown" to Pair(listOf(MuscleGroup.UPPER_LATS, MuscleGroup.LOWER_LATS), emptyList()),
    "Seated Cable Row" to Pair(listOf(MuscleGroup.MID_TRAPS_RHOMBOIDS, MuscleGroup.LOWER_LATS), listOf(MuscleGroup.BICEPS)),
    "Barbell Row" to Pair(listOf(MuscleGroup.MID_TRAPS_RHOMBOIDS, MuscleGroup.UPPER_LATS), listOf(MuscleGroup.BICEPS, MuscleGroup.ERECTOR_SPINAE)),
    "T-Bar Row" to Pair(listOf(MuscleGroup.MID_TRAPS_RHOMBOIDS, MuscleGroup.UPPER_LATS), listOf(MuscleGroup.BICEPS, MuscleGroup.REAR_DELT)),
    "Meadows Row" to Pair(listOf(MuscleGroup.UPPER_LATS, MuscleGroup.MID_TRAPS_RHOMBOIDS), listOf(MuscleGroup.REAR_DELT, MuscleGroup.BRACHIALIS_FOREARMS)),
    "One-Arm Dumbbell Row" to Pair(listOf(MuscleGroup.UPPER_LATS), listOf(MuscleGroup.LOWER_LATS, MuscleGroup.BICEPS, MuscleGroup.REAR_DELT)),
    "Chest Supported Row" to Pair(listOf(MuscleGroup.MID_TRAPS_RHOMBOIDS, MuscleGroup.UPPER_LATS), listOf(MuscleGroup.BICEPS, MuscleGroup.REAR_DELT)),
    "Straight Arm Pulldown" to Pair(listOf(MuscleGroup.UPPER_LATS, MuscleGroup.LOWER_LATS), listOf(MuscleGroup.LONG_TRICEPS)),
    "Face Pull" to Pair(listOf(MuscleGroup.REAR_DELT, MuscleGroup.MID_TRAPS_RHOMBOIDS), listOf(MuscleGroup.BICEPS)),
    "Dumbbell Shrugs" to Pair(listOf(MuscleGroup.UPPER_TRAPS), emptyList()),
    "Rack Pull" to Pair(listOf(MuscleGroup.ERECTOR_SPINAE, MuscleGroup.UPPER_TRAPS), listOf(MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS)),
    "Deadlift" to Pair(listOf(MuscleGroup.ERECTOR_SPINAE, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS), listOf(MuscleGroup.UPPER_TRAPS, MuscleGroup.QUADS)),

    // Shoulders (All 15 mappings updated to be completely unique and customized)
    "Seated Dumbbell Press" to Pair(listOf(MuscleGroup.FRONT_DELT, MuscleGroup.SIDE_DELT), listOf(MuscleGroup.LONG_TRICEPS, MuscleGroup.UPPER_TRAPS)),
    "Overhead Press" to Pair(listOf(MuscleGroup.FRONT_DELT, MuscleGroup.SIDE_DELT), listOf(MuscleGroup.LONG_TRICEPS, MuscleGroup.UPPER_TRAPS)),
    "Overhead Barbell Press" to Pair(listOf(MuscleGroup.FRONT_DELT, MuscleGroup.SIDE_DELT), listOf(MuscleGroup.LONG_TRICEPS, MuscleGroup.UPPER_TRAPS)),
    "Lateral Raise" to Pair(listOf(MuscleGroup.SIDE_DELT), listOf(MuscleGroup.UPPER_TRAPS, MuscleGroup.FRONT_DELT)),
    "Dumbbell Lateral Raise" to Pair(listOf(MuscleGroup.SIDE_DELT), listOf(MuscleGroup.UPPER_TRAPS, MuscleGroup.FRONT_DELT)),
    "Cable Lateral Raise" to Pair(listOf(MuscleGroup.SIDE_DELT, MuscleGroup.FRONT_DELT), listOf(MuscleGroup.UPPER_TRAPS, MuscleGroup.BRACHIALIS_FOREARMS)),
    "Machine Shoulder Press" to Pair(listOf(MuscleGroup.FRONT_DELT), listOf(MuscleGroup.LATERAL_TRICEPS, MuscleGroup.SIDE_DELT)),
    "Front Raise" to Pair(listOf(MuscleGroup.FRONT_DELT), listOf(MuscleGroup.SIDE_DELT, MuscleGroup.UPPER_PEC)),
    "Dumbbell Front Raise" to Pair(listOf(MuscleGroup.FRONT_DELT), listOf(MuscleGroup.SIDE_DELT, MuscleGroup.UPPER_PEC)),
    "Landmine Press" to Pair(listOf(MuscleGroup.FRONT_DELT, MuscleGroup.UPPER_PEC), listOf(MuscleGroup.LATERAL_TRICEPS, MuscleGroup.CORE_TRANSVERSE)),
    "Cable Front Raise" to Pair(listOf(MuscleGroup.FRONT_DELT, MuscleGroup.UPPER_PEC), listOf(MuscleGroup.LATERAL_TRICEPS, MuscleGroup.CORE_TRANSVERSE)),
    "Reverse Pec Deck" to Pair(listOf(MuscleGroup.REAR_DELT, MuscleGroup.MID_TRAPS_RHOMBOIDS), listOf(MuscleGroup.UPPER_TRAPS, MuscleGroup.LONG_TRICEPS)),
    "Rear Delt Pec Deck" to Pair(listOf(MuscleGroup.REAR_DELT, MuscleGroup.MID_TRAPS_RHOMBOIDS), listOf(MuscleGroup.UPPER_TRAPS, MuscleGroup.LONG_TRICEPS)),
    "Cable Face Pulls Shoulder" to Pair(listOf(MuscleGroup.REAR_DELT, MuscleGroup.MID_TRAPS_RHOMBOIDS), listOf(MuscleGroup.SIDE_DELT, MuscleGroup.UPPER_TRAPS, MuscleGroup.BICEPS)),
    "Face Pull" to Pair(listOf(MuscleGroup.REAR_DELT, MuscleGroup.MID_TRAPS_RHOMBOIDS), listOf(MuscleGroup.SIDE_DELT, MuscleGroup.UPPER_TRAPS, MuscleGroup.BICEPS)),
    "Rear Delt Fly" to Pair(listOf(MuscleGroup.REAR_DELT), listOf(MuscleGroup.MID_TRAPS_RHOMBOIDS, MuscleGroup.SIDE_DELT)),
    "Dumbbell Rear Delt Fly" to Pair(listOf(MuscleGroup.REAR_DELT), listOf(MuscleGroup.MID_TRAPS_RHOMBOIDS, MuscleGroup.SIDE_DELT)),
    "Smith Machine Shoulder Press" to Pair(listOf(MuscleGroup.FRONT_DELT, MuscleGroup.SIDE_DELT), listOf(MuscleGroup.LONG_TRICEPS, MuscleGroup.UPPER_TRAPS, MuscleGroup.MID_PEC)),
    "Arnold Press" to Pair(listOf(MuscleGroup.FRONT_DELT, MuscleGroup.SIDE_DELT), listOf(MuscleGroup.LONG_TRICEPS, MuscleGroup.LATERAL_TRICEPS)),
    "Barbell Upright Rows" to Pair(listOf(MuscleGroup.SIDE_DELT, MuscleGroup.UPPER_TRAPS), listOf(MuscleGroup.FRONT_DELT, MuscleGroup.BICEPS, MuscleGroup.BRACHIALIS_FOREARMS)),
    "Cable Y-Raise" to Pair(listOf(MuscleGroup.SIDE_DELT), listOf(MuscleGroup.UPPER_TRAPS, MuscleGroup.REAR_DELT, MuscleGroup.MID_TRAPS_RHOMBOIDS)),
    "Standing Dumbbell Shrugs" to Pair(listOf(MuscleGroup.UPPER_TRAPS), listOf(MuscleGroup.BRACHIALIS_FOREARMS, MuscleGroup.ERECTOR_SPINAE)),
    "Shrugs" to Pair(listOf(MuscleGroup.UPPER_TRAPS), listOf(MuscleGroup.BRACHIALIS_FOREARMS, MuscleGroup.ERECTOR_SPINAE)),

    // Biceps
    "Standing Barbell Curl" to Pair(listOf(MuscleGroup.BICEPS), listOf(MuscleGroup.BRACHIALIS_FOREARMS)),
    "Incline Dumbbell Curl" to Pair(listOf(MuscleGroup.BICEPS), listOf(MuscleGroup.BRACHIALIS_FOREARMS)),
    "Dumbbell Hammer Curl" to Pair(listOf(MuscleGroup.BRACHIALIS_FOREARMS), listOf(MuscleGroup.BICEPS)),
    "Preacher Curl EZ-Bar" to Pair(listOf(MuscleGroup.BICEPS), listOf(MuscleGroup.BRACHIALIS_FOREARMS)),
    "Cable Bicep Curl Straight Bar" to Pair(listOf(MuscleGroup.BICEPS), listOf(MuscleGroup.BRACHIALIS_FOREARMS)),
    "Concentration Curl" to Pair(listOf(MuscleGroup.BICEPS_PEAK), listOf(MuscleGroup.BRACHIALIS_FOREARMS)),
    "Spider Curl EZ-Bar" to Pair(listOf(MuscleGroup.BICEPS_PEAK), listOf(MuscleGroup.BRACHIALIS_FOREARMS)),
    "Barbell Drag Curl" to Pair(listOf(MuscleGroup.BICEPS), listOf(MuscleGroup.BRACHIALIS_FOREARMS)),
    "Rope Hammer Curl Cable" to Pair(listOf(MuscleGroup.BRACHIALIS_FOREARMS), listOf(MuscleGroup.BICEPS)),
    "High Cable Curl Double Arm" to Pair(listOf(MuscleGroup.BICEPS_PEAK), listOf(MuscleGroup.BRACHIALIS_FOREARMS)),
    "Reverse Grip Barbell Curl" to Pair(listOf(MuscleGroup.BRACHIALIS_FOREARMS), listOf(MuscleGroup.BICEPS)),
    "Zottman Curl" to Pair(listOf(MuscleGroup.BICEPS), listOf(MuscleGroup.BRACHIALIS_FOREARMS)),
    "Seated Alternating Dumbbell Curl" to Pair(listOf(MuscleGroup.BICEPS), listOf(MuscleGroup.BRACHIALIS_FOREARMS)),
    "Behind Bicep Cable Curl" to Pair(listOf(MuscleGroup.BICEPS_PEAK), listOf(MuscleGroup.BRACHIALIS_FOREARMS)),

    // Triceps
    "Rope Pushdown" to Pair(listOf(MuscleGroup.LATERAL_TRICEPS), listOf(MuscleGroup.LONG_TRICEPS)),
    "V-Bar Pushdown" to Pair(listOf(MuscleGroup.LATERAL_TRICEPS), listOf(MuscleGroup.LONG_TRICEPS)),
    "Overhead Dumbbell Extension" to Pair(listOf(MuscleGroup.LONG_TRICEPS), listOf(MuscleGroup.LATERAL_TRICEPS)),
    "EZ-Bar Skull Crushers" to Pair(listOf(MuscleGroup.LONG_TRICEPS), listOf(MuscleGroup.LATERAL_TRICEPS)),
    "Bodyweight Dips" to Pair(listOf(MuscleGroup.LONG_TRICEPS), listOf(MuscleGroup.LATERAL_TRICEPS, MuscleGroup.FRONT_DELT)),
    "Close-Grip Bench Press" to Pair(listOf(MuscleGroup.LONG_TRICEPS, MuscleGroup.LATERAL_TRICEPS), listOf(MuscleGroup.MID_PEC, MuscleGroup.FRONT_DELT)),
    "Single-Arm Cable Overhead" to Pair(listOf(MuscleGroup.LONG_TRICEPS), listOf(MuscleGroup.LATERAL_TRICEPS)),
    "Cable Kickbacks" to Pair(listOf(MuscleGroup.LATERAL_TRICEPS), listOf(MuscleGroup.LONG_TRICEPS)),
    "Bench Dips" to Pair(listOf(MuscleGroup.LONG_TRICEPS), listOf(MuscleGroup.LATERAL_TRICEPS, MuscleGroup.FRONT_DELT)),
    "Overhead Rope Extension Cable" to Pair(listOf(MuscleGroup.LONG_TRICEPS), listOf(MuscleGroup.LATERAL_TRICEPS)),
    "Triangle Floor Push-ups" to Pair(listOf(MuscleGroup.LATERAL_TRICEPS), listOf(MuscleGroup.LONG_TRICEPS, MuscleGroup.MID_PEC)),
    "Machine Tricep Dip" to Pair(listOf(MuscleGroup.LONG_TRICEPS), listOf(MuscleGroup.LATERAL_TRICEPS)),
    "Straight Bar Pushdown" to Pair(listOf(MuscleGroup.LATERAL_TRICEPS), listOf(MuscleGroup.LONG_TRICEPS)),
    "Single-Arm D-handle Pushdown" to Pair(listOf(MuscleGroup.LATERAL_TRICEPS), listOf(MuscleGroup.LONG_TRICEPS)),

    // Legs
    "Barbell Back Squat" to Pair(listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES), listOf(MuscleGroup.HAMSTRINGS, MuscleGroup.CALVES_GASTROC)),
    "Dumbbell Romanian Deadlift" to Pair(listOf(MuscleGroup.HAMSTRINGS, MuscleGroup.GLUTES), listOf(MuscleGroup.ERECTOR_SPINAE)),
    "Heavy Leg Press" to Pair(listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES), listOf(MuscleGroup.HAMSTRINGS)),
    "Hack Squat Machine" to Pair(listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES), listOf(MuscleGroup.HAMSTRINGS)),
    "Leg Extensions Machine" to Pair(listOf(MuscleGroup.QUADS), emptyList()),
    "Seated Leg Curl Machine" to Pair(listOf(MuscleGroup.HAMSTRINGS), listOf(MuscleGroup.GLUTES)),
    "Standing Calf Raises" to Pair(listOf(MuscleGroup.CALVES_GASTROC), listOf(MuscleGroup.CALVES_SOLEUS)),
    "Lying Leg Curl Bed" to Pair(listOf(MuscleGroup.HAMSTRINGS), listOf(MuscleGroup.GLUTES)),
    "Bulgarian Split Squat" to Pair(listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES), listOf(MuscleGroup.HAMSTRINGS)),
    "Goblet Squat Front Loaded" to Pair(listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES), listOf(MuscleGroup.HAMSTRINGS)),
    "Barbell Hip Thrust" to Pair(listOf(MuscleGroup.GLUTES), listOf(MuscleGroup.HAMSTRINGS)),
    "Walking Dumbbell Lunges" to Pair(listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES), listOf(MuscleGroup.HAMSTRINGS)),
    "Standing Calf Raise Machine" to Pair(listOf(MuscleGroup.CALVES_GASTROC), listOf(MuscleGroup.CALVES_SOLEUS)),
    "Seated Calf Raise Machine" to Pair(listOf(MuscleGroup.CALVES_SOLEUS), listOf(MuscleGroup.CALVES_GASTROC)),
    "Leg Press Calf PressSled" to Pair(listOf(MuscleGroup.CALVES_GASTROC), listOf(MuscleGroup.CALVES_SOLEUS)),

    // Abs
    "Hanging Knee Raise" to Pair(listOf(MuscleGroup.LOWER_ABS), listOf(MuscleGroup.OBLIQUES)),
    "Seated Cable Crunch" to Pair(listOf(MuscleGroup.UPPER_ABS), listOf(MuscleGroup.OBLIQUES)),
    "Captains Chair Leg Raise" to Pair(listOf(MuscleGroup.LOWER_ABS), listOf(MuscleGroup.OBLIQUES)),
    "Ab Wheel Rollout" to Pair(listOf(MuscleGroup.CORE_TRANSVERSE, MuscleGroup.UPPER_ABS, MuscleGroup.LOWER_ABS), listOf(MuscleGroup.OBLIQUES)),
    "Decline Russian Twist" to Pair(listOf(MuscleGroup.OBLIQUES), listOf(MuscleGroup.UPPER_ABS, MuscleGroup.LOWER_ABS)),
    "High Plank Hold" to Pair(listOf(MuscleGroup.CORE_TRANSVERSE), listOf(MuscleGroup.UPPER_ABS, MuscleGroup.LOWER_ABS, MuscleGroup.OBLIQUES)),
    "Bicycle Floor Crunches" to Pair(listOf(MuscleGroup.UPPER_ABS, MuscleGroup.OBLIQUES), listOf(MuscleGroup.LOWER_ABS)),
    "Hanging Straight Leg Raise" to Pair(listOf(MuscleGroup.LOWER_ABS), listOf(MuscleGroup.OBLIQUES)),
    "Hanging Windshield Wipers" to Pair(listOf(MuscleGroup.OBLIQUES), listOf(MuscleGroup.UPPER_ABS, MuscleGroup.LOWER_ABS)),
    "Cable Oblique Woodchopper" to Pair(listOf(MuscleGroup.OBLIQUES), listOf(MuscleGroup.UPPER_ABS, MuscleGroup.LOWER_ABS)),
    "Swiss Ball Crunch" to Pair(listOf(MuscleGroup.UPPER_ABS), listOf(MuscleGroup.OBLIQUES)),
    "Oblique Heel Taps Floor" to Pair(listOf(MuscleGroup.OBLIQUES), listOf(MuscleGroup.UPPER_ABS)),
    "Abdominal Crunch Machine" to Pair(listOf(MuscleGroup.UPPER_ABS), listOf(MuscleGroup.OBLIQUES))
)

fun getBioActivation(exerciseName: String, category: String): Pair<List<MuscleGroup>, List<MuscleGroup>> {
    val exactMatch = exerciseSpecificMappings.keys.firstOrNull { it.equals(exerciseName, ignoreCase = true) }
    if (exactMatch != null) {
        return exerciseSpecificMappings[exactMatch]!!
    }

    val name = exerciseName.lowercase(Locale.ROOT)
    val cat = category.lowercase(Locale.ROOT)
    
    val primary = mutableListOf<MuscleGroup>()
    val secondary = mutableListOf<MuscleGroup>()
    
    when {
        // --- CHEST ---
        name.contains("incline bench press") || name.contains("incline press") || name.contains("incline dumbbell press") -> {
            primary.add(MuscleGroup.UPPER_PEC)
            secondary.addAll(listOf(MuscleGroup.FRONT_DELT, MuscleGroup.LATERAL_TRICEPS))
        }
        name.contains("incline fly") || name.contains("cable low-to-high") || name.contains("low to high") -> {
            primary.add(MuscleGroup.UPPER_PEC)
            secondary.add(MuscleGroup.FRONT_DELT)
        }
        name.contains("decline bench") || name.contains("decline press") || name.contains("decline fly") || name.contains("high-to-low") || name.contains("high to low") -> {
            primary.add(MuscleGroup.LOWER_PEC)
            secondary.addAll(listOf(MuscleGroup.MID_PEC, MuscleGroup.LATERAL_TRICEPS))
        }
        name.contains("pec deck") || name.contains("fly") || name.contains("crossover") -> {
            primary.add(MuscleGroup.MID_PEC)
            secondary.add(MuscleGroup.FRONT_DELT)
        }
        name.contains("bench press") || name.contains("chest press") || name.contains("push-up") || name.contains("pushup") || cat.contains("chest") -> {
            primary.add(MuscleGroup.MID_PEC)
            secondary.addAll(listOf(MuscleGroup.UPPER_PEC, MuscleGroup.FRONT_DELT, MuscleGroup.LATERAL_TRICEPS))
        }
        
        // --- BACK ---
        name.contains("single arm dumbbell row") || name.contains("one arm row") -> {
            primary.add(MuscleGroup.UPPER_LATS)
            secondary.addAll(listOf(MuscleGroup.LOWER_LATS, MuscleGroup.BRACHIALIS_FOREARMS, MuscleGroup.REAR_DELT))
        }
        name.contains("wide grip pulldown") -> {
            primary.add(MuscleGroup.UPPER_LATS)
            secondary.addAll(listOf(MuscleGroup.BICEPS, MuscleGroup.MID_TRAPS_RHOMBOIDS))
        }
        name.contains("lat pulldown") || name.contains("pull up") || name.contains("pull-up") || name.contains("chin up") -> {
            primary.add(MuscleGroup.UPPER_LATS)
            primary.add(MuscleGroup.LOWER_LATS)
            secondary.addAll(listOf(MuscleGroup.BICEPS, MuscleGroup.MID_TRAPS_RHOMBOIDS))
        }
        name.contains("close grip row") || name.contains("seated row") || name.contains("t-bar row") -> {
            primary.add(MuscleGroup.MID_TRAPS_RHOMBOIDS)
            secondary.addAll(listOf(MuscleGroup.LOWER_LATS, MuscleGroup.BICEPS))
        }
        name.contains("face pull") -> {
            primary.add(MuscleGroup.REAR_DELT)
            primary.add(MuscleGroup.MID_TRAPS_RHOMBOIDS)
            secondary.addAll(listOf(MuscleGroup.SIDE_DELT, MuscleGroup.UPPER_TRAPS))
        }
        name.contains("shrug") -> {
            primary.add(MuscleGroup.UPPER_TRAPS)
            secondary.add(MuscleGroup.BRACHIALIS_FOREARMS)
        }
        name.contains("deadlift") || name.contains("rack pull") -> {
            primary.addAll(listOf(MuscleGroup.ERECTOR_SPINAE, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS))
            secondary.addAll(listOf(MuscleGroup.UPPER_TRAPS, MuscleGroup.BRACHIALIS_FOREARMS, MuscleGroup.QUADS))
        }
        name.contains("barbell row") || name.contains("row") || cat.contains("back") -> {
            primary.add(MuscleGroup.UPPER_LATS)
            primary.add(MuscleGroup.MID_TRAPS_RHOMBOIDS)
            secondary.addAll(listOf(MuscleGroup.REAR_DELT, MuscleGroup.BICEPS, MuscleGroup.ERECTOR_SPINAE))
        }
        
        // --- SHOULDERS ---
        name.contains("front raise") -> {
            primary.add(MuscleGroup.FRONT_DELT)
            secondary.add(MuscleGroup.SIDE_DELT)
        }
        name.contains("lateral raise") || name.contains("side delt") || name.contains("y-raise") -> {
            primary.add(MuscleGroup.SIDE_DELT)
            secondary.addAll(listOf(MuscleGroup.FRONT_DELT, MuscleGroup.UPPER_TRAPS))
        }
        name.contains("rear delt fly") || name.contains("reverse fly") || name.contains("rear delt") -> {
            primary.add(MuscleGroup.REAR_DELT)
            secondary.add(MuscleGroup.MID_TRAPS_RHOMBOIDS)
        }
        name.contains("overhead press") || name.contains("shoulder press") || name.contains("military press") || name.contains("arnold press") || cat.contains("shoulder") -> {
            primary.addAll(listOf(MuscleGroup.FRONT_DELT, MuscleGroup.SIDE_DELT))
            secondary.addAll(listOf(MuscleGroup.LONG_TRICEPS, MuscleGroup.UPPER_TRAPS))
        }
        
        // --- ARMS ---
        name.contains("preacher curl") -> {
            primary.add(MuscleGroup.BICEPS)
            secondary.add(MuscleGroup.BRACHIALIS_FOREARMS)
        }
        name.contains("hammer curl") -> {
            primary.add(MuscleGroup.BRACHIALIS_FOREARMS)
            secondary.add(MuscleGroup.BICEPS)
        }
        name.contains("concentration curl") -> {
            primary.add(MuscleGroup.BICEPS_PEAK)
            secondary.add(MuscleGroup.BRACHIALIS_FOREARMS)
        }
        name.contains("pushdown") || name.contains("tricep extension") -> {
            primary.add(MuscleGroup.LATERAL_TRICEPS)
            secondary.add(MuscleGroup.BRACHIALIS_FOREARMS)
        }
        name.contains("overhead extension") || name.contains("skull crusher") || name.contains("dip") || cat.contains("tricep") -> {
            primary.add(MuscleGroup.LONG_TRICEPS)
            secondary.addAll(listOf(MuscleGroup.LATERAL_TRICEPS, MuscleGroup.FRONT_DELT))
        }
        name.contains("curl") || cat.contains("bicep") -> {
            primary.add(MuscleGroup.BICEPS)
            secondary.add(MuscleGroup.BRACHIALIS_FOREARMS)
        }
        
        // --- LEGS ---
        name.contains("leg extension") -> {
            primary.add(MuscleGroup.QUADS)
        }
        name.contains("leg curl") || name.contains("hamstring curl") -> {
            primary.add(MuscleGroup.HAMSTRINGS)
            secondary.add(MuscleGroup.GLUTES)
        }
        name.contains("romanian deadlift") || name.contains("rdl") -> {
            primary.addAll(listOf(MuscleGroup.HAMSTRINGS, MuscleGroup.GLUTES))
            secondary.add(MuscleGroup.ERECTOR_SPINAE)
        }
        name.contains("calf raise") || name.contains("calf press") || name.contains("calves") || cat.contains("calf") -> {
            primary.add(MuscleGroup.CALVES_GASTROC)
            secondary.add(MuscleGroup.CALVES_SOLEUS)
        }
        name.contains("squat") || name.contains("leg press") || name.contains("lunge") || cat.contains("leg") || cat.contains("quad") || cat.contains("glute") -> {
            primary.addAll(listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES))
            secondary.addAll(listOf(MuscleGroup.HAMSTRINGS, MuscleGroup.CALVES_GASTROC))
        }
        
        // --- ABS & CORE ---
        name.contains("crunch") -> {
            primary.add(MuscleGroup.UPPER_ABS)
            secondary.add(MuscleGroup.OBLIQUES)
        }
        name.contains("leg raise") || name.contains("hanging raise") -> {
            primary.add(MuscleGroup.LOWER_ABS)
            secondary.add(MuscleGroup.OBLIQUES)
        }
        name.contains("russian twist") || name.contains("woodchop") || name.contains("side bend") -> {
            primary.add(MuscleGroup.OBLIQUES)
            secondary.addAll(listOf(MuscleGroup.UPPER_ABS, MuscleGroup.LOWER_ABS))
        }
        name.contains("plank") || name.contains("ab wheel") || cat.contains("abs") || cat.contains("core") -> {
            primary.addAll(listOf(MuscleGroup.CORE_TRANSVERSE, MuscleGroup.UPPER_ABS, MuscleGroup.LOWER_ABS))
            secondary.add(MuscleGroup.OBLIQUES)
        }
        
        else -> {
            // General Fallbacks
            when {
                cat.contains("chest") -> {
                    primary.add(MuscleGroup.MID_PEC)
                    secondary.add(MuscleGroup.FRONT_DELT)
                }
                cat.contains("back") -> {
                    primary.add(MuscleGroup.UPPER_LATS)
                    secondary.add(MuscleGroup.MID_TRAPS_RHOMBOIDS)
                }
                cat.contains("shoulder") -> {
                    primary.add(MuscleGroup.SIDE_DELT)
                    secondary.add(MuscleGroup.FRONT_DELT)
                }
                cat.contains("arm") || cat.contains("bicep") -> {
                    primary.add(MuscleGroup.BICEPS)
                    secondary.add(MuscleGroup.BRACHIALIS_FOREARMS)
                }
                cat.contains("tricep") -> {
                    primary.add(MuscleGroup.LATERAL_TRICEPS)
                    secondary.add(MuscleGroup.LONG_TRICEPS)
                }
                cat.contains("leg") || cat.contains("thigh") -> {
                    primary.add(MuscleGroup.QUADS)
                    secondary.add(MuscleGroup.GLUTES)
                }
                cat.contains("abs") || cat.contains("core") -> {
                    primary.add(MuscleGroup.UPPER_ABS)
                    secondary.add(MuscleGroup.OBLIQUES)
                }
                else -> {
                    primary.add(MuscleGroup.QUADS)
                    secondary.add(MuscleGroup.GLUTES)
                }
            }
        }
    }
    
    return Pair(primary, secondary)
}

/**
 * High-definition muscle percentage breakdown.
 */
fun getBioContributions(exerciseName: String, category: String): List<MuscleActivation> {
    val (primary, secondary) = getBioActivation(exerciseName, category)
    val list = mutableListOf<MuscleActivation>()
    
    if (primary.isNotEmpty()) {
        val totalPrimaryPercentage = if (secondary.isEmpty()) 100 else 75
        val baseP = totalPrimaryPercentage / primary.size
        primary.forEachIndexed { idx, m ->
            val p = if (idx == 0) baseP + (totalPrimaryPercentage % primary.size) else baseP
            list.add(MuscleActivation(m.getFriendlyName(), p, true))
        }
    }
    
    if (secondary.isNotEmpty()) {
        val totalSecondaryPercentage = if (primary.isEmpty()) 100 else 25
        val baseS = totalSecondaryPercentage / secondary.size
        secondary.forEachIndexed { idx, m ->
            val s = if (idx == 0) baseS + (totalSecondaryPercentage % secondary.size) else baseS
            list.add(MuscleActivation(m.getFriendlyName(), s, false))
        }
    }
    
    return list
}

fun oldGetBioContributions(exerciseName: String, category: String): List<MuscleActivation> {
    val name = exerciseName.lowercase(Locale.ROOT)
    val cat = category.lowercase(Locale.ROOT)
    
    return when {
        name.contains("incline bench press") || name.contains("incline press") -> {
            listOf(
                MuscleActivation("Upper Pectoralis (Clavicular)", 70, true),
                MuscleActivation("Anterior Deltoids", 20, false),
                MuscleActivation("Lateral Triceps Head", 10, false)
            )
        }
        name.contains("decline bench") || name.contains("decline press") -> {
            listOf(
                MuscleActivation("Lower Pectoralis (Sternal)", 75, true),
                MuscleActivation("Middle Pectoralis Major", 15, false),
                MuscleActivation("Triceps Brachii", 10, false)
            )
        }
        name.contains("bench press") || name.contains("chest press") -> {
            listOf(
                MuscleActivation("Middle Pectoralis Major", 65, true),
                MuscleActivation("Anterior Deltoids", 20, false),
                MuscleActivation("Triceps Brachii (Lateral Head)", 15, false)
            )
        }
        name.contains("incline fly") || name.contains("cable low-to-high") -> {
            listOf(
                MuscleActivation("Upper Pectoralis (Clavicular Head)", 85, true),
                MuscleActivation("Anterior Deltoids", 15, false)
            )
        }
        name.contains("cable high-to-low") -> {
            listOf(
                MuscleActivation("Lower Pectoralis (Sternal Head)", 85, true),
                MuscleActivation("Middle Pectoralis", 15, false)
            )
        }
        name.contains("pec deck") -> {
            listOf(
                MuscleActivation("Middle Pectoralis Major", 85, true),
                MuscleActivation("Anterior Deltoids", 15, false)
            )
        }
        name.contains("single arm dumbbell row") || name.contains("one arm row") -> {
            listOf(
                MuscleActivation("Latissimus Dorsi", 85, true),
                MuscleActivation("Forearms & Grip Muscles", 10, false),
                MuscleActivation("Rear Deltoids", 5, false)
            )
        }
        name.contains("wide grip pulldown") -> {
            listOf(
                MuscleActivation("Upper Latissimus Dorsi", 75, true),
                MuscleActivation("Teres Major", 15, false),
                MuscleActivation("Biceps Brachii", 10, false)
            )
        }
        name.contains("lat pulldown") -> {
            listOf(
                MuscleActivation("Latissimus Dorsi", 80, true),
                MuscleActivation("Biceps Brachii", 15, false),
                MuscleActivation("Rhomboids", 5, false)
            )
        }
        name.contains("close grip row") -> {
            listOf(
                MuscleActivation("Mid Back (Rhomboids)", 60, true),
                MuscleActivation("Latissimus Dorsi", 25, false),
                MuscleActivation("Biceps Brachii", 15, false)
            )
        }
        name.contains("face pull") -> {
            listOf(
                MuscleActivation("Rear Deltoids", 55, true),
                MuscleActivation("Middle Trapezius", 30, true),
                MuscleActivation("Side Deltoids", 15, false)
            )
        }
        name.contains("shrug") -> {
            listOf(
                MuscleActivation("Upper Trapezius", 90, true),
                MuscleActivation("Forearm Flexors", 10, false)
            )
        }
        name.contains("deadlift") -> {
            listOf(
                MuscleActivation("Erector Spinae (Lower Back)", 40, true),
                MuscleActivation("Gluteus Maximus", 30, true),
                MuscleActivation("Hamstrings (Biceps Femoris)", 20, true),
                MuscleActivation("Forearms & Trapezius", 10, false)
            )
        }
        name.contains("front raise") -> {
            listOf(
                MuscleActivation("Anterior Deltoids (Front)", 85, true),
                MuscleActivation("Lateral Deltoids (Side)", 15, false)
            )
        }
        name.contains("lateral raise") -> {
            listOf(
                MuscleActivation("Lateral Deltoids (Side)", 85, true),
                MuscleActivation("Anterior Deltoids", 10, false),
                MuscleActivation("Upper Trapezius", 5, false)
            )
        }
        name.contains("rear delt") -> {
            listOf(
                MuscleActivation("Posterior Deltoids (Rear)", 85, true),
                MuscleActivation("Rhomboids & Traps", 15, false)
            )
        }
        name.contains("overhead press") || name.contains("shoulder press") -> {
            listOf(
                MuscleActivation("Anterior Deltoids", 50, true),
                MuscleActivation("Lateral Deltoids", 35, true),
                MuscleActivation("Triceps Brachii", 15, false)
            )
        }
        name.contains("preacher curl") -> {
            listOf(
                MuscleActivation("Biceps Brachii", 85, true),
                MuscleActivation("Brachioradialis", 15, false)
            )
        }
        name.contains("hammer curl") -> {
            listOf(
                MuscleActivation("Brachialis & Brachioradialis", 75, true),
                MuscleActivation("Biceps Brachii", 25, false)
            )
        }
        name.contains("concentration curl") -> {
            listOf(
                MuscleActivation("Biceps Peak (Short Head)", 85, true),
                MuscleActivation("Forearms & Grip", 15, false)
            )
        }
        name.contains("pushdown") -> {
            listOf(
                MuscleActivation("Lateral Triceps Head", 85, true),
                MuscleActivation("Anconeus / Forearms", 15, false)
            )
        }
        name.contains("overhead extension") -> {
            listOf(
                MuscleActivation("Long Head of Triceps", 85, true),
                MuscleActivation("Lateral Triceps Head", 15, false)
            )
        }
        name.contains("squat") -> {
            listOf(
                MuscleActivation("Quadriceps", 50, true),
                MuscleActivation("Gluteus Maximus", 35, true),
                MuscleActivation("Hamstrings", 15, false)
            )
        }
        name.contains("leg extension") -> {
            listOf(
                MuscleActivation("Quadriceps Femoris", 95, true),
                MuscleActivation("Patellar Tendon", 5, false)
            )
        }
        name.contains("leg curl") -> {
            listOf(
                MuscleActivation("Hamstrings (Biceps Femoris)", 90, true),
                MuscleActivation("Gastrocnemius (Calves)", 10, false)
            )
        }
        name.contains("romanian deadlift") -> {
            listOf(
                MuscleActivation("Hamstrings", 55, true),
                MuscleActivation("Gluteus Maximus", 35, true),
                MuscleActivation("Lower Back (Erectors)", 10, false)
            )
        }
        name.contains("calf raise") -> {
            listOf(
                MuscleActivation("Gastrocnemius", 75, true),
                MuscleActivation("Soleus", 25, false)
            )
        }
        name.contains("crunch") -> {
            listOf(
                MuscleActivation("Upper Rectus Abdominis", 85, true),
                MuscleActivation("Obliques", 15, false)
            )
        }
        name.contains("leg raise") -> {
            listOf(
                MuscleActivation("Lower Rectus Abdominis", 80, true),
                MuscleActivation("Hip Flexors (Iliopsoas)", 20, false)
            )
        }
        name.contains("russian twist") -> {
            listOf(
                MuscleActivation("External Obliques", 80, true),
                MuscleActivation("Rectus Abdominis", 20, false)
            )
        }
        name.contains("plank") -> {
            listOf(
                MuscleActivation("Transverse Abdominis (Core)", 60, true),
                MuscleActivation("Rectus Abdominis", 30, true),
                MuscleActivation("Shoulders / Quads", 10, false)
            )
        }
        else -> {
            when {
                cat.contains("chest") -> listOf(
                    MuscleActivation("Pectoralis Major", 80, true),
                    MuscleActivation("Anterior Deltoids", 20, false)
                )
                cat.contains("back") -> listOf(
                    MuscleActivation("Latissimus Dorsi", 70, true),
                    MuscleActivation("Rhomboids & Traps", 30, false)
                )
                cat.contains("shoulder") -> listOf(
                    MuscleActivation("Lateral Deltoids", 65, true),
                    MuscleActivation("Anterior Deltoids", 35, false)
                )
                cat.contains("arm") -> listOf(
                    MuscleActivation("Biceps Brachii", 80, true),
                    MuscleActivation("Forearm Flexors", 20, false)
                )
                cat.contains("leg") -> listOf(
                    MuscleActivation("Quadriceps", 60, true),
                    MuscleActivation("Gluteus Maximus", 40, true)
                )
                else -> listOf(
                    MuscleActivation("Primary Muscle Target", 80, true),
                    MuscleActivation("Stabilizing Muscles", 20, false)
                )
            }
        }
    }
}

/**
 * Real clinical kinesiological tip from exercise scientists.
 */
fun getBioTip(exerciseName: String, category: String): String {
    val name = exerciseName.lowercase(Locale.ROOT)
    return when {
        name.contains("incline bench press") || name.contains("incline press") -> {
            "Keep the incline angle near 30 degrees. Anything steeper shifts the tension away from the upper chest clavicular fibers onto the front deltoids."
        }
        name.contains("decline bench") -> {
            "Drive along a downward arc to align perfectly with the sternal pectoralis fibers, fully minimizing rotator cuff shearing."
        }
        name.contains("bench press") -> {
            "Squeeze your shoulder blades back and down before initiating the descent. This locks in stability and forces your chest to drive the load instead of your shoulders."
        }
        name.contains("lat pulldown") -> {
            "Avoid pulling with your hands. Instead, imagine your hands are mere hooks and drive your elbows straight down into your rear pockets to fully engage the lats."
        }
        name.contains("single arm dumbbell row") -> {
            "Pull the dumbbell toward your hip crease rather than your chest. This maximizes the anatomical stretch-shorten cycle of the latissimus dorsi."
        }
        name.contains("face pull") -> {
            "Pull the rope toward your nose while flaring your elbows wide. Finish by rotating your hands outward to active rotator cuff stabilizers."
        }
        name.contains("shrug") -> {
            "Shrug vertically. Do not roll your shoulders, as rolling offers no biomechanical benefit and increases joint stress."
        }
        name.contains("deadlift") -> {
            "Keep the bar in contact with your shins. Drive through your heels and hinge your hips forward—never pull with a rounded spine."
        }
        name.contains("lateral raise") -> {
            "Lead the movement with your elbows and keep your hands slightly below elbow height to eliminate upper trap dominance."
        }
        name.contains("preacher curl") -> {
            "Maintain full contact of your upper arms with the pad to eliminate momentum, targeting the biceps brachii at their absolute peak elongation."
        }
        name.contains("hammer curl") -> {
            "By keeping your palms facing each other, you transition torque to the deep brachialis and brachioradialis, building thicker forearms."
        }
        name.contains("leg extension") -> {
            "Avoid locking out hyper-explosively. Instead, pause at the peak of contraction for 1 second to maximize vastus medialis hypertrophy."
        }
        name.contains("leg curl") -> {
            "Keep your hips firmly pressed into the pad to prevent lower back hyperextension from overriding hamstring isolation."
        }
        name.contains("calf raise") -> {
            "Perform a deep 3-second stretch at the absolute bottom, then squeeze high on your big toe. This eliminates rubber-band elasticity momentum."
        }
        name.contains("crunch") -> {
            "Peel your spine off the floor bone-by-bone. Exhale completely at the top to fully contract the rectus abdominis."
        }
        name.contains("plank") -> {
            "Squeeze your glutes, brace your abs as if expecting a blow, and actively pull your elbows toward your toes to spike core activation."
        }
        else -> {
            "Squeeze the target muscle at peak contraction for 1-2 seconds, then slowly lower the load over a full 3-second negative phase."
        }
    }
}

/**
 * Renders the clinical background anatomy silhouette vector layers on Canvas.
 */
fun drawClinicalHumanSilhouette(
    drawScope: androidx.compose.ui.graphics.drawscope.DrawScope,
    isBackView: Boolean,
    isSideView: Boolean,
    highlightedGroup: Set<MuscleGroup>,
    primaryGroup: List<MuscleGroup>,
    secondaryGroup: List<MuscleGroup>,
    isMale: Boolean = true
) {
    val w = drawScope.size.width
    val h = drawScope.size.height
    val cx = w / 2f
    val cy = h / 2f
    val deltRadius = if (isMale) w * 0.045f else w * 0.035f

    val primaryColor = Color(0xFFFF5400) // Bright Orange
    val secondaryColor = Color(0xFFFFB070) // Light Orange

    // Function to decide gradient/brush for a given muscle group
    fun getMuscleBrush(group: MuscleGroup, centerOffset: Offset, radius: Float): Brush {
        val isPrimary = primaryGroup.contains(group)
        val isSecondary = secondaryGroup.contains(group)
        
        val colors = when {
            isPrimary -> listOf(Color(0xFFFF7B00), Color(0xFFFF4000), Color(0xFFC01F00))
            isSecondary -> listOf(Color(0xFFFFB070), Color(0xFFFF8530), Color(0xFFD45500))
            else -> listOf(Color(0xFF383838), Color(0xFF222222), Color(0xFF101010)) // realistic shaded grey!
        }
        return Brush.radialGradient(
            colors = colors,
            center = centerOffset,
            radius = radius
        )
    }

    fun getMuscleOutline(group: MuscleGroup): Color {
        val isPrimary = primaryGroup.contains(group)
        val isSecondary = secondaryGroup.contains(group)
        return when {
            isPrimary -> Color(0xFFFF5400).copy(alpha = 0.9f)
            isSecondary -> Color(0xFFFFB070).copy(alpha = 0.9f)
            else -> Color(0xFF4A4A4A).copy(alpha = 0.6f)
        }
    }

    // Function to draw subtle realistic muscle fiber/striation layers inside the muscle path
    fun drawMuscleFibers(path: Path, group: MuscleGroup, start: Offset, end: Offset) {
        val isPrimary = primaryGroup.contains(group)
        val isSecondary = secondaryGroup.contains(group)
        
        val fiberColor = when {
            isPrimary -> Color(0xFFFFB070).copy(alpha = 0.3f)
            isSecondary -> Color(0xFFFFE0C0).copy(alpha = 0.3f)
            else -> Color(0xFF5E5E5E).copy(alpha = 0.2f)
        }
        
        // Draw parallel lines to simulate realistic muscle striations
        val steps = 3
        for (i in 1..steps) {
            val ratio = i.toFloat() / (steps + 1)
            val dx = (end.x - start.x) * ratio
            val dy = (end.y - start.y) * ratio
            val midX = start.x + dx
            val midY = start.y + dy
            
            val perpX = -(end.y - start.y) * 0.15f
            val perpY = (end.x - start.x) * 0.15f
            drawScope.drawLine(
                color = fiberColor,
                start = Offset(midX - perpX, midY - perpY),
                end = Offset(midX + perpX, midY + perpY),
                strokeWidth = 1f
            )
        }
    }

    // 1. Draw High-Resolution Athletic Underbody Silhouette
    val bodyOutline = Path().apply {
        if (isSideView) {
            moveTo(cx, cy - h * 0.42f)
            cubicTo(cx - w * 0.05f, cy - h * 0.42f, cx - w * 0.06f, cy - h * 0.34f, cx, cy - h * 0.34f)
            lineTo(cx - w * 0.03f, cy - h * 0.28f)
            cubicTo(cx - w * 0.14f, cy - h * 0.24f, cx - w * 0.16f, cy - h * 0.10f, cx - w * 0.06f, cy)
            cubicTo(cx - w * 0.08f, cy + h * 0.06f, cx - w * 0.04f, cy + h * 0.15f, cx, cy + h * 0.22f)
            cubicTo(cx + w * 0.16f, cy + h * 0.18f, cx + w * 0.12f, cy + h * 0.32f, cx + w * 0.04f, cy + h * 0.44f)
            cubicTo(cx + w * 0.06f, cy + h * 0.48f, cx + w * 0.02f, cy + h * 0.52f, cx - w * 0.03f, cy + h * 0.48f)
            cubicTo(cx - w * 0.06f, cy + h * 0.38f, cx - w * 0.08f, cy + h * 0.28f, cx - w * 0.04f, cy + h * 0.22f)
            close()
        } else {
            val shWidth = if (isMale) w * 0.23f else w * 0.17f
            val wstWidth = if (isMale) w * 0.09f else w * 0.07f
            val hpWidth = if (isMale) w * 0.11f else w * 0.14f
            
            moveTo(cx, cy - h * 0.42f)
            cubicTo(cx - w * 0.06f, cy - h * 0.42f, cx - w * 0.06f, cy - h * 0.34f, cx - w * 0.02f, cy - h * 0.33f)
            lineTo(cx - w * 0.04f, cy - h * 0.28f)
            cubicTo(cx - w * 0.10f, cy - h * 0.28f, cx - shWidth, cy - h * 0.24f, cx - shWidth, cy - h * 0.20f)
            cubicTo(cx - shWidth - w * 0.04f, cy - h * 0.10f, cx - shWidth - w * 0.02f, cy + h * 0.02f, cx - shWidth + w * 0.02f, cy + h * 0.12f)
            lineTo(cx - shWidth + w * 0.04f, cy + h * 0.12f)
            lineTo(cx - shWidth + w * 0.03f, cy)
            cubicTo(cx - shWidth + w * 0.05f, cy - h * 0.10f, cx - wstWidth - w * 0.02f, cy - h * 0.05f, cx - wstWidth, cy)
            cubicTo(cx - wstWidth, cy + h * 0.06f, cx - hpWidth, cy + h * 0.08f, cx - hpWidth, cy + h * 0.12f)
            cubicTo(cx - hpWidth - w * 0.02f, cy + h * 0.22f, cx - hpWidth + w * 0.01f, cy + h * 0.32f, cx - hpWidth + w * 0.02f, cy + h * 0.45f)
            lineTo(cx - hpWidth + w * 0.06f, cy + h * 0.45f)
            cubicTo(cx - hpWidth + w * 0.05f, cy + h * 0.32f, cx - w * 0.01f, cy + h * 0.22f, cx, cy + h * 0.14f)
            cubicTo(cx + w * 0.01f, cy + h * 0.22f, cx + hpWidth - w * 0.05f, cy + h * 0.32f, cx + hpWidth - w * 0.06f, cy + h * 0.45f)
            lineTo(cx + hpWidth - w * 0.02f, cy + h * 0.45f)
            cubicTo(cx + hpWidth - w * 0.01f, cy + h * 0.32f, cx + hpWidth + w * 0.02f, cy + h * 0.22f, cx + hpWidth, cy + h * 0.12f)
            cubicTo(cx + hpWidth, cy + h * 0.08f, cx + wstWidth, cy + h * 0.06f, cx + wstWidth, cy)
            cubicTo(cx + wstWidth + w * 0.02f, cy - h * 0.05f, cx + shWidth - w * 0.05f, cy - h * 0.10f, cx + shWidth - w * 0.03f, cy)
            lineTo(cx + shWidth - w * 0.04f, cy + h * 0.12f)
            lineTo(cx + shWidth - w * 0.02f, cy + h * 0.12f)
            cubicTo(cx + shWidth + w * 0.02f, cy + h * 0.02f, cx + shWidth + w * 0.04f, cy - h * 0.10f, cx + shWidth, cy - h * 0.20f)
            cubicTo(cx + shWidth, cy - h * 0.24f, cx + w * 0.10f, cy - h * 0.28f, cx + w * 0.04f, cy - h * 0.28f)
            lineTo(cx + w * 0.02f, cy - h * 0.33f)
            cubicTo(cx + w * 0.06f, cy - h * 0.34f, cx + w * 0.06f, cy - h * 0.42f, cx, cy - h * 0.42f)
            close()
        }
    }

    val shadowColor = Color(0xFF0F0F0F)
    val bodyBaseColor = Color(0xFF262626)
    drawScope.drawPath(
        path = bodyOutline,
        brush = Brush.linearGradient(
            colors = listOf(bodyBaseColor, shadowColor),
            start = Offset(cx, cy - h * 0.4f),
            end = Offset(cx, cy + h * 0.4f)
        )
    )
    drawScope.drawPath(
        path = bodyOutline,
        color = Color(0xFF424242).copy(alpha = 0.5f),
        style = Stroke(1.5f)
    )

    if (isSideView) {
        // --- HIGH DEFINITION 3D SIDE VIEW ---
        drawScope.drawCircle(color = Color(0xFF1E1E1E), radius = h * 0.06f, center = Offset(cx, cy - h * 0.35f))
        drawScope.drawCircle(color = Color(0xFF353535), radius = h * 0.06f, center = Offset(cx, cy - h * 0.35f), style = Stroke(1f))

        val sideDelt = Path().apply {
            addOval(Rect(Offset(cx - w * 0.05f, cy - h * 0.24f), Size(w * 0.12f, h * 0.09f)))
        }
        val isDelt = primaryGroup.contains(MuscleGroup.SIDE_DELT) || primaryGroup.contains(MuscleGroup.FRONT_DELT) || primaryGroup.contains(MuscleGroup.REAR_DELT)
        val deltGroup = if (primaryGroup.contains(MuscleGroup.SIDE_DELT)) MuscleGroup.SIDE_DELT else if (primaryGroup.contains(MuscleGroup.FRONT_DELT)) MuscleGroup.FRONT_DELT else MuscleGroup.REAR_DELT
        drawScope.drawPath(sideDelt, brush = getMuscleBrush(deltGroup, Offset(cx, cy - h * 0.2f), w * 0.1f))
        drawScope.drawPath(sideDelt, color = getMuscleOutline(deltGroup), style = Stroke(1.5f))

        val sideChest = Path().apply {
            moveTo(cx - w * 0.04f, cy - h * 0.18f)
            quadraticTo(cx - w * 0.14f, cy - h * 0.14f, cx - w * 0.08f, cy - h * 0.08f)
            lineTo(cx - w * 0.02f, cy - h * 0.1f)
            close()
        }
        val chestGroup = if (primaryGroup.contains(MuscleGroup.UPPER_PEC)) MuscleGroup.UPPER_PEC else MuscleGroup.MID_PEC
        drawScope.drawPath(sideChest, brush = getMuscleBrush(chestGroup, Offset(cx - w * 0.08f, cy - h * 0.13f), w * 0.1f))
        drawScope.drawPath(sideChest, color = getMuscleOutline(chestGroup), style = Stroke(1.5f))

        val sideAbs = Path().apply {
            moveTo(cx - w * 0.03f, cy - h * 0.08f)
            lineTo(cx - w * 0.07f, cy - h * 0.06f)
            quadraticTo(cx - w * 0.08f, cy + h * 0.08f, cx - w * 0.02f, cy + h * 0.1f)
            lineTo(cx, cy + h * 0.06f)
            close()
        }
        val absGroup = if (primaryGroup.contains(MuscleGroup.OBLIQUES)) MuscleGroup.OBLIQUES else MuscleGroup.UPPER_ABS
        drawScope.drawPath(sideAbs, brush = getMuscleBrush(absGroup, Offset(cx - w * 0.05f, cy), w * 0.1f))
        drawScope.drawPath(sideAbs, color = getMuscleOutline(absGroup), style = Stroke(1.5f))

        val sideGlute = Path().apply {
            moveTo(cx + w * 0.02f, cy + h * 0.12f)
            cubicTo(cx + w * 0.14f, cy + h * 0.14f, cx + w * 0.12f, cy + h * 0.28f, cx + w * 0.02f, cy + h * 0.26f)
            close()
        }
        drawScope.drawPath(sideGlute, brush = getMuscleBrush(MuscleGroup.GLUTES, Offset(cx + w * 0.08f, cy + h * 0.2f), w * 0.12f))
        drawScope.drawPath(sideGlute, color = getMuscleOutline(MuscleGroup.GLUTES), style = Stroke(1.5f))

        val sideQuad = Path().apply {
            moveTo(cx - w * 0.02f, cy + h * 0.14f)
            cubicTo(cx - w * 0.11f, cy + h * 0.18f, cx - w * 0.08f, cy + h * 0.35f, cx - w * 0.01f, cy + h * 0.34f)
            lineTo(cx + w * 0.01f, cy + h * 0.24f)
            close()
        }
        drawScope.drawPath(sideQuad, brush = getMuscleBrush(MuscleGroup.QUADS, Offset(cx - w * 0.05f, cy + h * 0.24f), w * 0.12f))
        drawScope.drawPath(sideQuad, color = getMuscleOutline(MuscleGroup.QUADS), style = Stroke(1.5f))

        val sideHam = Path().apply {
            moveTo(cx + w * 0.01f, cy + h * 0.24f)
            lineTo(cx - w * 0.01f, cy + h * 0.34f)
            cubicTo(cx + w * 0.06f, cy + h * 0.36f, cx + w * 0.10f, cy + h * 0.26f, cx + w * 0.02f, cy + h * 0.22f)
            close()
        }
        drawScope.drawPath(sideHam, brush = getMuscleBrush(MuscleGroup.HAMSTRINGS, Offset(cx + w * 0.05f, cy + h * 0.3f), w * 0.12f))
        drawScope.drawPath(sideHam, color = getMuscleOutline(MuscleGroup.HAMSTRINGS), style = Stroke(1.5f))

        val sideCalf = Path().apply {
            moveTo(cx - w * 0.01f, cy + h * 0.34f)
            cubicTo(cx + w * 0.08f, cy + h * 0.38f, cx + w * 0.06f, cy + h * 0.44f, cx, cy + h * 0.44f)
            lineTo(cx - w * 0.02f, cy + h * 0.44f)
            close()
        }
        drawScope.drawPath(sideCalf, brush = getMuscleBrush(MuscleGroup.CALVES_GASTROC, Offset(cx + w * 0.03f, cy + h * 0.39f), w * 0.1f))
        drawScope.drawPath(sideCalf, color = getMuscleOutline(MuscleGroup.CALVES_GASTROC), style = Stroke(1.5f))

        return
    }

    val neckY = cy - h * 0.28f
    val shoulderL = Offset(cx - (if (isMale) w * 0.23f else w * 0.17f), cy - h * 0.20f)
    val shoulderR = Offset(cx + (if (isMale) w * 0.23f else w * 0.17f), cy - h * 0.20f)

    if (!isBackView) {
        // --- HIGH DEFINITION 3D FRONT VIEW ---
        val shW = if (isMale) w * 0.18f else w * 0.13f

        // Left Upper Chest
        val lPecUp = Path().apply {
            moveTo(cx, cy - h * 0.18f)
            lineTo(cx - shW, cy - h * 0.18f)
            quadraticTo(cx - shW * 0.9f, cy - h * 0.12f, cx - w * 0.02f, cy - h * 0.13f)
            lineTo(cx, cy - h * 0.13f)
            close()
        }
        val pecUpBrush = getMuscleBrush(MuscleGroup.UPPER_PEC, Offset(cx - shW / 2f, cy - h * 0.15f), shW)
        drawScope.drawPath(lPecUp, brush = pecUpBrush)
        drawScope.drawPath(lPecUp, color = getMuscleOutline(MuscleGroup.UPPER_PEC), style = Stroke(1.5f))
        drawMuscleFibers(lPecUp, MuscleGroup.UPPER_PEC, Offset(cx - shW, cy - h * 0.18f), Offset(cx, cy - h * 0.13f))

        // Right Upper Chest
        val rPecUp = Path().apply {
            moveTo(cx, cy - h * 0.18f)
            lineTo(cx + shW, cy - h * 0.18f)
            quadraticTo(cx + shW * 0.9f, cy - h * 0.12f, cx + w * 0.02f, cy - h * 0.13f)
            lineTo(cx, cy - h * 0.13f)
            close()
        }
        val pecUpBrushR = getMuscleBrush(MuscleGroup.UPPER_PEC, Offset(cx + shW / 2f, cy - h * 0.15f), shW)
        drawScope.drawPath(rPecUp, brush = pecUpBrushR)
        drawScope.drawPath(rPecUp, color = getMuscleOutline(MuscleGroup.UPPER_PEC), style = Stroke(1.5f))
        drawMuscleFibers(rPecUp, MuscleGroup.UPPER_PEC, Offset(cx + shW, cy - h * 0.18f), Offset(cx, cy - h * 0.13f))

        // Left Mid Chest
        val lPecMid = Path().apply {
            moveTo(cx, cy - h * 0.13f)
            lineTo(cx - shW * 0.95f, cy - h * 0.12f)
            quadraticTo(cx - shW * 0.85f, cy - h * 0.06f, cx - w * 0.02f, cy - h * 0.07f)
            lineTo(cx, cy - h * 0.07f)
            close()
        }
        val pecMidBrush = getMuscleBrush(MuscleGroup.MID_PEC, Offset(cx - shW / 2f, cy - h * 0.1f), shW)
        drawScope.drawPath(lPecMid, brush = pecMidBrush)
        drawScope.drawPath(lPecMid, color = getMuscleOutline(MuscleGroup.MID_PEC), style = Stroke(1.5f))
        drawMuscleFibers(lPecMid, MuscleGroup.MID_PEC, Offset(cx - shW, cy - h * 0.12f), Offset(cx, cy - h * 0.07f))

        // Right Mid Chest
        val rPecMid = Path().apply {
            moveTo(cx, cy - h * 0.13f)
            lineTo(cx + shW * 0.95f, cy - h * 0.12f)
            quadraticTo(cx + shW * 0.85f, cy - h * 0.06f, cx + w * 0.02f, cy - h * 0.07f)
            lineTo(cx, cy - h * 0.07f)
            close()
        }
        val pecMidBrushR = getMuscleBrush(MuscleGroup.MID_PEC, Offset(cx + shW / 2f, cy - h * 0.1f), shW)
        drawScope.drawPath(rPecMid, brush = pecMidBrushR)
        drawScope.drawPath(rPecMid, color = getMuscleOutline(MuscleGroup.MID_PEC), style = Stroke(1.5f))
        drawMuscleFibers(rPecMid, MuscleGroup.MID_PEC, Offset(cx + shW, cy - h * 0.12f), Offset(cx, cy - h * 0.07f))

        // Left Lower Chest
        val lPecLow = Path().apply {
            moveTo(cx, cy - h * 0.07f)
            lineTo(cx - shW * 0.85f, cy - h * 0.06f)
            quadraticTo(cx - shW * 0.7f, cy - h * 0.02f, cx - w * 0.02f, cy - h * 0.03f)
            lineTo(cx, cy - h * 0.03f)
            close()
        }
        val pecLowBrush = getMuscleBrush(MuscleGroup.LOWER_PEC, Offset(cx - shW / 2f, cy - h * 0.05f), shW)
        drawScope.drawPath(lPecLow, brush = pecLowBrush)
        drawScope.drawPath(lPecLow, color = getMuscleOutline(MuscleGroup.LOWER_PEC), style = Stroke(1.5f))
        drawMuscleFibers(lPecLow, MuscleGroup.LOWER_PEC, Offset(cx - shW, cy - h * 0.06f), Offset(cx, cy - h * 0.03f))

        // Right Lower Chest
        val rPecLow = Path().apply {
            moveTo(cx, cy - h * 0.07f)
            lineTo(cx + shW * 0.85f, cy - h * 0.06f)
            quadraticTo(cx + shW * 0.7f, cy - h * 0.02f, cx + w * 0.02f, cy - h * 0.03f)
            lineTo(cx, cy - h * 0.03f)
            close()
        }
        val pecLowBrushR = getMuscleBrush(MuscleGroup.LOWER_PEC, Offset(cx + shW / 2f, cy - h * 0.05f), shW)
        drawScope.drawPath(rPecLow, brush = pecLowBrushR)
        drawScope.drawPath(rPecLow, color = getMuscleOutline(MuscleGroup.LOWER_PEC), style = Stroke(1.5f))
        drawMuscleFibers(rPecLow, MuscleGroup.LOWER_PEC, Offset(cx + shW, cy - h * 0.06f), Offset(cx, cy - h * 0.03f))

        // Abdominals (Upper Packs)
        val absW = if (isMale) w * 0.075f else w * 0.055f
        val upperAbsRectL = Rect(cx - absW - 2f, cy - h * 0.02f, cx - 2f, cy + h * 0.03f)
        val upperAbsRectR = Rect(cx + 2f, cy - h * 0.02f, cx + absW + 2f, cy + h * 0.03f)
        
        drawScope.drawRoundRect(
            brush = getMuscleBrush(MuscleGroup.UPPER_ABS, upperAbsRectL.center, absW),
            topLeft = upperAbsRectL.topLeft,
            size = upperAbsRectL.size,
            cornerRadius = CornerRadius(6f)
        )
        drawScope.drawRoundRect(
            color = getMuscleOutline(MuscleGroup.UPPER_ABS),
            topLeft = upperAbsRectL.topLeft,
            size = upperAbsRectL.size,
            cornerRadius = CornerRadius(6f),
            style = Stroke(1.5f)
        )

        drawScope.drawRoundRect(
            brush = getMuscleBrush(MuscleGroup.UPPER_ABS, upperAbsRectR.center, absW),
            topLeft = upperAbsRectR.topLeft,
            size = upperAbsRectR.size,
            cornerRadius = CornerRadius(6f)
        )
        drawScope.drawRoundRect(
            color = getMuscleOutline(MuscleGroup.UPPER_ABS),
            topLeft = upperAbsRectR.topLeft,
            size = upperAbsRectR.size,
            cornerRadius = CornerRadius(6f),
            style = Stroke(1.5f)
        )

        // Abdominals (Lower Packs)
        val lowerAbsRectL = Rect(cx - absW - 2f, cy + h * 0.04f, cx - 2f, cy + h * 0.09f)
        val lowerAbsRectR = Rect(cx + 2f, cy + h * 0.04f, cx + absW + 2f, cy + h * 0.09f)

        drawScope.drawRoundRect(
            brush = getMuscleBrush(MuscleGroup.LOWER_ABS, lowerAbsRectL.center, absW),
            topLeft = lowerAbsRectL.topLeft,
            size = lowerAbsRectL.size,
            cornerRadius = CornerRadius(6f)
        )
        drawScope.drawRoundRect(
            color = getMuscleOutline(MuscleGroup.LOWER_ABS),
            topLeft = lowerAbsRectL.topLeft,
            size = lowerAbsRectL.size,
            cornerRadius = CornerRadius(6f),
            style = Stroke(1.5f)
        )

        drawScope.drawRoundRect(
            brush = getMuscleBrush(MuscleGroup.LOWER_ABS, lowerAbsRectR.center, absW),
            topLeft = lowerAbsRectR.topLeft,
            size = lowerAbsRectR.size,
            cornerRadius = CornerRadius(6f)
        )
        drawScope.drawRoundRect(
            color = getMuscleOutline(MuscleGroup.LOWER_ABS),
            topLeft = lowerAbsRectR.topLeft,
            size = lowerAbsRectR.size,
            cornerRadius = CornerRadius(6f),
            style = Stroke(1.5f)
        )

        // Obliques
        val lOblique = Path().apply {
            moveTo(cx - absW - 4f, cy - h * 0.02f)
            lineTo(cx - (if (isMale) w * 0.13f else w * 0.10f), cy)
            lineTo(cx - (if (isMale) w * 0.11f else w * 0.09f), cy + h * 0.10f)
            lineTo(cx - absW - 4f, cy + h * 0.04f)
            close()
        }
        val obBrushL = getMuscleBrush(MuscleGroup.OBLIQUES, Offset(cx - w * 0.09f, cy + h * 0.04f), w * 0.07f)
        drawScope.drawPath(lOblique, brush = obBrushL)
        drawScope.drawPath(lOblique, color = getMuscleOutline(MuscleGroup.OBLIQUES), style = Stroke(1.5f))

        val rOblique = Path().apply {
            moveTo(cx + absW + 4f, cy - h * 0.02f)
            lineTo(cx + (if (isMale) w * 0.13f else w * 0.10f), cy)
            lineTo(cx + (if (isMale) w * 0.11f else w * 0.09f), cy + h * 0.10f)
            lineTo(cx + absW + 4f, cy + h * 0.04f)
            close()
        }
        val obBrushR = getMuscleBrush(MuscleGroup.OBLIQUES, Offset(cx + w * 0.09f, cy + h * 0.04f), w * 0.07f)
        drawScope.drawPath(rOblique, brush = obBrushR)
        drawScope.drawPath(rOblique, color = getMuscleOutline(MuscleGroup.OBLIQUES), style = Stroke(1.5f))

        // Front Delts
        val lFrontDelt = Path().apply {
            addOval(Rect(Offset(shoulderL.x - deltRadius, shoulderL.y - deltRadius), Size(deltRadius * 2, deltRadius * 2)))
        }
        val fdBrushL = getMuscleBrush(MuscleGroup.FRONT_DELT, shoulderL, deltRadius)
        drawScope.drawPath(lFrontDelt, brush = fdBrushL)
        drawScope.drawPath(lFrontDelt, color = getMuscleOutline(MuscleGroup.FRONT_DELT), style = Stroke(1.5f))

        val rFrontDelt = Path().apply {
            addOval(Rect(Offset(shoulderR.x - deltRadius, shoulderR.y - deltRadius), Size(deltRadius * 2, deltRadius * 2)))
        }
        val fdBrushR = getMuscleBrush(MuscleGroup.FRONT_DELT, shoulderR, deltRadius)
        drawScope.drawPath(rFrontDelt, brush = fdBrushR)
        drawScope.drawPath(rFrontDelt, color = getMuscleOutline(MuscleGroup.FRONT_DELT), style = Stroke(1.5f))

        // Side Delts
        val sdOffsetL = Offset(shoulderL.x - deltRadius * 0.8f, shoulderL.y + deltRadius * 0.5f)
        val lSideDelt = Path().apply {
            addOval(Rect(Offset(sdOffsetL.x - deltRadius * 0.6f, sdOffsetL.y - deltRadius * 0.9f), Size(deltRadius * 1.2f, deltRadius * 1.8f)))
        }
        drawScope.drawPath(lSideDelt, brush = getMuscleBrush(MuscleGroup.SIDE_DELT, sdOffsetL, deltRadius))
        drawScope.drawPath(lSideDelt, color = getMuscleOutline(MuscleGroup.SIDE_DELT), style = Stroke(1.5f))

        val sdOffsetR = Offset(shoulderR.x + deltRadius * 0.8f, shoulderR.y + deltRadius * 0.5f)
        val rSideDelt = Path().apply {
            addOval(Rect(Offset(sdOffsetR.x - deltRadius * 0.6f, sdOffsetR.y - deltRadius * 0.9f), Size(deltRadius * 1.2f, deltRadius * 1.8f)))
        }
        drawScope.drawPath(rSideDelt, brush = getMuscleBrush(MuscleGroup.SIDE_DELT, sdOffsetR, deltRadius))
        drawScope.drawPath(rSideDelt, color = getMuscleOutline(MuscleGroup.SIDE_DELT), style = Stroke(1.5f))

        // Biceps
        val armLStart = Offset(shoulderL.x - deltRadius * 0.2f, shoulderL.y + deltRadius * 1.2f)
        val lBicep = Path().apply {
            moveTo(armLStart.x, armLStart.y)
            quadraticTo(armLStart.x - (if (isMale) w * 0.05f else w * 0.035f), armLStart.y + h * 0.06f, armLStart.x, armLStart.y + h * 0.12f)
            lineTo(armLStart.x + w * 0.03f, armLStart.y + h * 0.06f)
            close()
        }
        val bicepBrushL = getMuscleBrush(MuscleGroup.BICEPS, armLStart, w * 0.07f)
        drawScope.drawPath(lBicep, brush = bicepBrushL)
        drawScope.drawPath(lBicep, color = getMuscleOutline(MuscleGroup.BICEPS), style = Stroke(1.5f))
        drawMuscleFibers(lBicep, MuscleGroup.BICEPS, armLStart, Offset(armLStart.x, armLStart.y + h * 0.12f))

        val armRStart = Offset(shoulderR.x + deltRadius * 0.2f, shoulderR.y + deltRadius * 1.2f)
        val rBicep = Path().apply {
            moveTo(armRStart.x, armRStart.y)
            quadraticTo(armRStart.x + (if (isMale) w * 0.05f else w * 0.035f), armRStart.y + h * 0.06f, armRStart.x, armRStart.y + h * 0.12f)
            lineTo(armRStart.x - w * 0.03f, armRStart.y + h * 0.06f)
            close()
        }
        val bicepBrushR = getMuscleBrush(MuscleGroup.BICEPS, armRStart, w * 0.07f)
        drawScope.drawPath(rBicep, brush = bicepBrushR)
        drawScope.drawPath(rBicep, color = getMuscleOutline(MuscleGroup.BICEPS), style = Stroke(1.5f))
        drawMuscleFibers(rBicep, MuscleGroup.BICEPS, armRStart, Offset(armRStart.x, armRStart.y + h * 0.12f))

        // Forearms & Brachialis
        val lForearm = Path().apply {
            moveTo(armLStart.x - w * 0.01f, armLStart.y + h * 0.12f)
            lineTo(armLStart.x - (if (isMale) w * 0.03f else w * 0.02f), armLStart.y + h * 0.24f)
            lineTo(armLStart.x + w * 0.01f, armLStart.y + h * 0.23f)
            lineTo(armLStart.x + w * 0.02f, armLStart.y + h * 0.12f)
            close()
        }
        drawScope.drawPath(lForearm, brush = getMuscleBrush(MuscleGroup.BRACHIALIS_FOREARMS, armLStart, w * 0.08f))
        drawScope.drawPath(lForearm, color = getMuscleOutline(MuscleGroup.BRACHIALIS_FOREARMS), style = Stroke(1.5f))

        val rForearm = Path().apply {
            moveTo(armRStart.x + w * 0.01f, armRStart.y + h * 0.12f)
            lineTo(armRStart.x + (if (isMale) w * 0.03f else w * 0.02f), armRStart.y + h * 0.24f)
            lineTo(armRStart.x - w * 0.01f, armRStart.y + h * 0.23f)
            lineTo(armRStart.x - w * 0.02f, armRStart.y + h * 0.12f)
            close()
        }
        drawScope.drawPath(rForearm, brush = getMuscleBrush(MuscleGroup.BRACHIALIS_FOREARMS, armRStart, w * 0.08f))
        drawScope.drawPath(rForearm, color = getMuscleOutline(MuscleGroup.BRACHIALIS_FOREARMS), style = Stroke(1.5f))

        // Quadriceps
        val legLY = cy + h * 0.11f
        val legLWidth = if (isMale) w * 0.09f else w * 0.075f
        
        val lQuad = Path().apply {
            moveTo(cx - (if (isMale) w * 0.12f else w * 0.13f), legLY)
            lineTo(cx - (if (isMale) w * 0.14f else w * 0.13f), legLY + h * 0.16f)
            cubicTo(cx - w * 0.08f, legLY + h * 0.18f, cx - w * 0.03f, legLY + h * 0.15f, cx - w * 0.02f, legLY)
            close()
        }
        val quadBrushL = getMuscleBrush(MuscleGroup.QUADS, Offset(cx - w * 0.08f, legLY + h * 0.08f), legLWidth * 2f)
        drawScope.drawPath(lQuad, brush = quadBrushL)
        drawScope.drawPath(lQuad, color = getMuscleOutline(MuscleGroup.QUADS), style = Stroke(1.5f))
        drawMuscleFibers(lQuad, MuscleGroup.QUADS, Offset(cx - w * 0.12f, legLY), Offset(cx - w * 0.05f, legLY + h * 0.16f))

        val rQuad = Path().apply {
            moveTo(cx + (if (isMale) w * 0.12f else w * 0.13f), legLY)
            lineTo(cx + (if (isMale) w * 0.14f else w * 0.13f), legLY + h * 0.16f)
            cubicTo(cx + w * 0.08f, legLY + h * 0.18f, cx + w * 0.03f, legLY + h * 0.15f, cx + w * 0.02f, legLY)
            close()
        }
        val quadBrushR = getMuscleBrush(MuscleGroup.QUADS, Offset(cx + w * 0.08f, legLY + h * 0.08f), legLWidth * 2f)
        drawScope.drawPath(rQuad, brush = quadBrushR)
        drawScope.drawPath(rQuad, color = getMuscleOutline(MuscleGroup.QUADS), style = Stroke(1.5f))
        drawMuscleFibers(rQuad, MuscleGroup.QUADS, Offset(cx + w * 0.12f, legLY), Offset(cx + w * 0.05f, legLY + h * 0.16f))

    } else {
        // --- HIGH DEFINITION 3D BACK VIEW ---
        val upperTraps = Path().apply {
            moveTo(cx, cy - h * 0.26f)
            lineTo(cx - (if (isMale) w * 0.12f else w * 0.09f), cy - h * 0.22f)
            lineTo(cx, cy - h * 0.17f)
            lineTo(cx + (if (isMale) w * 0.12f else w * 0.09f), cy - h * 0.22f)
            close()
        }
        drawScope.drawPath(upperTraps, brush = getMuscleBrush(MuscleGroup.UPPER_TRAPS, Offset(cx, cy - h * 0.22f), w * 0.1f))
        drawScope.drawPath(upperTraps, color = getMuscleOutline(MuscleGroup.UPPER_TRAPS), style = Stroke(1.5f))

        val midTraps = Path().apply {
            moveTo(cx, cy - h * 0.17f)
            lineTo(cx - w * 0.08f, cy - h * 0.14f)
            lineTo(cx - w * 0.04f, cy - h * 0.05f)
            lineTo(cx + w * 0.04f, cy - h * 0.05f)
            lineTo(cx + w * 0.08f, cy - h * 0.14f)
            close()
        }
        drawScope.drawPath(midTraps, brush = getMuscleBrush(MuscleGroup.MID_TRAPS_RHOMBOIDS, Offset(cx, cy - h * 0.11f), w * 0.1f))
        drawScope.drawPath(midTraps, color = getMuscleOutline(MuscleGroup.MID_TRAPS_RHOMBOIDS), style = Stroke(1.5f))

        val latW = if (isMale) w * 0.17f else w * 0.12f
        val lLatUp = Path().apply {
            moveTo(cx - w * 0.04f, cy - h * 0.11f)
            lineTo(cx - latW, cy - h * 0.11f)
            quadraticTo(cx - latW * 0.9f, cy - h * 0.03f, cx - w * 0.03f, cy - h * 0.02f)
            close()
        }
        drawScope.drawPath(lLatUp, brush = getMuscleBrush(MuscleGroup.UPPER_LATS, Offset(cx - latW / 2f, cy - h * 0.06f), latW))
        drawScope.drawPath(lLatUp, color = getMuscleOutline(MuscleGroup.UPPER_LATS), style = Stroke(1.5f))
        drawMuscleFibers(lLatUp, MuscleGroup.UPPER_LATS, Offset(cx - latW, cy - h * 0.11f), Offset(cx, cy - h * 0.02f))

        val rLatUp = Path().apply {
            moveTo(cx + w * 0.04f, cy - h * 0.11f)
            lineTo(cx + latW, cy - h * 0.11f)
            quadraticTo(cx + latW * 0.9f, cy - h * 0.03f, cx + w * 0.03f, cy - h * 0.02f)
            close()
        }
        drawScope.drawPath(rLatUp, brush = getMuscleBrush(MuscleGroup.UPPER_LATS, Offset(cx + latW / 2f, cy - h * 0.06f), latW))
        drawScope.drawPath(rLatUp, color = getMuscleOutline(MuscleGroup.UPPER_LATS), style = Stroke(1.5f))
        drawMuscleFibers(rLatUp, MuscleGroup.UPPER_LATS, Offset(cx + latW, cy - h * 0.11f), Offset(cx, cy - h * 0.02f))

        val lLatLow = Path().apply {
            moveTo(cx - w * 0.03f, cy - h * 0.02f)
            lineTo(cx - latW * 0.6f, cy - h * 0.03f)
            lineTo(cx - latW * 0.4f, cy + h * 0.05f)
            lineTo(cx - w * 0.02f, cy + h * 0.03f)
            close()
        }
        drawScope.drawPath(lLatLow, brush = getMuscleBrush(MuscleGroup.LOWER_LATS, Offset(cx - w * 0.05f, cy), w * 0.07f))
        drawScope.drawPath(lLatLow, color = getMuscleOutline(MuscleGroup.LOWER_LATS), style = Stroke(1.5f))

        val rLatLow = Path().apply {
            moveTo(cx + w * 0.03f, cy - h * 0.02f)
            lineTo(cx + latW * 0.6f, cy - h * 0.03f)
            lineTo(cx + latW * 0.4f, cy + h * 0.05f)
            lineTo(cx + w * 0.02f, cy + h * 0.03f)
            close()
        }
        drawScope.drawPath(rLatLow, brush = getMuscleBrush(MuscleGroup.LOWER_LATS, Offset(cx + w * 0.05f, cy), w * 0.07f))
        drawScope.drawPath(rLatLow, color = getMuscleOutline(MuscleGroup.LOWER_LATS), style = Stroke(1.5f))

        val erectors = Path().apply {
            moveTo(cx - w * 0.03f, cy - h * 0.02f)
            lineTo(cx - w * 0.04f, cy + h * 0.1f)
            lineTo(cx + w * 0.04f, cy + h * 0.1f)
            lineTo(cx + w * 0.03f, cy - h * 0.02f)
            close()
        }
        drawScope.drawPath(erectors, brush = getMuscleBrush(MuscleGroup.ERECTOR_SPINAE, Offset(cx, cy + h * 0.04f), w * 0.05f))
        drawScope.drawPath(erectors, color = getMuscleOutline(MuscleGroup.ERECTOR_SPINAE), style = Stroke(1.5f))

        val lRearDelt = Path().apply {
            addOval(Rect(Offset(shoulderL.x - deltRadius, shoulderL.y - deltRadius), Size(deltRadius * 2, deltRadius * 2)))
        }
        drawScope.drawPath(lRearDelt, brush = getMuscleBrush(MuscleGroup.REAR_DELT, shoulderL, deltRadius))
        drawScope.drawPath(lRearDelt, color = getMuscleOutline(MuscleGroup.REAR_DELT), style = Stroke(1.5f))

        val rRearDelt = Path().apply {
            addOval(Rect(Offset(shoulderR.x - deltRadius, shoulderR.y - deltRadius), Size(deltRadius * 2, deltRadius * 2)))
        }
        drawScope.drawPath(rRearDelt, brush = getMuscleBrush(MuscleGroup.REAR_DELT, shoulderR, deltRadius))
        drawScope.drawPath(rRearDelt, color = getMuscleOutline(MuscleGroup.REAR_DELT), style = Stroke(1.5f))

        val armLStart = Offset(shoulderL.x - deltRadius * 0.2f, shoulderL.y + deltRadius * 1.2f)
        val lTricep = Path().apply {
            moveTo(armLStart.x, armLStart.y)
            quadraticTo(armLStart.x - (if (isMale) w * 0.04f else w * 0.03f), armLStart.y + h * 0.06f, armLStart.x, armLStart.y + h * 0.12f)
            lineTo(armLStart.x + w * 0.02f, armLStart.y + h * 0.06f)
            close()
        }
        val isTricepSelected = primaryGroup.contains(MuscleGroup.LONG_TRICEPS) || primaryGroup.contains(MuscleGroup.LATERAL_TRICEPS)
        val tricepBrushL = getMuscleBrush(if (isTricepSelected) MuscleGroup.LONG_TRICEPS else MuscleGroup.LATERAL_TRICEPS, armLStart, w * 0.06f)
        drawScope.drawPath(lTricep, brush = tricepBrushL)
        drawScope.drawPath(lTricep, color = getMuscleOutline(MuscleGroup.LONG_TRICEPS), style = Stroke(1.5f))

        val armRStart = Offset(shoulderR.x + deltRadius * 0.2f, shoulderR.y + deltRadius * 1.2f)
        val rTricep = Path().apply {
            moveTo(armRStart.x, armRStart.y)
            quadraticTo(armRStart.x + (if (isMale) w * 0.04f else w * 0.03f), armRStart.y + h * 0.06f, armRStart.x, armRStart.y + h * 0.12f)
            lineTo(armRStart.x - w * 0.02f, armRStart.y + h * 0.06f)
            close()
        }
        drawScope.drawPath(rTricep, brush = tricepBrushL)
        drawScope.drawPath(rTricep, color = getMuscleOutline(MuscleGroup.LONG_TRICEPS), style = Stroke(1.5f))

        val gluteLY = cy + h * 0.1f
        val gluteW = if (isMale) w * 0.11f else w * 0.14f
        val lGlute = Path().apply {
            moveTo(cx, gluteLY)
            lineTo(cx - gluteW * 0.9f, gluteLY + h * 0.01f)
            quadraticTo(cx - gluteW * 0.8f, gluteLY + h * 0.08f, cx - w * 0.01f, gluteLY + h * 0.07f)
            lineTo(cx, gluteLY + h * 0.05f)
            close()
        }
        drawScope.drawPath(lGlute, brush = getMuscleBrush(MuscleGroup.GLUTES, Offset(cx - gluteW / 2f, gluteLY + h * 0.04f), gluteW))
        drawScope.drawPath(lGlute, color = getMuscleOutline(MuscleGroup.GLUTES), style = Stroke(1.5f))

        val rGlute = Path().apply {
            moveTo(cx, gluteLY)
            lineTo(cx + gluteW * 0.9f, gluteLY + h * 0.01f)
            quadraticTo(cx + gluteW * 0.8f, gluteLY + h * 0.08f, cx + w * 0.01f, gluteLY + h * 0.07f)
            lineTo(cx, gluteLY + h * 0.05f)
            close()
        }
        drawScope.drawPath(rGlute, brush = getMuscleBrush(MuscleGroup.GLUTES, Offset(cx + gluteW / 2f, gluteLY + h * 0.04f), gluteW))
        drawScope.drawPath(rGlute, color = getMuscleOutline(MuscleGroup.GLUTES), style = Stroke(1.5f))

        val hamY = gluteLY + h * 0.08f
        val hamW = if (isMale) w * 0.10f else w * 0.09f
        val lHamstring = Path().apply {
            moveTo(cx - gluteW * 0.85f, hamY)
            lineTo(cx - hamW * 1.1f, hamY + h * 0.12f)
            lineTo(cx - w * 0.02f, hamY + h * 0.12f)
            lineTo(cx - w * 0.01f, hamY)
            close()
        }
        drawScope.drawPath(lHamstring, brush = getMuscleBrush(MuscleGroup.HAMSTRINGS, Offset(cx - hamW / 2f, hamY + h * 0.06f), hamW))
        drawScope.drawPath(lHamstring, color = getMuscleOutline(MuscleGroup.HAMSTRINGS), style = Stroke(1.5f))
        drawMuscleFibers(lHamstring, MuscleGroup.HAMSTRINGS, Offset(cx - hamW, hamY), Offset(cx - w * 0.02f, hamY + h * 0.12f))

        val rHamstring = Path().apply {
            moveTo(cx + gluteW * 0.85f, hamY)
            lineTo(cx + hamW * 1.1f, hamY + h * 0.12f)
            lineTo(cx + w * 0.02f, hamY + h * 0.12f)
            lineTo(cx + w * 0.01f, hamY)
            close()
        }
        drawScope.drawPath(rHamstring, brush = getMuscleBrush(MuscleGroup.HAMSTRINGS, Offset(cx + hamW / 2f, hamY + h * 0.06f), hamW))
        drawScope.drawPath(rHamstring, color = getMuscleOutline(MuscleGroup.HAMSTRINGS), style = Stroke(1.5f))
        drawMuscleFibers(rHamstring, MuscleGroup.HAMSTRINGS, Offset(cx + hamW, hamY), Offset(cx + w * 0.02f, hamY + h * 0.12f))

        val calfY = hamY + h * 0.13f
        val calfW = if (isMale) w * 0.09f else w * 0.08f
        val lGastroc = Path().apply {
            moveTo(cx - hamW * 0.9f, calfY)
            quadraticTo(cx - calfW * 1.3f, calfY + h * 0.06f, cx - calfW * 0.9f, calfY + h * 0.12f)
            lineTo(cx - w * 0.03f, calfY + h * 0.12f)
            quadraticTo(cx - w * 0.04f, calfY + h * 0.06f, cx - w * 0.03f, calfY)
            close()
        }
        drawScope.drawPath(lGastroc, brush = getMuscleBrush(MuscleGroup.CALVES_GASTROC, Offset(cx - calfW / 2f, calfY + h * 0.06f), calfW))
        drawScope.drawPath(lGastroc, color = getMuscleOutline(MuscleGroup.CALVES_GASTROC), style = Stroke(1.5f))

        val rGastroc = Path().apply {
            moveTo(cx + hamW * 0.9f, calfY)
            quadraticTo(cx + calfW * 1.3f, calfY + h * 0.06f, cx + calfW * 0.9f, calfY + h * 0.12f)
            lineTo(cx + w * 0.03f, calfY + h * 0.12f)
            quadraticTo(cx + w * 0.04f, calfY + h * 0.06f, cx + w * 0.03f, calfY)
            close()
        }
        drawScope.drawPath(rGastroc, brush = getMuscleBrush(MuscleGroup.CALVES_GASTROC, Offset(cx + calfW / 2f, calfY + h * 0.06f), calfW))
        drawScope.drawPath(rGastroc, color = getMuscleOutline(MuscleGroup.CALVES_GASTROC), style = Stroke(1.5f))
    }
}

@Composable
fun DynamicAnatomyVisualizer(
    exerciseName: String,
    category: String,
    drawableRes: Int,
    modifier: Modifier = Modifier
) {
    var showHdAnatomyDialog by remember { mutableStateOf(false) }
    var isMaleModel by remember { mutableStateOf(true) }

    val (primaryHighlights, secondaryHighlights) = remember(exerciseName, category) {
        getBioActivation(exerciseName, category)
    }

    // Determine front/back/side layout automatically
    var viewMode by remember { mutableStateOf("Front") }
    
    LaunchedEffect(exerciseName, primaryHighlights) {
        val nameLower = exerciseName.lowercase(Locale.ROOT)
        viewMode = when {
            nameLower.contains("plank") || nameLower.contains("russian twist") || nameLower.contains("side bend") -> "Side"
            primaryHighlights.any { 
                it == MuscleGroup.UPPER_LATS || 
                it == MuscleGroup.LOWER_LATS || 
                it == MuscleGroup.MID_TRAPS_RHOMBOIDS || 
                it == MuscleGroup.UPPER_TRAPS || 
                it == MuscleGroup.ERECTOR_SPINAE || 
                it == MuscleGroup.REAR_DELT || 
                it == MuscleGroup.LONG_TRICEPS || 
                it == MuscleGroup.HAMSTRINGS || 
                it == MuscleGroup.GLUTES || 
                it == MuscleGroup.CALVES_GASTROC || 
                it == MuscleGroup.CALVES_SOLEUS 
            } -> "Back"
            else -> "Front"
        }
    }

    val isBackView = viewMode == "Back"
    val isSideView = viewMode == "Side"

    val baseOrange = Color(0xFFFF5400)
    val lightOrange = Color(0xFFFFB070)

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Column(
        modifier = modifier
            .background(Color(0xFF0C0C0C), RoundedCornerShape(16.dp))
            .border(1.5.dp, baseOrange.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
    ) {
        // Floating premium selectors Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Side: View Angle buttons
            Row(
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.75f), RoundedCornerShape(8.dp))
                    .border(1.dp, Color.DarkGray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                listOf("Front", "Back", "Side").forEach { mode ->
                    val isSelected = viewMode.lowercase(Locale.ROOT) == mode.lowercase(Locale.ROOT)
                    Box(
                        modifier = Modifier
                            .background(
                                if (isSelected) baseOrange else Color.Transparent,
                                RoundedCornerShape(6.dp)
                            )
                            .clickable { viewMode = mode }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = mode.uppercase(Locale.ROOT),
                            color = if (isSelected) Color.White else Color.Gray,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            // Right Side: Gender selector & Fullscreen Zoom button
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.75f), RoundedCornerShape(8.dp))
                        .border(1.dp, Color.DarkGray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    listOf("MALE" to true, "FEMALE" to false).forEach { (label, isMaleVal) ->
                        val isSelected = isMaleModel == isMaleVal
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isSelected) baseOrange else Color.Transparent,
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable { isMaleModel = isMaleVal }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) Color.White else Color.Gray,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }

                // HD Expand icon
                IconButton(
                    onClick = { showHdAnatomyDialog = true },
                    modifier = Modifier
                        .size(28.dp)
                        .background(Color.Black.copy(alpha = 0.75f), CircleShape)
                        .border(1.dp, Color.DarkGray.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh, // Standard rotate/refresh icon as a proxy or simple asset
                        contentDescription = "HD Fullscreen View",
                        tint = baseOrange,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        // Realistic Human Anatomy Illustration View Box
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1f, 4f)
                        if (scale > 1f) {
                            offset += pan
                        } else {
                            offset = Offset.Zero
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            if (scale > 1f) {
                                scale = 1f
                                offset = Offset.Zero
                            } else {
                                scale = 2f
                            }
                        }
                    )
                }
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                },
            contentAlignment = Alignment.Center
        ) {
            val anatomyImg = getAnatomyDrawable(exerciseName, category, viewMode)
            Image(
                painter = painterResource(id = anatomyImg),
                contentDescription = "Premium Realistic Human Anatomy Illustration",
                modifier = Modifier.fillMaxSize(0.95f),
                contentScale = ContentScale.Fit
            )
        }

        // Bottom details showing Primary / Secondary target muscles
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.85f))
                .border(1.dp, Color.DarkGray.copy(alpha = 0.3f), RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "PRIMARY TARGET",
                        color = baseOrange,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (primaryHighlights.isNotEmpty()) primaryHighlights.joinToString(", ") { it.getFriendlyName() } else "None",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (secondaryHighlights.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "SECONDARY TARGET",
                            color = lightOrange,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = secondaryHighlights.joinToString(", ") { it.getFriendlyName() },
                            color = Color.LightGray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    if (showHdAnatomyDialog) {
        HdAnatomyDialog(
            exerciseName = exerciseName,
            category = category,
            primaryHighlights = primaryHighlights,
            secondaryHighlights = secondaryHighlights,
            isBackView = isBackView,
            isSideView = isSideView,
            baseOrange = baseOrange,
            lightOrange = lightOrange,
            onDismiss = { showHdAnatomyDialog = false }
        )
    }
}

@Composable
fun HdAnatomyDialog(
    exerciseName: String,
    category: String,
    primaryHighlights: List<MuscleGroup>,
    secondaryHighlights: List<MuscleGroup>,
    isBackView: Boolean,
    isSideView: Boolean,
    baseOrange: Color,
    lightOrange: Color,
    onDismiss: () -> Unit
) {
    var scale by remember { mutableStateOf(1.2f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var activeViewMode by remember { mutableStateOf(if (isSideView) "Side" else if (isBackView) "Back" else "Front") }

    val contributions = remember(exerciseName, category) {
        getBioContributions(exerciseName, category)
    }

    val coachingTip = remember(exerciseName, category) {
        getBioTip(exerciseName, category)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF060606)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Interactive Zoomable Canvas Area
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                scale = (scale * zoom).coerceIn(1f, 5f)
                                offset = if (scale == 1f) Offset.Zero else offset + pan
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = getAnatomyDrawable(exerciseName, category, activeViewMode)),
                        contentDescription = "Premium Realistic Human Anatomy Illustration",
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .aspectRatio(0.75f)
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                translationX = offset.x
                                translationY = offset.y
                            },
                        contentScale = ContentScale.Fit
                    )
                }

                // Top Header controls
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = exerciseName.uppercase(Locale.ROOT),
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "BIOMECHANICAL TARGETING MAP",
                            color = baseOrange,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Interactive View Switcher
                        IconButton(
                            onClick = {
                                activeViewMode = when (activeViewMode) {
                                    "Front" -> "Back"
                                    "Back" -> "Side"
                                    else -> "Front"
                                }
                            },
                            modifier = Modifier.background(Color.White.copy(alpha = 0.1f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Rotate Body Model",
                                tint = Color.White
                            )
                        }

                        // Close IconButton
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.background(Color.White.copy(alpha = 0.1f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close details",
                                tint = Color.White
                            )
                        }
                    }
                }

                // Current View Text
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 90.dp)
                        .background(baseOrange.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .border(1.dp, baseOrange.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        "${activeViewMode.uppercase()} ANATOMICAL VIEW",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                }

                // Bottom Slide-up Activation details sheet panel
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .padding(16.dp)
                        .background(Color(0xFF0C0C0C), RoundedCornerShape(16.dp))
                        .border(1.dp, baseOrange.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "MUSCLE ACTIVATION BREAKDOWN",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    contributions.forEach { contribution ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(if (contribution.isPrimary) baseOrange else lightOrange, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = contribution.name,
                                    color = Color.LightGray,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Text(
                                text = "${contribution.percentage}%",
                                color = if (contribution.isPrimary) baseOrange else lightOrange,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Percentage activation progress bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .background(Color(0xFF1E1E1E), RoundedCornerShape(3.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(contribution.percentage / 100f)
                                    .fillMaxHeight()
                                    .background(
                                        if (contribution.isPrimary) baseOrange else lightOrange,
                                        RoundedCornerShape(3.dp)
                                    )
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Divider(color = Color.DarkGray.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 4.dp))

                    // Biomechanical / Kinesiological Tip Section
                    Text(
                        text = "KINESIOLOGY FORM TIP",
                        color = baseOrange,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = coachingTip,
                        color = Color.Gray,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "ℹ️ Use two-finger gestures to pinch-zoom or drag-pan the clinical model.",
                        color = Color.Gray.copy(alpha = 0.5f),
                        fontSize = 9.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

fun getAnatomyDrawable(exerciseName: String, category: String, viewMode: String): Int {
    val nameLower = exerciseName.lowercase(Locale.ROOT)
    val catLower = category.lowercase(Locale.ROOT)

    return when (viewMode) {
        "Back" -> {
            when {
                nameLower.contains("lat") || nameLower.contains("pulldown") || nameLower.contains("row") || nameLower.contains("pull-down") -> R.drawable.anatomy_back_lats
                nameLower.contains("shrug") || nameLower.contains("trap") -> R.drawable.anatomy_back_traps
                nameLower.contains("tricep") || nameLower.contains("dip") || nameLower.contains("pushdown") || nameLower.contains("kickback") || nameLower.contains("extension") -> R.drawable.anatomy_triceps
                nameLower.contains("calf") || nameLower.contains("raises") -> R.drawable.anatomy_legs_calves
                nameLower.contains("curl") || nameLower.contains("hamstring") || nameLower.contains("deadlift") || nameLower.contains("romanian") -> R.drawable.anatomy_legs_hamstrings
                catLower == "back" -> R.drawable.anatomy_back
                catLower == "legs" -> R.drawable.anatomy_legs
                else -> R.drawable.anatomy_back
            }
        }
        "Side" -> {
            when {
                catLower == "shoulders" || nameLower.contains("delt") || nameLower.contains("raise") || nameLower.contains("press") -> R.drawable.anatomy_shoulders
                catLower == "legs" || nameLower.contains("squat") || nameLower.contains("lunge") || nameLower.contains("leg") -> R.drawable.anatomy_legs
                catLower == "abs" || nameLower.contains("plank") || nameLower.contains("twist") || nameLower.contains("raise") || nameLower.contains("crunch") -> R.drawable.anatomy_abs
                else -> R.drawable.anatomy_shoulders
            }
        }
        else -> { // "Front"
            when {
                catLower == "chest" || nameLower.contains("bench") || nameLower.contains("press") || nameLower.contains("fly") || nameLower.contains("push-up") || nameLower.contains("dip") || nameLower.contains("cable") -> R.drawable.anatomy_chest
                catLower == "abs" || nameLower.contains("crunch") || nameLower.contains("raise") || nameLower.contains("twist") || nameLower.contains("plank") || nameLower.contains("rollout") -> R.drawable.anatomy_abs
                catLower == "shoulders" || nameLower.contains("press") || nameLower.contains("raise") || nameLower.contains("delt") -> R.drawable.anatomy_shoulders
                catLower == "biceps" || nameLower.contains("curl") || nameLower.contains("bicep") || nameLower.contains("chin") || nameLower.contains("hammer") -> R.drawable.anatomy_biceps
                catLower == "legs" || nameLower.contains("squat") || nameLower.contains("leg") || nameLower.contains("lunge") || nameLower.contains("press") || nameLower.contains("extension") -> R.drawable.anatomy_legs_quads
                else -> R.drawable.anatomy_chest
            }
        }
    }
}
