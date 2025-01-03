package com.example.duan.src.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.duan.R;
import com.example.duan.src.models.User;
import com.example.duan.src.repositories.UserRepository;

public class AccountManagementActivity extends AppCompatActivity {
    private static final String TAG = "AccountManagement"; // Tag để log
    private TextView textUsernameValue, textEmailValue, textPasswordValue;
    private String userId;
    private UserRepository userRepository; // Tham chiếu đến UserRepository

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);

        userId = getIntent().getStringExtra("USER_ID");
        TextView textBack = findViewById(R.id.text_less_than);
        TextView textDeleteAccount = findViewById(R.id.text_delete_your_account);
        ImageView imageTrash = findViewById(R.id.image_trash);

        textBack.setOnClickListener(v -> {
            finish(); // Quay về màn hình trước
        });

        // Log giá trị USER_ID
        Log.d(TAG, "USER_ID nhận được: " + userId);

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy ID người dùng!", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "USER_ID không hợp lệ hoặc rỗng");
            finish(); // Kết thúc Activity nếu userId không hợp lệ
            return;
        }

        // Khởi tạo UserRepository
        userRepository = new UserRepository();

        View.OnClickListener deleteClickListener = v -> {
            // Xác nhận xóa tài khoản
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận")
                    .setMessage("Bạn có chắc chắn muốn xóa tài khoản không?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        userRepository.deleteUser(userId, new UserRepository.DeleteUserCallback() {
                            @Override
                            public void onSuccess(String message) {
                                Toast.makeText(AccountManagementActivity.this, message, Toast.LENGTH_SHORT).show();
                                // Điều hướng về màn hình đăng nhập hoặc đóng app
                                Intent intent = new Intent(AccountManagementActivity.this, SignInActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                Toast.makeText(AccountManagementActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        };

        // Gắn sự kiện bấm cho cả TextView và ImageView
        textDeleteAccount.setOnClickListener(deleteClickListener);
        imageTrash.setOnClickListener(deleteClickListener);


        // Ánh xạ các TextView
        textUsernameValue = findViewById(R.id.text_username_value);
        textEmailValue = findViewById(R.id.text_email_value);
        textPasswordValue = findViewById(R.id.text_password_value);

        // Lấy thông tin người dùng từ UserRepository
        getUserInfo(userId);
    }

    /**
     * Hàm lấy thông tin người dùng từ UserRepository và cập nhật giao diện.
     *
     * @param userId ID của người dùng cần lấy thông tin.
     */
    private void getUserInfo(String userId) {
        Log.d(TAG, "Đang truy vấn UserRepository với USER_ID: " + userId);

        userRepository.getUserInfo(userId, new UserRepository.GetUserInfoCallback() {
            @Override
            public void onSuccess(User user) {
                Log.d(TAG, "Dữ liệu người dùng: " + user.toString());
                // Cập nhật giao diện với dữ liệu người dùng
                textUsernameValue.setText(user.getUsername());
                textEmailValue.setText(user.getEmail());
                textPasswordValue.setText(user.getPassword());
            }

            @Override
            public void onFailure(String errorMessage) {
                showError(errorMessage);
            }
        });
    }

    /**
     * Hiển thị thông báo lỗi và log lỗi nếu cần.
     *
     * @param errorMessage Tin nhắn lỗi cần hiển thị.
     */
    private void showError(String errorMessage) {
        Toast.makeText(AccountManagementActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
        Log.e(TAG, errorMessage);
    }
}
