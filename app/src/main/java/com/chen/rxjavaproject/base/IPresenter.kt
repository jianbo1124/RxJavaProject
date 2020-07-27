package com.chen.rxjavaproject.base

/**
 *    @Author : chenjianbo
 *    @Date   : 2020/7/22
 *    @Desc   :
 */
interface IPresenter<in V : IBaseView> {

    /**
     * 绑定 View
     */
    fun attachView(mView: V)

    /**
     * 解绑 View
     */
    fun detachView()

    fun onDestroy()
}