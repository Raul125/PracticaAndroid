package com.raulrh.practicaandroid.ui.minesweeper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.raulrh.practicaandroid.R;
import com.raulrh.practicaandroid.databinding.MinesweeperFragmentBinding;
import com.raulrh.practicaandroid.util.SharedPrefsUtil;
import com.raulrh.practicaandroid.util.Util;

import java.util.Arrays;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Angle;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.Spread;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;

public class MinesweeperFragment extends Fragment {
    private MinesweeperGame game;
    private Timer timer;
    private int secondsElapsed;
    public MinesweeperFragmentBinding binding;

    private boolean isGameStarted = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = MinesweeperFragmentBinding.inflate(inflater, container, false);
        setupGame();

        binding.startButton.setOnClickListener(v -> {
            if (!isGameStarted) {
                game.startGame();
                resetTimer();
                updateMinesLeft();
                binding.startButton.setText(getString(R.string.restart));
                isGameStarted = true;
            } else {
                setupGame();
                binding.startButton.setText(getString(R.string.start));
                isGameStarted = false;
            }
        });

        return binding.getRoot();
    }

    private void setupGame() {
        int rows = SharedPrefsUtil.getInt(requireContext(), SharedPrefsUtil.PREF_KEY_ROWS, 10);
        int cols = SharedPrefsUtil.getInt(requireContext(), SharedPrefsUtil.PREF_KEY_COLS, 10);
        int mines = SharedPrefsUtil.getInt(requireContext(), SharedPrefsUtil.PREF_KEY_MINES, 15);
        game = new MinesweeperGame(rows, cols, mines, this);
    }

    public void onCellClicked() {
        if (!game.isGameInProgress()) {
            showDialog(getString(R.string.loseTitle), getString(R.string.loseText));
            Util.playSound(requireContext(), R.raw.kabom);
            if (timer != null) {
                timer.cancel();
            }
        } else if (game.isGameWon()) {
            game.endGame();
            showDialog(getString(R.string.winTitle), getString(R.string.winText));
            Util.playSound(requireContext(), R.raw.victory);
            if (timer != null) {
                timer.cancel();
            }

            EmitterConfig emitterConfig = new Emitter(5, TimeUnit.SECONDS).perSecond(100);
            binding.konfettiView.start(
                    new PartyFactory(emitterConfig)
                            .angle(Angle.BOTTOM)
                            .spread(Spread.ROUND)
                            .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE))
                            .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                            .setSpeedBetween(0f, 15f)
                            .position(new Position.Relative(0.0, 0.0).between(new Position.Relative(1.0, 0.0)))
                            .build());
        }
    }

    public void updateMinesLeft() {
        long flaggedCount = game.getBoard().stream().filter(Cell::isFlagged).count();
        binding.minesLeft.setText(String.format(Locale.getDefault(), getString(R.string.remaining_mines), game.getNumOfMines() - flaggedCount));
    }

    private void resetTimer() {
        if (timer != null) {
            timer.cancel();
        }

        secondsElapsed = 0;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (game.isGameInProgress()) {
                    secondsElapsed++;
                    requireActivity().runOnUiThread(() -> binding.time.setText(String.format(Locale.getDefault(), getString(R.string.time), secondsElapsed / 60, secondsElapsed % 60)));
                } else {
                    cancel();
                }
            }
        }, 0, 1000);
    }

    private void showDialog(String title, String message) {
        new AlertDialog.Builder(requireContext())
                .setCancelable(false)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .show();
    }

}