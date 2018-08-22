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
    private URL serverHeadUrl;
    private URL serverFrontUrl;
    private Timer frameGrabTimer;

    private CameraView headCameraView;
    private ImageView headCameraImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            String serverIpAddress = "192.168.43.168";
            String serverPort = "49000";
            serverHeadUrl = new URL("http://" + serverIpAddress + ":" + serverPort + "/head");
            serverFrontUrl = new URL("http://" + serverIpAddress + ":" + serverPort + "/front");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        headCameraView = (CameraView) findViewById(R.id.headCameraPreview);
        headCameraView.setMethod(CameraKit.Constants.METHOD_STILL);
        headCameraView.setCropOutput(false);
        headCameraView.setJpegQuality(50);

        headCameraImageView = (ImageView) findViewById(R.id.headCameraImageView);
        frameGrabTimer = new Timer();

        headCameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {
               if (cameraKitEvent.getType() == CameraKitEvent.TYPE_CAMERA_OPEN){
                   frameGrabTimer.schedule(new FrameGrabber(headCameraView), 3000, 400);
                }
            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                if (cameraKitImage != null){
                    byte[] jpegBytes = cameraKitImage.getJpeg();

                    HttpNetworkTask nt = new HttpNetworkTask(jpegBytes, headCameraImageView);
                    nt.execute(serverHeadUrl);
                }
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

        ImageView frontCameraImageView = (ImageView) findViewById(R.id.frontCameraImageView);
        vcfListener = new VisionColorFrameListener(this, frontCameraImageView, serverFrontUrl);

        vision = Vision.getInstance();

        vsbListener = new VisionServiceBindListener(vision, vcfListener);
        //vision.bindService(getApplicationContext(), vsbListener);
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
