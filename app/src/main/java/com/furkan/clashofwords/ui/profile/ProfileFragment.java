package com.furkan.clashofwords.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.furkan.clashofwords.R;
import com.furkan.clashofwords.ResourceBarHelper;
import com.furkan.clashofwords.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private ResourceBarHelper resourceBarHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Firebase Auth ve Firestore başlat
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // ResourceBarHelper başlat
        View resourceBarView = view.findViewById(R.id.layout_resource_bar);
        resourceBarHelper = new ResourceBarHelper(requireContext(), resourceBarView);
        resourceBarHelper.startListeningForUserResources(); // Kullanıcı verilerini dinlemeye başla

        // Geri butonuna tıklanınca bir önceki fragment'a dön
        binding.imageViewBack.setOnClickListener(v ->
                Navigation.findNavController(v).popBackStack()
        );

        // Profil resmi butonuna tıklanınca ProfilePictureFragment'e geçiş
        binding.imageViewProfilePicture.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_profile_to_profile_picture)
        );

        // Kullanıcı verilerini ekrana yerleştir
        loadUserData();

        // ProfilePictureFragment'tan dönen sonucu dinle
        getParentFragmentManager().setFragmentResultListener("profilePictureKey", this, (requestKey, result) -> {
            String selectedImageUrl = result.getString("selectedImageUrl");
            if (selectedImageUrl != null) {
                // Yeni profil fotoğrafını güncelle
                Glide.with(requireContext())
                        .load(selectedImageUrl)
                        .placeholder(R.drawable.defaultppicon64) // Varsayılan resim
                        .into(binding.imageViewProfilePicture);
            }
        });
    }

    // Kullanıcı verilerini Firebase Firestore'dan al ve ekranda göster
    private void loadUserData() {
        String uid = auth.getCurrentUser().getUid();
        if (uid != null) {
            // Kullanıcı UID'sini göster
            binding.textViewUID.setText("Kullanıcı kimliği: " + uid);

            // Firestore'dan kullanıcı verilerini getir
            db.collection("users").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            updateUIWithUserData(documentSnapshot);
                        } else {
                            Log.w("Firestore", "Kullanıcı verisi bulunamadı.");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Kullanıcı verileri alınırken hata oluştu.", e);
                    });
        } else {
            binding.textViewUID.setText("Kullanıcı kimliği bulunamadı.");
        }
    }

    // Kullanıcı verilerini TextView bileşenlerine yerleştir
    private void updateUIWithUserData(DocumentSnapshot document) {
        // Kullanıcı adı
        String username = document.getString("username");
        if (username != null) {
            binding.textViewUsername.setText(username);
        } else {
            binding.textViewUsername.setText("Bilinmeyen Kullanıcı");
        }

        // Toplam oyun sayısı
        Long totalGames = document.getLong("totalGames");
        if (totalGames != null) {
            binding.textViewTotalGames.setText("Toplam Oyun: " + totalGames);
        } else {
            binding.textViewTotalGames.setText("Toplam Oyun: 0");
        }

        // Profil resmi
        String profilePictureUrl = document.getString("profilePicture");
        if (profilePictureUrl != null) {
            Glide.with(requireContext())
                    .load(profilePictureUrl)
                    .placeholder(R.drawable.defaultppicon64) // Varsayılan resim
                    .into(binding.imageViewProfilePicture);
        } else {
            // Varsayılan resmi yükle
            binding.imageViewProfilePicture.setImageResource(R.drawable.defaultppicon64);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        resourceBarHelper.stopEnergyRegeneration(); // Enerji artışını durdur
        binding = null; // Bellek sızıntısını önlemek için binding'i null yapıyoruz
    }
}
