@file:Suppress("ObjectPropertyName")

package dev.entao.app.imageloader

import android.content.Context
import dev.entao.app.http.httpGet
import dev.entao.app.task.Task
import java.io.File
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

internal typealias HttpReadyCallback = (String, File?) -> Unit

class HttpImage(private val context: Context, private val url: String) {
    private var oldFile: File? = null

    init {
        checkDatabase(context)
    }

    private fun foreReady(file: File?, callback: HttpReadyCallback) {
        if (Task.isMainThread) {
            callback(url, file)
        } else {
            Task.fore {
                callback(url, file)
            }
        }
    }

    private fun httpDown(url: String): File? {
        val file: File = makeCacheFile(context)
        val r = httpGet(url) {
            saveToFile = file
        }
        if (r.OK && file.exists()) {
            putCache(url, file)
            return file
        }
        return null
    }

    private fun doDownload(): File? {
        val file: File?
        try {
            synchronized(startUrl(url)) {
                file = findCache(url) ?: httpDown(url)
                if (file != null && file.absolutePath != oldFile?.absolutePath) {
                    oldFile?.delete()
                    oldFile = null
                }
            }
        } finally {
            endUrl(url)
        }
        return file
    }

    fun download(onReady: HttpReadyCallback) {
        oldFile = findCache(url)
        removeCache(url, false)
        Task.back {
            try {
                val f = doDownload()
                foreReady(f, onReady)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun retrive(onReady: HttpReadyCallback) {
        val file = findCache(url)
        if (file != null) {
            foreReady(file, onReady)
            return
        }
        download(onReady)
    }

    companion object {
        private var _cacheDir: File? = null

        private val atomMap = Hashtable<String, AtomicInteger>()

        private var db: UrlDatabase? = null
        private fun checkDatabase(context: Context) {
            if (db == null) {
                db = UrlDatabase(context)
            }
        }

        @Synchronized
        fun findCache(url: String): File? {
            return db?.find(url)
        }

        @Synchronized
        fun putCache(url: String, file: File) {
            db?.put(url, file)
        }

        @Synchronized
        fun removeCache(url: String, withFile: Boolean) {
            db?.remove(url, withFile)
        }


        @Synchronized
        fun endUrl(url: String) {
            val a = atomMap[url] ?: return
            val n = a.decrementAndGet()
            if (n <= 0) {
                atomMap.remove(url)
            }
        }

        @Synchronized
        private fun startUrl(url: String): AtomicInteger {
            val an = atomMap.getOrPut(url) { AtomicInteger(0) }
            val n = an.incrementAndGet()
            return an
        }


        @Synchronized
        private fun cacheDir(context: Context): File {
            _cacheDir?.also { return it }
            val dir = File(context.externalCacheDir ?: context.cacheDir, "imgloader")
            if (!dir.exists()) {
                dir.mkdir()
                val f = File(dir, ".nomedia")
                if (!f.exists()) {
                    f.createNewFile()
                }
            }
            _cacheDir = dir
            return dir
        }

        @Synchronized
        private fun makeCacheFile(context: Context): File {
            return File(cacheDir(context), UUID.randomUUID().hexText)
        }
    }
}

