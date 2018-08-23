package com.example.feder_000.loomo.HttpTask;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.feder_000.loomo.HttpTask.PostExecuteStrategy.IOnPostExecuteStrategy;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//E' stato necessario utilizzare la classe AsyncTask mettendo come primo tipo
//generico Object e non URL perché altrimenti viene lanciata un'eccezione
//di tipo ClassCastException che dice che non è possibile eseguire il cast
//da Object[] a URL[]. L'URL del server lo passo al costruttore della classe.
public abstract class HttpStreamTask<ResultType> extends AsyncTask<Object, Void, ResultType> {

    private byte[] jpegBytes;
    private int jpegQuality;
    private static final int defaultJpegQuality = 94;
    private IOnPostExecuteStrategy<ResultType> executeStrategy;
    private URL serverUrl;

    public abstract ResultType decodeStream(InputStream inputStream);

    public HttpStreamTask(@NonNull IOnPostExecuteStrategy<ResultType> executeStrategy,
                          @NonNull URL serverUrl,
                          @NonNull Bitmap bmp){
        this(executeStrategy, serverUrl, bmp, defaultJpegQuality);
    }

    public HttpStreamTask(@NonNull IOnPostExecuteStrategy<ResultType> executeStrategy,
                          @NonNull URL serverUrl,
                          @NonNull Bitmap bmp,
                          int jpegQuality){
        this.jpegQuality = jpegQuality;
        this.executeStrategy = executeStrategy;
        this.serverUrl = serverUrl;

        ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, jpegQuality, bmpStream);
        jpegBytes = bmpStream.toByteArray();
    }

    public HttpStreamTask(@NonNull IOnPostExecuteStrategy<ResultType> executeStrategy,
                          @NonNull URL serverUrl,
                          @NonNull byte[] jpegBytes){
        this(executeStrategy, serverUrl, jpegBytes, defaultJpegQuality);
    }

    public HttpStreamTask(@NonNull IOnPostExecuteStrategy<ResultType> executeStrategy,
                          @NonNull URL serverUrl,
                          @NonNull byte[] jpegBytes,
                          int jpegQuality){
        this.jpegQuality = jpegQuality;
        this.jpegBytes = jpegBytes;
        this.executeStrategy = executeStrategy;
        this.serverUrl = serverUrl;
    }

    @Override
    protected ResultType doInBackground(Object... unused){

        HttpURLConnection urlConnection = null;
        ResultType result = null;

        try {
            urlConnection = (HttpURLConnection) serverUrl.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "image/png");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setDoInput(true);

            //Log.d("Debug", String.format("Sending %d bytes", jpegBytes.length));
            //long start_time = System.currentTimeMillis();
            OutputStream os = urlConnection.getOutputStream();
            os.write(jpegBytes);
            os.close();
            urlConnection.connect();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            //long end_time = System.currentTimeMillis();
            //Log.d("Debug", String.format("RTT: %d ms", (end_time - start_time)));
            //Se chiamata in questo punto, in.available() ritorna 0 anche se nello stream
            //sono presenti i bytes del body.

            //Decodifico direttamente lo stream.
            //long decode_start_time = System.currentTimeMillis();
            result = decodeStream(in);
            //long decode_end_time = System.currentTimeMillis();
            //Log.d("Debug", String.format("Decode time: %d ms", (decode_end_time - decode_start_time)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
            return result;
        }
    }

    @Override
    public void onPostExecute(ResultType result){
        if (result == null){
            return;
        }

        executeStrategy.execute(result);
    }
}
