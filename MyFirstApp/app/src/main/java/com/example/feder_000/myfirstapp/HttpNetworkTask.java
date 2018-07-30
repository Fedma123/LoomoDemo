package com.example.feder_000.myfirstapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpNetworkTask extends AsyncTask<URL, Void, Bitmap> {
    Bitmap bmpImage;
    ImageView imgView;
    Context context;

    HttpNetworkTask(Bitmap bmp, ImageView imgView, Context context){
        bmpImage = bmp;
        this.imgView = imgView;
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(URL... urls) {
        if (urls != null && urls.length == 1){
            URL url = urls[0];
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "image/png");
                urlConnection.setConnectTimeout(5000);

                ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
                bmpImage.compress(Bitmap.CompressFormat.PNG, 100, bmpStream);
                byte[] bmpBytes = bmpStream.toByteArray();

                OutputStream os = urlConnection.getOutputStream();
                os.write(bmpBytes);
                os.close();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                byte[] response = new byte[in.available()];
                in.read(response);
                return BitmapFactory.decodeByteArray(response, 0, response.length);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    @Override
    public void onPostExecute(Bitmap result){
        if (result == null){
            Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show();
            return;
        }

        imgView.setImageBitmap(result);
    }
}
