package gabor.com.surfaceviewtweens.sprites;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;

import gabor.com.surfaceviewtweens.looper.DrawableObject;

/*
     p0 +----/ p1
        |  /
        |/
        p2

    A triangle is rendered clockwise from p0.
    It represents the minimum unit of the Grid system.
*/
public class Triangle implements DrawableObject {
    public int x;
    public int y; // x and y should change during the course of the animation - TODO
    private Paint paint = new Paint() {{
        setColor(Color.WHITE);
        setAntiAlias(true);
        setStyle(Paint.Style.FILL_AND_STROKE);
    }};
    private final int _type;
    private final int _size;
    private float _opacity;   //Valid values are 0 (fully transparent) to 1 (fully opaque).
    private Point _p0 = new Point(0,0);
    private Point _p1 = new Point(0,0);
    private Point _p2 = new Point(0,0);
    private Point _center = new Point(0,0);
    private Path path;

    public Triangle(int type, int size, int X, int Y)
    {
        _type = type;
        _size = size;
        x = X;
        y = Y;
        createPath();
    }

    private void createPath()
    {
        _p0.x = x; _p0.y = y;
        if (_type == 0)
        {
            _p1.x = -_size+x; _p1.y = y;
            _p2.x = x; _p2.y = -_size+y;
            _center.x = -_size/3+x; _center.y = -_size/3+y;
        }
        else if (_type == 1)
        {
            _p1.x = x; _p1.y = -_size+y;
            _p2.x = _size+x; _p2.y = y;
            _center.x = _size/3+x; _center.y = -_size/3+y;

        }
        else if (_type == 2)
        {
            _p1.x = _size+x; _p1.y = y;
            _p2.x = x; _p2.y = _size+y;
            _center.x = x+_size/3; _center.y = y+_size/3;
        }
        else
        {
            _p1.x = x; _p1.y = _size+y;
            _p2.x = -_size+x; _p2.y = y;
            _center.x = -_size/3+x; _center.y = _size/3+y;
        }

        if (path == null)
            path = new Path();
        else
            path.reset();
        path.moveTo(_p0.x, _p0.y);
        path.lineTo(_p1.x, _p1.y);
        path.lineTo(_p2.x, _p2.y);
        path.close();
    }

    private Path getPath() {
        if (path == null)
            createPath();
        return path;
    }

    public void draw(Canvas canvas) {
        int a = (int)(255.0f * _opacity);
        paint.setAlpha(a);
        canvas.drawPath(getPath(), paint);
    }

    public Point getCenter()
    {
        return _center;
    }

    public float getOpacity() // only called twice from Tween.to(...).target()
    {
        return _opacity;
    }

    public int getType()
    {
        return _type;
    }

    public void setOpacity(float value) { _opacity = value; }
}
