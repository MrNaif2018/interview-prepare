package com.mrnaif.interviewhelper

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

fun sendJsonPostRequest(
    url: String,
    jsonBody: JSONObject,
    headers: Map<String, String> = emptyMap(),
    callback: (response: JSONObject?, error: String?) -> Unit
) {
    val client = OkHttpClient()
    val mediaType = "application/json; charset=utf-8".toMediaType()

    // Create request body with JSON content
    val requestBody = jsonBody.toString().toRequestBody(mediaType)


    // Build request
    val requestBuilder = Request.Builder()
        .url(url)
        .post(requestBody)

    // Add headers if provided
    for ((key, value) in headers) {
        requestBuilder.addHeader(key, value)
    }

    val request = requestBuilder.build()

    // Send the request asynchronously
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            // Handle network failure
            callback(null, e.message)
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                parseBody(response, callback, false)
            } else {
                parseBody(response, callback, true)
            }
        }
    })
}


fun parseBody(
    response: Response,
    callback: (response: JSONObject?, error: String?) -> Unit,
    error: Boolean
) {
    response.body?.string()?.let { it ->
        tryParseJSON(it)?.let {
            // Parse the response to JSON
            // Return the JSON response in the callback
            if (error)
                callback(null, it.toString())
            else
                callback(it, null)
        } ?: run {
            // Handle JSON parsing error
            callback(null, "Error parsing JSON")
        }
    } ?: run {
        callback(null, "Empty response body")
    }
}
