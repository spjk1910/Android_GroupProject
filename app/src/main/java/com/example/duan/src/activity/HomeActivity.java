package com.example.duan.src.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.duan.R;
import com.example.duan.src.adapter.AccommodationAdapter;
import com.example.duan.src.models.Accommodation;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerMostPopular;
    private RecyclerView recyclerHotDeals;
    private AccommodationAdapter mostPopularAdapter;
    private AccommodationAdapter hotDealsAdapter;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Lấy userId từ Intent và lưu vào SharedPreferences
        userId = getIntent().getStringExtra("userId");
        Log.d("HomeActivity", "UserId: " + userId);
        saveUserIdToLocalStorage(userId);

        recyclerMostPopular = findViewById(R.id.recycler_most_popular);
        recyclerHotDeals = findViewById(R.id.recycler_hot_deals);

        recyclerMostPopular.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerHotDeals.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        FirebaseRecyclerOptions<Accommodation> optionsMostPopular = new FirebaseRecyclerOptions.Builder<Accommodation>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("accommodations").orderByChild("rating"), Accommodation.class)
                .build();
        mostPopularAdapter = new AccommodationAdapter(optionsMostPopular, this);
        recyclerMostPopular.setAdapter(mostPopularAdapter);

        FirebaseRecyclerOptions<Accommodation> optionsHotDeals = new FirebaseRecyclerOptions.Builder<Accommodation>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("accommodations").orderByChild("pricePerDay"), Accommodation.class)
                .build();
        hotDealsAdapter = new AccommodationAdapter(optionsHotDeals, this);
        recyclerHotDeals.setAdapter(hotDealsAdapter);

        setupBottomNavbar(userId);
    }

    // Lưu userId vào SharedPreferences
    private void saveUserIdToLocalStorage(String userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", userId);
        editor.apply();
    }

    // Lấy userId từ SharedPreferences
    public String getUserIdFromLocalStorage() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userId", null); // trả về null nếu không tìm thấy
    }

    // Thiết lập BottomNavigationView
    private void setupBottomNavbar(final String userId) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        // Setting the item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();  // Get the itemId of the selected item

                if (id == R.id.nav_home) {
                    // Navigate to Home
                    return true;
                } else if (id == R.id.nav_search) {
                    // Navigate to Search
                    return true;
                } else if (id == R.id.nav_profile) {
                    // Navigate to Profile and pass userId
                    Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
                    profileIntent.putExtra("USER_ID", userId);  // Passing userId via Intent
                    startActivity(profileIntent);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mostPopularAdapter.startListening();
        hotDealsAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mostPopularAdapter.stopListening();
        hotDealsAdapter.stopListening();
    }

    // Xóa userId khỏi SharedPreferences nếu cần
    public void clearUserIdFromLocalStorage() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("userId");
        editor.apply();
    }
}
