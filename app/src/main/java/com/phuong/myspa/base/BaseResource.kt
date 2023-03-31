package com.phuong.myspa.base

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

class BaseResource(ctx: Context) {

    var context: Context = ctx

    fun getString(resId: Int): String {
        return context.getString(resId)
    }

    fun getDrawable(resId: Int): Drawable? {
        return ContextCompat.getDrawable(context, resId)
    }

    fun getColor(resId: Int): Int {
        return ContextCompat.getColor(context, resId)
    }
}