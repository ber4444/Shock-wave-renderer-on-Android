package gabor.com.surfaceviewtweens;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import gabor.com.surfaceviewtweens.looper.LoopAdapterImpl;
import gabor.com.surfaceviewtweens.looper.LoopSurfaceView;

public class FullscreenActivity extends Activity {

    private LoopSurfaceView contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        contentView = (LoopSurfaceView)findViewById(R.id.fullscreen_content);
        contentView.setAdapter(new LoopAdapterImpl(dm));

        findViewById(R.id.restart_button).setOnTouchListener(touchListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        contentView.stop();
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            findViewById(R.id.restart_button).setVisibility(View.GONE);
            contentView.play();
            return false;
        }
    };
}
