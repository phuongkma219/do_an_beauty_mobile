package com.phuong.myspa.domain.repository.usecase

class ReminderUseCases(
    val deleteReminderUseCase: DeleteReminderUseCase,
    val insertReminderUseCase: InsertReminderUseCase,
    val getReminderByIdUseCase: GetReminderByIdUseCase,
    val updateReminderUseCase: UpdateReminderUseCase,
    val getRemindersUseCase: GetRemindersUseCase
)