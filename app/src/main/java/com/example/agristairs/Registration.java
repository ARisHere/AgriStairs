package com.example.agristairs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class Registration extends AppCompatActivity {

    private Button register;
    private TextView login;
    private EditText name;
    private EditText mobile;
    private EditText email;
    private EditText password;
    private EditText confirmpass;
    private ProgressBar pb;

    FirebaseAuth fauth;
    FirebaseApp firebase;
    boolean passwordVisible;

    private Intent in = new Intent();

    Intent a = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        register= (Button) findViewById(R.id.registrationbutton);
        login = (TextView) findViewById(R.id.loginbtn);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        mobile = (EditText) findViewById(R.id.phone2);
        password = (EditText) findViewById(R.id.pass1);
        confirmpass = (EditText) findViewById(R.id.pass2);
        pb= (ProgressBar) findViewById(R.id.pBar2);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                String fname = name.getText().toString().trim();
                String remail = email.getText().toString().trim();
                String phone = mobile.getText().toString().trim();
                String password1 = password.getText().toString().trim();
                String password2 = confirmpass.getText().toString().trim();

                if (TextUtils.isEmpty(fname)) {
                    name.setError("Please Enter your Full Name");
                    return;
                }

                if (TextUtils.isEmpty(remail)) {
                    email.setError("Please Enter your Email");
                    return;
                }


                if (TextUtils.isEmpty(phone)) {
                    mobile.setError("Please Enter you Phone Number");
                    return;
                }

                if (TextUtils.isEmpty(password1)) {
                    password.setError("Please Enter Password");
                    return;
                }
                if (TextUtils.isEmpty(password2)) {
                    confirmpass.setError("Please Enter Confirm Password");
                    return;
                }

                if (password1.length() < 6) {
                    password.setError("Please Enter password upto 6 Character");
                    return;
                }
                if (password2.length() < 6) {
                    confirmpass.setError("Please Enter password upto 6 Character");
                    return;
                }
//                }
//                if (!password.getText().toString().equals(confirmpass.getText().toString()))
//                {
//                    Toast.makeText(Registration.this, "Cheak the password again", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                pb.setVisibility(View.VISIBLE);

                fauth.createUserWithEmailAndPassword(remail,password2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Registration.this, "Account Create Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        } else {
                            Toast.makeText(Registration.this, "Error! Anything is wrong\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            pb.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                a.setClass(getApplicationContext(), Login.class);
                startActivity(a);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        a.setClass(getApplicationContext(), Login.class);
        startActivity(a);
        finish();
    }
}