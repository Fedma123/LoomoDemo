package com.example.feder_000.loomo.HttpTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.example.feder_000.loomo.HttpTask.PostExecuteStrategy.IOnPostExecuteStrategy;

import java.io.InputStream;
import java.net.URL;

public class HttpBitmapNetworkTask extends HttpStreamTask<Bitmap> {

    public HttpBitmapNetworkTask(@NonNull IOnPostExecuteStrategy<Bitmap> executeStrategy,
                                 @NonNull URL serverUrl,
                                 @NonNull Bitmap bmp) {
        super(executeStrategy, serverUrl, bmp);
    }

    public HttpBitmapNetworkTask(@NonNull IOnPostExecuteStrategy<Bitmap> executeStrategy,
                                 @NonNull URL serverUrl,
                                 @NonNull Bitmap bmp,
                                 int jpegQuality) {
        super(executeStrategy, serverUrl, bmp, jpegQuality);
    }

    public HttpBitmapNetworkTask(@NonNull IOnPostExecuteStrategy<Bitmap> executeStrategy,
                                 @NonNull URL serverUrl,
                                 @NonNull byte[] jpegBytes) {
        super(executeStrategy, serverUrl, jpegBytes);
    }

    public HttpBitmapNetworkTask(@NonNull IOnPostExecuteStrategy<Bitmap> executeStrategy,
                                 @NonNull URL serverUrl,
                                 @NonNull byte[] jpegBytes,
                                 int jpegQuality) {
        super(executeStrategy, serverUrl, jpegBytes, jpegQuality);
    }

    @Override
    public Bitmap decodeStream(InputStream inputStream) {
        return BitmapFactory.decodeStream(inputStream);
    }
}