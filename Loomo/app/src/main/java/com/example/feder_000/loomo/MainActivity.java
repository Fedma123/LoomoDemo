package com.example.feder_000.loomo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.segway.robot.sdk.vision.Vision;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;

public class MainActivity extends Activity {

    private Vision vision;
    private VisionServiceBindListener vsbListener;
    private VisionColorFrameListener vcfListener;
    private URL serverUrl;
    private Timer frameGrabTimer;

    private CameraView headCameraView;
    private ImageView headCameraImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            serverUrl = new URL("http://192.168.43.168:49000");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        headCameraView = (CameraView) findViewById(R.id.headCameraPreview);
        headCameraView.setMethod(CameraKit.Constants.METHOD_STILL);
        headCameraView.setCropOutput(true);

        headCameraImageView = (ImageView) findViewById(R.id.headCameraImageView);
        frameGrabTimer = new Timer();

        headCameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {
               if (cameraKitEvent.getType() == CameraKitEvent.TYPE_CAMERA_OPEN){
                   frameGrabTimer.schedule(new FrameGrabber(headCameraView), 3000, 500);
                }
            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                if (cameraKitImage != null){
                    Bitmap bmp = cameraKitImage.getBitmap();

                    HttpNetworkTask nt = new HttpNetworkTask(bmp, headCameraImageView);
                    nt.execute(serverUrl);
                }
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

        ImageView frontCameraImageView = (ImageView) findViewById(R.id.frontCameraImageView);
        vcfListener = new VisionColorFrameListener(this, frontCameraImageView, serverUrl);

        vision = Vision.getInstance();

        vsbListener = new VisionServiceBindListener(vision, vcfListener);
        vision.bindService(getApplicationContext(), vsbListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        headCameraView.start();
    }

    @Override
    protected void onPause() {
        headCameraView.stop();
        super.onPause();
    }
}
