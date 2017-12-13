package edu.illinois.finalproject;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;


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

    public static final String WHITE_TO_MOVE = "White to move";
    public static final String BLACK_TO_MOVE = "Black to move";

    private char[][] curDisplayedGameState;
    Square[][] boardDisplay;
    GridLayout gridLayout;
    TextView whoseTurnIsit;
    TextView gameHasStarted;

    public ChessGameDisplayer(GridLayout gridLayout, TextView whoseTurnIsitTextView,
                              TextView hasGameStartedTextView, boolean flipView) {
        boardDisplay = new Square[BOARD_LENGTH][BOARD_LENGTH];
        curDisplayedGameState = new char[BOARD_LENGTH][BOARD_LENGTH];

        this.whoseTurnIsit = whoseTurnIsitTextView;
        whoseTurnIsitTextView.setText(WHITE_TO_MOVE);

        this.gameHasStarted = hasGameStartedTextView;
        hasGameStartedTextView.setText("Waiting for a player to join");

        this.gridLayout= gridLayout;
        for(int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                ImageView curSquareImage = new ImageView(gridLayout.getContext());
                setUpSquareImage(curSquareImage);
                gridLayout.addView(curSquareImage);

                //used to flip the board vertically for playing black
                int row = flipView ? BOARD_LENGTH - 1 - i : i;

                boardDisplay[row][j] = new Square(curSquareImage, row, j);
                curDisplayedGameState[i][j] = EMPTY_SQUARE;
            }
        }

    }

    private void setUpSquareImage(ImageView curSquare) {
        GridLayout.LayoutParams squareParams = new GridLayout.LayoutParams();

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) gridLayout.getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        FrameLayout.LayoutParams gridLayoutParams = (FrameLayout.LayoutParams) gridLayout.getLayoutParams();
        ConstraintLayout.LayoutParams frameLayoutParams =
                (ConstraintLayout.LayoutParams)((ViewGroup)gridLayout.getParent()).getLayoutParams();

        int totalMargins = gridLayoutParams.leftMargin + gridLayoutParams.rightMargin +
                frameLayoutParams.leftMargin + frameLayoutParams.rightMargin;

        int boardWidth =
                metrics.widthPixels - totalMargins;
        int squareSideLength = boardWidth / 8;

        squareParams.height = squareSideLength;
        squareParams.width = squareSideLength;
        curSquare.setLayoutParams(squareParams);
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

    public void setWhiteToMove(boolean whiteToMove) {
        if (whiteToMove) {
            whoseTurnIsit.setText(WHITE_TO_MOVE);
        } else {
            whoseTurnIsit.setText(BLACK_TO_MOVE);
        }
    }

    public void startGame() {
        gameHasStarted.setText("Game has started");
    }
    public void endGame(boolean whiteHasWon) {
        gameHasStarted.setText("Game Over");
        whoseTurnIsit.setText((whiteHasWon ? "White" : "Black") + " has won!");
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
