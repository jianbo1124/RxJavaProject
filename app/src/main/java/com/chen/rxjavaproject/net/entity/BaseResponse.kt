package com.chen.rxjavaproject.net.entity

import java.io.Serializable

/**
 *    @Author : chenjianbo
 *    @Date   : 2020/7/27
 *    @Desc   :
 */
class BaseResponse<T> : Serializable {

    var code = 0
    var message: String? = null
    var success = false

    /**
     * 数据
     */
    var result: T? = null

    override fun toString(): String {
        return "BaseResponse(code=$code, message=$message, success=$success, result=$result)"
    }


}