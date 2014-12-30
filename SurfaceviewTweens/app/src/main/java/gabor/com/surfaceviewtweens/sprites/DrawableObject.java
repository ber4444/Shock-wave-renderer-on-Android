package gabor.com.surfaceviewtweens.sprites;

import android.graphics.Canvas;

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
