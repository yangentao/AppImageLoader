package dev.entao.app.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import java.io.InputStream
import java.util.*

internal val UUID.hexText: String get() = String.format("%x%016x", this.mostSignificantBits, this.leastSignificantBits)


internal fun Int.resDrawable(context: Context): Drawable = ResourcesCompat.getDrawable(context.resources, this, context.theme) ?: error("NO image resource: $this ")


internal fun decodeByStream(preferMaxEdge: Int, preferQulity: Bitmap.Config?, streamProvidor: () -> InputStream?): Bitmap? {
    val ins = streamProvidor() ?: return null
    val opt = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
        inPreferredConfig = preferQulity
    }
    ins.use { BitmapFactory.decodeStream(ins, null, opt) }
    val maxEdge = Integer.max(opt.outHeight, opt.outWidth)
    val sample = if (preferMaxEdge > 0 && maxEdge >= preferMaxEdge * 2) {
        StrictMath.floor(maxEdge * 1.0 / preferMaxEdge).toInt()
    } else 1

    val opt2 = BitmapFactory.Options().apply {
        inPreferredConfig = preferQulity
        inSampleSize = Integer.max(1, sample)
    }
    val ins2 = streamProvidor() ?: return null
    return ins2.use { BitmapFactory.decodeStream(ins2, null, opt2) }
}
