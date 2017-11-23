package edu.illinois.finalproject;

import android.graphics.Color;
import android.widget.GridLayout;
import android.widget.ImageView;

/**
 * Created by nmist on 11/21/2017.
 */

public class ChessGameDisplayer {

    private static final int DEFAULT_SQUARE_LENGTH = 128;
    private static final char BOARD_LENGTH = ChessGameController.BOARD_LENGTH;

    private  static final char EMPTY_SQUARE = ChessGameController.EMPTY_SQUARE;

    private static final char WHITE_PAWN = ChessGameController.WHITE_PAWN;
    private static final char WHITE_KNIGHT = ChessGameController.WHITE_KNIGHT;
    private static final char WHITE_BISHOP = ChessGameController.WHITE_BISHOP;
    private static final char WHITE_ROOK = ChessGameController.WHITE_ROOK;
    private static final char WHITE_QUEEN = ChessGameController.WHITE_QUEEN;
    private static final char WHITE_KING = ChessGameController.WHITE_KING;

    public static final char BLACK_PAWN = ChessGameController.BLACK_PAWN;
    public static final char BLACK_KNIGHT = ChessGameController.BLACK_KNIGHT;
    public static final char BLACK_BISHOP = ChessGameController.BLACK_BISHOP;
    public static final char BLACK_ROOK = ChessGameController.BLACK_ROOK;
    public static final char BLACK_QUEEN = ChessGameController.BLACK_QUEEN;
    public static final char BLACK_KING = ChessGameController.BLACK_KING;

    //Board squares a found using charAt((BOARD_LENGTH * ROW_INDEX) + COLUMN_INDEX)
    private String curDisplayedGameState;
    ImageView[][] boardDisplay;

    public ChessGameDisplayer(GridLayout gridLayout) {
        boardDisplay = new ImageView[BOARD_LENGTH][BOARD_LENGTH];
        for(int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                ImageView curSquare = new ImageView(gridLayout.getContext());
                setUpSquare(curSquare);
                boardDisplay[i][j] = curSquare;
                gridLayout.addView(curSquare);

                if((i + j) % 2 == 0) {
                    curSquare.setBackgroundColor(Color.rgb(200, 180, 120));
                } else {
                    curSquare.setBackgroundColor(Color.rgb(110, 80, 0));
                }
            }
        }

        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                curDisplayedGameState += EMPTY_SQUARE;
            }
        }

    }

    private void setUpSquare(ImageView curSquare) {
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.height = DEFAULT_SQUARE_LENGTH;
        layoutParams.width = DEFAULT_SQUARE_LENGTH;
        curSquare.setLayoutParams(layoutParams);
    }

    public void renderBoard(String newGameState) {
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                int boardIndex = (BOARD_LENGTH * i) + j;
                if (curDisplayedGameState.charAt(boardIndex) != newGameState.charAt(boardIndex)) {

                    setSquareImage(boardDisplay[i][j], newGameState.charAt(boardIndex));
                }

            }
        }

        curDisplayedGameState = newGameState;
    }

    private void setSquareImage(ImageView square, int piece) {
        switch (piece) {
            case EMPTY_SQUARE:
                square.setImageResource(android.R.color.transparent);
                break;
            case WHITE_PAWN:
                square.setImageResource(R.drawable.white_pawn);
                break;
            case WHITE_KNIGHT:
                square.setImageResource(R.drawable.white_knight);
                break;
            case WHITE_BISHOP:
                square.setImageResource(R.drawable.white_bishop);
                break;
            case WHITE_ROOK:
                square.setImageResource(R.drawable.white_rook);
                break;
            case WHITE_QUEEN:
                square.setImageResource(R.drawable.white_queen);
                break;
            case WHITE_KING:
                square.setImageResource(R.drawable.white_king);
                break;
            case BLACK_PAWN:
                square.setImageResource(R.drawable.black_pawn);
                break;
            case BLACK_KNIGHT:
                square.setImageResource(R.drawable.black_knight);
                break;
            case BLACK_BISHOP:
                square.setImageResource(R.drawable.black_bishop);
                break;
            case BLACK_ROOK:
                square.setImageResource(R.drawable.black_rook);
                break;
            case BLACK_QUEEN:
                square.setImageResource(R.drawable.black_queen);
                break;
            case BLACK_KING:
                square.setImageResource(R.drawable.black_king);
                break;
            default:
                break;
        }
    }

}
