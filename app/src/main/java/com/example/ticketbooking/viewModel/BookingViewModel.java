package com.example.ticketbooking.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.ticketbooking.repository.BookingRepository;
import java.util.HashMap;

public class BookingViewModel extends AndroidViewModel {
    private BookingRepository repository;
    private MutableLiveData<Boolean> bookingResult = new MutableLiveData<>();

    public BookingViewModel(@NonNull Application application) {
        super(application);
        repository = new BookingRepository();
    }

    public void createBooking(HashMap<String, Object> bookingMap) {
        repository.saveBooking(bookingMap).observeForever(result -> {
            bookingResult.setValue(result);
        });
    }

    public LiveData<Boolean> getBookingResult() {
        return bookingResult;
    }
}
