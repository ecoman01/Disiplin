package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getUserProfileSync(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(profile: UserProfile)

    @Update
    suspend fun update(profile: UserProfile)

    @Query("UPDATE user_profile SET name = :name WHERE id = 1")
    suspend fun updateName(name: String)
}

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workout_plans ORDER BY isCustom ASC, id ASC")
    fun getAllPlans(): Flow<List<WorkoutPlanEntity>>

    @Query("SELECT * FROM workout_plans ORDER BY isCustom ASC, id ASC")
    suspend fun getAllPlansSync(): List<WorkoutPlanEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: WorkoutPlanEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<PlanExerciseEntity>)

    @Query("SELECT * FROM plan_exercises WHERE planId = :planId ORDER BY orderIndex ASC")
    fun getExercisesForPlan(planId: Long): Flow<List<PlanExerciseEntity>>

    @Query("SELECT * FROM plan_exercises WHERE planId = :planId ORDER BY orderIndex ASC")
    suspend fun getExercisesForPlanSync(planId: Long): List<PlanExerciseEntity>

    @Query("UPDATE plan_exercises SET lastWeightKg = :weightKg WHERE id = :exerciseId")
    suspend fun updateExerciseWeight(exerciseId: Long, weightKg: Float)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutLog(log: WorkoutLogEntity): Long

    @Query("SELECT * FROM workout_logs ORDER BY timestamp DESC")
    fun getAllWorkoutLogs(): Flow<List<WorkoutLogEntity>>

    @Query("SELECT * FROM workout_logs ORDER BY timestamp DESC LIMIT 50")
    suspend fun getRecentWorkoutLogs(): List<WorkoutLogEntity>

    @Query("SELECT COUNT(*) FROM workout_plans")
    suspend fun getPlanCount(): Int

    @Query("SELECT * FROM workout_plans WHERE dayOfWeek = :dayOfWeek AND isCustom = 0 LIMIT 1")
    suspend fun getPlanForDay(dayOfWeek: Int): WorkoutPlanEntity?

    @Query("DELETE FROM workout_plans WHERE isCustom = 0")
    suspend fun clearDefaultPlans()
}

@Dao
interface JourneyDao {
    @Query("SELECT * FROM journey_milestones ORDER BY orderIndex ASC")
    fun getAllMilestones(): Flow<List<JourneyMilestoneEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestones(milestones: List<JourneyMilestoneEntity>)

    @Query("UPDATE journey_milestones SET isUnlocked = 1, dateText = :dateText WHERE id = :id")
    suspend fun unlockMilestone(id: String, dateText: String)

    @Query("UPDATE journey_milestones SET title = :title, subtitle = :subtitle, iconEmoji = :iconEmoji WHERE id = :id")
    suspend fun updateTitleAndSubtitle(id: String, title: String, subtitle: String, iconEmoji: String)

    @Query("SELECT COUNT(*) FROM journey_milestones")
    suspend fun getMilestoneCount(): Int
}
