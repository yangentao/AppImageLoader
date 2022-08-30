@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package dev.entao.app.imageloader

import android.graphics.Bitmap

class ImageOption {
    var maxEdge: Int = 720
    var quility: Bitmap.Config? = null

    var boundWidth: Int = 0
    var boundHeight: Int = 0

    var forceDownload: Boolean = false

    //加载失败的图片
    var failedImage: Int = 0

    //默认的图片, 下载前
    var defaultImage: Int = 0

    val keyString: String
        get() = "$maxEdge:${quility?.name ?: "default"}"

    fun hightQuility() {
        this.quility = Bitmap.Config.ARGB_8888
    }

    fun lowQuility() {
        this.quility = Bitmap.Config.RGB_565
    }
}