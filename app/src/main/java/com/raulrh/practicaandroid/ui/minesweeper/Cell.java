package com.raulrh.practicaandroid.ui.minesweeper;

public class Cell {
    public static final int EMPTY = 0;
    public static final int FLAGGED = -1;
    public static final int UNCHECKED = -2;
    public static final int MINE = -3;

    private int value;
    private boolean visited;

    public Cell() {
        this.value = UNCHECKED;
        this.visited = false;
    }

    public int getValue() {
        return value;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void flag() {
        if (!visited) {
            if (value == FLAGGED) {
                value = UNCHECKED;
            } else {
                value = FLAGGED;
            }
        }
    }

    public void setNumber(int number) {
        if (value == UNCHECKED) {
            this.value = number;
        }
    }
}