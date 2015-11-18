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
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.RecognizerIntent;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class PoliceRemarks extends Activity {
	PoliceRemarks _this;
	EditText txt_cause_code, txt_police_remarks;
	RadioGroup rg_input_method;
	RadioButton rb_type_voice;
	ImageButton img_next, img_reload_mic,img_cause_code;
	ProgressDialog pDialog;
	protected static final int RESULT_SPEECH = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		_this = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_police_remarks);
		txt_cause_code = (EditText) findViewById(R.id.txt_cause_code);
		txt_police_remarks = (EditText) findViewById(R.id.txt_police_remarks);
		rg_input_method = (RadioGroup) findViewById(R.id.rg_remarks);
		img_next = (ImageButton) findViewById(R.id.img_next);
		img_cause_code = (ImageButton)findViewById(R.id.img_cause_code);
		img_reload_mic = (ImageButton) findViewById(R.id.img_reload_mic);
		img_reload_mic.setVisibility(View.GONE);
		addListenerToNextButton();
		addListenerTOReloadMic();
		addCheckedListenerToInputMethodRadioGroup();
		addListenerToimgCauseCode();
		
		Intent i_cause_code= getIntent();
		
		if (i_cause_code!=null){
			
			String v_type = i_cause_code.getStringExtra("cause_code");
			txt_cause_code.setText(v_type);
		}
	}
	
	public void addListenerToimgCauseCode(){
		img_cause_code.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent ic = new Intent(_this, AccidentCauses.class);
				startActivity(ic);
				
			}
		});
	}

	public void addListenerToNextButton() {
		img_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String cause_code = txt_cause_code.getText().toString().trim();
				String remarks = txt_police_remarks.getText().toString().trim();
				String accident_id = readAccidentID();
				
				if (cause_code.equals("")){
					
					Toast.makeText(_this, "Cause code cannot be empty", Toast.LENGTH_LONG).show();
					
				}else if(remarks.equals("")){
					
					Toast.makeText(_this, "You forgot to complete your remarks", Toast.LENGTH_LONG).show();
					
				}else{
					
					Intent if_ = getIntent();

					String property_category = if_
							.getStringExtra("property_category");
					String name_of_property = if_
							.getStringExtra("name_of_property");
					String property_description = if_
							.getStringExtra("property_description");
					// /upload to online server

					uploadingFinalData(cause_code, remarks, accident_id,property_category, name_of_property,property_description);

				}
			}
		});

	}

	public void uploadingFinalData(final String cause_code,
			final String remarks, final String accident_id,
			final String property_category, final String name_of_property,
			final String property_description) {

		if (isOnline()) {

			// servercode comes here
			Thread trd = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Looper.prepare();
					String url = Utilities.REMARKS_URL;

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
							"rem"));
					nameValuePair.add(new BasicNameValuePair("accident_id",
							accident_id));
					nameValuePair.add(new BasicNameValuePair("cause_code",
							cause_code));
					nameValuePair
							.add(new BasicNameValuePair("remarks", remarks));
					nameValuePair.add(new BasicNameValuePair(
							"property_category", property_category));
					nameValuePair.add(new BasicNameValuePair(
							"name_of_property", name_of_property));
					nameValuePair.add(new BasicNameValuePair(
							"property_description", property_description));

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
		pDialog = new ProgressDialog(PoliceRemarks.this);
		pDialog.setMessage(Html
				.fromHtml("<b>Please wait...</b><br/>Saving all your details"));
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
		closeHandler.sendEmptyMessageDelayed(0, 12000);
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

	public void addListenerTOReloadMic() {

		img_reload_mic.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

				try {
					startActivityForResult(intent, RESULT_SPEECH);
					// txtText.setText("");
				} catch (ActivityNotFoundException a) {
					Toast t = Toast.makeText(getApplicationContext(),
							"Opps! Your device doesn't support Speech to Text",
							Toast.LENGTH_SHORT);
					t.show();
				}

			}
		});

	}

	public void addCheckedListenerToInputMethodRadioGroup() {
		rg_input_method
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						// TODO Auto-generated method stub
						if (getInputMethidOptinSelected().equals(
								"Use voice entry")) {
							// start voice input
							Intent intent = new Intent(
									RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

							intent.putExtra(
									RecognizerIntent.EXTRA_LANGUAGE_MODEL,
									"en-US");

							try {
								startActivityForResult(intent, RESULT_SPEECH);
								// txtText.setText("");
							} catch (ActivityNotFoundException a) {
								Toast t = Toast
										.makeText(
												getApplicationContext(),
												"Opps! Your device doesn't support Speech to Text",
												Toast.LENGTH_SHORT);
								t.show();
							}

						}
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {
				if (img_reload_mic.isShown()) {
					// do nothing
				} else {
					img_reload_mic.setVisibility(View.VISIBLE);
				}
				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				String currentText = txt_police_remarks.getText().toString();
				txt_police_remarks.setText(currentText + " " + text.get(0));
			}
			break;
		}

		}
	}

	public String getInputMethidOptinSelected() {

		int selectedRB = rg_input_method.getCheckedRadioButtonId();

		rb_type_voice = (RadioButton) findViewById(selectedRB);

		String voice_typeIn = rb_type_voice.getText().toString();

		return voice_typeIn;

	}

	public String readAccidentID() {
		SharedPreferences sp = getSharedPreferences(Utilities.ACCIDENT_ID, 1);
		String accidentID = sp.getString("accident_id", "");
		return accidentID;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_police_remarks, menu);
		return true;
	}

}
