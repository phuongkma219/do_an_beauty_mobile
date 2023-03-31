package com.phuong.myspa.data.api.model.login

import com.phuong.myspa.data.api.model.user.User


data class UserLogin(
    val access_token: String?= null,
    val expires_in: Int?= null,
    val user: User?= null


){
companion object{
    val emptyData = UserLogin(access_token="", expires_in =123,null)

}
}