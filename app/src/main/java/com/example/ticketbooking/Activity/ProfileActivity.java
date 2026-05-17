package com.example.ticketbooking.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ticketbooking.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(v -> finish());

        loadUserInfo();

        // Nút đăng xuất
        binding.logoutBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có chắc muốn đăng xuất không?")
                    .setPositiveButton("Đăng xuất", (d, w) -> {
                        FirebaseAuth.getInstance().signOut();
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

    private void loadUserInfo() {
        com.google.firebase.auth.FirebaseUser user =
                com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) return;

        String uid = user.getUid();
        String email = user.getEmail();

        // Hiện email ngay
        binding.emailTxt.setText(email != null ? email : "-");

        // Lấy chữ cái đầu để hiện avatar
        if (email != null && !email.isEmpty()) {
            binding.avatarTxt.setText(String.valueOf(email.charAt(0)).toUpperCase());
        }

        // Lấy tên từ Firebase
        com.google.firebase.database.FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(uid)
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(@androidx.annotation.NonNull
                                             com.google.firebase.database.DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String name = snapshot.child("name").getValue(String.class);
                            if (name != null && !name.isEmpty()) {
                                binding.nameTxt.setText(name);
                                // Cập nhật avatar theo chữ cái đầu của tên
                                binding.avatarTxt.setText(
                                        String.valueOf(name.charAt(0)).toUpperCase());
                            } else {
                                binding.nameTxt.setText("Chưa cập nhật tên");
                            }
                        } else {
                            // Không có trong database → dùng email
                            binding.nameTxt.setText(email != null ? email : "Người dùng");
                        }
                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull
                                            com.google.firebase.database.DatabaseError error) {
                        binding.nameTxt.setText("Lỗi tải dữ liệu");
                    }
                });
    }
}