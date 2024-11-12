package com.raulrh.practicaandroid.ui.minesweeper;

import java.util.Random;

public class GameLogic {
    private final Cell[][] cells;
    private final int rows;
    private final int cols;
    private final int mines;

    public GameLogic(int rows, int cols, int mines) {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        cells = new Cell[rows][cols];
        initializeCells();
        placeMines();
        calculateAdjacentMines();
    }

    private void initializeCells() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    private void placeMines() {
        Random random = new Random();
        int placedMines = 0;

        while (placedMines < mines) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);
            if (!cells[row][col].isMine()) {
                cells[row][col].setMine(true);
                placedMines++;
            }
        }
    }

    private void calculateAdjacentMines() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!cells[i][j].isMine()) {
                    int count = countMinesAround(i, j);
                    cells[i][j].setAdjacentMines(count);
                }
            }
        }
    }

    private int countMinesAround(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && cells[newRow][newCol].isMine()) {
                    count++;
                }
            }
        }
        return count;
    }

    public Cell getCell(int row, int col) {
        return cells[row][col];
    }
}