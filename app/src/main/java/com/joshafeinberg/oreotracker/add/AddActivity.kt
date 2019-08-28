package com.joshafeinberg.oreotracker.add

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
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
import com.google.android.material.textfield.TextInputLayout
import com.joshafeinberg.oreotracker.R
import com.joshafeinberg.oreotracker.arch.util.observe
import com.joshafeinberg.oreotracker.sharedmodule.Content
import com.joshafeinberg.oreotracker.sharedmodule.Time
import com.joshafeinberg.oreotracker.util.DateUtil
import com.joshafeinberg.oreotracker.util.readableName
import com.joshafeinberg.oreotracker.util.toFormattedDate
import java.util.*


class AddActivity : AppCompatActivity() {

    private val addViewModel: AddViewModel by viewModels()
    private lateinit var textLayoutDate: TextInputLayout
    private lateinit var textLayoutTime: TextInputLayout
    private lateinit var textLayoutExactTime: TextInputLayout
    private lateinit var textLayoutContent: TextInputLayout
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textLayoutDate = findViewById(R.id.textlayout_date)
        setupDatePicker()
        
        textLayoutTime = findViewById(R.id.textlayout_time)
        setupTimePicker()

        textLayoutExactTime = findViewById(R.id.textlayout_time_exact)
        setupExactTimePicker()

        textLayoutContent = findViewById(R.id.textlayout_content)
        setupContentPicker()

        addViewModel.state.observe(this) { state ->
            hideLoading()
            handleStateChanges(state)
        }

        addViewModel.events.observe(this) { event ->
            hideLoading()
            when (event) {
                is AddViewModel.AddViewEvents.Saving -> {
                    progressDialog = ProgressDialog(this)
                    progressDialog?.show()
                }
                is AddViewModel.AddViewEvents.ThrowUpSaved -> {
                    setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(EXTRA_THROW_UP, event.throwUp)
                    })
                    finish()
                }
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

    private fun handleStateChanges(state: AddViewModel.AddViewState) {
        textLayoutDate.editText?.setText(state.selectedDate.toFormattedDate(DateUtil.DATE_FORMAT))
        state.selectedTime?.let { time ->
            textLayoutTime.editText?.setText(time::class.java.readableName)
            if (time is Time.ExactTime) {
                textLayoutExactTime.editText?.setText(time.exactTime.toFormattedDate(DateUtil.TIME_FORMAT))
            }
        }

        if (state.timeFieldVisible) {
            textLayoutExactTime.visibility = View.VISIBLE
            textLayoutTime.editText?.setText(Time.ExactTime::class.java.readableName)
        } else {
            textLayoutExactTime.visibility = View.GONE
        }

        textLayoutContent.editText?.setText(state.selectedContent?.readableName)
    }

    private fun setupDatePicker() {
        textLayoutDate.editText?.setText(System.currentTimeMillis().toFormattedDate(DateUtil.DATE_FORMAT))
        textLayoutDate.editText?.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
                val newDate = Calendar.getInstance()
                newDate.set(year, monthOfYear, dayOfMonth)
                addViewModel.onDateSelected(newDate.timeInMillis)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }
    
    private fun setupTimePicker() {
        val times = Time::class.sealedSubclasses.map { kClass -> kClass.java.readableName }

        val adapter = ArrayAdapter(
                this,
                R.layout.dropdown_menu_popup_item,
                times
        )

        textLayoutTime.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                textLayoutTime.editText?.requestFocus()
            }
        }

        (textLayoutTime.editText as AutoCompleteTextView).apply {
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
        textLayoutExactTime.editText?.setOnClickListener {
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

        textLayoutContent.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                textLayoutContent.editText?.requestFocus()
            }
        }

        (textLayoutContent.editText as AutoCompleteTextView).apply {
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
        progressDialog?.hide()
        progressDialog = null
    }

    companion object {
        const val EXTRA_THROW_UP = "throwup"
    }
}