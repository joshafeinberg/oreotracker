package com.joshafeinberg.oreotracker.weight.add

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.joshafeinberg.oreotracker.R
import com.joshafeinberg.oreotracker.arch.util.observe
import com.joshafeinberg.oreotracker.util.DateUtil
import com.joshafeinberg.oreotracker.util.toFormattedDate
import java.util.Calendar

class AddWeightActivity : AppCompatActivity(R.layout.activity_add_weight) {

    private val addWeightViewModel: AddWeightViewModel by viewModels()
    private lateinit var textLayoutDate: TextInputLayout
    private lateinit var textMyWeight: TextInputLayout
    private lateinit var textOurWeight: TextInputLayout
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textLayoutDate = findViewById(R.id.textlayout_date)
        setupDatePicker()

        textMyWeight = findViewById(R.id.textlayout_my_weight)
        textOurWeight = findViewById(R.id.textlayout_our_weight)

        addWeightViewModel.state.observe(this) { state ->
            hideLoading()
            textLayoutDate.editText?.setText(state.selectedDate.toFormattedDate(DateUtil.DATE_FORMAT))
        }

        addWeightViewModel.events.observe(this) { event ->
            hideLoading()
            when (event) {
                is AddWeightViewModel.AddViewEvents.Saving -> {
                    progressDialog = ProgressDialog(this)
                    progressDialog?.show()
                }
                is AddWeightViewModel.AddViewEvents.WeightSaved -> {
                    setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(EXTRA_WEIGHT, event.weight)
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
            addWeightViewModel.onSaveClicked(textMyWeight.editText?.text?.toString(), textOurWeight.editText?.text?.toString())
            true
        } else {
            false
        }
    }

    private fun setupDatePicker() {
        textLayoutDate.editText?.setText(System.currentTimeMillis().toFormattedDate(DateUtil.DATE_FORMAT))
        textLayoutDate.editText?.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
                val newDate = Calendar.getInstance()
                newDate.set(year, monthOfYear, dayOfMonth)
                addWeightViewModel.onDateSelected(newDate.timeInMillis)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun hideLoading() {
        progressDialog?.hide()
        progressDialog = null
    }

    companion object {
        const val EXTRA_WEIGHT = "weight"
    }
}