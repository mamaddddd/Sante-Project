package com.example.sante;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;
import java.util.Locale;

public class SetUpProfile extends AppCompatActivity {

    Button buttonConfirm;
    ImageButton imageButtonGetLocation;
    String Email,Password;

    Uri imageuri;
    private FirebaseAuth mAuth;

    ProgressDialog progressDialog;
    String Image = null;
    private static final int PICK_IMAGE = 1;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    ImageView imageViewProfile;
    EditText editTextUsername,editTextPhone,editTextLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_profile);

        mAuth = FirebaseAuth.getInstance();

        Email = getIntent().getExtras().getString("Email");
        Password = getIntent().getExtras().getString("Password");

        Toast.makeText(this, ""+Email+Password, Toast.LENGTH_SHORT).show();
        imageViewProfile = (ImageView) findViewById(R.id.imageView);
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGallery = new Intent();
                intentGallery.setType("image/*");
                intentGallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intentGallery,"Select Image"),PICK_IMAGE);

            }
        });


        editTextUsername = (EditText) findViewById(R.id.etUsername);
        editTextLocation = (EditText) findViewById(R.id.etLocation);
        editTextPhone = (EditText) findViewById(R.id.etPhone);


        imageButtonGetLocation = (ImageButton) findViewById(R.id.btnGetLocation);
        imageButtonGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(
                            getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SetUpProfile.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_CODE_LOCATION_PERMISSION);
                    } else {
                        editTextLocation.setText("");
                        getCurrentLocation();
                    }
            }
        });


        buttonConfirm = (Button)findViewById(R.id.btnConfirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String Username, Phone, location;

                Username = editTextUsername.getText().toString().trim();
                Phone = editTextPhone.getText().toString().trim();
                location = editTextLocation.getText().toString().trim();


                if (TextUtils.isEmpty(Username)) {
                    Toast.makeText(getApplicationContext(), "Please Enter your Username", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(Phone)) {
                    Toast.makeText(getApplicationContext(), "Please Enter your Phone Number", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(location)) {
                    Toast.makeText(getApplicationContext(), "Please press button to get location", Toast.LENGTH_LONG).show();
                    return;
                }

                RegisterUser();

            }
        });

    }

    private void RegisterUser() {
        ProgressDialog progressDialog = new ProgressDialog(SetUpProfile.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        Long tsLong = System.currentTimeMillis()/1000;
        String timeStamp = tsLong.toString();

        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                    StorageReference storageReferenceProfilePic = firebaseStorage.getReference();
                    StorageReference imageRef = storageReferenceProfilePic.child("Users Images" + "/" + timeStamp + ".jpg");

                    imageRef.putFile(imageuri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Uri downloadUrl = uri;

                                            FirebaseDatabase database =  FirebaseDatabase.getInstance();
                                            DatabaseReference mRef =  database.getReference().child("Users").child("id_"+timeStamp);
                                            mRef.child("Email").setValue(Email);
                                            mRef.child("Username").setValue(editTextUsername.getText().toString().trim());
                                            mRef.child("Phone").setValue(editTextPhone.getText().toString().trim());
                                            mRef.child("Location").setValue(editTextLocation.getText().toString().trim());
                                            mRef.child("UserImage").setValue(downloadUrl.toString());
                                            Toast.makeText(SetUpProfile.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SetUpProfile.this, Login.class);
                                            startActivity(intent);
                                            progressDialog.dismiss();
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    progressDialog.dismiss();
                                    Toast.makeText(SetUpProfile.this, exception.getCause().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                }
                            });


                }else {

                    Toast.makeText(SetUpProfile.this, "Failed please try aagain", Toast.LENGTH_SHORT).show();


                }
            }
        });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            imageuri = data.getData();
            imageViewProfile.setImageURI(imageuri);
            Image = "Done";
        }
    }

    public void getCurrentLocation() {

        progressDialog = new ProgressDialog(SetUpProfile.this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        LocationServices.getFusedLocationProviderClient(SetUpProfile.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(SetUpProfile.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestlocationindex = locationResult.getLocations().size() - 1;

                            double latitude = locationResult.getLocations().get(latestlocationindex).getLatitude();
                            double longitude = locationResult.getLocations().get(latestlocationindex).getLongitude();


                            getCompleteAddressString(latitude, longitude);
                        } else {

                            progressDialog.dismiss();
                        }
                    }
                }, Looper.getMainLooper());
    }

    private void getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            Toast.makeText(this, "sdasd", Toast.LENGTH_SHORT).show();
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                editTextLocation.setText(strReturnedAddress.toString());
                progressDialog.dismiss();

            } else {

                progressDialog.dismiss();
                Toast.makeText(SetUpProfile.this, "Try Again", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();

            progressDialog.dismiss();
            Toast.makeText(SetUpProfile.this, "Try Again", Toast.LENGTH_SHORT).show();
        }
    }
}