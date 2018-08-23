package com.example.feder_000.loomo.HttpTask.PostExecuteStrategy;

import com.segway.robot.sdk.voice.Speaker;
import com.segway.robot.sdk.voice.VoiceException;
import com.segway.robot.sdk.voice.tts.TtsListener;

public class SpeakReceivedAnswerStrategy implements IOnPostExecuteStrategy<String> {

    private Speaker speaker;

    public SpeakReceivedAnswerStrategy(Speaker speaker){
        this.speaker = speaker;
    }

    @Override
    public void execute(String result) {
        try {
            speaker.speak(result, new TtsListener() {
                @Override
                public void onSpeechStarted(String word) {

                }

                @Override
                public void onSpeechFinished(String word) {

                }

                @Override
                public void onSpeechError(String word, String reason) {

                }
            });
        } catch (VoiceException e) {
            e.printStackTrace();
        }
    }
}
