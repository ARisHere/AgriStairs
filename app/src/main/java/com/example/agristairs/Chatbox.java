package com.example.agristairs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Chatbox extends AppCompatActivity {
    Intent a = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);
    }

    @Override
    public void onBackPressed() {
        a.setClass(getApplicationContext(), Homepage.class);
        startActivity(a);
        finish();
    }
}