package com.mrnaif.interviewhelper

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_constrained)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sharedPref =
            getSharedPreferences(Constants.SETTINGS_KEY, Context.MODE_PRIVATE) ?: return
        val email = sharedPref.getString("email", null)
        val token = sharedPref.getString("token", null)
        if (token == null) {
            navigateTo(this, MainActivity::class.java)
        }
        val label = findViewById<TextView>(R.id.welcome_text)
        label.text = "Добро пожаловать, $email!\nВаш токен: $token\n"
        val logoutButton = findViewById<Button>(R.id.logout_button)
        logoutButton.setOnClickListener {
            sharedPref.edit().clear().apply();
            navigateTo(this, MainActivity::class.java)
        }
        val itemsList = findViewById<RecyclerView>(R.id.itemsList)
        val items = arrayListOf<String>()
        items.add("Тест1")
        items.add("Тест2")
        items.add("Тест3")
        itemsList.layoutManager = LinearLayoutManager(this)
        itemsList.adapter = ItemsAdapter(items, this)
    }
}