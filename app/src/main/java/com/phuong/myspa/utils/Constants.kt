package com.phuong.myspa.utils

import com.phuong.myspa.MyApp
import com.phuong.myspa.R
import com.phuong.myspa.ui.popup.ActionModel

object Constants {
     const val BASE_URL = "https://ddf2-27-72-105-40.ap.ngrok.io"
     const val PREFIX_TOKEN = "Bearer "
     const val TITLE_NOTIFY = "My Spa"
     const val KEY_NOTIFICATION_ID = "ID_Notification"
     const val NOTIFICATION_ID = "NOTIFICATION_ID"
     const val ACTION_ALARM = "ACTION_ALARM"
     val actionsPopup by lazy {
          val titles = arrayListOf(
               MyApp.resource().getString(R.string.action_delete)
          )
          val icons = arrayListOf(
               R.drawable.ic_baseline_delete_outline_24,
          )
          val actions = mutableListOf<ActionModel>()
          titles.forEachIndexed { index, title ->
               val actionModel = ActionModel(icons[index], title)
               actions.add(actionModel)
          }

          actions
     }
    const val CLIENT_ID = "AVymvDCUSLQuksCEDwJJkSUD9lLYF1rx2jVg5Jpaf9BtIVWGVCs8031PlqWne0_Is9My80dVM6SriVM_"


//          android:usesCleartextTraffic="true"
//     android:networkSecurityConfig="@xml/network_security_config"
//const val BASE_URL = "http://10.0.2.2:3000"

}