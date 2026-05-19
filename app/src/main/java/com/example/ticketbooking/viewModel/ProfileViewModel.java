package com.example.ticketbooking.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.ticketbooking.repository.AuthRepository;
import com.example.ticketbooking.repository.MainRepository;
import com.google.firebase.auth.FirebaseUser;

public class ProfileViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    private MainRepository mainRepository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository();
        mainRepository = new MainRepository();
    }

    public FirebaseUser getCurrentUser() {
        return authRepository.getCurrentUser();
    }

    public LiveData<String> getUserName(String uid) {
        return mainRepository.getUserName(uid);
    }

    public void logout() {
        authRepository.logout();
    }
}
