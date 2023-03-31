package com.phuong.myspa.data.repository

import android.app.Application
import android.util.Log
import com.phuong.myspa.R
import com.phuong.myspa.data.PhotoModel
import com.phuong.myspa.utils.medialoader.MediaLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MediaStoreRepository(val app:Application) {
    suspend fun getImages(): List<PhotoModel> = withContext(Dispatchers.Default) {
        MediaLoader.loadAllImages(app)
    }

    suspend fun getAlbums(
        curAlbumId: Long,
        photoModels: List<PhotoModel>
    ): MutableList<PhotoModel> =
        withContext(Dispatchers.Default) {
            val albums = mutableListOf<PhotoModel>()
            if (photoModels.isNotEmpty()) {
                val allPhotoItem = PhotoModel(
                    photoModels[0].uri,
                    photoModels[0].file,
                    app.getString(R.string.all_images),
                    -1,
                    app.getString(R.string.all_images)
                )
                allPhotoItem.childCount = photoModels.size
                allPhotoItem.isChecked = curAlbumId == allPhotoItem.albumId
                Log.d("hhh", "curAlbumId: $curAlbumId -- allPhotoItem :${allPhotoItem.albumId}")
                albums.add(
                    allPhotoItem
                )
                val maps = photoModels.groupBy { it.albumId }
                if (maps.isNotEmpty()) {
                    maps.values.forEach {
                        it[0].childCount = it.size
                        it[0].isChecked = curAlbumId == it[0].albumId
                        albums.add(it[0])
                    }
                }
            }

            albums
        }

    suspend fun getAlbumDetail(
        photoModels: List<PhotoModel>,
        albumId: Long
    ): MutableList<PhotoModel> =
        withContext(Dispatchers.Default) {
            val albums = mutableListOf<PhotoModel>()
            if (photoModels.isNotEmpty()) {
                val maps = photoModels.filter { it.albumId == albumId }
                if (maps.isNotEmpty()) {
                    albums.addAll(maps)
                }
            }
            albums
        }
}