package com.phuong.myspa.data.helpers.notification

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
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
        val actionIntent = Intent(context, MainActivity::class.java)
        val actionPendingIntent = PendingIntent.getActivity(
            context, 0,
            actionIntent, PendingIntent.FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
        )

        val notificationId = notificationDTO.id.toInt()

        val workerTag =
            "${context.resources.getString(R.string.reminder_worker_tag)}${notificationDTO.id}"

        val ignoreIntent = Intent(context, NotificationBroadCastReceiver::class.java).apply {
            action = context.resources.getString(R.string.ok_action)
            putExtra(context.resources.getString(R.string.worker_tag), workerTag)
            putExtra(context.resources.getString(R.string.reminder_id), notificationDTO.id)
            putExtra(Constants.NOTIFICATION_ID, notificationId)
        }

        val ignorePendingIntent = PendingIntent.getBroadcast(
            context, 0,
            ignoreIntent, PendingIntent.FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
        )


        val cancelIntent = Intent(context, NotificationBroadCastReceiver::class.java).apply {
            action = context.resources.getString(R.string.complete_action)
            putExtra(context.resources.getString(R.string.worker_tag), workerTag)
            putExtra(Constants.NOTIFICATION_ID, notificationId)
        }

        val cancelPendingIntent = PendingIntent.getBroadcast(
            context, 0,
            cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
        )
        BeautyNotificationManager(context).notifyDailyBox(notificationDTO.description)

//        val builder =
//            NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id))
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(notificationDTO.title)
//                .setContentText(notificationDTO.description)
//                .setColor(context.getColor(R.color.purple_500))
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(actionPendingIntent)
//                .addAction(R.mipmap.ic_launcher, context.getString(R.string.ok), ignorePendingIntent)
//                .addAction(R.mipmap.ic_launcher, context.getString(R.string.done), cancelPendingIntent)
//                .setAutoCancel(true)
//
//        with(NotificationManagerCompat.from(context)) {
//            notify(notificationId, builder.build())
//        }
    }

    fun removeNotification(id: Int, context: Context){
        with(NotificationManagerCompat.from(context)) {
            cancel(id)
        }
    }
}
