package com.example.duan.src.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.duan.R;
import com.example.duan.src.models.User;
import com.example.duan.src.repositories.UserRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {
    private String userId;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userRepository = new UserRepository();
        userId = getIntent().getStringExtra("USER_ID");

        if (userId != null) {
            displayUserInfo(userId);
        } else {
            Log.e("ProfileActivity", "UserId is null");
        }

        // Cấu hình BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                return true;
            } else if (id == R.id.nav_search) {
                return true;
            } else if (id == R.id.nav_home) {
                Intent profileIntent = new Intent(ProfileActivity.this, HomeActivity.class);
                profileIntent.putExtra("USER_ID", userId);
                startActivity(profileIntent);
                return true;
            }
            return false;
        });

        // Cấu hình logout và chỉnh sửa thông tin profile
        ImageView logoutImageView = findViewById(R.id.image_log_out);
        logoutImageView.setOnClickListener(v -> logoutUser());

        ImageView editProfileImageView = findViewById(R.id.image_edit);
        editProfileImageView.setOnClickListener(v -> navigateToEditProfile());

        ImageView cloudChangeImageView = findViewById(R.id.image_cloud_change);
        cloudChangeImageView.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, AccountManagementActivity.class);
            intent.putExtra("USER_ID", userId);  // Chuyển userId nếu cần
            startActivity(intent);
        });

    }

    private void displayUserInfo(String userId) {
        userRepository.getUserInfo(userId, new UserRepository.GetUserInfoCallback() {
            @Override
            public void onSuccess(User user) {
                TextView nameTextView = findViewById(R.id.nameTextView);
                TextView emailTextView =findViewById(R.id.text_username);
                nameTextView.setText(user.getUsername());
                String email = user.getEmail();
                String username = email.split("@")[0];  // Lấy phần trước dấu @
                emailTextView.setText("@" + username);
                Log.d("ProfileActivity", "User Name: " + user.getUsername());
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("ProfileActivity", "Error: " + errorMessage);
            }
        });
    }

    private void logoutUser() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        Intent loginIntent = new Intent(ProfileActivity.this, SignInActivity.class);
        startActivity(loginIntent);
        finish();  // Finish current activity to remove it from the back stack
    }

    private final ActivityResultLauncher<Intent> editProfileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    // Lấy dữ liệu trả về
                    String updatedUserId = result.getData().getStringExtra("USER_ID");
                    if (updatedUserId != null) {
                        // Tải lại thông tin người dùng
                        displayUserInfo(updatedUserId);
                    }
                }
            }
    );

    private void navigateToEditProfile() {
        Intent editProfileIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        editProfileIntent.putExtra("USER_ID", userId);  // Pass userId to EditProfileActivity
        editProfileLauncher.launch(editProfileIntent);  // Start EditProfileActivity
    }
}
