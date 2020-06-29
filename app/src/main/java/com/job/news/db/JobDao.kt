package com.job.news.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.job.news.model.JobResponse


@Dao
interface JobDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(jobResponse: JobResponse?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(jobResponse: List<JobResponse>?)

    @Query("select * from jobresponse")
    fun getAllJob() : List<JobResponse>

    @Query("select * from jobresponse where id = :id")
    fun getJobById(id:String) : JobResponse

    @Query("select * from jobresponse where isSuggested = 1")
    fun getSuggestedJob() : List<JobResponse>

    @Query("select * from jobresponse where title like :query or position like :query or catName like :query")
    fun searchJob(query:String) : List<JobResponse>

    @Query("select * from jobresponse where catId = :catId")
    fun getJobsByCat(catId: String) : List<JobResponse>

}
