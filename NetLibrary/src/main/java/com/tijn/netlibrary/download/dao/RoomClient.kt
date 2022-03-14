package com.tijn.netlibrary.download.dao

import androidx.room.Room
import androidx.room.migration.Migration
import com.tijn.netlibrary.NetConfig

/**
 * Author:  Tomato.wl
 * CreateDate:    2020/3/16 17:29
 */
object RoomClient {
    init {
        if (NetConfig.application == null) {
            throw  Exception("Net not init")
        }
    }

    private const val DATA_BASE_NAME = "download.db"

    val dataBase: AppDataBase by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        Room
            .databaseBuilder(
                NetConfig.application?.applicationContext!!,
                AppDataBase::class.java,
                DATA_BASE_NAME
            )
            .build()
    }

    private fun createMigrations(): Array<Migration> {
        return arrayOf()
    }

}