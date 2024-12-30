package com.furkan.clashofwords.ui.gameplay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.furkan.clashofwords.R;
import com.furkan.clashofwords.ResourceBarHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ResultFragment extends Fragment {

    private TextView winMessageTextView;
    private TextView rewardTextView;
    private TextView scoreTextView;
    private ImageView playerProfileImage;
    private ImageView opponentProfileImage;
    private TextView playerNameTextView;
    private TextView opponentNameTextView;
    private Button backToHomeButton;
    private ConstraintLayout rootLayout;
    private ResourceBarHelper resourceBarHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ResourceBarHelper başlat
        View resourceBarView = view.findViewById(R.id.layout_resource_bar);
        resourceBarHelper = new ResourceBarHelper(requireContext(), resourceBarView);
        resourceBarHelper.startListeningForUserResources(); // Kullanıcı verilerini dinlemeye başla

        // View referanslarını bağlama
        rootLayout = view.findViewById(R.id.resultFragmentRoot);
        winMessageTextView = view.findViewById(R.id.tvWinMessage);
        rewardTextView = view.findViewById(R.id.tvReward);
        scoreTextView = view.findViewById(R.id.tvScore);
        playerProfileImage = view.findViewById(R.id.playerProfileImage);
        opponentProfileImage = view.findViewById(R.id.opponentProfileImage);
        playerNameTextView = view.findViewById(R.id.playerName);
        opponentNameTextView = view.findViewById(R.id.opponentName);
        backToHomeButton = view.findViewById(R.id.btnBackToHome);

        // Argumentlerden gelen verileri al
        if (getArguments() != null) {
            int playerScore = getArguments().getInt("playerScore", 0);
            int botScore = getArguments().getInt("botScore", 0);
            String winner = getArguments().getString("winner", "draw");
            String playerName = getArguments().getString("playerName", "Player");
            String opponentName = getArguments().getString("opponentName", "Bot");
            String playerProfileUrl = getArguments().getString("playerProfileUrl", null);
            String opponentProfileUrl = getArguments().getString("opponentProfileUrl", null);

            // Arka plan rengini belirle
            if ("player".equals(winner)) {
                rootLayout.setBackgroundColor(getResources().getColor(R.color.result_win_background)); // Sarımsı ton
                winMessageTextView.setText("KAZANDIN!");
                rewardTextView.setText("+10 Altın");
                resourceBarHelper.consumeEnergyAsync(new ResourceBarHelper.OnEnergyConsumeListener() {

                    @Override
                    public void onSuccess() {
                        // Oyuncunun UID'sini al
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        // Firestore'daki mevcut altın değerini güncelle
                        FirebaseFirestore.getInstance().collection("users").document(uid)
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {
                                        Long currentGold = documentSnapshot.getLong("gold");
                                        if (currentGold != null) {
                                            // 10 altın ekle
                                            long newGold = currentGold + 10;
                                            FirebaseFirestore.getInstance().collection("users").document(uid)
                                                    .update("gold", newGold)
                                                    .addOnSuccessListener(aVoid -> {
                                                        // Başarılı güncelleme mesajı (opsiyonel)
                                                        Toast.makeText(getContext(), "10 altın kazandınız!", Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        // Altın güncelleme hatası (opsiyonel)
                                                        Toast.makeText(getContext(), "Altın güncellenirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    // Firestore'dan veri çekme hatası
                                    Toast.makeText(getContext(), "Kullanıcı bilgileri alınırken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                                });
                    }
                    @Override
                    public void onFailure(String error) {
                        // Enerji tüketimi başarısız olduğunda hata mesajını göster
                        Toast.makeText(getContext(), "Enerji tüketimi başarısız: " + error, Toast.LENGTH_SHORT).show();
                    }
                });

            } else if ("bot".equals(winner)) {
                rootLayout.setBackgroundColor(getResources().getColor(R.color.result_lose_background)); // Kırmızı ton
                winMessageTextView.setText("KAYBETTİN!");
                rewardTextView.setText("");
            } else {
                rootLayout.setBackgroundColor(getResources().getColor(R.color.result_draw_background)); // Nötr bir ton
                winMessageTextView.setText("BERABERE!");
                rewardTextView.setText("");
            }

            // Skorları ayarla
            scoreTextView.setText(playerScore + " - " + botScore);

            // Profil resimlerini yükle
            if (playerProfileUrl != null) {
                Glide.with(this)
                        .load(playerProfileUrl)
                        .placeholder(R.drawable.defaultppicon64)
                        .into(playerProfileImage);
            } else {
                playerProfileImage.setImageResource(R.drawable.defaultppicon64);
            }

            if (opponentProfileUrl != null) {
                Glide.with(this)
                        .load(opponentProfileUrl)
                        .placeholder(R.drawable.defaultppicon64)
                        .into(opponentProfileImage);
            } else {
                opponentProfileImage.setImageResource(R.drawable.defaultppicon64);
            }

            // Kullanıcı adlarını ayarla
            playerNameTextView.setText(playerName);
            opponentNameTextView.setText(opponentName);
        }

        // Ana sayfaya dönme işlemi
        backToHomeButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_result_to_home));
    }
}
