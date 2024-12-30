package com.furkan.clashofwords.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.furkan.clashofwords.R;

import java.util.List;

public class ProfilePictureAdapter extends RecyclerView.Adapter<ProfilePictureAdapter.ProfilePictureViewHolder> {

    private final List<String> profilePictures; // Firebase Storage'dan gelen resim URL'leri
    private final OnPictureClickListener listener; // Tıklama olayını dinleyen interface

    // Constructor
    public ProfilePictureAdapter(List<String> profilePictures, OnPictureClickListener listener) {
        this.profilePictures = profilePictures;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProfilePictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // item_profile_picture.xml dosyasını inflate ediyoruz
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_picture, parent, false);
        return new ProfilePictureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilePictureViewHolder holder, int position) {
        String imageUrl = profilePictures.get(position);

        // Glide ile resmi yükle
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.defaultppicon64) // Yüklenirken gösterilecek varsayılan resim
                .error(R.drawable.erroricon) // Yükleme hatası durumunda gösterilecek resim
                .into(holder.imageViewProfileItem);

        // Fotoğraf tıklama olayını dinle
        holder.itemView.setOnClickListener(v -> listener.onPictureClick(imageUrl));
    }

    @Override
    public int getItemCount() {
        return profilePictures.size();
    }

    // ViewHolder Sınıfı
    public static class ProfilePictureViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProfileItem;

        public ProfilePictureViewHolder(@NonNull View itemView) {
            super(itemView);
            // item_profile_picture.xml içindeki ImageView'i bağla
            imageViewProfileItem = itemView.findViewById(R.id.imageViewProfileItem);
        }
    }

    // Tıklama olayını dinlemek için interface
    public interface OnPictureClickListener {
        void onPictureClick(String imageUrl); // URL olarak geri döner
    }
}
