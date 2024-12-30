package com.furkan.clashofwords.ui.settings;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.furkan.clashofwords.MainActivity;
import com.furkan.clashofwords.databinding.FragmentSettingsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentSettingsBinding binding = FragmentSettingsBinding.inflate(inflater, container, false);

        // Firebase Auth ve Firestore referanslarını başlat
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Çıkış Yap Butonu Tıklama
        binding.buttonLogout.setOnClickListener(v -> {
            showLogoutConfirmationDialog();
        });

        // Hesabı Sil Butonu Tıklama (Henüz İşlenmedi)
        binding.buttonDeleteAccount.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Hesap Silme İşlemi Henüz Hazır Değil", Toast.LENGTH_SHORT).show();
        });

        return binding.getRoot();
    }

    // Çıkış yapmadan önce uyarı göster
    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Hesap Silinecek")
                .setMessage("Çıkış yaptığınızda hesabınız ve tüm verileriniz silinecek. Devam etmek istediğinizden emin misiniz?")
                .setPositiveButton("Evet", (dialog, which) -> logoutAndDeleteAccount())
                .setNegativeButton("Hayır", null) // Kullanıcı iptal ederse hiçbir şey yapma
                .show();
    }

    // Çıkış yap ve kullanıcı verilerini sil
    private void logoutAndDeleteAccount() {
        if (auth.getCurrentUser() != null) {
            String uid = auth.getCurrentUser().getUid();

            // 1. Firestore'dan kullanıcı verilerini sil
            db.collection("users").document(uid).delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firestore", "Kullanıcı verileri silindi: UID = " + uid);
                        Toast.makeText(requireContext(), "Kullanıcı verileri silindi.", Toast.LENGTH_SHORT).show();

                        // 2. Firebase Authentication'dan kullanıcıyı sil
                        deleteFirebaseUser();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Kullanıcı verileri silinirken hata oluştu: UID = " + uid, e);
                        Toast.makeText(requireContext(), "Kullanıcı verileri silinirken hata oluştu.", Toast.LENGTH_SHORT).show();

                        // Yine de oturumu kapat
                        signOutAndNavigateToMain();
                    });
        } else {
            // Kullanıcı yoksa doğrudan oturum kapat
            signOutAndNavigateToMain();
        }
    }

    // Firebase Authentication'dan kullanıcıyı sil
    private void deleteFirebaseUser() {
        auth.getCurrentUser().delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Auth", "Kullanıcı Firebase Authentication'dan silindi.");
                        Toast.makeText(requireContext(), "Hesabınız silindi.", Toast.LENGTH_SHORT).show();

                        // MainActivity'ye yönlendir
                        navigateToMainActivity();
                    } else {
                        Log.e("Auth", "Kullanıcı Firebase Authentication'dan silinirken hata oluştu.", task.getException());
                        Toast.makeText(requireContext(), "Hesap silinirken hata oluştu.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Firebase Authentication'dan çıkış yap ve MainActivity'ye yönlendir
    private void signOutAndNavigateToMain() {
        auth.signOut();
        navigateToMainActivity();
    }

    // MainActivity'ye yönlendir
    private void navigateToMainActivity() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);

        // Fragment yığını temizlemek için activity'yi sonlandır
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}
