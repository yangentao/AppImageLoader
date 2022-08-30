package dev.entao.app.imageloader

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import dev.entao.app.task.Task
import java.lang.ref.WeakReference


internal var View.imageLoaderInstance: ImageLoader?
    get() = this.getTag(R.id.imageLoader) as? ImageLoader
    set(value) = this.setTag(R.id.imageLoader, value)

//只下载
interface ImageRetriver {
    //UI thread
    fun retriveImage(option: ImageOption, onReady: (Drawable?) -> Unit)
}


class ImageLoader(view: View, private val option: ImageOption,  retriver: ImageRetriver) {
    private val weakView: WeakReference<View> = WeakReference(view)
    private val context: Context = view.context

    init {
        view.imageLoaderInstance?.clear()
        view.imageLoaderInstance = this
        setImageByID(view, option.defaultImage)
        retriver.retriveImage(option, ::onRetriveImage)
    }

    private val validView: View?
        get() {
            if (weakView.get()?.imageLoaderInstance === this) {
                return weakView.get()
            }
            weakView.clear()
            return null
        }

    private fun onRetriveImage(image: Drawable?) {
        val view = validView ?: return
        if (image == null) {
            setImageByID(view, option.failedImage)
        } else {
            setImage(view, image)
        }
        clear()
    }

    private fun setImageByID(view: View, imgID: Int) {
        if (imgID == 0) return
        setImage(view, imgID.resDrawable(view.context))
    }

    private fun setImage(view: View, img: Drawable) {
        if (option.boundHeight > 0 && option.boundWidth > 0) {
            img.setBounds(0, 0, option.boundWidth, option.boundHeight)
        }
        Task.fore {
            when (view) {
                is ImageView -> view.setImageDrawable(img)
                is TextView -> view.setCompoundDrawables(null, img, null, null)
            }
        }
    }

    private fun clear() {
        if (weakView.get()?.imageLoaderInstance === this) {
            weakView.get()?.imageLoaderInstance = null
        }
        weakView.clear()
    }


}