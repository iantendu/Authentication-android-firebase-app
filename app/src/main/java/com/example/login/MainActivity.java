package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    TextView singup;
    Button login;
    TextInputLayout Password, Email;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        singup = findViewById(R.id.signuplink);
        login = findViewById(R.id.signin);
        Password = findViewById(R.id.password);
        Email = findViewById(R.id.email);
        firebaseAuth = FirebaseAuth.getInstance();

        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignup();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_password = Password.getEditText().getText().toString().trim();
                String txt_email = Email.getEditText().getText().toString().trim();

                signin(txt_email,txt_password);
            }
        });

    }

    private void signin(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(!user.isEmailVerified()){
                    Toast.makeText(MainActivity.this, "Check your email for verification", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(MainActivity.this, "Log in successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void startSignup() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
        finish();
    }

}