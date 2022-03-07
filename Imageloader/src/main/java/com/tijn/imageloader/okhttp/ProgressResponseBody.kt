package com.tijn.imageloader.okhttp

import android.util.Log
import com.tijn.imageloader.listeners.ProgressListener
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

class ProgressResponseBody(private val url: String?, val responseBody: ResponseBody?) : ResponseBody() {
    private var bufferedSource: BufferedSource? = null
    private var listener: ProgressListener? = ProgressInterceptor.LISTENER_MAP[url]
    override fun contentType(): MediaType? {
        return responseBody?.contentType()
    }

    override fun contentLength(): Long {
        return if (responseBody?.contentLength() != null) responseBody.contentLength() else 0
    }

    override fun source(): BufferedSource? {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(
                ProgressSource(
                    responseBody?.source()
                )
            )
        }
        return bufferedSource
    }

    private inner class ProgressSource internal constructor(source: Source?) :
        ForwardingSource(source) {
        var totalBytesRead: Long = 0
        var currentProgress = 0

        @Throws(IOException::class)
        override fun read(sink: Buffer, byteCount: Long): Long {
            val bytesRead = super.read(sink, byteCount)
            var fullLength: Long? = responseBody?.contentLength()
            responseBody?.contentLength()?.let {
                if (it > 0) {
                    if (bytesRead == -1L) {
                        totalBytesRead = it
                    } else {
                        totalBytesRead += bytesRead
                    }
                    val progress = (100f * totalBytesRead / it).toInt()
                    Log.d(TAG, "download progress is $progress")
                    if (listener != null && progress != currentProgress) {
                        listener?.onProgress(progress)
                    }
                    if (listener != null && totalBytesRead == fullLength) {
                        listener = null
                    }
                    currentProgress = progress
                }

            }
            return bytesRead
        }
    }

    companion object {
        private const val TAG = "XGlide"
    }


}