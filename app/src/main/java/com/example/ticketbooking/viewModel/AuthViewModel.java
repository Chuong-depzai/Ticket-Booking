package com.example.ticketbooking.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.ticketbooking.repository.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends AndroidViewModel {
    private AuthRepository repository;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<String> errorLiveData;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        repository = new AuthRepository();
        userLiveData = repository.getUserLiveData();
        errorLiveData = repository.getErrorLiveData();
    }

    public void login(String email, String password) {
        repository.login(email, password);
    }

    public void register(String email, String password, String name) {
        repository.register(email, password, name);
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() { return userLiveData; }
    public MutableLiveData<String> getErrorLiveData() { return errorLiveData; }
    public boolean isUserLoggedIn() { return repository.getCurrentUser() != null; }
}
