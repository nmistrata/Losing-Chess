package edu.illinois.finalproject;


import java.util.ArrayList;
import java.util.List;

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

    //The amount of layers of out of bounds squares to make, useful when computing possible moves
    private static final int PADDING =  2;
    private static final char OUT_OF_BOUNDS = '.';

    //includes out of bounds squares
    private int numberOfSquares = (BOARD_LENGTH + 2 *PADDING) * (BOARD_LENGTH + 2 * PADDING);
    private char[] board = new char[numberOfSquares];

    //useful for converting from coordinates on a chess board to the correct index in the 1d
    //representation of the board.
    private static final int[][] BOARD_MAP =
                    {{26, 27, 28, 29, 30, 31, 32, 33},
                    {38, 39, 40, 41, 42, 43, 44, 45},
                    {50, 51, 52, 53, 54, 55, 56, 57},
                    {62, 63, 64, 65, 66, 67, 68, 69},
                    {74, 75, 76, 77, 78, 79, 80, 81},
                    {86, 87, 88, 89, 90, 91, 92, 93},
                    {98, 99, 100, 101, 102, 103, 104,105},
                    {110, 111, 112, 113, 114, 115, 116, 117}};

    //outermost list indices refer to each square on the board
    //At each index, a list holds the movement rays for the piece at that square in no particular order
    //the innermost lists each represent a movement ray, the integers stored refer to spaces on the board
    //rays start at the moving piece and go in one direction till they go out of bounds
    public List<List<List<Integer>>> allMovementRays = new ArrayList<>();

    private static final char[] startingPosition =
            {OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS,
            OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS,
            OUT_OF_BOUNDS, OUT_OF_BOUNDS, BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN, BLACK_KING, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK, OUT_OF_BOUNDS, OUT_OF_BOUNDS,
            OUT_OF_BOUNDS, OUT_OF_BOUNDS, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, OUT_OF_BOUNDS, OUT_OF_BOUNDS,
            OUT_OF_BOUNDS, OUT_OF_BOUNDS, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, OUT_OF_BOUNDS, OUT_OF_BOUNDS,
            OUT_OF_BOUNDS, OUT_OF_BOUNDS, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, OUT_OF_BOUNDS, OUT_OF_BOUNDS,
            OUT_OF_BOUNDS, OUT_OF_BOUNDS, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, OUT_OF_BOUNDS, OUT_OF_BOUNDS,
            OUT_OF_BOUNDS, OUT_OF_BOUNDS, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, EMPTY_SQUARE, OUT_OF_BOUNDS, OUT_OF_BOUNDS,
            OUT_OF_BOUNDS, OUT_OF_BOUNDS, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, OUT_OF_BOUNDS, OUT_OF_BOUNDS,
            OUT_OF_BOUNDS, OUT_OF_BOUNDS, WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN, WHITE_KING, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK, OUT_OF_BOUNDS, OUT_OF_BOUNDS,
            OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS,
            OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS, OUT_OF_BOUNDS};

    public ChessBoard() {
        board = startingPosition.clone();
        createAllMovementRays();
    }

    public String getBoardAsString() {
        StringBuilder boardBuilder = new StringBuilder();
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                boardBuilder.append(board[BOARD_MAP[i][j]]);
            }
        }
        return boardBuilder.toString();
    }

    public char[][] getBoardAs2dArray() {
        char[][] returnBoard = new char[BOARD_LENGTH][BOARD_LENGTH];

        for (int i = 0; i < BOARD_LENGTH; i++) {
                System.arraycopy(board, BOARD_MAP[i][0], returnBoard[i], 0, BOARD_LENGTH);
        }

        return returnBoard;
    }

    public void setBoardAsString(String boardString) {
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                char curPiece = board[BOARD_MAP[i][j]];
                char newPiece = boardString.charAt((BOARD_LENGTH * i) + j);
                if (curPiece != newPiece) {
                    board[BOARD_MAP[i][j]] = newPiece;
                    reMakeMovementRays(BOARD_MAP[i][j]);
                }
            }
        }
    }

    public boolean makeMove(int startRow, int startColumn, int endRow, int endColumn) {
        if (startRow < 0 || startRow >= BOARD_LENGTH ||
                startColumn < 0 || startColumn >= BOARD_LENGTH ||
                endRow < 0 || endColumn >= BOARD_LENGTH ||
                endColumn < 0 || endColumn >= BOARD_LENGTH) {
            return false;
        }

        if(board[BOARD_MAP[startRow][startColumn]] == EMPTY_SQUARE) {
            return false;
        }

        List<List<Integer>> movementRays = allMovementRays.get(BOARD_MAP[startRow][startColumn]);

        for (List<Integer> movementRay : movementRays) {
            for (Integer square : movementRay) {
                boolean targetIsSameColor =
                        pieceColor(square) == pieceColor(BOARD_MAP[startRow][startColumn]);
                boolean moveIsValid = (board[square] != OUT_OF_BOUNDS) && !targetIsSameColor;

                //if the move is the target move and is valid, do it and exit the method.
                if (square == BOARD_MAP[endRow][endColumn] && moveIsValid) {
                    movePiece(startRow, startColumn, endRow, endColumn);
                    return true;
                }

                //if the square being checked is not empty, don't keep searching along the ray
                if (board[square] != EMPTY_SQUARE) {
                    break;
                }
            }
        }
        return false;
    }

    public void movePiece(int startStringIndex, int endStringIndex) {
        movePiece(startStringIndex / BOARD_LENGTH, startStringIndex % BOARD_LENGTH,
                endStringIndex / BOARD_LENGTH, endStringIndex % BOARD_LENGTH);
    }

    private void movePiece(int startRow, int startColumn, int endRow, int endColumn) {
        board[BOARD_MAP[endRow][endColumn]] = board[BOARD_MAP[startRow][startColumn]];
        reMakeMovementRays(BOARD_MAP[endRow][endColumn]);

        board[BOARD_MAP[startRow][startColumn]] = EMPTY_SQUARE;
        reMakeMovementRays(BOARD_MAP[startRow][startColumn]);
    }

    //-1 is black, 1 is white, 0 is empty.
    public int pieceColor(int row, int column) {
        return pieceColor(BOARD_MAP[row][column]);
    }
    public int pieceColor(int index) {
        Character piece = board[index];
        if (piece == EMPTY_SQUARE) {
            return 0;
        }
        return Character.isLowerCase(piece) ? 1 : -1;
    }

    public boolean isWhitePiece(int row, int column) {
        return pieceColor(row, column) == 1;
    }


    //EVERYTHING BELOW IS FOR THE CREATION OF MOVEMENT RAYS

    private static int UP_OFFSET = -(BOARD_LENGTH + (2 * PADDING));
    private static int LEFT_OFFSET = -1;
    private static int RIGHT_OFFSET = 1;
    private static int DOWN_OFFSET = BOARD_LENGTH + (2 * PADDING);
    private static int[] CARDINAL_DIRECTIONS = {UP_OFFSET, DOWN_OFFSET, LEFT_OFFSET, RIGHT_OFFSET};
    private static int[] DIAGONALS = {UP_OFFSET + RIGHT_OFFSET, UP_OFFSET + LEFT_OFFSET,
            DOWN_OFFSET + RIGHT_OFFSET, DOWN_OFFSET + LEFT_OFFSET};

    private void createAllMovementRays() {
        int numberOfSquares = ((2 * PADDING) + BOARD_LENGTH) * ((2 * PADDING) + BOARD_LENGTH);
        for (int i = 0; i < numberOfSquares; i++) {
            allMovementRays.add(new ArrayList<List<Integer>>());
            reMakeMovementRays(i);
        }
    }

    private void reMakeMovementRays(int startSquare) {
        List<List<Integer>> movementRays;
        switch (board[startSquare]) {
            case OUT_OF_BOUNDS:
            case EMPTY_SQUARE:
                movementRays = new ArrayList<>();
                break;
            case WHITE_PAWN:
                movementRays = createPawnMovementRays(startSquare, true);
                break;
            case BLACK_PAWN:
                movementRays = createPawnMovementRays(startSquare, false);
                break;
            case WHITE_KNIGHT:
            case BLACK_KNIGHT:
                movementRays = createKnightMovementRays(startSquare);
                break;
            case WHITE_BISHOP:
            case BLACK_BISHOP:
                movementRays = createBishopMovementRays(startSquare);
                break;
            case WHITE_ROOK:
            case BLACK_ROOK:
                movementRays = createRookMovementRays(startSquare);
                break;
            case WHITE_QUEEN:
            case BLACK_QUEEN:
                movementRays = createQueenMovementRays(startSquare);
                break;
            case WHITE_KING:
            case BLACK_KING:
                movementRays = createKingMovementRays(startSquare);
                break;
            default:
                movementRays = new ArrayList<>();
        }

        allMovementRays.set(startSquare, movementRays);
    }

    private List<List<Integer>> createRookMovementRays(int startSquare) {
        return createMovementRays(startSquare, CARDINAL_DIRECTIONS, true);
    }

    private List<List<Integer>> createBishopMovementRays(int startSquare) {
        return createMovementRays(startSquare, DIAGONALS, true);
    }

    private List<List<Integer>> createQueenMovementRays(int startSquare) {
        ArrayList<List<Integer>> movementRays = new ArrayList<>();
        movementRays.addAll(createBishopMovementRays(startSquare));
        movementRays.addAll(createRookMovementRays(startSquare));

        return movementRays;
    }

    private List<List<Integer>> createKingMovementRays(int startSquare) {
        ArrayList<List<Integer>> movementRays = new ArrayList<>();
        movementRays.addAll(createMovementRays(startSquare, CARDINAL_DIRECTIONS, false));
        movementRays.addAll(createMovementRays(startSquare, DIAGONALS, false));

        return movementRays;
    }

    private List<List<Integer>> createPawnMovementRays(int startSquare, boolean isWhite) {
        int movementDirection;
        int startRow;

        if (isWhite) {
            movementDirection = UP_OFFSET;
            startRow = 6;
        } else {
            movementDirection = DOWN_OFFSET;
            startRow = 1;
        }

        int[] offsets;
        //checks if the pawn hasn't moved yet
        if (startSquare >= BOARD_MAP[startRow][0] &&
                startSquare <= BOARD_MAP[startRow][BOARD_LENGTH - 1]) {
            offsets = new int[]{movementDirection, 2 * movementDirection};
        } else {
            offsets = new int[]{movementDirection};
        }

        return createMovementRays(startSquare, offsets, false);
    }

    private  List<List<Integer>> createKnightMovementRays(int startSquare) {
        List<Integer> offsetsList = new ArrayList<>();

        for (int firstDirection : CARDINAL_DIRECTIONS) {
            for (int secondDirection : CARDINAL_DIRECTIONS) {
                //check if the directions are orthogonal
                if (Math.abs(firstDirection) != Math.abs(secondDirection)) {
                    offsetsList.add(2 * firstDirection + secondDirection);
                }
            }
        }

        //unboxing the Integers into ints and putting in an array
        int[] offsets = new int[offsetsList.size()];
        for (int i = 0; i < offsets.length; i++) {
            offsets[i] = offsetsList.get(i);
        }

        return createMovementRays(startSquare, offsets, false);
    }


    private List<List<Integer>> createMovementRays(int startSquare, int[] directionOffsets,
                                                   boolean moveUntilOutOfBounds) {
        ArrayList<List<Integer>> movementRays = new ArrayList<>();

        //makes 1 ray for every direction.
        for (int offset : directionOffsets) {
            ArrayList<Integer> movementRay = new ArrayList<>();

            int curSquare = startSquare;
            //will only run once if moveUntilOutOfBounds is false, else will run till out of bounds.
            do {
                curSquare += offset;
                movementRay.add(curSquare);
            } while (board[curSquare] != OUT_OF_BOUNDS && moveUntilOutOfBounds);

            movementRays.add(movementRay);
        }

        return movementRays;
    }

}
