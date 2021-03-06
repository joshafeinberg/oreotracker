package com.joshafeinberg.oreotracker.weight.add

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.joshafeinberg.oreotracker.R
import com.joshafeinberg.oreotracker.arch.events
import com.joshafeinberg.oreotracker.arch.state
import com.joshafeinberg.oreotracker.arch.util.observe
import com.joshafeinberg.oreotracker.databinding.ActivityAddWeightBinding
import com.joshafeinberg.oreotracker.util.DateUtil
import com.joshafeinberg.oreotracker.util.toFormattedDate
import com.joshafeinberg.oreotracker.util.viewBinding

class AddWeightActivity : AppCompatActivity() {

    private val addWeightViewModel: AddWeightViewModel by viewModels()
    private val binding by viewBinding(ActivityAddWeightBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupDatePicker()

        addWeightViewModel.state.observe(this) { state ->
            hideLoading()
            binding.textlayoutDate.editText?.setText(state.selectedDate.toFormattedDate(DateUtil.DATE_FORMAT))
        }

        addWeightViewModel.events.observe(this) { event ->
            hideLoading()
            when (event) {
                is AddWeightEvents.Saving -> binding.layoutProgress.visibility = View.VISIBLE
                is AddWeightEvents.WeightSaved -> {
                    setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(EXTRA_WEIGHT, event.weight)
                    })
                    finish()
                }
                is AddWeightEvents.ShowDatePicker -> showDatePicker(event.selectedDate)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == R.id.action_save) {
            addWeightViewModel.onSaveClicked(
                binding.textlayoutMyWeight.editText?.text?.toString(),
                binding.textlayoutOurWeight.editText?.text?.toString()
            )
            true
        } else {
            false
        }
    }

    private fun setupDatePicker() {
        binding.textlayoutDate.editText?.apply {
            setText(System.currentTimeMillis().toFormattedDate(DateUtil.DATE_FORMAT))
            setOnClickListener {
                addWeightViewModel.onDatePickerSelected()
            }
        }
    }

    private fun showDatePicker(selectedDate: Long) {
        MaterialDatePicker.Builder.datePicker()
            .setSelection(selectedDate)
            .build().apply {
                addOnPositiveButtonClickListener { selection ->
                    addWeightViewModel.onDateSelected(selection)
                }
            }
            .show(supportFragmentManager, null)
    }

    private fun hideLoading() {
        binding.layoutProgress.visibility = View.GONE
    }

    companion object {
        const val EXTRA_WEIGHT = "weight"
    }
}
