package com.example.ticketbooking.ui.ticket;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketbooking.ui.BaseActivity;
import com.example.ticketbooking.R;
import com.example.ticketbooking.viewmodel.MyTicketsViewModel;
import com.example.ticketbooking.databinding.ActivityMyTicketsBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class MyTicketsActivity extends BaseActivity {

    private ActivityMyTicketsBinding binding;
    private MyTicketsViewModel myTicketsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyTicketsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myTicketsViewModel = new ViewModelProvider(this).get(MyTicketsViewModel.class);

        binding.backBtn.setOnClickListener(v -> finish());

        setupObservers();
    }

    private void setupObservers() {
        binding.progressBar.setVisibility(View.VISIBLE);
        myTicketsViewModel.getBookings().observe(this, list -> {
            binding.progressBar.setVisibility(View.GONE);
            if (list != null && !list.isEmpty()) {
                binding.emptyLayout.setVisibility(View.GONE);
                binding.ticketsRecyclerView.setVisibility(View.VISIBLE);
                setupRecyclerView(new ArrayList<>(list));
            } else {
                binding.emptyLayout.setVisibility(View.VISIBLE);
                binding.ticketsRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void setupRecyclerView(ArrayList<HashMap<String, Object>> list) {
        binding.ticketsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.ticketsRecyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_ticket, parent, false);
                return new RecyclerView.ViewHolder(v) {};
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                HashMap<String, Object> booking = list.get(position);
                View v = holder.itemView;

                String from = (String) booking.getOrDefault("from", "-");
                String to = (String) booking.getOrDefault("to", "-");

                ((TextView) v.findViewById(R.id.routeTxt)).setText(from + " → " + to);
                ((TextView) v.findViewById(R.id.seatsTxt)).setText((String) booking.getOrDefault("seats", "-"));
                ((TextView) v.findViewById(R.id.airlineTxt)).setText((String) booking.getOrDefault("airline", "-"));
                ((TextView) v.findViewById(R.id.priceTxt)).setText(booking.get("price") + " VND");
                
                TextView dateTxt = v.findViewById(R.id.dateTxt);
                if (dateTxt != null) {
                    dateTxt.setText((String) booking.getOrDefault("date", "-"));
                }
            }

            @Override
            public int getItemCount() { return list.size(); }
        });
    }
}
