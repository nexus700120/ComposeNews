package com.epa.composenews.common

import com.google.gson.annotations.SerializedName

class NewsResponse(
    @SerializedName("totalResults") val total: Int,
    @SerializedName("articles") val news: List<News>,
    @SerializedName("status") val status: Status,
    @SerializedName("message") val message: String?
)