package com.example.agristairs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    private Button login;
    private TextView register;

    Intent a = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize(savedInstanceState);
    }

    private void initialize(Bundle savedInstanceState) {

        login= (Button) findViewById(R.id.loginbutton);
        register= (TextView) findViewById(R.id.signup);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                a.setClass(getApplicationContext(), Homepage.class);
                startActivity(a);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                a.setClass(getApplicationContext(), Registration.class);
                startActivity(a);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed(){

        AlertDialog.Builder  ad =  new AlertDialog.Builder(Login.this);
        ad.setTitle("Exit App");
        ad.setMessage("Are You Want to Exit?");
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();;
            }
        });
        ad.setNegativeButton("No",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();;
            }
        });
        ad.show();
    }
}
