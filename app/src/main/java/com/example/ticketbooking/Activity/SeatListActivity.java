package com.example.ticketbooking.Activity;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;

import com.example.ticketbooking.Adapter.SeatAdapter;
import com.example.ticketbooking.Model.Flight;
import com.example.ticketbooking.Model.Seat;
import com.example.ticketbooking.databinding.ActivitySeatListBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeatListActivity extends BaseActivity {
     private ActivitySeatListBinding binding;
     private Flight flight;
     private Double price = 0.0;
    private double num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivitySeatListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        initSeatList();
        setVariable();

    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
        binding.confrimBtn.setOnClickListener(v -> {
            if (num > 0) {
                flight.setPassenger(binding.nameSelectedTxt.getText().toString());
                flight.setPrice(price);

                Intent intent = new Intent(SeatListActivity.this, TicketDetailActivity.class);

                intent.putExtra("flight", flight);

                startActivity(intent);
            } else {
                Toast.makeText(SeatListActivity.this,"Hãy chọn chỗ ngồi của bạn", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initSeatList() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,7);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position){
                return (position % 7 == 3)? 1 : 1;
            }
        });

        binding.seatRecyclerView.setLayoutManager(gridLayoutManager);
        List<Seat> seatList = new ArrayList<>();
        int row = 0;
        int numberSeat = flight.getNumberSeat() + (flight.getNumberSeat() % 7)+1;

        Map<Integer,String> seatAlphabeMap= new HashMap<>();
        seatAlphabeMap.put(0,"A");
        seatAlphabeMap.put(1,"B");
        seatAlphabeMap.put(2,"C");

        seatAlphabeMap.put(4,"D");
        seatAlphabeMap.put(5,"E");
        seatAlphabeMap.put(6,"F");

        for (int i = 0; i < numberSeat; i++) {
            if (i % 7 == 0) {
                row++;
            }
            if (i % 7 == 3) {
                seatList.add(new Seat(Seat.SeatStatus.EMPLY, String.valueOf(row)));
            } else {
                String seatName = seatAlphabeMap.get(i % 7) + String.valueOf(row);
                seatList.add(new Seat(Seat.SeatStatus.AVAILABLE, seatName));
                Seat.SeatStatus seatStatus = flight.getReservedSeats().contains(seatName) ? Seat.SeatStatus.UNAVAILABLE : Seat.SeatStatus.AVAILABLE;
                seatList.add(new Seat(seatStatus, seatName));
            }
        }


        SeatAdapter seatAdapter = new SeatAdapter(seatList,this, (selectedName, num) -> {

            binding.numberSelectedTxt.setText(num + " Chỗ");
            binding.nameSelectedTxt.setText(selectedName);
            DecimalFormat df = new DecimalFormat("#.##");
            price = (Double.valueOf(df.format(num * flight.getPrice())));
            binding.priceTxt.setText(df.format(price) + "VND");
            this.num = num;
            binding.priceTxt.setText(price + "VND");


        });
        binding.seatRecyclerView.setAdapter(seatAdapter);
        binding.seatRecyclerView.setNestedScrollingEnabled(false);




    }
    private void getIntentExtra() {
        flight = (Flight) getIntent().getSerializableExtra("flight");

    }
}