package com.mrnaif.interviewhelper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView


class ItemsAdapter(var context: Context) :
    RecyclerView.Adapter<ItemsAdapter.MyViewHolder>() {

    val questions = mutableListOf<Question>()

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text = view.findViewById<TextView>(R.id.question_name)
        val company = view.findViewById<TextView>(R.id.question_company)
        val difficulty = view.findViewById<TextView>(R.id.question_difficulty)
        val topic = view.findViewById<TextView>(R.id.question_topic)

        var layout = view.findViewById<ConstraintLayout>(R.id.question_list_layout)

//        val radioGroup = view.findViewById<RadioGroup>(R.id.radio_group)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return questions.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.text.text = questions[position].name
        holder.company.text = questions[position].company
        val difficulty = questions[position].difficulty
        holder.difficulty.text = difficulty.replaceFirstChar(Char::titlecase)
        holder.topic.text = questions[position].topic
        val difficultyColor = when (difficulty) {
            "easy" -> Color.GREEN
            "medium" -> Color.YELLOW
            "hard" -> Color.RED
            else -> { // Note the block
                Color.BLACK
            }
        }
        holder.difficulty.setTextColor(difficultyColor)
        holder.layout.setOnClickListener {
            navigateTo(
                context,
                QuestionDetail::class.java,
                mapOf("question" to questions[position])
            )
//            (context as MainPageActivity).finish()
        }
//        for (idx in 0..<questions[position].options.size) {
//            val rdbtn = RadioButton(context)
//            rdbtn.id = View.generateViewId()
//            rdbtn.text = questions[position].options[idx]
////            rdbtn.setOnClickListener(this)
//            holder.radioGroup.addView(rdbtn)
//        }
    }

//    fun onClick(v: View) {
//        Log.d(TAG, " Name " + (v as RadioButton).text + " Id is " + v.getId())
//    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newItems: List<Question>) {
        questions.clear()
        questions.addAll(newItems)
        notifyDataSetChanged()
    }
}