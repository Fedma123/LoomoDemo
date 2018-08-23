package com.example.feder_000.loomo.HttpTask.PostExecuteStrategy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.segway.robot.sdk.locomotion.sbv.Base;
import com.segway.robot.sdk.voice.Speaker;
import com.segway.robot.sdk.voice.VoiceException;
import com.segway.robot.sdk.voice.tts.TtsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class LogJsonUpdateAndTalkImageView implements IOnPostExecuteStrategy<String> {

    private ImageView imageView;
    private Speaker speaker;

    private static Timer speechTimer;
    private static boolean canTalk = false;

    static{
        speechTimer = new Timer();
        speechTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                canTalk = true;
            }
        }, 3000, 10000);
    }

    public LogJsonUpdateAndTalkImageView(ImageView imageView, Speaker speaker){
        this.imageView = imageView;
        this.speaker = speaker;
    }

    @Override
    public void execute(String result) {
        try {
            JSONObject json;
            json = new JSONObject(result);

            String text = json.getString("text");
            Integer data = json.getInt("data");
            String base64EncodedImage = json.getString("image");
            byte[] decodedImageBytes = Base64.decode(base64EncodedImage, Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(decodedImageBytes, 0, decodedImageBytes.length);
            imageView.setImageBitmap(bmp);

            speaker.setVolume(100);
            if (canTalk){
                canTalk = false;
                speaker.speak(text, new TtsListener() {
                    @Override
                    public void onSpeechStarted(String word) {

                    }

                    @Override
                    public void onSpeechFinished(String word) {

                    }

                    @Override
                    public void onSpeechError(String word, String reason) {
                        canTalk = false;
                    }
                });
            }

            Log.d("Json", String.format("Text->%s, data->%d", text, data));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (VoiceException e) {
            e.printStackTrace();
        }
    }
}
