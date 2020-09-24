package com.job.jobnews.model


import com.google.gson.annotations.SerializedName

data class AddViewResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)