package com.furkan.clashofwords.ui.gameplay;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GamePlayViewModel extends ViewModel {

    private final MutableLiveData<String> question = new MutableLiveData<>();
    private List<String> correctAnswers; // Doğru cevaplar
    private final MutableLiveData<List<AnswerItem>> answers = new MutableLiveData<>(new ArrayList<>()); // Cevap listesi
    private final Set<String> usedAnswers = new HashSet<>(); // Kullanılmış cevaplar

    private final MutableLiveData<Integer> roundNumber = new MutableLiveData<>(1); // Tur bilgisi
    private final MutableLiveData<Integer> playerScore = new MutableLiveData<>(0); // Kullanıcı skoru
    private final MutableLiveData<Integer> botScore = new MutableLiveData<>(0); // Bot skoru

    public LiveData<String> getQuestion() {
        return question;
    }

    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }

    public LiveData<List<AnswerItem>> getAnswers() {
        return answers;
    }

    public Set<String> getUsedAnswers() {
        return usedAnswers;
    }

    public LiveData<Integer> getRoundNumber() {
        return roundNumber;
    }

    public LiveData<Integer> getPlayerScore() {
        return playerScore;
    }

    public LiveData<Integer> getBotScore() {
        return botScore;
    }

    public void setQuestion(String questionText, List<String> correctAnswersList) {
        question.setValue(questionText);
        correctAnswers = new ArrayList<>();
        for (String answer : correctAnswersList) {
            correctAnswers.add(answer.toLowerCase());
        }
        usedAnswers.clear();
    }

    public void initializeFromBundle(String question, List<String> correctAnswers, int roundNumber, int playerScore, int botScore) {
        setRoundNumber(roundNumber);
        setPlayerScore(playerScore);
        setBotScore(botScore);

        if (question != null && correctAnswers != null) {
            setQuestion(question, correctAnswers);
        }
    }

    public boolean checkAnswer(String userAnswer) {
        String normalizedAnswer = userAnswer.trim().toLowerCase();

        if (usedAnswers.contains(normalizedAnswer)) {
            return false;
        }

        boolean isCorrect = correctAnswers != null && correctAnswers.contains(normalizedAnswer);
        addAnswer(new AnswerItem(userAnswer, isCorrect, true));
        usedAnswers.add(normalizedAnswer);
        return isCorrect;
    }

    public void addBotAnswer(String botAnswer) {
        String normalizedAnswer = botAnswer.toLowerCase();

        if (!usedAnswers.contains(normalizedAnswer)) {
            boolean isCorrect = correctAnswers != null && correctAnswers.contains(normalizedAnswer);
            addAnswer(new AnswerItem(botAnswer, isCorrect, false));
            usedAnswers.add(normalizedAnswer);
        }
    }

    private void addAnswer(AnswerItem answerItem) {
        List<AnswerItem> currentAnswers = answers.getValue();
        if (currentAnswers != null) {
            currentAnswers.add(answerItem);
            answers.setValue(currentAnswers);
        }
    }

    public void incrementRoundNumber() {
        if (roundNumber.getValue() != null) {
            roundNumber.setValue(roundNumber.getValue() + 1);
        }
    }

    public void addPointToWinner(boolean isPlayerWinner) {
        if (isPlayerWinner) {
            if (playerScore.getValue() != null) {
                playerScore.setValue(playerScore.getValue() + 1);
            }
        } else {
            if (botScore.getValue() != null) {
                botScore.setValue(botScore.getValue() + 1);
            }
        }
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber.setValue(roundNumber);
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore.setValue(playerScore);
    }

    public void setBotScore(int botScore) {
        this.botScore.setValue(botScore);
    }

    public boolean isAllCorrectAnswersUsed() {
        if (correctAnswers == null || correctAnswers.isEmpty()) {
            return false;
        }
        return usedAnswers.containsAll(correctAnswers);
    }

    public boolean isGameOver() {
        return (playerScore.getValue() != null && playerScore.getValue() == 2) ||
                (botScore.getValue() != null && botScore.getValue() == 2);
    }

    public String determineWinner() {
        if (playerScore.getValue() != null && playerScore.getValue() == 2) {
            return "player"; // Oyuncu kazandı
        } else if (botScore.getValue() != null && botScore.getValue() == 2) {
            return "bot"; // Bot kazandı
        }
        return "draw"; // Beraberlik durumu
    }



}

