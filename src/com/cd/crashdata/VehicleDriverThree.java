package com.cd.crashdata;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class VehicleDriverThree extends Activity {
	VehicleDriverThree _this;
	ProgressDialog pDialog;
	EditText txt_no_of_deaths, txt_no_of_serious_injuries,
			txt_no_of_minor_injuries, txt_driver_name, txt_driver_licens_no,
			txt_discrepancy;
	ImageButton img_next;
	RadioGroup rg_driver_dead, rg_driver_injured, rg_alcohol_test,
			rg_driver_gender;
	RadioButton rb_driver_dead, rb_driver_injured, rb_alcohol_test, rb_gender;
	String deaths, s_injuries, m_injuries, driver_name, d_license_no,
			discrepancy, driver_dead_yes_no, driver_s_injured_yes_no,
			alcohol_test, driver_gender,         
			
			insurance_expired,insurance_name,
			registered_to,vehicle_model,vehicle_insurance_policy_no,
			other_insurance,vehicle_reg_number,vehicle_type,loading,vehicle_defect;
	TextView txtv_driver_injured;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		_this = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vehicle_driver_three);
		// initialize all the controls
		txt_no_of_deaths = (EditText) findViewById(R.id.txt_persons_dead);
		txt_no_of_serious_injuries = (EditText) findViewById(R.id.txt_persons_ser_injured);
		txt_no_of_minor_injuries = (EditText) findViewById(R.id.txt_persons_minor_injuries);
		txt_driver_name = (EditText) findViewById(R.id.txt_driver_name);
		txt_driver_licens_no = (EditText) findViewById(R.id.txt_driver_license_no);
		txt_discrepancy = (EditText) findViewById(R.id.txt_discrepancy);
		img_next = (ImageButton) findViewById(R.id.img_next);
		rg_alcohol_test = (RadioGroup) findViewById(R.id.rg_alcohol_test);
		rg_driver_dead = (RadioGroup) findViewById(R.id.rg_driver_dead);
		rg_driver_injured = (RadioGroup) findViewById(R.id.rg_driver_injured);
		rg_driver_gender = (RadioGroup) findViewById(R.id.rg_driver_gender);
		txtv_driver_injured = (TextView) findViewById(R.id.txtview_driver_injured);
		addListenerToNextImageButton();
		addListenerToDriverDeadRadioGroup();
		addTextChangedListenerToTxt_driver_licens_no();
	}
	
	public void addTextChangedListenerToTxt_driver_licens_no(){
		
		txt_driver_licens_no.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus){
					
					fetchDriverName(txt_driver_licens_no.getText().toString().trim());
				}
			}
		});
	}
	
	
	public void fetchDriverName(final String license_number){
		if(isOnline()){
			
			// servercode comes here
			Thread trd = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.e("Registration",
							"gone to connectin class with url starting...1");
					Looper.prepare();
					
					final String url = Utilities.DRIVER_DETAILS_URL;

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
					
					
					nameValuePair.add(new BasicNameValuePair("license_number", license_number));
					

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
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
											txt_driver_name.setText(result);
										
									}
								});
						Log.d("res", "in function populate 1");

					} catch (Exception e) {
						Log.e("log_tag",
								"Error converting result " + e.toString());
					}
					Looper.loop();

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
	
	public void addListenerToNextImageButton() {
		img_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent ir = getIntent();
				deaths = txt_no_of_deaths.getText().toString().trim();
				s_injuries = txt_no_of_serious_injuries.getText()
						.toString().trim();
				m_injuries = txt_no_of_minor_injuries.getText().toString()
						.trim();
				driver_name = txt_driver_name.getText().toString().trim();
				d_license_no = txt_driver_licens_no.getText().toString()
						.trim();
				discrepancy = txt_discrepancy.getText().toString().trim();
				driver_dead_yes_no = getDriverDeadOption();
				alcohol_test = getAlcoholTestOption();
				driver_gender = getDriverGenderOption();
				
				insurance_expired = ir.getStringExtra("insurance_expired");
				insurance_name= ir.getStringExtra("insurance_name");
				registered_to= ir.getStringExtra("registered_to");
				vehicle_model= ir.getStringExtra("vehicle_model");
				vehicle_insurance_policy_no= ir.getStringExtra("vehicle_insurance_policy_no");
				other_insurance= ir.getStringExtra("other_insurance");
				vehicle_reg_number= ir.getStringExtra("vehicle_reg_number");
				vehicle_type= ir.getStringExtra("vehicle_type");
				loading= ir.getStringExtra("loading");
				vehicle_defect= ir.getStringExtra("vehicle_defect");

				if (rg_driver_injured.isShown()) {
					driver_s_injured_yes_no = getDriverInjuredOption();
				} else {
					driver_s_injured_yes_no = "Driver dead";
				}
				
				if(deaths.equals("")){
					Toast.makeText(_this, "Number of deaths cannot be empty. Use 0 if none", Toast.LENGTH_LONG).show();
				}else if(s_injuries.equals("")){
					Toast.makeText(_this, "Number of serious injuries cannot be empty. Use 0 if none", Toast.LENGTH_LONG).show();
				}else if(m_injuries.equals("")){
					Toast.makeText(_this, "Number of minor injuries cannot be empty. Use 0 if none", Toast.LENGTH_LONG).show();
				}else if(d_license_no.equals("")){
					Toast.makeText(_this, "Driver licence cannot be empty.", Toast.LENGTH_LONG).show();
				}else{
					
					
					if (readFromPreference() < 1) {
					
						/*
						 * Call the Http method to send this data to server
						 */
						uploadVehicleDriverDetails(deaths, s_injuries, m_injuries,
								driver_name, d_license_no, discrepancy,
								driver_dead_yes_no, alcohol_test, driver_gender,
								driver_s_injured_yes_no,insurance_expired,insurance_name,
								registered_to,vehicle_model,vehicle_insurance_policy_no,
								other_insurance,vehicle_reg_number,vehicle_type,loading,vehicle_defect);
						Intent i_f = new Intent(_this, Witnesses.class);
						_this.startActivity(i_f);
					} else {
						
						uploadVehicleDriverDetails(deaths, s_injuries, m_injuries,
								driver_name, d_license_no, discrepancy,
								driver_dead_yes_no, alcohol_test, driver_gender,
								driver_s_injured_yes_no,insurance_expired,insurance_name,
								registered_to,vehicle_model,vehicle_insurance_policy_no,
								other_insurance,vehicle_reg_number,vehicle_type,loading,vehicle_defect);
						
						Intent i_b = new Intent(_this, VehicleDriverOne.class);
						_this.startActivity(i_b);
					}
					
				}


			}
		});
	}

	public void uploadVehicleDriverDetails(final String deaths,
			final String s_injuries, final String m_injuries,
			final String driver_name, final String d_license_no,
			final String discrepancy, final String driver_dead_yes_no,
			final String alcohol_test, final String driver_gender,
			final String driver_s_injured_yes_no,final String insurance_expired,
			final String insurance_name,
			final String registered_to,final String vehicle_model,
			final String vehicle_insurance_policy_no,
			final String other_insurance,final String vehicle_reg_number,
			final String vehicle_type,final String loading,
			final String vehicle_defect) {

		if (isOnline()) {

			// servercode comes here
			Thread trd = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Looper.prepare();
					String url = Utilities.DRIVER_VEHICLE_URL;

					Log.e("Registration", "gone to connectin class with url "
							+ url);

					HttpResponse response = null;
					// code to do the HTTP request
					InputStream is = null;
					String result;
					Log.d("res", "in addListenerToButton func 5");
					// Creating HTTP client
					final HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParams, 4000);
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
							"dri_veh"));
					
					nameValuePair.add(new BasicNameValuePair("accident_ID",
							getAccidentID()));
					nameValuePair.add(new BasicNameValuePair("deaths",
							deaths));
					nameValuePair.add(new BasicNameValuePair("s_injuries",
							s_injuries));
					nameValuePair.add(new BasicNameValuePair("m_injuries",
							m_injuries));
					nameValuePair.add(new BasicNameValuePair("driver_name",
							driver_name));
					nameValuePair.add(new BasicNameValuePair("d_license_no",
							d_license_no));
					nameValuePair.add(new BasicNameValuePair("discrepancy",
							discrepancy));
					nameValuePair.add(new BasicNameValuePair("driver_dead_yes_no",
							driver_dead_yes_no));
					nameValuePair.add(new BasicNameValuePair("alcohol_test", 
							alcohol_test));
					nameValuePair.add(new BasicNameValuePair("driver_gender",
							driver_gender));
					nameValuePair.add(new BasicNameValuePair("driver_s_injured_yes_no",
							driver_s_injured_yes_no));
					
					nameValuePair.add(new BasicNameValuePair("insurance_expired",
							insurance_expired));
					nameValuePair.add(new BasicNameValuePair("insurance_name",
							insurance_name));
					nameValuePair.add(new BasicNameValuePair("registered_to",
							registered_to));
					nameValuePair.add(new BasicNameValuePair("vehicle_model",
							vehicle_model));
					nameValuePair.add(new BasicNameValuePair("vehicle_insurance_policy_no",
							vehicle_insurance_policy_no));
					nameValuePair.add(new BasicNameValuePair("other_insurance",
							other_insurance));
					nameValuePair.add(new BasicNameValuePair("vehicle_reg_number",
							vehicle_reg_number));
					nameValuePair.add(new BasicNameValuePair("vehicle_type",
							vehicle_type));
					nameValuePair.add(new BasicNameValuePair("loading",
							loading));
					nameValuePair.add(new BasicNameValuePair("vehicle_defect",
							vehicle_defect));
					

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
		pDialog = new ProgressDialog(VehicleDriverThree.this);
		pDialog.setMessage(Html
				.fromHtml("<b>Please wait...</b><br/>Saving driver/vehicle details"));
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

	public String getAccidentID() {
		SharedPreferences sp = getSharedPreferences(Utilities.ACCIDENT_ID, 1);
		String accidentID = sp.getString("accident_id", "");
		return accidentID;
	}
	

	public void addListenerToDriverDeadRadioGroup() {
		rg_driver_dead.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						// TODO Auto-generated method stub
						if (getDriverDeadOption().equals("yes")) {
							// disable radion group driver injured
							txtv_driver_injured.setVisibility(View.GONE);
							rg_driver_injured.setEnabled(false);
							rg_driver_injured.setVisibility(View.GONE);
						}
						if (getDriverDeadOption().equals("no")) {
							txtv_driver_injured.setVisibility(View.VISIBLE);
							rg_driver_injured.setEnabled(true);
							rg_driver_injured.setVisibility(View.VISIBLE);
						}

					}
				});
	}
	

	public String getAlcoholTestOption() {

		int selectedRB = rg_alcohol_test.getCheckedRadioButtonId();

		rb_alcohol_test = (RadioButton) findViewById(selectedRB);

		String yes_no = rb_alcohol_test.getText().toString();

		return yes_no;
	}

	public String getDriverGenderOption() {

		int selectedRB = rg_driver_gender.getCheckedRadioButtonId();

		rb_gender = (RadioButton) findViewById(selectedRB);

		String male_female = rb_gender.getText().toString();

		return male_female;
	}

	public String getDriverInjuredOption() {

		int selectedRB = rg_driver_injured.getCheckedRadioButtonId();

		rb_driver_injured = (RadioButton) findViewById(selectedRB);

		String yes_no = rb_driver_injured.getText().toString();

		return yes_no;
	}

	public String getDriverDeadOption() {

		int selectedRB = rg_driver_dead.getCheckedRadioButtonId();

		rb_driver_dead = (RadioButton) findViewById(selectedRB);

		String yes_no = rb_driver_dead.getText().toString();

		return yes_no;
	}

	private int readFromPreference() {
		SharedPreferences sp = getSharedPreferences(Utilities.VEHICLE_COUNT, 1);
		int vehicle_count = Integer.parseInt(sp.getString("vehicle_count", ""));
		return vehicle_count;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_vehicle_driver_three, menu);
		return true;
	}

}
