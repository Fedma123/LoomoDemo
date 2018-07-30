package com.example.feder_000.myfirstapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final int REQUEST_IMAGE_CAPTURE_HTTP = 1;
    private static final int REQUEST_IMAGE_CAPTURE_UDP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called when the user taps the Send button
     */
    public void sendHttpMessage(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_HTTP);
        }
    }

    public void sendUdpMessage(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_UDP);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView mImageView = (ImageView) findViewById(R.id.mImageView);

            String urlString = "http://";
            EditText txtIpAddress = findViewById(R.id.txtIpAddress);


            if (requestCode == REQUEST_IMAGE_CAPTURE_HTTP) {
                EditText txtPortNumber = findViewById(R.id.txtHttpPort);
                urlString += txtIpAddress.getText().toString() + ":" +
                        txtPortNumber.getText().toString() + "/";
                URL url = null;
                try {
                    url = new URL(urlString);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                SendHttpRequest(url, imageBitmap, mImageView);
            } else if (requestCode == REQUEST_IMAGE_CAPTURE_UDP){
                EditText txtPortNumber = findViewById(R.id.txtUdpPort);
                urlString += txtIpAddress.getText().toString() + ":" +
                        txtPortNumber.getText().toString() + "/";
                URL url = null;
                try {
                    url = new URL(urlString);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                SendUdpRequest(url, imageBitmap, mImageView);
            }
        }

    }

    private void SendHttpRequest(URL url, Bitmap bmp, ImageView imgView) {
        HttpNetworkTask nt = new HttpNetworkTask(bmp, imgView, this);
        nt.execute(url);
    }

    private void SendUdpRequest(URL url, Bitmap bmp, ImageView imgView) {
        UdpNetworkTask nt = new UdpNetworkTask(bmp, imgView, this);
        nt.execute(url);
    }
}
