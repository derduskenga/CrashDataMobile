package com.cd.crashdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
//import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import android.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
import android.os.Looper;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	EditText txt_police_no, txt_password;
	Button btn_login;
	String username, password, RSA_PRIVATE_KEY;
	String http_req_result, test_login_feedback;
	Login _this;

	// String key =
	// "MIICWwIBAAKBgQDVIJ8H3Oszc5fWdgpwymWksF1WxkXJHIfdS6Ta1bHeqwEzPIkN f3iVk14LfaoSZpRb9Yvi/jvkXxIzJbHq6aKfnQOC6tKIiixvVvpCfxr1eV4urDdz H9RNy9bqGdXzTQdgQi+KRx0Dcy9RNsl7ZGLAGrUFRnPI4GTdH+7wm4QogQIDAQAB AoGAcUcKX7KC7HDm5h0NRY+94H/AzItLsi3Q5MT81Tc5d+EqHSJysdLrs4yFMtRS 3b7Z4dqrxDVefe4uDTNe0j3lqboKz8oKwAU+paKx3wubHb7aeQnfzwM9mPQJHgEO zBjlvbL4oEa/gklu3VohZAc1daqpPajdWuOQQp4S+jUllrECQQDrITlSjvkxt8ud /vYIcEXHew3iW4nzaAH3z4PRAGZofRpk/OusGZ6cdZoYMTZcdxYTCCbZ5eeyGukW 5QCadie1AkEA6Atx8Z0F7WhLI2lGvCGy+vIOL0vBDZSma0cvLYLAXMx8duoWQ9J2 LwT7SsnRXMeq/8wlNHL7mFEf+YFZBKKlHQJAO78kfrr/zUdjwREBWaGVyZuWKpeS FTyvi1W6rAgK/bAUXeb6x69241DqyAzxQEuuW0WuAZ5u4o39/qhQH++4JQJAAepe RW1TaDNNM3yh/dmVXabz4QYSEOeiPA55YDnNFrcFbAHgryyklxzGakaiOM7ZJYVs 5TLxyr8YsXmU34nsLQJALzC8CaFXJcnU0+6+KoKX7iq1aP3X4LgP4Gianix6pfRo aV8UHnfFLRSgPdn1ZYmKtJfnsJXJYoE+o9xEErb5EQ==";

	// String str =
	// "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDWtJ/7+dzfSk3EC04PIpdaF0e T1Xr9fOFQVVcF6FokfJtxugs/NbcuPj86UDvIpdrWI2ztqyX7L7/PSQIdjvkATxs eI1dfdYJ5+aW0J5g80cJqNdL4Rr2APhUv9CJUHApUdT8q27p1xUgbgZ/ERjy5Anc HGaRJwBWBcW3CboomwIDAQAB";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		_this = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		txt_police_no = (EditText) findViewById(R.id.txt_police_no);
		txt_password = (EditText) findViewById(R.id.txt_password);
		btn_login = (Button) findViewById(R.id.btn_login);

		addListenerToLoginButton();

		fetchRsaKey("pri_key");

		// Log.d("res", "5 " + RSA_PRIVATE_KEY);
	}

	public String encryptWithPrivateKey(String plaintext) {

		String encoded = null;
		byte[] encrypted = null;

		try {
			Log.e("track", "key " + RSA_PRIVATE_KEY);
			String privKeyPEM = RSA_PRIVATE_KEY;
			byte[] decoded = Base64.decode(privKeyPEM, Base64.DEFAULT);
			X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			PrivateKey privKey = kf.generatePrivate(spec);

			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, privKey);

			encrypted = cipher.doFinal(plaintext.getBytes());

			Log.e("track", "encrypted " + encrypted.toString());
			// encoded = Base64.encodeToString(encrypted, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new String(encrypted);

	}

	public void saveJobNumber(String job_number) {

		SharedPreferences sp = getSharedPreferences(Utilities.JOB_NO, 1);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("job_no", job_number);
		editor.commit();
	}

	public String encrypt(String key, String original) throws IOException,
			NoSuchAlgorithmException, InvalidKeySpecException,
			NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {

		byte[] encodedKey = key.getBytes();

		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PublicKey pkPublic = kf.generatePublic(publicKeySpec);

		Cipher pkCipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
		pkCipher.init(Cipher.ENCRYPT_MODE, pkPublic);
		byte[] encryptedInByte = pkCipher.doFinal(original.getBytes());

		// String encryptedInString = new
		// String(Base64.encode(encryptedInByte));
		// String encryptedInString = Base64.encodeToString(encryptedInByte,
		// Base64.NO_WRAP);

		return new String(encryptedInByte);
	}



	public void addListenerToLoginButton() {
		btn_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				// Log.d("res", "5 " + RSA_PRIVATE_KEY);

				// rsaEncryption rsaEncryptionObj = new
				// rsaEncryption(RSA_PRIVATE_KEY);
				username = txt_police_no.getText().toString().trim();
				password = txt_password.getText().toString().trim();

				saveJobNumber(username);// save police job number to shared
										// preference.

				Log.i("test", "u " + username);
				Log.i("test", "p " + password);

				Log.e("track ", "1");

				String cipherUsername = "";
				String cipherPassword = "";
				Log.e("track ", "2");

				Log.e("track ", "3");
				try {
					//cipherUsername = new String(loginEncryption(RSA_PRIVATE_KEY, username));
					Log.e("track ", "4");
					//cipherPassword = new String(loginEncryption(RSA_PRIVATE_KEY, password));
				} catch (Exception e) {
					e.getStackTrace();
				}

				Log.e("track ", "5");

				Log.i("test", "ue " + cipherUsername);
				Log.i("test", "pe " + cipherPassword);

				sendEncryptedData(cipherUsername, cipherPassword);

			}
		});

	}

	public String encrypt(String pin) {
		String key = RSA_PRIVATE_KEY;
		String encryptedBase64PIN = "";
		byte[] sigBytes2 = key.getBytes();
		Log.e("track", "new key is: " + key);
		try {
			PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(
					new X509EncodedKeySpec(sigBytes2));
			encryptedBase64PIN = encode(publicKey, pin);

			Log.e("track", "encoded key is: " + encryptedBase64PIN);
			// getSecToken();
		} catch (Exception e) {
			Log.e("track", "error is: " + e.getMessage().toString());
		}

		return encryptedBase64PIN;
	}

	public static String encode(Key publicKey, String data)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

		byte[] byteData = data.getBytes(); // convert string to byte array

		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encryptedByteData = cipher.doFinal(byteData);

		// String s = Base64.encodeToString(encryptedByteData, Base64.NO_WRAP);
		return new String(encryptedByteData); // convert encrypted byte array to
												// string and return it
	}

	public void sendEncryptedData(final String cipherUsername,
			final String cipherPassword) {

		if (isOnline()) {

			// servercode comes here
			Thread trd = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.e("Registration",
							"gone to connectin class with url starting...1");
					Looper.prepare();

					final String url = Utilities.SEND_ENCRYPTED_DATA;

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

					nameValuePair.add(new BasicNameValuePair("cipherUsername",
							cipherUsername));
					nameValuePair.add(new BasicNameValuePair("cipherPassword",
							cipherPassword));

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

						test_login_feedback = sb.toString();

						Log.d("res", "4 " + test_login_feedback);

						// changes to gui can be done on the UI thread

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// do stuff on main GUI here
								// generated this stuff
								/*
							 * 
							 * 
							 *		
							 */

								Toast.makeText(_this, test_login_feedback,
										Toast.LENGTH_LONG).show();

							}
						});

						Log.d("res", "in function populate 1");
						// Log.d("res", "Count is " +
						// String.valueOf(str.length()));

					} catch (Exception e) {
						Log.e("log_tag",
								"Error converting result " + e.toString());
					}
					// Looper.loop();

				}

			});

			trd.start();

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

	public String fetchRsaKey(final String rsaStr) {

		if (isOnline()) {

			// servercode comes here
			Thread trd = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.e("Registration",
							"gone to connectin class with url starting...1");
					Looper.prepare();

					final String url = Utilities.RSA_KEY_REQUEST;

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

					nameValuePair.add(new BasicNameValuePair("vReg", rsaStr));

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

						http_req_result = sb.toString();

						Log.d("res", "4 " + http_req_result);
						RSA_PRIVATE_KEY = http_req_result;

						// changes to gui can be done on the UI thread

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// do stuff on main GUI here
								// generated this stuff
								/*
							 * 
							 * 
							 *		
							 */

								Toast.makeText(_this, http_req_result,
										Toast.LENGTH_LONG).show();

							}
						});

						Log.d("res", "in function populate 1");
						// Log.d("res", "Count is " +
						// String.valueOf(str.length()));

					} catch (Exception e) {
						Log.e("log_tag",
								"Error converting result " + e.toString());
					}
					// Looper.loop();

				}

			});

			trd.start();

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

		return http_req_result;

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

}
