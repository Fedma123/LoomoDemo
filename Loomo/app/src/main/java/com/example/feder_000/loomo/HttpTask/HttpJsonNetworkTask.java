package com.example.feder_000.loomo.HttpTask;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.example.feder_000.loomo.HttpTask.PostExecuteStrategy.IOnPostExecuteStrategy;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class HttpJsonNetworkTask extends HttpStreamTask<String> {

    public HttpJsonNetworkTask(@NonNull IOnPostExecuteStrategy<String> executeStrategy,
                               @NonNull URL serverUrl,
                               @NonNull Bitmap bmp) {
        super(executeStrategy, serverUrl, bmp);
    }

    public HttpJsonNetworkTask(@NonNull IOnPostExecuteStrategy<String> executeStrategy,
                               @NonNull URL serverUrl,
                               @NonNull Bitmap bmp,
                               int jpegQuality) {
        super(executeStrategy, serverUrl, bmp, jpegQuality);
    }

    public HttpJsonNetworkTask(@NonNull IOnPostExecuteStrategy<String> executeStrategy,
                               @NonNull URL serverUrl,
                               @NonNull byte[] jpegBytes) {
        super(executeStrategy, serverUrl, jpegBytes);
    }

    public HttpJsonNetworkTask(@NonNull IOnPostExecuteStrategy<String> executeStrategy,
                               @NonNull URL serverUrl,
                               @NonNull byte[] jpegBytes,
                               int jpegQuality) {
        super(executeStrategy, serverUrl, jpegBytes, jpegQuality);
    }

    @Override
    public String decodeStream(InputStream inputStream) {
        return new Scanner(inputStream).next();
    }
}