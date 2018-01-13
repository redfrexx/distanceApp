package de.s6chludw.distanceapp;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Preview extends SurfaceView implements SurfaceHolder.Callback {
	
	SurfaceHolder mHolder;
	Camera mCamera;
	
	public Preview(Context context, Camera camera) {
		super(context);
		mCamera = camera;

		mHolder = getHolder();
		mHolder.addCallback(this);
		//mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// not used
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	      try {
	    	  mCamera.setPreviewDisplay(mHolder);
	          mCamera.startPreview();
	      } catch (Exception d){
	          Log.d("Camera", "failed to open camera");
	      }; 
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {		
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {		
	}
	
}
