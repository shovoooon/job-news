package com.job.jobnews.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.job.jobnews.model.InfoResponse

@Dao
interface InfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(infoResponse: InfoResponse?)

    @Query("select * from inforesponse limit 1")
    fun getInfo(): InfoResponse

}
