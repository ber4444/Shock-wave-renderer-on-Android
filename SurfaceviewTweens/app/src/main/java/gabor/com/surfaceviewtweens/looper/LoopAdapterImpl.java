package gabor.com.surfaceviewtweens.looper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.List;
import gabor.com.surfaceviewtweens.sprites.Triangle;

public class LoopAdapterImpl implements LoopAdapter {

    private int screenCenterX, screenCenterY, triangleSize, width, height;
    private static double NUM; // helper for the animation effect
    public static float RADIUS;
    private static final int[][] LIGHT_AMOUNT = {{3, 2, 0, 1}, {3, 2, 0, 1}, {3, 2, 0, 1}, {3, 1, 0, 2}, {0, 1, 3, 2}};
    private ArrayList<DrawableObject> _triangles;

    public LoopAdapterImpl(int x, int y) {
        width = x; height = y;
    }

    public void update(long elapsedTime) {

        for (Triangle t : _triangles) {
            float dx = screenCenterX - t.x + t.getCenter().x;
            float dy = screenCenterY - t.y + t.getCenter().y;
            final double NUM2 = Math.sqrt(dx * dx + dy * dy);
            float d = (float) (NUM2 * 0.04 + Math.random() * 4.0);
            float ta = (float) ((4 - t.getType()) / 3 * 0.4 + Math.random() * 0.6);
            ta = ta * ta * 0.3f;
            Tween.to(t, TriangleAccessor.ALPHA, 10f).delay(d).ease(Cubic.OUT).target(ta);
            // and another one, after this tween completed:
            Tween.to(t, TriangleAccessor.ALPHA, 10f).target(t.getOpacity()).
                    delay((float)Math.random()*5f);
            // this third one is started at the same time as the first one, but with a fixed delay
            timeLine.pushPause(50f);
            d = (float)(Math.abs(NUM - NUM2) * 0.05 + (Math.random()-0.5) * 5);
            ta = (float) ((4 - t.getType()) / 3 * 0.4 + Math.random() * 0.6);
            ta = ta*ta*0.3f;
            Tween.to(t, TriangleAccessor.ALPHA, 10f).ease(Cubic.IN).target(ta).delay(d);
            // and another one, after this tween completed:
            Tween.to(t, TriangleAccessor.ALPHA, 10f).target(t.getOpacity()).
                    delay((float)Math.random()*5f);
        }
/* Property animation:
define duration (def. 300ms), interpolation, repeating or not, sets in parallel or sequential, refresh rate (def. 10ms)
       ObjectAnimator         TypeInterpolator e.g. LinearInterpolator
                               TypeEvaluator [how to calculate] e.g. IntEvaluator
 */

        /*scaling:             circle.setRadius(x);
        Tween.set(_circle, CircleAccessor.SCALE).target(RADIUS/8).delay(0f).start(tweenManager2);
        Tween.to(_circle, CircleAccessor.SCALE, 50).delay(0.1f).target(RADIUS).start(tweenManager2);
        Tween.to(_circle, CircleAccessor.SCALE, 60).delay(70f).ease(Cubic.OUT).target(0).start(tweenManager2);
        */
    }

    public ArrayList<DrawableObject> getDrawableObjects() {
        screenCenterX = width/2; screenCenterY = height/2;
        RADIUS = width * 2f;
        NUM = Math.sqrt(screenCenterX * screenCenterX + screenCenterY * screenCenterY);

        triangleSize = (screenCenterX < 250) ? 44 : 50;
        int a = width / (2 * triangleSize) + triangleSize;
        int b = height / triangleSize + triangleSize;
        _triangles = new ArrayList<>(a*b*4);
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

                    _triangles.add(triangle);
                }
            }
        }

        return _triangles; // TODO: new Circle(width, height);
    }

    public void drawBackground(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
    }

}
