package com.raulrh.practicaandroid.ui.minesweeper;

public class Cell {
    private boolean isVisited;
    private boolean isFlagged;
    private boolean isMine;
    private boolean isClicked;
    private int value;

    public Cell() {
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public void reset() {
        isVisited = false;
        isFlagged = false;
        isMine = false;
        value = 0;
    }

    public void flag(MinesweeperGame game) {
        if (isFlagged) {
            game.incrementMinesLeft(1);
        } else {
            game.incrementMinesLeft(-1);
        }

        isFlagged = !isFlagged;
    }
}