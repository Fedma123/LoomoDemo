package com.example.feder_000.loomo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.feder_000.loomo.HttpTask.HttpBitmapNetworkTask;
import com.example.feder_000.loomo.HttpTask.HttpStreamTask;
import com.example.feder_000.loomo.HttpTask.PostExecuteStrategy.IOnPostExecuteStrategy;
import com.example.feder_000.loomo.HttpTask.PostExecuteStrategy.UpdateImageViewStrategy;
import com.segway.robot.sdk.vision.Vision;
import com.segway.robot.sdk.vision.frame.Frame;

import java.net.URL;

public class VisionColorFrameListener implements Vision.FrameListener {

    private ImageView imgView;
    private Activity activity;
    final Bitmap mBitmap = Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888);
    ViewGroup.LayoutParams layout;
    private Integer frameCount;
    private HttpStreamTask httpNetworkTask;
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
        mBitmap.copyPixelsFromBuffer(frame.getByteBuffer());
        frameCount++;

        IOnPostExecuteStrategy strategy = new UpdateImageViewStrategy(imgView);
        httpNetworkTask = new HttpBitmapNetworkTask(strategy, serverUrl, mBitmap);
        httpNetworkTask.execute(serverUrl);
    }
}
