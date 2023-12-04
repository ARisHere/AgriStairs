package com.example.agristairs;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private Button login;
    private EditText phonen;
    private ProgressBar progressBar;
    private AlertDialog.Builder d;
    private TextView signupbtn;
    FirebaseAuth fauth;

    Intent a = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize(savedInstanceState);
    }

    private void initialize(Bundle savedInstanceState) {

        signupbtn= findViewById(R.id.signupbtn);
        login= (Button) findViewById(R.id.loginbutton);
        phonen = (EditText) findViewById(R.id.logphone);
        fauth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.pb);
        d = new AlertDialog.Builder(this);



        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.setClass(getApplicationContext(), Registration.class);
                startActivity(a);
                finish();
            }
        });





        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Phone = phonen.getText().toString().trim();


                if (TextUtils.isEmpty(Phone)) {
                    phonen.setError("You have must Enter a mobile number");
                    return;
                }

                if(phonen.length()!=11){
                    phonen.setError("Enter Correct Mobile Number");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //sign in
                    if(Phone.equals("01739411518")){
                        Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Homepage.class));
                        progressBar.setVisibility(View.INVISIBLE);
                    }else {
                        Toast.makeText(Login.this, "Log In Error! Try Again...", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }




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
