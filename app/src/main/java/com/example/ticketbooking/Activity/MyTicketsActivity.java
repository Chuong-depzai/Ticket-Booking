package com.example.ticketbooking.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketbooking.R;
import com.example.ticketbooking.databinding.ActivityMyTicketsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MyTicketsActivity extends BaseActivity {

    private ActivityMyTicketsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyTicketsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(v -> finish());

        loadMyTickets();
    }

    private void loadMyTickets() {
        // Lấy uid của user đang đăng nhập
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        binding.progressBar.setVisibility(View.VISIBLE);

        // Query: lấy tất cả booking có userId = uid hiện tại
        FirebaseDatabase.getInstance()
                .getReference("Bookings")
                .orderByChild("userId")
                .equalTo(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        binding.progressBar.setVisibility(View.GONE);

                        ArrayList<HashMap<String, Object>> list = new ArrayList<>();

                        for (DataSnapshot item : snapshot.getChildren()) {
                            HashMap<String, Object> booking = new HashMap<>();
                            booking.put("from", item.child("flightFrom").getValue(String.class));
                            booking.put("to", item.child("flightTo").getValue(String.class));
                            booking.put("seats", item.child("seats").getValue(String.class));
                            booking.put("airline", item.child("airlineName").getValue(String.class));
                            booking.put("price", item.child("totalPrice").getValue());
                            list.add(booking);
                        }

                        if (list.isEmpty()) {
                            binding.emptyLayout.setVisibility(View.VISIBLE);
                            binding.ticketsRecyclerView.setVisibility(View.GONE);
                        } else {
                            binding.emptyLayout.setVisibility(View.GONE);
                            binding.ticketsRecyclerView.setVisibility(View.VISIBLE);
                            setupRecyclerView(list);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void setupRecyclerView(ArrayList<HashMap<String, Object>> list) {
        binding.ticketsRecyclerView.setLayoutManager(
                new LinearLayoutManager(this));
        binding.ticketsRecyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_my_ticket, parent, false);
                return new RecyclerView.ViewHolder(v) {};
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                HashMap<String, Object> booking = list.get(position);
                View v = holder.itemView;

                String from = booking.get("from") != null ? (String) booking.get("from") : "-";
                String to = booking.get("to") != null ? (String) booking.get("to") : "-";

                ((TextView) v.findViewById(R.id.routeTxt)).setText(from + " → " + to);
                ((TextView) v.findViewById(R.id.seatsTxt)).setText(
                        booking.get("seats") != null ? (String) booking.get("seats") : "-");
                ((TextView) v.findViewById(R.id.airlineTxt)).setText(
                        booking.get("airline") != null ? (String) booking.get("airline") : "-");
                ((TextView) v.findViewById(R.id.priceTxt)).setText(
                        "$" + booking.get("price"));
            }

            @Override
            public int getItemCount() { return list.size(); }
        });
    }
}