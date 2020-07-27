package com.chen.rxjavaproject.module.presenter

import android.util.Log
import com.chen.rxjavaproject.base.BasePresenter
import com.chen.rxjavaproject.module.contract.MainContract
import com.chen.rxjavaproject.module.model.MainModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 *    @Author : chenjianbo
 *    @Date   : 2020/7/27
 *    @Desc   :
 */
class MainPresenter : BasePresenter<MainModel, MainContract.IMainView>(),
    MainContract.IMainPresenter {
    override fun createModule(): MainModel {
        return MainModel()
    }

    override fun loadQRCode() {
        getModule()?.loadQRCode()
            ?.subscribeOn(Schedulers.io())
            ?.doOnSubscribe { addDisposable(it) }
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                it?.let { getView()?.loadQRCodeSuccess(it) }
            }) {
                getView()?.loadQRCodeFail(Exception(it.message))
            }
    }

    override fun loop() {
        getModule()?.loop()
            ?.delay(5, TimeUnit.SECONDS, true)//定时5秒
            ?.repeat()//重复请求
            ?.retry()//异常重试
            ?.doOnSubscribe { addDisposable(it) }
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                Log.e("loadLoginState", Date().toString())
            }) {
                Log.e("loadLoginState", it.toString())
            }
    }
}