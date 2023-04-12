package com.phuong.myspa.data.api.model.shop

data class DataShop(
    val `data`: List<Nothing>,
    val shops: MutableList<ShopInfor>?,
    val next_page: String,
    val prev_page: String
)