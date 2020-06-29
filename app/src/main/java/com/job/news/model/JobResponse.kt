package com.job.news.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
data class JobResponse(
    @SerializedName("apply_url")
    val applyUrl: String,
    @SerializedName("cat_id")
    val catId: String,
    @SerializedName("cat_name")
    val catName: String,
    @SerializedName("cover_img")
    val coverImg: String,
    @SerializedName("desc")
    val desc: String,
    @ColumnInfo(defaultValue = "")
    @SerializedName("extra_url")
    val extraUrl: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("is_published")
    val isPublished: String,
    @SerializedName("is_suggested")
    val isSuggested: String,
    @SerializedName("job_img")
    val jobImg: String,
    @SerializedName("last_date")
    val lastDate: String,
    @SerializedName("pdf")
    val pdf: String,
    @SerializedName("position")
    val position: String,
    @SerializedName("reg_date")
    val regDate: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("views")
    val views: String
)