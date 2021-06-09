package com.example.sante.Adapter;

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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sante.MainMenu;
import com.example.sante.Profile;
import com.example.sante.R;
import com.example.sante.SetUpProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrderVaccine extends AppCompatActivity {

    String VaccineID;
    TextView textViewTitle,textViewDetail,textViewPrice;
    DatabaseReference databaseReference;

    String ID = "";
    EditText editTextName,editTextPhone,editTextBloodGroup,editTextAge,editTextAddress;
    Button buttonOrder;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_vaccine);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(OrderVaccine.this);
        ID = preferences.getString("id", "");

        ProgressDialog progressDialog = new ProgressDialog(OrderVaccine.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        VaccineID = getIntent().getExtras().getString("VaccineID");

        textViewTitle = (TextView) findViewById(R.id.txtTitle);
        textViewDetail = (TextView) findViewById(R.id.txtdetail);
        textViewPrice = (TextView) findViewById(R.id.txtPrice);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Vaccines").child(VaccineID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                textViewTitle.setText(dataSnapshot.child("Name").getValue().toString());
                textViewDetail.setText("Request a "+dataSnapshot.child("Name").getValue().toString() +" Vaccine");
                textViewPrice.setText("Rp "+dataSnapshot.child("Price").getValue().toString()+"-");
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

            }
        });



        editTextName = (EditText) findViewById(R.id.etName);
        editTextPhone = (EditText) findViewById(R.id.etPhone);
        editTextBloodGroup = (EditText) findViewById(R.id.etBloodGroup);
        editTextAge = (EditText) findViewById(R.id.etAge);
        editTextAddress = (EditText) findViewById(R.id.etAddress);

        buttonOrder = (Button) findViewById(R.id.btnOrder);
        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name,Phone,BloodGroup,Age,Address;

                Name = editTextName.getText().toString().trim();
                Phone = editTextPhone.getText().toString().trim();
                BloodGroup = editTextBloodGroup.getText().toString().trim();
                Age = editTextAge.getText().toString().trim();
                Address = editTextAddress.getText().toString().trim();

                if (TextUtils.isEmpty(Name)) {
                    Toast.makeText(getApplicationContext(), "Please Enter your Name", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(Phone)) {
                    Toast.makeText(getApplicationContext(), "Please Enter your Phone", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(BloodGroup)) {
                    Toast.makeText(getApplicationContext(), "Please Enter your BloodGroup", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(Age)) {
                    Toast.makeText(getApplicationContext(), "Please Enter your Age", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(Address)) {
                    Toast.makeText(getApplicationContext(), "Please Enter your Address", Toast.LENGTH_LONG).show();
                    return;
                }


                String currentDate = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(new Date());

                FirebaseDatabase database =  FirebaseDatabase.getInstance();
                Long tsLong = System.currentTimeMillis()/1000;
                String OrderId = tsLong.toString();
                DatabaseReference mRef =  database.getReference().child("OrderedVaccines").child(OrderId);
                mRef.child("OrderBy").setValue(ID);
                mRef.child("VaccineID").setValue(VaccineID);
                mRef.child("Name").setValue(Name);
                mRef.child("Phone").setValue(Phone);
                mRef.child("BloodGroup").setValue(BloodGroup);
                mRef.child("Age").setValue(Age);
                mRef.child("Address").setValue(Address);
                mRef.child("OrderDate").setValue(currentDate);
                Intent intent = new Intent(OrderVaccine.this, MainMenu.class);
                startActivity(intent);
                Toast.makeText(OrderVaccine.this, "Successfully Ordered", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();



            }
        });


    }
}