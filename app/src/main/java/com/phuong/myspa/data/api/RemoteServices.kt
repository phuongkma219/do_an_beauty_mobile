package com.phuong.myspa.data.api

import com.phuong.myspa.data.api.model.Category
import com.phuong.myspa.data.api.model.QueryCategory
import com.phuong.myspa.data.api.model.login.UserDTO
import com.phuong.myspa.data.api.model.login.UserLogin
import com.phuong.myspa.data.api.model.login.UserSignUp
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.model.shop.DataShop
import com.phuong.myspa.data.api.model.shop.Shop
import com.phuong.myspa.data.api.model.user.ImageUpload
import com.phuong.myspa.data.api.model.user.User
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RemoteServices {
    @POST("/auth/login")
    suspend fun userLogin(@Body userDTO: UserDTO): ApiResponse<UserLogin>

    @POST("/auth/register")
    suspend fun userRegister(@Body UserSignUp: UserSignUp): ApiResponse<Any>

    @GET("/v1/shops/get_cate")
    suspend fun getListCategory(): ApiResponse<MutableList<Category>>

    @POST("v1/shops/get_list_by_cate")
    suspend fun getShopsByCategory(
        @Body params: QueryCategory,
        @Query("page") page: String
    ): ApiResponse<DataShop>

    @GET("/v1/shops/get_detail")
    suspend fun getDetailShop(@Query("shop_id") shopId: String): ApiResponse<Shop>


    @GET("/v1/users/me")
    suspend fun getMyUser(@Header("Authorization") token: String):ApiResponse<User>

    @Multipart
    @POST("/v1/upload/avatar/{id}")
   suspend fun uploadAvatar(
        @Header("Authorization") token: String?,
        @Path("id") userId: String?,
        @Part image: MultipartBody.Part?
    ): ApiResponse<ImageUpload>

   @POST("/v1/users/me/update")
   suspend fun updateUser(@Header("Authorization") token: String,@Body user: User):ApiResponse<User>
}