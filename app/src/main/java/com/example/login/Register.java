package com.example.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class Register extends AppCompatActivity {

    TextInputLayout firstname, lastname, email, password;
    TextView link, mChooseImage;
    Button singUp;

    private Uri mImageUri;

    ImageView mImageView;

    private static final int REQUEST_CODE = 1;


    FirebaseAuth firebaseAuth;
    DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        link = findViewById(R.id.signinlink);
        singUp = findViewById(R.id.signup);
        mChooseImage = findViewById(R.id.choose_image);

        mImageView = findViewById(R.id.image_view);


        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference("ApplicationDb");

        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singup();
            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });

        mChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(mImageView);
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE);

    }

    private void startSignIn() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void singup() {
        String firstName = firstname.getEditText().getText().toString().trim();
        String lastName = lastname.getEditText().getText().toString().trim();
        String emailAddress = email.getEditText().getText().toString().trim();
        String Password = password.getEditText().getText().toString().trim();
        if (!validateEmail(emailAddress) | !validatePassword(Password)
                | !validateFirstname(firstName) | !validatelastname(lastName)) {
            return;
        }
        // Toast.makeText(this, "Validation okay", Toast.LENGTH_SHORT).show();

        firebaseAuth.createUserWithEmailAndPassword(emailAddress, Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            AppUser user = new AppUser(firstName, lastName, emailAddress);
                            db.child("user details").child(firebaseAuth.getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        firebaseAuth.getCurrentUser().sendEmailVerification();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(Register.this, "Check your email for verification", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Register.this, "Registration failed..", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        } else {
                            Toast.makeText(Register.this, "Email Password Reg Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateEmail(String emailAddress) {

        if (emailAddress.isEmpty()) {
            email.setError("Please fill this field");
            email.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            email.setError("Please enter valid email address");
            email.requestFocus();
            return false;
        } else {
            email.setError(null);
        }
        return true;
    }

    private boolean validatePassword(String Password) {

        if (Password.isEmpty()) {
            password.setError("Please fill this field");
            password.requestFocus();
            return false;

        } else if (Password.length() < 6) {
            password.setError("Password should have at least 6 characters");
            return false;
        } else {
            password.setError(null);
        }
        return true;
    }

    private boolean validateFirstname(String Firstname) {
        if (Firstname.isEmpty()) {
            firstname.setError("Please fill this field");
            firstname.requestFocus();
            return false;
        } else {
            firstname.setError(null);
        }
        return true;
    }

    private boolean validatelastname(String Lastname) {
        if (Lastname.isEmpty()) {
            lastname.setError("Please fill this field");
            return false;
        } else {
            lastname.setError(null);
        }
        return true;
    }


}