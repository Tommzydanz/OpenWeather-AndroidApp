package com.example.openweather;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import static android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

public class SplashScreen extends AppCompatActivity {
    Activity activity;
    private static final int TIMEPIECES = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);



        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));


        Handler handler = new Handler();


        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }, TIMEPIECES);
    }
}