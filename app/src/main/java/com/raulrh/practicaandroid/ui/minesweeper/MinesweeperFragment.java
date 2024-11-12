package com.raulrh.practicaandroid.ui.minesweeper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.raulrh.practicaandroid.databinding.MinesweeperFragmentBinding;

public class MinesweeperFragment extends Fragment {

    private MinesweeperFragmentBinding binding;

    private GameLogic gameLogic;
    private final int rows = 8;
    private final int cols = 8;
    private final int mines = 10;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = MinesweeperFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gameLogic = new GameLogic(rows, cols, mines);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Button cellButton = new Button(getContext());
                cellButton.setLayoutParams(new GridLayout.LayoutParams());
                int finalI = i;
                int finalJ = j;

                // Configura el evento de clic corto para revelar la celda
                cellButton.setOnClickListener(v -> revealCell(finalI, finalJ, cellButton));

                // Configura el evento de clic largo para marcar la celda
                cellButton.setOnLongClickListener(v -> {
                    Cell cell = gameLogic.getCell(finalI, finalJ);
                    if (!cell.isRevealed()) {
                        // Alterna entre marcar y desmarcar la celda como posible mina
                        if (cellButton.getText().equals("🚩")) {
                            cellButton.setText("");
                        } else {
                            cellButton.setText("🚩");
                        }
                    }
                    return true;
                });

                binding.gameGrid.addView(cellButton);
            }
        }
    }

    private void revealCell(int row, int col, Button cellButton) {
        Cell cell = gameLogic.getCell(row, col);

        if (cell.isMine()) {
            cellButton.setText("X");
            Toast.makeText(getContext(), "Game Over!", Toast.LENGTH_SHORT).show();
            // Revela todas las celdas y finaliza el juego
        } else {
            cellButton.setText(String.valueOf(cell.getAdjacentMines()));
            cellButton.setEnabled(false);
            cell.setRevealed(true);

            if (cell.getAdjacentMines() == 0) {
                // Si no hay minas adyacentes, revela las celdas circundantes
                revealAdjacentCells(row, col);
            }

            checkForWin();
        }
    }

    private void revealAdjacentCells(int row, int col) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;

                // Asegúrate de que la celda esté dentro de los límites
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                    Cell adjacentCell = gameLogic.getCell(newRow, newCol);

                    if (!adjacentCell.isRevealed() && !adjacentCell.isMine()) {
                        Button adjacentButton = (Button) binding.gameGrid.getChildAt(newRow * cols + newCol);
                        adjacentButton.setText(String.valueOf(adjacentCell.getAdjacentMines()));
                        adjacentButton.setEnabled(false);
                        adjacentCell.setRevealed(true);

                        // Si no hay minas adyacentes, sigue revelando recursivamente
                        if (adjacentCell.getAdjacentMines() == 0) {
                            revealAdjacentCells(newRow, newCol);
                        }
                    }
                }
            }
        }
    }

    private void checkForWin() {
        boolean won = true;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell cell = gameLogic.getCell(i, j);
                if (!cell.isMine() && !cell.isRevealed()) {
                    won = false;
                    break;
                }
            }
        }

        if (won) {
            Toast.makeText(getContext(), "¡Felicidades, has ganado!", Toast.LENGTH_SHORT).show();
            // Desactiva todas las celdas o reinicia el juego
            disableAllCells();
        }
    }

    private void disableAllCells() {
        for (int i = 0; i < binding.gameGrid.getChildCount(); i++) {
            binding.gameGrid.getChildAt(i).setEnabled(false);
        }
    }

}