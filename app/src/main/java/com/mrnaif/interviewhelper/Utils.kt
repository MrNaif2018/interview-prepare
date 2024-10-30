package com.mrnaif.interviewhelper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable


fun sendToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun navigateTo(
    context: Context,
    activity: Class<out Activity>,
    params: Map<String, Serializable> = emptyMap()
) {
    val intent = Intent(context, activity)
    val bundle = Bundle()
    for ((key, value) in params) {
        bundle.putSerializable(key, value)
    }
    intent.putExtras(bundle)
    context.startActivity(intent)
    if (context is Activity) {
        (context as Activity).overridePendingTransition(
            R.anim.swipe_left,
            R.anim.swipe_right,
        )
    }
    (context as Activity).finish()
}

fun getUserEmail(): String? {
    val sharedPref =
        InterviewHelper.applicationContext()
            .getSharedPreferences(Constants.SETTINGS_KEY, Context.MODE_PRIVATE)
    return sharedPref?.getString("email", null)
}

fun tryParseJSON(data: String): JSONObject? {
    return try {
        JSONObject(data)
    } catch (e: JSONException) {
        null
    }
}