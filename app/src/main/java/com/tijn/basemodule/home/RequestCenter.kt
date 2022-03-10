package com.tijn.basemodule.home

import com.tijn.netlibrary.model.BaseModel
import retrofit2.http.GET

/**
 * Create by liwen on 2020-05-18
 */
interface RequestCenter {

    @GET("/banner/json")
    suspend fun getBanner(): BaseModel<MutableList<Banner>>



}