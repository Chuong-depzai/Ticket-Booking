package com.example.ticketbooking.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.ticketbooking.model.Location;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainRepository {
    private FirebaseDatabase firebaseDatabase;

    public MainRepository() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public MutableLiveData<List<Location>> getLocations() {
        MutableLiveData<List<Location>> mutableLiveData = new MutableLiveData<>();
        DatabaseReference myRef = firebaseDatabase.getReference("Locations");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Location> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    list.add(dataSnapshot.getValue(Location.class));
                }
                mutableLiveData.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<String> getUserName(String uid) {
        MutableLiveData<String> nameData = new MutableLiveData<>();
        firebaseDatabase.getReference("Users").child(uid).child("name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            nameData.setValue(snapshot.getValue(String.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        return nameData;
    }
}
