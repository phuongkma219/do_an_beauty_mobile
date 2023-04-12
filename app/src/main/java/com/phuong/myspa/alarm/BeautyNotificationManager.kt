package com.phuong.myspa.alarm
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import com.phuong.myspa.MainActivity
import com.phuong.myspa.R
import com.phuong.myspa.data.helpers.notification.NotificationBroadCastReceiver
import com.phuong.myspa.utils.Constants
import com.phuong.myspa.utils.Utils
import java.util.*
import com.phuong.myspa.data.api.model.reminder.Notification
import com.phuong.myspa.utils.Constants.KEY_NOTIFICATION_ID

class BeautyNotificationManager(private val context: Context) {

    private var mNotificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val CHANNEL_ID = "Daily_Box"
        const val ChannelName = "Daily Box"
    }

    init {
        if (Utils.isAndroidO() && !Utils.isAndroidTIRAMISU()) {
            createChannel()
        }
    }

    private fun createChannel() {
        if (Utils.isAndroidO()) {
            val channel = NotificationChannel(
                CHANNEL_ID, ChannelName, NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.enableLights(true)
            mNotificationManager.createNotificationChannel(channel)
        }
    }

    private fun createChannels(contentText: String) {
        if (Utils.isAndroidTIRAMISU()) {
            val channel = NotificationChannel(
                String.format(Locale.US, "%s_%s", CHANNEL_ID, contentText),
                ChannelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableLights(true)
            channel.enableVibration(true)
            mNotificationManager.createNotificationChannel(channel)

        }
    }

    fun notifyDailyBox(
        notificationDTO: Notification
    ) {
        if (Utils.isAndroidTIRAMISU()) {
            createChannels(notificationDTO.description)
        }
        val id = SystemClock.elapsedRealtime().toInt()
        mNotificationManager.cancel(id)
        mNotificationManager.notify(
            notificationDTO.id.toInt(), getNotification(
                mId = id,
                title = notificationDTO.title,
                contentText =notificationDTO.description
            ).build()
        )
    }


    private fun getNotification(
        mId: Int,
        title: String,
        contentText: String,
    ): NotificationCompat.Builder {
        val nb: NotificationCompat.Builder
        val resultPendingIntent: PendingIntent
        val resultIntent = Intent(context, MainActivity::class.java)
        resultIntent.putExtra(KEY_NOTIFICATION_ID, mId)

        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addNextIntentWithParentStack(resultIntent)
        resultPendingIntent =
            stackBuilder.getPendingIntent(mId, Utils.getPendingIntentFlag())
        ///
        val workerTag =
            "${context.resources.getString(R.string.reminder_worker_tag)}${mId}"
//        val ignoreIntent = Intent(context, NotificationBroadCastReceiver::class.java).apply {
//            action = context.resources.getString(R.string.ok_action)
//            putExtra(context.resources.getString(R.string.worker_tag), workerTag)
//            putExtra(context.resources.getString(R.string.reminder_id),mId)
//            putExtra(Constants.NOTIFICATION_ID, mId)
//        }
//
//        val ignorePendingIntent = PendingIntent.getBroadcast(
//            context, 0,
//            ignoreIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )


        val cancelIntent = Intent(context, NotificationBroadCastReceiver::class.java).apply {
            action = context.resources.getString(R.string.complete_action)
            putExtra(context.resources.getString(R.string.worker_tag), workerTag)
            putExtra(Constants.NOTIFICATION_ID, mId)
        }
        context.sendBroadcast(cancelIntent)

//        val cancelPendingIntent = PendingIntent.getBroadcast(
//            context, 0,
//            cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
        var notificationChannelId = ""

        notificationChannelId = if (Utils.isAndroidTIRAMISU()) {
            String.format(Locale.US, "%s_%s", CHANNEL_ID, mId)
        } else {
            CHANNEL_ID
        }
        nb = NotificationCompat.Builder(context, notificationChannelId).setContentTitle(title)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.logo)
            .setContentIntent(resultPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVibrate(longArrayOf(0L, 100L))
            .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_SOUND)
            .setSilent(false)
        return nb
    }
}
