package de.s6chludw.distanceapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Surface;
import android.view.WindowManager;

public class Orientation implements SensorEventListener {

	SensorManager mSensorManager;
	WindowManager mWindowManager;
	Sensor mRotationSensor;
	Listener mListener;
	int mLastAccuracy;
	
	public Orientation(SensorManager sensorManager, WindowManager windowManager) {
		mSensorManager = sensorManager;
		mWindowManager = windowManager;
		mRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		if (mLastAccuracy != accuracy) {
			mLastAccuracy = accuracy;
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (mListener == null) {
			return; 
		}
		if (mLastAccuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) { 
			return;
		}
		if (event.sensor == mRotationSensor) { 
			updateOrientation(event.values);
		}
	}

	public interface Listener {
		void onOrientationChanged(float pitch, float roll, float azimuth);
	}
	
	public void startListener(Listener listener) {
		mListener = listener;
		mSensorManager.registerListener(this, mRotationSensor, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	public void stopListener() {
		mListener = null;
		mSensorManager.unregisterListener(this);		
	}
	
	public void updateOrientation(float[] rotationVector) {
		
		int worldAxisForDeviceAxisX = 0, worldAxisForDeviceAxisY = 0;
		float[] rotationMatrix = new float[9]; 
		SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVector);
		
		// Adjust the rotation matrix for the device orientation
		int screenRotation = mWindowManager.getDefaultDisplay().getRotation(); 
		
		if (screenRotation == Surface.ROTATION_0) {
			worldAxisForDeviceAxisX = SensorManager.AXIS_X;
			worldAxisForDeviceAxisY = SensorManager.AXIS_Z;
		} else if (screenRotation == Surface.ROTATION_90) { 
			worldAxisForDeviceAxisX = SensorManager.AXIS_Z;
			worldAxisForDeviceAxisY = SensorManager.AXIS_MINUS_X;
		} else if (screenRotation == Surface.ROTATION_180) {
			worldAxisForDeviceAxisX = SensorManager.AXIS_MINUS_X;
			worldAxisForDeviceAxisY = SensorManager.AXIS_MINUS_Z;
		} else if (screenRotation == Surface.ROTATION_270) {
			worldAxisForDeviceAxisX = SensorManager.AXIS_MINUS_Z;
			worldAxisForDeviceAxisY = SensorManager.AXIS_X; 
		}
		
		float[] adjustedRotationMatrix = new float[9]; 
		SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisForDeviceAxisX, worldAxisForDeviceAxisY, adjustedRotationMatrix);
		
		// Transform rotation matrix into azimuth/pitch/roll
		float[] orientation = new float[3]; 
		SensorManager.getOrientation(adjustedRotationMatrix, orientation);
		
		// Convert radians to degrees
		float azimuth = orientation[0] * -57;
		float pitch = orientation[1] * -57; 
		float roll = orientation[2] * -57; 
		
		mListener.onOrientationChanged(pitch, roll, azimuth);
	}
}
