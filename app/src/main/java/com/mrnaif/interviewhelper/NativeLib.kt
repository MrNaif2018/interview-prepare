package com.mrnaif.interviewhelper

class NativeLib {
    init {
        System.loadLibrary("native-lib")
    }

    external fun isUserValid(username: String, password: String): Boolean
}