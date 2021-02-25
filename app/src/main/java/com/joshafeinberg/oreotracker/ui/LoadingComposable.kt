package com.joshafeinberg.oreotracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun Loading(modifier: Modifier = Modifier, innerPadding: PaddingValues? = null) {
    val customModifier = when {
        modifier == Modifier -> modifier
        innerPadding != null -> Modifier.padding(innerPadding)
        else -> Modifier
    }
    Column(modifier = customModifier.then(
            Modifier.fillMaxSize()
                    .background(color = Color(0xCCCCCCCC))
                    .clickable(onClick = {})
    ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(modifier = customModifier)
        Text(text = "Loading...")
    }
}
