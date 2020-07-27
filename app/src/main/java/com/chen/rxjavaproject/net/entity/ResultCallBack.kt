package com.chen.rxjavaproject.net.entity

/**
 *    @Author : chenjianbo
 *    @Date   : 2020/7/21
 *    @Desc   :
 */
interface ResultCallBack<T> {
    fun onSuccess(wrapper: T)
    fun onError(e: Exception)
}