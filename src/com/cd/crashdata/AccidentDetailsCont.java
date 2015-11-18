package com.cd.crashdata;



import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class AccidentDetailsCont extends Activity {
	AccidentDetailsCont _this;
	Spinner spin_road_surface, spin_road_signs;
	String[] road_surface;
	String[] road_signs;
	ImageButton img_next;
	EditText txt_other;

	int total_images[] = { R.drawable.speedlimit, R.drawable.steepdescent,
			R.drawable.giveway, R.drawable.noovertaking, R.drawable.sharpbend,
			R.drawable.roadnarrowsahead, R.drawable.other,
			R.drawable.roadsigndamaged, R.drawable.noroadsign };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		_this = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accident_details_cont);

		img_next = (ImageButton) findViewById(R.id.img_next);
		txt_other = (EditText) findViewById(R.id.txt_other);

		// Spinner road surface
		road_surface = getResources()
				.getStringArray(R.array.road_surface_array);

		spin_road_surface = (Spinner) findViewById(R.id.spin_road_surface);

		ArrayAdapter<String> adapter_surface = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, road_surface);

		spin_road_surface.setAdapter(adapter_surface);

		spin_road_surface
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						int index = arg0.getSelectedItemPosition();

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		// Spinner road surface
		/*
		 * road_signs = getResources().getStringArray(R.array.road_signs_array);
		 * 
		 * Spinner spin_road_signs = (Spinner)
		 * findViewById(R.id.spin_road_signs);
		 * 
		 * ArrayAdapter<String> adapter_signs = new ArrayAdapter<String>(this,
		 * android.R.layout.simple_spinner_item, road_signs);
		 * 
		 * spin_road_signs.setAdapter(adapter_signs);
		 * 
		 * spin_road_signs.setOnItemSelectedListener(new
		 * OnItemSelectedListener() {
		 * 
		 * @Override public void onItemSelected(AdapterView<?> arg0, View arg1,
		 * int arg2, long arg3) { int index = arg0.getSelectedItemPosition();
		 * 
		 * }
		 * 
		 * @Override public void onNothingSelected(AdapterView<?> arg0) {} });
		 */
		road_signs = getResources().getStringArray(R.array.road_signs_array);
		spin_road_signs = (Spinner) findViewById(R.id.spin_road_signs);
		spin_road_signs.setAdapter(new MyAdapter(this, R.layout.custom_spinner,
				road_signs));
		addListenerToNextButton();
	}

	public void addListenerToNextButton() {
		img_next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String road_signs = spin_road_signs.getSelectedItem()
						.toString();
				String road_surface = spin_road_surface.getSelectedItem()
						.toString();
				String other_sign = txt_other.getText().toString().trim();

				Intent i_r = getIntent();

				Intent i = new Intent(_this, VehicleDriverOne.class);
				i.putExtra("road_signs", road_signs);
				i.putExtra("road_surface", road_surface);
				i.putExtra("other_sign", other_sign);
				
				i.putExtra("accident_ID", i_r.getStringExtra("accident_ID"));
				i.putExtra("area_name", i_r.getStringExtra("area_name"));
				i.putExtra("landmark", i_r.getStringExtra("landmark"));
				i.putExtra("date_of_accident",
						i_r.getStringExtra("date_of_accident"));
				i.putExtra("time_of_accident",
						i_r.getStringExtra("time_of_accident"));
				i.putExtra("time_of_record",
						i_r.getStringExtra("time_of_record"));
				i.putExtra("number_of_vehicles",
						i_r.getStringExtra("number_of_vehicles"));
				i.putExtra("hit_n_run", i_r.getStringExtra("hit_n_run"));
				i.putExtra("severity", i_r.getStringExtra("severity"));
				i.putExtra("pre_crush_event",
						i_r.getStringExtra("pre_crush_event"));
				i.putExtra("crash_configuration",
						i_r.getStringExtra("crash_configuration"));
				i.putExtra("weather_conditions",
						i_r.getStringExtra("weather_conditions"));
				i.putExtra("illumination", i_r.getStringExtra("illumination"));
				i.putExtra("region_name", i_r.getStringExtra("region_name"));
				i.putExtra("lat", i_r.getStringExtra("lat"));
				i.putExtra("lng", i_r.getStringExtra("lng"));
				
				
				//create a shared preference and store the number of vehicles int it.
				addVehicleCountToPref(i_r.getStringExtra("number_of_vehicles"));
				

				_this.startActivity(i);

			}
		});
	}

	public boolean checkVehicleCountPref() {
		SharedPreferences sp = getSharedPreferences(Utilities.VEHICLE_COUNT, 1);
		Log.e("Registration", "preference checking starting....2......");
		String vehicle_count = sp.getString("vehicle_count", "noValue");
		Log.e("Registration", "preference checking starting....3......");
		if (vehicle_count.equals("noValue")) {
			Log.e("Registration", "preference checking starting.4.........");
			//preference exist
			return false;
		} else {
			//preference do not exist
			return true;
		}
		
	}

	public void addVehicleCountToPref(String vehicle_count) {
		SharedPreferences sp = getSharedPreferences(Utilities.VEHICLE_COUNT, 1);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("vehicle_count", vehicle_count);
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_accident_details_cont, menu);
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
			main_text.setText(road_signs[position]);

			ImageView left_icon = (ImageView) mySpinner
					.findViewById(R.id.left_pic);
			left_icon.setImageResource(total_images[position]);

			return mySpinner;
		}
	}

}
