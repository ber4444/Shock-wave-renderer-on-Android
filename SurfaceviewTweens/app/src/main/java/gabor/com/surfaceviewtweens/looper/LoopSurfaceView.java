package gabor.com.surfaceviewtweens.looper;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

import gabor.com.surfaceviewtweens.sprites.DrawableObject;
import gabor.com.surfaceviewtweens.sprites.Triangle;

public final class LoopSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private InnerThread innerThread;
    private LoopAdapter adapter;

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

    /** Start the draw loop. */
    public void play() {

        if (adapter == null)
            throw new IllegalArgumentException("Can't run without adapter.");

        if (innerThread != null && innerThread.isRunning())
            return; //You can't call play() twice.

        if (innerThread == null)
            innerThread = new InnerThread();

        adapter.startAnimating();
        innerThread.start();
    }

    /** Stop the loop. You can start it again with play(). */
    public void stop() {
        if (innerThread != null && innerThread.isRunning())
            innerThread.cleanStop();
        innerThread = null;
        adapter.stopAnimating();
    }

    /** Set the adapter for this surface view. */
    public void setAdapter(LoopAdapter adapter) {
        this.adapter = adapter;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    public void surfaceCreated(SurfaceHolder holder) {}

    public void surfaceDestroyed(SurfaceHolder holder) {
        stop();
    }

    private class InnerThread extends Thread {

        private volatile boolean run = true;

        @Override
        public void run() {
            Canvas canvas;

            while (run) {
                canvas = getHolder().lockCanvas();
                if (canvas != null && adapter != null) {
                    renderTriangles(adapter.getDrawableObjects(), canvas);
                    adapter.drawForeground(canvas);
                }
                if (canvas != null)
                    getHolder().unlockCanvasAndPost(canvas);
            }
        }

        private void renderTriangles(List<Triangle> drawableObjects, Canvas canvas) {
            for (DrawableObject drawableObject : drawableObjects)
                drawableObject.draw(canvas);
        }

        public boolean isRunning() {
            return run;
        }

        public void cleanStop() {
            run = false;
        }

    }

}
