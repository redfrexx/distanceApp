package de.s6chludw.distanceapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class MainActivity extends ActionBarActivity implements Orientation.Listener{
	
	// Objects and variables
	private Camera mCamera;
	private Preview mPreview;
	private Orientation mOrientation;
	private AttitudeIndicator mAttitudeIndicator;
	SensorManager mSensorManager;
	WindowManager mWindowManager;
	
	static float height = 1.5f;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Display orientation rotated for Samsung Tab S
		mCamera = getCameraInstance();
		mCamera.setDisplayOrientation(90);
		
		mOrientation = new Orientation((SensorManager) getSystemService(Activity.SENSOR_SERVICE), getWindow().getWindowManager());
		
		// Create camera view
		mPreview = new Preview(this, mCamera);
		Context context = mPreview.getContext();
		// Create Attitude Indicator 
		mAttitudeIndicator = new AttitudeIndicator(context);
        // Add both to preview window
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        preview.addView(mAttitudeIndicator);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle action bar items
	    switch (item.getItemId()) {
	        case R.id.action_help:
	        	messageBox(getString(R.string.action_help), getString(R.string.action_help_text));
	            return true;
	        case R.id.height_130:
	            height = 1.3f;
	            return true;
	        case R.id.height_150:
	            height = 1.5f;
	            return true;
	        case R.id.height_170:
	            height = 1.7f;
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void messageBox(String title, String content)
	{
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setTitle(title);
		dialog.setMessage(Html.fromHtml(content));
		dialog.show();
	}
	
	@Override
	public void onOrientationChanged(float pitch, float roll, float azimuth) {
		mAttitudeIndicator.setData(roll, pitch, azimuth, height);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		mOrientation.startListener(this);	
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mOrientation.stopListener();
	}
	
	public static Camera getCameraInstance() {
		Camera c = null;
		
		try {
			c = Camera.open();
		} catch (Exception e) {
			Log.e("Failed","failed to open camera");
			e.printStackTrace();
		}
		
		return c;
	}
}
