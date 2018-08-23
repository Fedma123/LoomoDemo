package com.example.feder_000.loomo.HttpTask.PostExecuteStrategy;

import android.util.Log;

public class LogReceivedJsonStrategy implements IOnPostExecuteStrategy<String>{
    @Override
    public void execute(String result) {
        Log.d("Debug", "Received " + result);
    }
}
