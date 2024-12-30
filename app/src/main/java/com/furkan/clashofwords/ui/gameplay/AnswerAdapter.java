package com.furkan.clashofwords.ui.gameplay;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.furkan.clashofwords.R;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {

    private final List<AnswerItem> answers;

    public AnswerAdapter(List<AnswerItem> answers) {
        this.answers = answers;
    }

    @Override
    public int getItemViewType(int position) {
        AnswerItem answer = answers.get(position);
        return answer.isUser() ? 0 : 1; // Kullanıcı cevabı 0, bot cevabı 1
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            // Kullanıcı cevabı için layout
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer_user, parent, false);
        } else {
            // Bot cevabı için layout
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer_bot, parent, false);
        }
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        AnswerItem answer = answers.get(position);
        holder.answerTextView.setText(answer.getAnswerText());

        // Cevap doğruysa yeşil, yanlışsa kırmızı
        if (answer.isCorrect()) {
            holder.answerTextView.setTextColor(Color.GREEN);
        } else {
            holder.answerTextView.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public static class AnswerViewHolder extends RecyclerView.ViewHolder {
        TextView answerTextView;

        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            answerTextView = itemView.findViewById(R.id.answerText);
        }
    }
}
