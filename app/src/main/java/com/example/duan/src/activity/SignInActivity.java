package com.example.duan.src.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.duan.R;
import com.example.duan.src.repositories.UserRepository;
import com.example.duan.src.utils.DialogHelper;
import com.google.firebase.auth.FirebaseAuth;

import android.text.InputType;
import android.widget.ImageView;

public class SignInActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private TextView txtSignIn, txtForgotPassword;
    private ImageView imgEye; // ImageView cho nút mắt
    private UserRepository userRepository;
    private boolean isPasswordVisible = false; // Trạng thái hiển thị mật khẩu
    private TextView textNoAccount ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Initialize views
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        txtSignIn = findViewById(R.id.txtSignIn);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        imgEye = findViewById(R.id.img_eye);
        textNoAccount= findViewById(R.id.text_no_account);
        // Initialize UserRepository
        userRepository = new UserRepository();

        // Sign-in TextView click listener
        txtSignIn.setOnClickListener(view -> signIn());

        textNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish(); // Đóng màn hình SignUpActivity
            }
        });

        // Forgot password link click listener
        txtForgotPassword.setOnClickListener(view -> {
            String email = edtEmail.getText().toString();
            if (!email.isEmpty()) {
                forgotPassword(email);
            } else {
                showSafeDialog("Thông báo", "Vui lòng nhập email của bạn");
            }
        });

        // Toggle password visibility
        imgEye.setOnClickListener(view -> togglePasswordVisibility());
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Đang hiển thị mật khẩu -> Ẩn mật khẩu
            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            edtPassword.setSelection(edtPassword.getText().length()); // Đặt con trỏ ở cuối văn bản
            imgEye.setImageResource(R.drawable.image_eye);
        } else {
            // Đang ẩn mật khẩu -> Hiển thị mật khẩu
            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            edtPassword.setSelection(edtPassword.getText().length()); // Đặt con trỏ ở cuối văn bản
            imgEye.setImageResource(R.drawable.image_eye);
        }
        isPasswordVisible = !isPasswordVisible; // Đổi trạng thái
    }

    private void signIn() {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            showSafeDialog("Thông báo", "Vui lòng nhập đầy đủ thông tin");
            return;
        }

        // Call Firebase login through UserRepository
        userRepository.loginUser(email, password, new UserRepository.LoginCallback() {
            @Override
            public void onSuccess(String message) {
                if (!isDestroyed() && !isFinishing()) {
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    // Corrected order: first put the userId into the Intent
                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                if (!isDestroyed() && !isFinishing()) {
                    showSafeDialog("Lỗi", errorMessage);
                }
            }
        });
    }

    private void forgotPassword(String email) {
        userRepository.forgotPassword(email, new UserRepository.ForgotPasswordCallback() {
            @Override
            public void onSuccess(String message) {
                if (!isDestroyed() && !isFinishing()) {
                    showSafeDialog("Thông báo", message);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                if (!isDestroyed() && !isFinishing()) {
                    showSafeDialog("Lỗi", errorMessage);
                }
            }
        });
    }

    private void showSafeDialog(String title, String message) {
        if (!isDestroyed() && !isFinishing()) {
            DialogHelper.showDialog(this, title, message);
        }
    }
}
