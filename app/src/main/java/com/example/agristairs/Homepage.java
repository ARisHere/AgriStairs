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


    @Override
    public void onBackPressed(){

        new AlertDialog.Builder(this).setMessage("Are You want to Logout?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }
}