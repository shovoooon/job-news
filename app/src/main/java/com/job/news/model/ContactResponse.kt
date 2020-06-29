package com.job.news.model


import com.google.gson.annotations.SerializedName

data class ContactResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)