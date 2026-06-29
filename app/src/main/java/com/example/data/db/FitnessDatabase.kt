package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserProfileEntity::class,
        WaterLogEntity::class,
        WeightLogEntity::class,
        WorkoutPlanEntity::class,
        WorkoutLogEntity::class,
        ActiveWorkoutStateEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class FitnessDatabase : RoomDatabase() {

    abstract fun fitnessDao(): FitnessDao

    companion object {
        @Volatile
        private var INSTANCE: FitnessDatabase? = null

        fun getDatabase(context: Context): FitnessDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitnessDatabase::class.java,
                    "itsyou_fitness_db"
                )
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Background population of default plans
                        CoroutineScope(Dispatchers.IO).launch {
                            val emptyPlanCount = getDatabase(context).fitnessDao()
                            // Note: Pre-population is handled in Repository dynamically to prevent race conditions.
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
