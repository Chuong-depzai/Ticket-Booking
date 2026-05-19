package com.example.ticketbooking.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public class FirebaseHelper {
    public static DatabaseReference getDatabase(String node) {
        return FirebaseDatabase.getInstance().getReference(node);
    }
    
    public static FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }
}
