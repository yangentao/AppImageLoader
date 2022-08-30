package dev.entao.app.imageloader

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.content.res.ResourcesCompat
import dev.entao.app.task.Task
import java.io.FileInputStream


class ResImageRetriver(private val context: Context, private val resID: Int) : ImageRetriver {
    override fun retriveImage(option: ImageOption, onReady: (Drawable?) -> Unit) {
        val d = ResourcesCompat.getDrawable(context.resources, resID, context.theme)
        onReady(d)
    }
}


class AssetImageRetriver(private val context: Context, private val path: String) : ImageRetriver {
    override fun retriveImage(option: ImageOption, onReady: (Drawable?) -> Unit) {
        val bmp = decodeByStream(option.maxEdge, option.quility) {
            try {
                context.assets.open(path, AssetManager.ACCESS_BUFFER)
            } catch (ex: Exception) {
                null
            }
        }
        if (bmp != null) onReady(BitmapDrawable(context.resources, bmp)) else onReady(null)
    }
}

class UriImageRetriver(private val context: Context, private val uri: Uri) : ImageRetriver {
    override fun retriveImage(option: ImageOption, onReady: (Drawable?) -> Unit) {
        val scheme = uri.scheme ?: return onReady(null)
        if ("http" in scheme) {
            URLImageRetriver(context, uri.toString()).retriveImage(option, onReady)
            return
        }
        val bmp = decodeByStream(option.maxEdge, option.quility) {
            try {
                context.contentResolver.openInputStream(uri)
            } catch (ex: Exception) {
                null
            }
        }
        if (bmp != null) onReady(BitmapDrawable(context.resources, bmp)) else onReady(null)
    }
}


class URLImageRetriver(private val context: Context, private val url: String) : ImageRetriver {

    override fun retriveImage(option: ImageOption, onReady: (Drawable?) -> Unit) {
        val h = HttpImage(context, url)
        if (option.forceDownload) {
            h.download { _, file ->
                if (file != null) {
                    val bmp = decodeByStream(option.maxEdge, option.quility) {
                        try {
                            FileInputStream(file)
                        } catch (ex: Exception) {
                            null
                        }
                    }
                    foreCallback(bmp, onReady)
                } else {
                    foreCallback(null, onReady)
                }
            }
        } else {
            h.retrive { _, file ->
                if (file != null) {
                    val bmp = decodeByStream(option.maxEdge, option.quility) {
                        try {
                            FileInputStream(file)
                        } catch (ex: Exception) {
                            null
                        }
                    }
                    foreCallback(bmp, onReady)
                } else {
                    foreCallback(null, onReady)
                }
            }
        }
    }


    private fun foreCallback(bmp: Bitmap?, onReady: (Drawable?) -> Unit) {
        if (Task.isMainThread) {
            if (bmp != null) onReady(BitmapDrawable(context.resources, bmp)) else onReady(null)
        } else {
            Task.fore {
                if (bmp != null) onReady(BitmapDrawable(context.resources, bmp)) else onReady(null)
            }
        }
    }
}