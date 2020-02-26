package com.joshafeinberg.oreotracker.stats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.joshafeinberg.oreotracker.R
import com.joshafeinberg.oreotracker.arch.state
import com.joshafeinberg.oreotracker.arch.util.observe
import com.joshafeinberg.oreotracker.databinding.FragmentStatsBinding
import com.joshafeinberg.oreotracker.sharedmodule.Content
import com.joshafeinberg.oreotracker.sharedmodule.Stats
import com.joshafeinberg.oreotracker.sharedmodule.Time
import com.joshafeinberg.oreotracker.util.readableName
import com.joshafeinberg.oreotracker.util.viewBinding

class StatsFragment : Fragment(R.layout.fragment_stats) {

    private val statsViewModel: StatsViewModel by activityViewModels()
    private val binding by viewBinding(FragmentStatsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statsViewModel.state.observe(viewLifecycleOwner) { state ->
            if (state.isLoading) {
                binding.loading.show()
            } else {
                binding.loading.hide()
            }

            state.stats?.let {
                showStats(it)
            }
        }
    }

    private fun showStats(stats: Stats) {
        binding.text30DayHeader.text = getString(R.string.last_30_days, stats.lastThirtyDayCount)
        binding.text7DayHeader.text = getString(R.string.last_7_days, stats.lastSevenDayCount)

        val contents = Content.values().map { it.name.toLowerCase() }
        var thirtyDayContentString = ""
        var sevenDayContentString = ""
        for (content in contents) {
            val thirtyDayCount = stats.lastThirtyDayContentCount[content] ?: 0
            val sevenDayCount = stats.lastSevenDayContentCount[content] ?: 0

            thirtyDayContentString += "${content.capitalize()}: $thirtyDayCount\n"
            sevenDayContentString += "${content.capitalize()}: $sevenDayCount\n"
        }

        binding.text30DayContent.text = thirtyDayContentString
        binding.text7DayContent.text = sevenDayContentString

        val times = Time::class.sealedSubclasses.map { it.java }
        var thirtyDayTimesString = ""
        var sevenDayTimesString = ""
        for (time in times) {
            val tempTime = time.simpleName.toLowerCase()
            val thirtyDayCount = stats.lastThirtyDayTimeCount[tempTime] ?: 0
            val sevenDayCount = stats.lastSevenDayTimeCount[tempTime] ?: 0

            thirtyDayTimesString += "${time.readableName}: $thirtyDayCount\n"
            sevenDayTimesString += "${time.readableName}: $sevenDayCount\n"
        }

        binding.text30DayTime.text = thirtyDayTimesString
        binding.text7DayTime.text = sevenDayTimesString

    }
}