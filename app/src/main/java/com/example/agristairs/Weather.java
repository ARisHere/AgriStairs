package com.example.agristairs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Weather extends AppCompatActivity {

    private ProgressBar loadingPB;
    private TextView cityNameTV, temperatureTV, conditionTV;
    private RecyclerView weatherRV;
    private EditText cityEdit;
    private ImageView backIV, iconIV, searchIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        loadingPB = findViewById(R.id.pb);
        cityNameTV = findViewById(R.id.w_cityname);
        temperatureTV = findViewById(R.id.w_temp);
        conditionTV = findViewById(R.id.w_wtype);
        weatherRV = findViewById(R.id.recycle);
        cityEdit = findViewById(R.id.w_citynameenter);
//        backIV = findViewById(R.id.pb);
        iconIV = findViewById(R.id.w_img);
//        searchIV = findViewById(R.id.);





    }
}