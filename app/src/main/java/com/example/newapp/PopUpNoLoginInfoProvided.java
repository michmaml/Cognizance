package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class PopUpNoLoginInfoProvided extends Activity {

    Button btn_popupNoLoginInfoProvided;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_no_login_info_provided);
        btn_popupNoLoginInfoProvided = findViewById(R.id.noLoginInfoProvided);
        btn_popupNoLoginInfoProvided.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //DIMS THE SCREEN WHEN INCORRECT INPUT
        /*final WindowManager.LayoutParams layout = getWindow().getAttributes();
        layout.screenBrightness = 0.06F;
        getWindow().setAttributes(layout);*/

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .7), (int) (height * .2));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);


    }
}
