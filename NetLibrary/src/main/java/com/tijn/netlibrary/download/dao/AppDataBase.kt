package com.tijn.netlibrary.download.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tijn.netlibrary.download.core.DownloadInfo

/**
 * Author:  Tomato.wl
 * CreateDate:    2020/3/16 17:20
 */
@Database(entities = [DownloadInfo::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun downloadDao(): DownloadDao
}