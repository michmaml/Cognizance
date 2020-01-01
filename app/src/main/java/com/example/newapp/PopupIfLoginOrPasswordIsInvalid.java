package com.example.newapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class PopupIfLoginOrPasswordIsInvalid extends Activity {

    Button loginOrPasswordInvalid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_if_login_or_password_is_invalid);
        loginOrPasswordInvalid = (Button) findViewById(R.id.loginOrPasswordInvalid);
        loginOrPasswordInvalid.setOnClickListener(new View.OnClickListener(){
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

        getWindow().setLayout((int)(width*.7), (int)(height*.2));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);


    }
}
