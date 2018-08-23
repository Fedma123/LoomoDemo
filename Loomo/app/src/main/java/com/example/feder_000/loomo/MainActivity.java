package com.example.feder_000.loomo;

import android.app.Activity;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.view.SurfaceView;
import android.widget.ImageView;
import com.segway.robot.sdk.vision.Vision;
import java.net.URL;
import java.util.Timer;

public class MainActivity extends FragmentActivity {

    private Vision vision;
    private VisionServiceBindListener vsbListener;
    private VisionColorFrameListener vcfListener;
    private URL serverHeadUrl;
    private URL serverFrontUrl;
    private Timer frameGrabTimer;

    private SurfaceView headCameraSurfaceView;
    private ImageView headCameraImageView;
    private CameraManager cameraManager;

    private long previousMillis;
    private final long frame_grab_fps = 5;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(R.layout.fragment_camera2_basic);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }



        previousMillis = System.currentTimeMillis();

        ImageView frontCameraImageView = (ImageView) findViewById(R.id.frontCameraImageView);
        vcfListener = new VisionColorFrameListener(this, frontCameraImageView, serverFrontUrl);

        vision = Vision.getInstance();

        vsbListener = new VisionServiceBindListener(vision, vcfListener);
        //vision.bindService(getApplicationContext(), vsbListener);
    }
}
