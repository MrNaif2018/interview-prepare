package com.mrnaif.interviewhelper

import android.app.Application
import android.content.Context

class InterviewHelper : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: InterviewHelper? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}