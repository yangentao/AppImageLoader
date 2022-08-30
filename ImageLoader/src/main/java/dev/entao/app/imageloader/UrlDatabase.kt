package dev.entao.app.imageloader

import android.content.Context
import java.io.File

class UrlDatabase(context: Context) {
    private val db = context.applicationContext.openOrCreateDatabase("imageloader.db", 0, null)

    init {
        db.execSQL("CREATE TABLE IF NOT EXISTS urlfile (url TEXT PRIMARY KEY, file TEXT)")
    }

    fun put(url: String, file: File) {
        db.execSQL("REPLACE INTO urlfile(url, file) VALUES(?,?)", arrayOf(url, file.absolutePath))
    }

    fun find(url: String): File? {
        val cur = db.rawQuery("SELECT file FROM urlfile WHERE url=?", arrayOf(url)) ?: return null
        cur.use { c ->
            if (c.moveToNext()) {
                val f = c.getString(0) ?: return null
                val file = File(f)
                if (file.exists()) return file
                file.delete()
                remove(url, false)
            }
        }
        return null
    }

    fun remove(url: String, withFile: Boolean) {
        val old = find(url)
        db.execSQL("DELETE FROM urlfile WHERE url=?", arrayOf(url))
        if (withFile) old?.delete()
    }

}