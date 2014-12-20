package gabor.com.surfaceviewtweens.looper;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;

public interface LoopAdapter {

    public void update(long elapsedTime);
    
    public ArrayList<DrawableObject> getDrawableObjects();
    
    public void drawBackground(Canvas canvas);

}
