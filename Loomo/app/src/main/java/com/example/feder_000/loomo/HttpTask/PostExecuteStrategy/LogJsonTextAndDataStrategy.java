package com.example.feder_000.loomo.HttpTask.PostExecuteStrategy;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class LogJsonTextAndDataStrategy implements IOnPostExecuteStrategy<String> {
    @Override
    public void execute(String result) {
        try {
            JSONObject json;
            json = new JSONObject(result);

            String text = json.getString("text");
            Integer data = json.getInt("data");

            Log.d("Json", String.format("Text->%s, data->%d", text, data));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
