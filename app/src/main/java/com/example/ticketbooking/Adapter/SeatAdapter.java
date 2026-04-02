package com.example.ticketbooking.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketbooking.Model.Seat;
import com.example.ticketbooking.R;
import com.example.ticketbooking.databinding.SeatItemBinding;


import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder>{

    private final List<Seat> seatList;

    private final Context context;

    private ArrayList<String> selectedSeats = new ArrayList<>();

    private SelectedSeat selectedSeat;

    public SeatAdapter(List<Seat> seatList, Context context, SelectedSeat selectedSeat) {
        this.seatList = seatList;
        this.context = context;
        this.selectedSeat = selectedSeat;
    }

    @NonNull
    @Override
    public SeatAdapter.SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SeatItemBinding binding = SeatItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new SeatViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        Seat seat = seatList.get(position);
        holder.binding.seatImageView.setText(seat.getName());
        switch (seat.getStatus()){

            case AVAILABLE:
                holder.binding.seatImageView.setBackgroundResource(R.drawable.ic_seat_availble);
                holder.binding.seatImageView.setTextColor(context.getResources().getColor(R.color.white));
                break;
            case SELECTED:
                holder.binding.seatImageView.setBackgroundResource(R.drawable.ic_seat_selected);
                holder.binding.seatImageView.setTextColor(context.getResources().getColor(R.color.white));
                break;
            case UNAVAILABLE:
                holder.binding.seatImageView.setBackgroundResource(R.drawable.ic_seat_unavailble);
                holder.binding.seatImageView.setTextColor(context.getResources().getColor(R.color.grey));
                break;
            case EMPLY:
                holder.binding.seatImageView.setBackgroundResource(R.drawable.ic_seat_empty);
                holder.binding.seatImageView.setTextColor(Color.parseColor("#00000000"));
                break;


        }
        holder.binding.seatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();

                if(seat.getStatus() == Seat.SeatStatus.AVAILABLE){

                    seat.setStatus(Seat.SeatStatus.SELECTED);
                    selectedSeats.add(seat.getName());
                    notifyItemChanged(pos);


                }else if(seat.getStatus() == Seat.SeatStatus.SELECTED){

                    seat.setStatus(Seat.SeatStatus.AVAILABLE);
                    selectedSeats.remove(seat.getName());
                    notifyItemChanged(pos);

                }

                String selected = selectedSeats.toString()
                        .replace("[","")
                        .replace("]","")
                        .replace(",","");

                selectedSeat.Return(selected,selectedSeats.size());




            }


        });

    }

    @Override
    public int getItemCount() {
        return seatList.size();
    }

    public class SeatViewHolder extends RecyclerView.ViewHolder {
        SeatItemBinding binding;
        public SeatViewHolder(@NonNull SeatItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
    public interface SelectedSeat{
        void Return(String selectedName, int num);
    }
}