package com.phuong.myspa.data.api.model.comment

data class DataComment(
    val comments: MutableList<Comment>?,
    val `data`: List<Any>,
    val next_page: String,
    val prev_page: String
)