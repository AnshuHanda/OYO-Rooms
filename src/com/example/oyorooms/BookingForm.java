package com.example.oyorooms;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

public class BookingForm extends Activity {

private TextView mStartDate;
private TextView mEndDate;
private ImageButton mStartBtn;
private ImageButton mEndBtn;
int from_year, from_month, from_day, to_year, to_month, to_day;

static final int START_DATE_DIALOG_ID = 0;
static final int END_DATE_DIALOG_ID = 1;

static final int DATE_PICKER_TO = 0;
static final int DATE_PICKER_FROM = 1;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.booking);

    mStartDate = (TextView) findViewById(R.id.startdateDisplay);
    mStartBtn = (ImageButton) findViewById(R.id.startpickDate);

    mStartBtn.setOnClickListener(new View.OnClickListener() {
        @SuppressWarnings("deprecation")
        public void onClick(View v) {
            showDialog(DATE_PICKER_FROM);
        }
    });



    mEndDate = (TextView) findViewById(R.id.enddateDisplay);
    mEndBtn = (ImageButton) findViewById(R.id.endpickDate);

    /* add a click listener to the button */
    mEndBtn.setOnClickListener(new View.OnClickListener() {
        @SuppressWarnings("deprecation")
        public void onClick(View v) {
            showDialog(DATE_PICKER_TO);
        }
    });

    /* get the current date */
    final Calendar c = Calendar.getInstance();
    from_year = c.get(Calendar.YEAR);
    from_month = c.get(Calendar.MONTH);
    from_day = c.get(Calendar.DAY_OF_MONTH);
    to_year = c.get(Calendar.YEAR);
    to_month = c.get(Calendar.MONTH);
    to_day = c.get(Calendar.DAY_OF_MONTH);
     int minYear = from_year;//make a proper condition to change it the based on the child/infant 
    int minMonth =  from_month;
    int minDay =from_day;

    updateEndDisplay();
    updateStartDisplay();
}

private void updateEndDisplay() {
    mEndDate.setText(new StringBuilder()
            // Month is 0 based so add 1
            .append(to_month + 1).append("-").append(to_day).append("-")
            .append(to_year).append(" "));
}

private void updateStartDisplay() {
    mStartDate.setText(new StringBuilder()
            // Month is 0 based so add 1
            .append(from_month + 1).append("-").append(from_day)
            .append("-").append(from_year).append(" "));
}

private DatePickerDialog.OnDateSetListener from_dateListener = new DatePickerDialog.OnDateSetListener() {

    public void onDateSet(DatePicker view, int year, int monthOfYear,
            int dayOfMonth) {
        from_year = year;
        from_month = monthOfYear;
        from_day = dayOfMonth;
        updateStartDisplay();
    }
};
private DatePickerDialog.OnDateSetListener to_dateListener = new DatePickerDialog.OnDateSetListener() {

    public void onDateSet(DatePicker view, int year, int monthOfYear,
            int dayOfMonth) {
    	if (year > from_year ||monthOfYear > maxMonth && year == maxYear||
                dayOfMonth > maxDay && year == maxYear && monthOfYear == maxMonth){

           view.updateDate(maxYear, maxMonth, maxDay);
        //you can add toast message here for max year selection


       }
       else if (year < minYear ||monthOfYear < minMonth && year == minYear||
                dayOfMonth < minDay && year == minYear && monthOfYear == minMonth) {

           view.updateDate(minYear, minMonth, minDay);
   //you can add toast message here for min year selection

       }
       else {

           view.updateDate(year, monthOfYear, dayOfMonth);

       } 
    }
};

@Override
protected Dialog onCreateDialog(int id) {

    switch (id) {
    case DATE_PICKER_FROM:
        return new DatePickerDialog(this, from_dateListener, from_year,
                from_month, from_day);
    case DATE_PICKER_TO:
        return new DatePickerDialog(this, to_dateListener, to_year,
                to_month, to_day);
    }
    return null;
}
}