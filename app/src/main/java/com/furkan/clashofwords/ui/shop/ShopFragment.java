package com.furkan.clashofwords.ui.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.furkan.clashofwords.R;
import com.furkan.clashofwords.ResourceBarHelper;
import com.furkan.clashofwords.databinding.FragmentShopBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {

    private FragmentShopBinding binding;
    private FirebaseFirestore db;
    private String uid;
    private ResourceBarHelper resourceBarHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShopBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Resource Bar'ı başlat ve dinlemeyi başlat
        initializeResourceBar(view);

        // Shop item listesi
        List<ShopItem> items = new ArrayList<>();
        items.add(new ShopItem(R.drawable.energyicongreen, "1 Enerji", "5 Altın", 1, 5));
        items.add(new ShopItem(R.drawable.energyicongreen, "3 Enerji", "15 Altın", 3, 15));
        items.add(new ShopItem(R.drawable.energyicongreen, "5 Enerji", "25 Altın", 5, 25));
        items.add(new ShopItem(R.drawable.energyicongreen, "10 Enerji", "50 Altın", 10, 50));

        // RecyclerView ve Adapter
        ShopAdapter adapter = new ShopAdapter(items, this::processPurchase);
        binding.recyclerViewShop.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.recyclerViewShop.setAdapter(adapter);
    }

    private void initializeResourceBar(View view) {
        View resourceBarView = view.findViewById(R.id.layout_resource_bar);
        resourceBarHelper = new ResourceBarHelper(requireContext(), resourceBarView);
        resourceBarHelper.startListeningForUserResources();
    }

    private void processPurchase(ShopItem item) {
        db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Long currentGold = documentSnapshot.getLong("gold");
                Long currentEnergy = documentSnapshot.getLong("energy");

                if (currentGold != null && currentGold >= item.getGoldCost()) {
                    final long maxEnergy = 10; // Maksimum enerji değeri
                    long updatedEnergy = currentEnergy != null ? currentEnergy + item.getEnergyAmount() : item.getEnergyAmount();

                    if (currentEnergy != null && currentEnergy >= maxEnergy) {
                        // Eğer enerji maksimuma ulaşmışsa uyarı göster
                        showToast("Enerjiniz zaten dolu!");
                        return;
                    }

                    if (currentEnergy != null && updatedEnergy > maxEnergy) {
                        // Eğer toplam enerji maksimumu aşacaksa
                        showToast("Enerji sınırını aşamazsınız! Daha az enerji satın alabilirsiniz.");
                        return;
                    }

                    // Altın düşür ve enerji ekle
                    long updatedGold = currentGold - item.getGoldCost();

                    db.collection("users").document(uid).update("gold", updatedGold, "energy", updatedEnergy)
                            .addOnSuccessListener(aVoid -> {
                                showToast(item.getTitle() + " satın alındı!");
                                resourceBarHelper.startListeningForUserResources(); // Resource Bar'ı güncelle

                                // Enerji tam dolduysa geri sayımı durdur ve gizle
                                if (updatedEnergy == maxEnergy) {
                                    resourceBarHelper.stopEnergyRegeneration();
                                    resourceBarHelper.hideCountdown();
                                }
                            })
                            .addOnFailureListener(e -> showToast("Satın alma işlemi başarısız!"));
                } else {
                    showToast("Yetersiz altın!");
                }
            }
        }).addOnFailureListener(e -> showToast("Kullanıcı verileri alınamadı!"));
    }


    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        resourceBarHelper.stopEnergyRegeneration(); // Enerji artışını durdur
        binding = null;
    }
}
