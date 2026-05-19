package com.example.ticketbooking.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ticketbooking.ui.main.MainActivity;
import com.example.ticketbooking.viewmodel.AuthViewModel;
import com.example.ticketbooking.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        setupObservers();
        setupListeners();
    }

    private void setupObservers() {
        authViewModel.getUserLiveData().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                binding.progressBar.setVisibility(View.GONE);
                binding.registerBtn.setEnabled(true);
                Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        authViewModel.getErrorLiveData().observe(this, errorMessage -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.registerBtn.setEnabled(true);
            if (errorMessage != null) {
                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupListeners() {
        binding.registerBtn.setOnClickListener(v -> {
            String fullName = binding.fullNameInput.getText().toString().trim();
            String email = binding.emailInput.getText().toString().trim();
            String password = binding.passwordInput.getText().toString().trim();
            String confirmPass = binding.confirmPasswordInput.getText().toString().trim();

            if (TextUtils.isEmpty(fullName)) {
                binding.fullNameInput.setError("Vui lòng nhập họ tên");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                binding.emailInput.setError("Vui lòng nhập email");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                binding.passwordInput.setError("Vui lòng nhập mật khẩu");
                return;
            }
            if (password.length() < 6) {
                binding.passwordInput.setError("Mật khẩu ít nhất 6 ký tự");
                return;
            }
            if (!password.equals(confirmPass)) {
                binding.confirmPasswordInput.setError("Mật khẩu không khớp");
                return;
            }

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.registerBtn.setEnabled(false);

            authViewModel.register(email, password, fullName);
        });

        binding.backToLoginTxt.setOnClickListener(v -> finish());
    }
}
