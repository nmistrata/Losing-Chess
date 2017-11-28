package edu.illinois.finalproject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;


public class Square {
    private static final int LIGHT_COLOR = Color.rgb(110, 80, 0);
    private static final int DARK_COLOR = Color.rgb(200, 180, 120);
    private static final int HIGHLIGHT_COLOR_FACTOR = Color.rgb(40, 40, 40);

    private int columnIndex;
    private int rowIndex;
    private int baseColor;


    private ImageView imageView;

    public Square(ImageView imageView, int rowIndex, int columnIndex) {
        this.columnIndex = columnIndex;
        this.rowIndex = rowIndex;
        this.imageView = imageView;
        baseColor = ((rowIndex + columnIndex) % 2 == 0) ? DARK_COLOR : LIGHT_COLOR;
        imageView.setBackgroundColor(baseColor);
    }

    public int getColumn() {
        return columnIndex;
    }

    public int getRow() {
        return rowIndex;
    }

    public ImageView getImageView() {
        return imageView;
    }

    //used when selecting a square
    public void highlight() {
        imageView.setBackgroundColor(baseColor + HIGHLIGHT_COLOR_FACTOR);
    }

    public void unhighlight() {
        imageView.setBackgroundColor(baseColor);
    }

    public void setImage(int drawableResourceId) {
        imageView.setImageResource(drawableResourceId);
    }
}
