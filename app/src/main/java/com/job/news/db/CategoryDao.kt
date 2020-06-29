package com.job.news.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.job.news.model.CategoryResponse


@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(categoryResponse: List<CategoryResponse>?)

    @Query("select * from categoryresponse where id = :id")
    fun getCategoryById(id:String) : CategoryResponse

    @Query("select * from categoryresponse")
    fun getCategories() : List<CategoryResponse>
}
