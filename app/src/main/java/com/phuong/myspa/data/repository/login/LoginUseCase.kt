package com.phuong.myspa.data.repository.login

import com.phuong.myspa.base.BaseUseCase
import com.phuong.myspa.data.api.model.login.UserDTO
import com.phuong.myspa.data.api.model.login.UserLogin
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.repository.login.LoginRepository
import com.phuong.myspa.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor( private val loginRepository: LoginRepository):
    BaseUseCase<UserDTO, UserLogin>() {
    override suspend fun execute(param: UserDTO): Flow<DataResponse<UserLogin>> = flow{
        val data = loginRepository.loginUser(param)
        if (data?.success == true) {
            data.data?.user?.avatar = Constants.BASE_URL +  data.data?.user?.avatar?.replace("\\", "/")
            emit(DataResponse.DataSuccess(data.data!!))
        } else {
            emit(DataResponse.DataError())
        }
    }
}