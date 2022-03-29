package com.epa.composenews

import com.epa.composenews.common.NewsResponse
import com.google.gson.GsonBuilder
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class NewsRepository {

    private val okhttp = OkHttpClient()
    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        .create()
    private val fromFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT)

    suspend fun news(to: Date? = null): NewsResponse {
        val url = "https://newsapi.org/v2/everything".toHttpUrl()
            .newBuilder()
            .addQueryParameter("domains", "sports.ru")
            .apply {
                if (to != null) {
                    val newDate = Calendar.getInstance().let {
                        it.time = to
                        it.add(Calendar.SECOND, -1)
                        it.time
                    }
                    addEncodedQueryParameter("to", fromFormat.format(newDate))
                }
            }
            .addQueryParameter("apiKey", API_KEY)
            .build()
        val request = Request.Builder()
            .get()
            .url(url)
            .build()
        return okhttp.newCall(request).await().mapTo()
    }

    private inline fun <reified T> Response.mapTo(): T {
        val content = body?.string()
        checkNotNull(content)
        return gson.fromJson(content, T::class.java)
    }

    private suspend fun Call.await(): Response = suspendCancellableCoroutine { continuation ->
        continuation.invokeOnCancellation { cancel() }
        enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                continuation.resume(response)
            }
        })
    }

    companion object {
        private const val API_KEY = "7402e2bccebf4d4cac14845c6969701e"
    }
}