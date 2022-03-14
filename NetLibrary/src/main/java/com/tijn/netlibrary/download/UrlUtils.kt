package com.tijn.netlibrary.download

/**
 * Author:  Tomato.wl
 * CreateDate:    2020/3/26 15:25
 */
object UrlUtils {

    fun getUrlFileName(url: String?): String {
        url ?: return "unknownfile_${System.currentTimeMillis()}"
        var filename: String? = null
        val strings = url.split("/").toTypedArray()
        for (string in strings) {
            if (string.contains("?")) {
                val endIndex = string.indexOf("?")
                if (endIndex != -1) {
                    filename = string.substring(0, endIndex)
                    return filename
                }
            }
        }
        if (strings.isNotEmpty()) {
            filename = strings[strings.size - 1]
        }
        filename ?: return "unknownfile_${System.currentTimeMillis()}"
        return filename
    }
}