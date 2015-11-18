package com.cd.crashdata;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Witnesses extends Activity {
	ImageButton img_next, img_witess_1, img_witness_2;
	EditText txt_name_of_property, txt_property_descr;
	RadioGroup rg_property_category, rg_any_property_damaged;
	RadioButton rb_property_category, rb_any_property_damaged;
	TextView txtv_property_name, txtv_property_category,
			txtv_property_description;
	Witnesses _this;

	// Activity request codes
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 2000;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	// directory name to store captured images and videos
	private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
	private Uri fileUri; // file url to store image/video
	String selectedPath = "";
	public static File videoName;
	InputStream inputStream;
	int serverResponseCode = 0;
	String property_category, name_of_property, property_description;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		_this = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_witnesses);
		img_next = (ImageButton) findViewById(R.id.img_next);
		img_witess_1 = (ImageButton) findViewById(R.id.img_audio1);
		img_witness_2 = (ImageButton) findViewById(R.id.img_audio2);
		txt_name_of_property = (EditText) findViewById(R.id.txt_property_name);
		txt_property_descr = (EditText) findViewById(R.id.txt_description);
		rg_property_category = (RadioGroup) findViewById(R.id.rg_category);
		rg_any_property_damaged = (RadioGroup) findViewById(R.id.any_property_damaged_yes_no);
		txtv_property_name = (TextView) findViewById(R.id.txtv_property_name);
		txtv_property_category = (TextView) findViewById(R.id.txtv_property_category);
		txtv_property_description = (TextView) findViewById(R.id.txtv_property_description);

		addClickListenerToImgNext();
		addClickListenerWitness_1();
		addClickListenerToWitness_2();
		addListenerToAnyPropertyDamagedRadioGroup();

		// Checking camera availability
		if (!isDeviceSupportCamera()) {
			Toast.makeText(getApplicationContext(),
					"Sorry! Your device doesn't support camera",
					Toast.LENGTH_LONG).show();
			// will close the app if the device does't have camera
			finish();
		}
	}

	public void addListenerToAnyPropertyDamagedRadioGroup() {
		rg_any_property_damaged
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						// TODO Auto-generated method stub
						if (getSelectedOptionOnAnyPropertyDamaged().equals(
								"Yes")) {
							// disable radion group driver injured
							txtv_property_category.setVisibility(View.VISIBLE);
							txtv_property_description
									.setVisibility(View.VISIBLE);
							txtv_property_name.setVisibility(View.VISIBLE);

							txt_name_of_property.setEnabled(true);
							txt_name_of_property.setVisibility(View.VISIBLE);

							txt_property_descr.setEnabled(true);
							txt_property_descr.setVisibility(View.VISIBLE);

							rg_property_category.setEnabled(true);
							rg_property_category.setVisibility(View.VISIBLE);
						}
						if (getSelectedOptionOnAnyPropertyDamaged()
								.equals("No")) {

							// disable hide the following controls
							txtv_property_category.setVisibility(View.GONE);
							txtv_property_description.setVisibility(View.GONE);
							txtv_property_name.setVisibility(View.GONE);

							txt_name_of_property.setEnabled(false);
							txt_name_of_property.setVisibility(View.GONE);

							txt_property_descr.setEnabled(false);
							txt_property_descr.setVisibility(View.GONE);

							rg_property_category.setEnabled(false);
							rg_property_category.setVisibility(View.GONE);
							// txtv_driver_injured.setVisibility(View.VISIBLE);
							// rg_driver_injured.setEnabled(true);
							// rg_driver_injured.setVisibility(View.VISIBLE);
						}

					}
				});

	}

	public String getSelectedOptionOnAnyPropertyDamaged() {

		int selectedRB = rg_any_property_damaged.getCheckedRadioButtonId();
		rb_any_property_damaged = (RadioButton) findViewById(selectedRB);
		String private_public = rb_any_property_damaged.getText().toString();
		return private_public;

	}

	public void addClickListenerToImgNext() {
		img_next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (rg_property_category.isShown()&& txtv_property_name.isShown()) {
					property_category = checkPropertyCategoryOption();
					name_of_property = txt_name_of_property.getText()
							.toString().trim();
					property_description = txt_property_descr.getText()
							.toString().trim();
				} else {
					property_category = "No property damaged";
					name_of_property = "No property damaged";
					property_description = "No property damaged";
				}

				Intent i = new Intent(_this, AccidentPhotos.class);
				i.putExtra("property_category", property_category);
				i.putExtra("name_of_property", name_of_property);
				i.putExtra("property_description", property_description);
				
				//Log.i("empty", property_category);
				//Log.i("empty", name_of_property);
				//Log.i("empty", property_description);
				
				
				_this.startActivity(i);
			}
		});

	}

	public String checkPropertyCategoryOption() {
		int selectedRB = rg_property_category.getCheckedRadioButtonId();
		rb_property_category = (RadioButton) findViewById(selectedRB);
		String private_public = rb_property_category.getText().toString();
		return private_public;
	}

	public void addClickListenerWitness_1() {
		img_witess_1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				recordVideo();

			}
		});
	}

	public void addClickListenerToWitness_2() {
		img_witness_2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				recordVideo();

			}
		});
	}

	/**
	 * Receiving activity result method will be called after closing the camera
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if the result is capturing Image
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// successfully captured the image
				// display it in image view
			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				Toast.makeText(getApplicationContext(),
						"User cancelled image capture", Toast.LENGTH_SHORT)
						.show();
			} else {
				// failed to capture image
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
						.show();
			}
		} else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// video successfully recorded
				// preview the recorded video
				Log.e("ResultError", "Error is here 1");
				// Uri selectedImageUri = data.getData();

				Log.e("ResultError", "Error is here 2");
				// selectedPath = getPath(selectedImageUri);
				Log.e("ResultError", "Error is here 3");

				doFileUpload();

			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled recording
				Toast.makeText(getApplicationContext(),
						"User cancelled video recording", Toast.LENGTH_SHORT)
						.show();
			} else {
				// failed to record video
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to record video", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	private void doFileUpload() {

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Log.e("ResultError", "Error is here 4");
					HttpURLConnection conn = null;
					DataOutputStream dos = null;
					DataInputStream inStream = null;
					Log.e("ResultError", "Error is here 5");
					String lineEnd = "\r\n";
					String twoHyphens = "--";
					String boundary = "*****";
					int bytesRead, bytesAvailable, bufferSize;
					byte[] buffer;
					int maxBufferSize = 1 * 1024 * 1024;
					// String responseFromServer = "";
					String urlString = Utilities.WITNESS_VIDEO_URL;
					try {
						Log.e("ResultError", "Error is here 6");
						// ------------------ CLIENT REQUEST
						Log.e("ResultError", "Error is here 7");

						FileInputStream fileInputStream = new FileInputStream(
								new File(fileUri.getPath()));
						// open a URL connection to the <span id="IL_AD8"
						// class="IL_AD">Servlet</span>
						Log.e("ResultError", "Error is here 8");
						URL url = new URL(urlString);
						// Open a HTTP connection to the URL
						conn = (HttpURLConnection) url.openConnection();
						Log.e("ResultError", "Error is here 9");
						// Allow Inputs
						conn.setDoInput(true);
						// Allow Outputs
						conn.setDoOutput(true);
						// Don't use a cached copy.
						conn.setUseCaches(false);
						Log.e("ResultError", "Error is here 10");
						// Use a post method.
						conn.setRequestMethod("POST");
						conn.setRequestProperty("Connection", "Keep-Alive");
						Log.e("ResultError", "Error is here 11");
						conn.setRequestProperty("Content-Type",
								"multipart/form-data;boundary=" + boundary);

						Log.e("ResultError", "Error is here 12");
						// conn.setRequestProperty("uploaded_file",
						// fileUri.getPath());
						dos = new DataOutputStream(conn.getOutputStream());
						Log.e("ResultError", "Error is here 13");
						dos.writeBytes(twoHyphens + boundary + lineEnd);
						Log.e("ResultError", "Error is here 14");

						dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
								+ fileUri.getPath() + "\"" + lineEnd);

						dos.writeBytes(lineEnd);
						// create a buffer of maximum size
						bytesAvailable = fileInputStream.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						buffer = new byte[bufferSize];
						// read file and write it into form...
						bytesRead = fileInputStream.read(buffer, 0, bufferSize);
						while (bytesRead > 0) {
							dos.write(buffer, 0, bufferSize);
							bytesAvailable = fileInputStream.available();
							bufferSize = Math
									.min(bytesAvailable, maxBufferSize);
							bytesRead = fileInputStream.read(buffer, 0,
									bufferSize);
						}
						// send multipart form data necesssary after file
						// data...
						dos.writeBytes(lineEnd);
						dos.writeBytes(twoHyphens + boundary + twoHyphens
								+ lineEnd);
						Log.e("ResultError", "Error is here 16");

						serverResponseCode = conn.getResponseCode();
						String serverResponseMessage = conn
								.getResponseMessage();

						Log.i("uploadFile", "HTTP Response is : "
								+ serverResponseMessage + ": "
								+ serverResponseCode);
						Log.e("ResultError", "Error is here 17");
						if (serverResponseCode == 200) {

							runOnUiThread(new Runnable() {
								@Override
								public void run() {

									String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
											+ " http://www.androidexample.com/media/uploads/";
									Log.e("ResultError", "Error is here 18");
									Toast.makeText(Witnesses.this,
											"File Upload Complete.",
											Toast.LENGTH_SHORT).show();
								}
							});
						}
						// close streams
						Log.e("Debug", "File is written");
						fileInputStream.close();
						dos.flush();
						dos.close();
					} catch (MalformedURLException ex) {
						Log.e("Debug", "error: " + ex.getMessage(), ex);
					} catch (IOException ioe) {
						Log.e("Debug", "error: " + ioe.getMessage(), ioe);
					}
					// ------------------ read the SERVER RESPONSE
					try {
						inStream = new DataInputStream(conn.getInputStream());
						String str;

						while ((str = inStream.readLine()) != null) {
							Log.e("Debug", "Server Response " + str);

							runOnUiThread(new Runnable() {
								@Override
								public void run() {

									Toast.makeText(Witnesses.this, "sucess",
											Toast.LENGTH_SHORT).show();
								}
							});

						}
						inStream.close();

					} catch (IOException ioex) {
						Log.e("Debug", "error: " + ioex.getMessage(), ioex);
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		t.start();

	}

	public String getAccidentID() {
		SharedPreferences sp = getSharedPreferences(Utilities.ACCIDENT_ID, 1);
		String accidentID = sp.getString("accident_id", "");
		return accidentID;
	}

	/**
	 * Recording video
	 */
	private void recordVideo() {
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

		// set video quality
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
															// name

		// start the video capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);

	}

	/**
	 * Creating file uri to store image/video
	 */
	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/**
	 * returning image / video
	 */
	private File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
						+ IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create the storage directory if it does not exist

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {

			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4" + getAccidentID());

			videoName = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");

		} else {
			return null;
		}

		return mediaFile;
	}

	/**
	 * Checking device has camera hardware or not
	 * */
	private boolean isDeviceSupportCamera() {
		if (getApplicationContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_witnesses, menu);
		return true;
	}

}
