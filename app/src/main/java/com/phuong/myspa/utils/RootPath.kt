package com.phuong.myspa.utils

import android.content.Context
import java.io.File

class RootPath private constructor() {
    companion object {
        private const val CACHE_DIR = "Cache"
        private var instance: RootPath? = null
        fun getInstance(): RootPath {
            if (instance == null) {
                instance = RootPath()
            }
            return instance!!
        }
    }

    fun getCacheFolder(context: Context): File {
        val file = File(context.cacheDir.absolutePath, CACHE_DIR)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }
}