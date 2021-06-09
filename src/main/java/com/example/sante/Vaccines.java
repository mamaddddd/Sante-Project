package com.example.sante;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.sante.Adapter.VaccineAdapter;
import com.example.sante.Model.VaccineModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Vaccines extends AppCompatActivity {

    RecyclerView recyclerView;
    VaccineAdapter vaccineAdapter;
    List<VaccineModel> vaccineModelList;
    VaccineModel vaccineModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccines);


        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewVaccine);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        vaccineModelList=new ArrayList<>();
        vaccineAdapter = new VaccineAdapter(this,vaccineModelList);
        recyclerView.setAdapter(vaccineAdapter);
        fetchVaccineList();
    }

    private void fetchVaccineList() {

        final ProgressDialog progressDialog = new ProgressDialog(Vaccines.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Query query = FirebaseDatabase.getInstance().getReference("Vaccines");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                progressDialog.dismiss();

                    vaccineModelList.clear();
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String VaccineID, Title, Price;
                        VaccineID = childSnapshot.getKey().toString();
                        Title = childSnapshot.child("Name").getValue().toString();
                        Price = childSnapshot.child("Price").getValue().toString();

                            vaccineModel = new VaccineModel(VaccineID, Title, Price);
                            vaccineModelList.add(vaccineModel);
                            vaccineAdapter.notifyDataSetChanged();

                    }



            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}