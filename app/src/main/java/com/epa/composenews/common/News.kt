package com.epa.composenews.common

import com.google.gson.annotations.SerializedName
import java.util.*

class News(
    @SerializedName("title") val title: String,
    @SerializedName("description") val htmlDescription: String,
    @SerializedName("publishedAt") val date: Date,
    @SerializedName("urlToImage") val urlToImage: String
)