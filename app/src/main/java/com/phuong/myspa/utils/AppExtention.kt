package com.phuong.myspa.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData

//suspend fun <T> Flow<DataResponse<MutableList<T>>>.onCollectPostValue(liveData: MutableLiveData<DataResponse<MutableList<T>>>) {
//    this.collect {
//        delay(200)
//        liveData.postValue(it)
//    }
//}

fun <T> MutableLiveData<T>.postSelf() {
    postValue(this.value)
}

fun <T> MutableLiveData<T>.setSelf() {
    value = this.value
}
fun hideKeyBoard(context: Context, v: View) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(v.windowToken, 0)
}

fun showKeyBoard(context: Context, v: View) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
}


