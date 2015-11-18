package com.cd.crashdata;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ManageOptions extends Activity {

	TextView txt_help;
	TextView txt_emergency;
	TextView file_new;
	private ManageOptions _this;
	GetGPSLocation gps;
	public double latitude_;
	public double longitude_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		_this = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_options);

		file_new = (TextView) findViewById(R.id.txt_file_new);

		file_new.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				gps = new GetGPSLocation(ManageOptions.this);
				// check if GPS enabled
				if (gps.canGetLocation()) {

					try{
						Thread.sleep(3000);
					}catch(Exception e){
						
					}
					
					
					if (isOnline()) {

						if (getPlacesName().length() > 0) {

							Log.i("tag", getPlacesName());
							Intent i = new Intent(_this,LocationTime.class);
							
							i.putExtra("area_name", getPlacesName());
							i.putExtra("lat", String.valueOf(latitude_));
							i.putExtra("lng", String.valueOf(longitude_));
							
							_this.startActivity(i);

						} else {

							Toast.makeText(_this, "wait", Toast.LENGTH_LONG)
									.show();
						}

					} else {

						String dialogTitle = getResources().getString(
								R.string.no_internet_connection);
						String dialogBody = getResources().getString(
								R.string.enable_data_or_leave_airplane_mode);
						String negativeBtnLabel = getResources().getString(
								R.string.cancel);
						String positiveBtnLabel = getResources().getString(
								R.string.settings);
						final String settingArea = Settings.ACTION_AIRPLANE_MODE_SETTINGS;

						promptUserToCreateGoogleAccount(dialogTitle,
								dialogBody, negativeBtnLabel, positiveBtnLabel,
								settingArea);

					}
				} else {

					// can't get location
					// GPS or Network is not enabled
					// Ask user to enable GPS/network in settings
					gps.showSettingsAlert();
				}

			}
		});

		txt_help = (TextView) findViewById(R.id.txt_help);

		txt_help.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(getApplicationContext(), Help.class);

				startActivity(i);

			}

		});

		txt_emergency = (TextView) findViewById(R.id.txt_emergency);

		txt_emergency.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

			

				Intent i = new Intent(getApplicationContext(), Emergency.class);

				startActivity(i);

			}
		});
	}

	public String getPlacesName() {

		gps = new GetGPSLocation(ManageOptions.this);

		 latitude_ = gps.getLatitude();
		 longitude_ = gps.getLongitude();
		int factor = 10000000;

		int scaled_and_roundedLAT = (int) (latitude_ * factor + 0.5);
		int scaled_and_roundedLONG = (int) (longitude_ * factor + 0.5);

		final double latitude = (double) scaled_and_roundedLAT / factor;
		final double longitude = (double) scaled_and_roundedLONG / factor;

		return convertToLocation(latitude, longitude);
	}

	public String convertToLocation(double latitude, double longitude) {
		String exactLocation = "";
		String placeName = "";
		Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
		try {
			List<Address> addresses = geoCoder.getFromLocation(latitude,
					longitude, 1);

			if (addresses.size() > 0) {
				// place += addresses.get(0).getLocality() ;
				exactLocation = addresses.get(0).getFeatureName();
				placeName = addresses.get(0).getLocality();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Log.i("tag", "here " + exactLocation + ", " + placeName);

		return exactLocation + ", " + placeName;
	}

	private void promptUserToCreateGoogleAccount(String title, String body,
			String negativeButtonLabel, String positiveButtonLabbel,
			final String settingsArea) {
		// setResult(account);
		AlertDialog.Builder settingsAlert = new AlertDialog.Builder(_this);

		// Setting Dialog Title
		settingsAlert.setTitle(title);
		// Setting Dialog Message
		settingsAlert.setMessage(body);
		// On pressing Settings button
		settingsAlert.setPositiveButton(positiveButtonLabbel,
				new DialogInterface.OnClickListener() {
					// On pressing Settings button
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(settingsArea);

						_this.startActivity(intent);

					}
				});

		// on pressing cancel button
		settingsAlert.setNegativeButton(negativeButtonLabel,
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_manage_options, menu);
		return true;
	}

}
