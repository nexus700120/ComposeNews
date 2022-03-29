package com.epa.composenews.common

import com.google.gson.annotations.SerializedName

class ApiResponse(
    @SerializedName("status") val status: Status,
    @SerializedName("message") val message: String?
)