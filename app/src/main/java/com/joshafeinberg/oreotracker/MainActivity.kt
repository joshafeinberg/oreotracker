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
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
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
            var isLoading by remember { mutableStateOf(false) }

            addViewModel.events.observe(this) { event ->
                when (event) {
                    AddEvents.Saving -> isLoading = true
                    is AddEvents.ThrowUpSaved -> {
                        isLoading = false
                        homeViewModel.addItem(event.throwUp)
                        //screen = Screens.HOME
                    }
                }
            }

            addWeightViewModel.events.observe(this) { event ->
                when (event) {
                    AddWeightEvents.Saving -> isLoading = true
                    is AddWeightEvents.WeightSaved -> {
                        isLoading = false
                        weightViewModel.addWeight(event.weight)
                        //screen = Screens.HOME
                    }
                }
            }

            val navController = rememberNavController()

            MdcTheme {
                NavHost(navController = navController, startDestination = Screen.Home.route) {
                    composable(Screen.Home.route) {
                        HomeScreen { selectedRoute ->
                            val newRoute = when (selectedRoute) {
                                BottomNavScreen.Home.route -> Screen.AddSickness.route
                                BottomNavScreen.Weight.route -> Screen.AddWeight.route
                                else -> return@HomeScreen
                            }
                            navController.navigate(newRoute)
                        }
                    }
                    composable(Screen.AddSickness.route) {
                        AddSicknessPage(
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
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(Screen.AddWeight.route) {
                        AddWeightPage(
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
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun HomeScreen(
        onFabClicked: (String?) -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)

    Scaffold(
            bottomBar = { BottomNavigation(navController, currentRoute) },
            floatingActionButton = {
                AnimatedVisibility(visible = currentRoute != BottomNavScreen.Statistics.route) {
                    AddItemFab {
                        onFabClicked(currentRoute)
                    }
                }
            }) { innerPadding ->

        NavHost(navController, startDestination = BottomNavScreen.Home.route) {
            composable(BottomNavScreen.Home.route) { SicknessScreen(innerPadding) }
            composable(BottomNavScreen.Statistics.route) { StatisticsScreen(innerPadding) }
            composable(BottomNavScreen.Weight.route) { WeightScreen(innerPadding) }
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
fun BottomNavigation(navController: NavHostController, currentRoute: String?) {
    BottomNavigation {
        bottomNavScreens.forEach { screen ->
            val screenName = stringResource(id = screen.resourceId)
            BottomNavigationItem(
                    icon = {
                        when (val icon = screen.image) {
                            is ImageVector -> Icon(imageVector = icon, contentDescription = screenName)
                            is Int -> Icon(painter = painterResource(id = icon), contentDescription = screenName)
                        }
                    },
                    label = { Text(text = screenName) },
                    selected = currentRoute == screen.route,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo = navController.graph.startDestination
                            launchSingleTop = true
                        }
                    }
            )
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