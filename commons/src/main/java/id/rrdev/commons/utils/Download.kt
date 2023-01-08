package id.rrdev.commons.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import id.rrdev.commons.R
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

class Download(okHttpClient: OkHttpClient) {

    companion object {
        private const val BUFFER_LENGTH_BYTES = 1024 * 8
        private const val HTTP_TIMEOUT = 30
    }

    private var okHttpClient: OkHttpClient

    init {
        val okHttpBuilder = okHttpClient.newBuilder()
            .connectTimeout(HTTP_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(HTTP_TIMEOUT.toLong(), TimeUnit.SECONDS)
        this.okHttpClient = okHttpBuilder
            .build()
    }

    fun download(context: Context, type: String, url: String, destination: File, fileName: String): Observable<Int> {
        return Observable.create { emitter ->
            try {
                val request = Request.Builder().url(url).build()
                val response = okHttpClient.newCall(request).execute()
                val body = response.body
                val responseCode = response.code

                if (responseCode >= HttpURLConnection.HTTP_OK &&
                    responseCode < HttpURLConnection.HTTP_MULT_CHOICE &&
                    body != null
                ) {

                    val length = body.contentLength()

                    body.byteStream().apply {
                        if (type == "auto") { //if auto install
                            destination.outputStream().use { fileOut ->
                                var bytesCopied = 0
                                val buffer = ByteArray(BUFFER_LENGTH_BYTES)
                                var bytes = read(buffer)
                                while (bytes >= 0) {
                                    try {
                                        fileOut.write(buffer, 0, bytes)
                                        bytesCopied += bytes
                                        bytes = read(buffer)
                                        emitter.onNext(((bytesCopied * 100) / length).toInt())
                                    } catch (e: IOException) {
                                        bytes = -1
                                        emitter.tryOnError(e)
                                        Log.e("bytes", e.message.toString())
                                    }
                                }
                            }
                        }else { //if manual install
                            val outputStream: OutputStream?

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                val resolver: ContentResolver = context.contentResolver
                                val contentValues = ContentValues()
                                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/zip")
                                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                                val fileUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                                outputStream = resolver.openOutputStream(fileUri!!)
                            } else {
                                val fileDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                    .absolutePath
                                val image = File(fileDir, "$fileName.zip")
                                outputStream = FileOutputStream(image)
                            }

                            outputStream.use { fileOut ->
                                var bytesCopied = 0
                                val buffer = ByteArray(BUFFER_LENGTH_BYTES)
                                var bytes = read(buffer)
                                while (bytes >= 0) {
                                    try {
                                        fileOut?.write(buffer, 0, bytes)
                                        bytesCopied += bytes
                                        bytes = read(buffer)
                                        emitter.onNext(((bytesCopied * 100) / length).toInt())
                                    } catch (e: IOException) {
                                        bytes = -1
                                        emitter.tryOnError(e)
                                        Log.e("bytes", e.message.toString())
                                    }
                                }
                            }
                        }
                        emitter.onComplete()
                    }
                } else {
                    emitter.onError(IllegalArgumentException(context.resources.getString(R.string.server_error)))
                }

            } catch (e: IOException) {
                emitter.tryOnError(IllegalArgumentException(context.resources.getString(R.string.mohon_cek_koneksi)))
                Log.e("emitter", e.message.toString())
            }
        }
    }
}