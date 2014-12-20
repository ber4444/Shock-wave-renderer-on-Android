package gabor.com.surfaceviewtweens.looper;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public final class LoopSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = LoopSurfaceView.class.getSimpleName();

    private InnerThread innerThread;

    private LoopAdapter adapter;

    public boolean surfaceCreated = false;

    public LoopSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public LoopSurfaceView(Context context, AttributeSet attrs) {

        super(context, attrs);
        getHolder().addCallback(this);

    }

    public LoopSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getHolder().addCallback(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO
    }

    /** Start the update/draw loop. */
    public void play() {

        if (adapter == null)
            throw new IllegalArgumentException("Can't run without adapter.");

        // If the thread is running the surface view has been started before
        // and has never been stopped.
        if (innerThread != null && innerThread.isRunning()) {
            throw new IllegalStateException("You can't call play() twice.");
        }

        innerThread = new InnerThread();
        innerThread.start();

    }

    /** Stop the loop. You can start it again with play(). */
    public void stop() {
        innerThread.cleanStop();
        innerThread = null;
    }

    /** Set the adapter for this surface view. */
    public void setAdapter(LoopAdapter adapter) {
        this.adapter = adapter;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (!surfaceCreated) {
            play();
            surfaceCreated = true;
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        stop();
        surfaceCreated = false;
    }

    private class InnerThread extends Thread {

        private volatile boolean run = true;

        @Override
        public void run() {
            while (run) {

                Canvas canvas = null;

                try {
                    canvas = getHolder().lockCanvas();
                    if (canvas != null) { // don't we need synchronized (getHolder()) here ?
                        adapter.update(0);
                        adapter.drawBackground(canvas);
                        renderObjects(adapter.getDrawableObjects(), canvas);
                    }
                    Thread.sleep(500);//or 10
                } catch (InterruptedException e) {
                    Log.e(TAG, "Interrupted while sleeping.", e);

                } finally {
                    if (canvas != null) {
                        getHolder().unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        private void renderObjects(List<DrawableObject> drawableObjects, Canvas canvas) {
            for (DrawableObject drawableObject : drawableObjects) {
                drawableObject.draw(canvas);
            }
        }

        public boolean isRunning() {
            return run;
        }

        public void cleanStop() {
            run = false;
        }

    }

}
