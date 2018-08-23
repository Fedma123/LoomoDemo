package com.example.feder_000.loomo.HttpTask;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.widget.ImageView;

import com.alibaba.fastjson.util.IOUtils;
import com.example.feder_000.loomo.HttpTask.PostExecuteStrategy.IOnPostExecuteStrategy;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;
import java.util.stream.Collectors;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public String decodeStream(InputStream inputStream) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int readBytes;
        byte[] data = new byte[271360];
        try {
            while((readBytes = inputStream.read(data, 0, data.length)) != -1){
                buffer.write(data, 0, readBytes);
            }
            buffer.flush();
        } catch (IOException e){
            e.printStackTrace();
        }

        return new String(buffer.toByteArray());
    }
}