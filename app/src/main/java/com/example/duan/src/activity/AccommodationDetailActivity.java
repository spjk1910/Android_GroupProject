package com.example.duan.src.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan.R;
import com.example.duan.src.adapter.AccommodationImageAdapter;
import com.example.duan.src.models.Accommodation;
import com.example.duan.src.repositories.AccommodationRepository;

import java.util.List;

public class AccommodationDetailActivity extends AppCompatActivity {
    private TextView textLocation, textPrice, textDescription, textRating, textName, textPhone, textCountReview, textArrow;
    private RecyclerView recyclerView;
    private ImageView imageWiFi;
    private AccommodationRepository accommodationRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acccommodation_information);

        textName = findViewById(R.id.text_hotel_name);
        textPhone = findViewById(R.id.text_phone_number);
        textLocation = findViewById(R.id.text_location);
        textCountReview = findViewById(R.id.text_review_count);
        textPrice = findViewById(R.id.text_total_price);
        textDescription = findViewById(R.id.text_hotel_description);
        textRating = findViewById(R.id.text_reviews);
        recyclerView = findViewById(R.id.recycler_accommodation_images);  // RecyclerView for images
        imageWiFi = findViewById(R.id.image_wifi);
        textArrow = findViewById(R.id.text_arrow);

        // Add listener for textArrow to navigate back
        textArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and go back
                finish();
            }
        });

        // Initialize repository
        accommodationRepository = new AccommodationRepository();

        // Get accommodation ID from intent
        String accommodationId = getIntent().getStringExtra("accommodationId");
        if (accommodationId != null) {
            // Fetch accommodation details from repository
            accommodationRepository.getAccommodationById(accommodationId, new AccommodationRepository.AccommodationCallback() {
                @Override
                public void onSuccess(Accommodation accommodation) {
                    // Set the text views
                    textName.setText(accommodation.getName());
                    textPhone.setText("+84" + accommodation.getPhone());
                    textLocation.setText(accommodation.getLocation());
                    textPrice.setText("$" + accommodation.getPricePerDay() + " / night");
                    textDescription.setText(accommodation.getDescription());
                    textRating.setText(String.valueOf(accommodation.getRating()));
                    textCountReview.setText("(" + accommodation.getReviews() + " reviews)");

                    // Set up RecyclerView for images
                    List<String> imageUrls = accommodation.getImageUrls();
                    if (imageUrls != null && !imageUrls.isEmpty()) {
                        // Set up RecyclerView with the Adapter
                        AccommodationImageAdapter adapter = new AccommodationImageAdapter(imageUrls);
                        recyclerView.setLayoutManager(new LinearLayoutManager(AccommodationDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(String error) {
                    // Show error message
                    Toast.makeText(AccommodationDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
