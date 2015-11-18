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
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class UploadVideo extends Activity {

	// Activity request codes
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;


	// directory name to store captured images and videos
	private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";


	private Uri fileUri; // file url to store image/video
	String selectedPath = "";
	public static File videoName;

	private ImageView imgPreview;
	private VideoView videoPreview;
	private Button btnCapturePicture, btnRecordVideo;
	public TextView messageText;
	ProgressDialog dialog = null;
	 InputStream inputStream;
	 int serverResponseCode = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_options);

		imgPreview = (ImageView) findViewById(R.id.imgPreview);
		videoPreview = (VideoView) findViewById(R.id.videoPreview);
		btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);
		btnRecordVideo = (Button) findViewById(R.id.btnRecordVideo);
		messageText =(TextView) findViewById(R.id.status);

		/**
		 * Capture image button click event
		 */
		btnCapturePicture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// capture picture
				captureImage();
			}
		});

		/**
		 * Record video button click event
		 */
		btnRecordVideo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// record video
				recordVideo();
			}
		});

		// Checking camera availability
		if (!isDeviceSupportCamera()) {
			Toast.makeText(getApplicationContext(),
					"Sorry! Your device doesn't support camera",
					Toast.LENGTH_LONG).show();
			// will close the app if the device does't have camera
			finish();
		}
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
	 * Here we store the file url as it will be null after returning from camera
	 * app
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// save file url in bundle as it will be null on scren orientation
		// changes
		outState.putParcelable("file_uri", fileUri);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// get the file url
		fileUri = savedInstanceState.getParcelable("file_uri");
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
	 * Receiving activity result method will be called after closing the camera
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if the result is capturing Image
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// successfully captured the image
				// display it in image view

				previewCapturedImage();
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
	                //selectedPath = getPath(selectedImageUri);
	                Log.e("ResultError", "Error is here 3");
	                
	                doFileUpload();
	                
	                previewVideo();
				
				
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

	
	public String getPath(Uri uri) {
        String[] projection = { MediaColumns.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

	
    private void doFileUpload(){
    	
    	Thread t = new Thread(new Runnable() {
            
            @Override
            public void run() {
                  try{
    	Log.e("ResultError", "Error is here 4");
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        Log.e("ResultError", "Error is here 5");
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1*1024*1024;
       // String responseFromServer = "";
        String urlString = "http://10.0.2.2/crashData/2.php";
        try
        {
        	Log.e("ResultError", "Error is here 6");
         //------------------ CLIENT REQUEST
        	Log.e("ResultError", "Error is here 7");
        	
        FileInputStream fileInputStream = new FileInputStream(new File(fileUri.getPath()));
         // open a URL connection to the <span id="IL_AD8" class="IL_AD">Servlet</span>
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
         conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
         Log.e("ResultError", "Error is here 12");
         //conn.setRequestProperty("uploaded_file", fileUri.getPath());
         dos = new DataOutputStream( conn.getOutputStream());
         Log.e("ResultError", "Error is here 13");
         dos.writeBytes(twoHyphens + boundary + lineEnd);
         Log.e("ResultError", "Error is here 14");
        
         dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" +fileUri.getPath() + "\"" + lineEnd);
     
         dos.writeBytes(lineEnd);
         // create a buffer of maximum size
         bytesAvailable = fileInputStream.available();
         bufferSize = Math.min(bytesAvailable, maxBufferSize);
         buffer = new byte[bufferSize];
         // read file and write it into form...
         bytesRead = fileInputStream.read(buffer, 0, bufferSize);
         while (bytesRead > 0)
         {
          dos.write(buffer, 0, bufferSize);
          bytesAvailable = fileInputStream.available();
          bufferSize = Math.min(bytesAvailable, maxBufferSize);
          bytesRead = fileInputStream.read(buffer, 0, bufferSize);
         }
         // send multipart form data necesssary after file data...
         dos.writeBytes(lineEnd);
         dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
         Log.e("ResultError", "Error is here 16");
         
         serverResponseCode = conn.getResponseCode();
         String serverResponseMessage = conn.getResponseMessage();
         
         Log.i("uploadFile", "HTTP Response is : "
                 + serverResponseMessage + ": " + serverResponseCode);
         Log.e("ResultError", "Error is here 17");
         if(serverResponseCode == 200){
             
             runOnUiThread(new Runnable() {
                  @Override
				public void run() {
                       
                      String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    +" http://www.androidexample.com/media/uploads/";
                      Log.e("ResultError", "Error is here 18");
                       
                      messageText.setText(msg);
                      Toast.makeText(UploadVideo.this, "File Upload Complete.", 
                                   Toast.LENGTH_SHORT).show();
                  }
              });                
         }    
         // close streams
         Log.e("Debug","File is written");
         fileInputStream.close();
         dos.flush();
         dos.close();
        }
        catch (MalformedURLException ex)
        {
             Log.e("Debug", "error: " + ex.getMessage(), ex);
        }
        catch (IOException ioe)
        {
             Log.e("Debug", "error: " + ioe.getMessage(), ioe);
        }
        //------------------ read the SERVER RESPONSE
        try {
              inStream = new DataInputStream ( conn.getInputStream() );
               String str;
 
              while (( str = inStream.readLine()) != null)
              {
                   Log.e("Debug","Server Response "+str);
                   
                   runOnUiThread(new Runnable() {
                       @Override
					public void run() {
                    	   
                           Toast.makeText(UploadVideo.this,"sucess", 
                                        Toast.LENGTH_SHORT).show();
                       }
                   });   
                   
              }
              inStream.close();
 
        }
        catch (IOException ioex){
             Log.e("Debug", "error: " + ioex.getMessage(), ioex);
        }
        
                  }catch (Exception e) {
					// TODO: handle exception
				}
            }
    	});
    	t.start();
        
    }
      
	
	/**
	 * Display image from a path to ImageView
	 */
	private void previewCapturedImage() {
		try {
			// hide video preview
			videoPreview.setVisibility(View.GONE);

			imgPreview.setVisibility(View.VISIBLE);

			// bimatp factory
			BitmapFactory.Options options = new BitmapFactory.Options();

			// downsizing image as it throws OutOfMemory Exception for larger
			// images
			options.inSampleSize = 8;

			final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
					options);

			imgPreview.setImageBitmap(bitmap);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Previewing recorded video
	 */
	private void previewVideo() {
		try {
			// hide image preview
			imgPreview.setVisibility(View.GONE);

			videoPreview.setVisibility(View.VISIBLE);
			videoPreview.setVideoPath(fileUri.getPath());
			//uploadFile(fileUri.getPath(),"","","http://10.0.2.2/crashData/2.php");
			// start playing
			videoPreview.start();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ------------ Helper Methods ----------------------
	 * */

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
}