package com.joshafeinberg.oreotracker.weight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joshafeinberg.oreotracker.R
import com.joshafeinberg.oreotracker.databinding.ItemWeightBinding
import com.joshafeinberg.oreotracker.sharedmodule.Weight
import com.joshafeinberg.oreotracker.util.DateUtil
import com.joshafeinberg.oreotracker.util.toFormattedDate

class WeightAdapter : RecyclerView.Adapter<WeightAdapter.ViewHolder>() {

    var items: List<Weight> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_weight, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemWeightBinding.bind(itemView)

        fun bind(item: Weight) {
            binding.textDate.text = item.date.toFormattedDate(DateUtil.DATE_FORMAT)
            binding.textWeight.text = String.format("%.1f lbs", item.weight)
        }
    }
}
