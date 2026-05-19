package com.example.ticketbooking.repository;

import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class AuthRepository {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<String> errorLiveData;

    public AuthRepository() {
        this.mAuth = FirebaseAuth.getInstance();
        this.mDatabase = FirebaseDatabase.getInstance();
        this.userLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
    }

    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userLiveData.postValue(mAuth.getCurrentUser());
                    } else {
                        errorLiveData.postValue(task.getException().getMessage());
                    }
                });
    }

    public void register(String email, String password, String name) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("uid", uid);
                        userMap.put("email", email);
                        userMap.put("name", name);

                        mDatabase.getReference("Users").child(uid).setValue(userMap)
                                .addOnCompleteListener(dbTask -> {
                                    userLiveData.postValue(mAuth.getCurrentUser());
                                });
                    } else {
                        errorLiveData.postValue(task.getException().getMessage());
                    }
                });
    }

    public void logout() {
        mAuth.signOut();
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() { return userLiveData; }
    public MutableLiveData<String> getErrorLiveData() { return errorLiveData; }
    public FirebaseUser getCurrentUser() { return mAuth.getCurrentUser(); }
}
