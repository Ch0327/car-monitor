package com.quectel.qcarapi;

import android.view.Surface;

public class QCarCamera {
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
