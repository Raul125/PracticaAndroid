package com.raulrh.practicaandroid.ui.minesweeper;

import java.util.Random;

public class MinesweeperGame {

    private final int rows;
    private final int cols;
    private final int numOfMines;
    private final Cell[][] board;

    private int minesLeft;

    public MinesweeperGame(int rows, int cols, int numOfMines) {
        this.rows = rows;
        this.cols = cols;
        this.numOfMines = numOfMines;
        this.board = new Cell[rows][cols];
    }

    public void setup() {
        initializeBoard();
        generateMines();
        calculateMinesAroundCells();
        minesLeft = numOfMines;
    }

    public int getMinesLeft() {
        return minesLeft;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    private void initializeBoard() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                board[row][col] = new Cell();
            }
        }
    }

    private void generateMines() {
        Random random = new Random();
        int minesPlaced = 0;
        while (minesPlaced < numOfMines) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);
            Cell cell = board[row][col];
            if (cell.getValue() != Cell.MINE) {
                cell.setNumber(Cell.MINE);
                minesPlaced++;
            }
        }
    }

    private void calculateMinesAroundCells() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (board[row][col].getValue() == Cell.MINE)
                    continue;

                int minesAround = countMinesAround(row, col);
                board[row][col].setNumber(minesAround);
            }
        }
    }

    private int countMinesAround(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;
                if (isValidCell(newRow, newCol) && board[newRow][newCol].getValue() == Cell.MINE) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public Cell getCell(int row, int col) {
        if (!isValidCell(row, col)) {
            return null;
        }
        return board[row][col];
    }

    public boolean clickCell(int row, int col) {
        if (!isValidCell(row, col)) {
            return false;
        }

        Cell cell = board[row][col];
        if (cell.isVisited())
            return false;

        revealCell(row, col);
        return cell.getValue() == Cell.MINE;
    }

    private void revealCell(int row, int col) {
        if (!isValidCell(row, col)) {
            return;
        }

        Cell cell = board[row][col];
        if (cell.isVisited()) {
            return;
        }

        cell.setVisited(true);
        if (cell.getValue() == Cell.EMPTY) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    revealCell(row + i, col + j);
                }
            }
        }
    }

    public void flagCell(int row, int col) {
        if (!isValidCell(row, col)) {
            return;
        }

        Cell cell = board[row][col];
        if (cell.isVisited()) {
            return;
        }

        cell.flag();
        minesLeft--;
    }

    public boolean isGameWon() {
        int revealedCells = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Cell cell = board[row][col];
                if (cell.isVisited() && cell.getValue() != Cell.MINE) {
                    revealedCells++;
                }
            }
        }

        return revealedCells == (rows * cols - numOfMines);
    }

    public void endGame() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                board[row][col].setVisited(true);
            }
        }
    }
}