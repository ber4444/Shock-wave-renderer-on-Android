package com.mdm.surface;

import android.content.Context;
import android.graphics.*;
import android.os.SystemClock;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.*;
import com.mdm.sprites.Circle;
import com.mdm.sprites.Triangle;
import com.mdm.sprites.TriangleAccessor;
import android.graphics.PorterDuff.Mode;
import java.util.ArrayList;

import android.view.MotionEvent;

public class SurfaceContainer  extends SurfaceView implements SurfaceHolder.Callback {
	public boolean surfaceCreated;
	private SurfaceRunThread thread;
	private int frameSamplesCollected = 0;
	private int frameSampleTime = 0;
	private long mLastTime;
	private int fps = 0;
	private TweenManager tweenManager, tweenManager2;
    private ArrayList<Triangle> _triangles;
    private Circle _circle;
    private int color1, color2;
    public static float RADIUS;
    private static final int[][] LIGHT_AMOUNT = {{3, 2, 0, 1}, {3, 2, 0, 1}, {3, 2, 0, 1}, {3, 1, 0, 2}, {0, 1, 3, 2}};
    private static double NUM; // helper for the animation effect
    private int screenCenterX, screenCenterY, triangleSize, _width, _height;
    private Paint paint = new Paint() {{
        setColor(Color.WHITE);
        setAntiAlias(true);
        setStyle(Paint.Style.FILL_AND_STROKE);
    }};
    private final Paint textPaint = new Paint() {
        {
            setColor(Color.WHITE);
            setTextAlign(Paint.Align.CENTER);
            setTextSize(21f);
            setAntiAlias(true);
            setTypeface(Typeface.SANS_SERIF);
        }
    };
    private Paint gradientPaint;

