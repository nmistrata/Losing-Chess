package edu.illinois.finalproject;

import android.graphics.Color;
import android.widget.GridLayout;
import android.widget.ImageView;


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

    private char[][] curDisplayedGameState;
    Square[][] boardDisplay;

    public ChessGameDisplayer(GridLayout gridLayout) {
        boardDisplay = new Square[BOARD_LENGTH][BOARD_LENGTH];
        curDisplayedGameState = new char[BOARD_LENGTH][BOARD_LENGTH];
        for(int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                ImageView curSquare = new ImageView(gridLayout.getContext());
                setUpSquare(curSquare);
                gridLayout.addView(curSquare);

                boardDisplay[i][j] = new Square(curSquare, i, j);
                curDisplayedGameState[i][j] = EMPTY_SQUARE;
            }
        }

    }

    private void setUpSquare(ImageView curSquare) {
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.height = DEFAULT_SQUARE_LENGTH;
        layoutParams.width = DEFAULT_SQUARE_LENGTH;
        curSquare.setLayoutParams(layoutParams);
    }

    public void renderBoard(ChessBoard newBoard) {
        char[][] newGameState = newBoard.getBoardAs2dArray();
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                if (curDisplayedGameState[i][j] != newGameState[i][j]) {
                    setSquareImage(boardDisplay[i][j], newGameState[i][j]);
                }

            }
        }

        curDisplayedGameState = newGameState.clone();
    }

    private void setSquareImage(Square square, int piece) {
        switch (piece) {
            case EMPTY_SQUARE:
                square.setImage(android.R.color.transparent);
                break;
            case WHITE_PAWN:
                square.setImage(R.drawable.white_pawn);
                break;
            case WHITE_KNIGHT:
                square.setImage(R.drawable.white_knight);
                break;
            case WHITE_BISHOP:
                square.setImage(R.drawable.white_bishop);
                break;
            case WHITE_ROOK:
                square.setImage(R.drawable.white_rook);
                break;
            case WHITE_QUEEN:
                square.setImage(R.drawable.white_queen);
                break;
            case WHITE_KING:
                square.setImage(R.drawable.white_king);
                break;
            case BLACK_PAWN:
                square.setImage(R.drawable.black_pawn);
                break;
            case BLACK_KNIGHT:
                square.setImage(R.drawable.black_knight);
                break;
            case BLACK_BISHOP:
                square.setImage(R.drawable.black_bishop);
                break;
            case BLACK_ROOK:
                square.setImage(R.drawable.black_rook);
                break;
            case BLACK_QUEEN:
                square.setImage(R.drawable.black_queen);
                break;
            case BLACK_KING:
                square.setImage(R.drawable.black_king);
                break;
            default:
                break;
        }
    }

    public Square[][] getBoardDisplay() {
        return boardDisplay;
    }
}
