package com.samsung;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.samsung.image.AnimBitmap_Threaded;
import com.samsung.image.BitmapScaler;
import com.samsung.image.Core;

public class FieldView_Threaded extends SurfaceView implements SurfaceHolder.Callback {

	private AnimBitmap_Threaded thread;

	public FieldView_Threaded(Context context, String path) {
		super(context);

		// register our interest in hearing about changes to our surface
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);

		Bitmap bm;
		try {
			Display display = ((WindowManager) context.getSystemService(Activity.WINDOW_SERVICE)).getDefaultDisplay();
			BitmapScaler scaler = new BitmapScaler(new File(path), display.getWidth());
			bm = scaler.getScaled();
		} catch (IOException e) {
			Log.e("Demo", "Oops, image couldn't be scaled");
			return;
		}

		// create thread only; it's started in surfaceCreated() 
		thread = new AnimBitmap_Threaded(holder, new Core(bm));

		setFocusable(true); 
		setFocusableInTouchMode(true);
	}
	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (thread != null)
        	thread.onTouchEvent((int) event.getX(), (int) event.getY());
        return true;
    }

	public void surfaceCreated(SurfaceHolder holder) {
		// start the thread here so that we don't busy-wait in run()
		// waiting for the surface to be created
        if (thread != null && !thread.isAlive())
        	thread.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
        thread.interrupt();
    }
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}
}