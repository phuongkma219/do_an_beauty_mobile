package com.phuong.myspa.data.api

import com.phuong.myspa.data.api.model.Category
import com.phuong.myspa.data.api.model.ChangePassDTO
import com.phuong.myspa.data.api.model.FavoriteDT0
import com.phuong.myspa.data.api.model.QueryCategory
import com.phuong.myspa.data.api.model.cart.CartDTO
import com.phuong.myspa.data.api.model.cart.DataCart
import com.phuong.myspa.data.api.model.comment.DataComment
import com.phuong.myspa.data.api.model.comment.UploadComment
import com.phuong.myspa.data.api.model.history.History
import com.phuong.myspa.data.api.model.history.HistoryDTO
import com.phuong.myspa.data.api.model.login.UserDTO
import com.phuong.myspa.data.api.model.login.UserLogin
import com.phuong.myspa.data.api.model.login.UserSignUp
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.model.search.DataSearch
import com.phuong.myspa.data.api.model.shop.*
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
    suspend fun getDetailShop(@Header("Authorization") token: String,@Query("shop_id") shopId: String): ApiResponse<Shop>


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
    suspend fun searchShops(@Query("keyword") keyword : String?,@Query("page") page: String):ApiResponse<DataSearch>

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
    suspend fun addCart(@Header("Authorization") token: String?,@Body cart: CartDTO):ApiResponse<Any>

    @GET("/v1/shops/get_list_cart")
    suspend fun getListCart(@Header("Authorization") token: String?):ApiResponse<MutableList<DataCart>>?

    @POST("/v1/shops/delete_cart")
    suspend fun deleteCart(@Header("Authorization") token: String?,@Body cart: CartDTO):ApiResponse<Any>

    @POST("/v1/shops/add_history")
    suspend fun addHistory(@Header("Authorization") token: String?,@Body historyDTO: HistoryDTO):ApiResponse<Any>

    @GET("/v1/shops/get_list_history")
    suspend fun getListHistory(@Header("Authorization") token: String?):ApiResponse<MutableList<History>>?

    @GET("/v1/shops/get_detail_service")
    suspend fun getDetailService(@Query("_id")  id:String,):ApiResponse<ShopService>?

    @GET("/v1/shops/get_detail_history")
    suspend fun getDetailHistory(@Header("Authorization") token: String?,@Query("_id")  id:String,@Query("shop_id")  shopId:String,@Query("service_id")  serviceId:String):ApiResponse<History?>

    @POST("/v1/shops/delete_history")
    suspend fun deleteHistory(@Header("Authorization") token: String?,@Body historyDTO: HistoryDTO):ApiResponse<Any>

    @POST("/v1/shops/post/report")
    suspend fun uploadReport(@Header("Authorization") token: String,@Body uploadComment: UploadComment):ApiResponse<Any>
    @GET("/v1/shops/get_count_cart")
    suspend fun getCountCart(@Header("Authorization") token: String):ApiResponse<Int>
    @POST("/auth/change_password")
    suspend fun changePass(@Header("Authorization") token: String,@Body changePassDTO: ChangePassDTO):ApiResponse<Any>
}