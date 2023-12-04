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


import com.example.agristairs.ml.Wheat;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class WheatActivity extends AppCompatActivity {

    private ImageView upimage;
    private Button upbtn;
    private TextView disease, diseasecause, cure, cureinfo;

    int imagesize = 224;
    private Interpreter interpreter;
    Intent a = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheat);

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
            Wheat model = Wheat.newInstance(getApplicationContext());


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

            Wheat.Outputs outputs =model.process(inputFeature0);
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

            String[] classes = {"Septoria","Stripe Rust"};

            if(classes[maxPos].equals(classes[0])){
                disease.setText(classes[maxPos]);
                diseasecause.setText("Septoria leaf spot or speckled leaf blotch of wheat, is a fungal disease caused by the fungus Mycosphaerella graminicola (asexual stage: Septoria tritici). It is one of the most important foliar diseases of wheat, causing significant yield losses worldwide.");
                cureinfo.setText("Septoria leaf spot is a common plant disease caused by the fungus Septoria. It affects a variety of plants, including tomatoes, wheat, and other crops. While there is no specific \"Septoria pesticide,\" fungicides are commonly used to manage and control Septoria diseases. Here are some general guidelines for managing Septoria leaf spot:\n" +
                        "\n" +
                        "Fungicides: Various fungicides are effective against Septoria, and their selection depends on the specific crop and the severity of the disease. Common fungicides for Septoria control include those containing active ingredients such as azoxystrobin, chlorothalonil, mancozeb, and myclobutanil. Always follow the manufacturer's recommendations and guidelines for application rates and timings." +
                        "\n Resistant Varieties: Consider planting crop varieties that are resistant or tolerant to Septoria leaf spot.\n" +
                        "\n" +
                        "Early Detection: Monitor your plants regularly for signs of Septoria leaf spot. Early detection allows for prompt action and better disease management.\n" +
                        "\n" +
                        "Proper Watering: Avoid overhead irrigation if possible, as it can create a conducive environment for the development and spread of fungal diseases. Water at the base of the plants to keep foliage dry.");
            }

            else if(classes[maxPos].equals(classes[1])){
                disease.setText(classes[maxPos]);
                diseasecause.setText("Stripe rust is caused by the fungal pathogen Puccinia striiformis. This fungus primarily infects the leaves of wheat plants but can also infect other grasses and cereals.");
                cureinfo.setText("Triazole Fungicides (DMI Fungicides):** Triazole fungicides are widely used for controlling stripe rust. Examples include tebuconazole, propiconazole, and metconazole. These fungicides are systemic, meaning they are absorbed by the plant and inhibit the growth of the fungus.\n" +
                        "\n" +
                        "Strobilurin Fungicides: Strobilurin fungicides like azoxystrobin and pyraclostrobin are effective against stripe rust. They work by disrupting the energy production of the fungus.\n" +
                        "\n" +
                        "SDHI Fungicides: Succinate dehydrogenase inhibitor (SDHI) fungicides, such as fluxapyroxad and bixafen, provide effective control of stripe rust. They have a different mode of action compared to triazoles and strobilurins and can be used in fungicide rotation programs to manage resistance.\n" +
                        "\n" +
                        "Multi-Mode of Action Fungicides: Some fungicides combine active ingredients with different modes of action to provide broad-spectrum disease control and reduce the risk of fungicide resistance development.\n" +
                        "\n" +
                        "Protectant Fungicides: Non-systemic protectant fungicides like chlorothalonil and mancozeb can be used for stripe rust management. They form a protective barrier on the plant's surface, preventing the fungus from infecting the plant.\n");
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