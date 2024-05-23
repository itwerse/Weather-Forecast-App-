package com.example.weatherapplication.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.weatherapplication.Classes.ProgressBarAnimation;
import com.example.weatherapplication.R;

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {
    ProgressBar progressBar;
    ImageButton btn_start;
    Animation animFade;
    private static int SPLASH_SCREEN_TIME_OUT = 2500;
    ImageView sp_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        animFade = AnimationUtils.loadAnimation(this, R.anim.fade);
        animFade.setAnimationListener(this);

        Window window = SplashActivity.this.getWindow();
        sp_icon = findViewById(R.id.imageView);
        sp_icon.startAnimation(animFade);

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(SplashActivity.this, R.color.app_primary_dark12));
        }

        ProgressBarAnimation anim = new ProgressBarAnimation(progressBar, 3, 3);
        anim.setDuration(1000);

        btn_start = findViewById(R.id.start);
        btn_start.setVisibility(View.INVISIBLE);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        if (!prefs.getBoolean("firstTimepack1", false)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTimepack1", true);
            editor.apply();

            // Show splash screen and launch SettingsActivity
            btn_start.setVisibility(View.VISIBLE);
            btn_start.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View view) {
                    btn_start.setBackground(getDrawable(R.drawable.round_btn2));
                    Intent i = new Intent(SplashActivity.this, SettingsActivity.class);
                    startActivity(i);
                }
            });
        } else {
            // Show splash screen and launch MainActivity
//            btn_start.setVisibility(View.VISIBLE);
//            btn_start.setOnClickListener(new View.OnClickListener() {
//                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//                @Override
//                public void onClick(View view) {
//                    btn_start.setBackground(getDrawable(R.drawable.round_btn2));
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
//                }
//            });
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btn_start.setVisibility(View.VISIBLE);
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        btn_start.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}
