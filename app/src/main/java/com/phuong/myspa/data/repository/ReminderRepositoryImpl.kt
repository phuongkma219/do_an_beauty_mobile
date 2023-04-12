package com.phuong.myspa.data.repository

import android.content.Context
import com.phuong.myspa.R
import com.phuong.myspa.data.api.model.reminder.Reminder
import com.phuong.myspa.data.helpers.notification.NotificationHelper
import com.phuong.myspa.data.helpers.worker.ReminderWorkManagerRepository
import com.phuong.myspa.data.local.dao.ReminderDao
import com.phuong.myspa.data.local.entity.ReminderEntity
import com.phuong.myspa.domain.repository.ReminderRepository
import com.phuong.myspa.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ReminderRepositoryImpl(
    private val reminderDao: ReminderDao,
    private val appContext: Context
) : ReminderRepository {

    private val reminderWorkManager = ReminderWorkManagerRepository(reminderDao)

    override fun insertReminder(reminder: Reminder): Flow<Resource<Long>> {

        return flow {
            emit(Resource.Loading<Long>())
            val reminderEntity = ReminderEntity(
                reminderStart = reminder.reminderStart,
                reminderEnd = reminder.reminderEnd,
                remindType = reminder.remindType,
                shopId = reminder.shopId,
                serviceId = reminder.serviceId,
                serviceName = reminder.serviceName,
                title = reminder.title,
                description = reminder.description,
                hasCompleted = reminder.hasCompleted,
                completedOn = reminder.completedOn,
                hasCanceled = reminder.hasCanceled
            )
            val id = reminderDao.insertReminder(reminderEntity)

            reminderWorkManager.createWorkRequestAndEnqueue(
                appContext,
                reminderId = id,
                time = reminderEntity.reminderStart
            )

            emit(Resource.Success<Long>(id))
        }
    }

    override fun deleteReminder(reminder: Reminder): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading<Unit>())
            reminderDao.deleteReminder(reminder.id)
            val tag = "${appContext.getString(R.string.reminder_worker_tag)}${reminder.id}"
            reminderWorkManager.cancelWorkRequest(appContext, tag)
            NotificationHelper().removeNotification(reminder.id.toInt(),appContext)
            emit(Resource.Success<Unit>(null))
        }
    }

    override fun getReminderById(reminderId: Long): Flow<Resource<Reminder>> {
        return flow {
            emit(Resource.Loading<Reminder>())
            val reminderEntity = reminderDao.getReminderById(reminderId)
            emit(Resource.Success<Reminder>(reminderEntity.toReminder()))
        }
    }

    override fun updateReminder(reminder: Reminder): Flow<Resource<Unit>> {
        return flow { }
    }

    override fun getReminders(): Flow<Resource<List<Reminder>>> {
        return flow {
            emit(Resource.Loading<List<Reminder>>())
            reminderDao.getReminders().collect { reminderList ->
                emit(
                    Resource.Success<List<Reminder>>(reminderList.map { it.toReminder() })
                )
            }
        }
    }
}