package com.phuong.myspa.data.repository.signUp

import com.phuong.myspa.base.BaseUseCase
import com.phuong.myspa.data.api.model.login.UserSignUp
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.repository.signUp.SignUpRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val signUpRepository: SignUpRepository):
    BaseUseCase<UserSignUp, ApiResponse<Any>>() {
    override suspend fun execute(param: UserSignUp): Flow<DataResponse< ApiResponse<Any>>> = flow{
        val data = signUpRepository.userRegister(param)
        if (data?.success == true) {
            emit(DataResponse.DataSuccess(data))
        } else {
            emit(DataResponse.DataError(data))
        }
    }
}