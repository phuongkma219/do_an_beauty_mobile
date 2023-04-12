package com.phuong.myspa.domain.repository

import com.phuong.myspa.data.api.model.reminder.Reminder
import com.phuong.myspa.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {

    fun insertReminder(reminder: Reminder): Flow<Resource<Long>>

    fun deleteReminder(reminder: Reminder): Flow<Resource<Unit>>

    fun getReminderById(reminderId: Long): Flow<Resource<Reminder>>

    fun updateReminder(reminder: Reminder): Flow<Resource<Unit>>

    fun getReminders(): Flow<Resource<List<Reminder>>>
}