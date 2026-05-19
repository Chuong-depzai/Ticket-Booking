package com.example.ticketbooking.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ticketbooking.ui.IntroActivity;
import com.example.ticketbooking.databinding.ActivityProfileBinding;
import com.example.ticketbooking.viewmodel.ProfileViewModel;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding.backBtn.setOnClickListener(v -> finish());

        setupObservers();
        loadUserInfo();

        binding.logoutBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có chắc muốn đăng xuất không?")
                    .setPositiveButton("Đăng xuất", (d, w) -> {
                        profileViewModel.logout();
                        Intent intent = new Intent(ProfileActivity.this, IntroActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    private void setupObservers() {
        FirebaseUser user = profileViewModel.getCurrentUser();
        if (user != null) {
            profileViewModel.getUserName(user.getUid()).observe(this, name -> {
                if (name != null && !name.isEmpty()) {
                    binding.nameTxt.setText(name);
                    binding.avatarTxt.setText(String.valueOf(name.charAt(0)).toUpperCase());
                } else {
                    binding.nameTxt.setText(user.getEmail() != null ? user.getEmail() : "Người dùng");
                }
            });
        }
    }

    private void loadUserInfo() {
        FirebaseUser user = profileViewModel.getCurrentUser();
        if (user == null) return;

        String email = user.getEmail();
        binding.emailTxt.setText(email != null ? email : "-");

        if (email != null && !email.isEmpty()) {
            binding.avatarTxt.setText(String.valueOf(email.charAt(0)).toUpperCase());
        }

        // Tên người dùng sẽ được cập nhật bởi setupObservers() khi dữ liệu từ Firebase trả về
    }
}
