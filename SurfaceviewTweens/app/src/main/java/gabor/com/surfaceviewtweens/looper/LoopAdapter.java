package gabor.com.surfaceviewtweens.looper;

import android.graphics.Canvas;

import java.util.ArrayList;

import gabor.com.surfaceviewtweens.sprites.Triangle;

public interface LoopAdapter {

    public boolean startAnimating();
    
    public void stopAnimating();
    
    public ArrayList<Triangle> getDrawableObjects(); // where Triangle can be replaced by any DrawableObject
    
    public void drawForeground(Canvas canvas);

}
