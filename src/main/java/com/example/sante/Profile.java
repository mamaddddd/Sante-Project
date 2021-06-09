package com.example.sante;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {


    String ID = "";
    ImageView imageViewUser;
    TextView textViewUsername,textViewEmail,textViewPhone,textViewLocation;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Profile.this);
        ID = preferences.getString("id", "");

        imageViewUser = (ImageView) findViewById(R.id.UserImageView);
        textViewUsername = (TextView) findViewById(R.id.txtUsername);
        textViewEmail = (TextView) findViewById(R.id.txtEmail);
        textViewPhone = (TextView) findViewById(R.id.txtPhoneNumber);
        textViewLocation = (TextView) findViewById(R.id.txtLocation);

        progressDialog = new ProgressDialog(Profile.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(ID);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String FullName = dataSnapshot.child("Username").getValue().toString();
                String Email = dataSnapshot.child("Email").getValue().toString();
                String PhoneNumber = dataSnapshot.child("Phone").getValue().toString();
                String Location = dataSnapshot.child("Location").getValue().toString();
                String UserImage = dataSnapshot.child("UserImage").getValue().toString();

                Glide.with(Profile.this).load(UserImage).into(imageViewUser);
                textViewUsername.setText(FullName);
                textViewEmail.setText(Email);
                textViewPhone.setText(PhoneNumber);
                textViewLocation.setText(Location);
                progressDialog.dismiss();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }
}