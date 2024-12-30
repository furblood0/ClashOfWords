package com.furkan.clashofwords.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.furkan.clashofwords.R;
import com.furkan.clashofwords.databinding.FragmentProfilePictureBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProfilePictureFragment extends Fragment {

    private FragmentProfilePictureBinding binding;
    private String selectedImageUrl; // Kullanıcının seçtiği profil fotoğrafının URL'si
    private ProfilePictureAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfilePictureBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // RecyclerView Ayarı
        binding.recyclerViewProfilePictures.setLayoutManager(new GridLayoutManager(getContext(), 3)); // 3 sütunlu GridLayout

        // Firebase Storage'dan profil resimlerini yükle
        loadProfilePicturesFromStorage();

        // "Tamam" butonuna tıklama
        binding.buttonConfirm.setOnClickListener(v -> {
            if (selectedImageUrl != null) {
                saveSelectedProfilePictureToFirestore(selectedImageUrl);
            } else {
                Toast.makeText(getContext(), "Lütfen bir profil resmi seçin.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Profil fotoğraflarını Firebase Storage'dan yükle
    private void loadProfilePicturesFromStorage() {
        ProfilePictureHelper helper = new ProfilePictureHelper();
        helper.loadProfilePictures(urls -> {
            // Sıralama işlemi
            List<String> sortedUrls = new ArrayList<>();

            for (String url : urls) {
                if (url.contains("default")) {
                    sortedUrls.add(0, url); // Varsayılan resim en başa
                } else if (url.contains("male")) {
                    sortedUrls.add(url); // Erkek resimleri sona ekle
                } else {
                    sortedUrls.add(url); // Kadın resimleri en sona ekle
                }
            }

            // Sıralanmış URL'leri RecyclerView'e gönder
            setupRecyclerView(sortedUrls);

            // Kullanıcının mevcut profil resmini yükle
            loadCurrentProfilePicture();
        });
    }


    // RecyclerView için ayarları yap
    private void setupRecyclerView(List<String> urls) {
        adapter = new ProfilePictureAdapter(urls, imageUrl -> {
            selectedImageUrl = imageUrl; // Kullanıcının seçtiği URL'yi güncelle
            updateCurrentProfilePicture(imageUrl); // Üstteki profil resmini güncelle
        });
        binding.recyclerViewProfilePictures.setAdapter(adapter);
    }

    // Mevcut profil resmini yükle
    private void loadCurrentProfilePicture() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.contains("profilePicture")) {
                        String currentProfileUrl = documentSnapshot.getString("profilePicture");
                        updateCurrentProfilePicture(currentProfileUrl); // Mevcut resmi göster
                    } else {
                        updateCurrentProfilePicture(null); // Varsayılan resmi göster
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Mevcut profil fotoğrafı yüklenemedi.", Toast.LENGTH_SHORT).show();
                });
    }

    // Üstteki mevcut profil resmini güncelle
    private void updateCurrentProfilePicture(String imageUrl) {
        Glide.with(requireContext())
                .load(imageUrl != null ? imageUrl : R.drawable.defaultppicon64)
                .placeholder(R.drawable.defaultppicon64)
                .error(R.drawable.erroricon)
                .into(binding.imageViewCurrentProfile);
    }

    // Seçilen profil resmini Firestore'a kaydet
    private void saveSelectedProfilePictureToFirestore(String imageUrl) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .update("profilePicture", imageUrl)
                .addOnSuccessListener(aVoid -> {
                    // ProfileFragment ve HomeFragment için geri bildirim gönder
                    Bundle result = new Bundle();
                    result.putString("selectedImageUrl", imageUrl);
                    getParentFragmentManager().setFragmentResult("profilePictureKey", result);
                    requireActivity().onBackPressed(); // Geri dön
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Profil fotoğrafı güncellenemedi.", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Memory Leak'i önlemek için
    }
}
