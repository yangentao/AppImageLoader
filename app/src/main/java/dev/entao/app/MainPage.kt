package dev.entao.app

import android.graphics.Color
import android.widget.ImageView
import android.widget.LinearLayout
import dev.entao.app.imageloader.loadAsset
import dev.entao.app.imageloader.loadRes
import dev.entao.app.imageloader.loadURL
import dev.entao.app.log.logd
import dev.entao.app.page.Page
import dev.entao.app.page.PageActivity
import dev.entao.app.ui.backColor
import dev.entao.app.viewbuilder.*
import dev.entao.app.viewname.NamedView
import dev.entao.app.viewname.name
import java.util.concurrent.atomic.AtomicInteger


class MainPage(activity: PageActivity) : Page(activity) {
    //    val lastImageView: ImageView by lazy { namedView("pick") }
    val pickView: ImageView by NamedView("img4")
    val dogURL = "http://app800.cn/dog1.jpg"
    val atomN = AtomicInteger(0)

    fun doForce() {
        val n = atomN.getAndIncrement()
        logd("n:", n)
//        pickView.loadURL(dogURL) {
//            maxEdge = 500
//            lowQuility()
//            forceDownload = true
//        }
    }

    fun dog() {
        for (i in 0..20) {
            pickView.loadURL(dogURL) {
                maxEdge = 500
                lowQuility()
                forceDownload = true
            }
            if (i % 5 == 0) {
                Thread.sleep(600)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        pageView.linearLayout {
            relativeParams {
                match()
            }
            orientation = LinearLayout.VERTICAL
            backColor = Color.CYAN
            onCreateContent(this)
        }
    }

    private fun onCreateContent(contentView: LinearLayout) {

        contentView.apply {
            imageView {
                linearParams {
                    matchX()
                    flexY(1f)
                    grav.center()
                }
                backColor = Color.LTGRAY
                loadRes( dev.entao.app.imageloader.demo.R.mipmap.cat1) {}
            }
            imageView {
                linearParams {
                    matchX()
                    flexY(1f)
                    grav.center()
                }
                backColor = Color.CYAN
                loadAsset("imgs/cat1.jpg") {}
            }
            imageView {
                linearParams {
                    matchX()
                    flexY(1f)
                    grav.center()
                }
                backColor = Color.BLUE
                loadURL("http://app800.cn/cat1.jpg") {
                    maxEdge = 500
                    lowQuility()
                }
            }
            imageView {
                linearParams {
                    matchX()
                    flexY(1f)
                    grav.center()
                }
                name = "img4"
                backColor = Color.BLACK
                loadURL("http://app800.cn/cat1.jpg") {}
            }
        }
    }
}