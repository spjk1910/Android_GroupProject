package com.example.duan.src.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.duan.R;
import com.example.duan.src.models.User;
import com.example.duan.src.repositories.UserRepository;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {
    private EditText editProfileName, editAddress, editPhoneNumber, editEmail;
    private UserRepository userRepository;
    private String userId;
    private String savedPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize UI components
        editProfileName = findViewById(R.id.edit_profile_name);
        editAddress = findViewById(R.id.edit_address);
        editPhoneNumber = findViewById(R.id.edit_phone_number);
        editEmail = findViewById(R.id.edit_email);

        TextView backArrow = findViewById(R.id.text_back_arrow);
        TextView saveButton = findViewById(R.id.text_save_button);

        userRepository = new UserRepository();
        userId = getIntent().getStringExtra("USER_ID");

        if (userId != null) {
            loadUserData(userId);
        } else {
            Log.e("EditProfileActivity", "User ID is null");
        }

        // Back button logic
        backArrow.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("USER_ID", userId);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // Save button logic
        saveButton.setOnClickListener(v -> saveUserData());
    }

    private void loadUserData(String userId) {
        userRepository.getUserInfo(userId, new UserRepository.GetUserInfoCallback() {
            @Override
            public void onSuccess(User user) {
                editProfileName.setText(user.getUsername());
                editEmail.setText(user.getEmail());
                editAddress.setText(user.getAddress());
                editPhoneNumber.setText(user.getPhone());
                String password = user.getPassword();
                // Bạn có thể lưu mật khẩu vào một biến tạm, ví dụ:
                savedPassword = password;
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("EditProfileActivity", "Error: " + errorMessage);
                Toast.makeText(EditProfileActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserData() {
        String name = editProfileName.getText().toString();
        String email = editEmail.getText().toString();
        String address = editAddress.getText().toString();
        String phoneNumber = editPhoneNumber.getText().toString();

        if (name.isEmpty() || email.isEmpty() || address.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng User với thông tin mới
        User updatedUser = new User(name, email, phoneNumber, address, savedPassword);

        // Sử dụng UserRepository để cập nhật thông tin
        UserRepository userRepository = new UserRepository();
        userRepository.updateUserInfo(userId, updatedUser, new UserRepository.UpdateUserCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                finish(); // Đóng Activity
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("EditProfileActivity", "Error: " + errorMessage);
                Toast.makeText(EditProfileActivity.this, "Cập nhật thất bại: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
