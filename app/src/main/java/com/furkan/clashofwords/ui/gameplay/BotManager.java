package com.furkan.clashofwords.ui.gameplay;

import android.os.Handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BotManager {
    private final List<String> correctAnswers;
    private final List<String> remainingAnswers;
    private final Random random = new Random();

    public BotManager(List<String> correctAnswers) {
        this.correctAnswers = new ArrayList<>();
        for (String answer : correctAnswers) {
            this.correctAnswers.add(answer.toLowerCase()); // Küçük harfe çevirerek ekle
        }

        this.remainingAnswers = new ArrayList<>(this.correctAnswers);

        // Botun bileceği cevapları seçelim (%40 doğru cevap)
        int botAnswerCount = (int) Math.ceil(this.correctAnswers.size() * 0.2); // %40
        Collections.shuffle(this.remainingAnswers); // Karıştır
        while (this.remainingAnswers.size() > botAnswerCount) {
            this.remainingAnswers.remove(this.remainingAnswers.size() - 1);
        }
    }

    /**
     * Botun sırasını başlatır ve cevabı Callback ile geri döner.
     */
    public void botTurn(Handler handler, BotCallback callback) {
        if (remainingAnswers.isEmpty()) {
            // Eğer cevap yoksa, botun süresinin dolmasını bekle
            return;
        }

        int responseTime = random.nextInt(2000) + 3000; // 3-5 saniye arasında cevaplama süresi

        handler.postDelayed(() -> {
            if (!remainingAnswers.isEmpty()) {
                String botAnswer = remainingAnswers.remove(0); // Bot cevabını al
                callback.onBotAnswer(botAnswer, true); // Bot cevabını gönder
            }
        }, responseTime);
    }


    /**
     * Botun bilebileceği cevapları döndürür (debug veya test için kullanılabilir).
     */
    public List<String> getRemainingAnswers() {
        return new ArrayList<>(remainingAnswers); // Kalan cevapları döndür
    }

    /**
     * Callback interface'i: Botun cevabını veya işlem bitişini yönetmek için kullanılır.
     */
    public interface BotCallback {
        void onBotAnswer(String answer, boolean isCorrect);

        void onBotFinished();
    }
}
