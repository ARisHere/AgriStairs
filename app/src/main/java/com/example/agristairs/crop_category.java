package com.example.agristairs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class crop_category extends AppCompatActivity {

    Intent a = new Intent();

    private ImageButton rice;
    private ImageButton wheat;
    private ImageButton sugarcane;
    private ImageButton mustard;
    private ImageButton jute;
    private ImageButton tea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_category);


        rice = (ImageButton) findViewById(R.id.rice);
        wheat = (ImageButton) findViewById(R.id.wheat);
        sugarcane = (ImageButton) findViewById(R.id.sugarcane);
        mustard = (ImageButton) findViewById(R.id.mustard);
        jute = (ImageButton) findViewById(R.id.jute);
        tea = (ImageButton) findViewById(R.id.tea);


        rice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.setClass(getApplicationContext(), RiceActivity.class);
                startActivity(a);
                finish();
            }
        });

        wheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.setClass(getApplicationContext(), WheatActivity.class);
                startActivity(a);
                finish();
            }
        });

        mustard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(crop_category.this, "This section is under development.", Toast.LENGTH_SHORT).show();
            }
        });

        sugarcane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(crop_category.this, "This section is under development.", Toast.LENGTH_SHORT).show();
            }
        });

        jute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(crop_category.this, "This section is under development.", Toast.LENGTH_SHORT).show();
            }
        });

        tea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(crop_category.this, "This section is under development.", Toast.LENGTH_SHORT).show();
            }
        });



    }
        @Override
        public void onBackPressed() {
            a.setClass(getApplicationContext(), Homepage.class);
            startActivity(a);
            finish();
        }

}