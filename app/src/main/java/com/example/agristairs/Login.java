package com.example.agristairs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private Button login;
    private TextView register;
    private EditText pass;
    private EditText phonen;
    private ProgressBar progressBar;
    private FrameLayout signg;
    private AlertDialog.Builder d;
    FirebaseAuth fauth;

//    GoogleSignInOptions gso;
//    GoogleSignInClient gsc;



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
        phonen = (EditText) findViewById(R.id.logphone);
        pass = (EditText) findViewById(R.id.logpassword);
        signg = (FrameLayout) findViewById(R.id.googleLogin);
        fauth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        d = new AlertDialog.Builder(this);


//        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();






        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Phone = phonen.getText().toString().trim();
                String password = pass.getText().toString().trim();


                if (TextUtils.isEmpty(Phone)) {
                    phonen.setError("You have must Enter an Email");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    pass.setError("Please Enter your Password");
                    return;
                }

                if (pass.length() < 6) {
                    pass.setError("Please Enter password upto 6 Character");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //sign in

                fauth.signInWithEmailAndPassword(Phone,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Homepage.class));
                        }else {
                            Toast.makeText(Login.this, "Error\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                    }
                });
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
