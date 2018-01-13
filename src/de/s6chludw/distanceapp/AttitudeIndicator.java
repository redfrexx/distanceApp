package de.s6chludw.distanceapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class AttitudeIndicator extends View {

	float mRoll, mPitch, mAzimuth, mHeight;
	
	public AttitudeIndicator(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public AttitudeIndicator(Context context, AttributeSet attrs) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// Account for padding
		float xpad = (float) (getPaddingLeft() + getPaddingRight()); 
		float ypad = (float) (getPaddingTop() + getPaddingBottom());
		float ww = (float) w - xpad;
		float hh = (float) h - ypad;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// Calculate a factor for the size of the canvas ...
		int x = getWidth();
        int y = getHeight();
		
        // Paint definitions
        int white = Color.parseColor("#FFFFFF"); 
        int red = Color.parseColor("#D70B0B");
        
        // Horizon 1 (white) is moved according to roll and pitch values
		Paint myPainter = new Paint(); 
		myPainter.setAntiAlias(true); 
		myPainter.setColor(white);
		myPainter.setStrokeWidth(6);
		myPainter.setAlpha(128);
		
		// Horizon 2 (red) is just moved according to roll value -> stays in the middle of the screen for distance measurement
		Paint myPainterHor = new Paint(); 
		myPainterHor.setAntiAlias(true); 
		myPainterHor.setColor(red); 
		myPainterHor.setStrokeWidth(10);
		//myPainterHor.setAlpha(200);
		
		// Text for distance value
		Paint textPainter = new Paint();
		textPainter.setAntiAlias(true); 
		textPainter.setColor(white);
		textPainter.setTextSize(100);
		textPainter.setTextAlign(Paint.Align.CENTER);
		textPainter.setAlpha(128);
		
		// Calculate distance
		float distance = (float) (mHeight / Math.tan(Math.toRadians(mPitch)));
		// Update distance value on canvas
		canvas.drawText("Distance = " + Float.toString(Math.round(Math.abs(distance))) + " m", x/2, y*19/20, textPainter);
		// Rotation according to roll value
		canvas.rotate(mRoll, x/2, y/2);
		// Draw horizon (red) for distance measurement
		canvas.drawLine(x/6, y/2, x*5/6, y/2, myPainterHor);
		// Rotation according to pitch value
		float pitch = (float) Math.sin(Math.toRadians( mPitch )) * 300;
		canvas.translate(0, pitch );
		// Draw artificial horizon (white)
				float[] pts = {x*3/8,y*9/20,x*5/8,y*9/20,x*3/8,y*8/20,x*5/8,y*8/20,x*3/8,y*7/20,x*5/8,y*7/20,
							   x/4,y/2,3*x/4,y/2,
							   x*3/8,y*11/20,x*5/8,y*11/20,x*3/8,y*12/20,x*5/8,y*12/20,x*3/8,y*13/20,x*5/8,y*13/20};
		canvas.drawLines(pts, myPainter);
		// Update compass value on canvas
		canvas.drawText(Integer.toString((int)(Math.abs(mAzimuth))) + "¡", x/2, y*13/40, textPainter);
	}
	
	public void setData(float roll, float pitch, float azimuth, float height) {
		mRoll = roll; // roll
		mPitch = pitch; // pitch
		mAzimuth = azimuth; // azimuth
		mHeight = height; // height of the device
		invalidate();
	}
	
}
