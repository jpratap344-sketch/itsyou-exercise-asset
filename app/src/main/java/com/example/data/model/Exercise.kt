package com.example.data.model

data class Exercise(
    val name: String,
    val category: String, // "Chest", "Back", "Shoulders", "Biceps", "Triceps", "Legs", "Abs"
    val difficulty: String, // "Beginner", "Easy", "Medium", "Hard", "Advanced"
    val primaryMuscles: List<String>,
    val secondaryMuscles: List<String>,
    val equipment: String, // "Cable Machine", "Smith Machine", "Leg Press", "Hack Squat", "Pec Deck", "Chest Press", "Lat Pulldown", "Seated Row", "Assisted Pull-Up", "Dumbbells", "Barbell", "Bodyweight"
    val machineSetup: String,
    val seatAdjustment: String = "N/A",
    val pulleyPosition: String = "N/A",
    val attachment: String = "N/A", // "Rope", "V-bar", "EZ bar", "D-handle", "Straight bar", "N/A"
    val instructions: List<String>,
    val tips: List<String>,
    val mistakes: List<String>,
    val safetyWarning: String,
    val beginnerWeight: String,
    val warmUpRoutine: String,
    val stretchingRoutine: String,
    // Exact positions for vector drawings
    val handPlacement: String = "Medium grip on bar",
    val footPlacement: String = "Flat on ground",
    val heightAdjustment: String = "Align chest with resistance",
    val videoUrl: String? = null // Future video URL placeholder support
)
