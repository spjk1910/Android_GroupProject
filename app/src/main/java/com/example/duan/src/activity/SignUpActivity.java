package com.example.duan.src.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.duan.R;
import com.example.duan.src.models.User;
import com.example.duan.src.repositories.UserRepository;
import com.example.duan.src.utils.DialogHelper;
import com.google.firebase.FirebaseApp;

public class SignUpActivity extends AppCompatActivity {

    private EditText edtEmail, edtUsername, edtPassword, edtConfirmPassword, edtAddress, edtPhone;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        FirebaseApp.initializeApp(this);

        userRepository = new UserRepository();

        edtEmail = findViewById(R.id.text_email);
        edtUsername = findViewById(R.id.text_username);
        edtPassword = findViewById(R.id.text_password);
        edtConfirmPassword = findViewById(R.id.text_confirm_password);
        edtAddress = findViewById(R.id.text_address);
        edtPhone = findViewById(R.id.text_phone_number);
        TextView textNoAccount = findViewById(R.id.text_no_account);
        TextView btnSignUp = findViewById(R.id.text_sign_up_button);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        textNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish(); // Đóng màn hình SignUpActivity
            }
        });

    }

    private void registerUser() {
        String email = edtEmail.getText().toString().trim();
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            DialogHelper.showDialog(this, "Lỗi", "Vui lòng điền đầy đủ thông tin!"); // Use DialogHelper
            return;
        }

        if (!password.equals(confirmPassword)) {
            DialogHelper.showDialog(this, "Lỗi", "Mật khẩu không khớp!"); // Use DialogHelper
            return;
        }
        User user = new User(username, email, address, phone,password);
        userRepository.registerUser(email, password, user, new UserRepository.SignUpCallback() {
            @Override
            public void onSuccess(String message) {
                DialogHelper.showDialog(SignUpActivity.this, "Thành công", "Đăng ký thành công!"); // Use DialogHelper
            }

            @Override
            public void onFailure(String errorMessage) {
                DialogHelper.showDialog(SignUpActivity.this, "Lỗi", errorMessage); // Use DialogHelper
            }
        });
    }
}
