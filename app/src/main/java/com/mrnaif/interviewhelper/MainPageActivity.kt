package com.mrnaif.interviewhelper

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class MainPageActivity : AppCompatActivity() {
    private val mAdapter: ItemsAdapter = ItemsAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_constrained)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val itemsList = findViewById<RecyclerView>(R.id.itemsList)
        itemsList.layoutManager = LinearLayoutManager(this)
        itemsList.adapter = mAdapter

        val sharedPref =
            getSharedPreferences(Constants.SETTINGS_KEY, Context.MODE_PRIVATE) ?: return
        val email = sharedPref.getString("email", null)
        val token = sharedPref.getString("token", null)
        if (token == null) {
            navigateTo(this, MainActivity::class.java)
        }
        sendJsonPostRequest(
            "GET",
            "${Constants.API_URL}/questions"
        ) { response, error ->
            Log.w("debug", "WE RUN")
            Log.w("debug", response.toString())
            val result = arrayListOf<Question>()
            val respList = response?.getJSONArray("result") ?: return@sendJsonPostRequest
            for (idx in 0 until respList.length()) {
                result.add(
                    Gson().fromJson(
                        respList.getString(idx),
                        Question::class.java
                    )
                )
            }
            Handler(Looper.getMainLooper()).post {
                mAdapter.setItems(result)
            }
        }
//        val label = findViewById<TextView>(R.id.welcome_text)
//        label.text = "Добро пожаловать, $email!\nВаш токен: $token\n"
        val logoutButton = findViewById<Button>(R.id.logout_button)
        logoutButton.setOnClickListener {
            sharedPref.edit().clear().apply();
            navigateTo(this, MainActivity::class.java)
//            finish()
        }
    }
}