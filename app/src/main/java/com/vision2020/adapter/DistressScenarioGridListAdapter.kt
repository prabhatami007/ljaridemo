package com.vision2020.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.vision2020.R
import com.vision2020.ui.distress.DistressScenarioFragment


internal class DistressScenarioGridListAdapter internal constructor(context: Context, private val resource: Int, private val itemList: MutableList<String>) : ArrayAdapter<DistressScenarioGridListAdapter.ItemHolder>(context, resource) {

    override fun getCount(): Int {
        return if (this.itemList != null) this.itemList.size else 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val holder: ItemHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, null)
            holder = ItemHolder()
            holder.name = convertView!!.findViewById(R.id.textView)


            // holder.icon = convertView.findViewById(R.id.icon)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ItemHolder
        }

        holder.name!!.text = this.itemList!![position]
        //holder.icon!!.setImageResource(R.mipmap.ic_launcher)

        return convertView
    }

    internal class ItemHolder {
        var name: TextView? = null
        // var icon: ImageView? = null
    }
}