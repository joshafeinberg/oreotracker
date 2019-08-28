package com.joshafeinberg.oreotracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.joshafeinberg.oreotracker.add.AddActivity
import com.joshafeinberg.oreotracker.home.HomeFragment
import com.joshafeinberg.oreotracker.home.HomeViewModel
import com.joshafeinberg.oreotracker.sharedmodule.ThrowUp
import com.joshafeinberg.oreotracker.sharedmodule.Weight
import com.joshafeinberg.oreotracker.stats.StatsFragment
import com.joshafeinberg.oreotracker.weight.WeightFragment
import com.joshafeinberg.oreotracker.weight.WeightViewModel
import com.joshafeinberg.oreotracker.weight.add.AddWeightActivity

class MainActivity : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val weightViewModel: WeightViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    showFragment(HomeFragment())
                    true
                }
                R.id.action_stats -> {
                    showFragment(StatsFragment())
                    true
                }
                R.id.action_weight -> {
                    showFragment(WeightFragment())
                    true
                }
                else -> false
            }
        }

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            if (supportFragmentManager.findFragmentByTag(HomeFragment::class.java.toString()) != null) {
                val intent = Intent(this, AddActivity::class.java)
                startActivityForResult(intent, REQUEST_ADD)
            } else if (supportFragmentManager.findFragmentByTag(WeightFragment::class.java.toString()) != null) {
                val intent = Intent(this, AddWeightActivity::class.java)
                startActivityForResult(intent, REQUEST_ADD_WEIGHT)
            }
        }

        showFragment(HomeFragment())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_ADD && resultCode == Activity.RESULT_OK) {
            homeViewModel.addItem(data?.getSerializableExtra(AddActivity.EXTRA_THROW_UP) as ThrowUp)
        } else if (requestCode == REQUEST_ADD_WEIGHT && resultCode == Activity.RESULT_OK) {
            weightViewModel.addWeight(data?.getSerializableExtra(AddWeightActivity.EXTRA_WEIGHT) as Weight)
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.framelayout, fragment, fragment::class.java.toString()).commit()
    }

    companion object {
        private const val REQUEST_ADD = 1127
        private const val REQUEST_ADD_WEIGHT = 33
    }
}
