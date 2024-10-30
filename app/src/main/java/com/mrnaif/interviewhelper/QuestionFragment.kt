package com.mrnaif.interviewhelper

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 * Use the [QuestionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuestionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private val viewModel: QuestionViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = view.findViewById<TextView>(R.id.question_detail_name)
        val text = view.findViewById<TextView>(R.id.question_detail_text)
        val radioGroup = view.findViewById<RadioGroup>(R.id.question_detail_options)
        val hintPrompt = view.findViewById<TextView>(R.id.need_hint_prompt)
        val hintText = view.findViewById<TextView>(R.id.question_hints)
        val submitBtn = view.findViewById<Button>(R.id.question_submit)
        val question = viewModel.question.value ?: return
        name.text = question.name
        text.text = question.question
        var currentHint = -1

        for (idx in 0..<question.options.size) {
            val rdbtn = RadioButton(view.context)
            rdbtn.id = View.generateViewId()
            rdbtn.text = question.options[idx]
            radioGroup.addView(rdbtn)
        }

        hintPrompt.setOnClickListener {
            if (currentHint + 1 < question.hints.size) {
                currentHint++
                hintText.text = question.hints[currentHint]
                hintText.visibility = View.VISIBLE
            }
        }

        submitBtn.setOnClickListener {
            val radioButtonID: Int = radioGroup.getCheckedRadioButtonId()
            val radioButton =
                radioGroup.findViewById<RadioButton>(radioButtonID) ?: return@setOnClickListener
            val toSend = radioButton.text.toString()
            sendJsonPostRequest(
                "POST",
                "${Constants.API_URL}/questions/${question.id}/solve",
                JSONObject().apply {
                    put("answer", toSend)
                }
            ) { response, error ->
                val myEmail = getUserEmail()
                val newQuestion = Gson().fromJson(
                    response.toString(),
                    Question::class.java
                )
                if (newQuestion == null) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Неправильный ответ!", Toast.LENGTH_SHORT).show()
                    }
                    return@sendJsonPostRequest
                }
                if (myEmail in newQuestion.solutions) {
                    Handler(Looper.getMainLooper()).post {
                        viewModel.question.value = newQuestion
                        markSolved(view)
                    }
                } else {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Неправильный ответ!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        if (getUserEmail() in question.solutions) {
            markSolved(view, false)
        }
    }

    fun markSolved(view: View, notify: Boolean = true) {
        val submitBtn = view.findViewById<Button>(R.id.question_submit)
        val solvedText = view.findViewById<TextView>(R.id.solved_txt)
        if (notify) {
            Toast.makeText(context, "Правильный ответ!", Toast.LENGTH_SHORT).show()
        }
        submitBtn.setEnabled(false)
        solvedText.visibility = View.VISIBLE
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QuestionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QuestionFragment().apply {
            }
    }
}