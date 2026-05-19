package com.example.ticketbooking.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.ticketbooking.repository.BookingRepository;
import com.google.firebase.auth.FirebaseAuth;
import java.util.HashMap;
import java.util.List;

public class MyTicketsViewModel extends AndroidViewModel {
    private BookingRepository repository;

    public MyTicketsViewModel(@NonNull Application application) {
        super(application);
        repository = new BookingRepository();
    }

    public LiveData<List<HashMap<String, Object>>> getBookings() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            return repository.getMyBookings(uid);
        }
        return null;
    }
}
