package dev.entao.app.imageloader

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes


fun ImageView.loadURL(url: String, block: ImageOption.() -> Unit) {
    ImageLoader(this, ImageOption().apply(block), URLImageRetriver(this.context, url))
}

fun ImageView.loadRes(@DrawableRes resID: Int, block: ImageOption.() -> Unit) {
    ImageLoader(this, ImageOption().apply(block), ResImageRetriver(this.context, resID))
}

fun ImageView.loadUri(uri: Uri, block: ImageOption.() -> Unit) {
    ImageLoader(this, ImageOption().apply(block), UriImageRetriver(this.context, uri))
}

fun ImageView.loadAsset(assetpath: String, block: ImageOption.() -> Unit) {
    ImageLoader(this, ImageOption().apply(block), AssetImageRetriver(this.context, assetpath))
}

fun TextView.loadURL(url: String, block: ImageOption.() -> Unit) {
    ImageLoader(this, ImageOption().apply(block), URLImageRetriver(this.context, url))
}

fun TextView.loadRes(resID: Int, block: ImageOption.() -> Unit) {
    ImageLoader(this, ImageOption().apply(block), ResImageRetriver(this.context, resID))
}

fun TextView.loadUri(uri: Uri, block: ImageOption.() -> Unit) {
    ImageLoader(this, ImageOption().apply(block), UriImageRetriver(this.context, uri))
}

fun TextView.loadAsset(assetpath: String, block: ImageOption.() -> Unit) {
    ImageLoader(this, ImageOption().apply(block), AssetImageRetriver(this.context, assetpath))
}