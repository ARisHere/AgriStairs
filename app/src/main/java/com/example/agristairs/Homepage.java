package com.example.agristairs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Homepage extends AppCompatActivity {

    private Button chatbox;
    Intent a = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        chatbox = (Button) findViewById(R.id.chatbotbutton);


        chatbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                a.setClass(getApplicationContext(), Chatbox.class);
                startActivity(a);
                finish();
            }
        });
    }


    public void onBackPressed(){

        AlertDialog.Builder  ad =  new AlertDialog.Builder(Homepage.this);
        ad.setTitle("LOGOUT");
        ad.setMessage("Are You Want to Logout?");
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                a.setClass(getApplicationContext(), Login.class);
                startActivity(a);
                finish();
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