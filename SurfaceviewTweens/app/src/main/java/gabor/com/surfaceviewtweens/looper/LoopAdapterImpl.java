package gabor.com.surfaceviewtweens.looper;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

import java.util.ArrayList;

import gabor.com.surfaceviewtweens.sprites.Circle;
import gabor.com.surfaceviewtweens.sprites.Triangle;

public class LoopAdapterImpl implements LoopAdapter {

    private int screenCenterX, screenCenterY, triangleSize, width, height;
    private static double NUM; // helper for the animation effect
    public static float RADIUS;
    private static final int[][] LIGHT_AMOUNT = {{3, 2, 0, 1}, {3, 2, 0, 1}, {3, 2, 0, 1}, {3, 1, 0, 2}, {0, 1, 3, 2}};
    private ArrayList<Triangle> triangles;
    private Circle circle;

    public LoopAdapterImpl(DisplayMetrics dm) {
        width = dm.widthPixels; height = dm.heightPixels;
        triangleSize = Math.round(10 * (dm.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void startAnimating() {
        
        if (triangles == null || circle == null) 
            setup();

        ObjectAnimator anim0 = ObjectAnimator.ofFloat(circle, "radius", RADIUS);
        anim0.setDuration(5000);
        ObjectAnimator anim02 = ObjectAnimator.ofFloat(circle, "radius", 0);
        anim02.setDuration(6000);
        anim02.setStartDelay(7000);
        anim02.setInterpolator(new AccelerateDecelerateInterpolator());
        AnimatorSet circleAnim = new AnimatorSet();
        circleAnim.play(anim0).before(anim02);
        circleAnim.start();
        
        for (Triangle t : triangles) {
            float dx = screenCenterX - t.getX() + t.getCenter().x;
            float dy = screenCenterY - t.getY() + t.getCenter().y;
            final double NUM2 = Math.sqrt(dx * dx + dy * dy);
            float d = (float) (NUM2 * 0.04 + Math.random() * 4.0);
            float ta = (float) ((4 - t.getType()) / 3 * 0.4 + Math.random() * 0.6);
            ta = ta * ta * 0.3f;
            
            ObjectAnimator anim = ObjectAnimator.ofFloat(t, "opacity", ta);
            anim.setDuration(1000);
            anim.setStartDelay((long)(d*100));
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(t, "opacity", t.getOpacity());
            anim2.setDuration(1000);
            anim2.setStartDelay((long)(Math.random()*500));
            AnimatorSet set1 = new AnimatorSet();
            set1.play(anim).before(anim2);

            d = (float)(Math.abs(NUM - NUM2) * 0.05 + (Math.random()-0.5) * 5);
            ta = (float) ((4 - t.getType()) / 3 * 0.4 + Math.random() * 0.6);
            ta = ta*ta*0.3f;
            ObjectAnimator anim3 = ObjectAnimator.ofFloat(t, "opacity", ta); 
            anim3.setDuration(1000);
            anim3.setStartDelay((long)(d*100));
            anim3.setInterpolator(new AccelerateInterpolator());
            ObjectAnimator anim4 = ObjectAnimator.ofFloat(t, "opacity", t.getOpacity()); 
            anim4.setDuration(1000);
            anim4.setStartDelay((long)(Math.random()*500));
            AnimatorSet set2 = new AnimatorSet();
            set2.setStartDelay(5000);
            set2.play(anim3).before(anim4);

            set1.start();
            set2.start();
        }
    }
    
    public void stopAnimating() {
        // TODO stop animations
        triangles = null;
        circle = null;
    }

    private void setup() {
        screenCenterX = width/2; screenCenterY = height/2;
        RADIUS = width * 2f;
        NUM = Math.sqrt(screenCenterX * screenCenterX + screenCenterY * screenCenterY);

        int a = width / (2*triangleSize);
        int b = height / triangleSize;
        triangles = new ArrayList<>();
        for (int i = 0; i < a; i++) {
            for (int j = 0; j < b; j++) {
                int rnd = (int)(Math.random() * LIGHT_AMOUNT.length);
                double s = Math.random() * 0.5 + 0.25;
                /*   A diamond consists of 4 triangles:
                    t4 /\ t1
                      /  \
                      \  /
                    t3 \/ t2
                */
                for (int k = 0; k < 4; k++) {
                    Point center = new Point(i * 2*triangleSize + (j % 2) * triangleSize, j * triangleSize);
                    Triangle triangle = new Triangle(k, triangleSize, center.x, center.y);

                    double light = LIGHT_AMOUNT[rnd][k] * s * 0.03;
                    double opacity = light + Math.random() * 0.05;
                    triangle.setOpacity((float)opacity);

                    triangles.add(triangle);
                }
            }
        }

        circle = new Circle(width, height, RADIUS / 8f);
    }

    public ArrayList<Triangle> getDrawableObjects() {
        return triangles;
    }

    public void drawForeground(Canvas canvas) {
        circle.draw(canvas);
    }

}
