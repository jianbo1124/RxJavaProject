package com.chen.rxjavaproject

import android.graphics.Bitmap
import com.chen.rxjavaproject.base.BaseMVPActivity
import com.chen.rxjavaproject.module.contract.MainContract
import com.chen.rxjavaproject.module.presenter.MainPresenter
import com.jakewharton.rxbinding4.view.longClicks
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseMVPActivity<MainContract.IMainView, MainPresenter>(),
    MainContract.IMainView {


    override fun createP(): MainPresenter {
        return MainPresenter()
    }

    override fun initData() {
        getP()?.loadQRCode()
    }

    override fun initView() {
        addDisposable(ivQRCode.longClicks().subscribe {
            getP()?.loadQRCode()
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
}