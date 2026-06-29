package com.example.data.repository

import com.example.data.db.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class FitnessRepository(private val fitnessDao: FitnessDao) {

    // Profile Flow
    val userProfile: Flow<UserProfileEntity?> = fitnessDao.getProfileFlow()

    suspend fun saveProfile(profile: UserProfileEntity) = withContext(Dispatchers.IO) {
        fitnessDao.insertOrUpdateProfile(profile)
    }

    suspend fun getProfileDirect(): UserProfileEntity? = withContext(Dispatchers.IO) {
        fitnessDao.getProfileDirect()
    }

    // Water Input
    fun getWaterLog(date: String): Flow<WaterLogEntity?> = fitnessDao.getWaterLogFlow(date)

    suspend fun logWater(date: String, deltaMl: Double, currentTargetLiters: Double) = withContext(Dispatchers.IO) {
        val existing = fitnessDao.getWaterLogDirect(date)
        val currentLiters = existing?.liters ?: 0.0
        val target = existing?.targetLiters ?: currentTargetLiters
        val updated = WaterLogEntity(
            date = date,
            liters = (currentLiters + (deltaMl / 1000.0)).coerceAtLeast(0.0),
            targetLiters = target
        )
        fitnessDao.insertOrUpdateWaterLog(updated)
    }

    val recentWaterLogs: Flow<List<WaterLogEntity>> = fitnessDao.getRecentWaterLogs()

    // Weight Records
    val allWeightLogs: Flow<List<WeightLogEntity>> = fitnessDao.getAllWeightLogs()

    suspend fun logWeight(date: String, weightKg: Double) = withContext(Dispatchers.IO) {
        val log = WeightLogEntity(date = date, weightKg = weightKg)
        fitnessDao.insertWeightLog(log)
    }

    suspend fun deleteWeightLog(id: Int) = withContext(Dispatchers.IO) {
        fitnessDao.deleteWeightLogById(id)
    }

    // Workout Plans
    val allWorkoutPlans: Flow<List<WorkoutPlanEntity>> = fitnessDao.getAllWorkoutPlans()

    suspend fun saveWorkoutPlan(plan: WorkoutPlanEntity): Long = withContext(Dispatchers.IO) {
        if (plan.id == 0) {
            fitnessDao.insertPlan(plan)
        } else {
            fitnessDao.updatePlan(plan)
            plan.id.toLong()
        }
    }

    suspend fun deleteWorkoutPlan(id: Int) = withContext(Dispatchers.IO) {
        fitnessDao.deletePlanById(id)
    }

    suspend fun getPlanById(id: Int): WorkoutPlanEntity? = withContext(Dispatchers.IO) {
        fitnessDao.getPlanById(id)
    }

    // Completed Workout Logs & Streak Calculator
    val allWorkoutLogs: Flow<List<WorkoutLogEntity>> = fitnessDao.getAllWorkoutLogs()

    suspend fun logCompletedWorkout(
        planName: String,
        date: String,
        durationMinutes: Int,
        calories: Int,
        completedCount: Int,
        totalCount: Int,
        personalRecords: String
    ) = withContext(Dispatchers.IO) {
        val log = WorkoutLogEntity(
            planName = planName,
            date = date,
            durationMinutes = durationMinutes,
            calories = calories,
            completedCount = completedCount,
            totalCount = totalCount,
            personalRecords = personalRecords
        )
        fitnessDao.insertWorkoutLog(log)

        // Increment or calculate streak
        val profile = fitnessDao.getProfileDirect()
        if (profile != null) {
            val lastDate = profile.lastWorkoutDate
            var currentStreak = profile.streakCount
            
            if (lastDate == null) {
                currentStreak = 1
            } else {
                try {
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val last = sdf.parse(lastDate)
                    val today = sdf.parse(date)
                    if (last != null && today != null) {
                        val diff = today.time - last.time
                        val diffDays = diff / (24 * 60 * 60 * 1000)
                        if (diffDays == 1L) {
                            currentStreak += 1
                        } else if (diffDays > 1L) {
                            currentStreak = 1
                        }
                        // if diffDays == 0L (same day workout), streak remains unchanged
                    }
                } catch (e: Exception) {
                    currentStreak = 1
                }
            }
            
            val updatedProfile = profile.copy(
                streakCount = currentStreak,
                lastWorkoutDate = date
            )
            fitnessDao.insertOrUpdateProfile(updatedProfile)
        }
    }

    suspend fun getActiveWorkoutStateDirect(): ActiveWorkoutStateEntity? = withContext(Dispatchers.IO) {
        fitnessDao.getActiveWorkoutStateDirect()
    }

    suspend fun saveActiveWorkoutState(state: ActiveWorkoutStateEntity) = withContext(Dispatchers.IO) {
        fitnessDao.saveActiveWorkoutState(state)
    }

    suspend fun clearActiveWorkoutState() = withContext(Dispatchers.IO) {
        fitnessDao.clearActiveWorkoutState()
    }

    // Initialize Default Seed Plans
    suspend fun prepopulateDefaultPlans() = withContext(Dispatchers.IO) {
        val plans = fitnessDao.getAllWorkoutPlans().firstOrNull() ?: emptyList()
        if (plans.isEmpty()) {
            val p1 = WorkoutPlanEntity(
                name = "Beginner Full-Body Blast",
                exercises = "Bench Press,Classic Push-Ups,Lat Pulldown,Seated Dumbbell Press,Dumbbell Hammer Curl,Rope Pushdown,Goblet Squat Front Loaded,Bicycle Floor Crunches",
                difficulty = "Beginner",
                durationMinutes = 45,
                isCustom = false
            )
            val p2 = WorkoutPlanEntity(
                name = "Intermediate Upper Split",
                exercises = "Incline Dumbbell Press,Seated Cable Row,Seated Dumbbell Press,EZ-Bar Skull Crushers,Preacher Curl EZ-Bar,Cable Crossover High-to-Low",
                difficulty = "Intermediate",
                durationMinutes = 75,
                isCustom = false
            )
            val p3 = WorkoutPlanEntity(
                name = "Advanced Iron Gladiator",
                exercises = "Barbell Back Squat,Dumbbell Romanian Deadlift,Heavy Leg Press,Barbell Row,Lat Pulldown,Bench Press,Overhead Barbell Press,Hanging Straight Leg Raise",
                difficulty = "Advanced",
                durationMinutes = 105,
                isCustom = false
            )
            fitnessDao.insertPlan(p1)
            fitnessDao.insertPlan(p2)
            fitnessDao.insertPlan(p3)
        }
    }

    // Water Requirement Calculator based on weight
    fun calculateRequiredWater(weightKg: Double, heightCm: Double, gender: String, activity: String): Double {
        return String.format(Locale.US, "%.2f", 0.035 * weightKg).toDouble()
    }
}
