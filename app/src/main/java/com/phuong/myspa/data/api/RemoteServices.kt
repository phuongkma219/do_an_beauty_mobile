package com.phuong.myspa.data.api

import com.phuong.myspa.data.api.model.Category
import com.phuong.myspa.data.api.model.FavoriteDT0
import com.phuong.myspa.data.api.model.QueryCategory
import com.phuong.myspa.data.api.model.comment.DataComment
import com.phuong.myspa.data.api.model.comment.UploadComment
import com.phuong.myspa.data.api.model.login.UserDTO
import com.phuong.myspa.data.api.model.login.UserLogin
import com.phuong.myspa.data.api.model.login.UserSignUp
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.model.shop.AddCart
import com.phuong.myspa.data.api.model.shop.DataShop
import com.phuong.myspa.data.api.model.shop.Shop
import com.phuong.myspa.data.api.model.shop.ShopInfor
import com.phuong.myspa.data.api.model.user.ImageUpload
import com.phuong.myspa.data.api.model.user.User
import okhttp3.MultipartBody
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

   @POST("/v1/shops/add_favorite")
   suspend fun addFavorite(@Header("Authorization") token: String,@Body favorite :FavoriteDT0):ApiResponse<Any>
    @POST("/v1/shops/delete_favorite")
    suspend fun deleteFavorite(@Header("Authorization") token: String,@Body favorite :FavoriteDT0):ApiResponse<Any>

    @GET("/v1/shops/get_list_favorite")
    suspend fun getListFavorite(@Header("Authorization") token: String):ApiResponse<MutableList<ShopInfor>>

    @GET("/v1/shops/get_list")
    suspend fun searchShops(@Query("keyword") keyword : String?,@Query("page") page: String):ApiResponse<DataShop>

    @POST("/v1/shops/upload_comment")
    suspend fun uploadComment(@Header("Authorization") token: String,@Body uploadComment: UploadComment):ApiResponse<Any>

    @GET("/v1/shops/comments_shop/{id}")
    suspend fun getCommentsShop(@Path("id") id:String,@Query("page") page : String):ApiResponse<DataComment>

    @Multipart
    @POST("/v1/upload/image")
    suspend fun uploadImage(
        @Header("Authorization") token: String?,
        @Part image: MultipartBody.Part?
    ): ApiResponse<ImageUpload>
    @POST("/v1/shops/add_cart")
    suspend fun addCart(@Header("Authorization") token: String?,@Body cart:AddCart):ApiResponse<Any>
}