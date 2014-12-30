package gabor.com.surfaceviewtweens.sprites;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

/*
     p0 +----/ p1
        |  /
        |/
        p2

    A triangle is rendered clockwise from p0.
    It represents the minimum unit of the Grid system.
*/
public class Triangle implements DrawableObject {
    private int x, y;
    private Paint paint = new Paint() {{
        setColor(Color.WHITE);
        setAntiAlias(true);
        setStyle(Paint.Style.FILL_AND_STROKE);
    }};
    private final int type;
    private final int size;
    private float opacity;
    private Point p0 = new Point(0,0);
    private Point p1 = new Point(0,0);
    private Point p2 = new Point(0,0);
    private Point center = new Point(0,0);
    private Path path;

    public Triangle(int type, int size, int x, int y)
    {
        this.type = type;
        this.size = size;
        this.x = x;
        this.y = y;
        createPath();
    }

    private void createPath()
    {
        p0.x = x; p0.y = y;
        if (type == 0)
        {
            p1.x = -size +x; p1.y = y;
            p2.x = x; p2.y = -size +y;
            center.x = -size /3+x; center.y = -size /3+y;
        }
        else if (type == 1)
        {
            p1.x = x; p1.y = -size +y;
            p2.x = size +x; p2.y = y;
            center.x = size /3+x; center.y = -size /3+y;

        }
        else if (type == 2)
        {
            p1.x = size +x; p1.y = y;
            p2.x = x; p2.y = size +y;
            center.x = x+ size /3; center.y = y+ size /3;
        }
        else
        {
            p1.x = x; p1.y = size +y;
            p2.x = -size +x; p2.y = y;
            center.x = -size /3+x; center.y = size /3+y;
        }

        if (path == null)
            path = new Path();
        else
            path.reset();
        path.moveTo(p0.x, p0.y);
        path.lineTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.close();
    }

    public void draw(Canvas canvas) {
        int a = (int)(255.0f * opacity);
        paint.setAlpha(a);
        if (path == null)
            createPath();
        canvas.drawPath(path, paint);
    }
    
    public int getX() { return x; }
    
    public int getY() { return y; }

    public Point getCenter()
    {
        return center;
    }

    public float getOpacity() // only called twice from Tween.to(...).target()
    {
        return opacity;
    }

    public int getType()
    {
        return type;
    }

    public void setOpacity(float value) { opacity = value; }
}
