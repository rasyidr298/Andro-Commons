package id.rrdev.commons.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import id.rrdev.commons.utils.state.DownloadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

object DownloadUtils {
    private const val BUFFER_LENGTH_BYTES = 1024 * 8

    fun download(
        context: Context,
        responseBody: ResponseBody,
        length: Long,
        type: String,
        destination: File,
        fileName: String
    ): Flow<DownloadState<Int>> {
        return flow {
            responseBody.byteStream().apply {
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
                                emit(DownloadState.Progress(((bytesCopied * 100) / length).toInt()))
                            } catch (e: IOException) {
                                bytes = -1
                                emit(DownloadState.Error(e.message.toString()))
                                Log.e("bytes", e.message.toString())
                            }
                        }
                    }
                }else { //if manual install
                    val outputStream: OutputStream? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val resolver: ContentResolver = context.contentResolver
                        val contentValues = ContentValues()
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/zip")
                        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                        val fileUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                        resolver.openOutputStream(fileUri!!)
                    } else {
                        val fileDir = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS)
                            .absolutePath
                        val image = File(fileDir, "$fileName.zip")
                        FileOutputStream(image)
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
                                emit(DownloadState.Progress(((bytesCopied * 100) / length).toInt()))
                            } catch (e: IOException) {
                                bytes = -1
                                emit(DownloadState.Error(e.message.toString()))
                                Log.e("bytes", e.message.toString())
                            }
                        }
                    }
                }
                emit(DownloadState.Success)
            }
        }.flowOn(Dispatchers.IO)
    }
}