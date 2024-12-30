package com.furkan.clashofwords.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.furkan.clashofwords.R;
import com.furkan.clashofwords.ResourceBarHelper;
import com.furkan.clashofwords.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private ResourceBarHelper resourceBarHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // View Binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Firebase Başlat
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // ResourceBarHelper başlat
        View resourceBarView = view.findViewById(R.id.layout_resource_bar);
        resourceBarHelper = new ResourceBarHelper(requireContext(), resourceBarView);
        resourceBarHelper.startListeningForUserResources(); // Kullanıcı verilerini dinlemeye başla

        // Profil Fotoğrafını Yükle
        loadProfilePicture();

        // Profil Fotoğrafına Tıklayınca ProfileFragment'e Geçiş
        binding.imageViewProfile.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_home_to_profile)
        );

        // ProfileFragment'tan dönen sonucu dinle
        getParentFragmentManager().setFragmentResultListener("profilePictureKey", this, (requestKey, result) -> {
            String selectedImageUrl = result.getString("selectedImageUrl");
            if (selectedImageUrl != null) {
                // Glide ile yeni fotoğrafı yükle
                Glide.with(requireContext())
                        .load(selectedImageUrl)
                        .placeholder(R.drawable.defaultppicon64)
                        .into(binding.imageViewProfile);
            }
        });


        binding.buttonPlayGame.setOnClickListener(v -> {
            ResourceBarHelper resourceBarHelper = new ResourceBarHelper(requireContext(), resourceBarView);

            resourceBarHelper.consumeEnergyAsync(new ResourceBarHelper.OnEnergyConsumeListener() {
                @Override
                public void onSuccess() {
                    Navigation.findNavController(v)
                            .navigate(R.id.action_navigation_home_to_questionStartFragment);
                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
                }
            });
        });



    }


    // Profil Fotoğrafını Firebase Firestore'dan Yükle
    private void loadProfilePicture() {
        String uid = auth.getCurrentUser().getUid();
        if (uid != null) {
            db.collection("users").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String profilePictureUrl = documentSnapshot.getString("profilePicture");
                            if (profilePictureUrl != null) {
                                Glide.with(requireContext())
                                        .load(profilePictureUrl)
                                        .placeholder(R.drawable.defaultppicon64) // Yüklenirken varsayılan resim
                                        .into(binding.imageViewProfile);
                            } else {
                                // Eğer profil resmi yoksa varsayılan resmi yükle
                                binding.imageViewProfile.setImageResource(R.drawable.defaultppicon64);
                            }
                        }
                    })
                    .addOnFailureListener(e -> Log.e("Firestore", "Profil resmi alınamadı: ", e));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        resourceBarHelper.stopEnergyRegeneration(); // Enerji artışını durdur
        binding = null; // Bellek sızıntısını önlemek için binding'i null yap
    }
}
