package gabor.com.surfaceviewtweens.sprites;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;

import gabor.com.surfaceviewtweens.looper.LoopAdapterImpl;

public class Circle implements DrawableObject {

    private float radius = 1f;
    private Paint gradientPaint = new Paint();
    private int width, height, screenCenterX, screenCenterY;
    private final int color1 = Color.argb(255, 0,0,0);
    private final int color2 = Color.argb(0, 0,0,0);

    public Circle(int screenWidth, int screenHeight, float radius) {
        width = screenWidth; height = screenHeight;
        setRadius(radius);
        screenCenterX = width/2; screenCenterY = height/2;
        gradientPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    public float getRadius() { return radius; }

    public void setRadius(float s) {
        if (s <= 1f)
            radius = 1f;
        else if (s > LoopAdapterImpl.RADIUS)
            radius = LoopAdapterImpl.RADIUS;
        else
            radius = s;
    }

    public void draw(Canvas canvas) {
        // Create an off-screen buffer
        int layer = canvas.saveLayer(0, 0, width, height, null,
                Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG);
        canvas.drawColor(Color.BLACK);
        if (radius > 1f) {
            gradientPaint.setShader(new RadialGradient(screenCenterX, screenCenterY, radius, color1, color2, Shader.TileMode.CLAMP));
            canvas.drawCircle(screenCenterX, screenCenterY, radius, gradientPaint);
        }
        // Composite the off-screen buffer (black layer masked with transparent gradient circle) to the canvas
        canvas.restoreToCount(layer);
    }
}
