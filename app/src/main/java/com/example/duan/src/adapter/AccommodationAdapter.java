package com.example.duan.src.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.duan.R;
import com.example.duan.src.activity.AccommodationDetailActivity;
import com.example.duan.src.models.Accommodation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class AccommodationAdapter extends FirebaseRecyclerAdapter<Accommodation, AccommodationAdapter.AccommodationViewHolder> {

    private Context context;

    public AccommodationAdapter(FirebaseRecyclerOptions<Accommodation> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(AccommodationViewHolder holder, int position, Accommodation model) {
        if (model == null || position >= getItemCount()) return;

        String accommodationId = getRef(position).getKey();
        holder.textLocation.setText(model.getName() + " - " + model.getLocation());
        holder.textPrice.setText("$" + model.getPricePerDay() + " / night");

        if (model.getImageUrls() != null && !model.getImageUrls().isEmpty()) {
            Picasso.get()
                    .load(model.getImageUrls().get(0))
                    .placeholder(R.drawable.placeholder) // Optional: Add a placeholder
                    .error(R.drawable.error_image)      // Optional: Add an error image
                    .into(holder.imageView);
        }

        holder.textRating.setText(String.valueOf(model.getRating()));
        holder.textRating.setVisibility(model.getRating() > 4 ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AccommodationDetailActivity.class);
            intent.putExtra("accommodationId", accommodationId);
            context.startActivity(intent);
        });
    }

    @Override
    public AccommodationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rental, parent, false);
        return new AccommodationViewHolder(view);
    }

    public static class AccommodationViewHolder extends RecyclerView.ViewHolder {
        // Declare the views in the item layout
        TextView textLocation, textPrice, textRating;
        ImageView imageView, imageWiFi;

        public AccommodationViewHolder(View itemView) {
            super(itemView);
            textLocation = itemView.findViewById(R.id.text_address);
            textPrice = itemView.findViewById(R.id.text_price);
            textRating = itemView.findViewById(R.id.text_rating);
            imageView = itemView.findViewById(R.id.image_rectangle);
            imageWiFi = itemView.findViewById(R.id.image_wifi);
        }
    }
}
