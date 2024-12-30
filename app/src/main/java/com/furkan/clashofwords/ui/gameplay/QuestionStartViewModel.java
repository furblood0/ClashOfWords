package com.furkan.clashofwords.ui.gameplay;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Random;

public class QuestionStartViewModel extends ViewModel {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<String> currentQuestion = new MutableLiveData<>();
    private final MutableLiveData<List<String>> correctAnswers = new MutableLiveData<>();
    private final MutableLiveData<String> username = new MutableLiveData<>();
    private final MutableLiveData<String> profilePicture = new MutableLiveData<>();
    private final MutableLiveData<Integer> roundNumber = new MutableLiveData<>(1); // Başlangıç turu
    private final MutableLiveData<Score> scores = new MutableLiveData<>(new Score(0, 0)); // Skorları yönetmek için Score sınıfı
    private String lastQuestion; // Önceki soruyu tutmak için

    // Getter'lar
    public LiveData<String> getCurrentQuestion() {
        return currentQuestion;
    }

    public LiveData<List<String>> getCorrectAnswers() {
        return correctAnswers;
    }

    public LiveData<String> getUsername() {
        return username;
    }

    public LiveData<String> getProfilePicture() {
        return profilePicture;
    }

    public LiveData<Integer> getRoundNumber() {
        return roundNumber;
    }

    public LiveData<Score> getScores() {
        return scores;
    }

    // Kullanıcı bilgilerini çekmek için metod
    public void fetchUserDetails(String userId) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        username.setValue(documentSnapshot.getString("username"));
                        profilePicture.setValue(documentSnapshot.getString("profilePicture"));
                    }
                })
                .addOnFailureListener(Throwable::printStackTrace);
    }

    // Soru çekme
    public void fetchRandomQuestion() {
        db.collection("questions")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                        DocumentSnapshot randomDocument;

                        // Önceki soru ile aynı olmayan bir soru seç
                        do {
                            int randomIndex = new Random().nextInt(documents.size());
                            randomDocument = documents.get(randomIndex);
                        } while (lastQuestion != null && randomDocument.getString("question").equals(lastQuestion));

                        String questionText = randomDocument.getString("question");
                        List<String> answers = (List<String>) randomDocument.get("correctAnswers");

                        currentQuestion.setValue(questionText);
                        correctAnswers.setValue(answers);
                        lastQuestion = questionText; // Yeni soruyu kaydet
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace(); // Hata durumunda loglama
                });
    }

    private List<String> getAnswersForQuestion(String question) {
        // Örnek cevaplar (Firebase ile bağlantılı bu listeyi çekmeniz gerekiyor)
        return List.of("example1", "example2", "example3");
    }

    // Oyun durumunu güncelleme
    public void updateGameState(int playerScore, int botScore, int roundNumber) {
        scores.setValue(new Score(playerScore, botScore));
        this.roundNumber.setValue(roundNumber);
    }

    // Skorları temsil eden sınıf
    public static class Score {
        private int playerScore;
        private int botScore;

        public Score(int playerScore, int botScore) {
            this.playerScore = playerScore;
            this.botScore = botScore;
        }

        public int getPlayerScore() {
            return playerScore;
        }

        public void setPlayerScore(int playerScore) {
            this.playerScore = playerScore;
        }

        public int getBotScore() {
            return botScore;
        }

        public void setBotScore(int botScore) {
            this.botScore = botScore;
        }
    }
}
