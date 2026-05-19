package com.example.ticketbooking.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.ticketbooking.model.Location;
import com.example.ticketbooking.repository.MainRepository;
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private MainRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new MainRepository();
    }

    public LiveData<List<Location>> getLocations() {
        return repository.getLocations();
    }

    public LiveData<String> getUserName() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            return repository.getUserName(uid);
        }
        return new MutableLiveData<>("");
    }
}
