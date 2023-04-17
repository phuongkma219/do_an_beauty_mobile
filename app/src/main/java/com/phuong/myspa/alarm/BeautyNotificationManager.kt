package com.phuong.myspa.alarm

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import com.phuong.myspa.MainActivity
import com.phuong.myspa.R
import com.phuong.myspa.utils.Constants
import com.phuong.myspa.utils.Utils
import java.util.*

class BeautyNotificationManager(private val context: Context) {

    private var mNotificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val CHANNEL_ID = "My Spa"
        const val ChannelName = "My Spa"
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

    fun notifyDailyBox(contentText: String) {
        if (Utils.isAndroidTIRAMISU()) {
            createChannels(contentText)
        }
        val id = SystemClock.elapsedRealtime().toInt()
        mNotificationManager.cancel(id)
        mNotificationManager.notify(
            id, getNotification(
                mId = id,
                title = Constants.TITLE_NOTIFY,
                contentText =contentText
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
        resultIntent.putExtra(Constants.KEY_NOTIFICATION_ID, mId)

        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addNextIntentWithParentStack(resultIntent)
        resultPendingIntent =
            stackBuilder.getPendingIntent(mId, Utils.getPendingIntentFlag())
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
            .setPriority(Notification.PRIORITY_MAX)
            .setVibrate(longArrayOf(0L, 100L))
            .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setSilent(false)
        return nb
    }
}
