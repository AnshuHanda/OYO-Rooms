package com.example.oyorooms;

import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("NewApi")
public class BookingActivity extends Activity implements OnClickListener {
	 private RadioGroup radioSexGroup;
	 private RadioButton radioSexButton;
	 private EditText Fname;
	 private EditText Lname;
	 private TextView hotelname;
	 
	 private EditText mDateDisplay;
	 private EditText endDateDisplay;
	 private ImageButton mPickDate;
	 private ImageButton endPickDate;
	 int from_year, from_month, from_day,to_year, to_month, to_day;
	 static final int START_DATE_DIALOG_ID=0;
	 static final int END_DATE_DIALOG_ID=0; 
				  
	protected void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
              setContentView(R.layout.booking);
              TextView hotelName = (TextView)findViewById(R.id.hotel_name);
      		hotelName.setText(getSharedPreferences("MYPREFS", 0).getString("name", ""));
              Fname = (EditText) findViewById(R.id.EditTextName); //post api
      		  Lname = (EditText) findViewById(R.id.EditTextName1); // post api
      		  
      		/*  capture our View elements for the start date function   */
      	    mDateDisplay = (EditText) findViewById(R.id.startdateDisplay);
      	    mPickDate = (ImageButton) findViewById(R.id.startpickDate);

      	    /* add a click listener to the button   */
      	  mPickDate.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {
                  showDialog(START_DATE_DIALOG_ID);
              }
          });
      	  
      	final Calendar c = Calendar.getInstance();
        from_year = c.get(Calendar.YEAR);
        String str=Integer.toString(from_year);
        Log.e("from_year",str);
        from_month = c.get(Calendar.MONTH);
        from_day = c.get(Calendar.DAY_OF_MONTH);

        /* display the current date (this method is below)  */
        updateStartDisplay();


     /* capture our View elements for the end date function */
        endDateDisplay = (EditText) findViewById(R.id.enddateDisplay);
        endPickDate = (ImageButton) findViewById(R.id.endpickDate);

        /* add a click listener to the button   */
        endPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(END_DATE_DIALOG_ID);
            }
        });
        
        final Calendar c1 = Calendar.getInstance();
        to_year = c1.get(Calendar.YEAR);
        to_month = c1.get(Calendar.MONTH);
        to_day = c1.get(Calendar.DAY_OF_MONTH);

        /* display the current date (this method is below)  */
        updateEndDisplay();
      	

              Button btnSubmit = (Button)findViewById(R.id.submitbutton);
              btnSubmit.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      /*
                      Validation class will check the error and display the error on respective fields
                      but it won't resist the form submission, so we need to check again before submit
                       */
                      if ( checkValidation () )
                          submitForm();
                      else
                          Toast.makeText(BookingActivity.this, "Fill the fields correct", Toast.LENGTH_LONG).show();
                  }
              });
              
           
	}
	public void onClick(View v) {
		  showDialog(0);
		 }

		// @Override
		 @Deprecated
		// protected Dialog onCreateDialog(int id) {
		//	 DatePickerDialog dialog = new DatePickerDialog(this, datePickerListener, year, month, day);
		//	    dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
		//	    return dialog;
		// }
		 

		// private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		//  public void onDateSet(DatePicker view, int selectedYear,
		//    int selectedMonth, int selectedDay) {
		//   et.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
		 //    + selectedYear);
		//  }
		// };
		 
		// public void addListenerOnButton1() {
			 
		//		radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
			
			 
			 // }
		
		 private void updateEndDisplay() {
			    endDateDisplay.setText(
			            new StringBuilder()
			                // Month is 0 based so add 1
			                .append(from_month + 1).append("-")
			                .append(from_day).append("-")
			                .append(from_year).append(" "));

			}



			private void updateStartDisplay() {
			    mDateDisplay.setText(
			            new StringBuilder()
			                // Month is 0 based so add 1
			                .append(to_month + 1).append("-")
			                .append(to_day).append("-")
			                .append(to_year).append(" "));


			}
			
			private DatePickerDialog.OnDateSetListener from_dateListener =
		            new DatePickerDialog.OnDateSetListener() {

		            public void onDateSet(DatePicker view, int year, 
		                                  int monthOfYear, int dayOfMonth) {
		                
		                from_year = year;
		                from_month = monthOfYear;
		                from_day = dayOfMonth;
		                updateStartDisplay();
		            }
		        };
		        
		        int DATE_PICKER_TO = 0;
		        int DATE_PICKER_FROM = 1;

		        @Override
		        protected Dialog onCreateDialog(int id) {

		            switch(id){
		                case 1: //setMinDate(System.currentTimeMillis() - 1000);
		                        return new DatePickerDialog(this, from_dateListener, from_year, from_month, from_day); 
		                    case 0:
		                        return new DatePickerDialog(this, to_dateListener, to_year, to_month, to_day);
		            }
		                return null;
		        }
		      
		private DatePickerDialog.OnDateSetListener to_dateListener =
		        new DatePickerDialog.OnDateSetListener() {

		            public void onDateSet(DatePicker view, int year, 
		                                  int monthOfYear, int dayOfMonth) {
		            	to_year = year;
		                to_month = monthOfYear;
		                to_day = dayOfMonth;
		                updateStartDisplay();
		            }
		        };

		/*protected Dialog onCreateDialog1(int id) {
		    switch (id) {
		    case END_DATE_DIALOG_ID:
		        return new DatePickerDialog(this,
		                    endDateSetListener,
		                    from_year, from_month, from_day);
		    }
		    return null;
		}*/
		 
		
			private boolean isValidFirst(EditText First) {
				
				if (Fname.getText().toString().length() <= 0) {
					Fname.setError("Accept Alphabets Only.");
					return false;
				} else if (!Fname.getText().toString().matches("[a-zA-Z ]+")) {
					Fname.setError("Accept Alphabets Only.");
					return false;
					
				} else {
					return true;
					
				}

			}
			
			// validating last name
			
			private boolean isValidLast(EditText Lname) {
				
				if (Lname.getText().toString().length() <= 0) {
					Lname.setError("Accept Alphabets Only.");
					return false;
				} else if (!Lname.getText().toString().matches("[a-zA-Z ]+")) {
					Lname.setError("Accept Alphabets Only.");
					return false;
					
				} else {
					return true;
					
				}

			}
			private boolean isValidDate(EditText et) {
				if (et.getText().toString().length() <= 0)
				{
					et.setError("ENTER DATE");
					return false;
				}
				else
					return true;
			}
			
			
			 private void submitForm() {
			        // Submit your form here. your form is valid
			        Toast.makeText(this, "Submitting form...", Toast.LENGTH_LONG).show();
			    }
			 
			private boolean checkValidation() {
		        boolean ret = true;
		 
		        if (!isValidFirst(Fname)) ret = false;
		        if (!isValidLast(Lname)) ret = false;
		        if (!isValidDate(mDateDisplay)) ret = false;
		        if (!isValidDate(endDateDisplay)) ret = false;
		        
		 
		        return ret;
		    }
		 
			
		 
		 
}
	
	
	