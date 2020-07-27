package com.chen.rxjavaproject

import android.graphics.Bitmap
import android.util.Log
import com.chen.rxjavaproject.base.BaseMVPActivity
import com.chen.rxjavaproject.module.contract.MainContract
import com.chen.rxjavaproject.module.presenter.MainPresenter
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.view.longClicks
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : BaseMVPActivity<MainContract.IMainView, MainPresenter>(),
    MainContract.IMainView {


    override fun createP(): MainPresenter {
        return MainPresenter()
    }

    override fun initData() {
        // getP()?.loadQRCode()

    }

    override fun initView() {
        addDisposable(ivQRCode.clicks().throttleFirst(3, TimeUnit.SECONDS).subscribe { })
        addDisposable(ivQRCode.longClicks().subscribe {
            getP()?.startCountDown()
        })


    }


    override fun getLayoutID(): Int {
        return R.layout.activity_main
    }

    override fun loadQRCodeSuccess(bitmap: Bitmap) {
        ivQRCode.setImageBitmap(bitmap)
    }

    override fun loadQRCodeFail(exception: Exception) {
        TODO("Not yet implemented")
    }

    override fun updateText(value: Long) {
        tvText.text = "剩余 $value 秒"
    }

    override fun countDownFinish() {
        finish()
    }
}