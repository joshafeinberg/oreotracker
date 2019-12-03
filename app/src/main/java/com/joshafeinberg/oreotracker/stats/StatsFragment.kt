package com.joshafeinberg.oreotracker.stats

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.joshafeinberg.oreotracker.R
import com.joshafeinberg.oreotracker.arch.state
import com.joshafeinberg.oreotracker.arch.util.observe
import com.joshafeinberg.oreotracker.sharedmodule.Content
import com.joshafeinberg.oreotracker.sharedmodule.Stats
import com.joshafeinberg.oreotracker.sharedmodule.Time
import com.joshafeinberg.oreotracker.util.readableName

class StatsFragment : Fragment(R.layout.fragment_stats) {

    private val statsViewModel: StatsViewModel by activityViewModels()
    private lateinit var textThirtyDayHeader: TextView
    private lateinit var textThirtyDayContent: TextView
    private lateinit var textThirtyDayTime: TextView
    private lateinit var textSevenDayHeader: TextView
    private lateinit var textSevenDayContent: TextView
    private lateinit var textSevenDayTime: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loadingView = view.findViewById<ContentLoadingProgressBar>(R.id.loading)
        textThirtyDayHeader = view.findViewById(R.id.text_30_day_header)
        textThirtyDayContent = view.findViewById(R.id.text_30_day_content)
        textThirtyDayTime = view.findViewById(R.id.text_30_day_time)
        textSevenDayHeader = view.findViewById(R.id.text_7_day_header)
        textSevenDayContent = view.findViewById(R.id.text_7_day_content)
        textSevenDayTime = view.findViewById(R.id.text_7_day_time)

        statsViewModel.state.observe(this) { state ->
            if (state.isLoading) {
                loadingView.show()
            } else {
                loadingView.hide()
            }

            state.stats?.let {
                showStats(it)
            }
        }

        statsViewModel.downloadStats()
    }

    private fun showStats(stats: Stats) {
        textThirtyDayHeader.text = getString(R.string.last_30_days, stats.lastThirtyDayCount)
        textSevenDayHeader.text = getString(R.string.last_7_days, stats.lastSevenDayCount)

        val contents = Content.values().map { it.name.toLowerCase() }
        var thirtyDayContentString = ""
        var sevenDayContentString = ""
        for (content in contents) {
            val thirtyDayCount = stats.lastThirtyDayContentCount[content] ?: 0
            val sevenDayCount = stats.lastSevenDayContentCount[content] ?: 0

            thirtyDayContentString += "${content.capitalize()}: $thirtyDayCount\n"
            sevenDayContentString += "${content.capitalize()}: $sevenDayCount\n"
        }

        textThirtyDayContent.text = thirtyDayContentString
        textSevenDayContent.text = sevenDayContentString

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

        textThirtyDayTime.text = thirtyDayTimesString
        textSevenDayTime.text = sevenDayTimesString

    }
}