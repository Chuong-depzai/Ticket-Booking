package com.example.ticketbooking.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.ticketbooking.model.Flight;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FlightRepository {
    private FirebaseDatabase firebaseDatabase;

    public FlightRepository() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public MutableLiveData<List<Flight>> getFlights(String from, String to) {
        MutableLiveData<List<Flight>> mutableLiveData = new MutableLiveData<>();
        DatabaseReference myRef = firebaseDatabase.getReference("Flights");
        
        Query query = myRef.orderByChild("from").equalTo(from);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Flight> flightList = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Flight flight = issue.getValue(Flight.class);
                        if (flight != null && flight.getTo().equals(to)) {
                            flightList.add(flight);
                        }
                    }
                }
                mutableLiveData.setValue(flightList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }
}
