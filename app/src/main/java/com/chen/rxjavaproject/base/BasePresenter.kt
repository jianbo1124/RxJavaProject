package com.chen.rxjavaproject.base

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import java.lang.ref.WeakReference

/**
 *    @Author : chenjianbo
 *    @Date   : 2020/7/22
 *    @Desc   :
 */
abstract class BasePresenter<M : IBaseModel, V : IBaseView> : IPresenter<V> {

    /**
     * 通过该方法创建Module
     */
    protected abstract fun createModule(): M
    private var mvpView: WeakReference<V?>? = null
    private var mvpModel: M? = null
    private var compositeDisposable: CompositeDisposable? = null

    /**
     * 绑定View
     */

    override fun attachView(view: V) {
        if (mvpView == null) {
            mvpView = WeakReference(view)
        }
        if (mvpModel == null) {
            mvpModel = createModule()
        }
    }

    /**
     * 解绑View
     */
    override fun detachView() {
        if (null != mvpView) {
            mvpView!!.clear()
            mvpView = null
        }
        mvpModel = null
    }

    protected fun addDisposable(disposable: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable!!.add(disposable)
    }

    private fun clearDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable!!.clear()
        }
    }

    /**
     * 是否与View建立连接
     */
    protected open fun isViewAttached(): Boolean {
        return null != mvpView && null != mvpView!!.get()
    }

    protected open fun getView(): V? {
        return if (isViewAttached()) mvpView!!.get() else null
    }

    protected open fun getModule(): M? {
        return mvpModel
    }

    override fun onDestroy() {
        clearDisposable()
    }
}