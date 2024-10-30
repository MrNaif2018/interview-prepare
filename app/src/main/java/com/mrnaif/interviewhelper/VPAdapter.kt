package com.mrnaif.interviewhelper

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class VPAdapter(fragmentActivity: FragmentActivity, private val viewModel: QuestionViewModel) :
    FragmentStateAdapter(fragmentActivity) {

    companion object {
        private const val QUESTION_FRAGMENT_ID = 0L
        private const val SOLUTION_FRAGMENT_ID = 1L
        private const val DISCUSSION_FRAGMENT_ID = 2L
        private const val UNSOLVED_FRAGMENT_ID = 3L
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return if (getUserEmail() in (viewModel.question.value as Question).solutions) {
            when (position) {
                0 -> QuestionFragment()
                1 -> SolutionFragment()
                else -> DiscussionFragment()
            }
        } else {
            when (position) {
                0 -> QuestionFragment()
                else -> UnsolvedFragment()
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return if (getUserEmail() in (viewModel.question.value as Question).solutions) {
            when (position) {
                0 -> QUESTION_FRAGMENT_ID
                1 -> SOLUTION_FRAGMENT_ID
                else -> DISCUSSION_FRAGMENT_ID
            }
        } else {
            when (position) {
                0 -> QUESTION_FRAGMENT_ID
                else -> UNSOLVED_FRAGMENT_ID
            }
        }
    }

    override fun containsItem(itemId: Long): Boolean {
        val emailInSolutions = getUserEmail() in (viewModel.question.value as Question).solutions
        val validItemIds = if (emailInSolutions) {
            listOf(QUESTION_FRAGMENT_ID, SOLUTION_FRAGMENT_ID, DISCUSSION_FRAGMENT_ID)
        } else {
            listOf(QUESTION_FRAGMENT_ID, UNSOLVED_FRAGMENT_ID)
        }
        return validItemIds.contains(itemId)
    }
}
