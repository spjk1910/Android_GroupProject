package com.example.duan.src.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AccommodationImageAdapter extends RecyclerView.Adapter<AccommodationImageAdapter.ImageViewHolder> {

    private List<String> imageUrls;

    public AccommodationImageAdapter(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        holder.textImagePosition.setText((position + 1) + "/" + getItemCount());

        Picasso.get().load(imageUrl).into(holder.imageView);  // Load image with Picasso
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textImagePosition;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_item_recycler);
            textImagePosition = itemView.findViewById(R.id.text_image_position); // ID của TextView

        }
    }
}
