package com.androidbegin.parsedownloadimage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MainActivity extends Activity {
	Button button;
	private ProgressDialog progressDialog;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from main.xml
		setContentView(R.layout.activity_main);
		// Show progress dialog

		// Locate the button in main.xml
		button = (Button) findViewById(R.id.button);

		// Capture button clicks
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {

				progressDialog = ProgressDialog.show(MainActivity.this, "",
						"Downloading Image...", true);

				// Locate the class table named "ImageUpload" in Parse.com
				ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
						"Event");

				// Locate the objectId from the class
				query.getInBackground("HxmPtSWnqa",
						new GetCallback<ParseObject>() {

							public void done(ParseObject object,
									ParseException e) {
								// TODO Auto-generated method stub

								// Locate the column named "ImageName" and set
								// the string
								ParseFile fileObject = (ParseFile) object
										.get("eventImage");
								fileObject
										.getDataInBackground(new GetDataCallback() {

											public void done(byte[] data,
													ParseException e) {
												if (e == null) {
													Log.d("test",
															"We've got data in data.");
													// Decode the Byte[] into
													// Bitmap
													Bitmap bmp = BitmapFactory
															.decodeByteArray(
																	data, 0,
																	data.length);

													// Get the ImageView from
													// main.xml
													ImageView image = (ImageView) findViewById(R.id.image);

													// Set the Bitmap into the
													// ImageView
													image.setImageBitmap(bmp);

													// Close progress dialog
													progressDialog.dismiss();

												} else {
													Log.d("test",
															"There was a problem downloading the data.");
												}
											}
										});
							}
						});
			}

		});
	}
}