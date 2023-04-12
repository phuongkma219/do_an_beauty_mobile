package com.phuong.testalarmclock.domain.usecase

import com.phuong.myspa.domain.repository.usecase.DeleteReminderUseCase
import com.phuong.myspa.domain.repository.usecase.GetReminderByIdUseCase

class ReminderUseCases(
    val deleteReminderUseCase: DeleteReminderUseCase,
    val insertReminderUseCase: InsertReminderUseCase,
    val getReminderByIdUseCase: GetReminderByIdUseCase,
    val updateReminderUseCase: UpdateReminderUseCase,
    val getRemindersUseCase: GetRemindersUseCase
)