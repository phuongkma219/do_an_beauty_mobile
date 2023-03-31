package com.phuong.myspa.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.phuong.myspa.utils.Constants
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    private val TIME_SYSTEM_CHANGE_LIST = arrayOf(
        Intent.ACTION_BOOT_COMPLETED,
        Intent.ACTION_TIMEZONE_CHANGED,
        Intent.ACTION_LOCALE_CHANGED,
        "android.intent.action.TIME_SET",
        Intent.ACTION_MY_PACKAGE_REPLACED
    )

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        val alarmAction =
            String.format(Locale.US, "%s.%s", context?.packageName, Constants.ACTION_ALARM)
        Log.d("kkk", "onReceive: vao day ")

        if (action != null) {
            if (action.equals(alarmAction)) {
                    val alarmJson = intent.extras?.getString("IDS")
                    if (alarmJson?.isNotEmpty() == true) {
                        BeautyNotificationManager(context!!).notifyDailyBox(alarmJson)
                    }
            }
            else if (listOf(*TIME_SYSTEM_CHANGE_LIST).contains(action)) {
                findNextEvent(context!!)
            }

        }

    }

    private fun findNextEvent(context: Context) {
       try{
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//               Utils.startAlarm(context)
           }
       } catch (ex: Exception) {

        }
    }


}