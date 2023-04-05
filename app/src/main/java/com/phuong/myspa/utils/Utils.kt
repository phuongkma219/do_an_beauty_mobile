@file:Suppress("DEPRECATION")

package com.phuong.myspa.utils

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.*
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import com.phuong.myspa.R
import com.phuong.myspa.alarm.AlarmReceiver
import java.io.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


object Utils {
    private var STORAGE_PERMISSION_UNDER_STORAGE_SCOPE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )

    private var WRITE_SETTING_PERMISSION = arrayOf(
        Manifest.permission.WRITE_SETTINGS
    )
    private var RECORD_PERMISSION = arrayOf(
        Manifest.permission.RECORD_AUDIO,
    )

    private var STORAGE_PERMISSION_STORAGE_SCOPE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @RequiresApi(33)
    private var STORAGE_PERMISSION_STORAGE_SCOPE_TIRAMISU = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES
    )

    private var CAMERA_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    private var LOCATION_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    private const val REQUEST_CODE_PERMISSIONS = 10

    fun isAndroidQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    fun isAndroidTIRAMISU(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    fun isAndroidP(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }

    fun isAndroidO(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    fun isAndroidO_MR1(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1
    }

    fun isAndroidM(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    fun isAndroidR(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }
    fun isAndroidRS(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }
    fun isAndroidLOLLIPOP(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    fun getPendingIntentFlags(): Int {
        return if (isAndroidM()) {
            if (isAndroidQ()) {
                PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_IMMUTABLE
            }

        } else {
            PendingIntent.FLAG_ONE_SHOT
        }
    }

    fun getAlarmManagerFlags(): Int {
        return if (isAndroidM()) {
            if (isAndroidQ()) {
                AlarmManager.RTC_WAKEUP
            } else {
                AlarmManager.RTC_WAKEUP
            }

        } else {
            AlarmManager.RTC_WAKEUP
        }
    }

    fun writeSettingPermissionGrant(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.System.canWrite(context)
        } else {
            allPermissionGrant(context, getWritingPermission())
        }
    }

    fun storagePermissionGrant(context: Context): Boolean {
        return allPermissionGrant(context, getStoragePermissions())
    }

    fun recordPermissionGrant(context: Context): Boolean {
        return allPermissionGrant(context, getRecorderPermissions())
    }

    fun cameraPermissionsGranted(context: Context) = CAMERA_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            context, it
        ) == PackageManager.PERMISSION_GRANTED
    }
    fun locationPermissionGrant(context: Context): Boolean {
        return allPermissionGrant(context, getLocationPermissions())
    }


    //check all permission is granted
    private fun allPermissionGrant(context: Context, intArray: Array<String>): Boolean {
        var isGranted = true
        for (permission in intArray) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                isGranted = false
                break
            }
        }
        return isGranted
    }

    //get permission mapping with API Level
    fun getStoragePermissions(): Array<String> {
        return if (isAndroidR()) {
            if (isAndroidTIRAMISU()) {
                STORAGE_PERMISSION_STORAGE_SCOPE_TIRAMISU
            } else {
                STORAGE_PERMISSION_STORAGE_SCOPE
            }

        } else {
            STORAGE_PERMISSION_UNDER_STORAGE_SCOPE
        }
    }

    fun getRecorderPermissions(): Array<String> {
        return RECORD_PERMISSION
    }

    fun getWritingPermission(): Array<String> {
        return WRITE_SETTING_PERMISSION
    }

    fun getCameraPermission(): Array<String> {
        return CAMERA_PERMISSIONS
    }
    fun getLocationPermissions(): Array<String> {
        return LOCATION_PERMISSIONS
    }

    //show request permission to user
    private fun hasShowRequestPermissionRationale(
        activity: Activity?,
        vararg permissions: String?
    ): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity != null) {
            for (permission in permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        permission!!
                    )
                ) {
                    return true
                }
            }
        }
        return false
    }


    fun showOverOtherAppPermission(activity: Activity) {
        val intent = Intent()
        intent.action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        activity.startActivity(intent)
    }


    fun getBasePath(): String {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).absolutePath
    }

    fun requestWriteSettingPermission(activity: FragmentActivity, context: Context) {
        Toast.makeText(
            context,
            activity.resources?.getString(R.string.goto_settings),
            Toast.LENGTH_LONG
        ).show()

        val intent = Intent()
        intent.action = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.ACTION_MANAGE_WRITE_SETTINGS
        } else {
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        }
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
    }
    fun showAlertPermissionNotGrant(view: View, activity: Activity) {
        if (!hasShowRequestPermissionRationale(activity, *getStoragePermissions())) {
            val snackBar = Snackbar.make(
                view,
                activity.resources.getString(R.string.goto_settings),
                Snackbar.LENGTH_LONG
            )
            snackBar.setAction(
                activity.resources.getString(R.string.settings)
            ) { view: View? ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", activity.packageName, null)
                intent.data = uri
                activity.startActivity(intent)
            }
            snackBar.show()
        } else {
            Toast.makeText(
                activity,
                activity.resources.getString(R.string.grant_permission),
                Toast.LENGTH_SHORT
            ).show()
        }

    }
    fun checkImage(uri: Uri, context: Context): Boolean {
        val input: InputStream?
        try {
            input = context.contentResolver.openInputStream(uri)
        } catch (ex: Exception) {
            return false
        }
        val onlyBoundsOptions = BitmapFactory.Options()
        onlyBoundsOptions.inJustDecodeBounds = true
        onlyBoundsOptions.inDither = true

        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888

        BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
        input?.close()
        if (onlyBoundsOptions.outWidth == -1 || onlyBoundsOptions.outHeight == -1) {
            return false
        }
        return true
    }
    fun getTime(time: String?): String? {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val inputFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
            val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy", Locale.ENGLISH)
            try {
                val date = LocalDate.parse(time, inputFormatter)
                return outputFormatter.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            null
        }
    }
    fun getPendingIntentFlag(): Int {
        return if (isAndroidR()) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
    }
    fun startAlarm(context: Context,minute:Int,hour:Int,day:Int,month:Int,content:String) {
        val alarmReceiverIntent = Intent(context, AlarmReceiver::class.java)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        Log.d("kkk", "startAlarm: $content")
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2023)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        val bundle = Bundle()
        alarmReceiverIntent.action =
            String.format(Locale.US, "%s.%s", context.packageName, Constants.ACTION_ALARM)


        bundle.putString("IDS", content)
        alarmReceiverIntent.putExtras(bundle)
        val id = SystemClock.elapsedRealtime().toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            context, id, alarmReceiverIntent, getPendingIntentFlag()
        )
//        alarmManager.cancel(pendingIntent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP, calendar.timeInMillis,AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }

    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

}





