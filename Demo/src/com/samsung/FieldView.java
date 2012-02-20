package com.samsung;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.samsung.image.AnimDrawable;
import com.samsung.image.BitmapScaler;

public class FieldView extends View {

	private AnimDrawable mDrawable;

	public FieldView(Context context, String path) {
		super(context);

		setFocusable(true);
		setFocusableInTouchMode(true);
	
		try {
			Display display = ((WindowManager) context.getSystemService(Activity.WINDOW_SERVICE)).getDefaultDisplay();
			BitmapScaler scaler = new BitmapScaler(new File(path), display.getWidth());
			Bitmap bm = scaler.getScaled();
			mDrawable = new AnimDrawable(new BitmapDrawable(bm), bm);
		} catch (IOException e) {
			Log.e("Demo", "Oops, image couldn't be scaled");
		}
	}

	@Override protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		if (mDrawable != null)
			mDrawable.draw(canvas);
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mDrawable != null)
			mDrawable.click(event.getX(), event.getY());
		return true;
	}
}
