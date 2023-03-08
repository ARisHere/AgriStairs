package com.example.agristairs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import java.util.Timer;
import java.util.TimerTask;

public class Splash_Screen extends AppCompatActivity {

    private Timer _timer = new Timer();


    //private ImageView imageview1;

    private Intent in = new Intent();
    private TimerTask i;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {


    }

    private void initializeLogic() {
        i = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        in.setClass(getApplicationContext(), Login.class);
                        startActivity(in);
                        finish();
                    }
                });
            }
        };
        _timer.schedule(i, (int) (2000));
    }
}