package com.example.agristairs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.agristairs.ml.RiceUnquant;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class RiceActivity extends AppCompatActivity {

    private ImageView upimage;
    private Button upbtn;
    private TextView disease, diseasecause, cure, cureinfo;

    int imagesize = 224;
    private Interpreter interpreter;
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

            image = Bitmap.createScaledBitmap(image,imagesize,imagesize,false);
            classifyImage(image);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void classifyImage(Bitmap image) {
        try {
            RiceUnquant model = RiceUnquant.newInstance(getApplicationContext());


            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1,224,224,3}, DataType.FLOAT32);
            ByteBuffer byteBuffer;
            byteBuffer = ByteBuffer.allocateDirect(4 * imagesize * 3 * imagesize);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValue = new int[imagesize * imagesize];
            image.getPixels(intValue,0,image.getWidth(),0,0,image.getWidth(),image.getHeight());

            int pixel=0;
            for (int i=0; i< imagesize;i++){
                for (int j=0;j< imagesize;j++){
                    int val = intValue[pixel++];
                    byteBuffer.putFloat(((val >> 16 ) & 0xFF) *(1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8 ) & 0xFF) *(1.f / 255.f));
                    byteBuffer.putFloat((val  & 0xFF) *(1.f / 255.f));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);

            RiceUnquant.Outputs outputs =model.process(inputFeature0);
            TensorBuffer outputFeaturesO = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidence = outputFeaturesO.getFloatArray();

            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0;i< confidence.length;i++){
                if(confidence[i] > maxConfidence){
                    maxConfidence = confidence[i];
                    maxPos=i;
                }
            }

            String[] classes = {"Rice_Bacterial leaf blight","Rice_Brown spot","Rice_Leaf smut","Rice_Leaf Scaled","Rice_Blast","Rice_Tungro","Rice_Sheath blight"};

            if(classes[maxPos].equals(classes[0])){
                disease.setText(classes[maxPos]);
                diseasecause.setText("Rice_Bacterial leaf blight cause");
                cureinfo.setText("Cure info of Bacterial leaf blight");
            }

            else if(classes[maxPos].equals(classes[1])){
                disease.setText(classes[maxPos]);
                diseasecause.setText("Rice_Brown spot cause");
                cureinfo.setText("cure of Rice___Brown Spot");
            }

            else if(classes[maxPos].equals(classes[2])){
                disease.setText(classes[maxPos]);
                diseasecause.setText("Rice_Leaf smut cause");
                cureinfo.setText("cure of Rice_Leaf smut");
            }

            else if(classes[maxPos].equals(classes[3])){
                disease.setText(classes[maxPos]);
                diseasecause.setText("Rice Leaf Scaled");
                cureinfo.setText("cure of Rice_Leaf Scaled");
            }

            else if(classes[maxPos].equals(classes[4])){
                disease.setText(classes[maxPos]);
                diseasecause.setText("Rice___Rice Blast cause");
                cureinfo.setText("cure of Rice___Rice Blast");
            }

            else if(classes[maxPos].equals(classes[5])){
                disease.setText(classes[maxPos]);
                diseasecause.setText("Rice___Rice Tungro cause");
                cureinfo.setText("cure of Rice___Rice Tungro");
            }

            else if(classes[maxPos].equals(classes[6])){
                disease.setText(classes[maxPos]);
                diseasecause.setText("Rice___Shath Blight cause");
                cureinfo.setText("cure of Rice___Shath Blight");
            }

//            else if(classes[maxPos].equals(classes[7])){
//                cure.setVisibility(View.GONE);
//                disease.setVisibility(View.GONE);
//                cureinfo.setText("Your plant is Healthy!!!!!!!!!!!");
//                cure.setText("");
//            }
            else{
                cure.setVisibility(View.GONE);
                disease.setVisibility(View.GONE);
                cureinfo.setText("This is not a rice plant");
            }

            int finalMaxPos = maxPos;
            disease.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.com/search?q="+ classes[finalMaxPos])));
                }
            });
            model.close();
        }catch (IOException e ){

        }
    }



    @Override
    public void onBackPressed() {
        a.setClass(getApplicationContext(), crop_category.class);
        startActivity(a);
        finish();
    }
}