package com.furkan.clashofwords.ui.gameplay;

public class AnswerItem {
    private final String answerText;
    private final boolean isCorrect;
    private final boolean isUser; // Kullanıcı cevabı mı?

    public AnswerItem(String answerText, boolean isCorrect, boolean isUser) {
        this.answerText = answerText;
        this.isCorrect = isCorrect;
        this.isUser = isUser;
    }

    public String getAnswerText() {
        return answerText;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public boolean isUser() {
        return isUser;
    }
}
