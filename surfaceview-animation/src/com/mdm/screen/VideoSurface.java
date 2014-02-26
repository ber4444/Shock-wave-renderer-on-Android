package com.mdm.screen;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import com.mdm.surface.SurfaceContainer;

public class VideoSurface extends Activity {
    LinearLayout containerBase;
    SurfaceContainer surfaceAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        containerBase = new LinearLayout(this);
        surfaceAnimation = new SurfaceContainer(this, dm.widthPixels, dm.heightPixels);
        containerBase.addView(surfaceAnimation, dm.widthPixels, dm.heightPixels);
        setContentView(containerBase);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        surfaceAnimation.terminateThread();
        System.gc();
        System.gc();
    }

    @Override
    protected void onPause() {
        super.onPause();
        surfaceAnimation.terminateThread();
        System.gc();
        System.gc();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (surfaceAnimation == null)
            return;
        if (surfaceAnimation.surfaceCreated)
            surfaceAnimation.createThread();
    }

    /* @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // rotation of the image is automatic, animations continue to work as started
        // although some devices (e.g. Galaxy S2) get confused when multitasking from landscape to portrait apps
        super.onConfigurationChanged(newConfig);
    }  */
}