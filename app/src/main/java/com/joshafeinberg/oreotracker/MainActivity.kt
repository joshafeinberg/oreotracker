package com.joshafeinberg.oreotracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.joshafeinberg.oreotracker.add.AddActivity
import com.joshafeinberg.oreotracker.home.HomeFragment
import com.joshafeinberg.oreotracker.home.HomeViewModel
import com.joshafeinberg.oreotracker.sharedmodule.ThrowUp
import com.joshafeinberg.oreotracker.stats.StatsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

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
                else -> false
            }
        }

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivityForResult(intent, REQUEST_ADD)
        }

        showFragment(HomeFragment())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_ADD && resultCode == Activity.RESULT_OK) {
            homeViewModel.addItem(data?.getSerializableExtra(AddActivity.EXTRA_THROW_UP) as ThrowUp)
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.framelayout, fragment).commit()
    }

    companion object {
        private const val REQUEST_ADD = 1127
    }
}
