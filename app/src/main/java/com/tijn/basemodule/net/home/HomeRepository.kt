package com.tijn.basemodule.net.home

import com.tijn.netlibrary.model.NetResult
import com.tijn.netlibrary.net.BaseRepository
import com.tijn.netlibrary.net.RetrofitClient


/**
 * Create by liwen on 2020-05-18
 */
class HomeRepository : BaseRepository() {

    suspend fun getBanner(): NetResult<List<Banner>> {
        return callRequest(call = {
            handleResponse(
                RetrofitClient.instance.create(RequestCenter::class.java).getBanner()
            )
        })
    }


}