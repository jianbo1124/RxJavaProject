package com.chen.rxjavaproject.net.api

import com.chen.rxjavaproject.net.entity.BaseResponse
import com.chen.rxjavaproject.net.entity.mainpage.Result
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.POST

/**
 *    @Author : chenjianbo
 *    @Date   : 2020/7/27
 *    @Desc   :
 */
interface MainPageApi {
    @GET("getJoke?page=1&count=1&type=text")
    fun getQRCodeValue(): Observable<BaseResponse<List<Result>>>
}