package com.sharewithme.swm.declare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.sharewithme.swm.R


class DeclareListAdapter(val declareList : MutableList<DeclareModel>) : BaseAdapter() {

    override fun getCount(): Int {
        return declareList.size
    }

    override fun getItem(position: Int): Any {
        return declareList[position]
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = convertView
        view = LayoutInflater.from(parent?.context).inflate(R.layout.declare_list_item, parent, false)

        val itemLinearLayoutView = view?.findViewById<LinearLayout>(R.id.itemView)
        val title = view?.findViewById<TextView>(R.id.titleArea)
        val content = view?.findViewById<TextView>(R.id.contentArea)

        title!!.text = declareList[position].title
        content!!.text = declareList[position].content

        return view!!
    }
}