    public void init(){
        triangleSize = (screenCenterX < 250) ? 44 : 50;
        int a = _width / (2 * triangleSize) + triangleSize;
        int b = _height / triangleSize + triangleSize;
        _triangles = new ArrayList<Triangle>(a*b*4);
        for (int i = 0; i < a; i++) {
            for (int j = 0; j < b; j++) {
                //int rnd = (int)(Math.random() * 5);
                //double s = Math.random() * 0.5 + 0.25;
                /*   A diamond consists of 4 triangles:
                    t4 /\ t1
                      /  \
                      \  /
                    t3 \/ t2
                */
                for (int k = 0; k < 4; k++) {
                    Point center = new Point(i * 2*triangleSize + (j % 2) * triangleSize, j * triangleSize);
                    Triangle triangle = new Triangle(k, triangleSize, center.x, center.y);
                    triangle.createPath();
                    _triangles.add(triangle);

                    //double light = LIGHT_AMOUNT[rnd][k] * s * 0.03;
                    //double opacity = light + Math.random() * 0.15;
                    //triangle.setOpacity((float)opacity);
                }
            }
        }

        gradientPaint = new Paint();
        gradientPaint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
        color1 = Color.argb(255, 0,0,0);
        color2 = Color.argb(0, 0,0,0);
        _circle = new Circle();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (canvas == null) return;
        canvas.drawColor(Color.BLACK);

        for (Triangle t : _triangles) {
            int a = (int)(255.0f * t.alpha); // TODO: optimize by moving this calculation out of onDraw()
            paint.setAlpha(a);
            canvas.drawPath(t.getPath(), paint);
        }

        /* Create an offscreen buffer
        int layer = canvas.saveLayer(0, 0, _width, _height, null,
                Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG);
        canvas.drawColor(Color.BLACK);
        if (_circle.getRadius() > 1f) {
            gradientPaint.setShader(new RadialGradient(screenCenterX, screenCenterY, _circle.getRadius(), color1, color2, Shader.TileMode.CLAMP));
            canvas.drawCircle(screenCenterX, screenCenterY, _circle.getRadius(), gradientPaint);
        }
        // Composit the offscreen buffer (black layer masked with transparent gradient circle) to the canvas
        canvas.restoreToCount(layer);
         */

        canvas.drawText("Touch to restart the animation", _width/2,15, textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch(action){
            case MotionEvent.ACTION_UP:
                synchronized (getHolder()) {
                    if (tweenManager != null) {
                        tweenManager.killAll();
                        init();
                    }
                }
                try { Thread.sleep(20); } catch (InterruptedException e) {}
                startEffect();
                break;
        }
        return true;
    }

	public SurfaceContainer(Context context, int width, int height) {
		super(context);
        screenCenterX = width/2; screenCenterY = height/2;
        _width = width; _height = height;
        RADIUS = width * 2f;
        NUM = Math.sqrt(screenCenterX * screenCenterX + screenCenterY * screenCenterY);
		getHolder().addCallback(this);
		surfaceCreated = false;
		setFocusable(true);
		tweenManager = new TweenManager();
		Tween.registerAccessor(Triangle.class, new TriangleAccessor());
        synchronized (getHolder()) {
            init();
        }
        startEffect();
        /*tweenManager2 = new TweenManager();
        Tween.registerAccessor(Circle.class, new CircleAccessor());
        Tween.set(_circle, CircleAccessor.SCALE).target(RADIUS/8).delay(0f).start(tweenManager2);
        Tween.to(_circle, CircleAccessor.SCALE, 50).delay(0.1f).target(RADIUS).start(tweenManager2);
        Tween.to(_circle, CircleAccessor.SCALE, 60).delay(70f).ease(Cubic.OUT).target(0).start(tweenManager2);
        */
    }

    public void updatePhysics() {
    	long now = System.currentTimeMillis();
		tweenManager.update(SystemClock.elapsedRealtime()%2);
//        tweenManager2.update(SystemClock.elapsedRealtime()%2);
		if (mLastTime != 0) {
			// Time difference between now and last time we were here
			int time = (int) (now - mLastTime);
			frameSampleTime += time;
			frameSamplesCollected++;
			// After 10 frames
			if (frameSamplesCollected == 10) {
				// Update the fps variable
				fps = 10000 / frameSampleTime;
				// Reset the sampletime + frames collected
				frameSampleTime = 0;
				frameSamplesCollected = 0;
			}
		}
		mLastTime = now;
    }

    public class Disturb implements Runnable {
        private Triangle t;
        public Disturb(Triangle triangle) {
            t = triangle;
        }
        public void run() {
            synchronized (getHolder()) {
                float dx = screenCenterX - t.x + t.getCenter().x;
                float dy = screenCenterY - t.y + t.getCenter().y;
                //Log.e(" =====================> ", "starting with " + screenCenterX + "x" + screenCenterY +" "+dx+"x"+dy);
                float d = (float)(Math.sqrt(dx * dx + dy * dy) * 0.04 + Math.random() * 4.0);
                float ta = (float)((4 - t.getType()) / 3 * 0.4 + Math.random() * 0.6);
                ta = ta*ta*0.3f;
                // was 10 and d
                Tween.to(t, TriangleAccessor.ALPHA, 30f).delay(d).ease(Cubic.OUT).target(ta).start(tweenManager);

                // Suspend this thread via sleep() and yield control to other threads.
                // Also provide the necessary delay.
                try {
                    Thread.sleep(10);  // milliseconds
                } catch (InterruptedException ex) {}

            }
        }
    }

    private void startEffect() {
        //for (Triangle t : _triangles) {
        for (int i = 0; i < 150; i++) {
            Triangle t = _triangles.get(i);
            (new Thread(new Disturb(t))).start();  // TODO: thread pool, otherwise runs out of memory



            //.setCallback(innerWaveEffect).setCallbackTriggers(TweenCallback.COMPLETE).setUserData(t).start(tweenManager);

            // --> when iterating through 50 items, it's fine, but at 250 there's substential difference

                /* AS3 values:
                t (-16,-16) |   | dx/y (343,623)
                t (16,-16)  |   | dx   (376,
                   16,16                   ,656
                   -16,16               343,

                  Android values:
                    -10,-10      374,582
                    10,-10       394,
                     10,10           ,602
                     -10,10      374,

                 */

                /*timeLine.pushPause(50f);
                d = (float)(Math.abs(NUM - Math.sqrt(dx * dx + dy * dy)) * 0.05 + (Math.random()-0.5) * 5);
                ta = (float)((4 - t.getType()) / 3) * .4 + Math.random() * .6);
                ta = ta*ta*0.3f;
                timeLine.beginParallel();
                timeLine.push(Tween.to(t, TriangleAccessor.ALPHA, 10).ease(Cubic.IN).target(ta).delay(d));
                timeLine.end();
                timeLine.beginParallel();
                timeLine.push(Tween.to(t, TriangleAccessor.ALPHA, 10).target(t.getOpacity()).delay((float)Math.random()*5f));
                timeLine.end();                                   */
        }
	}

    private final TweenCallback innerWaveEffect = new TweenCallback() {
        @Override
        public void onEvent(int type, BaseTween source) {
            if (type != TweenCallback.SYNC_COMPLETE) return;
            Triangle t = (Triangle) source.getUserData();
            //Log.e(" =====================> ", "callback at " + t.y + "x" + t.y);
            //Tween.to(t, TriangleAccessor.ALPHA, 10f).target(t.getOpacity()).delay((float)Math.random()*5f).start(tweenManager);
        }
    };

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (!surfaceCreated) {
            createThread();
            surfaceCreated = true;
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        thread.interrupt();
        surfaceCreated = false;
    }

    public void createThread() {
        thread = new SurfaceRunThread(this);
        thread.setRunning(true);
        thread.start();
    }

    public void terminateThread() {
        boolean retry = true;
        if (thread == null) return;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {}
        }
    }
}
