package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserProfile::class,
        WorkoutPlanEntity::class,
        PlanExerciseEntity::class,
        WorkoutLogEntity::class,
        JourneyMilestoneEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class DisciplineDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun journeyDao(): JourneyDao

    companion object {
        @Volatile
        private var INSTANCE: DisciplineDatabase? = null

        fun getDatabase(context: Context): DisciplineDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DisciplineDatabase::class.java,
                    "disiplin_app.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
