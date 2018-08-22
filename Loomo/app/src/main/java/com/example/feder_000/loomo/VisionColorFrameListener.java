package com.example.feder_000.loomo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.segway.robot.sdk.vision.Vision;
import com.segway.robot.sdk.vision.frame.Frame;

import java.net.URL;

public class VisionColorFrameListener implements Vision.FrameListener {

    private ImageView imgView;
    private Activity activity;
    final Bitmap mBitmap = Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888);
    ViewGroup.LayoutParams layout;
    private Integer frameCount;
    private HttpBitmapNetworkTask httpNetworkTask;
    private URL serverUrl;

    public VisionColorFrameListener(Activity activity, ImageView imgView, URL serverUrl){
        this.imgView = imgView;
        this.activity = activity;
        layout = null;
        frameCount = 0;
        this.serverUrl = serverUrl;
    }

    @Override
    public void onNewFrame(int streamType, final Frame frame) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    mBitmap.copyPixelsFromBuffer(frame.getByteBuffer());
                    //imgView.setImageBitmap(Bitmap.createScaledBitmap(mBitmap, imgView.getWidth(), imgView.getHeight(), true));
                    frameCount++;

                    httpNetworkTask = new HttpBitmapNetworkTask(mBitmap, imgView);
                    httpNetworkTask.execute(serverUrl);
                } catch (RuntimeException e){
                    Log.d("OnNewFrame", "Frame count " + frameCount.toString() + " " + e.getMessage());
                    //throw e;
                }
            }
        });
    }
}
