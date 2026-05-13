package com.example.ticketbooking.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ticketbooking.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        // Nút ĐĂNG KÝ
        binding.registerBtn.setOnClickListener(v -> {
            String fullName = binding.fullNameInput.getText().toString().trim();
            String email = binding.emailInput.getText().toString().trim();
            String password = binding.passwordInput.getText().toString().trim();
            String confirmPass = binding.confirmPasswordInput.getText().toString().trim();

            // Kiểm tra từng ô
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

            // Hiện loading
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.registerBtn.setEnabled(false);

            // Tạo tài khoản trên Firebase Auth
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Lấy uid của user vừa tạo

                            mAuth.getCurrentUser().sendEmailVerification();
                            Toast.makeText(RegisterActivity.this,
                                    "Đã gửi email xác thực tới " + email + "\nVui lòng kiểm tra hộp thư!",
                                    Toast.LENGTH_LONG).show();

                            // Lấy uid của user vừa tạo
                            String uid = mAuth.getCurrentUser().getUid();
                            // Lưu thêm tên + email vào Realtime Database
                            HashMap<String, Object> userMap = new HashMap<>();
                            userMap.put("name", fullName);
                            userMap.put("email", email);

                            FirebaseDatabase.getInstance()
                                    .getReference("Users")
                                    .child(uid)
                                    .setValue(userMap)
                                    .addOnCompleteListener(saveTask -> {
                                        binding.progressBar.setVisibility(View.GONE);
                                        binding.registerBtn.setEnabled(true);

                                        Toast.makeText(RegisterActivity.this,
                                                "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(RegisterActivity.this,
                                                MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    });
                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.registerBtn.setEnabled(true);

                            Toast.makeText(RegisterActivity.this,
                                    "Lỗi: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Chữ "Đã có tài khoản?"
        binding.backToLoginTxt.setOnClickListener(v -> finish());
    }
}