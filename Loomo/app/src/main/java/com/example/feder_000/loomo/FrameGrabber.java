package com.example.feder_000.loomo;

import com.wonderkiln.camerakit.CameraView;

import java.util.TimerTask;

public class FrameGrabber extends TimerTask {
    private CameraView cameraView;

    public FrameGrabber(CameraView cameraView){
        this.cameraView = cameraView;
    }

    @Override
    public void run() {
        cameraView.captureImage();
    }
}
