package com.phuong.myspa.utils

import android.content.Context
import com.phuong.myspa.data.api.model.Category
import com.phuong.myspa.data.api.model.remote.ApiResponse
import java.io.*

object CacheUtils {

    private const val FILE_NAME = "cache.txt"

    fun writeSaveCache(context: Context, response: ApiResponse<MutableList<Category>>): Boolean {
        val file = File(RootPath.getInstance().getCacheFolder(context), FILE_NAME)
        return try {
            FileOutputStream(file).use {
                ObjectOutputStream(it).writeObject(response)
            }
            true
        } catch (ex:Exception) {
            ex.printStackTrace()
            false
        }
    }

    fun readSaveCache(context: Context): ApiResponse<MutableList<Category>>? {
        val file = File(RootPath.getInstance().getCacheFolder(context), FILE_NAME)
        return try {
            FileInputStream(file).use {
                ObjectInputStream(it).readObject()
            } as ApiResponse<MutableList<Category>>
        } catch (ex:Exception) {
            ex.printStackTrace()
            null
        }
    }
}