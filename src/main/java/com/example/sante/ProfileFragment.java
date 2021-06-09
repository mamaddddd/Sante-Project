package com.example.sante;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {



    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    String ID = "";
    ImageView imageViewUser;
    TextView textViewUsername,textViewEmail,textViewPhone,textViewLocation;
    ProgressDialog progressDialog;
    Button buttonLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        ID = preferences.getString("id", "");

        imageViewUser = (ImageView) view.findViewById(R.id.imageViewUser);
        textViewUsername = (TextView) view.findViewById(R.id.txtUName);
        textViewEmail = (TextView) view.findViewById(R.id.txtemail);
        textViewPhone = (TextView) view.findViewById(R.id.txtphone);
        textViewLocation = (TextView) view.findViewById(R.id.txtaddress);

        buttonLogout = (Button) view.findViewById(R.id.btnLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("isLoggedIn","0");
                editor.apply();
                Intent intent = new Intent(getContext(), LoginORSignup.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        progressDialog = new ProgressDialog(getContext());
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

                Glide.with(getContext()).load(UserImage).into(imageViewUser);
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

        return view;
    }
}