package com.cd.crashdata;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AccidentPhotos extends Activity {
	ImageButton img_next;
	TextView txtv_take_photo;
	AccidentPhotos _this;
	InputStream inputStream;
	// Activity request codes
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private Uri fileUri; // file url to store image/video
	String selectedPath = "";
	public static File videoName;
	
	// directory name to store captured images and videos
	private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		_this = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accident_photos);
		img_next = (ImageButton )findViewById(R.id.img_next);
		txtv_take_photo = (TextView)findViewById(R.id.txtv_take_photo);
		
		// Checking camera availability
				if (!isDeviceSupportCamera()) {
					Toast.makeText(getApplicationContext(),
							"Sorry! Your device doesn't support camera",
							Toast.LENGTH_LONG).show();
					// will close the app if the device does't have camera
					finish();
				}
		addListenerToNextButton();
		addListenerToTakePhotoTextView();
	}
	
	public void addListenerToNextButton(){
		
		img_next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(_this,PoliceRemarks.class);
				Intent if_ = getIntent();
				i.putExtra("property_category", if_.getStringExtra("property_category"));
				i.putExtra("name_of_property", if_.getStringExtra("name_of_property"));
				i.putExtra("property_description", if_.getStringExtra("property_description"));
				_this.startActivity(i);
				
			}
		});
	}
	public void addListenerToTakePhotoTextView(){

		txtv_take_photo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				captureImage();
				
			}
		});
	}    
	
	/**
	 * Capturing Camera Image will lauch camera app requrest image capture
	 */
	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
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
	private static File getOutputMediaFile(int type) {

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
					+ "VID_" + timeStamp + ".mp4");
			videoName= new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
			
		} else {
			return null;
		}

		return mediaFile;
	}
	
	public String convertResponseToString(HttpResponse response) throws IllegalStateException, IOException{
		 
        String res = "";
        StringBuffer buffer = new StringBuffer();
        inputStream = response.getEntity().getContent();
       final int contentLength = (int) response.getEntity().getContentLength(); //getting content length…..
         runOnUiThread(new Runnable() {
        
       @Override
       public void run() {
           //Toast.makeText(AccidentPhotos.this, "contentLength : " + contentLength, Toast.LENGTH_LONG).show();                     
       }
   });
     
        if (contentLength < 0){
        }
        else{
               byte[] data = new byte[512];
               int len = 0;
               try
               {
                   while (-1 != (len = inputStream.read(data)) )
                   {
                       buffer.append(new String(data, 0, len)); //converting to string and appending  to stringbuffer…..
                   }
               }
               catch (IOException e)
               {
                   e.printStackTrace();
               }
               try
               {
                   inputStream.close(); // closing the stream…..
               }
               catch (IOException e)
               {
                   e.printStackTrace();
               }
               res = buffer.toString();     // converting stringbuffer to string…..

               runOnUiThread(new Runnable() {
                
               @Override
               public void run() {
                  //Toast.makeText(UploadImage.this, "Result : " + res, Toast.LENGTH_LONG).show();
               }
           });
               //System.out.println("Response => " +  EntityUtils.toString(response.getEntity()));
        }
        return res;
   }
	
	
	private void uploadImageToSever(){
		
		Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath());       
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
        byte [] byte_arr = stream.toByteArray();
        String image_str = Base64.encodeBytes(byte_arr);
        final ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("image",image_str));
        nameValuePairs.add(new BasicNameValuePair("accident_ID",getAccidentID()));
        nameValuePairs.add(new BasicNameValuePair("photo_name","PHOTO_" + getDateTime()));

         Thread t = new Thread(new Runnable() {
         
        @Override
        public void run() {
              try{
                     HttpClient httpclient = new DefaultHttpClient();
                     HttpPost httppost = new HttpPost(Utilities.ACCIDENT_PHOTO_URL);
                     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                     HttpResponse response = httpclient.execute(httppost);
                     final String the_string_response = convertResponseToString(response);
                     runOnUiThread(new Runnable() {
                             
                            @Override
                            public void run() {
                                Toast.makeText(AccidentPhotos.this, "Response " + the_string_response, Toast.LENGTH_LONG).show();                          
                            }
                        });
                      
                 }catch(final Exception e){
                      runOnUiThread(new Runnable() {
                         
                        @Override
                        public void run() {
                            Toast.makeText(AccidentPhotos.this, "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();                              
                        }
                    });
                       System.out.println("Error in http connection "+e.toString());
                 }  
        }
    });
     t.start();
		
	}
	
	/**
	 * Receiving activity result method will be called after closing the camera
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		txtv_take_photo.setText("");
		txtv_take_photo.setText("Take another photo");
		// if the result is capturing Image
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				//upload image to server
				uploadImageToSever();
				
			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				Toast.makeText(getApplicationContext(),
						"You cancelled camera image", Toast.LENGTH_SHORT)
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
	                //selectedPath = getPath(selectedImageUri);
	                Log.e("ResultError", "Error is here 3");
				
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
	
	
	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date date = new Date();
		return dateFormat.format(date);
	}
	public String getAccidentID() {
		SharedPreferences sp = getSharedPreferences(Utilities.ACCIDENT_ID, 1);
		String accidentID = sp.getString("accident_id", "");
		return accidentID;
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
	
	public String readAccidentID(){
		SharedPreferences sp = getSharedPreferences(Utilities.ACCIDENT_ID, 1);
		String accidentID = sp.getString("accident_id", "");
		return accidentID;
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_accident_photos, menu);
		return true;
	}

}
