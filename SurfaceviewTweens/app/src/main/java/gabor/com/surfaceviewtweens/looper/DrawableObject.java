package gabor.com.surfaceviewtweens.looper;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * 
 * All object that is to be drawn (except the background) needs to implement
 * DrawableObject.
 */
public interface DrawableObject {

	/**
	 * Draw itself on the given canvas.
	 */
    void draw(Canvas canvas);

}
