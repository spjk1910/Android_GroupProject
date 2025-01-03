package com.example.duan.src.repositories;

import com.example.duan.src.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserRepository {
    private final FirebaseAuth firebaseAuth;
    private final DatabaseReference databaseReference;

    public UserRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
    }

    // Callback interfaces
    public interface SignUpCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }

    public interface LoginCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }

    public interface ForgotPasswordCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }

    public interface GetUserInfoCallback {
        void onSuccess(User user);
        void onFailure(String errorMessage);
    }

    // Register user
    public void registerUser(String email, String password, User user, SignUpCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = firebaseAuth.getCurrentUser().getUid();
                        databaseReference.child(userId).setValue(user)
                                .addOnCompleteListener(saveTask -> {
                                    if (saveTask.isSuccessful()) {
                                        callback.onSuccess("Đăng ký người dùng thành công!");
                                    } else {
                                        callback.onFailure("Lỗi lưu dữ liệu người dùng: " + saveTask.getException().getMessage());
                                    }
                                });
                    } else {
                        callback.onFailure("Lỗi đăng ký người dùng: " + task.getException().getMessage());
                    }
                });
    }

    // Login user
    public void loginUser(String email, String password, LoginCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess("Đăng nhập thành công!");
                    } else {
                        callback.onFailure("Đăng nhập thất bại: " + task.getException().getMessage());
                    }
                });
    }

    // Forgot password
    public void forgotPassword(String email, ForgotPasswordCallback callback) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = firebaseAuth.getCurrentUser() != null ? firebaseAuth.getCurrentUser().getUid() : null;
                        if (userId != null) {
                            User updatedUser = new User();
                            updatedUser.setEmail(email);
                            updatedUser.setPassword("");
                            databaseReference.child(userId).setValue(updatedUser)
                                    .addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            callback.onSuccess("Đã gửi email reset mật khẩu và cập nhật dữ liệu người dùng!");
                                        } else {
                                            callback.onFailure("Lỗi cập nhật dữ liệu người dùng: " + updateTask.getException().getMessage());
                                        }
                                    });
                        } else {
                            callback.onSuccess("Đã gửi email reset mật khẩu!");
                        }
                    } else {
                        callback.onFailure("Lỗi gửi email reset mật khẩu: " + task.getException().getMessage());
                    }
                });
    }

    // Get user information based on userId
    public void getUserInfo(String userId, final GetUserInfoCallback callback) {
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve user data
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        callback.onSuccess(user); // Return the user object through the callback
                    } else {
                        callback.onFailure("User data is null");
                    }
                } else {
                    callback.onFailure("User not found in the database");
                }
            }

            // Logout user
            public void logoutUser() {
                try {
                    firebaseAuth.signOut();
                    System.out.println("Đăng xuất thành công!");
                } catch (Exception e) {
                    System.out.println("Đăng xuất thất bại: " + e.getMessage());
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure("Failed to fetch user data: " + databaseError.getMessage());
            }
        });
    }



    public interface UpdateUserCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }

    public void updateUserInfo(String userId, User updatedUser, UpdateUserCallback callback) {
        databaseReference.child(userId).setValue(updatedUser)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess("Cập nhật thông tin người dùng thành công!");
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        callback.onFailure("Lỗi khi cập nhật thông tin người dùng: " + errorMessage);
                    }
                });
    }


    public interface DeleteUserCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }

    public void deleteUser(String userId, DeleteUserCallback callback) {
        databaseReference.child(userId).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Xóa tài khoản người dùng trong Firebase Authentication
                        firebaseAuth.getCurrentUser().delete()
                                .addOnCompleteListener(deleteTask -> {
                                    if (deleteTask.isSuccessful()) {
                                        callback.onSuccess("Tài khoản đã được xóa thành công!");
                                    } else {
                                        callback.onFailure("Lỗi khi xóa tài khoản: " + deleteTask.getException().getMessage());
                                    }
                                });
                    } else {
                        callback.onFailure("Lỗi khi xóa dữ liệu trong cơ sở dữ liệu: " + task.getException().getMessage());
                    }
                });
    }



}
