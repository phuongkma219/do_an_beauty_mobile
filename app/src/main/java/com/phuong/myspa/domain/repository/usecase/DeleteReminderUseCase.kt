package com.phuong.myspa.domain.repository.usecase

import com.phuong.myspa.data.api.model.reminder.Reminder
import com.phuong.myspa.domain.repository.ReminderRepository
import com.phuong.myspa.utils.Resource
import kotlinx.coroutines.flow.Flow

class DeleteReminderUseCase(private val reminderRepository: ReminderRepository) {

    operator fun invoke(reminder: Reminder): Flow<Resource<Unit>> {
        return reminderRepository.deleteReminder(reminder)
    }
}