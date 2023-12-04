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


import com.example.agristairs.ml.Rice;

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

            image = Bitmap.createScaledBitmap(image,224,224,false);
            classifyImage(image);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void classifyImage(Bitmap image) {
        try {
            Rice model = Rice.newInstance(getApplicationContext());


            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1,224,224,3}, DataType.FLOAT32);
            ByteBuffer byteBuffer;
            byteBuffer = ByteBuffer.allocateDirect(4 * imagesize * imagesize * 3);
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

            Rice.Outputs outputs =model.process(inputFeature0);
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

            String[] classes = {"Bacterial Leaf Blight","Brawn Spot","Lead Scaled","Leaf Smut","Rice Blust","Rice Tungro","Sheath Blight"};

            if(classes[maxPos].equals(classes[0])){
                disease.setText(classes[maxPos]);
                diseasecause.setText("The bacterium Xanthomonas oryzae pv. Oryzae is the causal agent. It enters the plant through natural openings, wounds, or stomata (tiny pores on the leaf surface).");
                cureinfo.setText("Copper-based Sprays: Copper-based fungicides or bactericides, such as copper oxychloride or copper hydroxide, can help control bacterial leaf blight. \n" +
                        "\n" +
                        "Streptomycin: Streptomycin is an antibiotic that has been used to control bacterial diseases in crops, including BLB. However, its use can lead to the development of antibiotic-resistant strains of the pathogen and is not recommended due to potential negative impacts on public health and the environment.\n" +
                        "\n" +
                        "Bactericides: Some bactericides formulated specifically for managing bacterial diseases in rice might be available. These products are designed to target the bacterial pathogen while minimizing harm to beneficial organisms.\n");
            }

            else if(classes[maxPos].equals(classes[1])){
                disease.setText(classes[maxPos]);
                diseasecause.setText("The fungal pathogen Bipolaris oryzae is responsible for causing brown spot. It enters the plant through wounds or natural openings, such as stomata.");
                cureinfo.setText("Triazole Fungicides: Triazole fungicides such as propiconazole and tebuconazole are commonly used for controlling fungal diseases, including brown spot. They can be effective in preventing and managing the disease. Follow label instructions for proper application rates and timings.\n" +
                        "\n" +
                        "Strobilurin Fungicides: Strobilurin fungicides like azoxystrobin and trifloxystrobin have fungicidal activity against a range of fungal pathogens, including those causing brown spot. They provide protection to the plant by inhibiting fungal respiration.\n" +
                        "\n" +
                        "Mancozeb: Mancozeb is a broad-spectrum fungicide that can be used to manage brown spot. It's important to note that mancozeb is a multi-site fungicide, meaning it acts on multiple targets within the pathogen, which reduces the risk of developing resistance.\n" +
                        "\n" +
                        "Chlorothalonil: Chlorothalonil is another multi-site fungicide that can be used to manage brown spot. Like mancozeb, it is less likely to lead to the development of resistance in the pathogen.\n");
            }

            else if(classes[maxPos].equals(classes[2])){
                disease.setText(classes[maxPos]);
                diseasecause.setText("The bacterium Xanthomonas oryzae pv. oryzicola is responsible for causing leaf scald in rice.");
                cureinfo.setText("For bacterial diseases like leaf scald, the term \"bactericide\" is more appropriate. Copper-based compounds are often used as bactericides to manage bacterial diseases in crops, including rice. Copper sprays can help control the spread of the bacterium by reducing bacterial populations on the plant surfaces.Copper hydroxide and copper oxychloride are common copper-based bactericides used for controlling bacterial diseases in rice.");
            }

            else if(classes[maxPos].equals(classes[3])){
                disease.setText(classes[maxPos]);
                diseasecause.setText("The fungal pathogen Ustilaginoidea virens infects rice panicles and causes the characteristic smut balls.");
                cureinfo.setText("Triadimefon: Triadimefon is a systemic fungicide that can provide control against false smut. It inhibits fungal growth and development.\n" +
                        "\n" +
                        "Tebuconazole: Tebuconazole is a triazole fungicide that provides both preventive and curative control of false smut. It's absorbed by the plant and has systemic properties.\n" +
                        "\n" +
                        "Propiconazole: Propiconazole is another triazole fungicide that inhibits fungal growth and can be used to manage false smut disease.\n" +
                        "\n" +
                        "Flutriafol: Flutriafol is a triazole fungicide that can protect rice plants against false smut and other fungal diseases.\n");
            }

            else if(classes[maxPos].equals(classes[4])){
                disease.setText(classes[maxPos]);
                diseasecause.setText("The fungus Magnaporthe oryzae has various strains and can attack different rice varieties. It spreads through airborne spores, called conidia, which are produced in the lesions.");
                cureinfo.setText("Tricyclazole: Tricyclazole is a systemic fungicide commonly used to manage blast disease in rice. It provides preventive and curative control and is effective against the fungus causing blast.\n" +
                        "Difenoconazole: Difenoconazole is a triazole fungicide that inhibits fungal growth and can effectively manage blast disease. It provides both preventative and curative action.\n" +
                        "Pyraclostrobin: Pyraclostrobin is a strobilurin fungicide that interferes with fungal respiration. It can be effective for controlling blast in rice.\n" +
                        "Propiconazole: Propiconazole is another triazole fungicide that inhibits fungal growth and can be used to manage blast disease.\n" +
                        "Hexaconazole: Hexaconazole is a triazole fungicide with systemic properties that can provide control against blast disease.\n" +
                        "Azoxystrobin: Azoxystrobin is a strobilurin fungicide that can also help manage blast in rice by inhibiting fungal respiration.\n" +
                        "Isoprothiolane: Isoprothiolane is a systemic fungicide and plant growth regulator that can be used to manage blast disease. It can enhance plant resistance.\n" +
                        "Fenoxanil: Fenoxanil is a fungicide that interferes with fungal cell wall formation and can provide effective control against blast.\n");
            }

            else if(classes[maxPos].equals(classes[5])){
                disease.setText(classes[maxPos]);
                diseasecause.setText("•\tRice Tungro Bacilliform Virus (RTBV): Transmitted in a persistent manner by the green leafhopper.•\tRice Tungro Spherical Virus (RTSV): Also transmitted by the green leafhopper.");
                cureinfo.setText("Insecticides targeting the green leafhopper may be used to control its population and reduce the transmission of tungro viruses. Common insecticides used for controlling leafhoppers in rice fields include neonicotinoids, pyrethroids, and organophosphates.Implementing an integrated approach that combines the use of resistant varieties, cultural practices, and judicious insecticide use can be more effective in managing tungro disease.");
            }

            else if(classes[maxPos].equals(classes[6])){
                disease.setText(classes[maxPos]);
                diseasecause.setText("The fungus Rhizoctonia solani survives in soil and crop debris between growing seasons. It can infect rice plants through direct contact and via mycelium growth.");
                cureinfo.setText("Azoxystrobin: Azoxystrobin is a systemic fungicide from the strobilurin group. It provides broad-spectrum disease control by inhibiting fungal respiration. It is effective against sheath blight and other rice diseases.\n" +
                        "\n" +
                        "Trifloxystrobin: Trifloxystrobin is another strobilurin fungicide that works similarly to azoxystrobin. It offers preventive and curative action against sheath blight.\n" +
                        "\n" +
                        "Boscalid: Boscalid is a fungicide from the carboxamide group. It inhibits fungal respiration and can be effective against sheath blight.\n" +
                        "\n" +
                        "Prothioconazole: Prothioconazole is a triazole fungicide that has systemic and protectant properties. It's used for controlling a range of fungal diseases, including sheath blight.\n" +
                        "\n" +
                        "Fluazinam: Fluazinam is a fungicide with protectant and curative properties. It provides control against sheath blight by inhibiting fungal cell wall synthesis.\n" +
                        "\n" +
                        "Isoprothiolane: Isoprothiolane is a systemic fungicide and plant growth regulator that can be effective against sheath blight. It enhances plant resistance and can improve yield under disease pressure.\n" +
                        "\n" +
                        "Thiophanate-methyl: Thiophanate-methyl is a systemic fungicide that offers protection against sheath blight. It's absorbed by the plant and moves within its tissues.\n" +
                        "\n" +
                        "Propiconazole: Propiconazole is a triazole fungicide that inhibits fungal growth. It's used for managing a variety of fungal diseases, including sheath blight.\n" +
                        "\n" +
                        "Fenoxanil: Fenoxanil is a fungicide that interferes with fungal cell wall formation. It can provide effective control of sheath blight.\n");
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