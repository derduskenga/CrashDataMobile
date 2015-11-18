package com.cd.crashdata;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AccidentDetails extends Activity {

	String[] yes_no;
	String[] severity;
	String[] crash;
	String[] pre_crash;
	String[] weather;
	String[] light;
	AccidentDetails _this;
	String number_of_vehicles, hit_n_run,severity_of_acc, pre_crush_event,crash_conf, weather_cond, illumination;
	int total_images[] = { R.drawable.headon, R.drawable.sideswipe,
			R.drawable.frontrear, R.drawable.frontside, R.drawable.pedestrian,
			R.drawable.rollover, R.drawable.fire, R.drawable.object,
			R.drawable.na };
	private ImageButton image_next;
	private EditText txt_vehicle_count;
	private Spinner spin_hit_run, spin_severity, spin_crash, spin_pre_crash,
			spin_weather, spin_light;
	GetGPSLocation gps;
	ProgressDialog pDialog;
	String accident_ID_generated = "ACC_";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		_this = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accident_details);

		image_next = (ImageButton) findViewById(R.id.img_next);
		addListenerToButton();
		// Spinner hit and run
		yes_no = getResources().getStringArray(R.array.yes_no_array);

		spin_hit_run = (Spinner) findViewById(R.id.spin_hit_run);
		
		txt_vehicle_count = (EditText) findViewById(R.id.txt_no_vehicles);

		/*
		 * this function retrieves coordinates and uses it to retrieve places
		 * name and a landmarks name
		 */

		ArrayAdapter<String> adapter_yes_no = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, yes_no);

		spin_hit_run.setAdapter(adapter_yes_no);

		spin_hit_run.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int index = arg0.getSelectedItemPosition();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// Spinner Accident severity
		severity = getResources().getStringArray(R.array.severity_array);

		spin_severity = (Spinner) findViewById(R.id.spin_severity);

		ArrayAdapter<String> adapter_severity = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, severity);

		spin_severity.setAdapter(adapter_severity);

		spin_severity.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int index = arg0.getSelectedItemPosition();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		crash = getResources().getStringArray(R.array.crash_array);
		spin_crash = (Spinner) findViewById(R.id.spin_crash);
		spin_crash.setAdapter(new MyAdapter(this, R.layout.custom_spinner,
				crash));

		// Spinner pre-crash event
		pre_crash = getResources().getStringArray(R.array.pre_crash_array);

		spin_pre_crash = (Spinner) findViewById(R.id.spin_pre_crash);

		ArrayAdapter<String> adapter_pre_crash = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, pre_crash);

		spin_pre_crash.setAdapter(adapter_pre_crash);

		spin_pre_crash.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int index = arg0.getSelectedItemPosition();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// Spinner weather
		weather = getResources().getStringArray(R.array.weather_array);

		spin_weather = (Spinner) findViewById(R.id.spin_weather);

		ArrayAdapter<String> adapter_weather = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, weather);

		spin_weather.setAdapter(adapter_weather);

		spin_weather.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int index = arg0.getSelectedItemPosition();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// Spinner light conditions / illumination
		light = getResources().getStringArray(R.array.light_array);

		spin_light = (Spinner) findViewById(R.id.spin_light);

		ArrayAdapter<String> adapter_light = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, light);

		spin_light.setAdapter(adapter_light);

		spin_light.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int index = arg0.getSelectedItemPosition();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}


	private Handler closeHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (pDialog != null)
				pDialog.dismiss();
		}
	};

	public void openDialog() {
		// Open the dialog
		pDialog = new ProgressDialog(AccidentDetails.this);
		pDialog.setMessage(Html.fromHtml("<b>Loading Places...</b>"));
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();

		// Close it after 2 seconds
		closeHandler.sendEmptyMessageDelayed(0, 12000);
	}

	public void addListenerToButton() {

		image_next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				number_of_vehicles = txt_vehicle_count.getText().toString()
						.trim();
				 hit_n_run = spin_hit_run.getSelectedItem().toString()
						.trim();
				 severity_of_acc = spin_severity.getSelectedItem().toString()
						.trim();
				 pre_crush_event = spin_crash.getSelectedItem()
						.toString().trim();
				 crash_conf = spin_crash.getSelectedItem()
						.toString().trim();
				 weather_cond = spin_weather.getSelectedItem()
						.toString().trim();
				 illumination = spin_light.getSelectedItem().toString()
						.trim();
				 if(number_of_vehicles.equals("")){
					 
					 Toast.makeText(_this, "Number vehicles canno be empty", Toast.LENGTH_LONG).show();
				 }else{
					 
					 
					 Intent i_r = getIntent();

					// Toast.makeText(_this,"crash configuration is " +
					// crash_configuration,Toast.LENGTH_LONG).show();

					Intent i = new Intent(_this, AccidentDetailsCont.class);

				//	i.putExtra("accident_ID", accident_ID);
					i.putExtra("number_of_vehicles", number_of_vehicles);
					i.putExtra("hit_n_run", hit_n_run);
					i.putExtra("severity", severity_of_acc);
					i.putExtra("pre_crush_event", pre_crush_event);
					i.putExtra("crash_configuration", crash_conf);
					i.putExtra("weather_conditions", weather_cond);
					i.putExtra("illumination", illumination);
					
					i.putExtra("accident_ID", i_r.getStringExtra("accident_ID"));
					i.putExtra("area_name", i_r.getStringExtra("area_name"));
					i.putExtra("landmark", i_r.getStringExtra("landmark"));
					i.putExtra("date_of_accident", i_r.getStringExtra("date_of_accident"));
					i.putExtra("time_of_accident", i_r.getStringExtra("time_of_accident"));
					i.putExtra("time_of_record", i_r.getStringExtra("time_of_record"));
					i.putExtra("region_name", i_r.getStringExtra("region_name"));
					i.putExtra("lat", i_r.getStringExtra("lat"));
					i.putExtra("lng", i_r.getStringExtra("lng"));
					
					_this.startActivity(i);
					 
				 }


			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_accident_details, menu);
		return true;
	}

	public class MyAdapter extends ArrayAdapter<String> {

		public MyAdapter(Context ctx, int txtViewResourceId, String[] objects) {
			super(ctx, txtViewResourceId, objects);
		}

		@Override
		public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
			return getCustomView(position, cnvtView, prnt);
		}

		@Override
		public View getView(int pos, View cnvtView, ViewGroup prnt) {
			return getCustomView(pos, cnvtView, prnt);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View mySpinner = inflater.inflate(R.layout.custom_spinner, parent,
					false);
			TextView main_text = (TextView) mySpinner
					.findViewById(R.id.text_main_seen);
			main_text.setText(crash[position]);

			ImageView left_icon = (ImageView) mySpinner
					.findViewById(R.id.left_pic);
			left_icon.setImageResource(total_images[position]);

			return mySpinner;
		}
	}

}
