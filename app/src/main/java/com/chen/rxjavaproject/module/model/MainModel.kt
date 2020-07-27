package com.chen.rxjavaproject.module.model

import android.graphics.Bitmap
import com.chen.rxjavaproject.module.contract.MainContract
import com.chen.rxjavaproject.net.RetrofitClient
import com.chen.rxjavaproject.net.api.MainPageApi
import com.chen.rxjavaproject.net.entity.BaseResponse
import com.chen.rxjavaproject.net.entity.ResultCallBack
import com.chen.rxjavaproject.net.entity.mainpage.Result
import com.chen.rxjavaproject.utils.QRCodeUtil
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import java.util.concurrent.TimeUnit

/**
 *    @Author : chenjianbo
 *    @Date   : 2020/7/27
 *    @Desc   :
 */
class MainModel : MainContract.IMainModel {
    override fun loadQRCode(): Observable<Bitmap> {
        var qrCodeValueObservable =
            RetrofitClient.getInstance().create(MainPageApi::class.java).getQRCodeValue()
        return qrCodeValueObservable.concatMap {
            return@concatMap Observable.create { emitter: ObservableEmitter<Bitmap> ->
                it?.result.let { result ->
                    QRCodeUtil.createQRCode(object : ResultCallBack<Bitmap> {
                        override fun onSuccess(wrapper: Bitmap) {
                            emitter.onNext(wrapper)
                        }
                        override fun onError(e: Exception) {
                            emitter.onError(e)
                        }
                    }, result?.get(0)?.name)
                }
            }
        }
    }

    override fun loop(): Observable<BaseResponse<List<Result>>> {
        return RetrofitClient.getInstance().create(MainPageApi::class.java).getQRCodeValue()
    }

    override fun startCountDown(): Observable<Long> {
        return    Observable.interval(0, 1, TimeUnit.SECONDS)
            .take(4)
    }
}