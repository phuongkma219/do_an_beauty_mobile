package com.phuong.myspa.utils

import com.phuong.myspa.data.Advertisement
import com.phuong.myspa.data.api.model.comment.Content

object DataUtils {
    val listBanner = mutableListOf<Advertisement>(
        Advertisement("1", Constants.BASE_URL+"/storage/images/banner1.jpg"),
        Advertisement("2", Constants.BASE_URL+"/storage/images/banner2.jpg"),
        Advertisement("3", Constants.BASE_URL+"/storage/images/banner3.jpg"),
        Advertisement("4", Constants.BASE_URL+"/storage/images/banner4.jpg"),
    )
    val listReport = mutableListOf(
        Content(text = "Address with signs of fraud!!"),
        Content(text = "There are images, offensive content")
    )

}