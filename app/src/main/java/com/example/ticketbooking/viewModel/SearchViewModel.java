package com.example.ticketbooking.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.ticketbooking.model.Flight;
import com.example.ticketbooking.repository.FlightRepository;
import java.util.List;

public class SearchViewModel extends AndroidViewModel {
    private FlightRepository repository;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = new FlightRepository();
    }

    public LiveData<List<Flight>> getFlights(String from, String to) {
        return repository.getFlights(from, to);
    }
}
