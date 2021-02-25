package com.joshafeinberg.oreotracker.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.joshafeinberg.oreotracker.R
import com.joshafeinberg.oreotracker.arch.state
import com.joshafeinberg.oreotracker.sharedmodule.Time
import com.joshafeinberg.oreotracker.ui.Loading
import com.joshafeinberg.oreotracker.util.DateUtil
import com.joshafeinberg.oreotracker.util.readableName
import com.joshafeinberg.oreotracker.util.toFormattedDate

@Composable
fun SicknessScreen(innerPadding: PaddingValues) {
    val viewModel: HomeViewModel = viewModel()
    val homeState = viewModel.state.observeAsState()

    SicknessList(innerPadding = innerPadding, homeState = homeState)
}

@Composable
fun SicknessList(innerPadding: PaddingValues, homeState: State<HomeViewState?>) {
    val state: HomeViewState = homeState.value ?: return

    if (state.isLoading) {
        Loading(innerPadding = innerPadding)
    } else {
        LazyColumn(contentPadding = innerPadding) {
            items(state.items) { item ->
                Card(modifier = Modifier
                        .padding(8.dp)
                        .then(Modifier.fillParentMaxWidth())) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = item.date.toFormattedDate(DateUtil.DATE_FORMAT), style = MaterialTheme.typography.h6)
                        Text(text = when (item.time) {
                            is Time.ExactTime -> stringResource(R.string.exact_time_format, (item.time as Time.ExactTime).exactTime.toFormattedDate(DateUtil.TIME_FORMAT))
                            else -> item.time.javaClass.readableName
                        })
                        Text(text = item.content.readableName)
                    }
                }
            }
        }
    }
}