package com.phuong.myspa.data.api.model.comment

import com.phuong.myspa.data.api.model.user.User

data class Comment(
    val __v: Int?,
    val _id: String?,
    val content: Content?,
    val created_at: String?,
    val shop_id: String?,
    val updated_at: String?,
    val user: User?,
    val user_id: String?
)