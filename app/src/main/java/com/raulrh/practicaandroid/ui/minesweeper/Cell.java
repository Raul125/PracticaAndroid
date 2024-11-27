package com.raulrh.practicaandroid.ui.minesweeper;

import android.widget.ImageView;

import com.raulrh.practicaandroid.R;

public class Cell {
    private final ImageView imageView;
    private final int rowPosition;
    private final int columnPosition;
    private boolean isVisited;
    private boolean isFlagged;
    private boolean isMine;
    private boolean isClicked;
    private int value;

    public Cell(ImageView view, int rowPosition, int columnPosition) {
        this.imageView = view;
        this.rowPosition = rowPosition;
        this.columnPosition = columnPosition;
    }

    public int getRowPosition() {
        return rowPosition;
    }

    public int getColumnPosition() {
        return columnPosition;
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

    public void flag() {
        isFlagged = !isFlagged;
        updateIcon();
    }

    public void updateIcon() {
        if (isFlagged()) {
            imageView.setImageResource(R.drawable.flag);
        } else if (isVisited()) {
            if (isMine()) {
                if (isClicked()) {
                    imageView.setImageResource(R.drawable.mine_clicked);
                } else {
                    imageView.setImageResource(R.drawable.mine);
                }
            } else {
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
}