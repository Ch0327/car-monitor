package com.example.carmonitor;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    // 直接在内部声明厂家的底层驱动方法，避免多文件夹路径出错
    public static class QCarCamera {
        static {
            System.loadLibrary("mmqcar_qcar_jni");
        }
        public native int cameraOpen(int csi, int channel, int flag);
        public native int cameraClose(int csi, int channel);
        public native int setVideoSize(int csi, int channel, int width, int height);
        public native int setFps(int csi, int channel, int fps);
        public native int startPreview(int csi, int channel, Surface surface);
        public native int stopPreview(int csi, int channel);
    }

    private QCarCamera mCameraAPI;
    private SurfaceView[] surfaceViews = new SurfaceView[4];
    private boolean[] isSurfaceReady = new boolean[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mCameraAPI = new QCarCamera();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }

        initSurfaces();
    }

    private void initSurfaces() {
        surfaceViews[0] = findViewById(R.id.surfaceView0);
        surfaceViews[1] = findViewById(R.id.surfaceView1);
        surfaceViews[2] = findViewById(R.id.surfaceView2);
        surfaceViews[3] = findViewById(R.id.surfaceView3);

        for (int i = 0; i < 4; i++) {
            final int channel = i;
            surfaceViews[i].getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    isSurfaceReady[channel] = true;
                    startAHDCamera(channel, holder.getSurface());
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    isSurfaceReady[channel] = false;
                    mCameraAPI.stopPreview(0, channel);
                    mCameraAPI.cameraClose(0, channel);
                }
            });
        }
    }

    private void startAHDCamera(int channel, Surface surface) {
        new Thread(() -> {
            int csiNum = 0;
            mCameraAPI.cameraOpen(csiNum, channel, 0);
            mCameraAPI.setVideoSize(csiNum, channel, 1280, 720);
            mCameraAPI.setFps(csiNum, channel, 25);
            mCameraAPI.startPreview(csiNum, channel, surface);
        }).start();
    }
}
