package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ImagePickerAdapter extends RecyclerView.Adapter<ImagePickerAdapter.ViewHolder> {

    private final List<String> imageNames;
    private final OnImageClickListener onImageClickListener;

    public ImagePickerAdapter(List<String> imageNames, OnImageClickListener listener) {
        this.imageNames = imageNames;
        this.onImageClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageName = imageNames.get(position);
        holder.textView.setText(imageName);
        holder.itemView.setOnClickListener(v -> onImageClickListener.onImageClick(imageName));
    }

    @Override
    public int getItemCount() {
        return imageNames.size();
    }

    public interface OnImageClickListener {
        void onImageClick(String imageName);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}