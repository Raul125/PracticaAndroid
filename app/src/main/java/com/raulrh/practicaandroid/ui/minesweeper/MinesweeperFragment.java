package com.raulrh.practicaandroid.ui.minesweeper;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;

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

    private MinesweeperFragmentBinding binding;
    private MinesweeperGame game;
    private Timer timer;
    private int secondsAfterStart;

    private boolean isStarted;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = MinesweeperFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int rows = SharedPrefsUtil.getInt(requireContext(), SharedPrefsUtil.PREF_KEY_ROWS, 10);
        int cols = SharedPrefsUtil.getInt(requireContext(), SharedPrefsUtil.PREF_KEY_COLS, 10);
        int mines = SharedPrefsUtil.getInt(requireContext(), SharedPrefsUtil.PREF_KEY_MINES, 15);
        game = new MinesweeperGame(rows, cols, mines);

        binding.startButton.setOnClickListener(v -> {
            if (!isStarted) {
                startGame();
            } else {
                restartGame();
            }
        });

        setupGame();
    }

    private void setupGame() {
        binding.buttonsPanel.removeAllViews();
        createGameBoard();
        updateMinesLeft();
        resetTimer();
    }

    private void restartGame() {
        game.restart();
        updateUI();
        resetTimer();
        isStarted = false;
        binding.startButton.setText(getString(R.string.start));
    }

    private void startGame() {
        binding.startButton.setText(getString(R.string.restart));
        setupTimer();
        isStarted = true;
    }

    private void endGame(String title, String message) {
        timer.cancel();
        showGameOverDialog(title, message);
    }

    private void setupTimer() {
        if (timer != null) {
            timer.cancel();
        }

        secondsAfterStart = 0;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                requireActivity().runOnUiThread(() -> {
                    secondsAfterStart++;
                    updateTimeDisplay();
                });
            }
        }, 0, 1000);
    }

    private void resetTimer() {
        if (timer != null) {
            timer.cancel();
        }

        secondsAfterStart = 0;
        updateTimeDisplay();
    }

    private void createGameBoard() {
        for (int row = 0; row < game.getRows(); row++) {
            TableRow tableRow = createTableRow();
            for (int col = 0; col < game.getCols(); col++) {
                ImageView cell = createCell(row, col);
                tableRow.addView(cell);
            }

            binding.buttonsPanel.addView(tableRow);
        }
    }

    private TableRow createTableRow() {
        TableRow tableRow = new TableRow(getContext());
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tableRow.setGravity(Gravity.CENTER);
        return tableRow;
    }

    private ImageView createCell(int row, int col) {
        ImageView cell = new ImageView(requireContext());
        cell.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        cell.setId(row * game.getCols() + col);
        cell.setImageResource(R.drawable.non_clicked_cell);
        cell.setScaleType(ImageView.ScaleType.FIT_CENTER);

        setupCellListeners(cell, row, col);
        return cell;
    }

    private void setupCellListeners(ImageView cell, int row, int col) {
        cell.setOnClickListener(v -> handleCellClick(row, col));
        cell.setOnLongClickListener(v -> {
            if (isStarted) {
                game.flagCell(row, col);
                updateUI();
            }

            return true;
        });
    }

    private void handleCellClick(int row, int col) {
        if (!isStarted || game.isGameOver()) {
            return;
        }

        if (game.clickCell(row, col)) {
            endGame(getString(R.string.loseTitle), getString(R.string.loseText));
            Util.playSound(requireContext(), R.raw.kabom);
        } else if (game.isGameWon()) {
            endGame(getString(R.string.winTitle), getString(R.string.winText));
            Util.playSound(requireContext(), R.raw.victory);
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

        updateUI();
    }

    private void updateUI() {
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getCols(); col++) {
                ImageView cell = binding.getRoot().findViewById(row * game.getCols() + col);
                setIconToButton(cell, game.getCell(row, col));
            }
        }

        updateMinesLeft();
    }

    private void updateMinesLeft() {
        binding.minesLeft.setText(String.format(Locale.getDefault(), getString(R.string.remaining_mines), game.getMinesLeft()));
    }

    private void updateTimeDisplay() {
        String strTime = String.format(Locale.getDefault(), getString(R.string.time), secondsAfterStart / 60, secondsAfterStart % 60);
        binding.time.setText(strTime);
    }

    private void showGameOverDialog(String title, String message) {
        game.endGame();
        new AlertDialog.Builder(requireContext())
                .setCancelable(false)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .show();
    }

    private void setIconToButton(ImageView imageView, Cell cell) {
        if (imageView == null) {
            return;
        }

        if (cell.isFlagged()) {
            imageView.setImageResource(R.drawable.flag);
        } else if (cell.isVisited()) {
            if (cell.isMine()) {
                if (cell.isClicked()) {
                    imageView.setImageResource(R.drawable.mine_clicked);
                } else {
                    imageView.setImageResource(R.drawable.mine);
                }
            } else {
                int value = cell.getValue();
                switch (value) {
                    case 0:
                        imageView.setImageResource(R.drawable.empty_cell);
                        break;
                    case 1:
                        imageView.setImageResource(R.drawable.mine_1);
                        break;
                    case 2:
                        imageView.setImageResource(R.drawable.mine_2);
                        break;
                    case 3:
                        imageView.setImageResource(R.drawable.mine_3);
                        break;
                    case 4:
                        imageView.setImageResource(R.drawable.mine_4);
                        break;
                    case 5:
                        imageView.setImageResource(R.drawable.mine_5);
                        break;
                    case 6:
                        imageView.setImageResource(R.drawable.mine_6);
                        break;
                    case 7:
                        imageView.setImageResource(R.drawable.mine_7);
                        break;
                    case 8:
                        imageView.setImageResource(R.drawable.mine_8);
                        break;
                }
            }
        } else {
            imageView.setImageResource(R.drawable.non_clicked_cell);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}