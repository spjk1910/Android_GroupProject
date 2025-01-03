package com.example.duan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.duan.src.models.Accommodation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class AddAccommodationActivity extends AppCompatActivity {

    private EditText editTextName, editTextLocation, editTextRating, editTextReviews;
    private EditText editTextPricePerDay, editTextDaysUse, editTextContact, editTextPhone;
    private EditText editTextDescription, editTextImageUrl;
    private Button buttonAddAccommodation, buttonAddImage;
    private LinearLayout imageContainer;  // Container to hold image views
    private ArrayList<String> imageUrls = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("accommodations");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        // Initialize EditTexts, Buttons, LinearLayout (for images)
        editTextName = findViewById(R.id.editTextName);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextRating = findViewById(R.id.editTextRating);
        editTextReviews = findViewById(R.id.editTextReviews);
        editTextPricePerDay = findViewById(R.id.editTextPricePerDay);
        editTextDaysUse = findViewById(R.id.editTextDaysUse);
        editTextContact = findViewById(R.id.editTextContact);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextImageUrl = findViewById(R.id.editTextImageUrl);
        buttonAddAccommodation = findViewById(R.id.buttonAddAccommodation);
        buttonAddImage = findViewById(R.id.buttonAddImage);
        imageContainer = findViewById(R.id.imageContainer);  // LinearLayout to hold images

        // Handle add image button click
        buttonAddImage.setOnClickListener(v -> {
            String imageUrl = editTextImageUrl.getText().toString();
            if (!imageUrl.isEmpty()) {
                imageUrls.add(imageUrl);  // Add image URL to list
                displayImage(imageUrl);  // Display image in LinearLayout
                editTextImageUrl.setText("");  // Clear the input after adding
            } else {
                Toast.makeText(AddAccommodationActivity.this, "Please enter an image URL", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle add accommodation button click
        buttonAddAccommodation.setOnClickListener(v -> {
            try {
                String name = editTextName.getText().toString();
                String location = editTextLocation.getText().toString();
                double rating = Double.parseDouble(editTextRating.getText().toString());
                int reviews = Integer.parseInt(editTextReviews.getText().toString());
                double pricePerDay = Double.parseDouble(editTextPricePerDay.getText().toString());
                int daysUse = Integer.parseInt(editTextDaysUse.getText().toString());
                double totalPrice = pricePerDay * daysUse;
                String contact = editTextContact.getText().toString();
                String phone = editTextPhone.getText().toString();
                String description = editTextDescription.getText().toString();
                if (!name.isEmpty() && !location.isEmpty()) {
                    Accommodation accommodation = new Accommodation(
                            name, location, rating, reviews, pricePerDay, daysUse, totalPrice,
                            contact, phone, description, imageUrls
                    );
                    saveAccommodationToDatabase(accommodation);
                } else {
                    Toast.makeText(AddAccommodationActivity.this, "Please fill in all information", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(AddAccommodationActivity.this, "Invalid input data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Display image in LinearLayout
    private void displayImage(String imageUrl) {
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        Glide.with(this)
                .load(imageUrl)
                .into(imageView);  // Use Glide to load image from URL
        imageContainer.addView(imageView);  // Add ImageView to the LinearLayout
    }

    // Save accommodation to Firebase
    private void saveAccommodationToDatabase(Accommodation accommodation) {
        String accommodationId = myRef.push().getKey();
        if (accommodationId != null) {
            myRef.child(accommodationId).setValue(accommodation)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AddAccommodationActivity.this, "Accommodation added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddAccommodationActivity.this, "Error adding accommodation: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
