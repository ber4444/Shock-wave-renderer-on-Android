package gabor.com.surfaceviewtweens.sprites;
import gabor.com.surfaceviewtweens.surface.SurfaceContainer;
public class Circle {

    public float radius = 1f;

    public float getRadius() { return radius; }

    public void setRadius(float s) {
        if (s <= 1f) {
            radius = 1f;
            return; // (Canvas wouldn't accept it anyway)
        } else if (s > SurfaceContainer.RADIUS) {
            radius = SurfaceContainer.RADIUS;
            return;
        } else
            radius = s;
    }
}
