package com.example.newapp;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Thread.sleep;

public class lottie_ninja extends AppCompatActivity {

    String input;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lottie_ninja);
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3500);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Intent mainIntent = new Intent(lottie_ninja.this, MainActivity.class);
                    startActivity(mainIntent);
                }
            }
        };
        thread.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}