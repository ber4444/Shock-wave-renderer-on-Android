package gabor.com.surfaceviewtweens.looper;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.animation.LinearInterpolator;

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
    private AnimatorSet globalAnim;

    public LoopAdapterImpl(int x, int y) {
        width = x; height = y;
    }

    public boolean startAnimating() {
        
        if (triangles == null || circle == null) 
            setup();

        int i = 0;
        ArrayList<AnimatorSet> triangleAnims = new ArrayList<>(triangles.size()*2);
        if (triangleAnims.size() == 0)
            return false;

        for (Triangle t : triangles) {
            float dx = screenCenterX - t.getX() + t.getCenter().x;
            float dy = screenCenterY - t.getY() + t.getCenter().y;
            final double NUM2 = Math.sqrt(dx * dx + dy * dy);
            float d = (float) (NUM2 * 0.04 + Math.random() * 4.0);
            float ta = (float) ((4 - t.getType()) / 3 * 0.4 + Math.random() * 0.6);
            ta = ta * ta * 0.3f;
            
            ObjectAnimator anim = ObjectAnimator.ofFloat(t, "opacity", ta);
            anim.setDuration(10);
            anim.setStartDelay((long) d);
            anim.setInterpolator(new LinearInterpolator());// TODO find TimeInterpolator (or implement a custom one) subclass equivalent for TweenMax's ease:Cubic.easeOut
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(t, "opacity", t.getOpacity());
            anim.setDuration(10);
            anim.setStartDelay((long)Math.random()*5);
            triangleAnims.get(i++).play(anim).before(anim2);

            d = (float)(Math.abs(NUM - NUM2) * 0.05 + (Math.random()-0.5) * 5);
            ta = (float) ((4 - t.getType()) / 3 * 0.4 + Math.random() * 0.6);
            ta = ta*ta*0.3f;
            ObjectAnimator anim3 = ObjectAnimator.ofFloat(t, "opacity", ta); // TODO ease:Cubic.easeIn
            anim3.setDuration(10);
            anim3.setStartDelay((long) d);
            ObjectAnimator anim4 = ObjectAnimator.ofFloat(t, "opacity", t.getOpacity()); 
            anim4.setDuration(10);
            anim4.setStartDelay((long)Math.random()*5);
            triangleAnims.get(i).setStartDelay(50);
            triangleAnims.get(i++).play(anim3).before(anim4);
        }
        globalAnim = new AnimatorSet();
        AnimatorSet first = triangleAnims.get(0);
        for (AnimatorSet a : triangleAnims)
            if (first != a)
                globalAnim.play(first).with(a);

        ObjectAnimator anim = ObjectAnimator.ofFloat(circle, "radius", RADIUS);
        anim.setDuration(50);
        anim.setInterpolator(new LinearInterpolator());// TODO find TimeInterpolator (or implement a custom one) subclass equivalent for TweenMax's ease:Cubic.easeOut
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(circle, "radius", 0);
        anim.setDuration(60);
        anim.setStartDelay(70);
        AnimatorSet circleAnim = new AnimatorSet();
        circleAnim.play(anim).before(anim2);
        globalAnim.play(first).with(circleAnim);

        globalAnim.start();
        
        return true;
    }
    
    public void stopAnimating() {
        if (globalAnim != null)
            globalAnim.cancel();
        triangles = null;
        circle = null;
    }

    private void setup() {
        screenCenterX = width/2; screenCenterY = height/2;
        RADIUS = width * 2f;
        NUM = Math.sqrt(screenCenterX * screenCenterX + screenCenterY * screenCenterY);

        triangleSize = (screenCenterX < 250) ? 44 : 50;
        int a = width / (2 * triangleSize) + triangleSize;
        int b = height / triangleSize + triangleSize;
        triangles = new ArrayList<>(a*b*4);
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
                    double opacity = light + Math.random() * 0.15;//or 0.05
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
