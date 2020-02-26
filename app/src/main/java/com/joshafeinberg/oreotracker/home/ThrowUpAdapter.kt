package com.joshafeinberg.oreotracker.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.joshafeinberg.oreotracker.R
import com.joshafeinberg.oreotracker.sharedmodule.ThrowUp
import com.joshafeinberg.oreotracker.sharedmodule.Time
import com.joshafeinberg.oreotracker.util.DateUtil
import com.joshafeinberg.oreotracker.util.readableName
import com.joshafeinberg.oreotracker.util.toFormattedDate


class ThrowUpAdapter : RecyclerView.Adapter<ThrowUpAdapter.ViewHolder>() {

    var items: List<ThrowUp> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_throw_up, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textDate = itemView.findViewById<TextView>(R.id.text_date)
        private val textTime = itemView.findViewById<TextView>(R.id.text_time)
        private val textContent = itemView.findViewById<TextView>(R.id.text_content)

        fun bind(item: ThrowUp) {
            textDate.text = item.date.toFormattedDate(DateUtil.DATE_FORMAT)
            textTime.text = when (item.time) {
                is Time.ExactTime -> itemView.context.getString(
                    R.string.exact_time_format,
                    (item.time as Time.ExactTime).exactTime.toFormattedDate(DateUtil.TIME_FORMAT)
                )
                else -> item.time.javaClass.readableName
            }
            textContent.text = item.content.readableName
        }
    }

}
