package com.phuong.myspa.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.phuong.myspa.data.api.model.login.UserLogin
import org.intellij.lang.annotations.Language


class SharedPreferenceUtils private constructor(context: Context) {
    private val PREFERENCE_NAME = "MY_SPA"
    private val LANGUAGE ="LANGUAGE"
    private val DATA = "DATA"
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun putAcceptPolicy(value: Boolean){
        putBooleanValue("isAcceptPolicy",value)
    }

    fun getAcceptPolicy():Boolean{
        return getBooleanValue("isAcceptPolicy")
    }

    fun putStringValue(key: String?, value: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value).apply()
    }

    fun getStringValue(key: String?): String? {
        return sharedPreferences.getString(key, "")
    }

    fun putBooleanValue(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value).apply()
    }

    fun getBooleanValue(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun getBooleanValueWithTrueDefault(key: String): Boolean {
        return sharedPreferences.getBoolean(key, true)
    }
    fun writeLanguageCode(language: String){
        val editor = sharedPreferences.edit()
        editor.putString(LANGUAGE, language).apply()
    }
    fun readLanguageCode(): String {
        return sharedPreferences.getString(LANGUAGE, "en")!!
    }
    fun putData(data : UserLogin){
        val editor = sharedPreferences.edit()
        val gson = Gson()
        editor.putString(DATA,gson.toJson(data))
        editor.apply()
    }
    fun getData():UserLogin?{
        val gson = Gson()
        val json = sharedPreferences.getString(DATA, null)
        return gson.fromJson(json, UserLogin::class.java)
    }
    fun clearData(){
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object : SingletonHolder<SharedPreferenceUtils, Context>(::SharedPreferenceUtils)


}