package com.example.sante;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.example.sante.Adapter.MyOrdersAdapter;
import com.example.sante.Adapter.VaccineAdapter;
import com.example.sante.Model.OrderModel;
import com.example.sante.Model.VaccineModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyOrders extends AppCompatActivity {

    RecyclerView recyclerView;
    MyOrdersAdapter myOrdersAdapter;
    List<OrderModel> orderModelList;
    OrderModel orderModel;
    String ID = "";

    Boolean data = false;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyOrders.this);
        ID = preferences.getString("id", "");

        data = true;

        recyclerView = (RecyclerView) findViewById(R.id.RecyclerMyOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        orderModelList=new ArrayList<>();
        myOrdersAdapter = new MyOrdersAdapter(this,orderModelList);
        recyclerView.setAdapter(myOrdersAdapter);
        fetchOrderList();
    }

    private void fetchOrderList() {


        final ProgressDialog progressDialog = new ProgressDialog(MyOrders.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Query query = FirebaseDatabase.getInstance().getReference("OrderedVaccines");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                if (data == true) {
                    orderModelList.clear();
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String VaccineID, OrderDate;
                        if (ID.equals(childSnapshot.child("OrderBy").getValue().toString())) {
                            VaccineID = childSnapshot.child("VaccineID").getValue().toString();
                            OrderDate = childSnapshot.child("OrderDate").getValue().toString();

                            Query query = FirebaseDatabase.getInstance().getReference("Vaccines").child(VaccineID);
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    String Title, Price;
                                    Title = snapshot.child("Name").getValue().toString();
                                    Price = snapshot.child("Price").getValue().toString();

                                    orderModel = new OrderModel(VaccineID, Title, Price, OrderDate);
                                    orderModelList.add(orderModel);
                                    myOrdersAdapter.notifyDataSetChanged();


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }

                    data = false;
                }
                progressDialog.dismiss();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MyOrders.this, MainMenu.class);
        startActivity(intent);
        finish();
    }


}