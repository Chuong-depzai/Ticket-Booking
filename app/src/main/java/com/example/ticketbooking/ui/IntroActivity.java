package com.example.ticketbooking.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ticketbooking.ui.auth.LoginActivity;
import com.example.ticketbooking.ui.auth.RegisterActivity;
import com.example.ticketbooking.ui.main.MainActivity;
import com.example.ticketbooking.databinding.ActivityIntroBinding;
import com.google.firebase.auth.FirebaseAuth;

public class IntroActivity extends AppCompatActivity {
    private ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
            finish();
            return;
        }

        binding.startBtn.setOnClickListener(v ->
            startActivity(new Intent(IntroActivity.this, RegisterActivity.class)));
        binding.button4.setOnClickListener(v ->
            startActivity(new Intent(IntroActivity.this, LoginActivity.class)));
    }
}
