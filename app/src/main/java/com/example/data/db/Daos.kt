package com.example.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FitnessDao {

    // User Profile
    @Query("SELECT * FROM user_profiles LIMIT 1")
    fun getProfileFlow(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profiles LIMIT 1")
    suspend fun getProfileDirect(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    // Water Log
    @Query("SELECT * FROM water_logs WHERE date = :date LIMIT 1")
    fun getWaterLogFlow(date: String): Flow<WaterLogEntity?>

    @Query("SELECT * FROM water_logs WHERE date = :date LIMIT 1")
    suspend fun getWaterLogDirect(date: String): WaterLogEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateWaterLog(log: WaterLogEntity)

    @Query("SELECT * FROM water_logs ORDER BY date DESC LIMIT 7")
    fun getRecentWaterLogs(): Flow<List<WaterLogEntity>>

    // Weight Log
    @Query("SELECT * FROM weight_logs ORDER BY date ASC")
    fun getAllWeightLogs(): Flow<List<WeightLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightLog(weight: WeightLogEntity)

    @Query("DELETE FROM weight_logs WHERE id = :id")
    suspend fun deleteWeightLogById(id: Int)

    // Workout Plans
    @Query("SELECT * FROM workout_plans ORDER BY id DESC")
    fun getAllWorkoutPlans(): Flow<List<WorkoutPlanEntity>>

    @Query("SELECT * FROM workout_plans WHERE id = :id LIMIT 1")
    suspend fun getPlanById(id: Int): WorkoutPlanEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: WorkoutPlanEntity): Long

    @Update
    suspend fun updatePlan(plan: WorkoutPlanEntity)

    @Query("DELETE FROM workout_plans WHERE id = :id")
    suspend fun deletePlanById(id: Int)

    // Workout Logs
    @Query("SELECT * FROM workout_logs ORDER BY date DESC")
    fun getAllWorkoutLogs(): Flow<List<WorkoutLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutLog(log: WorkoutLogEntity)

    // Active Workout State Persistence
    @Query("SELECT * FROM active_workout_state LIMIT 1")
    suspend fun getActiveWorkoutStateDirect(): ActiveWorkoutStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveActiveWorkoutState(state: ActiveWorkoutStateEntity)

    @Query("DELETE FROM active_workout_state")
    suspend fun clearActiveWorkoutState()
}
