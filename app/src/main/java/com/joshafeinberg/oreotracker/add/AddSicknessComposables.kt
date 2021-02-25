package com.joshafeinberg.oreotracker.add

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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import com.joshafeinberg.oreotracker.R
import com.joshafeinberg.oreotracker.ui.FilledTextInputComponent
import com.joshafeinberg.oreotracker.ui.Loading
import com.joshafeinberg.oreotracker.util.DateUtil
import com.joshafeinberg.oreotracker.util.toFormattedDate
import java.util.*

@Composable
fun AddSicknessPage(
        stateLiveData: LiveData<AddViewState>,
        isLoading: Boolean,
        onDatePickerSelected: (Long) -> Unit,
        onSaveClicked: () -> Unit,
        onExitClicked: () -> Unit
) {
    val state by stateLiveData.observeAsState(initial = AddViewState())

    AddSicknessLayout(
            state,
            onDatePickerSelected = onDatePickerSelected,
            onSaveClicked = { onSaveClicked() },
            onExitClicked = onExitClicked
    )

    if (isLoading) {
        Loading()
    }
}

@Composable
fun AddSicknessLayout(
        state: AddViewState,
        onDatePickerSelected: (Long) -> Unit,
        onSaveClicked: () -> Unit,
        onExitClicked: () -> Unit
) {
    Column {
        TopAppBar(
                title = { Text(text = stringResource(R.string.add_title)) },
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

        FilledTextInputComponent(
                label = stringResource(R.string.date),
                value = state.selectedDate.toFormattedDate(DateUtil.DATE_FORMAT),
                onFocusChange = { focused ->
                    if (focused) {
                        onDatePickerSelected(state.selectedDate)
                    }
                }
        )
    }
}