package edu.illinois.finalproject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;


public class SquareImageWrapper {
    private int columnIndex;
    private int rowIndex;
    private int indexInString;
    private int baseColor;
    private SquareImageWrapper wrapper = this;

    private ImageView imageView;

    public SquareImageWrapper(ImageView imageView, int rowIndex, int columnIndex) {
        this.columnIndex = columnIndex;
        this.rowIndex = rowIndex;
        this.indexInString = (ChessGameController.BOARD_LENGTH * rowIndex) + columnIndex;
        this.baseColor = ((ColorDrawable) imageView.getBackground()).getColor();
        this.imageView = imageView;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    //useful when using the string representation of a board as opposed to a 2d array
    public int getIndexInString() {
        return indexInString;
    }

    public ImageView getImageView() {
        return imageView;
    }

    //used when selecting a square
    public void highlight() {
        imageView.setBackgroundColor(baseColor + Color.rgb(30, 30, 30));
    }

    public void unhighlight() {
        imageView.setBackgroundColor(baseColor);
    }

    public void setImage(int drawableResourceId) {
        imageView.setImageResource(drawableResourceId);
    }
}
