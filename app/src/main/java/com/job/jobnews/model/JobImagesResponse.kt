package com.job.jobnews.model


import com.google.gson.annotations.SerializedName

data class JobImagesResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("img")
    val img: String,
    @SerializedName("job_id")
    val jobId: String
)