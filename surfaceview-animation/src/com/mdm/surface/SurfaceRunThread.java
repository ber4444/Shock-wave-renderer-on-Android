package com.mdm.surface;

import android.graphics.Canvas;

public class SurfaceRunThread  extends Thread{
	private final SurfaceContainer panel;
	private boolean run = false;

    public SurfaceRunThread(SurfaceContainer pan) {
        panel = pan;
    }

    public void setRunning(boolean Run) {
        run = Run;
    }

    @Override
    public void run() {
        Canvas c;
        while (run) {
            c = null;
            try {
                c = panel.getHolder().lockCanvas(null);
                if (c != null)
                    synchronized (panel.getHolder()) {
                        panel.updatePhysics();
                        panel.onDraw(c);
                    }
            } finally {
                if (c != null)
                    panel.getHolder().unlockCanvasAndPost(c);
            }
            // adjust to a normal frame rate in order to avoid busy-waiting:
            // try { Thread.sleep(10); } catch (Exception e) {}
        }
    }
}
