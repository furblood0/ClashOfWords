package com.furkan.clashofwords.ui.profile;

import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ProfilePictureHelper {

    private final FirebaseStorage storage;

    public ProfilePictureHelper() {
        this.storage = FirebaseStorage.getInstance();
    }

    public interface OnUrlsLoadedListener {
        void onUrlsLoaded(List<String> urls);
    }

    public void loadProfilePictures(OnUrlsLoadedListener listener) {
        StorageReference profilePicturesRef = storage.getReference().child("profilePictures");

        profilePicturesRef.listAll().addOnSuccessListener(listResult -> {
            List<String> defaultPictures = new ArrayList<>();
            List<String> malePictures = new ArrayList<>();
            List<String> femalePictures = new ArrayList<>();

            for (StorageReference item : listResult.getItems()) {
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    String fileName = item.getName(); // Dosya adını al
                    if (fileName.startsWith("default")) {
                        defaultPictures.add(uri.toString());
                    } else if (fileName.startsWith("male")) {
                        malePictures.add(uri.toString());
                    } else if (fileName.startsWith("female")) {
                        femalePictures.add(uri.toString());
                    }

                    // Eğer tüm resimler işlendi ise sıralı listeyi birleştir
                    int totalSize = defaultPictures.size() + malePictures.size() + femalePictures.size();
                    if (totalSize == listResult.getItems().size()) {
                        List<String> orderedPictures = new ArrayList<>();
                        orderedPictures.addAll(defaultPictures);
                        orderedPictures.addAll(malePictures);
                        orderedPictures.addAll(femalePictures);

                        listener.onUrlsLoaded(orderedPictures);
                    }
                }).addOnFailureListener(e -> Log.e("FirebaseStorage", "URL alma hatası: " + e.getMessage()));
            }
        }).addOnFailureListener(e -> Log.e("FirebaseStorage", "Dosya listeleme hatası: " + e.getMessage()));
    }
}
