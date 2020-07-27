package com.chen.rxjavaproject.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.text.TextUtils
import android.util.Log
import com.chen.rxjavaproject.MyApplication
import com.chen.rxjavaproject.R
import com.chen.rxjavaproject.net.entity.ResultCallBack
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

import java.util.*

/**
 *    @Author : chenjianbo
 *    @Date   : 2020/7/21
 *    @Desc   :
 */
class QRCodeUtil {
    companion object {


        fun createQRCode(callBack: ResultCallBack<Bitmap>, content: String?) {
            Log.e("createQRCode", content)

            if (TextUtils.isEmpty(content)) {
                callBack.onError(java.lang.Exception("数据异常"))
                return
            }
            try {
                var res = MyApplication.instance?.applicationContext?.resources
                val w = 400
                val h = 400
                val logo = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher)

                /*偏移量*/
                var offsetX = w / 2
                var offsetY = h / 2

                /*生成logo*/
                var logoBitmap: Bitmap? = null
                if (logo != null) {
                    val matrix = Matrix()
                    val scaleFactor =
                        (w * 1.0f / 5 / logo.width).coerceAtMost(h * 1.0f / 5 / logo.height)
                    matrix.postScale(scaleFactor, scaleFactor)
                    logoBitmap =
                        Bitmap.createBitmap(logo, 0, 0, logo.width, logo.height, matrix, true)
                }


                /*如果log不为null,重新计算偏移量*/
                var logoW = 0
                var logoH = 0
                if (logoBitmap != null) {
                    logoW = logoBitmap.width
                    logoH = logoBitmap.height
                    offsetX = (w - logoW) / 2
                    offsetY = (h - logoH) / 2
                }

                /*指定为UTF-8*/
                val hints = Hashtable<EncodeHintType, Any?>()
                hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
                //容错级别
                hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
                //设置空白边距的宽度
                hints[EncodeHintType.MARGIN] = 0
                // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
                var matrix: BitMatrix? = null
                matrix = MultiFormatWriter().encode(
                    content,
                    BarcodeFormat.QR_CODE, w, h, hints
                )

                // 二维矩阵转为一维像素数组,也就是一直横着排了
                val pixels = IntArray(w * h)
                for (y in 0 until h) {
                    for (x in 0 until w) {
                        if (x >= offsetX && x < offsetX + logoW && y >= offsetY && y < offsetY + logoH) {
                            var pixel = logoBitmap!!.getPixel(x - offsetX, y - offsetY)
                            if (pixel == 0) {
                                pixel = if (matrix[x, y]) {
                                    -0x1000000
                                } else {
                                    -0x1
                                }
                            }
                            pixels[y * w + x] = pixel
                        } else {
                            if (matrix[x, y]) {
                                pixels[y * w + x] = -0x1000000
                            } else {
                                pixels[y * w + x] = -0x1
                            }
                        }
                    }
                }
                val bitmap = Bitmap.createBitmap(
                    w, h,
                    Bitmap.Config.ARGB_8888
                )
                bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
                callBack.onSuccess(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
                callBack.onError(e)
            }
        }
    }
}