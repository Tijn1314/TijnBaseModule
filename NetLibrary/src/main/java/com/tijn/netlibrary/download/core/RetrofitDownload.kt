package com.tijn.netlibrary.download.core

import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Author:  Tomato.wl
 * CreateDate:    2020/3/25 17:58
 */
object RetrofitDownload {

    val downloadService: DownloadService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        val okHttpClient = createOkHttpClient()
        val retrofit = createRetrofit(okHttpClient)
        retrofit.create(DownloadService::class.java)
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    private fun createRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://download")
            .client(client)
            .build()
    }
}