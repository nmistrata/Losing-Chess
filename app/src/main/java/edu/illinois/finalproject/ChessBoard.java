package edu.illinois.finalproject;


public class ChessBoard {

    public static final int BOARD_LENGTH = 8;

    public static final char EMPTY_SQUARE = '-';

    public static final char WHITE_PAWN = 'p';
    public static final char WHITE_KNIGHT = 'n';
    public static final char WHITE_BISHOP = 'b';
    public static final char WHITE_ROOK = 'r';
    public static final char WHITE_QUEEN = 'q';
    public static final char WHITE_KING = 'k';

    public static final char BLACK_PAWN = 'P';
    public static final char BLACK_KNIGHT = 'N';
    public static final char BLACK_BISHOP = 'B';
    public static final char BLACK_ROOK = 'R';
    public static final char BLACK_QUEEN = 'Q';
    public static final char BLACK_KING = 'K';

    private static final int O =  2;
    //used when calculating possible moves but not when returning boards in getters
    private static final char OUT_OF_BOUNDS = '.';

    //in a visual representation squares are found using [indexOfRow][indexOfColumn]
    private char[][] board =
            new char[BOARD_LENGTH + O][BOARD_LENGTH + O];

    private static final char[][] startingPosition =
            {{OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS},
            {OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS},
            {OUT_OF_BOUNDS, OUT_OF_BOUNDS, BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN, BLACK_KING, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK, OUT_OF_BOUNDS, OUT_OF_BOUNDS},
            {OUT_OF_BOUNDS, OUT_OF_BOUNDS, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, OUT_OF_BOUNDS, OUT_OF_BOUNDS},
            {OUT_OF_BOUNDS, OUT_OF_BOUNDS, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, OUT_OF_BOUNDS, OUT_OF_BOUNDS},
            {OUT_OF_BOUNDS, OUT_OF_BOUNDS, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, OUT_OF_BOUNDS, OUT_OF_BOUNDS},
            {OUT_OF_BOUNDS, OUT_OF_BOUNDS, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, OUT_OF_BOUNDS, OUT_OF_BOUNDS},
            {OUT_OF_BOUNDS, OUT_OF_BOUNDS, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, OUT_OF_BOUNDS, OUT_OF_BOUNDS},
            {OUT_OF_BOUNDS, OUT_OF_BOUNDS, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, OUT_OF_BOUNDS, OUT_OF_BOUNDS},
            {OUT_OF_BOUNDS, OUT_OF_BOUNDS, WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN, WHITE_KING, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK, OUT_OF_BOUNDS, OUT_OF_BOUNDS},
            {OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS},
            {OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS}};

    public ChessBoard() {
        setupStartingPosition();
    }

    public String getBoardAsString() {
        StringBuilder boardBuilder = new StringBuilder();
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                boardBuilder.append(board[O + i][O + j]);
            }
        }
        return boardBuilder.toString();
    }

    public char[][] getBoard() {
        char[][] returnBoard = new char[BOARD_LENGTH][BOARD_LENGTH];

        for (int i = 0; i < BOARD_LENGTH; i++) {
                System.arraycopy(board[O + i], O, returnBoard[i], 0, BOARD_LENGTH);
        }

        return returnBoard;
    }

    public void setBoardAsString(String boardString) {
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                board[O + i][O + j] =
                        boardString.charAt((BOARD_LENGTH * i) + j);
            }
        }
    }

    public void setupStartingPosition() {
        board = startingPosition.clone();
    }

    public void movePiece(int startStringIndex, int endStringIndex) {
        movePiece(startStringIndex / BOARD_LENGTH, startStringIndex % BOARD_LENGTH,
                endStringIndex / BOARD_LENGTH, endStringIndex % BOARD_LENGTH);
    }

    public void movePiece(int startRow, int startColumn, int endRow, int endColumn) {
        board[endRow + O][endColumn + O] = board[startRow + O][startColumn + O];

        board[startRow + O][startColumn + O] = EMPTY_SQUARE;
    }
}
