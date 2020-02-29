package com.joshafeinberg.oreotracker.add

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.joshafeinberg.oreotracker.R
import com.joshafeinberg.oreotracker.arch.events
import com.joshafeinberg.oreotracker.arch.state
import com.joshafeinberg.oreotracker.arch.util.observe
import com.joshafeinberg.oreotracker.databinding.ActivityAddBinding
import com.joshafeinberg.oreotracker.sharedmodule.Content
import com.joshafeinberg.oreotracker.sharedmodule.Time
import com.joshafeinberg.oreotracker.util.DateUtil
import com.joshafeinberg.oreotracker.util.readableName
import com.joshafeinberg.oreotracker.util.toFormattedDate
import com.joshafeinberg.oreotracker.util.viewBinding
import java.util.Calendar

class AddActivity : AppCompatActivity() {

    private val addViewModel: AddViewModel by viewModels()
    private val binding by viewBinding(ActivityAddBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupDatePicker()
        setupTimePicker()
        setupExactTimePicker()
        setupContentPicker()

        addViewModel.state.observe(this) { state ->
            hideLoading()
            handleStateChanges(state)
        }

        addViewModel.events.observe(this) { event ->
            hideLoading()
            when (event) {
                is AddEvents.Saving -> binding.layoutProgress.visibility = View.VISIBLE
                is AddEvents.ThrowUpSaved -> {
                    setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(EXTRA_THROW_UP, event.throwUp)
                    })
                    finish()
                }
                is AddEvents.ShowDatePicker -> showDatePicker(event.selectedDate)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.add_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == R.id.action_save) {
            addViewModel.onSaveClicked()
            true
        } else {
            false
        }
    }

    private fun handleStateChanges(state: AddViewState) {
        binding.textlayoutDate.editText?.setText(state.selectedDate.toFormattedDate(DateUtil.DATE_FORMAT))
        state.selectedTime?.let { time ->
            binding.textlayoutTime.editText?.setText(time::class.java.readableName)
            if (time is Time.ExactTime) {
                binding.textlayoutTimeExact.editText?.setText(time.exactTime.toFormattedDate(DateUtil.TIME_FORMAT))
            }
        }

        if (state.timeFieldVisible) {
            binding.textlayoutTimeExact.visibility = View.VISIBLE
            binding.textlayoutTime.editText?.setText(Time.ExactTime::class.java.readableName)
        } else {
            binding.textlayoutTimeExact.visibility = View.GONE
        }

        binding.textlayoutContent.editText?.setText(state.selectedContent?.readableName)
    }

    private fun setupDatePicker() {
        binding.textlayoutDate.editText?.apply {
            setText(System.currentTimeMillis().toFormattedDate(DateUtil.DATE_FORMAT))
            setOnClickListener {
                addViewModel.onDatePickerSelected()
            }
        }
    }

    private fun showDatePicker(selectedDate: Long) {
        MaterialDatePicker.Builder.datePicker()
            .setSelection(selectedDate)
            .build().apply {
                addOnPositiveButtonClickListener { selection ->
                    addViewModel.onDateSelected(selection)
                }
            }
            .show(supportFragmentManager, null)
    }

    private fun setupTimePicker() {
        val times = Time::class.sealedSubclasses.map { kClass -> kClass.java.readableName }

        val adapter = ArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            times
        )

        binding.textlayoutTime.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.textlayoutTimeExact.editText?.requestFocus()
            }
        }

        (binding.textlayoutTime.editText as AutoCompleteTextView).apply {
            setAdapter(adapter)
            setOnItemClickListener { _, _, position, _ ->
                addViewModel.onTimeSelected(Time::class.sealedSubclasses[position])
            }
            setOnClickListener {
                setText("")
            }
        }
    }

    private fun setupExactTimePicker() {
        binding.textlayoutTimeExact.editText?.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(this, { _, hourOfDay, minute ->
                val newDate = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                }
                addViewModel.onExactTimeSelected(newDate.timeInMillis)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
        }
    }

    private fun setupContentPicker() {
        val times = Content.values().map { it.readableName }

        val adapter = ArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            times
        )

        binding.textlayoutContent.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.textlayoutContent.editText?.requestFocus()
            }
        }

        (binding.textlayoutContent.editText as AutoCompleteTextView).apply {
            setAdapter(adapter)
            setOnItemClickListener { _, _, position, _ ->
                addViewModel.onContentSelected(Content.values()[position])
            }
            setOnClickListener {
                setText("")
            }
        }
    }

    private fun hideLoading() {
        binding.layoutProgress.visibility = View.GONE
    }

    companion object {
        const val EXTRA_THROW_UP = "throwup"
    }
}
