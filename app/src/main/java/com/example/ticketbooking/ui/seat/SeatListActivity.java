package com.example.ticketbooking.ui.seat;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.ticketbooking.ui.BaseActivity;
import com.example.ticketbooking.ui.ticket.TicketDetailActivity;
import com.example.ticketbooking.adapter.SeatAdapter;
import com.example.ticketbooking.model.Flight;
import com.example.ticketbooking.model.Seat;
import com.example.ticketbooking.viewmodel.BookingViewModel;
import com.example.ticketbooking.databinding.ActivitySeatListBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeatListActivity extends BaseActivity {
    private ActivitySeatListBinding binding;
    private BookingViewModel bookingViewModel;
    private Flight flight;
    private Double price = 0.0;
    private double num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySeatListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);

        getIntentExtra();
        initSeatList();
        setVariable();
        setupObservers();
    }

    private void setupObservers() {
        bookingViewModel.getBookingResult().observe(this, isSuccess -> {
            binding.confrimBtn.setEnabled(true);
            binding.confrimBtn.setText("Chọn Chỗ Ngồi");

            if (isSuccess != null && isSuccess) {
                Toast.makeText(SeatListActivity.this, "Đặt vé thành công!", Toast.LENGTH_SHORT).show();
                goToTicketDetail();
            } else {
                Toast.makeText(SeatListActivity.this, "Đặt vé thất bại, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
        binding.confrimBtn.setOnClickListener(v -> {
            if (num > 0) {
                flight.setPassenger(binding.nameSelectedTxt.getText().toString());
                flight.setPrice(price);
                prepareBooking();
            } else {
                Toast.makeText(SeatListActivity.this, "Hãy chọn chỗ ngồi của bạn", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prepareBooking() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            goToTicketDetail();
            return;
        }

        binding.confrimBtn.setEnabled(false);
        binding.confrimBtn.setText("Đang xử lý...");

        HashMap<String, Object> bookingMap = new HashMap<>();
        bookingMap.put("userId", auth.getCurrentUser().getUid());
        bookingMap.put("flightFrom", flight.getFrom());
        bookingMap.put("flightTo", flight.getTo());
        bookingMap.put("flightDate", flight.getDate());
        bookingMap.put("seats", binding.nameSelectedTxt.getText().toString());
        bookingMap.put("totalPrice", price);
        bookingMap.put("airlineName", flight.getAirlineName());
        bookingMap.put("bookingTime", System.currentTimeMillis());
        bookingMap.put("status", "confirmed");

        bookingViewModel.createBooking(bookingMap);
    }

    private void initSeatList() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });

        binding.seatRecyclerView.setLayoutManager(gridLayoutManager);
        List<Seat> seatList = new ArrayList<>();
        int row = 0;
        int numberSeat = flight.getNumberSeat() + (flight.getNumberSeat() % 7) + 1;

        Map<Integer, String> seatAlphabeMap = new HashMap<>();
        seatAlphabeMap.put(0, "A"); seatAlphabeMap.put(1, "B"); seatAlphabeMap.put(2, "C");
        seatAlphabeMap.put(4, "D"); seatAlphabeMap.put(5, "E"); seatAlphabeMap.put(6, "F");

        for (int i = 0; i < numberSeat; i++) {
            if (i % 7 == 0) row++;
            if (i % 7 == 3) {
                seatList.add(new Seat(Seat.SeatStatus.EMPLY, String.valueOf(row)));
            } else {
                String seatName = seatAlphabeMap.get(i % 7) + String.valueOf(row);
                Seat.SeatStatus seatStatus = flight.getReservedSeats().contains(seatName)
                        ? Seat.SeatStatus.UNAVAILABLE : Seat.SeatStatus.AVAILABLE;
                seatList.add(new Seat(seatStatus, seatName));
            }
        }

        SeatAdapter seatAdapter = new SeatAdapter(seatList, this, (selectedName, num) -> {
            binding.numberSelectedTxt.setText(num + " Chỗ");
            binding.nameSelectedTxt.setText(selectedName);
            DecimalFormat df = new DecimalFormat("#.##");
            price = Double.valueOf(df.format(num * flight.getPrice()));
            this.num = num;
            binding.priceTxt.setText(df.format(price) + " VND");
        });
        binding.seatRecyclerView.setAdapter(seatAdapter);
        binding.seatRecyclerView.setNestedScrollingEnabled(false);
    }

    private void goToTicketDetail() {
        Intent intent = new Intent(SeatListActivity.this, TicketDetailActivity.class);
        intent.putExtra("flight", flight);
        startActivity(intent);
    }

    private void getIntentExtra() {
        flight = (Flight) getIntent().getSerializableExtra("flight");
    }
}
