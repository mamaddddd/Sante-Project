package com.example.sante.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sante.Model.OrderModel;
import com.example.sante.Model.VaccineModel;
import com.example.sante.R;

import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrderAdapterViewHolder>{


    Context context;
    private List<OrderModel> orderModelList;

    public MyOrdersAdapter(Context context, List<OrderModel> orderModelList) {
        this.context = context;
        this.orderModelList = orderModelList;
    }

    @Override
    public MyOrderAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ordered_vaccines_layout,parent,false);
        return new MyOrderAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapterViewHolder holder, int position) {
        final OrderModel orderModel = orderModelList.get(position);
        holder.textViewTitle.setText(orderModel.getTitle());
        holder.textViewDetail.setText("Ordered a "+orderModel.getTitle() +" Vaccine");
        holder.textViewPrice.setText("Rp "+orderModel.getPrice()+"-");
        holder.textViewDate.setText("Order Date: "+orderModel.getDate());
    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }
}

class MyOrderAdapterViewHolder extends RecyclerView.ViewHolder{

    TextView textViewTitle,textViewDetail,textViewPrice,textViewDate;

    public MyOrderAdapterViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewTitle=itemView.findViewById(R.id.txtTitle);
        textViewDetail=itemView.findViewById(R.id.txtdetail);
        textViewPrice=itemView.findViewById(R.id.txtPrice);
        textViewDate=itemView.findViewById(R.id.txtOrderDate);

    }
}