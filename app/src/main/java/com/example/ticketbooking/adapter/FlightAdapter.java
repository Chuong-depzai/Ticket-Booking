package com.example.ticketbooking.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ticketbooking.ui.seat.SeatListActivity;
import com.example.ticketbooking.model.Flight;
import com.example.ticketbooking.databinding.ViewholderFlightBinding;

import java.util.List;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.ViewHolder> {

    private List<Flight> list;
    private Context context;

    public FlightAdapter(List<Flight> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public FlightAdapter(List<Flight> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderFlightBinding binding = ViewholderFlightBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Flight flight = list.get(position);

        Glide.with(context)
                .load(flight.getAirlineLogo())
                .into(holder.binding.logo);

        holder.binding.fromShortTxt.setText(flight.getFromShort());
        holder.binding.fromTxt.setText(flight.getFrom());
        holder.binding.toShortTxt.setText(flight.getToShort());
        holder.binding.toTxt.setText(flight.getTo());

        holder.binding.priceTxt.setText(String.valueOf(flight.getPrice()));
        holder.binding.classTxt.setText(flight.getClassSeat());
        holder.binding.arrivalTxt.setText(flight.getArriveTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SeatListActivity.class);
                intent.putExtra("flight", flight);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderFlightBinding binding;

        public ViewHolder(ViewholderFlightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
