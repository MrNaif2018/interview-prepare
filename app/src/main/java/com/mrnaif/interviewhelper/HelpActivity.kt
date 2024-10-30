package com.mrnaif.interviewhelper

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_help)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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