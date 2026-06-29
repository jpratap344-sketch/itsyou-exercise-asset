package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey val email: String,
    val name: String,
    val age: Int,
    val gender: String,
    val heightCm: Double,
    val weightKg: Double,
    val goalWeightKg: Double = 70.0,
    val activityLevel: String, // "Sedentary", "Active", "Very Active"
    val goal: String, // "Fat Loss", "Muscle Gain", "Strength", "Fitness"
    val streakCount: Int = 0,
    val lastWorkoutDate: String? = null
) : Serializable {

    val bmi: Double
        get() {
            val hM = heightCm / 100.0
            return if (hM > 0.0) weightKg / (hM * hM) else 22.0
        }

    val bmiCategory: String
        get() = when {
            bmi < 18.5 -> "Underweight"
            bmi < 25.0 -> "Healthy Range"
            bmi < 30.0 -> "Overweight"
            else -> "Obese"
        }

    val minHealthyWeight: Double
        get() {
            val hM = heightCm / 100.0
            return 18.5 * (hM * hM)
        }

    val maxHealthyWeight: Double
        get() {
            val hM = heightCm / 100.0
            return 24.9 * (hM * hM)
        }

    val bmr: Double
        get() = if (gender.lowercase() == "female") {
            (10.0 * weightKg) + (6.25 * heightCm) - (5.0 * age) - 161.0
        } else {
            (10.0 * weightKg) + (6.25 * heightCm) - (5.0 * age) + 5.0
        }

    val tdee: Double
        get() {
            val multiplier = when (activityLevel) {
                "Sedentary" -> 1.2
                "Active" -> 1.55
                "Very Active" -> 1.725
                else -> 1.3
            }
            return bmr * multiplier
        }

    val fatLossCalories: Double
        get() = tdee * 0.80

    val muscleGainCalories: Double
        get() = tdee * 1.10

    val waterGoal: Double
        get() = 0.035 * weightKg // 35 ml per kg bodyweight

    val stepGoal: Int
        get() = when (activityLevel) {
            "Sedentary" -> 5000
            "Active" -> 10000
            "Very Active" -> 15000
            else -> 8000
        }

    val workoutFrequency: String
        get() = when (activityLevel) {
            "Sedentary" -> "3 sessions / week"
            "Active" -> "4 sessions / week"
            "Very Active" -> "5 sessions / week"
            else -> "3-4 sessions / week"
        }
}

@Entity(tableName = "water_logs")
data class WaterLogEntity(
    @PrimaryKey val date: String, // e.g., "2026-06-06"
    val liters: Double,
    val targetLiters: Double
)

@Entity(tableName = "weight_logs")
data class WeightLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val weightKg: Double
)

@Entity(tableName = "workout_plans")
data class WorkoutPlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val exercises: String, // Comma-separated exercise names
    val difficulty: String, // "Beginner", "Intermediate", "Advanced"
    val durationMinutes: Int,
    val isCustom: Boolean = false
)

@Entity(tableName = "workout_logs")
data class WorkoutLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val planName: String,
    val date: String, // "2026-06-06"
    val durationMinutes: Int,
    val calories: Int,
    val completedCount: Int,
    val totalCount: Int,
    val personalRecords: String // Comma separated, like "Bench Press 100kg"
)

@Entity(tableName = "active_workout_state")
data class ActiveWorkoutStateEntity(
    @PrimaryKey val id: Int = 1,
    val planName: String,
    val totalExercises: Int,
    val exercises: String, // Comma-separated exercise names
    val estimatedLength: Int,
    val difficulty: String,
    val currentExerciseIndex: Int = 0,
    val currentSetCount: Int = 1,
    val countdownSeconds: Int = 3,
    val isStarted: Boolean = false,
    val isResting: Boolean = false,
    val restSecondsLeft: Int = 90,
    val elapsedSeconds: Int = 0,
    val completedExercisesCount: Int = 0,
    val isFinished: Boolean = false,
    val caloriesReward: Int = 0,
    val voiceNotification: String = "",
    val flashMessage: String = "",
    val isPaused: Boolean = false,
    val setTimerSeconds: Int = 0,
    val isSetTimerRunning: Boolean = false,
    val targetSetDuration: Int = 30
)
