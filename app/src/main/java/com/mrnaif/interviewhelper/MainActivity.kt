package com.mrnaif.interviewhelper

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_constrained)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sharedPref =
            getSharedPreferences(Constants.SETTINGS_KEY, Context.MODE_PRIVATE) ?: return
        val token = sharedPref.getString("token", null)
        if (token != null) {
            navigateTo(this, MainPageActivity::class.java)
        }
        val button = findViewById<Button>(R.id.login_btn)
        val toRegister = findViewById<TextView>(R.id.no_account)
        button.setOnClickListener(this::doLogin)
        toRegister.setOnClickListener {
            navigateTo(this, SignUpActivity::class.java)
        }

    }

    private fun doLogin(view: View) {
        val emailField = findViewById<EditText>(R.id.email)
        val passwordField = findViewById<EditText>(R.id.password)
        val email = emailField.text.toString().trim()
        val password = passwordField.text.toString().trim()
        if (email == "" || password == "") {
            return sendToast("Пожалуйста, заполните все поля")
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return sendToast("Неправильный e-mail")
        }
        sendJsonPostRequest(
            "${Constants.API_URL}/token", JSONObject().apply {
                put("email", email)
                put("password", password)
            }, mapOf(
                "Content-Type" to "application/json"
            )
        ) { response, error ->
            if (error != null) {
                tryParseJSON(error)?.let {
                    try {
                        val detail = it.getJSONObject("detail")
                        try {
                            val status = detail.getInt("status")
                            if (status == 404) {
                                Handler(Looper.getMainLooper()).post {
                                    sendToast("Такого пользователя не существует")
                                }
                            }
                            if (status == 401) {
                                Handler(Looper.getMainLooper()).post {
                                    sendToast("Неправильный пароль")
                                }
                            }
                        } catch (_: JSONException) {
                        }
                    } catch (_: JSONException) {
                    }
                }
                Log.w("debug", "Ошибка: $error")
            } else {
                // Handle success, response is already a JSONObject
                response?.let {
                    Handler(Looper.getMainLooper()).post {
                        try {
                            val token = it.getString("access_token")
                            val sharedPref =
                                getSharedPreferences(Constants.SETTINGS_KEY, Context.MODE_PRIVATE)
                            with(sharedPref.edit()) {
                                putString("email", email)
                                putString("token", token)
                                apply()
                            }
                            Log.w("debug", token)
                            navigateTo(this, MainPageActivity::class.java)
                        } catch (_: JSONException) {
                            Handler(Looper.getMainLooper()).post {
                                sendToast("Неожиданная ошибка")
                            }
                        }
                    }

                }
            }
        }

    }

    fun sendToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}