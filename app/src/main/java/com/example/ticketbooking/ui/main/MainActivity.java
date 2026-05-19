package com.example.ticketbooking.ui.main;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.example.ticketbooking.ui.BaseActivity;
import com.example.ticketbooking.ui.search.SearchActivity;
import com.example.ticketbooking.ui.ticket.MyTicketsActivity;
import com.example.ticketbooking.ui.profile.ProfileActivity;
import com.example.ticketbooking.model.Location;
import com.example.ticketbooking.R;
import com.example.ticketbooking.viewmodel.MainViewModel;
import com.example.ticketbooking.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends BaseActivity {
    private int adultPassengers = 1, childPassengers = 1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private Calendar calendar = Calendar.getInstance();

    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        initLocationsUI();
        initPassenggers();
        initClassSeat();
        initDatePickup();
        setVariable();
        setupBottomNav();
        setupObservers();
    }

    private void setupObservers() {
        mainViewModel.getUserName().observe(this, name -> {
            if (name != null && !name.isEmpty()) {
                binding.textView4.setText("Xin chào, " + name);
            }
        });

        mainViewModel.getLocations().observe(this, locations -> {
            if (locations != null) {
                ArrayAdapter<Location> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item, locations);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.fromSp.setAdapter(adapter);
                binding.toSp.setAdapter(adapter);
                binding.fromSp.setSelection(1);

                binding.progressBarFrom.setVisibility(View.GONE);
                binding.progressBarTo.setVisibility(View.GONE);
            }
        });
    }

    private void setVariable() {
        binding.searchBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);

            intent.putExtra("from", ((Location) binding.fromSp.getSelectedItem()).getName());
            intent.putExtra("to", ((Location) binding.toSp.getSelectedItem()).getName());
            intent.putExtra("date", binding.departureDateTxt.getText().toString());
            intent.putExtra("numPassenger", adultPassengers + childPassengers);

            startActivity(intent);
        });
    }

    private void initDatePickup() {
        Calendar calendarToday = Calendar.getInstance();
        String currenDate = dateFormat.format(calendarToday.getTime());
        binding.departureDateTxt.setText(currenDate);
        binding.returnDateTxt.setText(currenDate);

        Calendar calendarTomorrow = Calendar.getInstance();
        calendarTomorrow.add(Calendar.DAY_OF_MONTH, 1);
        String tomorrowDate = dateFormat.format(calendarTomorrow.getTime());
        binding.departureDateTxt.setText(tomorrowDate);
        binding.returnDateTxt.setText(tomorrowDate);
        binding.departureDateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(binding.departureDateTxt);
            }
        });

        binding.returnDateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(binding.returnDateTxt);
            }
        });

    }

    private void initClassSeat() {
        binding.progressBarClass.setVisibility(View.VISIBLE);
        ArrayList<String> list = new ArrayList<>();
        list.add("Business Class");
        list.add("Economy Class");
        list.add("First Class");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.classSp.setAdapter(adapter);
        binding.progressBarClass.setVisibility(View.GONE);
    }

    private void initPassenggers() {
        binding.plusAdultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adultPassengers++;
                binding.AdultTxt.setText(adultPassengers + " Adult");
            }
        });
        binding.minusAdultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adultPassengers > 1) {
                    adultPassengers--;
                    binding.AdultTxt.setText(adultPassengers + " Adult");
                }
            }
        });
        binding.plusChildBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childPassengers++;
                binding.childTxt.setText(childPassengers + " Child");
            }
        });
        binding.minusChildBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (childPassengers > 0) {
                    childPassengers--;
                    binding.childTxt.setText(childPassengers + " Child");
                }
            }
        });
    }

    private void initLocationsUI() {
        binding.progressBarFrom.setVisibility(View.VISIBLE);
        binding.progressBarTo.setVisibility(View.VISIBLE);
    }

    private void showDatePickerDialog(TextView textView) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            calendar.set(selectedYear, selectedMonth, selectedDay);
            String formattedDate = dateFormat.format(calendar.getTime());
            textView.setText(formattedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void setupBottomNav() {
        binding.chipNav.setOnItemSelectedListener(id -> {
            if (id == R.id.home) {
                // Đang ở home
            } else if (id == R.id.bookmark) {
                startActivity(new Intent(MainActivity.this, MyTicketsActivity.class));
            } else if (id == R.id.profile) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            } else if (id == R.id.explorer) {
                android.widget.Toast.makeText(this, "Tính năng sắp ra mắt!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
