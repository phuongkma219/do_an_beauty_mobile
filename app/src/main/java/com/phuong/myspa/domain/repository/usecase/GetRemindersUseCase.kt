package com.phuong.testalarmclock.domain.usecase

import com.phuong.myspa.data.api.model.reminder.Reminder
import com.phuong.myspa.domain.repository.ReminderRepository
import com.phuong.myspa.utils.Resource
import kotlinx.coroutines.flow.Flow

class GetRemindersUseCase(private val reminderRepository: ReminderRepository) {

    operator fun invoke(): Flow<Resource<List<Reminder>>> {
        return reminderRepository.getReminders()
    }
}