package com.example.feder_000.loomo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpBitmapNetworkTask extends AsyncTask<URL, Void, Bitmap> {
    byte[] jpegBytes;

    ImageView imgView;
    Context context;

    public HttpBitmapNetworkTask(Bitmap bmp, ImageView imgView){
        this.imgView = imgView;
        this.context = context;

        ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 94, bmpStream);
        jpegBytes = bmpStream.toByteArray();
    }

    public HttpBitmapNetworkTask(byte[] jpegBytes, ImageView imgView){
        this.jpegBytes = jpegBytes;
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
                urlConnection.setDoInput(true);

                Log.d("Debug", String.format("Sending %d bytes", jpegBytes.length));
                long start_time = System.currentTimeMillis();
                OutputStream os = urlConnection.getOutputStream();
                os.write(jpegBytes);
                os.close();
                urlConnection.connect();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                long end_time = System.currentTimeMillis();
                Log.d("Debug", String.format("RTT: %d ms", (end_time - start_time)));
                //Se chiamata in questo punto, in.available() ritorna 0 anche se nello stream
                //sono presenti i bytes del body.

                //Decodifico direttamente lo stream.
                long decode_start_time = System.currentTimeMillis();
                Bitmap result = BitmapFactory.decodeStream(in);
                long decode_end_time = System.currentTimeMillis();
                Log.d("Debug", String.format("Decode time: %d ms", (decode_end_time - decode_start_time)));
                return result;
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
            return;
        }

        imgView.setImageBitmap(Bitmap.createScaledBitmap(result, imgView.getWidth(), imgView.getHeight(), true));
    }
}