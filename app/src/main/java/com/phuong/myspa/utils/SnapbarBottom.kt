package com.phuong.myspa.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.phuong.myspa.R
import com.phuong.myspa.databinding.LayoutCustomToastBinding

class SnapbarBottom constructor(contexts: Context){
    private var instance: SnapbarBottom? = null
    private var toast: Toast? = null
    private var context: Context? = null
    private var binding: LayoutCustomToastBinding? = null
    private val DEEP_COLORS =
        intArrayOf(R.color.md_green_500, R.color.md_red_500)
    private val BG_COLORS =
        intArrayOf(R.color.md_green_100 , R.color.md_red_100)
    private val ICONS = intArrayOf(
        R.drawable.ic_toast_done,
        R.drawable.ic_error

    )
    init {
        context = contexts
        binding = LayoutCustomToastBinding.inflate(LayoutInflater.from(context), null, false)
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
        toast!!.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 120)
        toast!!.setView(binding!!.getRoot())
        toast!!.setDuration(Toast.LENGTH_SHORT)
    }

    fun getInstance(context: Context): SnapbarBottom? {
        if (instance == null) {
            instance = SnapbarBottom(context)
        }
        return instance
    }
    fun showToast(messageType: MessageType, title: String?, message: String?) {
        binding!!.tvTitle.setText(title)
        binding!!.tvMsg.setText(message)
        applyTheme(binding, messageType)
        toast!!.show()
    }

    private fun applyTheme(binding: LayoutCustomToastBinding?, messageType: MessageType) {
        binding!!.startSpace.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                DEEP_COLORS[messageType.ordinal]
            )
        )
        binding.cardToast.setCardBackgroundColor(
            ContextCompat.getColor(
                context!!,
                BG_COLORS[messageType.ordinal]
            )
        )
        binding.imMessageType.setColorFilter(
            ContextCompat.getColor(
                context!!,
                DEEP_COLORS[messageType.ordinal]
            )
        )
        binding.imMessageType.setImageResource(ICONS[messageType.ordinal])
    }

    fun release() {
        if (instance != null) {
            instance = null
        }
    }

    enum class MessageType {
        Success, Error
    }
}