package com.example.sante;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sante.Adapter.VaccineAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {



    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    CardView cardViewVaccines,cardViewProfile,cardViewOrders;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        cardViewVaccines = (CardView) view.findViewById(R.id.cvVaccines);
        cardViewVaccines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), Vaccines.class);
                startActivity(intent);
            }
        });

        cardViewProfile = (CardView) view.findViewById(R.id.cvProfile);
        cardViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), Profile.class);
                startActivity(intent);
            }
        });

        cardViewOrders = (CardView) view.findViewById(R.id.cvOrders);
        cardViewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), MyOrders.class);
                startActivity(intent);
            }
        });



        return view;
    }
}