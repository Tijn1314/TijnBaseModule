package com.tijn.netlibrary.download.core

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url


/**
 * Author:  Tomato.wl
 * CreateDate:    2020/3/25 17:54
 */
interface DownloadService {

    @Streaming
    @GET
    suspend fun download(@Header("RANGE") start: String? = "0", @Url url: String?): Response<ResponseBody>
}