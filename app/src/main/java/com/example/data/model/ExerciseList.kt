package com.example.data.model

object ExerciseList {
    val library: List<Exercise> by lazy {
        val list = mutableListOf<Exercise>()

        // --- CHEST (30 PRODUCTION QUALITY WITH VIDEO INTEGRATION) ---
        val chestNames = listOf(
            // Existing (Renamed/Mapped)
            "Barbell Bench Press" to Triple("Barbell", "Beginner", "Barbell Bench Station"),
            "Barbell Incline Bench Press" to Triple("Barbell", "Intermediate", "Incline Barbell Bench"),
            "Dumbbell Bench Press" to Triple("Dumbbells", "Beginner", "Flat Bench"),
            "Dumbbell Incline Bench Press" to Triple("Dumbbells", "Beginner", "Incline Bench"),
            "Dumbbell Fly" to Triple("Dumbbells", "Intermediate", "Flat Bench"),
            "Push-ups" to Triple("Bodyweight", "Beginner", "Floor setup"),
            "Chest Dips" to Triple("Bodyweight", "Advanced", "Dip Bars"),
            "Cable Standing Fly, Crossover fly" to Triple("Cable Machine", "Intermediate", "High Pulley Crossover"),
            "Cable Lying Fly, Flat Bench Cable Fly" to Triple("Cable Machine", "Intermediate", "Low Pulley Crossover"),
            "Lever Pec Deck Fly" to Triple("Pec Deck Machine", "Intermediate", "Seat adjusted so handles are at chest height"),

            // New Exercises
            "Band High Fly" to Triple("Resistance Band", "Beginner", "Anchor point high level"),
            "Barbell Decline Bench Press" to Triple("Barbell", "Intermediate", "Decline Barbell Bench"),
            "Decline Dumbbell Bench Press (45 degree)" to Triple("Dumbbells", "Intermediate", "Decline Bench 45 degree"),
            "Deep Push-ups" to Triple("Bodyweight", "Intermediate", "Push-up handles or blocks"),
            "Dumbbell Decline Fly (45 degree)" to Triple("Dumbbells", "Intermediate", "Decline Bench 45 degree"),
            "Dumbbell Fly On Exercise Ball" to Triple("Dumbbells", "Intermediate", "Stability Swiss Ball"),
            "Dumbbell Incline Fly On Exercise Ball" to Triple("Dumbbells", "Intermediate", "Stability Swiss Ball"),
            "Dumbbell Incline Fly" to Triple("Dumbbells", "Intermediate", "Incline Bench 30-45 degrees"),
            "Dumbbell Lying Hammer Press" to Triple("Dumbbells", "Intermediate", "Flat Bench"),
            "Incline Push-Ups" to Triple("Bodyweight", "Beginner", "Sturdy bench or box elevation"),
            "Lever Chest Press" to Triple("Lever Machine", "Beginner", "Lever chest press machine"),
            "Lever Incline Hammer Chest Press" to Triple("Lever Machine", "Intermediate", "Lever incline press machine"),
            "Lever Lying Chest Press" to Triple("Lever Machine", "Intermediate", "Lever flat press machine"),
            "Stretching - Above Head Chest Stretch" to Triple("Bodyweight", "Beginner", "Standing upright"),
            "Stretching - Back Pec Stretch" to Triple("Bodyweight", "Beginner", "Standing flat floor"),
            "Stretching - Dynamic Chest Stretch" to Triple("Bodyweight", "Beginner", "Standing flat floor"),
            "Stretching - Kneeling Back Rotation Stretch" to Triple("Bodyweight", "Beginner", "Floor mat area"),
            "Stretching - Standing Wheel Rollout" to Triple("Ab Wheel", "Intermediate", "Floor mat area"),
            "Triceps Dips" to Triple("Bodyweight", "Intermediate", "Dip station bars"),
            "Wide Grip Push-ups" to Triple("Bodyweight", "Beginner", "Floor setup with wide stance")
        )
        chestNames.forEachIndexed { index, pair ->
            val name = pair.first
            val primaryMuscles = when {
                name.contains("Pec Deck") -> listOf("Middle Chest (Pectoralis Major)")
                name.contains("Incline") -> listOf("Upper Pectoralis Major")
                name.contains("Decline") -> listOf("Lower Pectoralis Major")
                name.contains("Dips") -> listOf("Lower Pectoralis Major")
                else -> listOf("Pectoralis Major")
            }
            val secondaryMuscles = when {
                name.contains("Pec Deck") || name.contains("Fly") -> listOf("Front Deltoids")
                name.contains("Push-Up") || name.contains("Push-ups") -> listOf("Triceps", "Anterior Deltoids", "Core")
                name.contains("Dumbbell Fly") -> listOf("Anterior Deltoids", "Biceps")
                name.contains("Stretch") || name.contains("Stretching") -> listOf("Shoulders", "Triceps")
                else -> listOf("Anterior Deltoids", "Triceps")
            }
            val isStretch = name.contains("Stretch") || name.contains("Stretching")
            val instructions = when {
                isStretch -> listOf(
                    "Assume the starting position for the stretch as indicated.",
                    "Slowly extend or rotate to feel a comfortable stretch in your chest muscles.",
                    "Hold the stretch for 20-30 seconds while breathing deeply.",
                    "Slowly return to the starting position and repeat if desired."
                )
                name == "Barbell Bench Press" || name == "Bench Press" -> listOf(
                    "Lie flat on the bench, keep your feet firmly planted on the floor, and grip the bar slightly wider than shoulder width.",
                    "Squeeze your shoulder blades together, unrack the bar, and position it directly over your chest.",
                    "Lower the bar slowly under strict control until it lightly touches your mid-chest.",
                    "Press the bar vertically with power while exhaling, squeezing your chest at the top."
                )
                name == "Barbell Incline Bench Press" || name == "Incline Bench Press" -> listOf(
                    "Lie back on an incline bench set to 30-45 degrees, and grip the barbell slightly wider than shoulder width.",
                    "Unrack the bar and hold it stable directly over your upper collarbone area.",
                    "Slowly lower the bar to your upper chest, keeping your elbows tucked at roughly 45 degrees.",
                    "Push the bar up dynamically back to the starting position while exhaling."
                )
                name == "Dumbbell Bench Press" -> listOf(
                    "Sit on the edge of a flat bench with a dumbbell in each hand, resting on your thighs.",
                    "Lie back carefully, bringing the dumbbells to the sides of your torso near your chest.",
                    "Press the dumbbells straight up dynamically until your arms are fully extended.",
                    "Lower the weights slowly under complete control to the level of your chest."
                )
                name == "Dumbbell Incline Bench Press" || name == "Incline Dumbbell Press" -> listOf(
                    "Sit on an incline bench (30-45 degrees) with a pair of dumbbells resting on your knees.",
                    "Lie back and position the dumbbells at the sides of your upper chest with your elbows bent.",
                    "Press the weights vertically upwards in a smooth motion until your arms are locked.",
                    "Slowly lower the dumbbells back down, feeling a deep stretch in your upper chest."
                )
                name == "Dumbbell Fly" -> listOf(
                    "Lie flat on a bench, holding dumbbells above your chest with palms facing each other and elbows slightly bent.",
                    "Lower your arms out in a wide arc under control, maintaining the elbow angle, until you feel a comfortable stretch.",
                    "Squeeze your chest to bring the dumbbells back up to the center in a hugging arc motion."
                )
                name == "Push-ups" || name == "Push-Up" -> listOf(
                    "Place your hands flat on the floor slightly wider than shoulder width, body in a perfectly straight line from head to heels.",
                    "Lower your body slowly by bending your elbows, keeping them tucked at a 45-degree angle.",
                    "Touch your chest to the floor lightly, keeping your core braced and spine neutral.",
                    "Push back up to the starting position with power, extending your arms fully."
                )
                name == "Chest Dips" -> listOf(
                    "Grab the dip bars, push yourself up, lean your upper body forward slightly, and cross your feet.",
                    "Bend your elbows and lower your body under control until your shoulders are slightly below your elbow joint.",
                    "Drive through your palms to press your body vertically back up to the starting position."
                )
                name == "Cable Standing Fly, Crossover fly" || name == "Cable Crossover (High-to-Low)" -> listOf(
                    "Position the pulleys at the highest level, grab both D-handles, and stand midway between the columns.",
                    "Take a step forward for stability, lean slightly, and extend your arms out wide with a slight elbow bend.",
                    "Squeeze your pectorals to bring the handles forward and downward in a wide arc until they meet in front of your hips.",
                    "Slowly return the handles to the starting position, maintaining control of the weight."
                )
                name == "Cable Lying Fly, Flat Bench Cable Fly" || name == "Cable Crossover (Low-to-High)" -> listOf(
                    "Position the pulleys at the lowest level, grab both D-handles, and stand midway between the columns.",
                    "Step forward slightly and lean from your hips, extending your arms backward with a slight elbow bend.",
                    "Sweep your arms upward and inward in a wide arc, meeting the handles together at upper chest level.",
                    "Slowly control the weights back to the starting position, feeling the chest stretch."
                )
                name == "Lever Pec Deck Fly" || name == "Pec Deck Fly" -> listOf(
                    "Sit on the pec deck machine with your back flat against the pad, feet flat on the floor.",
                    "Grip the handles or place your forearms against the pads, with your elbows bent at a 90-degree angle.",
                    "Squeeze your chest together as you bring your arms in front of you, feeling a peak contraction.",
                    "Slowly reverse the motion back to the start, feeling a deep stretch across your pectorals."
                )
                else -> listOf(
                    "Set up your equipment and sit or lie down comfortably.",
                    "Retract and depress your scapula to lock your shoulders back.",
                    "Grip the weights firmly and engage your chest.",
                    "Lower the weight slowly under control until you feel a deep stretch.",
                    "Press the weight up dynamically while breathing out, squeezing your chest at the top."
                )
            }
            val tips = when {
                name.contains("Stretch") || name.contains("Stretching") -> listOf("Breathe slowly and deeply.", "Do not bounce; hold a steady, gentle stretch.")
                name.contains("Bench Press") || name == "Bench Press" -> listOf("Keep your elbows at a 45-degree angle.", "Drive through your feet for maximum stability.")
                name.contains("Dumbbell Press") -> listOf("Keep dumbbells parallel to get a safer shoulder alignment.", "Squeeze dumbbells together slightly at the top.")
                name == "Dumbbell Fly" -> listOf("Keep a constant angle at your elbows; do not turn it into a press.", "Focus on a deep chest stretch.")
                name.contains("Push") -> listOf("Keep your core braced and glutes squeezed to prevent hips from sagging.", "Tuck your elbows to protect your rotator cuffs.")
                name.contains("Dips") -> listOf("Leaning forward targets your chest, while staying upright targets your triceps.", "Control the descent to protect shoulder joints.")
                name.contains("Crossover") || name.contains("Fly") -> listOf("Imagine you are hugging a tree to keep the correct arm arc.", "Squeeze and hold for 1 second at the peak.")
                name.contains("Pec Deck") -> listOf("Focus on squeezing your elbows together to fully activate the inner chest.", "Keep your head and shoulders firmly against the back pad.")
                else -> listOf("Keep your elbows at a 45-degree angle.", "Drive through your feet for stability.")
            }
            val mistakes = when {
                name.contains("Stretch") || name.contains("Stretching") -> listOf("Holding your breath.", "Bouncing or forcing the stretch too deep.")
                name.contains("Bench Press") || name == "Bench Press" -> listOf("Flaring the elbows out past 75-degrees.", "Bouncing the bar off your chest.")
                name.contains("Dumbbell Press") -> listOf("Letting the dumbbells collide loudly at the top.", "Dropping the weights too fast.")
                name.contains("Fly") -> listOf("Bending the elbows too much (press-fly hybrid).", "Going too deep and straining shoulder capsules.")
                name.contains("Push") -> listOf("Letting your hips sag or head drop.", "Flaring elbows out to 90 degrees.")
                name.contains("Dips") -> listOf("Staying completely vertical.", "Dropping down too fast without control.")
                name.contains("Crossover") -> listOf("Using body momentum / swinging.", "Bending elbows too much during movement.")
                name.contains("Pec Deck") -> listOf("Letting the handles fly back too fast (lack of eccentric control).", "Using your shoulders and body momentum to pull.")
                else -> listOf("Flaring the elbows out past 75-degrees.", "Bouncing the weights off your chest.")
            }
            val safetyWarning = when {
                name.contains("Stretch") || name.contains("Stretching") -> "Only stretch to the point of mild tension, never pain."
                name.contains("Bench Press") || name == "Bench Press" -> "Always use a safety spotter or Smith safety collars when loading heavy weights."
                name.contains("Dips") -> "Avoid this exercise if you have pre-existing shoulder issues or rotator cuff pain."
                name.contains("Fly") -> "Use a light, controllable weight. Never go beyond comfort range on stretch."
                name.contains("Pec Deck") -> "Do not go too far back past your shoulders to avoid rotator cuff strain."
                else -> "Maintain strict form to protect joints and ligaments."
            }
            val vUrl = when (name) {
                "Band High Fly" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Band%20High%20Fly.mp4"
                "Barbell Bench Press" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Barbell%20Bench%20Press.mp4"
                "Barbell Decline Bench Press" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Barbell%20Decline%20Bench%20Press.mp4"
                "Barbell Incline Bench Press" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Barbell%20Incline%20Bench%20Press.mp4"
                "Cable Lying Fly, Flat Bench Cable Fly" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Cable%20Lying%20Fly,%20Flat%20Bench%20Cable%20Fly.mp4"
                "Cable Standing Fly, Crossover fly" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Cable%20Standing%20Fly,%20Crossover%20fly.mp4"
                "Chest Dips" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Chest%20Dips.mp4"
                "Decline Dumbbell Bench Press (45 degree)" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Decline%20Dumbbell%20Bench%20Press%20(45%20degree).mp4"
                "Deep Push-ups" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Deep%20Push-ups.mp4"
                "Dumbbell Bench Press" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Dumbbell%20Bench%20Press.mp4"
                "Dumbbell Decline Fly (45 degree)" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Dumbbell%20Decline%20Fly%20(45%20degree).mp4"
                "Dumbbell Fly On Exercise Ball" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Dumbbell%20Fly%20On%20Exercise%20Ball.mp4"
                "Dumbbell Fly" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Dumbbell%20Fly.mp4"
                "Dumbbell Incline Bench Press" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Dumbbell%20Incline%20Bench%20Press.mp4"
                "Dumbbell Incline Fly On Exercise Ball" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Dumbbell%20Incline%20Fly%20On%20Exercise%20Ball.mp4"
                "Dumbbell Incline Fly" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Dumbbell%20Incline%20Fly.mp4"
                "Dumbbell Lying Hammer Press" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Dumbbell%20Lying%20Hammer%20Press.mp4"
                "Incline Push-Ups" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Incline%20Push-Ups.mp4"
                "Lever Chest Press" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Lever%20Chest%20Press.mp4"
                "Lever Incline Hammer Chest Press" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Lever%20Incline%20Hammer%20Chest%20Press.mp4"
                "Lever Lying Chest Press" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Lever%20Lying%20Chest%20Press.mp4"
                "Lever Pec Deck Fly" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Lever%20Pec%20Deck%20Fly.mp4"
                "Push-ups" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Push-ups.mp4"
                "Stretching - Above Head Chest Stretch" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Stretching%20-%20Above%20Head%20Chest%20Stretch.mp4"
                "Stretching - Back Pec Stretch" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Stretching%20-%20Back%20Pec%20Stretch.mp4"
                "Stretching - Dynamic Chest Stretch" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Stretching%20-%20Dynamic%20Chest%20Stretch.mp4"
                "Stretching - Kneeling Back Rotation Stretch" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Stretching%20-%20Kneeling%20Back%20Rotation%20Stretch.mp4"
                "Stretching - Standing Wheel Rollout" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Stretching%20-%20Standing%20Wheel%20Rollout.mp4"
                "Triceps Dips" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Triceps%20Dips.mp4"
                "Wide Grip Push-ups" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/Wide%20Grip%20Push-ups.mp4"
                else -> null
            }
            list.add(
                Exercise(
                    name = name,
                    category = "Chest",
                    difficulty = pair.second.second,
                    primaryMuscles = primaryMuscles,
                    secondaryMuscles = secondaryMuscles,
                    equipment = pair.second.first,
                    machineSetup = pair.second.third,
                    seatAdjustment = if (pair.second.first in listOf("Pec Deck", "Chest Press", "Pec Deck Machine", "Lever Machine")) "Level 3 - Chest alignment" else "N/A",
                    pulleyPosition = if (name.contains("High")) "Level 10 (Highest)" else if (name.contains("Low") || name.contains("Cable")) "Level 2 (Lowest)" else "N/A",
                    attachment = if (pair.second.first == "Cable Machine") "D-handle" else "N/A",
                    instructions = instructions,
                    tips = tips,
                    mistakes = mistakes,
                    safetyWarning = safetyWarning,
                    beginnerWeight = when (pair.second.first) {
                        "Barbell" -> "20kg (empty bar) - 30kg"
                        "Dumbbells" -> "8kg - 12kg dumbbells"
                        "Bodyweight" -> "Bodyweight"
                        else -> "15kg - 25kg stack"
                    },
                    warmUpRoutine = "5 minutes arm circles, light push-ups or light bench press (15 reps)",
                    stretchingRoutine = "Doorway chest stretch (30 seconds each side)",
                    handPlacement = "Width slightly wider than shoulders",
                    footPlacement = "Symmetric flat feet driven into floor",
                    videoUrl = vUrl
                )
            )
        }

        // --- BACK (54 PRODUCTION QUALITY WITH VIDEO INTEGRATION) ---
        val backNames = listOf(
            "45 Degree Hyperextension" to Triple("Bodyweight", "Beginner", "Hyperextension bench set to 45 degrees"),
            "Band Assisted Pull-up" to Triple("Bodyweight", "Intermediate", "Pull-up bar with a loop resistance band under feet or knees"),
            "Band Bent-over Row" to Triple("Resistance Band", "Beginner", "Step on the center of the band, grasp handles/ends"),
            "Band Kneeling One Arm Pulldown" to Triple("Resistance Band", "Beginner", "Band anchored at a high level, kneeling position"),
            "Band One Arm Twisting Seated Row" to Triple("Resistance Band", "Beginner", "Band anchored low in front of you, seated position"),
            "Band One Leg Kickback Bent Position" to Triple("Resistance Band", "Beginner", "Band looped around one foot, quadruped position on floor"),
            "Band Pull Through" to Triple("Resistance Band", "Beginner", "Band anchored low behind you, standing with back to anchor"),
            "Band Seated Row" to Triple("Resistance Band", "Beginner", "Seated on floor, band wrapped securely around feet"),
            "Band Straight Back Seated Row" to Triple("Resistance Band", "Beginner", "Seated with upright posture, band wrapped around feet"),
            "Barbell Bent-over Row Overgrip" to Triple("Barbell", "Intermediate", "Bent-over stance, overhand overgrip grip on barbell"),
            "Barbell Straight Leg Deadlift" to Triple("Barbell", "Intermediate", "Standing upright with barbell, feet hip-width apart"),
            "Barbell Underhand Bent-over Row" to Triple("Barbell", "Intermediate", "Bent-over stance, underhand grip on barbell"),
            "Bench Pull-ups" to Triple("Bodyweight", "Beginner", "Pull-up bar with a bench or platform supporting your feet"),
            "Cable Bar Lateral Pulldown (reverse-grip)" to Triple("Lat Pulldown", "Beginner", "Lat pulldown station with a straight bar, underhand reverse grip"),
            "Cable Bar Lateral Pulldown (wide shoulder grip)" to Triple("Lat Pulldown", "Beginner", "Lat pulldown station with a wide bar, overhand wide grip"),
            "Cable Close Grip Front Lat Pulldown" to Triple("Lat Pulldown", "Beginner", "Lat pulldown station with a close-grip V-bar attachment"),
            "Cable One Arm Lateral Pulldown" to Triple("Cable Machine", "Intermediate", "High pulley with a single-hand D-handle attachment"),
            "Cable One Arm Twisting Seated Row" to Triple("Cable Machine", "Intermediate", "Seated row station with a single D-handle attachment"),
            "Cable Pulldown" to Triple("Lat Pulldown", "Beginner", "Lat pulldown station with a standard lat bar attachment"),
            "Cable Seated High Row (V-bar)" to Triple("Seated Row", "Beginner", "Seated high row machine/pulley with V-bar attachment"),
            "Cable Seated Row (normal grip)" to Triple("Seated Row", "Beginner", "Seated row station with a standard straight or lat bar"),
            "Cable Seated Row (parallel grip)" to Triple("Seated Row", "Beginner", "Seated row station with parallel close-grip handle"),
            "Cable Seated Row (wide-grip)" to Triple("Seated Row", "Beginner", "Seated row station with a wide straight bar attachment"),
            "Cable Straight Arm Pulldown" to Triple("Cable Machine", "Intermediate", "High pulley with straight bar attachment"),
            "Cable Straight Back Seated High Row (reverse-grip)" to Triple("Seated Row", "Intermediate", "High row machine/pulley with reverse underhand grip"),
            "Cable Straight Back Seated Row (V-grip)" to Triple("Seated Row", "Beginner", "Seated row station with a close grip V-handle"),
            "Cambered Bar Lying Row" to Triple("Barbell", "Intermediate", "Elevated flat bench with cambered bar resting underneath"),
            "Chin-ups Pull-Ups" to Triple("Bodyweight", "Intermediate", "Standard chin-up bar, mixed/neutral or close underhand grip"),
            "Chin-ups (narrow parallel grip)" to Triple("Bodyweight", "Intermediate", "Chin-up bar with narrow parallel neutral handles"),
            "Close Grip Chin-up" to Triple("Bodyweight", "Intermediate", "Standard pull-up bar, close underhand grip"),
            "Commando Pull-up" to Triple("Bodyweight", "Advanced", "Stand sideways directly under pull-up bar, split grip"),
            "Dumbbell Bent-over Row" to Triple("Dumbbells", "Beginner", "Bent-over stance holding a dumbbell in each hand"),
            "Dumbbell Deadlift" to Triple("Dumbbells", "Beginner", "Standing with a pair of heavy dumbbells at your sides"),
            "Dumbbell Hammer Grip Incline Bench Row" to Triple("Dumbbells", "Intermediate", "Incline bench set to 30-45 degrees, chest-down hammer grip"),
            "Dumbbell Incline Row" to Triple("Dumbbells", "Intermediate", "Incline bench set to 30-45 degrees, chest-down position"),
            "Dumbbell Lying Rear Delt Row" to Triple("Dumbbells", "Intermediate", "Flat or incline bench, chest-down holding dumbbells"),
            "Dumbbell Palm Rotational Bent Over Row" to Triple("Dumbbells", "Intermediate", "Bent-over stance, rotating palms neutral-to-underhand during row"),
            "Dumbbell Reverse Grip Row" to Triple("Dumbbells", "Intermediate", "Bent-over stance, holding dumbbells with palms facing forward"),
            "Dumbbell Stiff Leg Deadlift" to Triple("Dumbbells", "Beginner", "Standing upright holding dumbbells in front of your thighs"),
            "Hammer Grip Pull-up" to Triple("Bodyweight", "Intermediate", "Pull-up bar with parallel neutral grip handles"),
            "Kettlebell Deadlift" to Triple("Dumbbells", "Beginner", "Standing flat on floor directly over a heavy kettlebell"),
            "Lever Back Extension" to Triple("Lever Machine", "Beginner", "Back extension machine, padded rollers behind shoulders or lower back"),
            "Lever High Row" to Triple("Lever Machine", "Intermediate", "Lever high row machine, adjustable seat set to chest level"),
            "Lever Reverse Hyperextension" to Triple("Lever Machine", "Intermediate", "Reverse hyperextension bench, pelvis on edge of pad"),
            "Lever Reverse T-Bar Row" to Triple("Lever Machine", "Intermediate", "Lever T-bar row machine chest supported, reversed grip handles"),
            "Lever T-bar Row" to Triple("Lever Machine", "Intermediate", "Lever chest-supported T-bar row machine, neutral handles"),
            "Pull-up (shoulder grip)" to Triple("Bodyweight", "Intermediate", "Pull-up bar, overhand shoulder-width grip"),
            "Pull-up (wide back grip)" to Triple("Bodyweight", "Advanced", "Pull-up bar, wide overhand grip pulling bar behind neck"),
            "Pull-up (wide front grip)" to Triple("Bodyweight", "Advanced", "Pull-up bar, wide overhand grip pulling bar to upper chest"),
            "Reverse Grip Machine Lat Pulldown" to Triple("Lat Pulldown", "Beginner", "Lat pulldown machine, underhand reverse grip handles"),
            "Reverse Grip Pull-up" to Triple("Bodyweight", "Intermediate", "Pull-up bar, underhand shoulder-width grip"),
            "Single Dumbbell Stiff Leg Deadlift" to Triple("Dumbbells", "Beginner", "Standing holding a single dumbbell in both hands"),
            "Smith Deadlift - Deadlift (1)" to Triple("Smith Machine", "Intermediate", "Smith Machine bar set at lowest position (variation 1)"),
            "Smith Deadlift - Deadlift" to Triple("Smith Machine", "Intermediate", "Smith Machine bar set at lowest position (standard)")
        )

        backNames.forEachIndexed { index, pair ->
            val name = pair.first
            val eq = pair.second.first
            val diff = pair.second.second
            val setup = pair.second.third

            // Target Muscles & Anatomy (Primary & Secondary)
            val primaryMuscles = when {
                name.contains("Pulldown") || name.contains("Pull-up") || name.contains("Chin-up") || name.contains("Pull-Ups") -> listOf("Latissimus Dorsi", "Teres Major")
                name.contains("Row") -> listOf("Latissimus Dorsi", "Rhomboids", "Middle Trapezius")
                name.contains("Deadlift") || name.contains("Hyperextension") -> listOf("Erector Spinae", "Glutes", "Hamstrings")
                else -> listOf("Latissimus Dorsi")
            }

            val secondaryMuscles = when {
                name.contains("Deadlift") || name.contains("Hyperextension") -> listOf("Lower Back", "Glutes", "Core", "Hamstrings")
                name.contains("Pulldown") || name.contains("Pull-up") || name.contains("Chin-up") || name.contains("Row") -> listOf("Biceps", "Forearms", "Rear Deltoids", "Brachialis")
                else -> listOf("Biceps", "Rhomboids")
            }

            // Pulley position & attachment based on equipment and name
            val pulley = when {
                eq == "Lat Pulldown" || name.contains("Pulldown") || name.contains("Straight Arm") -> "Level 10 (Highest)"
                eq == "Seated Row" || name.contains("Seated Row") -> "Level 3"
                else -> "N/A"
            }

            val attach = when {
                name.contains("V-bar") || name.contains("V-grip") || name.contains("Close Grip") -> "V-bar"
                name.contains("reverse-grip") || name.contains("underhand") -> "EZ bar (or Reverse Grip Bar)"
                name.contains("wide") || name.contains("normal") -> "Straight bar"
                name.contains("parallel") -> "Parallel handles"
                eq == "Cable Machine" && name.contains("One Arm") -> "D-handle"
                else -> "N/A"
            }

            // Custom high quality instructions
            val instructions = when {
                name.contains("Deadlift") -> listOf(
                    "Stand with your feet hip-width apart, holding the weights in front of you.",
                    "Hinge at your hips and lower the weight, keeping your knees slightly bent and spine completely straight.",
                    "Lower until you feel a deep stretch in your hamstrings, keeping the weights close to your body.",
                    "Squeeze your glutes and hamstrings to drive yourself back upright to starting posture."
                )
                name.contains("Hyperextension") -> listOf(
                    "Position yourself securely in the hyperextension apparatus with hips at the pad edge.",
                    "Lower your upper body under control by bending at the waist, keeping your spine neutral.",
                    "Raise your torso until your body is in a straight line, squeezing your lower back and glutes.",
                    "Avoid hyperextending or arching your back past the parallel alignment at the top."
                )
                name.contains("Pulldown") -> listOf(
                    "Sit securely in the lat pulldown machine, adjusting the knee pad for a locked-in fit.",
                    "Grasp the bar with your selected grip (wide, shoulder, or close reverse-grip).",
                    "Depress and retract your shoulder blades, then pull the bar down toward your upper chest.",
                    "Lead with your elbows and squeeze your lats at the bottom contraction.",
                    "Slowly reverse the motion to return the bar to the starting stretch."
                )
                name.contains("Pull-up") || name.contains("Chin-up") -> listOf(
                    "Hang from the pull-up bar with your selected grip width and hand orientation.",
                    "Keep your core engaged, chest up, and shoulders packed down.",
                    "Pull your body upward by driving your elbows down toward your hips until your chin clears the bar.",
                    "Lower yourself under complete control until your arms are fully extended."
                )
                name.contains("Row") -> listOf(
                    "Assume the starting position (bent-over at 45 degrees or seated on the machine bench).",
                    "Grasp the weights or handles with your back flat and chest fully elevated.",
                    "Pull the handles or weights toward your lower abdomen/hip crease.",
                    "Drive your elbows back and squeeze your shoulder blades together at the peak.",
                    "Slowly extend your arms back to the starting stretch under control."
                )
                else -> listOf(
                    "Assume the starting position with proper alignment and a flat, neutral spine.",
                    "Initiate the movement by packing your shoulders and engaging your target back muscles.",
                    "Move the weight through a full range of motion under strict, controlled biomechanics.",
                    "Pause at peak contraction, squeeze, and return slowly to starting position."
                )
            }

            // Tips & Common Mistakes
            val tips = when {
                name.contains("Deadlift") -> listOf("Keep the weights extremely close to your shins/thighs.", "Drive through your heels, not your lower back.")
                name.contains("Pulldown") || name.contains("Pull-up") || name.contains("Chin-up") -> listOf("Pull through your elbows, imagining your hands are hooks.", "Keep your chest tall and open at the top of the rep.")
                name.contains("Row") -> listOf("Keep your elbows tucked and pull toward your hips.", "Focus on squeezing your shoulder blades together.")
                else -> listOf("Keep your core braced to stabilize your pelvis.", "Focus on a deep stretch on the eccentric phase.")
            }

            val mistakes = when {
                name.contains("Deadlift") -> listOf("Rounding your lumbar spine under heavy loads.", "Hyperextending your back excessively at the top lock-out.")
                name.contains("Pulldown") || name.contains("Pull-up") || name.contains("Chin-up") -> listOf("Using body momentum or swinging to pull yourself up.", "Short-changing the range of motion at the top or bottom.")
                name.contains("Row") -> listOf("Rounding your shoulders and upper back.", "Pulling too high toward your chest, which flares the elbows.")
                else -> listOf("Using excessive momentum / swinging your torso.", "Releasing the weight too fast on the descent.")
            }

            val safetyWarning = when {
                name.contains("Deadlift") -> "Always keep your spine completely neutral. Never lift with a rounded back."
                name.contains("Hyperextension") -> "Avoid swinging or hyperextending your spine beyond a straight line at the top."
                else -> "Maintain strict form. Avoid using excessive momentum to prevent shoulder or lower back strain."
            }

            // Automatically map every GitHub RAW URL to the matching exercise using the filename.
            val encodedName = name
                .replace(" ", "%20")
                .replace("(", "%28")
                .replace(")", "%29")
                .replace(",", "%2C")
            val vUrl = "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/7babe7e69ee02fa99b5a12997bff5836161b4ee7/$encodedName.mp4"

            list.add(
                Exercise(
                    name = name,
                    category = "Back",
                    difficulty = diff,
                    primaryMuscles = primaryMuscles,
                    secondaryMuscles = secondaryMuscles,
                    equipment = eq,
                    machineSetup = setup,
                    seatAdjustment = if (eq in listOf("Lat Pulldown", "Seated Row", "Lever Machine")) "Level 3 - Chest alignment" else "N/A",
                    pulleyPosition = pulley,
                    attachment = attach,
                    instructions = instructions,
                    tips = tips,
                    mistakes = mistakes,
                    safetyWarning = safetyWarning,
                    beginnerWeight = when (eq) {
                        "Barbell" -> "20kg (empty bar) - 30kg"
                        "Dumbbells" -> "8kg - 12kg dumbbells"
                        "Bodyweight" -> "Bodyweight"
                        else -> "15kg - 25kg stack"
                    },
                    warmUpRoutine = "5 minutes arm circles, light pulldowns or rows (15 reps)",
                    stretchingRoutine = "Behind-head lat stretch (30 seconds each arm)",
                    handPlacement = "Width slightly wider than shoulders",
                    footPlacement = "Symmetric flat feet driven into floor",
                    videoUrl = vUrl
                )
            )
        }

        // --- SHOULDERS (15) ---
        val shoulderNames = listOf(
            "Seated Dumbbell Press" to Triple("Dumbbells", "Beginner", "90-degree Upright Bench"),
            "Overhead Press" to Triple("Barbell", "Intermediate", "Power Rack or Press Stand"),
            "Lateral Raise" to Triple("Dumbbells", "Beginner", "Standing flat surface"),
            "Cable Lateral Raise" to Triple("Cable Machine", "Intermediate", "Low pulley single column"),
            "Machine Shoulder Press" to Triple("Chest Press", "Beginner", "Shoulder Press selectorized machine"),
            "Front Raise" to Triple("Dumbbells", "Beginner", "Standing space"),
            "Landmine Press" to Triple("Barbell", "Intermediate", "Landmine holder setup"),
            "Reverse Pec Deck" to Triple("Pec Deck", "Beginner", "Pec Deck reversed handles"),
            "Cable Face Pulls Shoulder" to Triple("Cable Machine", "Beginner", "High pulley column"),
            "Rear Delt Fly" to Triple("Dumbbells", "Intermediate", "Flat bench under hips slant"),
            "Smith Machine Shoulder Press" to Triple("Smith Machine", "Beginner", "Smith Machine Upright Bench"),
            "Arnold Press" to Triple("Dumbbells", "Intermediate", "Upright seat Support"),
            "Barbell Upright Rows" to Triple("Barbell", "Intermediate", "Standing barbell spacing"),
            "Cable Y-Raise" to Triple("Cable Machine", "Advanced", "Double columns cross-pulleys"),
            "Standing Dumbbell Shrugs" to Triple("Dumbbells", "Beginner", "Standing floor")
        )
        val shoulderPrimary = mapOf(
            "Overhead Press" to listOf("Anterior Deltoid", "Lateral Deltoid"),
            "Machine Shoulder Press" to listOf("Anterior Deltoid"),
            "Landmine Press" to listOf("Anterior Deltoid", "Clavicular Head (Upper Chest)"),
            "Lateral Raise" to listOf("Lateral Deltoid"),
            "Cable Lateral Raise" to listOf("Lateral Deltoid", "Anterior Deltoid"),
            "Front Raise" to listOf("Anterior Deltoid"),
            "Rear Delt Fly" to listOf("Posterior Deltoid (Rear)"),
            "Reverse Pec Deck" to listOf("Posterior Deltoid (Rear)", "Mid Trapezius & Rhomboids")
        )
        val shoulderSecondary = mapOf(
            "Overhead Press" to listOf("Triceps Brachii", "Upper Trapezius"),
            "Machine Shoulder Press" to listOf("Triceps (Lateral Head)", "Lateral Deltoid"),
            "Landmine Press" to listOf("Triceps (Lateral Head)", "Transversus Abdominis (Core)"),
            "Lateral Raise" to listOf("Upper Trapezius", "Anterior Deltoid"),
            "Cable Lateral Raise" to listOf("Upper Trapezius", "Brachialis & Forearms"),
            "Front Raise" to listOf("Lateral Deltoid", "Upper Chest"),
            "Rear Delt Fly" to listOf("Mid Trapezius & Rhomboids", "Lateral Deltoid"),
            "Reverse Pec Deck" to listOf("Upper Trapezius", "Triceps (Long Head)")
        )

        shoulderNames.forEach { pair ->
            val prim = shoulderPrimary[pair.first] ?: listOf("Anterior Deltoids", "Lateral Deltoids", "Posterior Deltoids")
            val sec = shoulderSecondary[pair.first] ?: listOf("Triceps", "Trapezius", "Upper Chest")
            list.add(
                Exercise(
                    name = pair.first,
                    category = "Shoulders",
                    difficulty = pair.second.second,
                    primaryMuscles = prim,
                    secondaryMuscles = sec,
                    equipment = pair.second.first,
                    machineSetup = pair.second.third,
                    seatAdjustment = if (pair.second.first == "Pec Deck") "Level 2 (High)" else if (pair.second.first == "Chest Press") "Level 3" else "N/A",
                    pulleyPosition = if (pair.second.first == "Cable Machine" && pair.first.contains("Lateral")) "Level 1 (Lowest)" else if (pair.first.contains("Face Pulls") || pair.first.contains("Y-Raise")) "Level 8-10" else "N/A",
                    attachment = if (pair.first.contains("Cable")) "D-handle" else if (pair.first.contains("Face Pulls")) "Rope" else "N/A",
                    instructions = when (pair.first) {
                        "Overhead Press" -> listOf(
                            "Place a barbell at collarbone height on a rack. Grab it with an overhand grip.",
                            "Unrack the bar and take a step back. Stand with feet shoulder-width apart.",
                            "Brace your core, squeeze your glutes, and keep your elbows slightly forward.",
                            "Press the bar overhead in a straight line, pulling your head back slightly as it passes.",
                            "Lock out your elbows at the top and shrug your shoulders upward.",
                            "Lower the barbell under control back to your upper chest."
                        )
                        "Machine Shoulder Press" -> listOf(
                            "Adjust the seat height so that the handles align with your shoulders.",
                            "Sit back firmly against the pad and grip the handles with an overhand grip.",
                            "Brace your feet against the floor or footrests, keeping your core tight.",
                            "Press the handles upward explosively until your arms are fully extended but not locked.",
                            "Inhale as you slowly lower the handles back to starting position."
                        )
                        "Landmine Press" -> listOf(
                            "Set up a barbell in a landmine attachment or wedge it in a corner.",
                            "Stand with feet shoulder-width apart, holding the end of the sleeve in one hand at shoulder level.",
                            "Lean slightly forward into the barbell, loading the front foot.",
                            "Press the bar upward and forward at an angle until your arm is fully extended.",
                            "Control the descent back to your shoulder, keeping your elbow tucked."
                        )
                        "Lateral Raise" -> listOf(
                            "Stand with feet hip-width apart holding dumbbells at your sides.",
                            "Lean your torso slightly forward to align with the lateral deltoid fibers.",
                            "With a slight bend in your elbows, raise the weights out to your sides.",
                            "Lead with your elbows, keeping your hands slightly below or level with your elbows.",
                            "Stop when your arms are parallel to the floor, squeeze the side delts.",
                            "Lower the weights slowly back to the starting position."
                        )
                        "Cable Lateral Raise" -> listOf(
                            "Set the cable pulley to the lowest position. Stand sideways to the machine.",
                            "Reach across your body and grab the D-handle with your outside hand.",
                            "Stand tall with a tight core, keeping a slight bend in your elbow.",
                            "Raise your arm outward and upward until it is parallel to the floor.",
                            "Focus on driving with the lateral deltoid and keeping tension on the cable.",
                            "Slowly reverse the motion back to the starting point."
                        )
                        "Front Raise" -> listOf(
                            "Stand with feet shoulder-width apart holding dumbbells in front of your thighs.",
                            "Keep your core tight and a very slight bend in your elbows.",
                            "Raise the weights directly in front of you until your arms are parallel to the floor.",
                            "Avoid using momentum or swinging your body.",
                            "Lower the dumbbells slowly back to your thighs."
                        )
                        "Rear Delt Fly" -> listOf(
                            "Stand with feet hip-width apart holding dumbbells at your sides.",
                            "Hinge at your hips until your torso is almost parallel to the floor, keeping your back flat.",
                            "Let the dumbbells hang straight down, palms facing each other.",
                            "Raise your arms out to the sides by squeezing your rear delts.",
                            "Keep a slight bend in your elbows and focus on pulling with the back of your shoulders.",
                            "Control the descent back to the starting position."
                        )
                        "Reverse Pec Deck" -> listOf(
                            "Adjust the seat so that the handles are at shoulder height when sitting.",
                            "Sit facing the machine pad with your chest firmly against it.",
                            "Grip the handles with a neutral or overhand grip.",
                            "Keep your elbows slightly bent and pull your arms backward in a wide arc.",
                            "Squeeze your rear delts and mid-back at the peak of the movement.",
                            "Slowly return to the starting position without letting the weights touch."
                        )
                        else -> listOf(
                            "Align your shoulders perfectly parallel with gravity/system.",
                            "Hold dumbbells at your ears or capture the machine handles.",
                            "Brace your core tight, keep feet locked firmly into the ground.",
                            "Drive weight vertically upward directly above your shoulder joints.",
                            "Bring weights back down slowly to complete the movement."
                        )
                    },
                    tips = when (pair.first) {
                        "Overhead Press" -> listOf("Squeeze your glutes and core to protect your lower back.", "Don't let your elbows flare too wide.")
                        "Machine Shoulder Press" -> listOf("Keep your lower back flat against the back pad.", "Focus on pushing with your shoulders, not your neck.")
                        "Landmine Press" -> listOf("Drive from the balls of your feet to transfer power up.", "Keep your elbow tucked at a 45-degree angle.")
                        "Lateral Raise" -> listOf("Raise the weights in the scapular plane (slightly forward).", "Do not shrug your shoulders up to your ears.")
                        "Cable Lateral Raise" -> listOf("Maintain a steady, slow negative to maximize tension.", "Do not lean away from the machine.")
                        "Front Raise" -> listOf("Control the descent of the dumbbells.", "Keep your shoulder blades pulled back.")
                        "Rear Delt Fly" -> listOf("Do not swing the weights.", "Keep your neck in a neutral, relaxed position.")
                        "Reverse Pec Deck" -> listOf("Keep your chest pressed firmly against the pad.", "Avoid using your triceps; keep the elbow angle constant.")
                        else -> listOf("Do not shrug during raise movements.", "Keep lateral raises slightly in front of your chest.")
                    },
                    mistakes = when (pair.first) {
                        "Overhead Press" -> listOf("Arching your back excessively.", "Bouncing the bar off your chest.")
                        "Machine Shoulder Press" -> listOf("Flaring elbows outward excessively.", "Slipping forward off the seat.")
                        "Landmine Press" -> listOf("Pressing purely vertically instead of at an angle.", "Letting your core go loose.")
                        "Lateral Raise" -> listOf("Using too much weight and swinging.", "Leading with the hands instead of the elbows.")
                        "Cable Lateral Raise" -> listOf("Bending the elbow too much during the raise.", "Using momentum.")
                        "Front Raise" -> listOf("Swinging the upper body.", "Using excessively heavy weights.")
                        "Rear Delt Fly" -> listOf("Rounding your lower back.", "Using momentum to lift the weights.")
                        "Reverse Pec Deck" -> listOf("Shrugging up with the upper traps.", "Letting the head drop forward.")
                        else -> listOf("Arching your back too much on heavy press.", "Swaying the arms or torso.")
                    },
                    safetyWarning = when (pair.first) {
                        "Overhead Press" -> "Use a safety rack or spotter when lifting heavy weights."
                        "Machine Shoulder Press" -> "Do not adjust the machine settings while seated with load."
                        "Landmine Press" -> "Keep a firm grip on the bar to prevent slipping."
                        "Lateral Raise" -> "Stop immediately if you feel sharp pain in the front of your shoulder."
                        "Cable Lateral Raise" -> "Ensure the cable is secure and doesn't rub against your arm."
                        "Front Raise" -> "Keep your core tightly braced to support your spine."
                        "Rear Delt Fly" -> "Keep your neck neutral and do not look up."
                        "Reverse Pec Deck" -> "Set a safe range of motion that doesn't overstretch the rotator cuff."
                        else -> "Control the descent on lateral movements to protect the rotator cuff."
                    },
                    beginnerWeight = when (pair.first) {
                        "Overhead Press" -> "Empty barbell (20kg)"
                        "Machine Shoulder Press" -> "10kg load"
                        "Landmine Press" -> "Empty bar or +5kg"
                        "Lateral Raise" -> "4kg - 6kg dumbbells"
                        "Cable Lateral Raise" -> "2.5kg on cable stack"
                        "Front Raise" -> "5kg - 8kg dumbbells"
                        "Rear Delt Fly" -> "4kg - 6kg dumbbells"
                        "Reverse Pec Deck" -> "15kg on selectorized stack"
                        else -> "6kg dumbbells or 15kg load"
                    },
                    warmUpRoutine = "Rotator cuff warm-up with light cable band",
                    stretchingRoutine = "Cross-body arm stretch (30 seconds each side)"
                )
            )
        }

        // --- BICEPS (30) ---
        val bicepNames = listOf(
            // New & mapped videos (21 items)
            "Barbell Curl" to Triple("Barbell", "Beginner", "Straight barbell station"),
            "Barbell Drag Curl" to Triple("Barbell", "Intermediate", "Standing barbell space"),
            "Barbell Prone Incline Curl" to Triple("Barbell", "Intermediate", "Incline bench prone position"),
            "Cable One Arm Curl" to Triple("Cable Machine", "Intermediate", "Low pulley station"),
            "Cable Standing Inner Curl" to Triple("Cable Machine", "Intermediate", "Dual low columns"),
            "Dumbbell Alternate Biceps Curl" to Triple("Dumbbells", "Beginner", "Standing flat floor"),
            "Dumbbell Alternate Seated Biceps Curl" to Triple("Dumbbells", "Beginner", "Seated flat bench"),
            "Dumbbell Biceps Curl" to Triple("Dumbbells", "Beginner", "Standing flat floor"),
            "Dumbbell Concentration Curl" to Triple("Dumbbells", "Beginner", "Seated flat bench edge"),
            "Dumbbell Cross Body Hammer Curl" to Triple("Dumbbells", "Intermediate", "Standing flat floor"),
            "Dumbbell Incline Biceps Curl" to Triple("Dumbbells", "Intermediate", "Incline bench incline incline"),
            "Dumbbell Incline Curl" to Triple("Dumbbells", "Intermediate", "Incline bench incline incline"),
            "Dumbbell Incline Hammer Curl" to Triple("Dumbbells", "Intermediate", "Incline bench incline incline"),
            "Dumbbell One Arm Zottman Preacher Curl" to Triple("Dumbbells", "Intermediate", "Preacher bench station"),
            "Dumbbell Preacher Curl Over Exercise Ball" to Triple("Dumbbells", "Intermediate", "Stability Swiss Ball"),
            "Dumbbell Prone Incline Curl" to Triple("Dumbbells", "Intermediate", "Incline bench prone position"),
            "Dumbbell Seated Preacher Curl" to Triple("Dumbbells", "Intermediate", "Preacher bench station"),
            "EZ Barbell Biceps Curl" to Triple("Barbell", "Beginner", "EZ barbell station"),
            "Lever Preacher Curl" to Triple("Lever Machine", "Beginner", "Lever preacher curl machine"),
            "Lying Supine Dumbbell Curl" to Triple("Dumbbells", "Intermediate", "Flat bench lying down"),
            "Butterfly Yoga Pose" to Triple("Bodyweight", "Beginner", "Floor mat area"),

            // Existing ones without video (9 items)
            "Dumbbell Hammer Curl" to Triple("Dumbbells", "Beginner", "Standing or sitting"),
            "Preacher Curl EZ-Bar" to Triple("Barbell", "Beginner", "Preacher Curl Bench Station"),
            "Cable Bicep Curl Straight Bar" to Triple("Cable Machine", "Beginner", "Low pulley level 1"),
            "Spider Curl EZ-Bar" to Triple("Barbell", "Intermediate", "Prone on Incline bench"),
            "Rope Hammer Curl Cable" to Triple("Cable Machine", "Beginner", "Low pulley level 1 mount"),
            "High Cable Curl Double Arm" to Triple("Cable Machine", "Intermediate", "Dual High columns"),
            "Reverse Grip Barbell Curl" to Triple("Barbell", "Beginner", "Standing barbell space"),
            "Zottman Curl" to Triple("Dumbbells", "Intermediate", "Standing floor spacer"),
            "Behind Bicep Cable Curl" to Triple("Cable Machine", "Advanced", "Low pulley rear layout")
        )
        bicepNames.forEach { pair ->
            val vUrl = when (pair.first) {
                "Barbell Curl" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/e3649759962c1cfaf6694f1c9d894b28efef00dd/Barbell%20Curl.mp4"
                "Barbell Drag Curl" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/e3649759962c1cfaf6694f1c9d894b28efef00dd/Barbell%20Drag%20Curl.mp4"
                "Barbell Prone Incline Curl" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/e3649759962c1cfaf6694f1c9d894b28efef00dd/Barbell%20Prone%20Incline%20Curl.mp4"
                "Cable One Arm Curl" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/e3649759962c1cfaf6694f1c9d894b28efef00dd/Cable%20One%20Arm%20Curl.mp4"
                "Cable Standing Inner Curl" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/e3649759962c1cfaf6694f1c9d894b28efef00dd/Cable%20Standing%20Inner%20Curl.mp4"
                "Dumbbell Alternate Biceps Curl" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/e3649759962c1cfaf6694f1c9d894b28efef00dd/Dumbbell%20Alternate%20Biceps%20Curl.mp4"
                "Dumbbell Alternate Seated Biceps Curl" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/e3649759962c1cfaf6694f1c9d894b28efef00dd/Dumbbell%20Alternate%20Seated%20Biceps%20Curl.mp4"
                "Dumbbell Biceps Curl" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/e3649759962c1cfaf6694f1c9d894b28efef00dd/Dumbbell%20Biceps%20Curl.mp4"
                "Dumbbell Concentration Curl" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/e3649759962c1cfaf6694f1c9d894b28efef00dd/Dumbbell%20Concentration%20Curl.mp4"
                "Dumbbell Cross Body Hammer Curl" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/e3649759962c1cfaf6694f1c9d894b28efef00dd/Dumbbell%20Cross%20Body%20Hammer%20Curl.mp4"
                "Dumbbell Incline Biceps Curl" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/e3649759962c1cfaf6694f1c9d894b28efef00dd/Dumbbell%20Incline%20Biceps%20Curl.mp4"
                "Dumbbell Incline Curl" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/e3649759962c1cfaf6694f1c9d894b28efef00dd/Dumbbell%20Incline%20Curl.mp4"
                "Dumbbell Incline Hammer Curl" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/e3649759962c1cfaf6694f1c9d894b28efef00dd/Dumbbell%20Incline%20Hammer%20Curl.mp4"
                "Dumbbell One Arm Zottman Preacher Curl" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/e3649759962c1cfaf6694f1c9d894b28efef00dd/Dumbbell%20One%20Arm%20Zottman%20Preacher%20Curl.mp4"
                "Dumbbell Preacher Curl Over Exercise Ball" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/e3649759962c1cfaf6694f1c9d894b28efef00dd/Dumbbell%20Preacher%20Curl%20Over%20Exercise%20Ball.mp4"
                "Dumbbell Prone Incline Curl" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/e3649759962c1cfaf6694f1c9d894b28efef00dd/Dumbbell%20Prone%20Incline%20Curl.mp4"
                "Dumbbell Seated Preacher Curl" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/e3649759962c1cfaf6694f1c9d894b28efef00dd/Dumbbell%20Seated%20Preacher%20Curl.mp4"
                "EZ Barbell Biceps Curl" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/e3649759962c1cfaf6694f1c9d894b28efef00dd/EZ%20Barbell%20Biceps%20Curl.mp4"
                "Lever Preacher Curl" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/e3649759962c1cfaf6694f1c9d894b28efef00dd/Lever%20Preacher%20Curl.mp4"
                "Lying Supine Dumbbell Curl" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/e3649759962c1cfaf6694f1c9d894b28efef00dd/Lying%20Supine%20Dumbbell%20Curl.mp4"
                "Butterfly Yoga Pose" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/e3649759962c1cfaf6694f1c9d894b28efef00dd/Stretching%20-%20Butterfly%20Yoga%20Pose.mp4"
                else -> null
            }
            list.add(
                Exercise(
                    name = pair.first,
                    category = "Biceps",
                    difficulty = pair.second.second,
                    primaryMuscles = listOf("Biceps Brachii"),
                    secondaryMuscles = listOf("Brachioradialis", "Biceps Brachialis", "Forearms"),
                    equipment = pair.second.first,
                    machineSetup = pair.second.third,
                    seatAdjustment = "N/A",
                    pulleyPosition = if (pair.second.first == "Cable Machine" && pair.first.contains("High")) "Level 8" else if (pair.second.first == "Cable Machine") "Level 1 (Lowest)" else "N/A",
                    attachment = if (pair.first.contains("Rope")) "Rope" else if (pair.first.contains("EZ-Bar") || pair.first.contains("Preacher")) "EZ bar" else if (pair.first.contains("Straight")) "Straight bar" else "N/A",
                    instructions = listOf(
                        "Set up your target weights and stand flat on the floor.",
                        "Squeeze your elbows tightly into your ribs and rotate wrists upwards.",
                        "Contract your biceps dynamically to pull control weight to your chest.",
                        "Keep your elbows stationary, do not let them swing forward.",
                        "Lower the weight back down slowly under precise negative tension."
                    ),
                    tips = listOf("Squeeze your biceps hard at the peak.", "Control the lowering phase completely."),
                    mistakes = listOf("Swinging the torso to lift weight.", "Letting elbows drift too far forward."),
                    safetyWarning = "Extend your arm fully but do not snap your elbow at the bottom.",
                    beginnerWeight = "10kg EZ-bar or 6kg dumbbells",
                    warmUpRoutine = "5 minutes dynamic rotational shoulder exercises",
                    stretchingRoutine = "Wall bicep forearm stretch (30 seconds each side)",
                    videoUrl = vUrl
                )
            )
        }

        // --- TRICEPS (14) ---
        val tricepNames = listOf(
            "Rope Pushdown" to Triple("Cable Machine", "Beginner", "High pulley level 10 Column"),
            "V-Bar Pushdown" to Triple("Cable Machine", "Beginner", "High pulley level 10 Column"),
            "Overhead Dumbbell Extension" to Triple("Dumbbells", "Beginner", "Seated backless bench"),
            "EZ-Bar Skull Crushers" to Triple("Barbell", "Intermediate", "Flat Bench & EZ Bar"),
            "Bodyweight Dips" to Triple("Bodyweight", "Advanced", "Parallel Dip Station"),
            "Close-Grip Bench Press" to Triple("Barbell", "Intermediate", "Flat Bench Bench Station"),
            "Single-Arm Cable Overhead" to Triple("Cable Machine", "Intermediate", "Low pulley level 1"),
            "Cable Kickbacks" to Triple("Cable Machine", "Intermediate", "Low pulley level 2 Column"),
            "Bench Dips" to Triple("Bodyweight", "Beginner", "Two Parallel Flat Bench Heights"),
            "Overhead Rope Extension Cable" to Triple("Cable Machine", "Beginner", "Low/High pulley adjustable"),
            "Triangle Floor Push-ups" to Triple("Bodyweight", "Beginner", "Floor floor mat"),
            "Machine Tricep Dip" to Triple("Chest Press", "Beginner", "Seated Selectorized Chest machine"),
            "Straight Bar Pushdown" to Triple("Cable Machine", "Beginner", "High pulley level 10 Column"),
            "Single-Arm D-handle Pushdown" to Triple("Cable Machine", "Intermediate", "High pulley station")
        )
        tricepNames.forEach { pair ->
            list.add(
                Exercise(
                    name = pair.first,
                    category = "Triceps",
                    difficulty = pair.second.second,
                    primaryMuscles = listOf("Triceps Brachii"),
                    secondaryMuscles = listOf("Anterior Deltoids", "Chest"),
                    equipment = pair.second.first,
                    machineSetup = pair.second.third,
                    seatAdjustment = "N/A",
                    pulleyPosition = if (pair.second.first == "Cable Machine" && pair.first.contains("Overhead")) "Level 2 (Low)" else if (pair.second.first == "Cable Machine") "Level 10 (Highest)" else "N/A",
                    attachment = if (pair.first.contains("Rope")) "Rope" else if (pair.first.contains("V-Bar")) "V-bar" else if (pair.first.contains("EZ-Bar") || pair.first.contains("Skull")) "EZ bar" else if (pair.first.contains("D-handle") || pair.first.contains("Single-Arm")) "D-handle" else if (pair.first.contains("Straight Bar")) "Straight bar" else "N/A",
                    instructions = listOf(
                        "Position yourself facing the high pulley with your chest slightly tilted.",
                        "Hold the rope or bar attachment at high chest level.",
                        "Squeeze your elbows tightly into your rib cage.",
                        "Push your hands down by flexing your triceps until arms are fully locked.",
                        "Let the attachment slowly slide back up under complete control."
                    ),
                    tips = listOf("Spread the rope apart at the bottom of the rep.", "Do not let your shoulders roll forward."),
                    mistakes = listOf("Allowing elbows to drift out to the sides.", "Swinging torso to complete deep reps."),
                    safetyWarning = "Brace your lower body. Use a controlled tempo.",
                    beginnerWeight = "15kg stack or 8kg dumbbell",
                    warmUpRoutine = "Warm-up pushdowns (15 reps at 50% max)",
                    stretchingRoutine = "Behind-neck overhead elbow stretch"
                )
            )
        }

        // --- LEGS (15) ---
        val legNames = listOf(
            "Barbell Back Squat" to Triple("Barbell", "Beginner", "Squat Rack Stand"),
            "Dumbbell Romanian Deadlift" to Triple("Dumbbells", "Beginner", "Open space"),
            "Heavy Leg Press" to Triple("Leg Press", "Beginner", "Sled Footboard Platform"),
            "Hack Squat Machine" to Triple("Hack Squat", "Intermediate", "Incline Hack Squat Plate loaded"),
            "Leg Extensions Machine" to Triple("Leg Extensions", "Beginner", "Seated Extension Pin Sled"),
            "Seated Leg Curl Machine" to Triple("Seated Leg Curl", "Beginner", "Seated Leg Curl Station"),
            "Standing Calf Raises" to Triple("Bodyweight", "Beginner", "Standing calf platform"),
            "Lying Leg Curl Bed" to Triple("Seated Leg Curl", "Beginner", "Lying Leg Curl Sled"),
            "Bulgarian Split Squat" to Triple("Dumbbells", "Intermediate", "Flat bench supporting hind-foot"),
            "Goblet Squat Front Loaded" to Triple("Dumbbells", "Beginner", "Standing spacing floor"),
            "Barbell Hip Thrust" to Triple("Barbell", "Intermediate", "Flat bench & barbell pad"),
            "Walking Dumbbell Lunges" to Triple("Dumbbells", "Beginner", "Long path space"),
            "Standing Calf Raise Machine" to Triple("Bodyweight", "Beginner", "Calf Raise Machine shoulder pad"),
            "Seated Calf Raise Machine" to Triple("Bodyweight", "Beginner", "Seated Calf machine with knee pad"),
            "Leg Press Calf PressSled" to Triple("Leg Press", "Intermediate", "Leg Press bottom edge")
        )
        legNames.forEach { pair ->
            list.add(
                Exercise(
                    name = pair.first,
                    category = "Legs",
                    difficulty = pair.second.second,
                    primaryMuscles = listOf("Quadriceps", "Hamstrings", "Gluteus Maximus"),
                    secondaryMuscles = listOf("Gastrocnemius (Calves)", "Adductors", "Lower Back"),
                    equipment = pair.second.first,
                    machineSetup = pair.second.third,
                    seatAdjustment = if (pair.second.first in listOf("Leg Press", "Leg Extensions", "Seated Leg Curl")) "Level 4 (Middle)" else "N/A",
                    pulleyPosition = "N/A",
                    attachment = "N/A",
                    instructions = listOf(
                        "Situate yourself correctly inside the machine or setup.",
                        "Grip the handles or load barbell securely across your back.",
                        "Set feet shoulder-width apart with toes pointed slightly outward.",
                        "Squat down or push weight by bending hips until glutes are deep.",
                        "Drive weight back up explosively by extending knees and hips together."
                    ),
                    tips = listOf("Keep your weight driven through your heels.", "Keep your knees in line with your toes."),
                    mistakes = listOf("Swaying knees inward (dynamic knee valgus).", "Lifting heels off the ground during squats."),
                    safetyWarning = "Never lock your knees out in Leg Press. Always utilize back safeties.",
                    beginnerWeight = "60kg in Leg Press or empty Squat bar",
                    warmUpRoutine = "Bodyweight air squats (15 reps), leg swings",
                    stretchingRoutine = "Deep quad stretch (30s) & toe touches"
                )
            )
        }

        // --- ABS (38) ---
        val absNames = listOf(
            // Original 13
            "Hanging Knee Raise" to Triple("Bodyweight", "Beginner", "Chin-up bar or Captains chair"),
            "Seated Cable Crunch" to Triple("Cable Machine", "Intermediate", "High pulley cable system"),
            "Captains Chair Leg Raise" to Triple("Bodyweight", "Beginner", "Captains Chair Rack"),
            "Ab Wheel Rollout" to Triple("Bodyweight", "Advanced", "Floor roll wheel"),
            "Decline Russian Twist" to Triple("Bodyweight", "Beginner", "Decline Sit-up Bench"),
            "High Plank Hold" to Triple("Bodyweight", "Beginner", "Floor mat space"),
            "Bicycle Floor Crunches" to Triple("Bodyweight", "Beginner", "Floor mat area"),
            "Hanging Straight Leg Raise" to Triple("Bodyweight", "Advanced", "High chin pull-up bar"),
            "Hanging Windshield Wipers" to Triple("Bodyweight", "Advanced", "High chin Pull bar"),
            "Cable Oblique Woodchopper" to Triple("Cable Machine", "Intermediate", "Mid pulley level 5"),
            "Swiss Ball Crunch" to Triple("Bodyweight", "Beginner", "Stability Swiss Ball"),
            "Oblique Heel Taps Floor" to Triple("Bodyweight", "Beginner", "Floor mat area"),
            "Abdominal Crunch Machine" to Triple("Bodyweight", "Beginner", "Seated Crunch dual machine"),
            
            // Additional exercises mapped from the user's provided URLs
            "45 Degree Side Bend" to Triple("Bodyweight", "Beginner", "45 degree back extension bench"),
            "Band Decline Sit-ups" to Triple("Resistance Band", "Intermediate", "Decline Sit-up Bench"),
            "Band Side Bend" to Triple("Resistance Band", "Beginner", "Standing flat floor"),
            "Band Standing Crunch" to Triple("Resistance Band", "Beginner", "Standing flat floor"),
            "Band Standing Lift" to Triple("Resistance Band", "Intermediate", "Standing flat floor"),
            "Band Twist (Up-down)" to Triple("Resistance Band", "Intermediate", "Standing flat floor"),
            "Band Twist" to Triple("Resistance Band", "Beginner", "Standing flat floor"),
            "Bench Leg Raise" to Triple("Bodyweight", "Beginner", "Flat bench"),
            "Cable Standing Lift" to Triple("Cable Machine", "Intermediate", "Low pulley system"),
            "Crunch (on bench)" to Triple("Bodyweight", "Beginner", "Flat bench"),
            "Dumbbell Side Bend" to Triple("Dumbbells", "Beginner", "Standing flat floor"),
            "Incline Leg Hip Raise" to Triple("Bodyweight", "Intermediate", "Incline bench"),
            "Landmine 180" to Triple("Barbell", "Advanced", "Landmine corner sleeve"),
            "Lying Floor Leg Raise" to Triple("Bodyweight", "Beginner", "Floor mat area"),
            "Lying Straight Leg Raise" to Triple("Bodyweight", "Beginner", "Floor mat area"),
            "Otis-ups" to Triple("Bodyweight", "Intermediate", "Floor mat area"),
            "Sit-ups" to Triple("Bodyweight", "Beginner", "Floor mat area"),
            "Spell Caster" to Triple("Dumbbells", "Advanced", "Standing flat floor"),
            "V-up" to Triple("Bodyweight", "Intermediate", "Floor mat area"),
            "Weighted Leg Extension Crunch" to Triple("Bodyweight", "Intermediate", "Flat bench"),
            
            // Stretching exercises in Abs category
            "Chin-to-chest Stretch" to Triple("Bodyweight", "Beginner", "Sitting or standing upright"),
            "Iron Cross Stretch" to Triple("Bodyweight", "Beginner", "Lying flat on floor"),
            "Seated Twist (straight arm)" to Triple("Bodyweight", "Beginner", "Sitting on flat bench"),
            "Spinal Stretch On Exercise Ball" to Triple("Bodyweight", "Beginner", "Stability Swiss Ball"),
            "Standing Side Bend (bent arm)" to Triple("Bodyweight", "Beginner", "Standing flat floor")
        )
        absNames.forEach { pair ->
            val vUrl = when (pair.first) {
                "Hanging Knee Raise" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Hanging%20Leg%20Hip%20Raise.mp4"
                "Seated Cable Crunch" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Cable%20Kneeling%20Crunch.mp4"
                "Captains Chair Leg Raise" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Vertical%20Leg%20Raise%20(on%20parallel%20bars).mp4"
                "Decline Russian Twist" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Bent%20Knee%20Lying%20Twist.mp4"
                "High Plank Hold" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Side%20Bridge%20-%20Side%20Plank.mp4"
                "Bicycle Floor Crunches" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Air%20Twisting%20Crunch.mp4"
                "Hanging Straight Leg Raise" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Hanging%20Straight%20Leg%20Raise.mp4"
                "Cable Oblique Woodchopper" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Cable%20Twist%20(up-down).mp4"
                "Swiss Ball Crunch" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Sit-up%20On%20Exercise%20Ball.mp4"
                "Oblique Heel Taps Floor" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Twisting%20Crunch.mp4"
                "Abdominal Crunch Machine" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Lever%20Seated%20Crunch.mp4"
                "45 Degree Side Bend" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/45%20Degree%20Side%20Bend.mp4"
                "Band Decline Sit-ups" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Band%20Decline%20Sit-ups.mp4"
                "Band Side Bend" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Band%20Side%20Bend.mp4"
                "Band Standing Crunch" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Band%20Standing%20Crunch.mp4"
                "Band Standing Lift" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Band%20Standing%20Lift.mp4"
                "Band Twist (Up-down)" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Band%20Twist%20(Up-down).mp4"
                "Band Twist" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Band%20Twist.mp4"
                "Bench Leg Raise" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Bench%20Leg%20Raise.mp4"
                "Cable Standing Lift" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Cable%20Standing%20Lift.mp4"
                "Crunch (on bench)" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Crunch%20(on%20bench).mp4"
                "Dumbbell Side Bend" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Dumbbell%20Side%20Bend.mp4"
                "Incline Leg Hip Raise" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Incline%20Leg%20Hip%20Raise.mp4"
                "Landmine 180" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Landmine%20180.mp4"
                "Lying Floor Leg Raise" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Lying%20Floor%20Leg%20Raise.mp4"
                "Lying Straight Leg Raise" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Lying%20Straight%20Leg%20Raise.mp4"
                "Otis-ups" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Otis-ups.mp4"
                "Sit-ups" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Sit-ups.mp4"
                "Spell Caster" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Spell%20Caster.mp4"
                "V-up" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/V-up.mp4"
                "Weighted Leg Extension Crunch" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Weighted%20Leg%20Extension%20Crunch.mp4"
                "Chin-to-chest Stretch" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Stretching%20-%20Chin-to-chest%20Stretch.mp4"
                "Iron Cross Stretch" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Stretching%20-%20Iron%20Cross%20Stretch.mp4"
                "Seated Twist (straight arm)" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Stretching%20-%20Seated%20Twist%20(straight%20arm).mp4"
                "Spinal Stretch On Exercise Ball" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Stretching%20-%20Spinal%20Stretch%20On%20Exercise%20Ball.mp4"
                "Standing Side Bend (bent arm)" -> "https://github.com/jpratap344-sketch/itsyou-exercise-assets/raw/0bf494d77212c0f495ebd632150530fbf8cff67d/Stretching%20-%20Standing%20Side%20Bend%20(bent%20arm).mp4"
                else -> null
            }
            list.add(
                Exercise(
                    name = pair.first,
                    category = "Abs",
                    difficulty = pair.second.second,
                    primaryMuscles = listOf("Rectus Abdominis"),
                    secondaryMuscles = listOf("Obliques", "Transverse Abdominis", "Hip Flexors"),
                    equipment = pair.second.first,
                    machineSetup = pair.second.third,
                    seatAdjustment = "N/A",
                    pulleyPosition = if (pair.first.contains("Cable")) "Level 8 (Crunch)" else if (pair.first.contains("Woodchopper")) "Level 5" else "N/A",
                    attachment = if (pair.first.contains("Cable")) "Rope" else if (pair.first.contains("Woodchopper")) "D-handle" else "N/A",
                    instructions = listOf(
                        "Prepare of your focus position on the floor or apparatus.",
                        "Contract your core, bringing your sternum toward your hips.",
                        "Breath all air out during maximal flexion of abs.",
                        "Squeeze your core tightly for 2 full seconds.",
                        "Slowly release under tension to fully stretch your rectus range."
                    ),
                    tips = listOf("Focus on spinal curling, not hip lifting.", "Exhale fully on every contraction."),
                    mistakes = listOf("Using hip flexors entirely instead of abs.", "Pulling neck with hands on crunches."),
                    safetyWarning = "Avoid pulling too hard on your spine with cables. Focus on abs engagement.",
                    beginnerWeight = "Bodyweight or 15kg stack on Cable Crunch",
                    warmUpRoutine = "Cat-cow spinal stretches, light bird-dog stabilization",
                    stretchingRoutine = "Cobra stretch on floor (45 seconds)",
                    videoUrl = vUrl
                )
            )
        }

        list
    }
}
