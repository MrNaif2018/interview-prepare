package com.mrnaif.interviewhelper

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_constrained)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val label = findViewById<TextView>(R.id.welcome_text)
        val email = getUserEmail()
        label.text = "Добро пожаловать, $email!"
        val backBtn = findViewById<Button>(R.id.back_button)
        backBtn.setOnClickListener {
            navigateTo(this, MainPageActivity::class.java)
        }
        val ctx = this
        window.decorView.setOnTouchListener(object : OnSwipeTouchListener(ctx) {
            override fun onSwipeTop() {
                super.onSwipeTop()
            }

            override fun onSwipeBottom() {
                super.onSwipeBottom()
            }

            override fun onSwipeLeft() {
                super.onSwipeLeft()
            }

            override fun onSwipeRight() {
                navigateTo(ctx, MainPageActivity::class.java)
                finish()
            }
        })
    }
}