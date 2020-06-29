package com.job.news.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.job.news.model.InfoResponse

@Dao
interface InfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(infoResponse: InfoResponse?)

    @Query("select * from inforesponse limit 1")
    fun getInfo(): InfoResponse
}
