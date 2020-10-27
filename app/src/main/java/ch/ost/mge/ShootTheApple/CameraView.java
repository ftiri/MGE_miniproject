package ch.ost.mge.ShootTheApple;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    private Camera camera;

    public CameraView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public CameraView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            camera.release();
            camera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int width, int height, int i) {
        try {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPreviewSize(width, height);
            camera.setParameters(parameters);
        } catch(Exception e) {
        }
        camera.startPreview();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public void setOneShotPreviewCallback(Camera.PreviewCallback callback) {
        if(camera != null) {
            camera.setOneShotPreviewCallback(callback);
        }
    }
}
