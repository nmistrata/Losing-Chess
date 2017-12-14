package edu.illinois.finalproject;


import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
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
    public static  final  String WHITE_PIECES = "" + WHITE_PAWN + WHITE_KNIGHT + WHITE_BISHOP +
            WHITE_ROOK + WHITE_QUEEN + WHITE_KING;

    public static final char BLACK_PAWN = 'P';
    public static final char BLACK_KNIGHT = 'N';
    public static final char BLACK_BISHOP = 'B';
    public static final char BLACK_ROOK = 'R';
    public static final char BLACK_QUEEN = 'Q';
    public static final char BLACK_KING = 'K';
    public static final String BLACK_PIECES = ""  + BLACK_PAWN + BLACK_KNIGHT + BLACK_BISHOP +
            BLACK_ROOK + BLACK_QUEEN + BLACK_KING;

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
    //At each index, a list holds the movement rays for the piece at that square
    //the innermost lists each represent a movement ray, the integers stored refer to spaces on the board
    //rays start at a square adjacent to the moving piece and go in one direction
    private List<List<List<Integer>>> allMovementRays = new ArrayList<>();

    //used to keep  track of available attacks to minimize calls to isAttackAvailable()
    private boolean whiteAttackAvailable = false;
    private boolean blackAttackAvailable = false;

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

    /**
     * @return board representated as a string, starting at top left, going across a row and then down to next row
     */
    public String getBoardAsString() {
        StringBuilder boardBuilder = new StringBuilder();
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                boardBuilder.append(board[BOARD_MAP[i][j]]);
            }
        }
        return boardBuilder.toString();
    }

    /**
     * @return board represented as a 2d array, top left is [0][0], board[rowIndex][column index]
     */
    public char[][] getBoardAs2dArray() {
        char[][] returnBoard = new char[BOARD_LENGTH][BOARD_LENGTH];

        for (int i = 0; i < BOARD_LENGTH; i++) {
                System.arraycopy(board, BOARD_MAP[i][0], returnBoard[i], 0, BOARD_LENGTH);
        }

        return returnBoard;
    }

    /**
     * used to load a string representation of a board into this board object.
     *
     * @param boardString A string representation of a board to load
     */
    public void setBoardAsString(String boardString) {
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                char curPiece = board[BOARD_MAP[i][j]];
                char newPiece = boardString.charAt((BOARD_LENGTH * i) + j);
                if (curPiece != newPiece) {
                    board[BOARD_MAP[i][j]] = newPiece;
                    reMakeMovementRays(BOARD_MAP[i][j]);
                    updateAttackAvailableTrackers();
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

        int startSquare = BOARD_MAP[startRow][startColumn];
        int endSquare = BOARD_MAP[endRow][endColumn];

        if(board[startSquare] == EMPTY_SQUARE) {
            return false;
        }

        //Will return false if there is an attack to be made and the attempted move is not an attack
        if(pieceColor(board[startSquare]) == 1) {
            if (whiteAttackAvailable && (board[endSquare] == EMPTY_SQUARE)) {
                return false;
            }
        } else {
            if (blackAttackAvailable && board[endSquare] == EMPTY_SQUARE) {
                return false;
            }
        }

        if (board[startSquare] == BLACK_PAWN || board[startSquare] == WHITE_PAWN) {
            return makeMove(startSquare, endSquare, true);
        } else {
            return makeMove(startSquare, endSquare, false);
        }
    }

    private boolean makeMove(int startSquare, int endSquare, boolean pieceIsPawn) {
        List<List<Integer>> pieceMovementRays = allMovementRays.get(startSquare);
        for (int i = 0; i < pieceMovementRays.size(); i++) {
            //walks along a movement ray looking  for the target move or a blocking piece/out of bounds
            for (Integer curSquare : pieceMovementRays.get(i)) {

                boolean moveIsValid;
                //special case: pawns do not attack as they move, first ray  will always be non-attack ray
                if (pieceIsPawn) {
                    //if (checking non attack ray) else (checking an attack ray)
                    if (i == 0) {
                        moveIsValid = board[curSquare] == EMPTY_SQUARE;
                    } else {
                        moveIsValid = (pieceColor(board[curSquare]) == -pieceColor(board[startSquare]));
                    }
                } else {
                    boolean targetIsSameColor =
                            pieceColor(board[curSquare]) == pieceColor(board[startSquare]);
                    moveIsValid = (board[curSquare] != OUT_OF_BOUNDS) && !targetIsSameColor;
                }

                //if the move is the target move and is valid, do it and exit the method.
                if (curSquare == endSquare && moveIsValid) {
                    movePiece(startSquare, endSquare);
                    return true;
                }

                //if the curSquare being checked is not empty, don't keep searching along the ray
                if (board[curSquare] != EMPTY_SQUARE) {
                    break;
                }
            }
        }
        return false;
    }

    //moves a piece to a new square and makes the starting square empty
    private void movePiece(int startSquare, int endSquare) {
        board[endSquare] = board[startSquare];

        //white pawn promotion
        if (endSquare <= BOARD_MAP[0][BOARD_LENGTH - 1] && endSquare >= BOARD_MAP[0][0] &&
                board[endSquare] == WHITE_PAWN) {
            board[endSquare] = WHITE_QUEEN;
        }
        //black pawn promotion
        if (endSquare <= BOARD_MAP[BOARD_LENGTH - 1][BOARD_LENGTH -  1] && endSquare >= BOARD_MAP[6][0] &&
                board[endSquare] == BLACK_PAWN) {
            board[endSquare] = BLACK_QUEEN;
        }
        reMakeMovementRays(endSquare);

        board[startSquare] = EMPTY_SQUARE;
        reMakeMovementRays(startSquare);

        updateAttackAvailableTrackers();
    }

    private void updateAttackAvailableTrackers() {
        whiteAttackAvailable = isAttackAvailable(true);
        blackAttackAvailable = isAttackAvailable(false);
    }

    //CheckForWhiteAttack is true --> check for white attacks, else --> check for black attacks
    private boolean isAttackAvailable(boolean checkForWhiteAttack) {
        int colorToCheck;
        if (checkForWhiteAttack) {
            colorToCheck = 1;
        } else {
            colorToCheck = -1;
        }

        for (int square = 0; square < board.length; square++) {
            //only go through a squares movement rays if it contains a piece ofthe color being checked.
            if (pieceColor(board[square]) == colorToCheck) {

                int startRay = 0;
                //special case for pawns because  they do not attack and move the same way
                if (board[square] == BLACK_PAWN || board[square] == WHITE_PAWN) {
                    //skip over the forward movement ray to only check attacks
                    startRay = 1;
                }

                List<List<Integer>> squareMovementRays = allMovementRays.get(square);
                for (int i = startRay; i < squareMovementRays.size(); i++) {
                    for (int squareOnRay : squareMovementRays.get(i)) {

                        //if a piece of the other color is found, than an attack is possible
                        if (pieceColor(board[squareOnRay]) == -colorToCheck) {
                            return true;
                        }

                        //only continue checking ray if the currently checked square is empty
                        if (board[squareOnRay] != EMPTY_SQUARE) {
                            break;
                        }
                    }
                }
            }
        }

        //no attack was found
        return false;
    }

    /**
     * used to determine the color  of a piecec ona square if there is a piece there
     *
     * @param row the row of the piecec to checck, top row is 0
     * @param column the column of piecec to check, left column is 0
     * @return -1 is piecec is black, 1 if piece is white, 0 is square is empty
     */
    public int pieceColor(int row, int column) {
        return pieceColor(board[BOARD_MAP[row][column]]);
    }
    private int pieceColor(char piece) {
        if (piece == EMPTY_SQUARE || piece == OUT_OF_BOUNDS) {
            return 0;
        }
        return Character.isLowerCase(piece) ? 1 : -1;
    }

    public boolean whiteHasLessPieces() {
        int whitePieces = 0;
        int blackPieces = 0;
        for (char piece : board) {
            if (WHITE_PIECES.contains("" + piece)) {
                whitePieces++;
            } else if (BLACK_PIECES.contains("" + piece)) {
                blackPieces++;
            }
        }
        return (whitePieces < blackPieces);

    }

    public boolean isMoveAvailable(boolean checkForWhiteMove) {
        int colorToCheck;
        if (checkForWhiteMove) {
            colorToCheck = 1;
        } else {
            colorToCheck = -1;
        }

        for (int square = 0; square < board.length; square++) {
            //only go through a squares movement rays if it contains a piece of the color being checked.
            if (pieceColor(board[square]) == colorToCheck) {
                //if the piece is a pawn only check the first square of the first movement ray
                //else check the first square  of each movement ray
                if (board[square] == BLACK_PAWN || board[square] == WHITE_PAWN) {
                    if (board[allMovementRays.get(square).get(0).get(0)] == EMPTY_SQUARE) {
                        return true;
                    }
                } else {
                    for (List<Integer> movementRay : allMovementRays.get(square)) {
                        if (board[movementRay.get(0)] == EMPTY_SQUARE) {
                            return true;
                        }
                    }
                }
            }
        }

        //no non-attack was found, checck for attacks
        Log.d("Board", "isMoveAvailable: no move, checking attacks");
        return isAttackAvailable(checkForWhiteMove);
    }

    //EVERYTHING BELOW IS FOR THE CREATION OF MOVEMENT RAYS

    private static int UP_OFFSET = -(BOARD_LENGTH + (2 * PADDING));
    private static int LEFT_OFFSET = -1;
    private static int RIGHT_OFFSET = 1;
    private static int DOWN_OFFSET = BOARD_LENGTH + (2 * PADDING);
    private static int[] CARDINAL_DIRECTIONS = {UP_OFFSET, DOWN_OFFSET, LEFT_OFFSET, RIGHT_OFFSET};
    private static int[] DIAGONALS = {UP_OFFSET + RIGHT_OFFSET, UP_OFFSET + LEFT_OFFSET,
            DOWN_OFFSET + RIGHT_OFFSET, DOWN_OFFSET + LEFT_OFFSET};

    //used for the instantiation of the movement rays
    private void createAllMovementRays() {
        int numberOfSquares = ((2 * PADDING) + BOARD_LENGTH) * ((2 * PADDING) + BOARD_LENGTH);
        for (int i = 0; i < numberOfSquares; i++) {
            allMovementRays.add(new ArrayList<List<Integer>>());
            reMakeMovementRays(i);
        }
    }

    //remakes the movement rays for 1 square, useful when the piece changes on that square
    private void reMakeMovementRays(int square) {
        List<List<Integer>> movementRays;
        switch (board[square]) {
            case OUT_OF_BOUNDS:
            case EMPTY_SQUARE:
                movementRays = new ArrayList<>();
                break;
            case WHITE_PAWN:
                movementRays = createPawnMovementRays(square, true);
                break;
            case BLACK_PAWN:
                movementRays = createPawnMovementRays(square, false);
                break;
            case WHITE_KNIGHT:
            case BLACK_KNIGHT:
                movementRays = createKnightMovementRays(square);
                break;
            case WHITE_BISHOP:
            case BLACK_BISHOP:
                movementRays = createBishopMovementRays(square);
                break;
            case WHITE_ROOK:
            case BLACK_ROOK:
                movementRays = createRookMovementRays(square);
                break;
            case WHITE_QUEEN:
            case BLACK_QUEEN:
                movementRays = createQueenMovementRays(square);
                break;
            case WHITE_KING:
            case BLACK_KING:
                movementRays = createKingMovementRays(square);
                break;
            default:
                movementRays = new ArrayList<>();
        }

        allMovementRays.set(square, movementRays);

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

    //The list of pawn movement rays will always start with a ray representing a forward move
    //followed by 2 rays representing the 2 possible diagonal attacks
    private List<List<Integer>> createPawnMovementRays(int startSquare, boolean isWhite) {
        int movementDirection;
        int homeRow;
        ArrayList<List<Integer>> movementRays = new ArrayList<>();

        if (isWhite) {
            movementDirection = UP_OFFSET;
            homeRow = 6;
        } else {
            movementDirection = DOWN_OFFSET;
            homeRow = 1;
        }

        List<Integer> forwardMovenentRay = new ArrayList<>();

        forwardMovenentRay.add(startSquare + movementDirection);
        if (startSquare >= BOARD_MAP[homeRow][0] &&
                startSquare <= BOARD_MAP[homeRow][BOARD_LENGTH - 1]) {
            forwardMovenentRay.add(startSquare + 2* movementDirection);
        }
        movementRays.add(forwardMovenentRay);

        int[] attackOffsets = {movementDirection + LEFT_OFFSET, movementDirection + RIGHT_OFFSET};
        movementRays.addAll(createMovementRays(startSquare, attackOffsets, false));

        return   movementRays;
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

    //creates a list of movement rays in the directions specified by directionOffsets
    //if moveUntilOutOfBounds is false the movement rays will only contain a single square
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
