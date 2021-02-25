package com.joshafeinberg.oreotracker.weight

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.joshafeinberg.oreotracker.arch.state
import com.joshafeinberg.oreotracker.ui.Loading
import com.joshafeinberg.oreotracker.util.DateUtil
import com.joshafeinberg.oreotracker.util.toFormattedDate

@Composable
fun WeightScreen(innerPadding: PaddingValues) {
    val viewModel: WeightViewModel = viewModel()
    val weightState = viewModel.state.observeAsState()

    WeightList(innerPadding = innerPadding, weightState = weightState)
}

@Composable
fun WeightList(innerPadding: PaddingValues, weightState: State<WeightViewState?>) {
    val state: WeightViewState = weightState.value ?: return

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
                        Text(text = String.format("%.1f lbs", item.weight))
                    }
                }
            }
        }
    }
}