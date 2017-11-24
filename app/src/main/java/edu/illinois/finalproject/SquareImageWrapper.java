package edu.illinois.finalproject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class SquareImageWrapper {
    private int columIndex;
    private int rowIndex;
    private int indexInString;
    private int baseColor;
    private SquareImageWrapper wrapper = this;

    private ImageView imageView;

    public SquareImageWrapper(ImageView imageView, int rowIndex, int columIndex) {
        this.columIndex = columIndex;
        this.rowIndex = rowIndex;
        this.indexInString = (ChessGameController.BOARD_LENGTH * rowIndex) +columIndex;
        this.baseColor = ((ColorDrawable) imageView.getBackground()).getColor();
        this.imageView = imageView;;
    }

    public int getColumIndex() {
        return columIndex;
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
