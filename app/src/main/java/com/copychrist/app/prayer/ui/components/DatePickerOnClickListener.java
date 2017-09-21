package com.copychrist.app.prayer.ui.components;

import android.app.DatePickerDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by jim on 8/22/17.
 */

public class DatePickerOnClickListener implements View.OnClickListener {
    private SimpleDateFormat dateFormat;

    public DatePickerOnClickListener(SimpleDateFormat dateFormat) {
        super();
        this.dateFormat = dateFormat;
    }

    @Override
    public void onClick(View v) {
        final EditText editText = (EditText) v;
        final Calendar calendarSelection = Calendar.getInstance();
        Calendar calendarToday = Calendar.getInstance();
        int calYear;
        int calMonth;
        int calDay;

        String currentDate = editText.getText().toString();

        if(!TextUtils.isEmpty(currentDate)) {
            try {
                calendarSelection.setTime(dateFormat.parse(currentDate));
                calYear = calendarSelection.get(Calendar.YEAR);
                calMonth = calendarSelection.get(Calendar.MONTH);
                calDay = calendarSelection.get(Calendar.DAY_OF_MONTH);
            } catch (ParseException e) {
                calYear = calendarToday.get(Calendar.YEAR);
                calMonth = calendarToday.get(Calendar.MONTH);
                calDay = calendarToday.get(Calendar.DAY_OF_MONTH);
            }
        } else {
            calYear = calendarToday.get(Calendar.YEAR);
            calMonth = calendarToday.get(Calendar.MONTH);
            calDay = calendarToday.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog (
                editText.getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker,
                                          int selectedYear,
                                          int selectedMonth,
                                          int selectedDay) {
                        Calendar selection = Calendar.getInstance();
                        selection.set(selectedYear, selectedMonth, selectedDay);
                        editText.setText(dateFormat.format(selection.getTime()));
                    }
                },
                calYear, calMonth, calDay);

        datePickerDialog.getDatePicker().setMinDate(calendarToday.getTimeInMillis());
        datePickerDialog.show();
    }
}
