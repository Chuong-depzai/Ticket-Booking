package com.example.ticketbooking.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.ticketbooking.Model.Flight;
import com.example.ticketbooking.R;
import com.example.ticketbooking.databinding.ActivityTicketDetailBinding;

public class TicketDetailActivity extends BaseActivity {
    private ActivityTicketDetailBinding binding;
    private Flight flight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityTicketDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getIntentExtra();
        setVariable();

    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());

        binding.fromTxt.setText(flight.getFromShort());
        binding.fromSmallTxt.setText(flight.getFrom());
        binding.toTxt.setText(flight.getTo());
        binding.toShortTxt.setText(flight.getToShort());
        binding.toSmallTxt.setText(flight.getTo());
        binding.dateTxt.setText(flight.getDate());
        binding.timeTxt.setText(flight.getTime());
        binding.arrivalTxt.setText(flight.getArriveTime());
        binding.classTxt.setText(flight.getClassSeat());
        binding.priceTxt.setText("$" + flight.getPrice());
        binding.airlines.setText(flight.getAirlineName());
        binding.seatsTxt.setText(flight.getPassenger());

        Glide.with(TicketDetailActivity.this)
                .load(flight.getAirlineLogo())
                .into(binding.logo);

        binding.downloadTicketBtn.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(TicketDetailActivity.this)
                    .setTitle("Dat ve thanh cong!")
                    .setMessage(
                            "Chuyen bay: " + flight.getFrom() + " - " + flight.getTo() + "\n" +
                                    "Ngay: " + flight.getDate() + "\n" +
                                    "Ghe: " + flight.getPassenger() + "\n" +
                                    "Tong tien: $" + flight.getPrice() + "\n\n" +
                                    "Ve da duoc luu vao muc Ve cua toi.\n" +
                                    "Chuc ban co chuyen bay vui ve!"
                    )
                    .setPositiveButton("Xem ve cua toi", (dialog, which) -> {
                        Intent intent = new Intent(TicketDetailActivity.this,
                                MyTicketsActivity.class);
                        startActivity(intent);
                    })
                    .setNegativeButton("Dong", null)
                    .show();
        });
    }

    private void getIntentExtra() {
        flight = (Flight) getIntent().getSerializableExtra("flight");
    }
}