package com.example.duan.src.utils;

import android.app.Activity;
import android.content.Context;
import androidx.appcompat.app.AlertDialog;

public class DialogHelper {
    public static void showDialog(Context context, String title, String message) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return; // Không hiển thị Dialog nếu Activity đã bị hủy
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
