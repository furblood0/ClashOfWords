package com.furkan.clashofwords.ui.gameplay;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.furkan.clashofwords.R;

import java.util.ArrayList;

public class GamePlayFragment extends Fragment {

    private GamePlayViewModel viewModel;
    private BotManager botManager;
    private Handler handler;

    private ProgressBar progressBar;
    private CountDownTimer countDownTimer;
    private static final int TIMER_DURATION = 15000;
    private static final int TIMER_INTERVAL = 100;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_play, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(GamePlayViewModel.class);
        handler = new Handler();

        initializeViewModelFromBundle();

        setupRecyclerView(view);
        setupProgressBar(view);
        handlePlayerInput(view);

        startTimer(this::handleTimeOutForPlayer);
    }

    private void initializeViewModelFromBundle() {
        if (getArguments() != null) {
            String question = getArguments().getString("question");
            ArrayList<String> correctAnswers = getArguments().getStringArrayList("correctAnswers");
            int roundNumber = getArguments().getInt("roundNumber", 1);
            int playerScore = getArguments().getInt("playerScore", 0);
            int botScore = getArguments().getInt("botScore", 0);

            viewModel.initializeFromBundle(question, correctAnswers, roundNumber, playerScore, botScore);

            if (correctAnswers != null) {
                botManager = new BotManager(correctAnswers);
            }
        }
    }

    private void setupRecyclerView(View view) {
        TextView questionText = view.findViewById(R.id.questionText);
        viewModel.getQuestion().observe(getViewLifecycleOwner(), questionText::setText);

        RecyclerView answersRecyclerView = view.findViewById(R.id.answersRecyclerView);
        AnswerAdapter answerAdapter = new AnswerAdapter(viewModel.getAnswers().getValue());
        answersRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        answersRecyclerView.setAdapter(answerAdapter);

        viewModel.getAnswers().observe(getViewLifecycleOwner(), answers -> {
            answerAdapter.notifyDataSetChanged();
            answersRecyclerView.scrollToPosition(answers.size() - 1);
        });
    }

    private void setupProgressBar(View view) {
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void handlePlayerInput(View view) {
        EditText answerInput = view.findViewById(R.id.answerInput);
        Button sendAnswerButton = view.findViewById(R.id.sendAnswerButton);

        sendAnswerButton.setOnClickListener(v -> {
            String playerAnswer = answerInput.getText().toString().trim();
            if (!playerAnswer.isEmpty()) {
                boolean isCorrect = viewModel.checkAnswer(playerAnswer);

                answerInput.setText("");

                if (isCorrect) {
                    countDownTimer.cancel();
                    disablePlayerInput(answerInput, sendAnswerButton);

                    // Eğer tüm doğru cevaplar kullanıldıysa, oyuncu turu kazanır.
                    if (viewModel.isAllCorrectAnswersUsed()) {
                        viewModel.addPointToWinner(true); // Oyuncuya 1 puan ekle
                        navigateToNextRound(view);
                    } else {
                        startBotTurn(answerInput, sendAnswerButton);
                    }
                }
            }
        });
    }

    private void startBotTurn(EditText input, Button button) {
        startTimer(() -> handleTimeOutForBot(input, button));

        botManager.botTurn(handler, new BotManager.BotCallback() {
            @Override
            public void onBotAnswer(String answer, boolean isCorrect) {
                countDownTimer.cancel();
                viewModel.addBotAnswer(answer);

                if (isCorrect) {
                    // Eğer tüm doğru cevaplar kullanıldıysa, bot turu kazanır.
                    if (viewModel.isAllCorrectAnswersUsed()) {
                        viewModel.addPointToWinner(false); // Bota 1 puan ekle
                        navigateToNextRound(requireView());
                    } else {
                        enablePlayerInput(input, button);
                        startTimer(GamePlayFragment.this::handleTimeOutForPlayer);
                    }
                }
            }

            @Override
            public void onBotFinished() {
                // Botun cevapları tükendiğinde süresi dolunca işlem yapılacak
                handleTimeOutForBot(input, button);
            }
        });
    }

    private void startTimer(Runnable onTimeOut) {
        progressBar.setMax(TIMER_DURATION);
        progressBar.setProgress(TIMER_DURATION);

        countDownTimer = new CountDownTimer(TIMER_DURATION, TIMER_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress((int) millisUntilFinished);
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(0);
                onTimeOut.run();
            }
        }.start();
    }

    private void handleTimeOutForPlayer() {
        Toast.makeText(requireContext(), "Süre doldu! Turu kaybettiniz.", Toast.LENGTH_SHORT).show();
        viewModel.addPointToWinner(false); // Bot kazandı

        if (viewModel.isGameOver()) {
            navigateToResultFragment(requireView()); // Eğer biri 2 puana ulaştıysa, ResultFragment'a geç
        } else {
            navigateToNextRound(requireView()); // Aksi halde bir sonraki tura geç
        }
    }

    private void handleTimeOutForBot(EditText input, Button button) {
        Toast.makeText(requireContext(), "Botun süresi doldu! Turu kazandınız.", Toast.LENGTH_SHORT).show();
        viewModel.addPointToWinner(true); // Oyuncu kazandı

        if (viewModel.isGameOver()) {
            navigateToResultFragment(requireView()); // Eğer biri 2 puana ulaştıysa, ResultFragment'a geç
        } else {
            navigateToNextRound(requireView()); // Aksi halde bir sonraki tura geç
        }
    }

    private void navigateToNextRound(View view) {
        if (viewModel.isGameOver()) {
            navigateToResultFragment(view); // Eğer oyun bittiyse ResultFragment'a geç
        } else {
            // Aksi halde bir sonraki tura geç
            Bundle bundle = new Bundle();
            bundle.putInt("playerScore", viewModel.getPlayerScore().getValue());
            bundle.putInt("botScore", viewModel.getBotScore().getValue());
            bundle.putInt("roundNumber", viewModel.getRoundNumber().getValue() + 1);

            Navigation.findNavController(view).navigate(
                    R.id.action_gameplay_to_question_start,
                    bundle
            );
        }
    }

    private void navigateToResultFragment(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("playerScore", viewModel.getPlayerScore().getValue());
        bundle.putInt("botScore", viewModel.getBotScore().getValue());
        bundle.putString("winner", viewModel.determineWinner()); // Kazananı belirle

        Navigation.findNavController(view).navigate(
                R.id.action_gameplay_to_result, // ResultFragment yönü
                bundle
        );
    }


    private void disablePlayerInput(EditText input, Button button) {
        input.setEnabled(false);
        input.setHint("Sıra rakipte...");
        button.setEnabled(false);
    }

    private void enablePlayerInput(EditText input, Button button) {
        input.setEnabled(true);
        input.setHint("Cevap yaz...");
        button.setEnabled(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
