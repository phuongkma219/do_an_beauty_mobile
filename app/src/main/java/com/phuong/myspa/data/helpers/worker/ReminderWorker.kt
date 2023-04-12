package com.phuong.myspa.data.helpers.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.phuong.myspa.R
import com.phuong.myspa.data.api.model.reminder.Notification
import com.phuong.myspa.data.api.model.reminder.RemindType
import com.phuong.myspa.data.api.model.reminder.Reminder
import com.phuong.myspa.data.helpers.notification.NotificationHelper
import com.phuong.myspa.data.local.dao.ReminderDao
import com.phuong.myspa.data.local.entity.ReminderEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val reminderDao: ReminderDao,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(appContext, workerParams) {

    private val reminderWorkManager = ReminderWorkManagerRepository(reminderDao)

    override suspend fun doWork(): Result {


        val reminderId =
            inputData.getLong(appContext.getString(R.string.reminder_instance_key), -1)
        if (reminderId == -1L) {
            return Result.failure()
        }
        val reminder: Reminder = reminderDao.getReminderById(reminderId).toReminder()

        LocalDateTime.ofInstant(reminder.reminderStart, ZoneOffset.UTC)

        notificationHelper.removeNotification(reminderId.toInt(),appContext)

        notificationHelper.createNotification(
            appContext,
            Notification(
                title = reminder.title,
                description = reminder.description,
                id = reminderId,
            )
        )

        if (reminder.remindType != RemindType.NONE) {
            reminderWorkManager.createWorkRequestAndEnqueue(
                reminderId = reminderId,
                context = appContext,
                isFirstTime = false,
                time = reminder.reminderStart
            )
        } else {
            val newReminder = reminder.copy(hasCompleted = true, completedOn = Instant.now())
            reminderDao.insertReminder(
                reminder = ReminderEntity(
                    newReminder.id,
                    newReminder.title,
                    newReminder.shopId,
                    newReminder.serviceId,
                    newReminder.serviceName,
                    newReminder.description,
                    newReminder.reminderStart,
                    newReminder.reminderEnd,
                    newReminder.remindType,
                    newReminder.hasCompleted,
                    newReminder.completedOn,
                    newReminder.hasCanceled
                )
            )
        }
        return Result.success()
    }
}
