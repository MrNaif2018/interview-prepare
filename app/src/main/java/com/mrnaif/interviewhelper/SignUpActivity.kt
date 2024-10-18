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

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_constrained)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toLogin = findViewById<TextView>(R.id.has_account)
        toLogin.setOnClickListener {
            navigateTo(this, MainActivity::class.java)
        }
        val signupBtn = findViewById<Button>(R.id.signup_btn)
        signupBtn.setOnClickListener(this::doSignup)
    }

    private fun doSignup(view: View) {
        val emailField = findViewById<EditText>(R.id.email)
        val passwordField1 = findViewById<EditText>(R.id.password1)
        val passwordField2 = findViewById<EditText>(R.id.password2)
        val email = emailField.text.toString().trim()
        val password1 = passwordField1.text.toString().trim()
        val password2 = passwordField2.text.toString().trim()
        if (email == "" || password1 == "" || password2 == "") {
            return sendToast("Пожалуйста, заполните все поля")
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return sendToast("Неправильный e-mail")
        }
        if (password1.length < 8) {
            return sendToast("Пароль минимум из 8 символов")
        }
        if (password1 != password2) {
            return sendToast("Пароли должны совпадать")
        }
        sendJsonPostRequest(
            "${Constants.API_URL}/users", JSONObject().apply {
                put("email", email)
                put("password", password1)
            }, mapOf(
                "Content-Type" to "application/json"
            )
        ) { response, error ->
            if (error != null) {
                tryParseJSON(error)?.let {
                    try {
                        val detail = it.getString("detail")
                        if ("already exists" in detail) {
                            Handler(Looper.getMainLooper()).post {
                                sendToast("Такой пользователь уже зарегистрирован")
                            }
                        }
                    } catch (_: JSONException) {
                    }
                }
            } else {
                // Handle success, response is already a JSONObject
                response?.let {
                    try {
                        val token = it.getString("token")
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

    fun sendToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}