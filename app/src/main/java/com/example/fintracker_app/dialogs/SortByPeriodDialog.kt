package com.example.fintracker_app.dialogs

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.fintracker_app.R
import com.example.fintracker_app.components.DatePicker
import com.example.fintracker_app.screens.transactions.TransactionsListActivity
import java.util.*

class SortByPeriodDialog(private val activity: TransactionsListActivity): AppCompatDialogFragment() {

    private lateinit var editTextDateFrom: EditText;
    private lateinit var editTextDateTo: EditText;
    private lateinit var buttonDatePickerFrom: Button;
    private lateinit var buttonDatePickerTo: Button;
    private lateinit var buttonSort: Button;

    private lateinit var datePickerFrom: DatePicker;
    private lateinit var datePickerTo: DatePicker;

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity);
        val view = layoutInflater.inflate(R.layout.dialog_sort_by_period, null);
        findViews(view);

        buttonSort.setOnClickListener {
            val dateFrom = editTextDateFrom.text.toString();
            val dateTo = editTextDateTo.text.toString();
            activity.sortByPeriod(dateFrom, dateTo);
            dismiss();
        }

        builder.setView(view);
        return builder.create()
    }

    private fun findViews(view: View) {
        editTextDateFrom = view.findViewById(R.id.editTextDateFrom);
        editTextDateTo = view.findViewById(R.id.editTextDateTo);
        buttonDatePickerFrom = view.findViewById(R.id.buttonDatePickerFrom);
        buttonDatePickerTo = view.findViewById(R.id.buttonDatePickerTo);
        buttonSort = view.findViewById(R.id.buttonSort);

        datePickerFrom = DatePicker(activity, editTextDateFrom, buttonDatePickerFrom);
        datePickerTo = DatePicker(activity, editTextDateTo, buttonDatePickerTo);
    }
}