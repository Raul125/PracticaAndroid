package com.raulrh.practicaandroid.ui.minesweeper;

import android.widget.ImageView;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class MinesweeperGame {
    private final int rows;
    private final int cols;
    private final int numOfMines;
    private final List<Cell> board;
    private boolean gameInProgress;
    private final MinesweeperFragment fragment;

    public MinesweeperGame(int rows, int cols, int numOfMines, MinesweeperFragment fragment) {
        this.rows = rows;
        this.cols = cols;
        this.numOfMines = numOfMines;
        this.board = new ArrayList<>();
        this.gameInProgress = false;
        this.fragment = fragment;

        initializeBoard();
        generateMines();
    }

    private void initializeBoard() {
        fragment.binding.buttonsPanel.removeAllViews();
        for (int row = 0; row < rows; row++) {
            TableRow tableRow = new TableRow(fragment.requireContext());
            for (int col = 0; col < cols; col++) {
                ImageView imageView = new ImageView(fragment.requireContext());
                Cell cell = new Cell(imageView, row, col);
                imageView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                imageView.setOnClickListener(v -> revealCell(cell));
                imageView.setOnLongClickListener(v -> {
                    if (isGameInProgress()) {
                        cell.flag();
                        fragment.updateMinesLeft();
                    }

                    return true;
                });

                tableRow.addView(imageView);
                board.add(cell);
            }

            fragment.binding.buttonsPanel.addView(tableRow);
        }
    }

    public void startGame() {
        gameInProgress = true;
    }

    public void endGame() {
        for (Cell cell : board) {
            cell.setVisited(true);
            cell.updateIcon();
        }

        gameInProgress = false;
    }

    private void generateMines() {
        Random random = new Random();
        HashSet<Integer> minePositions = new HashSet<>();
        while (minePositions.size() < numOfMines) {
            minePositions.add(random.nextInt(rows * cols));
        }

        for (int position : minePositions) {
            int row = position / cols;
            int col = position % cols;
            board.stream()
                    .filter(cell -> cell.getRowPosition() == row && cell.getColumnPosition() == col)
                    .findFirst()
                    .ifPresent(x -> {
                        x.setMine(true);
                        x.updateIcon();
                    });
        }

        for (Cell cell : board) {
            if (cell.isMine()) {
                continue;
            }

            int minesAround = countMinesAround(cell.getRowPosition(), cell.getColumnPosition());
            cell.setValue(minesAround);
            cell.updateIcon();
        }
    }

    private int countMinesAround(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;
                boolean isMine = board.stream().anyMatch(cell -> cell.getRowPosition() == newRow
                        && cell.getColumnPosition() == newCol && cell.isMine());
                if (isMine) {
                    count++;
                }
            }
        }

        return count;
    }

    public void revealCell(Cell cell) {
        if (!gameInProgress || cell.isVisited() || cell.isFlagged()) {
            return;
        }

        cell.setVisited(true);
        cell.setClicked(true);
        if (cell.isMine()) {
            endGame();
        } else if (cell.getValue() == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int row = cell.getRowPosition() + i;
                    int col = cell.getColumnPosition() + j;
                    board.stream()
                            .filter(newCell -> newCell.getRowPosition() == row && newCell.getColumnPosition() == col)
                            .findFirst()
                            .ifPresent(this::revealCell);
                }
            }
        }

        cell.updateIcon();
        fragment.onCellClicked();
    }

    public boolean isGameWon() {
        for (Cell cell : board) {
            if (!cell.isMine() && !cell.isVisited()) {
                return false;
            }
        }

        return true;
    }

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public int getNumOfMines() {
        return numOfMines;
    }

    public List<Cell> getBoard() {
        return board;
    }
}