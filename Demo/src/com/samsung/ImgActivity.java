package com.samsung;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

public class ImgActivity extends Activity {
	
	public static int RATE_OF_DECAY;
	public static int RIPPLE_RADIUS;
	public static int RIPPLE_HEIGHT;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String path = getIntent().getStringExtra("path");
        boolean threaded = getIntent().getBooleanExtra("threaded", true);
        CharSequence tmp = getIntent().getCharSequenceExtra("decay");
        try {
        	RATE_OF_DECAY =  (tmp == null) ? 5 : Integer.parseInt(tmp.toString());
        } catch(NumberFormatException e) {
        	RATE_OF_DECAY = 5;
        }
        tmp = getIntent().getCharSequenceExtra("radius");
        try {
        	RIPPLE_RADIUS =  (tmp == null) ? 3 : Integer.parseInt(tmp.toString());
        } catch(NumberFormatException e) {
        	RIPPLE_RADIUS = 3;
        }
        tmp = getIntent().getCharSequenceExtra("height");
        try {
        	RIPPLE_HEIGHT =  (tmp == null) ? 1 : Integer.parseInt(tmp.toString());
        } catch(NumberFormatException e) {
        	RIPPLE_HEIGHT = 1;
        }
        if (path==null) 
        	finish();
        
        Log.d("Test: ", RATE_OF_DECAY + " " + RIPPLE_RADIUS + " " + RIPPLE_HEIGHT);
        setContentView(R.layout.imgview);
        RelativeLayout v1 = (RelativeLayout) findViewById(R.id.LinearLayout01);
        
        View widget = (threaded) ? new FieldView_Threaded(this, path) : new FieldView(this, path);

		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        widget.setLayoutParams(lp);
        
		v1.addView(widget);

    }
}