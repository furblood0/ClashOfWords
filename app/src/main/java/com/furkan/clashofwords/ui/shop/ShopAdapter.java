package com.furkan.clashofwords.ui.shop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.furkan.clashofwords.R;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

    private final List<ShopItem> items; // Satış öğelerini tutar
    private final OnShopItemClickListener listener; // Tıklama olayını dinleyen interface

    // Tıklama olayları için interface
    public interface OnShopItemClickListener {
        void onItemClick(ShopItem item);
    }

    // Constructor
    public ShopAdapter(List<ShopItem> items, OnShopItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // item_shop.xml dosyasını inflate et
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        ShopItem item = items.get(position);

        // Görselleri ve metinleri bağlama
        holder.imageViewIcon.setImageResource(item.getImageResId());
        holder.textViewTitle.setText(item.getTitle());
        holder.textViewPrice.setText(item.getGoldCost() + " Altın");

        // Tıklama olayını bağla
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // ViewHolder sınıfı
    public static class ShopViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewIcon;
        TextView textViewTitle;
        TextView textViewPrice;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);

            // item_shop.xml'deki bileşenleri bağla
            imageViewIcon = itemView.findViewById(R.id.imageViewIcon);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
        }
    }
}
