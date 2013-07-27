package com.samsung;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.content.res.Configuration;

public class ImgActivity extends Activity {
	
	public static int RATE_OF_DECAY;
	public static int RIPPLE_RADIUS;
	public static int RIPPLE_HEIGHT;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String path = getIntent().getStringExtra("path");
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
        
        View widget = new FieldView_Threaded(this, path);

		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        widget.setLayoutParams(lp);
        
		v1.addView(widget);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // rotation of the image is automatic, animations continue to work as started
        // although some devices (e.g. Galaxy S2) get confused when multitasking from landscape to portrait apps
        super.onConfigurationChanged(newConfig);
    }
}