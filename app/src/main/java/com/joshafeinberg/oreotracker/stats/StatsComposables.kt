package com.joshafeinberg.oreotracker.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.joshafeinberg.oreotracker.R
import com.joshafeinberg.oreotracker.arch.state
import com.joshafeinberg.oreotracker.sharedmodule.Content
import com.joshafeinberg.oreotracker.sharedmodule.Time
import com.joshafeinberg.oreotracker.ui.Loading
import com.joshafeinberg.oreotracker.util.readableName
import java.util.*

@Composable
fun StatisticsScreen(innerPadding: PaddingValues) {
    val statsViewModel: StatsViewModel = viewModel()
    val statsState = statsViewModel.state.observeAsState()

    StatisticsLayout(innerPadding = innerPadding, statsState = statsState)
}

@Composable
fun StatisticsLayout(innerPadding: PaddingValues, statsState: State<StatsViewState?>) {
    val state: StatsViewState = statsState.value ?: return

    if (state.isLoading) {
        Loading(innerPadding = innerPadding)
    } else {
        val stats = state.stats!!

        ConstraintLayout(
                modifier = Modifier.padding(innerPadding).then(Modifier.fillMaxSize().padding(16.dp))) {
            val (text30DayHeader, layout30DayContent, layout30DayTime, text7DayHeader, layout7DayContent, layout7DayTime) = createRefs()
            val half = createGuidelineFromStart(fraction = 0.5f)
            val barrier = createBottomBarrier(layout30DayContent, layout30DayTime)

            Text(
                    modifier = Modifier.constrainAs(text30DayHeader) {
                        top.linkTo(parent.top)
                    },
                    text = stringResource(R.string.last_30_days, stats.lastThirtyDayCount),
                    style = MaterialTheme.typography.h5
            )

            Column(modifier = Modifier.constrainAs(layout30DayContent) {
                top.linkTo(text30DayHeader.bottom, margin = 8.dp)
                start.linkTo(parent.start)
            }) {
                ContentLayout(contentStats = stats.lastThirtyDayContentCount)
            }

            Column(modifier = Modifier.constrainAs(layout30DayTime) {
                top.linkTo(layout30DayContent.top)
                start.linkTo(half)
            }) {
                TimeLayout(timeStats = stats.lastThirtyDayTimeCount)
            }

            Text(
                    modifier = Modifier.constrainAs(text7DayHeader) {
                        top.linkTo(barrier, margin = 16.dp)
                    },
                    text = stringResource(R.string.last_7_days, stats.lastSevenDayCount),
                    style = MaterialTheme.typography.h5
            )

            Column(modifier = Modifier.constrainAs(layout7DayContent) {
                top.linkTo(text7DayHeader.bottom, margin = 8.dp)
                start.linkTo(parent.start)
            }) {
                ContentLayout(contentStats = stats.lastThirtyDayContentCount)
            }

            Column(modifier = Modifier.constrainAs(layout7DayTime) {
                top.linkTo(layout7DayContent.top)
                start.linkTo(half)
            }) {
                TimeLayout(timeStats = stats.lastThirtyDayTimeCount)
            }
        }
    }
}

@Composable
fun ContentLayout(contentStats: Map<String, Int>) {
    Text(
            text = stringResource(id = R.string.content),
            style = MaterialTheme.typography.h6
    )

    val contents = Content.values().map { it.name.toLowerCase(Locale.getDefault()) }
    for (content in contents) {
        val thirtyDayCount = contentStats[content] ?: 0
        Text(text = "${content.capitalize()}: $thirtyDayCount")
    }
}

@Composable
fun TimeLayout(timeStats: Map<String, Int>) {
    Text(
            text = stringResource(id = R.string.time),
            style = MaterialTheme.typography.h6
    )

    val times = Time::class.sealedSubclasses.map { it.java }
    for (time in times) {
        val tempTime = time.simpleName.toLowerCase()
        val thirtyDayCount = timeStats[tempTime] ?: 0

        Text(text = "${time.readableName}: $thirtyDayCount")
    }
}