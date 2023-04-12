package com.phuong.myspa.domain.repository.usecase

import com.phuong.myspa.data.api.model.reminder.Reminder
import com.phuong.myspa.domain.repository.ReminderRepository
import com.phuong.myspa.utils.Resource
import kotlinx.coroutines.flow.Flow

class GetReminderByIdUseCase(private val reminderRepository: ReminderRepository) {

    operator fun invoke(reminderId: Long): Flow<Resource<Reminder>> {
        return reminderRepository.getReminderById(reminderId = reminderId)
    }
}