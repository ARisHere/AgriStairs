package com.example.agristairs;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RiceActivity extends AppCompatActivity {

    private ImageView upimage;
    private Button upbtn;
    private TextView disease, diseasecause, cure, cureinfo;

    int imagesize = 224;

    Intent a = new Intent();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rice_activity);

        upimage = findViewById(R.id.imageView);
        upbtn = findViewById(R.id.uploadimg);
        disease = findViewById(R.id.diseases);
        diseasecause = findViewById(R.id.diseasesInfo);
        cure = findViewById(R.id.cure);
        cureinfo = findViewById(R.id.cureInfo);

        disease.setVisibility(View.GONE);
        diseasecause.setVisibility(View.GONE);
        cureinfo.setVisibility((View.GONE));
        cure.setVisibility(View.GONE);


        upbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, 1);
                    }else {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},100);
                    }
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK)  {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            int dimension = Math.min(image.getWidth(),image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image,dimension,dimension);
            upimage.setImageBitmap(image);

            cureinfo.setVisibility(View.VISIBLE);
            diseasecause.setVisibility(View.VISIBLE);
            disease.setVisibility((View.VISIBLE));
            cure.setVisibility(View.VISIBLE);

//            image = Bitmap.createScaledBitmap(image,imagesize,imagesize,false);
//            classifyImage(image);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public void onBackPressed() {
        a.setClass(getApplicationContext(), crop_category.class);
        startActivity(a);
        finish();
    }
}