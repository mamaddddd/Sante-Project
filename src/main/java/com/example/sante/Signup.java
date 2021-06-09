package com.example.sante;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {


    Button buttonSignup;
    EditText editTextEmail,editTextPassword,editTextConfirmPassword;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        mAuth = FirebaseAuth.getInstance();
        editTextEmail = (EditText) findViewById(R.id.etEmail);
        editTextPassword = (EditText) findViewById(R.id.etPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);

        buttonSignup = (Button)findViewById(R.id.btnSingup);
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Email,Password,ConfirmPassword;

                Email = editTextEmail.getText().toString().trim();
                Password = editTextPassword.getText().toString().trim();
                ConfirmPassword = editTextConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(Email)) {
                    Toast.makeText(getApplicationContext(), "Please Enter your Email", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(Password)) {
                    Toast.makeText(getApplicationContext(), "Please Enter your Password", Toast.LENGTH_LONG).show();
                    return;
                }
                if (Password.length()<6) {
                    Toast.makeText(getApplicationContext(), "Password must contain 6 characters", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(ConfirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Please Enter your Confirm Password", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!Password.equals(ConfirmPassword)){
                    Toast.makeText(getApplicationContext(), "Password not match", Toast.LENGTH_LONG).show();
                    return;
                }


                Intent intent = new Intent(Signup.this, SetUpProfile.class);
                intent.putExtra("Email", Email);
                intent.putExtra("Password", Password);
                startActivity(intent);
            }
        });
    }
}