package com.furkan.clashofwords;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.furkan.clashofwords.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Kullanıcı oturumunu kontrol et
        checkIfUserAlreadyLoggedIn();

        // Giriş butonlarını ayarla
        setupLoginButtons();
    }

    private void checkIfUserAlreadyLoggedIn() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d("Auth", "Kullanıcı zaten giriş yapmış: UID = " + currentUser.getUid());
            navigateToHomeActivity();
        } else {
            Log.d("Auth", "Kullanıcı oturum açmamış.");
        }
    }

    private void setupLoginButtons() {
        binding.guestLoginButton.setOnClickListener(v -> guestLogin());
        binding.googleLoginButton.setOnClickListener(v -> {
            Toast.makeText(this, "Google Girişi Henüz Hazır Değil", Toast.LENGTH_SHORT).show();
        });
    }

    private void guestLogin() {
        mAuth.signInAnonymously().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    String uid = user.getUid();
                    Log.d("Auth", "Misafir Giriş Başarılı: UID = " + uid);

                    // HomeActivity'ye yönlendir
                    navigateToHomeActivity();

                    // Firestore'a kullanıcıyı kaydet
                    saveUserToFirestore(uid);
                }
            } else {
                Log.w("Auth", "Misafir Girişi Başarısız", task.getException());
                Toast.makeText(this, "Misafir girişi başarısız. Lütfen tekrar deneyin.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserToFirestore(String uid) {
        // Varsayılan kullanıcı verileri
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", "U" + (100 + (int) (Math.random() * 900)));
        userData.put("gold", 100);
        userData.put("energy", 10);
        userData.put("totalGames", 0);
        userData.put("lastEnergyUpdate", System.currentTimeMillis()); // Şimdiki zamanı ekliyoruz

        db.collection("users").document(uid).set(userData)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Kullanıcı Firestore'a kaydedildi: UID = " + uid))
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Kullanıcı Firestore'a kaydedilemedi.", e);
                    Toast.makeText(this, "Kullanıcı verileri kaydedilemedi.", Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}
