package com.mdm.sprites;

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
public class Triangle
{
    // Triangle is a native Shape object in the original code and these are its native properties
    // we access them directly and not via getter methods -- for speed:
    public int x;
    public int y;
    public float alpha; //Valid values are 0 (fully transparent) to 1 (fully opaque).
    // ------- end if native properties

    private final int _type;
    private final int _size;
    private float _opacity;   // confusingly, this is a different property than alpha
    private Point _p0 = new Point(0,0);
    private Point _p1 = new Point(0,0);
    private Point _p2 = new Point(0,0);
    private Point _center = new Point(0,0);
    private Path path;

    public Triangle(int type, int size, int X, int Y)
    {
        _type = type;
        _size = size;
        // note that x,y are initial values, they will be changed with the tween animation (via TriangleAccessor)
        x = X;
        y = Y;
        createPath();
    }

    public void createPath()
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

    public Path getPath() {
        if (path == null)
            createPath();
        return path;
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

    public void setOpacity(float value)
    {
        _opacity = value;
        alpha = value;
    }
}
