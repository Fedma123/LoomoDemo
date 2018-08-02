package com.example.feder_000.loomo;

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

    HttpNetworkTask(Bitmap bmp, ImageView imgView){
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
                urlConnection.setDoInput(true);


                ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
                bmpImage.compress(Bitmap.CompressFormat.JPEG, 94, bmpStream);
                byte[] bmpBytes = bmpStream.toByteArray();

                OutputStream os = urlConnection.getOutputStream();
                os.write(bmpBytes);
                os.close();
                urlConnection.connect();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                //Se chiamata in questo punto, in.available() ritorna 0 anche se nello stream
                //sono presenti i bytes del body.

                //Decodifico direttamente lo stream.
                Bitmap result = BitmapFactory.decodeStream(in);
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