package com.phuong.myspa.data.api.model.search

import com.phuong.myspa.data.api.model.shop.ShopInfor

data class DataSearch(
    val `data`: List<Nothing>,
    val list_shop: MutableList<Search>?,
    val next_page: String,
    val prev_page: String
)