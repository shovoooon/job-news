package com.job.news.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
data class CategoryResponse(
    @SerializedName("cat_img")
    val catImg: String,
    @SerializedName("cat_name")
    val catName: String,
    @SerializedName("id")
    val id: String,
    @ColumnInfo(defaultValue = "0")
    @SerializedName("job_count")
    val jobCount: String,
    @SerializedName("reg_date")
    val regDate: String
)