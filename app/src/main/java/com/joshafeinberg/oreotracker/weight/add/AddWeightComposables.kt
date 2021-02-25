package com.joshafeinberg.oreotracker.weight.add

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.LiveData
import com.joshafeinberg.oreotracker.R
import com.joshafeinberg.oreotracker.ui.ClickableTextInputComponent
import com.joshafeinberg.oreotracker.ui.FilledTextInputComponent
import com.joshafeinberg.oreotracker.ui.Loading
import com.joshafeinberg.oreotracker.util.DateUtil
import com.joshafeinberg.oreotracker.util.toFormattedDate
import java.util.*

class DogWeight(myWeight: String, ourWeight: String) {
    var myWeight by mutableStateOf(myWeight)
    var ourWeight by mutableStateOf(ourWeight)
}

@Composable
fun AddWeightPage(
        stateLiveData: LiveData<AddWeightViewState>,
        isLoading: Boolean,
        onDatePickerSelected: (Long) -> Unit,
        onSaveClicked: (DogWeight) -> Unit,
        onExitClicked: () -> Unit
) {
    val state by stateLiveData.observeAsState(initial = AddWeightViewState())
    val weights = remember { DogWeight("", "") }

    AddWeightLayout(
            state,
            onDatePickerSelected = onDatePickerSelected,
            onExitClicked = onExitClicked,
            weights = weights,
            onMyWeightChanged = { newWeight ->
                weights.myWeight = newWeight
            },
            onOurWeightChanged = { newWeight ->
                weights.ourWeight = newWeight
            },
            onSaveClicked = { onSaveClicked(weights) }
    )

    if (isLoading) {
        Loading()
    }
}

@Composable
fun AddWeightLayout(
        state: AddWeightViewState,
        onDatePickerSelected: (Long) -> Unit,
        onExitClicked: () -> Unit,
        weights: DogWeight,
        onMyWeightChanged: (String) -> Unit,
        onOurWeightChanged: (String) -> Unit,
        onSaveClicked: () -> Unit
) {
    Column {
        TopAppBar(
                title = { Text(text = stringResource(R.string.weight_add)) },
                navigationIcon = {
                    IconButton(onClick = onExitClicked) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        onSaveClicked()
                    }) {
                        Text(text = stringResource(R.string.save).toUpperCase(Locale.getDefault()))
                    }
                }
        )

        ClickableTextInputComponent(
                label = stringResource(R.string.date),
                value = state.selectedDate.toFormattedDate(DateUtil.DATE_FORMAT),
                onInputClicked = { onDatePickerSelected(state.selectedDate) }
        )

        FilledTextInputComponent(
                label = stringResource(id = R.string.weight_add_me),
                value = weights.myWeight,
                onValueChange = onMyWeightChanged,
                keyboardType = KeyboardType.Number,
                characterFilter = Regex("[0-9]+\\.?[0-9]{0,2}")
        )

        FilledTextInputComponent(
                label = stringResource(id = R.string.weight_add_ours),
                value = weights.ourWeight,
                onValueChange = onOurWeightChanged,
                keyboardType = KeyboardType.Number,
                characterFilter = Regex("[0-9]+\\.?[0-9]{0,2}")
        )
    }
}
