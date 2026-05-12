package com.example.ticketbooking.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ticketbooking.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        // Nút ĐĂNG NHẬP
        binding.loginBtn.setOnClickListener(v -> {
            String email = binding.emailInput.getText().toString().trim();
            String password = binding.passwordInput.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                binding.emailInput.setError("Vui lòng nhập email");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                binding.passwordInput.setError("Vui lòng nhập mật khẩu");
                return;
            }

            // Hiện loading
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.loginBtn.setEnabled(false);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.loginBtn.setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Sai email hoặc mật khẩu!", Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Chữ "Quên mật khẩu?"
        binding.forgotPasswordTxt.setOnClickListener(v -> {
            String email = binding.emailInput.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this,
                        "Nhập email trước rồi bấm quên mật khẩu",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    "Đã gửi email đặt lại mật khẩu!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Email không tồn tại!", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Chữ "Chưa có tài khoản?"
        binding.goToRegisterTxt.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }
}