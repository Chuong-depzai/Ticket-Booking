package com.example.ticketbooking.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ticketbooking.databinding.ActivityIntroBinding;

public class IntroActivity extends AppCompatActivity   {
    private ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.startBtn.setOnClickListener(v -> startActivity(new Intent(IntroActivity.this, RegisterActivity.class)));        binding.button4.setOnClickListener(v ->
                startActivity(new Intent(IntroActivity.this, LoginActivity.class)));
    }

}
