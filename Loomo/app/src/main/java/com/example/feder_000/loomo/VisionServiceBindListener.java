package com.example.feder_000.loomo;

import android.util.Log;
import android.view.ViewGroup;

import com.segway.robot.sdk.base.bind.ServiceBinder;
import com.segway.robot.sdk.vision.Vision;
import com.segway.robot.sdk.vision.stream.StreamInfo;
import com.segway.robot.sdk.vision.stream.StreamType;

public class VisionServiceBindListener implements ServiceBinder.BindStateListener {

    final String TAG = "VisionServiceBind";
    private VisionColorFrameListener vcfListener;
    private Vision vision;

    public VisionServiceBindListener(Vision vision, VisionColorFrameListener vcfListener){
        this.vcfListener = vcfListener;
        this.vision = vision;
    }

    @Override
    public void onBind() {
        Log.d(TAG, "Vision service bound. Start color frame listener.");
        vision.startListenFrame(StreamType.COLOR, vcfListener);
    }

    @Override
    public void onUnbind(String reason) {
        Log.d(TAG, "Vision service unbound");
    }
}
