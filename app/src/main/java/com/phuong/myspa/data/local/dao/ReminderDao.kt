package com.phuong.myspa.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.phuong.myspa.data.local.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ReminderDao {

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity): Long

    @Query("DELETE FROM reminders WHERE id == :reminderId")
    suspend fun deleteReminder(reminderId: Long)

    @Query("SELECT * FROM reminders WHERE id == :reminderId")
    suspend fun getReminderById(reminderId: Long): ReminderEntity

    @Query("SELECT * FROM reminders")
    fun getReminders(): Flow<List<ReminderEntity>>
}