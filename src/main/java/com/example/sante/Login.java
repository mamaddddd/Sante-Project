package com.example.sante;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Button buttonLogin;
    EditText editTextEmail,editTextPassword;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.etEmailAddress);
        editTextPassword = (EditText) findViewById(R.id.etPassword);

        buttonLogin = (Button) findViewById(R.id.btnLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email,Password;

                Email = editTextEmail.getText().toString().trim();
                Password = editTextPassword.getText().toString().trim();


                if (TextUtils.isEmpty(Email)) {
                    Toast.makeText(getApplicationContext(), "Please Enter your Email", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(Password)) {
                    Toast.makeText(getApplicationContext(), "Please Enter your Password", Toast.LENGTH_LONG).show();
                    return;
                }


                UserLogin();

            }
        });

    }

    private void UserLogin() {


        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString().trim(),editTextPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user =  mAuth.getCurrentUser();
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users");
                    Query query = db.orderByChild("Email").equalTo(editTextEmail.getText().toString().trim());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String UserID = null;
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                UserID = child.getKey();
                            }
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("id",UserID);
                            editor.putString("isLoggedIn","1");
                            editor.apply();
                            Intent intent = new Intent(Login.this, MainMenu.class);
                            startActivity(intent);
                            finish();

                            progressDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else {
                    progressDialog.dismiss();
                    if (task.getException().getMessage().equals("The email address is badly formatted")){
                        Toast.makeText(Login.this, "The email address is badly formatted", Toast.LENGTH_SHORT).show();
                    }
                    else if (task.getException().getMessage().equals("The password is invalid")){
                        Toast.makeText(Login.this, "The password is invalid", Toast.LENGTH_SHORT).show();
                    }
                    else if (task.getException().getMessage().equals("There is no user record")){
                        Toast.makeText(Login.this, "There is no user record", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}