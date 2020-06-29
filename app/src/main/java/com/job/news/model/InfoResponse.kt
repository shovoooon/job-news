package com.job.news.model


import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
data class InfoResponse(
    @SerializedName("about_body")
    val aboutBody: String,
    @SerializedName("about_head")
    val aboutHead: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("package")
    val packageName: String,
    @SerializedName("privacy_url")
    val privacyUrl: String,
    @SerializedName("version")
    val version: String
)