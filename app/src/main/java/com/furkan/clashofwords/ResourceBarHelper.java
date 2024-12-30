package com.furkan.clashofwords;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ResourceBarHelper {

    private final Context context;
    private final View resourceBarView;
    private final ProgressBar energyProgressBar;
    private final TextView energyTextView;
    private final TextView goldTextView;
    private final TextView nextEnergyTextView;
    private final Handler handler = new Handler();
    private static final long ENERGY_UPDATE_INTERVAL = 300000; // 5 dakika (ms)

    public ResourceBarHelper(@NonNull Context context, @NonNull View resourceBarView) {
        this.context = context;
        this.resourceBarView = resourceBarView;

        // Layout'taki ProgressBar ve TextView bileşenlerini bağla
        energyProgressBar = resourceBarView.findViewById(R.id.energy_progress_bar);
        energyTextView = resourceBarView.findViewById(R.id.energy_text);
        goldTextView = resourceBarView.findViewById(R.id.gold_text);
        nextEnergyTextView = resourceBarView.findViewById(R.id.next_energy_text);
    }

    public void startListeningForUserResources() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection("users").document(uid)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        e.printStackTrace();
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        updateResourceBar(snapshot);
                        handleEnergyRegeneration(snapshot); // Enerji yenilenmesini başlat
                    }
                });
    }

    private void updateResourceBar(@NonNull DocumentSnapshot documentSnapshot) {
        if (documentSnapshot.exists()) {
            // Enerji değeri
            Long energy = documentSnapshot.getLong("energy");
            if (energy != null) {
                int maxEnergy = 10; // Maksimum enerji değeri
                energyProgressBar.setMax(maxEnergy);
                energyProgressBar.setProgress(energy.intValue());
                energyTextView.setText(energy + "/" + maxEnergy);

                // Enerji tam doluysa süre metnini gizle ve geri sayımı durdur
                if (energy >= maxEnergy) {
                    nextEnergyTextView.setVisibility(View.GONE);
                    handler.removeCallbacksAndMessages(null); // Geri sayımı durdur
                } else {
                    nextEnergyTextView.setVisibility(View.VISIBLE);
                }
            }

            // Altın değeri
            Long gold = documentSnapshot.getLong("gold");
            if (gold != null) {
                goldTextView.setText(String.valueOf(gold));
            }
        }
    }


    private void handleEnergyRegeneration(@NonNull DocumentSnapshot documentSnapshot) {
        Long energy = documentSnapshot.getLong("energy");
        Long lastEnergyUpdate = documentSnapshot.getLong("lastEnergyUpdate");
        int maxEnergy = 10;

        if (energy != null && lastEnergyUpdate != null && energy < maxEnergy) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - lastEnergyUpdate;
            long energyToRegenerate = elapsedTime / ENERGY_UPDATE_INTERVAL;

            if (energyToRegenerate > 0) {
                long newEnergy = Math.min(energy + energyToRegenerate, maxEnergy);
                long nextEnergyUpdateTime = currentTime - (elapsedTime % ENERGY_UPDATE_INTERVAL);

                updateEnergyInFirestore(newEnergy, nextEnergyUpdateTime);
            } else {
                startCountdown(ENERGY_UPDATE_INTERVAL - (elapsedTime % ENERGY_UPDATE_INTERVAL));
            }
        }
    }

    private void updateEnergyInFirestore(long newEnergy, long nextEnergyUpdateTime) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection("users").document(uid)
                .update("energy", newEnergy, "lastEnergyUpdate", nextEnergyUpdateTime)
                .addOnSuccessListener(aVoid -> {
                    startCountdown(ENERGY_UPDATE_INTERVAL);
                })
                .addOnFailureListener(e -> e.printStackTrace());
    }

    private void startCountdown(long timeUntilNextEnergy) {
        handler.post(new Runnable() {
            long remainingTime = timeUntilNextEnergy;

            @Override
            public void run() {
                if (remainingTime > 0) {
                    long minutes = remainingTime / 60000;
                    long seconds = (remainingTime % 60000) / 1000;
                    nextEnergyTextView.setText(String.format("%02d:%02d", minutes, seconds));
                    nextEnergyTextView.setVisibility(View.VISIBLE);

                    remainingTime -= 1000;
                    handler.postDelayed(this, 1000);
                } else {
                    nextEnergyTextView.setVisibility(View.GONE);
                }
            }
        });
    }

    public void consumeEnergyAsync(OnEnergyConsumeListener listener) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection("users").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Long currentEnergy = documentSnapshot.getLong("energy");
                        if (currentEnergy != null && currentEnergy > 0) {
                            FirebaseFirestore.getInstance().collection("users").document(uid)
                                    .update(
                                            "energy", currentEnergy - 1,
                                            "lastEnergyUpdate", System.currentTimeMillis()
                                    )
                                    .addOnSuccessListener(aVoid -> listener.onSuccess())
                                    .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
                        } else {
                            listener.onFailure("Yeterli enerji yok!");
                        }
                    }
                })
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }

    public interface OnEnergyConsumeListener {
        void onSuccess();
        void onFailure(String error);
    }


    public void hideCountdown() {
        nextEnergyTextView.setVisibility(View.GONE);
    }

    public void stopEnergyRegeneration() {
        handler.removeCallbacksAndMessages(null);
    }
}
