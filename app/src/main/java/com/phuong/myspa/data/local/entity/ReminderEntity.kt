package com.phuong.myspa.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.phuong.myspa.data.api.model.reminder.RemindType
import com.phuong.myspa.data.api.model.reminder.Reminder
import java.time.Instant

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val title: String,
    val description: String,
    val reminderStart: Instant,
    val reminderEnd: Instant,
    val remindType: RemindType,
    val hasCompleted: Boolean = false,
    val completedOn: Instant? = null,
    val hasCanceled: Boolean = false
) {
    fun toReminder(): Reminder {
        return Reminder(
            id ?: -1L,
            title,
            description,
            reminderStart,
            reminderEnd,
            remindType,
            hasCompleted,
            completedOn,
            hasCanceled
        )
    }
}