package com.example.agristairs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class Registration extends AppCompatActivity {
    private Button register;
    private TextView login;
    private EditText uname;
    private EditText phoneno;
    private EditText pass1;
    private EditText pass2;
    private ProgressBar progressBar;
    Intent a = new Intent();
    FirebaseAuth fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        register= (Button) findViewById(R.id.rbutton);
        login = (TextView) findViewById(R.id.loginText);
        phoneno = (EditText) findViewById(R.id.phone);
        uname = (EditText) findViewById(R.id.name);
        pass1 = (EditText) findViewById(R.id.pass1);
        pass2 = (EditText) findViewById(R.id.pass2);
        progressBar = (ProgressBar) findViewById(R.id.pBar2);

        fauth = FirebaseAuth.getInstance();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = uname.getText().toString().trim();
                String phone = phoneno.getText().toString().trim();
                String password1 = pass1.getText().toString().trim();
                String password2 = pass2.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    uname.setError("Please Enter your Full name");
                    return;
                }

                if (TextUtils.isEmpty(phone)) {
                    phoneno.setError("Please Enter your Phone Number");
                    return;
                }

                if (TextUtils.isEmpty(password1)) {
                    pass1.setError("Please Enter Password");
                    return;
                }
                if (TextUtils.isEmpty(password2)) {
                    pass2.setError("Please Enter Password");
                    return;
                }

                if (password1.length() < 6) {
                    pass1.setError("Please Enter password upto 6 Character");
                    return;
                }

                if (password2.length() < 6) {
                    pass2.setError("Please Enter password upto 6 Character");
                    return;
                }

//                                if (password1!=password2)
//                                {
//                                        Toast.makeText(Registration.this, "Cheak the password again", Toast.LENGTH_SHORT).show();
//                                        return;
//                                }

                progressBar.setVisibility(View.VISIBLE);

                //register using firebase

                fauth.createUserWithEmailAndPassword(phone, password2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Registration.this, "Account Create Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        } else {
                            Toast.makeText(Registration.this, "Error! Anything is wrong\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
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