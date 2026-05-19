package com.example.ticketbooking.ui.search;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ticketbooking.ui.BaseActivity;
import com.example.ticketbooking.adapter.FlightAdapter;
import com.example.ticketbooking.viewmodel.SearchViewModel;
import com.example.ticketbooking.databinding.ActivitySearchBinding;

public class SearchActivity extends BaseActivity {
    private ActivitySearchBinding binding;
    private SearchViewModel searchViewModel;
    private String from, to, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo ViewModel
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        getIntentExtra();
        setVariable();
        setupObservers();
    }

    private void setupObservers() {
        binding.progressBarSearch.setVisibility(View.VISIBLE);

        // Quan sát danh sách chuyến bay từ ViewModel
        searchViewModel.getFlights(from, to).observe(this, flights -> {
            binding.progressBarSearch.setVisibility(View.GONE);
            
            if (flights != null && !flights.isEmpty()) {
                binding.searchView.setLayoutManager(new LinearLayoutManager(SearchActivity.this, 
                        LinearLayoutManager.VERTICAL, false));
                binding.searchView.setAdapter(new FlightAdapter(flights));
            } else {
                binding.searchView.setAdapter(null);
            }
        });
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void getIntentExtra() {
        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");
        date = getIntent().getStringExtra("date");
    }
}
