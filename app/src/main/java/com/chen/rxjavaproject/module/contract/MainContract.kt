package com.chen.rxjavaproject.module.contract

import android.graphics.Bitmap
import com.chen.rxjavaproject.base.IBaseModel
import com.chen.rxjavaproject.base.IBaseView
import com.chen.rxjavaproject.net.entity.BaseResponse
import com.chen.rxjavaproject.net.entity.mainpage.Result
import io.reactivex.rxjava3.core.Observable

/**
 *    @Author : chenjianbo
 *    @Date   : 2020/7/27
 *    @Desc   :
 */
interface MainContract {
    interface IMainModel : IBaseModel {
        fun loadQRCode(): Observable<Bitmap>
        fun loop(): Observable<BaseResponse<List<Result>>>
    }

    interface IMainView : IBaseView {
        fun loadQRCodeSuccess(bitmap: Bitmap)
        fun loadQRCodeFail(exception: Exception)

    }

    interface IMainPresenter {
        fun loadQRCode()
        fun loop()
    }
}