package com.mrnaif.interviewhelper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuestionViewModel : ViewModel() {
    val question: MutableLiveData<Question> = MutableLiveData()
}