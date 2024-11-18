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
import com.raulrh.practicaandroid.util.Util;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MinesweeperFragment extends Fragment {

    private MinesweeperFragmentBinding binding;
    private MinesweeperGame game;
    private Timer timer;
    private int secondsAfterStart;

    private boolean isStarted = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = MinesweeperFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        game = new MinesweeperGame(10, 10, 15);
        setupGame();

        binding.startButton.setOnClickListener(v -> {
            if (!isStarted) {
                startGame();
                isStarted = true;
                binding.startButton.setText("Reiniciar");
            } else {
                setupGame();
                binding.startButton.setText("Empezar");
                timer.cancel();
                isStarted = false;
            }
        });
    }

    private void setupGame() {
        binding.buttonsPanel.removeAllViews();
        createGameBoard();
        game.setup();
    }

    private void startGame() {
        setupTimer();
        updateMinesLeft();
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

    private void createGameBoard() {
        for (int row = 0; row < game.getRows(); row++) {
            TableRow tableRow = createTableRow();
            for (int col = 0; col < game.getCols(); col++) {
                if (isValidCell(row, col)) {
                    ImageView cell = createCell(row, col);
                    tableRow.addView(cell);
                }
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
        if (!isStarted) {
            return;
        }

        if (isValidCell(row, col)) {
            if (game.clickCell(row, col)) {
                endGame("Allahu Akbar", "Has perdido el juego!");
                Util.playSound(requireContext(), R.raw.kabom);
            } else if (game.isGameWon()) {
                endGame("Felicidades", "Has ganado la partida!");
            }

            updateUI();
        }
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < game.getRows() && col >= 0 && col < game.getCols();
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
        binding.minesLeft.setText(String.format(Locale.getDefault(), "Minas Restantes: %d", game.getMinesLeft()));
    }

    private void updateTimeDisplay() {
        String strTime = String.format(Locale.getDefault(), "Tiempo: %02d:%02d", secondsAfterStart / 60, secondsAfterStart % 60);
        binding.time.setText(strTime);
    }

    private void showGameOverDialog(String title, String message) {
        game.endGame();
        new AlertDialog.Builder(requireContext())
                .setCancelable(false)
                .setIcon(R.mipmap.ic_launcher_round)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .show();
    }

    private void setIconToButton(ImageView imageView, Cell cell) {
        if (imageView == null) {
            return;
        }

        int value = cell.getValue();
        if (cell.isVisited()) {
            switch (value) {
                case Cell.EMPTY:
                    imageView.setImageResource(R.drawable.empty_cell);
                    break;
                case 1:
                    imageView.setImageResource(R.drawable.digit_1);
                    break;
                case 2:
                    imageView.setImageResource(R.drawable.digit_2);
                    break;
                case 3:
                    imageView.setImageResource(R.drawable.digit_3);
                    break;
                case 4:
                    imageView.setImageResource(R.drawable.digit_4);
                    break;
                case 5:
                    imageView.setImageResource(R.drawable.digit_5);
                    break;
                case 6:
                    imageView.setImageResource(R.drawable.digit_6);
                    break;
                case 7:
                    imageView.setImageResource(R.drawable.digit_7);
                    break;
                case 8:
                    imageView.setImageResource(R.drawable.digit_8);
                    break;
                case Cell.FLAGGED:
                    imageView.setImageResource(R.drawable.flag);
                    break;
                case Cell.MINE:
                    imageView.setImageResource(R.drawable.mine_clicked);
                    break;
            }
        } else if (cell.getValue() == Cell.UNCHECKED) {
            imageView.setImageResource(R.drawable.non_clicked_cell);
        } else if (cell.getValue() == Cell.FLAGGED) {
            imageView.setImageResource(R.drawable.flag);
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