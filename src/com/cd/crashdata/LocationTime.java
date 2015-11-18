package com.cd.crashdata;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;

public class LocationTime extends Activity {
	public EditText txt_area_name,txt_landmark,txt_date_of_accident,txt_time_of_accident,txt_date_of_recording,txt_accident_ID;
	public ImageButton img_next,img_date_acc,img_time_acc,img_dat_rec; 
	private static final int ACCIDENT_DATE_DIALOG_ID = 999;
	private static final int ACCIDENT_DATE_REC_DIALOG_ID = 888;
	String accident_ID_generated;
	
	private int year;
	private int month;
	private int day;
	LocationTime _this;
	
	Spinner spin_region;
	String [] region;
	
	
	private int hour;
	private int minute;
 
	static final int TIME_DIALOG_ID = 777;
	private String area_name_1, landmark,date_of_accident,time_of_accident,time_of_record,region_name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("tag", "am at location 1");
		_this = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_time);
		/*
		 * receive values from a previous activity
		 */
		Log.i("tag", "am at location 2");
		
		region = getResources().getStringArray(R.array.regions_array);
		
		spin_region = (Spinner) findViewById(R.id.spin_region);
		
		ArrayAdapter<String> adapter_region = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, region);

		spin_region.setAdapter(adapter_region);
		
		txt_accident_ID=(EditText) findViewById(R.id.txt_acc_id);
		img_next = (ImageButton)findViewById(R.id.img_next);
		txt_area_name =(EditText)findViewById(R.id.txt_area_name);
		txt_landmark = (EditText)findViewById(R.id.txt_landmark);
		txt_date_of_accident=(EditText)findViewById(R.id.txt_date_of_accident);
		txt_time_of_accident = (EditText)findViewById(R.id.txt_time_acc);
		txt_date_of_recording = (EditText)findViewById(R.id.txt_date_rec);
		Log.i("tag", "am at location 3");
		
		
		img_time_acc = (ImageButton)findViewById(R.id.img_time_of_acc);
		img_dat_rec = (ImageButton)findViewById(R.id.img_date_of_rec);
		
		Log.i("tag", "am at location 4");
		accident_ID_generated="ACC_" + getDateTime() + "_" + randomNumber();
		Log.i("tag", "am at location 5");
		
		Intent i_r=getIntent();
		area_name_1 = i_r.getStringExtra("area_name");
		Log.i("tag", "am at location 6");
		txt_area_name.setText(area_name_1);
		Log.i("tag", "am at location 7");
		txt_accident_ID.setText(accident_ID_generated);
		
		setCurrentDateOnView();
		setCurrentDateOnView_2();
		addListenerImageDateOfAccidentButton();
		addListenerImageDateOfAccidentButton_2();
		Log.i("tag", "am at location 4");
		setAccidentTime();
		setListenerToNextButton();
	}
	
	public void setListenerToNextButton(){
		
		img_next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String area_name=txt_area_name.getText().toString().trim();
				landmark=txt_landmark.getText().toString().trim();
				date_of_accident=txt_date_of_accident.getText().toString().trim();
				time_of_accident=txt_time_of_accident.getText().toString().trim();
				time_of_record=txt_date_of_recording.getText().toString().trim();
				region_name = spin_region.getSelectedItem().toString();
				
				//store accident ID in shared preference to be retrieved later
				if(checkAccidentIDPref()){
					//no preference exists, so create it and add a value
					addVehicleCountToPref(accident_ID_generated);
				}else{
					
					//update the value in that prference
					updateVehicleCountPreference(accident_ID_generated);
				}
				
				Intent i_f = getIntent();

				Intent i = new Intent(_this, AccidentDetails.class);
				i.putExtra("accident_ID", accident_ID_generated);
				i.putExtra("area_name", area_name);
				i.putExtra("landmark", landmark);
				i.putExtra("date_of_accident", date_of_accident);
				i.putExtra("time_of_accident", time_of_accident);
				i.putExtra("time_of_record", time_of_record);
				i.putExtra("region_name", region_name);
				i.putExtra("lat", i_f.getStringExtra("lat"));
				i.putExtra("lng", i_f.getStringExtra("lng"));
				
				_this.startActivity(i);
				
			}
		});
	}
	
	public boolean checkAccidentIDPref() {
		SharedPreferences sp = getSharedPreferences(Utilities.ACCIDENT_ID, 1);
		Log.e("Registration", "preference checking starting....2......");
		String acc_id = sp.getString("accident_id", "noValue");
		Log.e("Registration", "preference checking starting....3......");
		if (acc_id.equals("noValue")) {
			Log.e("Registration", "preference checking starting.4.........");
			//preference exist
			return false;
		} else {
			//preference do not exist
			return true;
		}
		
	}

	public void addVehicleCountToPref(String acc_id) {
		SharedPreferences sp = getSharedPreferences(Utilities.ACCIDENT_ID, 1);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("accident_id", acc_id);
		editor.commit();
	}
	/*
	 * update the value in the accidenet ID shared prefe
	 */
	public void updateVehicleCountPreference(String acc_id) {
		Log.e("status", "updating shared preference to false after logout");
		SharedPreferences sp = getSharedPreferences(Utilities.ACCIDENT_ID, 1);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("accident_id", acc_id);
		editor.commit();
	}
	
	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	private String randomNumber(){
		Random randomGenerator = new Random();
		int random_number_= 10+randomGenerator.nextInt(90);
		String random_number = String.valueOf(random_number_);
		return random_number;
	}
	public void setAccidentTime(){
		
		img_time_acc.setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	            Calendar mcurrentTime = Calendar.getInstance();
	            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
	            int minute = mcurrentTime.get(Calendar.MINUTE);
	            TimePickerDialog mTimePicker;
	            mTimePicker = new TimePickerDialog(LocationTime.this, new TimePickerDialog.OnTimeSetListener() {
	                @Override
	                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
	                	txt_time_of_accident.setText( selectedHour + ":" + selectedMinute);
	                }
	            }, hour, minute, true);//Yes 24 hour time
	            mTimePicker.setTitle("Select Time");
	            mTimePicker.show();

	        }
	    });
		
		
	}

	private void addListenerImageDateOfAccidentButton() {
		// TODO Auto-generated method stub
		img_date_acc = (ImageButton)findViewById(R.id.img_date_acc);
		img_date_acc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(ACCIDENT_DATE_DIALOG_ID);

			}
		});

	}
	
	private void addListenerImageDateOfAccidentButton_2() {
		// TODO Auto-generated method stub
		img_dat_rec = (ImageButton)findViewById(R.id.img_date_of_rec);
		img_dat_rec.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(ACCIDENT_DATE_REC_DIALOG_ID);

			}
		});

	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case ACCIDENT_DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year, month,day);
			
		case ACCIDENT_DATE_REC_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year, month,day);
		}
		return null;
	}
	
	private void setCurrentDateOnView() {
		// TODO Auto-generated method stub

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		// set current date into EditText
		txt_date_of_accident.setText(new StringBuilder().append(month + 1).append("-")
				.append(day).append("-").append(year).append(" "));

	}
	
	private void setCurrentDateOnView_2() {
		// TODO Auto-generated method stub

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		// set current date into EditText
		txt_date_of_recording.setText(new StringBuilder().append(month + 1).append("-")
				.append(day).append("-").append(year).append(" "));

	}
	
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// set selected date into textview
			// tvDisplayDate = (TextView) findViewById(R.id.tvDate);
			txt_date_of_accident.setText(new StringBuilder().append(month + 1)
					.append("-").append(day).append("-").append(year)
					.append(" "));

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_location_time, menu);
		return true;
	}

}
