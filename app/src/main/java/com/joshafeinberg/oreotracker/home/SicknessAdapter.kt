package com.joshafeinberg.oreotracker.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joshafeinberg.oreotracker.R
import com.joshafeinberg.oreotracker.databinding.ItemThrowUpBinding
import com.joshafeinberg.oreotracker.sharedmodule.ThrowUp
import com.joshafeinberg.oreotracker.sharedmodule.Time
import com.joshafeinberg.oreotracker.util.DateUtil
import com.joshafeinberg.oreotracker.util.readableName
import com.joshafeinberg.oreotracker.util.toFormattedDate

class SicknessAdapter : RecyclerView.Adapter<SicknessAdapter.ViewHolder>() {

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
        private val binding = ItemThrowUpBinding.bind(itemView)

        fun bind(item: ThrowUp) {
            binding.textDate.text = item.date.toFormattedDate(DateUtil.DATE_FORMAT)
            binding.textTime.text = when (item.time) {
                is Time.ExactTime -> itemView.context.getString(
                    R.string.exact_time_format,
                    (item.time as Time.ExactTime).exactTime.toFormattedDate(DateUtil.TIME_FORMAT)
                )
                else -> item.time.javaClass.readableName
            }
            binding.textContent.text = item.content.readableName
        }
    }
}
