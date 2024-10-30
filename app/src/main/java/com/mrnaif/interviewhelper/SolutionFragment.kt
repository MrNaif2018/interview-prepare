package com.mrnaif.interviewhelper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

/**
 * A simple [Fragment] subclass.
 * Use the [SolutionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SolutionFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_solution, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val solutionText = view.findViewById<TextView>(R.id.question_solution)
        val question = viewModel.question.value ?: return
        solutionText.text = question.answer
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SolutionFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SolutionFragment().apply {
            }
    }
}