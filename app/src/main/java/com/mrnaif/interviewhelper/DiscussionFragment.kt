package com.mrnaif.interviewhelper

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 * Use the [DiscussionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiscussionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private val viewModel: QuestionViewModel by activityViewModels()
    private lateinit var mAdapter: CommentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discussion, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mAdapter = CommentsAdapter(context)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val question = viewModel.question.value ?: return
        val commentsList = view.findViewById<RecyclerView>(R.id.question_reply_list)
        commentsList.layoutManager = LinearLayoutManager(context)
        commentsList.adapter = mAdapter
        val sendField = view.findViewById<EditText>(R.id.send_msg_text)
        val sendBtn = view.findViewById<Button>(R.id.send_msg_btn)
        sendBtn.setOnClickListener {
            val sendText = sendField.text.toString()
            sendField.text.clear()
            sendJsonPostRequest(
                "POST",
                "${Constants.API_URL}/questions/${question.id}/comment",
                JSONObject().apply {
                    put("message", sendText)
                }
            ) { response, error ->
                Handler(Looper.getMainLooper()).post {
                    addComment(view, Comment(email = "Me", message = sendText))
                }
                Log.w("debug", response.toString())
            }
        }
        for (comment in question.comments) {
            addComment(view, comment)
        }

//        val scrollView = view.findViewById<NestedScrollView>(R.id.scroll_view)
//        scrollView.setOnTouchListener { v, event ->
//            v.parent.requestDisallowInterceptTouchEvent(true)
//            false
//        }
    }

    fun addComment(view: View, comment: Comment) {
        val inflater: LayoutInflater = InterviewHelper.applicationContext().getSystemService(
            Context.LAYOUT_INFLATER_SERVICE
        ) as LayoutInflater
        mAdapter.addComment(comment)
//        val newView: View = inflater.inflate(R.layout.question_reply_layout, null)
//        val container = view.findViewById<ListView>(R.id.question_reply_layout)
//        val commentText = newView.findViewById<TextView>(R.id.question_reply)
//        commentText.text = comment.message
//        commentText.setTextColor(Color.BLACK)
//        container.addView(newView)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DiscussionFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DiscussionFragment().apply {
            }
    }
}