package com.mdm.sprites;

import aurelienribon.tweenengine.TweenAccessor;

//The TweenAccessor interface lets you interpolate any attribute from any object
public class TriangleAccessor implements TweenAccessor<Triangle> {
    // The following lines define the different possible tween types.
    // It's up to you to define what you need :-)
	public static final int POS_XY = 1;
	public static final int CPOS_XY = 2;
	public static final int SCALE_XY = 3;
	public static final int ROTATION = 4;
	public static final int ALPHA = 5;
	public static final int TINT = 6;

    // here you need to modify the "returnValues" float array with values corresponding to the given tweenType
    // plus you have to return the amount of modified array cells = number of parameters in Tween.to's ".target()"
    public int getValues(Triangle target, int tweenType, float[] returnValues) {
		switch (tweenType) {
			case POS_XY:
				returnValues[0] = target.x;
				returnValues[1] = target.y;
				return 2;
            case ALPHA:
                returnValues[0] = target.alpha;
                return 1;
			default: assert false; return -1;
		}
	}

    // here you need to update your object attributes with the given values
    public void setValues(Triangle target, int tweenType, float[] newValues) {
		switch (tweenType) {
			case POS_XY:
                target.x = (int)newValues[0];
                target.y = (int)newValues[1];
                target.createPath();
                break;
            case ALPHA: target.alpha = newValues[0]; break;
			default: assert false;
		}
	}
}
