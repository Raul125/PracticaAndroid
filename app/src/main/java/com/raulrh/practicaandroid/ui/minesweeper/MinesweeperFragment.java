package com.raulrh.practicaandroid.ui.minesweeper;

import android.os.Bundle;
import android.util.Log;
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

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MinesweeperFragment extends Fragment {

    private MinesweeperFragmentBinding binding;
    private MinesweeperGame game;
    private Timer timer;
    private int secondsAfterStart;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = MinesweeperFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeGame();
        setupTimer();
        createGameBoard();
    }

    private void initializeGame() {
        game = new MinesweeperGame(10, 10, 15);
        updateMinesLeft();
    }

    private void setupTimer() {
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
                } else {
                    Log.e("MinesweeperFragment", "Invalid cell access: row=" + row + ", col=" + col);
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
            game.flagCell(row, col);
            updateUI();
            return true;
        });
    }

    private void handleCellClick(int row, int col) {
        if (isValidCell(row, col)) {
            if (game.clickCell(row, col)) {
                endGame("Game over", "Unfortunately, you've lost the game!");
            } else if (game.isGameWon()) {
                endGame("Congratulations", "You've won the game!");
            }

            updateUI();
        } else {
            Log.e("MinesweeperFragment", "Invalid cell access: row=" + row + ", col=" + col);
        }
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < game.getRows() && col >= 0 && col < game.getCols();
    }

    private void endGame(String title, String message) {
        timer.cancel();
        showGameOverDialog(title, message);
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
        binding.minesLeft.setText(String.format(Locale.getDefault(), "Mines Left: %d", game.getMinesLeft()));
    }

    private void updateTimeDisplay() {
        String strTime = String.format(Locale.getDefault(), "Time: %02d:%02d", secondsAfterStart / 60, secondsAfterStart % 60);
        binding.time.setText(strTime);
    }

    private void showGameOverDialog(String title, String message) {
        new AlertDialog.Builder(requireContext())
                .setCancelable(false)
                .setIcon(R.mipmap.ic_launcher_round)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", (dialogInterface, i) -> Log.d("Minesweeper", "Reiniciar juego"))
                .show();
    }

    private void setIconToButton(ImageView imageView, Cell cell) {
        if (imageView == null) {
            Log.d("setIconToButton", "Button was not found!");
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