package com.example.ticketbooking.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookingRepository {
    private FirebaseDatabase firebaseDatabase;

    public BookingRepository() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    // Lưu thông tin đặt vé vào Firebase
    public MutableLiveData<Boolean> saveBooking(HashMap<String, Object> bookingMap) {
        MutableLiveData<Boolean> resultData = new MutableLiveData<>();
        firebaseDatabase.getReference("Bookings")
                .push() // Tạo ID booking tự động
                .setValue(bookingMap)
                .addOnCompleteListener(task -> {
                    resultData.setValue(task.isSuccessful());
                });
        return resultData;
    }

    // Lấy danh sách vé đã đặt của User
    public MutableLiveData<List<HashMap<String, Object>>> getMyBookings(String uid) {
        MutableLiveData<List<HashMap<String, Object>>> bookingsLiveData = new MutableLiveData<>();
        firebaseDatabase.getReference("Bookings")
                .orderByChild("userId")
                .equalTo(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<HashMap<String, Object>> list = new ArrayList<>();
                        for (DataSnapshot item : snapshot.getChildren()) {
                            HashMap<String, Object> booking = new HashMap<>();
                            booking.put("from", item.child("flightFrom").getValue(String.class));
                            booking.put("to", item.child("flightTo").getValue(String.class));
                            booking.put("seats", item.child("seats").getValue(String.class));
                            booking.put("airline", item.child("airlineName").getValue(String.class));
                            booking.put("price", item.child("totalPrice").getValue());
                            booking.put("date", item.child("flightDate").getValue(String.class));
                            list.add(booking);
                        }
                        bookingsLiveData.setValue(list);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        bookingsLiveData.setValue(null);
                    }
                });
        return bookingsLiveData;
    }
}
