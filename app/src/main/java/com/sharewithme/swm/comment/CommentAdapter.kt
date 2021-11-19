package com.sharewithme.swm.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.sharewithme.swm.R

class CommentAdapter(private val commentList : MutableList<CommentModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return commentList.size
    }

    override fun getItem(position: Int): Any {
        return commentList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.comment_list_item, parent, false)
        }

        val nickname = view?.findViewById<TextView>(R.id.tvNickname)
        val content = view?.findViewById<TextView>(R.id.tvCommentContent)
        val time = view?.findViewById<TextView>(R.id.tvWrittenTime)


        nickname!!.text = commentList[position].nickname
        content!!.text = commentList[position].commentContent
        time!!.text = commentList[position].commentWrittenTime

        return view!!
    }

}