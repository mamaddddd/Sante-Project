package com.example.sante.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sante.Model.VaccineModel;
import com.example.sante.R;

import java.util.List;

public class VaccineAdapter extends RecyclerView.Adapter<VaccineAdapterViewHolder>{


    Context context;
    private List<VaccineModel> vaccineModelList;

    public VaccineAdapter(Context context, List<VaccineModel> vaccineModelList) {
        this.context = context;
        this.vaccineModelList = vaccineModelList;
    }

    @Override
    public VaccineAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaccines_layout,parent,false);
        return new VaccineAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VaccineAdapterViewHolder holder, int position) {


        final VaccineModel vaccineModel = vaccineModelList.get(position);
        holder.textViewTitle.setText(vaccineModel.getTitle());
        holder.textViewDetail.setText("Request a "+vaccineModel.getTitle() +" Vaccine");
        holder.textViewPrice.setText("Rp "+vaccineModel.getPrice()+"-");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,OrderVaccine.class);
                intent.putExtra("VaccineID", vaccineModel.getVaccineID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vaccineModelList.size();
    }
}

class VaccineAdapterViewHolder extends RecyclerView.ViewHolder{

    TextView textViewTitle,textViewDetail,textViewPrice;

    CardView cardView;
    public VaccineAdapterViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewTitle=itemView.findViewById(R.id.txtTitle);
        textViewDetail=itemView.findViewById(R.id.txtdetail);
        textViewPrice=itemView.findViewById(R.id.txtPrice);

        cardView=itemView.findViewById(R.id.cvVaccine);
    }
}