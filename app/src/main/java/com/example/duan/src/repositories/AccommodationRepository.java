package com.example.duan.src.repositories;

import com.example.duan.src.models.Accommodation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import androidx.annotation.NonNull;

public class AccommodationRepository {

    private DatabaseReference mDatabase;

    public AccommodationRepository() {
        mDatabase = FirebaseDatabase.getInstance().getReference("accommodations");
    }

    public void getAccommodationById(String accommodationId, final AccommodationCallback callback) {
        mDatabase.child(accommodationId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Accommodation accommodation = dataSnapshot.getValue(Accommodation.class);
                    if (accommodation != null) {
                        callback.onSuccess(accommodation);
                    } else {
                        callback.onFailure("Accommodation data is null");
                    }
                } else {
                    callback.onFailure("Accommodation not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFailure(databaseError.getMessage());
            }
        });
    }

    public interface AccommodationCallback {
        void onSuccess(Accommodation accommodation);
        void onFailure(String error);
    }
}
