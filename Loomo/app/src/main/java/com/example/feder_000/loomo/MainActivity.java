package com.example.feder_000.loomo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.segway.robot.sdk.base.bind.ServiceBinder;
import com.segway.robot.sdk.vision.Vision;
import com.segway.robot.sdk.vision.frame.Frame;
import com.segway.robot.sdk.vision.stream.StreamType;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity {

    private Vision vision;
    private VisionServiceBindListener vsbListener;
    private VisionColorFrameListener vcfListener;
    private URL serverUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            serverUrl = new URL("http://192.168.1.2:49000");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ImageView rgbFrameView = (ImageView) findViewById(R.id.rgbFrameView);
        vcfListener = new VisionColorFrameListener(this, rgbFrameView, serverUrl);

        vision = Vision.getInstance();

        vsbListener = new VisionServiceBindListener(vision, vcfListener);
        vision.bindService(getApplicationContext(), vsbListener);
    }
}
