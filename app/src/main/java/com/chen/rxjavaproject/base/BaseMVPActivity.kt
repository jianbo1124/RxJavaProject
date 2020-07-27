package com.chen.rxjavaproject.base

import android.content.Context
import android.content.Intent
import android.os.Bundle

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

import com.chen.rxjavaproject.R

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_base.*

/**
 *    @Author : chenjianbo
 *    @Date   : 2020/7/16
 *    @Desc   :
 */
abstract class BaseMVPActivity<in V : IBaseView, P : IPresenter<V>> : AppCompatActivity(),
    IBaseView {
    protected open val TAG: String = javaClass.simpleName

    protected open var mContext: Context? = null

    protected open var mPresenter: P? = null
    private var compositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        mContext = this
        try {
            val vgContent: View = layoutInflater.inflate(getLayoutID(), null)
            mLlRoot.addView(
                vgContent,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            initPresenter()
            initView()
            initData()
            initClick()
            loadData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initPresenter() {
        if (mPresenter == null) {
            mPresenter = createP()
        }
        mPresenter?.attachView(this as V)
    }

    /**
     * 初始化mPresenter
     */
    protected abstract fun createP(): P

    protected fun getP(): P? {
        return mPresenter
    }

    protected open fun loadData() {}

    protected open fun initClick() {

    }

    abstract fun initData()

    abstract fun initView()

    abstract fun getLayoutID(): Int

    /**
     * @param cls
     */
    protected open fun startActivity(cls: Class<*>?) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }

    protected open fun startActivity(cls: Class<*>?, bundle: Bundle) {
        val intent = Intent(this, cls).apply { putExtras(bundle) }
        startActivity(intent)
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

    override fun onDestroy() {
        super.onDestroy()
        clearDisposable()
        if (mPresenter != null) {
            mPresenter!!.detachView()
            mPresenter!!.onDestroy()
            mPresenter = null
        }

    }

    protected fun log(value: String) {
        Log.e(TAG, value)
    }
}


