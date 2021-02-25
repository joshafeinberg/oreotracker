package com.joshafeinberg.oreotracker

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.FragmentManager
import com.google.android.material.composethemeadapter.MdcTheme
import com.google.android.material.datepicker.MaterialDatePicker
import com.joshafeinberg.oreotracker.add.AddEvents
import com.joshafeinberg.oreotracker.add.AddSicknessPage
import com.joshafeinberg.oreotracker.add.AddViewModel
import com.joshafeinberg.oreotracker.arch.state
import com.joshafeinberg.oreotracker.home.HomeViewModel
import com.joshafeinberg.oreotracker.home.SicknessScreen
import com.joshafeinberg.oreotracker.stats.StatisticsScreen
import com.joshafeinberg.oreotracker.weight.WeightScreen
import com.joshafeinberg.oreotracker.weight.WeightViewModel
import com.joshafeinberg.oreotracker.weight.add.AddWeightEvents
import com.joshafeinberg.oreotracker.weight.add.AddWeightPage
import com.joshafeinberg.oreotracker.weight.add.AddWeightViewModel

class MainActivity : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val weightViewModel: WeightViewModel by viewModels()

    private val addViewModel: AddViewModel by viewModels()
    private val addWeightViewModel: AddWeightViewModel by viewModels()

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var screen by remember { mutableStateOf(Screens.HOME) }
            var selectedIndex by remember { mutableStateOf(0) }
            var isLoading by remember { mutableStateOf(false) }

            /*AmbientBackPressedDispatcher = staticCompositionLocalOf { this }
            backButtonHandler {
                when (screen) {
                    Screens.ADD_WEIGHT,
                    Screens.ADD_SICKNESS -> screen = Screens.HOME
                    else -> finish()
                }
            }*/

            addViewModel.events.observe(this) { event ->
                when (event) {
                    AddEvents.Saving -> isLoading = true
                    is AddEvents.ThrowUpSaved -> {
                        isLoading = false
                        homeViewModel.addItem(event.throwUp)
                        screen = Screens.HOME
                    }
                }
            }

            addWeightViewModel.events.observe(this) { event ->
                when (event) {
                    AddWeightEvents.Saving -> isLoading = true
                    is AddWeightEvents.WeightSaved -> {
                        isLoading = false
                        weightViewModel.addWeight(event.weight)
                        screen = Screens.HOME
                    }
                }
            }

            MdcTheme {
                when (screen) {
                    Screens.HOME -> HomeScreen(
                            selectedIndex = selectedIndex,
                            onPageChange = { selectedIndex = it },
                            onFabClicked = { selectedIndex ->
                                when (selectedIndex) {
                                    0 -> screen = Screens.ADD_SICKNESS
                                    2 -> screen = Screens.ADD_WEIGHT
                                }
                            }
                    )
                    Screens.ADD_SICKNESS -> AddSicknessPage(
                            stateLiveData = addViewModel.state,
                            isLoading = isLoading,
                            onDatePickerSelected = { selectedDate ->
                                showDatePicker(supportFragmentManager, selectedDate) { selection ->
                                    addViewModel.onDateSelected(selection)
                                }
                            },
                            onSaveClicked = {
                                addViewModel.onSaveClicked()
                            },
                            onExitClicked = {
                                screen = Screens.HOME
                            }
                    )
                    Screens.ADD_WEIGHT -> AddWeightPage(
                            stateLiveData = addWeightViewModel.state,
                            isLoading = isLoading,
                            onDatePickerSelected = { selectedDate ->
                                showDatePicker(supportFragmentManager, selectedDate) { selection ->
                                    addWeightViewModel.onDateSelected(selection)
                                }
                            },
                            onSaveClicked = { dogWeight ->
                                addWeightViewModel.onSaveClicked(dogWeight.myWeight, dogWeight.ourWeight)
                            },
                            onExitClicked = {
                                screen = Screens.HOME
                            }
                    )
                }
            }
        }
    }
}

enum class Screens {
    HOME, ADD_SICKNESS, ADD_WEIGHT
}

@ExperimentalAnimationApi
@Composable
fun HomeScreen(
        selectedIndex: Int,
        onPageChange: (Int) -> Unit,
        onFabClicked: (Int) -> Unit
) {
    Scaffold(
            bottomBar = { BottomNavigation(selectedIndex, onPageChange) },
            floatingActionButton = {
                AnimatedVisibility(visible = selectedIndex != 1) {
                    AddItemFab {
                        onFabClicked(selectedIndex)
                    }
                }
            }) { innerPadding ->
        when (selectedIndex) {
            0 -> SicknessScreen(innerPadding)
            1 -> StatisticsScreen(innerPadding)
            2 -> WeightScreen(innerPadding)
        }
    }
}

@Composable
fun AddItemFab(onFabClicked: () -> Unit) {
    FloatingActionButton(onClick = onFabClicked) {
        Image(imageVector = Icons.Filled.Add, contentDescription = "Add")
    }
}

@Composable
fun BottomNavigation(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    val listItems = listOf(
            stringResource(id = R.string.home) to Icons.Filled.Home,
            stringResource(id = R.string.statistics) to painterResource(id = R.drawable.ic_equalizer_black_24dp),
            stringResource(id = R.string.weight) to painterResource(id = R.drawable.ic_pets_black_24dp)
    )


    BottomNavigation {
        listItems.forEachIndexed { index, label ->
            BottomNavigationItem(
                    icon = {
                        when (val icon = label.second) {
                            is ImageVector -> Icon(imageVector = icon, contentDescription = label.first)
                            is Painter -> Icon(painter = icon, contentDescription = label.first)
                        }
                    },
                    label = { Text(text = label.first) },
                    selected = selectedIndex == index,
                    onClick = {
                        onItemSelected(index)
                    })
        }
    }
}

fun showDatePicker(fragmentManager: FragmentManager, selectedDate: Long, positiveButtonClickListener: (Long) -> Unit) {
    MaterialDatePicker.Builder.datePicker()
            .setSelection(selectedDate)
            .build().apply {
                addOnPositiveButtonClickListener { selection ->
                    positiveButtonClickListener(selection)
                }
            }
            .show(fragmentManager, null)
}