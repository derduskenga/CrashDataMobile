package com.cd.crashdata;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class VehicleDriverOne extends Activity {
	VehicleDriverOne _this;
	String[] vehicle;
	String[] loading;
	Spinner spin_vehicle, spin_loading;
	EditText txt_vehicle_reg_no, txt_vehicle_type;
	String vehicle_reg_no, vehicle_type, loading_status,DB_vehicle_type,DB_vehicle_model="", DB_registered_to="";
	ImageButton img_next, img_vehicle_type;
	CheckBox chk_bad_side_mirror, chk_bad_tire, chk_defective_lights,
			chk_hard_to_say, chk_general_road_unworthy;
	ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		_this = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vehicle_driver_one);

		// initialize Edit texts
		txt_vehicle_reg_no = (EditText) findViewById(R.id.txt_reg_no);
		txt_vehicle_type = (EditText) findViewById(R.id.txt_veh_type);
		img_next = (ImageButton) findViewById(R.id.img_next);
		img_vehicle_type = (ImageButton)findViewById(R.id.img_veh_type);
		// initialize checkboxes
		chk_bad_side_mirror = (CheckBox) findViewById(R.id.check_side_mirrors);
		chk_bad_tire = (CheckBox) findViewById(R.id.check_bad_tire);
		chk_defective_lights = (CheckBox) findViewById(R.id.check_defective_lights);
		chk_general_road_unworthy = (CheckBox) findViewById(R.id.check_general);
		chk_hard_to_say = (CheckBox) findViewById(R.id.check_hard_to_say);

		// Spinner Vehicle
		// vehicle = getResources().getStringArray(R.array.vehicle_array);
		int vehicle_count_from_pref = readFromPreference();
		String vehicle_string = "";

		while (vehicle_count_from_pref > 0) {
			vehicle_string = vehicle_string + "Vehicle "
					+ vehicle_count_from_pref + ",";
			vehicle_count_from_pref--;
		}

		vehicle = generateArray(vehicle_string, ",");

		spin_vehicle = (Spinner) findViewById(R.id.spin_vehicle);

		ArrayAdapter<String> adapter_vehicle = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, vehicle);

		spin_vehicle.setAdapter(adapter_vehicle);

		spin_vehicle.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int index = arg0.getSelectedItemPosition();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// Spinner Loading
		loading = getResources().getStringArray(R.array.loading_array);

		spin_loading = (Spinner) findViewById(R.id.spin_loading);

		ArrayAdapter<String> adapter_loading = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, loading);

		spin_loading.setAdapter(adapter_loading);

		spin_loading.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int index = arg0.getSelectedItemPosition();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		Intent irV= getIntent();
		
		if (irV!=null){
			
			String v_type = irV.getStringExtra("vehicle_type");
			txt_vehicle_type.setText(v_type);
		}
		
		addLostFocusToTxt_vehicle_reg_no();
		addListenerToNextImgButton();
		addListenerToImgVehicleType();
	}
	
	public void addListenerToImgVehicleType(){
		img_vehicle_type.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(_this, VehicleTypeList.class);
				startActivity(i);
				
			}
		});
		
	}
	
	public void addLostFocusToTxt_vehicle_reg_no(){
		txt_vehicle_reg_no.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus){
					
					fetchDetails(txt_vehicle_reg_no.getText().toString().trim());
				}
			}
		});

	}

	public void fetchDetails(final String vReg){
		
		if(isOnline()){
			
			// servercode comes here
			Thread trd = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.e("Registration",
							"gone to connectin class with url starting...1");
					Looper.prepare();
					
					final String url = Utilities.VEHICLE_DETAILS_URL;

					Log.e("Registration", "gone to connectin class with url "
							+ url);

					HttpResponse response = null;
					// code to do the HTTP request
					InputStream is = null;
					// String result;
					Log.d("res", "in addListenerToButton func 5");
					// Creating HTTP client
					final HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams
							.setConnectionTimeout(httpParams, 13000);
					Log.d("res", "in addListenerToButton func 6");

					HttpClient httpClient = new DefaultHttpClient(httpParams);
					Log.d("res", "in addListenerToButton func 7");

					// Creating HTTP Post
					HttpPost httpPost = new HttpPost(url);

					Log.d("res", "in addListenerToButton func 8");
					List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(
							1);
					Log.d("res", "in addListenerToButton func 7");
					
					
					nameValuePair.add(new BasicNameValuePair("vReg", vReg));
					

					Log.d("res", "in addListenerToButton func 9");
					try {
						httpPost.setEntity(new UrlEncodedFormEntity(
								nameValuePair));
					} catch (UnsupportedEncodingException e) {
						// writing error to Log
						e.printStackTrace();
					}

					try {
						response = httpClient.execute(httpPost);

						HttpEntity entity = response.getEntity();

						is = entity.getContent();

						// writing response to log
						Log.d("res", "1 " + response.toString());

					} catch (ConnectTimeoutException ctEx) {

						Toast.makeText(getApplicationContext(),
								"Server not responding", Toast.LENGTH_LONG)
								.show();
					} catch (Exception e) {
						// writing exception to log
						Log.d("res", "2 " + e.toString());
						// e.printStackTrace();
					}

					try {
						
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(is, "iso-8859-1"), 8);
						StringBuilder sb = new StringBuilder();
						String line = null;
						while ((line = reader.readLine()) != null) {
							
							sb.append(line + "\n");
							
						}
						
						is.close();

						final String result = sb.toString();

						Log.d("res", "4 " + result);
	
								// changes to gui can be done on the UI thread
						
						if(!result.equals("null")){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
										String[] output = generateArray(result, ",");
										txt_vehicle_type.setText(output[0]);
										DB_vehicle_model = output[1];
										DB_registered_to = output[2];
										Log.d("res", "in function populate 111111");
								}
							});
						}
						
								
						Log.d("res", "in function populate 1");

					} catch (Exception e) {
						Log.e("log_tag",
								"Error converting result " + e.toString());
					}
					//Looper.loop();

				}

			});

			trd.start();
			
		}else{
			
			String dialogTitle = getResources().getString(
					R.string.no_internet_connection);
			String dialogBody = getResources().getString(
					R.string.enable_data_or_leave_airplane_mode);
			String negativeBtnLabel = getResources().getString(R.string.cancel);
			String positiveBtnLabel = getResources().getString(
					R.string.settings);
			final String settingArea = Settings.ACTION_AIRPLANE_MODE_SETTINGS;
			promptUserToCreateGoogleAccount(dialogTitle, dialogBody,
					negativeBtnLabel, positiveBtnLabel, settingArea);
			
		}
		
	}
	
	public void addListenerToNextImgButton() {

		img_next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent ir = getIntent();
				// data to be submitted before moving to next activity
				String road_signs = ir.getStringExtra("road_signs");
				String road_surface = ir.getStringExtra("road_surface");
				String other_sign = ir.getStringExtra("other_sign");
				// Accident_ID will be stored in a shared preference for easier
				// retrival
				String accident_ID = ir.getStringExtra("accident_ID");
				String area_name = ir.getStringExtra("area_name");
				String landmark = ir.getStringExtra("landmark");
				String date_of_accident = ir.getStringExtra("date_of_accident");
				String time_of_accident = ir.getStringExtra("time_of_accident");
				String time_of_record = ir.getStringExtra("time_of_record");
				String number_of_vehicles = ir
						.getStringExtra("number_of_vehicles");
				String hit_n_run = ir.getStringExtra("hit_n_run");
				String severity = ir.getStringExtra("severity");
				String pre_crush_event = ir.getStringExtra("pre_crush_event");
				String crash_configuration = ir
						.getStringExtra("crash_configuration");
				String weather_conditions = ir
						.getStringExtra("weather_conditions");
				String illumination = ir.getStringExtra("illumination");
				String region_name = ir.getStringExtra("region_name");
				String lat = ir.getStringExtra("lat");
				String lng = ir.getStringExtra("lng");
				String jobNumber = getJobNumber();
				// end of data to be submitted before moving to next activity.

			
				

				// This data will move to the next activity
				String vehicle_reg_number = txt_vehicle_reg_no.getText().toString().trim().toUpperCase();
				String vehicle_type = txt_vehicle_type.getText().toString()
						.trim();
				String loading = spin_loading.getSelectedItem().toString();
				String vehicle_defect = checkDefects();
				

				updateVehicleCountPreference();
				
				if(vehicle_reg_number.equals("")){
					Toast.makeText(_this, "Vehicle registration cannot be empty", Toast.LENGTH_LONG).show();
				}else if (vehicle_type.equals("")){
					Toast.makeText(_this, "Vehicle type cannot be empty", Toast.LENGTH_LONG).show();
					
				}else{
					try{
						
						uploadAccidentDetails(road_signs, road_surface, other_sign,
								accident_ID, area_name, landmark, date_of_accident,
								time_of_accident, time_of_record, number_of_vehicles,
								hit_n_run, severity, pre_crush_event,
								crash_configuration, weather_conditions, illumination,
								region_name,lat,lng,jobNumber);
						//sleep a bit
						Thread.sleep(4000);

						Intent i = new Intent(_this, VehicleDriverTwo.class);
						i.putExtra("vehicle_reg_number", vehicle_reg_number);
						i.putExtra("vehicle_type", vehicle_type);
						i.putExtra("loading", loading);
						i.putExtra("vehicle_defect", vehicle_defect);
						i.putExtra("DB_vehicle_model", DB_vehicle_model);
						i.putExtra("DB_registered_to", DB_registered_to);

						_this.startActivity(i);
					}catch (InterruptedException e) {

			            e.printStackTrace();
			        }
					
				}
			}
		});
	}
	
	public String getJobNumber(){
		
		SharedPreferences sp = getSharedPreferences(Utilities.JOB_NO, 1);
		String jobNo = sp.getString("job_no", "");
		return jobNo;
	}

	public void uploadAccidentDetails(final String road_signs,
			final String road_surface, final String other_sign,
			final String accident_ID, final String area_name,
			final String landmark, final String date_of_accident,
			final String time_of_accident, final String date_of_record,
			final String number_of_vehicles, final String hit_n_run,
			final String severity, final String pre_crush_event,
			final String crash_configuration, final String weather_conditions,
			final String illumination, final String region_name,
			final String lat, final String lng, final String jno) {
		if (isOnline()) {

			// servercode comes here
			Thread trd = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Looper.prepare();
					String url = Utilities.ACCIDENT_DETAILS;

					Log.e("Registration", "gone to connectin class with url "
							+ url);

					HttpResponse response = null;
					// code to do the HTTP request
					InputStream is = null;
					String result;
					Log.d("res", "in addListenerToButton func 5");
					// Creating HTTP client
					final HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams
							.setConnectionTimeout(httpParams, 4000);
					Log.d("res", "in addListenerToButton func 6");

					HttpClient httpClient = new DefaultHttpClient(httpParams);
					Log.d("res", "in addListenerToButton func 7");

					// Creating HTTP Post
					HttpPost httpPost = new HttpPost(url);

					Log.d("res", "in addListenerToButton func 8");
					List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(
							17);
					Log.d("res", "in addListenerToButton func 7");
					
					nameValuePair.add(new BasicNameValuePair("tag",
							"acc_det"));
					nameValuePair.add(new BasicNameValuePair("road_signs",
							road_signs));
					nameValuePair.add(new BasicNameValuePair("road_surface",
							road_surface));
					nameValuePair.add(new BasicNameValuePair("other_sign",
							other_sign));
					nameValuePair.add(new BasicNameValuePair("accident_ID",
							accident_ID));
					nameValuePair.add(new BasicNameValuePair("area_name",
							area_name));
					nameValuePair.add(new BasicNameValuePair("landmark",
							landmark));
					nameValuePair.add(new BasicNameValuePair(
							"date_of_accident", date_of_accident));
					nameValuePair.add(new BasicNameValuePair(
							"time_of_accident", time_of_accident));
					nameValuePair.add(new BasicNameValuePair("date_of_record",
							date_of_record));
					nameValuePair.add(new BasicNameValuePair(
							"number_of_vehicles", number_of_vehicles));
					nameValuePair.add(new BasicNameValuePair("hit_n_run",
							hit_n_run));
					nameValuePair.add(new BasicNameValuePair("severity",
							severity));
					nameValuePair.add(new BasicNameValuePair("pre_crush_event",
							pre_crush_event));
					nameValuePair.add(new BasicNameValuePair(
							"crash_configuration", crash_configuration));
					nameValuePair.add(new BasicNameValuePair(
							"weather_conditions", weather_conditions));
					nameValuePair.add(new BasicNameValuePair("illumination",
							illumination));
					nameValuePair.add(new BasicNameValuePair("region_name",
							region_name));
					
					nameValuePair.add(new BasicNameValuePair("lat",
							lat));
					nameValuePair.add(new BasicNameValuePair("lng",
							lng));
					
					nameValuePair.add(new BasicNameValuePair("jno",
							jno));

					Log.d("res", "in addListenerToButton func 9");
					try {
						httpPost.setEntity(new UrlEncodedFormEntity(
								nameValuePair));
					} catch (UnsupportedEncodingException e) {
						// writing error to Log
						e.printStackTrace();
					}

					try {
						response = httpClient.execute(httpPost);

						HttpEntity entity = response.getEntity();

						is = entity.getContent();

						// writing response to log
						Log.d("res", "1 " + response.toString());

					} catch (ConnectTimeoutException ctEx) {

						Toast.makeText(getApplicationContext(),
								"Server not responding", Toast.LENGTH_LONG)
								.show();
					} catch (Exception e) {
						// writing exception to log
						Log.d("res", "2 " + e.toString());
						// e.printStackTrace();
					}

					try {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(is, "iso-8859-1"), 8);
						StringBuilder sb = new StringBuilder();
						String line = null;
						while ((line = reader.readLine()) != null) {
							sb.append(line + "\n");
						}
						is.close();

						result = sb.toString();

						pDialog.dismiss();

						Toast.makeText(_this, result, Toast.LENGTH_LONG).show();

						Log.d("res", "4" + result);
						// call the method that will return a
						// value

					} catch (Exception e) {
						Log.e("log_tag",
								"Error converting result " + e.toString());
					}
					Looper.loop();

				}

			});

			trd.start();
			openDialog();

		} else {
			String dialogTitle = getResources().getString(
					R.string.no_internet_connection);
			String dialogBody = getResources().getString(
					R.string.enable_data_or_leave_airplane_mode);
			String negativeBtnLabel = getResources().getString(R.string.cancel);
			String positiveBtnLabel = getResources().getString(
					R.string.settings);
			final String settingArea = Settings.ACTION_AIRPLANE_MODE_SETTINGS;
			promptUserToCreateGoogleAccount(dialogTitle, dialogBody,
					negativeBtnLabel, positiveBtnLabel, settingArea);
		}

	}

	public void promptUserToCreateGoogleAccount(String dialogTitle,
			String dialogBody, String negativeBtnLabel,
			String positiveBtnLabel, final String settingArea) {
		// TODO Auto-generated method stub
		// setResult(account);
		AlertDialog.Builder settingsAlert = new AlertDialog.Builder(_this);

		// Setting Dialog Title
		settingsAlert.setTitle(dialogTitle);
		// Setting Dialog Message
		settingsAlert.setMessage(dialogBody);
		// On pressing Settings button
		settingsAlert.setPositiveButton(positiveBtnLabel,
				new DialogInterface.OnClickListener() {
					// On pressing Settings button
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(settingArea);

						_this.startActivity(intent);

					}
				});

		// on pressing cancel button
		settingsAlert.setNegativeButton(negativeBtnLabel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		settingsAlert.show();

	}

	public boolean isOnline() {

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnectedOrConnecting()) {

			return true;
		}
		return false;
	}

	public void openDialog() {
		// Open the dialog
		pDialog = new ProgressDialog(VehicleDriverOne.this);
		pDialog.setMessage(Html
				.fromHtml("<b>Please wait...</b><br/>Saving accident details"));
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
		closeHandler.sendEmptyMessageDelayed(0, 1000);
		// show toast
	}

	private Handler closeHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (pDialog != null)
				pDialog.dismiss();

		}
	};

	/*
	 * return a string of what has been selected by te user in the vehicle
	 * defecs checkboxes
	 */

	public String checkDefects() {
		String defects = "";

		if (chk_bad_side_mirror.isChecked()) {
			defects += chk_bad_side_mirror.getText().toString()+ ",";
		}
		if (chk_bad_tire.isSelected()) {
			defects += chk_bad_tire.getText().toString()+ ",";
		}
		if (chk_defective_lights.isSelected()) {
			defects += chk_defective_lights.getText().toString()+ ",";
		}
		if (chk_general_road_unworthy.isSelected()) {
			defects += chk_general_road_unworthy.getText().toString()+ ",";
		}
		if (chk_hard_to_say.isSelected()) {
			defects += chk_hard_to_say.getText().toString()+ ",";
		}
		if (!chk_bad_side_mirror.isSelected() && !chk_bad_tire.isSelected()
				&& !chk_defective_lights.isSelected()
				&& !chk_general_road_unworthy.isSelected()
				&& !chk_hard_to_say.isSelected()) {
			defects = "None";
		}
		return defects;
	}

	/*
	 * Reading a value from a shared preference
	 */
	public int readFromPreference() {
		SharedPreferences sp = getSharedPreferences(Utilities.VEHICLE_COUNT, 1);
		int vehicle_count = Integer.parseInt(sp.getString("vehicle_count", ""));
		return vehicle_count;
	}

	/*
	 * update the vehicle status by subtrating one each time
	 */
	public void updateVehicleCountPreference() {
		Log.e("status", "updating shared preference to false after logout");
		SharedPreferences sp = getSharedPreferences(Utilities.VEHICLE_COUNT, 1);
		// subtract one from the vehicle count
		String newValue = String.valueOf(readFromPreference() - 1);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("vehicle_count", newValue);
		editor.commit();
	}

	public String[] generateArray(String original, String separator) {

		Vector<String> nodes = new Vector<String>();

		// Parse nodes into vector
		int index = original.indexOf(separator);
		while (index >= 0) {
			nodes.addElement(original.substring(0, index));
			original = original.substring(index + separator.length());
			index = original.indexOf(separator);
		}
		// Get the last node
		nodes.addElement(original);

		// Create splitted string array
		String[] result = new String[nodes.size() - 1];

		if (nodes.size() > 0) {
			for (int loop = 0; loop < nodes.size() - 1; loop++) {
				result[loop] = nodes.elementAt(loop);
				// System.out.println(result[loop]); - uncomment this line to
				// see the result on output console
			}
		}

		return result;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_vehicle_driver_one, menu);
		return true;
	}

}
