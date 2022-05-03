package com.example.fintracker_app.components

import android.app.DatePickerDialog
import android.content.Context
import android.widget.Button
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class DatePicker(val context: Context, val tvDatePicker: TextView, val btnDatePicker: Button) {

    init {
        val myCalendar = Calendar.getInstance();

        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(myCalendar);
        }

        btnDatePicker.setOnClickListener {
            DatePickerDialog(context, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    private fun updateLabel(myCalendar: Calendar) {
        val myFormat = "dd-MM-yyyy";
        val sdf = SimpleDateFormat(myFormat, Locale.UK);
        tvDatePicker.setText(sdf.format(myCalendar.time));
    }
}