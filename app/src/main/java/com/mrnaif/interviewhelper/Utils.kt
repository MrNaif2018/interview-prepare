package com.mrnaif.interviewhelper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import org.json.JSONException
import org.json.JSONObject

fun sendToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun navigateTo(context: Context, activity: Class<out Activity>) {
    val intent = Intent(context, activity)
    context.startActivity(intent)
}

fun tryParseJSON(data: String): JSONObject? {
    return try {
        JSONObject(data)
    } catch (e: JSONException) {
        null
    }
}