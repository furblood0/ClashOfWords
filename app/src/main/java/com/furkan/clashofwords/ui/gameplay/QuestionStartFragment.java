package com.furkan.clashofwords.ui.gameplay;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.furkan.clashofwords.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class QuestionStartFragment extends Fragment {
    private QuestionStartViewModel viewModel;
    private TextView questionTextView;
    private TextView usernameTextView;
    private TextView botNameTextView;
    private TextView countdownText;
    private TextView roundTextView;
    private TextView scoreTextView;
    private ImageView profileImageView;
    private ImageView botProfileImageView;

    private final String BOT_NAME = "Bot";
    private final String BOT_PROFILE_PICTURE_URL = "https://firebasestorage.googleapis.com/v0/b/clashofwordsproject.firebasestorage.app/o/botProfiles%2Fcat.png?alt=media&token=d101b023-c8da-49a0-b5e5-3291d16c530b"; // Bot resminin URL'si

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question_start, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        questionTextView = view.findViewById(R.id.questionBackground);
        usernameTextView = view.findViewById(R.id.playerName);
        botNameTextView = view.findViewById(R.id.opponentName);
        countdownText = view.findViewById(R.id.countdownText);
        roundTextView = view.findViewById(R.id.turText); // Tur bilgisi
        scoreTextView = view.findViewById(R.id.scoreText); // Skor bilgisi
        profileImageView = view.findViewById(R.id.playerProfile);
        botProfileImageView = view.findViewById(R.id.opponentProfile);

        viewModel = new ViewModelProvider(this).get(QuestionStartViewModel.class);

        // GamePlayFragment'tan gelen verileri kontrol et
        if (getArguments() != null) {
            int playerScore = getArguments().getInt("playerScore", 0);
            int botScore = getArguments().getInt("botScore", 0);
            int roundNumber = getArguments().getInt("roundNumber", 1);

            // Tek bir metod çağrısı ile tüm state'i güncelle
            viewModel.updateGameState(playerScore, botScore, roundNumber);
        }

        // Mevcut kullanıcı UID'sini al
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Kullanıcı bilgilerini çek
        viewModel.fetchUserDetails(userId);

        // Kullanıcı adı güncellemesi
        viewModel.getUsername().observe(getViewLifecycleOwner(), username -> {
            if (username != null) {
                usernameTextView.setText(username);
            } else {
                usernameTextView.setText("Oyuncu"); // Varsayılan değer
            }
        });

        // Kullanıcı profil fotoğrafı güncellemesi
        viewModel.getProfilePicture().observe(getViewLifecycleOwner(), profilePictureUrl -> {
            if (profilePictureUrl != null) {
                Glide.with(this)
                        .load(profilePictureUrl)
                        .placeholder(R.drawable.defaultppicon64)
                        .into(profileImageView);
            } else {
                profileImageView.setImageResource(R.drawable.defaultppicon64); // Varsayılan profil resmi
            }
        });

        // Bot bilgilerini ayarla
        botNameTextView.setText(BOT_NAME);
        Glide.with(this)
                .load(BOT_PROFILE_PICTURE_URL)
                .placeholder(R.drawable.defaultppicon64)
                .into(botProfileImageView);

        // Tur ve skor bilgilerini güncelle
        viewModel.getRoundNumber().observe(getViewLifecycleOwner(), round -> {
            roundTextView.setText("Tur: " + (round != null ? round : 1));
        });

        viewModel.getScores().observe(getViewLifecycleOwner(), scores -> {
            if (scores != null) {
                scoreTextView.setText(scores.getPlayerScore() + " - " + scores.getBotScore());
            } else {
                scoreTextView.setText("0 - 0"); // Varsayılan skor
            }
        });

        // Soru bilgilerini çek
        viewModel.fetchRandomQuestion();

        // Soru güncellenince TextView'i güncelle
        viewModel.getCurrentQuestion().observe(getViewLifecycleOwner(), question -> {
            if (question != null) {
                questionTextView.setText(question);
            } else {
                questionTextView.setText("Soru yüklenemedi!"); // Varsayılan metin
            }
        });

        // Geri sayımı başlat
        startCountdown(view);
    }

    private void startCountdown(View view) {
        new CountDownTimer(10000, 1000) { // 10 saniyelik geri sayım
            @Override
            public void onTick(long millisUntilFinished) {
                countdownText.setText(String.valueOf(millisUntilFinished / 1000)); // Her saniye güncelle
            }

            @Override
            public void onFinish() {
                countdownText.setText("0"); // Geri sayım tamamlandı

                // Soru ve doğru cevapları al
                String question = viewModel.getCurrentQuestion().getValue();
                List<String> correctAnswers = viewModel.getCorrectAnswers().getValue();
                int roundNumber = viewModel.getRoundNumber().getValue() != null ? viewModel.getRoundNumber().getValue() : 1;
                int playerScore = viewModel.getScores().getValue() != null ? viewModel.getScores().getValue().getPlayerScore() : 0;
                int botScore = viewModel.getScores().getValue() != null ? viewModel.getScores().getValue().getBotScore() : 0;

                if (question != null && correctAnswers != null) {
                    // Bundle oluştur ve veriyi GamePlayFragment'a ilet
                    Bundle bundle = new Bundle();
                    bundle.putString("question", question);
                    bundle.putStringArrayList("correctAnswers", new ArrayList<>(correctAnswers));
                    bundle.putInt("roundNumber", roundNumber);
                    bundle.putInt("playerScore", playerScore);
                    bundle.putInt("botScore", botScore);

                    // GamePlayFragment'a geçiş
                    View currentView = getView();
                    if (currentView != null) {
                        Navigation.findNavController(currentView).navigate(R.id.action_question_start_to_gameplay, bundle);
                    } else {
                        Toast.makeText(requireContext(), "Navigasyon sırasında bir hata oluştu.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }.start();
    }
}
