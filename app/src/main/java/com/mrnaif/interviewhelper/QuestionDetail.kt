package com.mrnaif.interviewhelper

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2

class QuestionDetail : AppCompatActivity() {
    private var startX: Float = 0f
    private var endX: Float = 0f
    private val viewModel: QuestionViewModel by viewModels()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_question_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val question = intent.getSerializableExtra("question") as Question
        viewModel.question.value = question
        val viewPager = findViewById<ViewPager2>(R.id.vp)
        viewPager.adapter = VPAdapter(this, viewModel)

        val tabBar = findViewById<CustomTabBar>(R.id.tab_bar)
        tabBar.attachTo(viewPager)
        val backBtn = findViewById<ImageButton>(R.id.arrow_back)
        backBtn.setOnClickListener {
            navigateTo(this, MainPageActivity::class.java)
        }

//        val name = findViewById<TextView>(R.id.question_detail_name)
//        val text = findViewById<TextView>(R.id.question_detail_text)
//        val radioGroup = findViewById<RadioGroup>(R.id.question_detail_options)
//        val hintPrompt = findViewById<TextView>(R.id.need_hint_prompt)
//        val hintText = findViewById<TextView>(R.id.question_hints)
//        name.text = question.name
//        text.text = question.question
//        var currentHint = -1
//
//        for (idx in 0..<question.options.size) {
//            val rdbtn = RadioButton(this)
//            rdbtn.id = View.generateViewId()
//            rdbtn.text = question.options[idx]
//            radioGroup.addView(rdbtn)
//        }
//
//        hintPrompt.setOnClickListener {
//            if (currentHint + 1 < question.hints.size) {
//                currentHint++
//                hintText.text = question.hints[currentHint]
//                hintText.visibility = View.VISIBLE
//            }
//        }
//        val ctx = this
//        window.decorView.setOnTouchListener(object : OnSwipeTouchListener(ctx) {
//
//            override fun onSwipeTop() {
//                super.onSwipeTop()
//            }
//
//            override fun onSwipeBottom() {
//                super.onSwipeBottom()
//            }
//
//            override fun onSwipeLeft() {
//                super.onSwipeLeft()
//            }
//
//            override fun onSwipeRight() {
//                navigateTo(ctx, MainPageActivity::class.java)
//                finish()
//            }
//        })
    }
}