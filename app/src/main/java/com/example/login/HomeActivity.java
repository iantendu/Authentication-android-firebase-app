package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    TextView firstname, lastname, email,title;

    Button signOut;
    FirebaseAuth firebaseAuth;

    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        signOut = findViewById(R.id.logout);
        firebaseAuth = FirebaseAuth.getInstance();

        db = FirebaseDatabase.getInstance().getReference("ApplicationDb");

        firstname = findViewById(R.id.firtname_textview);
        lastname = findViewById(R.id.lastname_textview);
        email = findViewById(R.id.email_textview);
        title = findViewById(R.id.welcome);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Toast.makeText(HomeActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userId = user.getUid();

        db.child("user details").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AppUser appUser = snapshot.getValue(AppUser.class);
                if(appUser != null) {
                    String s_title = appUser.getFirstname();
                    String s_firstname = appUser.getFirstname();
                    String s_lastname = appUser.getLastname();
                    String s_email = appUser.getEmail();

                    title.setText("Welcome " +s_title);
                    firstname.setText("First Name : " +s_firstname);
                    lastname.setText("Last Name : " +s_lastname);
                    email.setText("Email : " +s_email);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Error Happened", Toast.LENGTH_SHORT).show();

            }
        });


    }
}