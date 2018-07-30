package com.example.feder_000.myfirstapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

public class UdpNetworkTask extends AsyncTask<URL, Void, Bitmap> {
    Bitmap bmpImage;
    ImageView imgView;
    Context context;

    UdpNetworkTask(Bitmap bmp, ImageView imgView, Context context) {
        bmpImage = bmp;
        this.imgView = imgView;
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(URL... urls) {
        if (urls != null && urls.length == 1) {
            URL url = urls[0];
            DatagramSocket ds = null;

            try {
                ds = new DatagramSocket();
                DatagramPacket dp;

                ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
                bmpImage.compress(Bitmap.CompressFormat.PNG, 100, bmpStream);
                byte[] bmpBytes = bmpStream.toByteArray();

                InetAddress iaddr = InetAddress.getByName(url.getHost());
                dp = new DatagramPacket(bmpBytes, bmpBytes.length, iaddr, url.getPort());
                ds.send(dp);

                byte[] receivedBytes = new byte[1024 * 1024 * 5];
                DatagramPacket receivedPacket = new DatagramPacket(receivedBytes, receivedBytes.length);
                ds.receive(receivedPacket);

                byte[] response = new byte[receivedPacket.getLength()];
                System.arraycopy(receivedPacket.getData(), receivedPacket.getOffset(), response, 0, receivedPacket.getLength());

                return BitmapFactory.decodeByteArray(response, 0, response.length);
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void onPostExecute(Bitmap result) {
        if (result == null){
            Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT);
            return;
        }

        imgView.setImageBitmap(result);
    }
}
