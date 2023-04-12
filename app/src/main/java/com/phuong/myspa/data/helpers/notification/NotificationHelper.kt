package com.phuong.myspa.data.helpers.notification

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.phuong.myspa.MainActivity
import com.phuong.myspa.R
import com.phuong.myspa.alarm.BeautyNotificationManager
import com.phuong.myspa.data.api.model.reminder.Notification
import com.phuong.myspa.utils.Constants

class NotificationHelper {

    fun createNotification(
        context: Context,
        notificationDTO: Notification
    ) {
        BeautyNotificationManager(context).notifyDailyBox(notificationDTO)
    }

    fun removeNotification(id: Int, context: Context){
        with(NotificationManagerCompat.from(context)) {
            cancel(id)
        }
    }
}
