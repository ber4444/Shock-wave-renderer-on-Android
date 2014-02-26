package com.mdm.sprites;

import aurelienribon.tweenengine.TweenAccessor;

//The TweenAccessor interface lets you interpolate any attribute from any object
public class CircleAccessor implements TweenAccessor<Circle> {
    // The following lines define the different possible tween types.
    // It's up to you to define what you need :-)
    public static final int POS_XY = 1;
    public static final int CPOS_XY = 2;
    public static final int SCALE_XY = 3;
    public static final int ROTATION = 4;
    public static final int ALPHA = 5;
    public static final int TINT = 6;
    public static final int SCALE = 7;

    // here you need to modify the "returnValues" float array with values corresponding to the given tweenType
    // plus you have to return the amount of modified array cells = number of parameters in Tween.to's ".target()"
    public int getValues(Circle target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case SCALE:
                returnValues[0] = target.getRadius();
                return 1;
            default: assert false; return -1;
        }
    }

    // here you need to update your object attributes with the given values
    public void setValues(Circle target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case SCALE: target.setRadius(newValues[0]); break;
            default: assert false;
        }
    }
}
