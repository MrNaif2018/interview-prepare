package com.mrnaif.interviewhelper

import java.io.Serializable

data class Comment(val email: String, val message: String) : Serializable {}

data class Question(
    val id: String,
    val name: String,
    val question: String,
    val options: List<String>,
    val answer: String,
    val company: String,
    val difficulty: String,
    val hints: List<String>,
    val comments: List<Comment>,
    val solutions: List<String>
) :
    Serializable {
}