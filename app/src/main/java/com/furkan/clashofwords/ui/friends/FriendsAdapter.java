package com.furkan.clashofwords.ui.friends;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.furkan.clashofwords.R;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    private List<Friend> friends;

    public FriendsAdapter(List<Friend> friends) {
        this.friends = friends;
    }

    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new FriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {
        Friend friend = friends.get(position);

        holder.imageViewProfile.setImageResource(friend.getProfileImageResId());
        holder.textViewUsername.setText(friend.getUsername());

        // Durum kontrolü
        if (friend.isOnline()) {
            holder.textViewStatus.setText("Çevrimiçi");
            holder.textViewStatus.setTextColor(Color.GREEN);
            holder.imageViewStatus.setImageResource(R.drawable.green_circle);
        } else {
            holder.textViewStatus.setText("Çevrimdışı");
            holder.textViewStatus.setTextColor(Color.GRAY);
            holder.imageViewStatus.setImageResource(R.drawable.gray_circle);
        }
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProfile;
        TextView textViewUsername;
        TextView textViewStatus;
        ImageView imageViewStatus;

        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProfile = itemView.findViewById(R.id.imageViewProfile);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            imageViewStatus = itemView.findViewById(R.id.imageViewStatus);
        }
    }
}

