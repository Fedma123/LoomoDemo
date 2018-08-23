package com.example.feder_000.loomo.HttpTask.PostExecuteStrategy;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class UpdateImageViewStrategy implements IOnPostExecuteStrategy<Bitmap> {

    private ImageView imageView;

    public UpdateImageViewStrategy(ImageView imageView){
        this.imageView = imageView;
    }

    @Override
    public void execute(Bitmap result) {
        imageView.setImageBitmap(result);
    }
}
