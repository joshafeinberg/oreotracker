package com.joshafeinberg.oreotracker.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.isFocused
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun FilledTextInputComponent(
        modifier: Modifier = Modifier,
        label: String,
        value: String,
        onFocusChange: ((Boolean) -> Unit)? = null,
        onValueChange: (String) -> Unit = {},
        keyboardType: KeyboardType = KeyboardType.Text,
        characterFilter: Regex? = null
) {
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    var isFocused by remember { mutableStateOf(false) }

    TextField(
            value = value,
            onValueChange = { newValue ->
                if (characterFilter == null || characterFilter.matches(newValue) || newValue.isEmpty()) {
                    onValueChange(newValue)
                }
            },
            modifier = modifier.then(Modifier.padding(16.dp)).then(Modifier.fillMaxWidth())
                    .focusRequester(focusRequester = focusRequester)
                    .onFocusChanged { newFocusState ->
                        if (onFocusChange != null && isFocused != newFocusState.isFocused) {
                            onFocusChange(newFocusState.isFocused)
                            isFocused = newFocusState.isFocused
                        }
                    },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}

@Composable
fun ClickableTextInputComponent(
        modifier: Modifier = Modifier,
        label: String,
        value: String,
        onInputClicked: () -> Unit = {}) {
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    var isFocused by remember { mutableStateOf(false) }

    TextField(
            modifier = modifier.then(Modifier.padding(16.dp)).then(Modifier.fillMaxWidth())
                    .clickable(onClick = onInputClicked)
                    .focusRequester(focusRequester = focusRequester)
                    .onFocusChanged {
                        Log.d("textcomposable", "current state -> $isFocused; new state -> ${it.isFocused}")
                        if (it.isFocused && !isFocused) {
                            onInputClicked()
                        }
                        isFocused = it.isFocused
                        focusRequester.freeFocus()
                    },
            value = value,
            onValueChange = { },
            label = { Text(text = label) },
    )
}
