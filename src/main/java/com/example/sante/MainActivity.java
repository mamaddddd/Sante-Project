package com.example.sante;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {


    String isLoggedin = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        isLoggedin = preferences.getString("isLoggedIn", "0");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLoggedin.equals("0")) {
                    Intent intent = new Intent(MainActivity.this, LoginORSignup.class);
                    startActivity(intent);
                    finish();
                }
                else if (isLoggedin.equals("1")){
                    Intent intent = new Intent(MainActivity.this, MainMenu.class);
                    startActivity(intent);
                    finish();
                }
            }
        },3000);


    }
}