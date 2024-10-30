package com.mrnaif.interviewhelper

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class CommentsAdapter(var context: Context) :
    RecyclerView.Adapter<CommentsAdapter.MyViewHolder>() {

    val comments = mutableListOf<Comment>()

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text = view.findViewById<TextView>(R.id.question_reply)
        val email = view.findViewById<TextView>(R.id.question_email)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.question_reply_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return comments.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.text.text = comments[position].message
        if (comments[position].email == getUserEmail() || comments[position].email == "Me")
            holder.email.text = "Me"
        else {
            holder.email.text = comments[position].email
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addComment(newComment: Comment) {
        comments.add(newComment)
        notifyDataSetChanged()
    }
}