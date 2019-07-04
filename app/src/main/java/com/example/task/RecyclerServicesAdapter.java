package com.example.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerServicesAdapter extends RecyclerView.Adapter<RecyclerServicesAdapter.ServicesViewHolder> {
    private List<Services> servicesList;
    Context context;

    public RecyclerServicesAdapter(List<Services> servicesList, Context context){
        this.servicesList= servicesList;
        this.context = context;
    }

    @Override
    public ServicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View servicesView = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item, parent, false);
        ServicesViewHolder svh = new ServicesViewHolder(servicesView);
        return svh;
    }




    @Override
    public void onBindViewHolder(ServicesViewHolder holder, final int position) {
        holder.imageView.setImageResource(servicesList.get(position).getProductImage());
        holder.txtview.setText(servicesList.get(position).getProductName());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = servicesList.get(position).getProductName().toString();
                Toast.makeText(context, productName + " is being chosen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    public class ServicesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtview;
        public ServicesViewHolder(View view) {
            super(view);
            imageView=view.findViewById(R.id.serviceImage);
            txtview=view.findViewById(R.id.serviceName);
        }
    }
}
