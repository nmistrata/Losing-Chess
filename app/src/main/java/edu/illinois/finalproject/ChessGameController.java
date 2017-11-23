package edu.illinois.finalproject;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChessGameController {

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

    public static final String LOBBY_NAME_KEY = "lobbyName";
    public static final String WHITE_TO_MOVE_KEY = "whiteToMove";
    public static final String GAME_STARTED_KEY = "gameStarted";
    public static final String BOARD_DATA_KEY = "booardData";

    //Board squares a found using charAt((BOARD_LENGTH * ROW_INDEX) + COLUMN_INDEX)
    String boardData;

    private String lobbyName;
    int id;
    ChessGameDisplayer displayer;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference gameRef;


    //used for creating games
    public ChessGameController(int id, ChessGameDisplayer displayer, String lobbyName) {
        this.lobbyName = lobbyName;
        this.id = id;
        this.displayer = displayer;
        boardData = "";
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                boardData += EMPTY_SQUARE;
            }
        }

        setupStartingPos();

        setupDatabase();
        setupBoardListener();
    }

    //Used for joining games
    public ChessGameController(int id, ChessGameDisplayer displayer) {
        this.id = id;
        this.displayer = displayer;
        boardData = "";
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                boardData += EMPTY_SQUARE;
            }
        }

        gameRef = database.getReference("" + id);
        setupBoardListener();
        gameRef.child(GAME_STARTED_KEY).setValue(true);
    }

    private void setupDatabase() {
        gameRef = database.getReference(""+ id);
        gameRef.child(LOBBY_NAME_KEY).setValue(lobbyName);
        gameRef.child(WHITE_TO_MOVE_KEY).setValue(true);
        gameRef.child(GAME_STARTED_KEY).setValue(false);
        gameRef.child(BOARD_DATA_KEY).setValue(boardData);
    }

    public void updateDisplayer() {
        displayer.renderBoard(boardData);
    }

    private void setupStartingPos(){
        StringBuilder boardBuilder = new StringBuilder(boardData);

        //sets up pawns  Board squares a found using (BOARD_LENGTH * ROW_INDEX) + COLUMN_INDEX
        for (int i = 0; i < BOARD_LENGTH; i++) {
            boardBuilder.setCharAt((BOARD_LENGTH * 1) + i, BLACK_PAWN);
            boardBuilder.setCharAt((BOARD_LENGTH * 6) + i, WHITE_PAWN);
        }

        //sets up back ranks
        boardBuilder.setCharAt((BOARD_LENGTH * 0) + 0, BLACK_ROOK);
        boardBuilder.setCharAt((BOARD_LENGTH * 0) + 1, BLACK_KNIGHT);
        boardBuilder.setCharAt((BOARD_LENGTH * 0) + 2, BLACK_BISHOP);
        boardBuilder.setCharAt((BOARD_LENGTH * 0) + 3, BLACK_QUEEN);
        boardBuilder.setCharAt((BOARD_LENGTH * 0) + 4, BLACK_KING);
        boardBuilder.setCharAt((BOARD_LENGTH * 0) + 5, BLACK_BISHOP);
        boardBuilder.setCharAt((BOARD_LENGTH * 0) + 6, BLACK_KNIGHT);
        boardBuilder.setCharAt((BOARD_LENGTH * 0) + 7, BLACK_ROOK);

        boardBuilder.setCharAt((BOARD_LENGTH * 7) + 0, WHITE_ROOK);
        boardBuilder.setCharAt((BOARD_LENGTH * 7) + 1, WHITE_KNIGHT);
        boardBuilder.setCharAt((BOARD_LENGTH * 7) + 2, WHITE_BISHOP);
        boardBuilder.setCharAt((BOARD_LENGTH * 7) + 3, WHITE_QUEEN);
        boardBuilder.setCharAt((BOARD_LENGTH * 7) + 4, WHITE_KING);
        boardBuilder.setCharAt((BOARD_LENGTH * 7) + 5, WHITE_BISHOP);
        boardBuilder.setCharAt((BOARD_LENGTH * 7) + 6, WHITE_KNIGHT);
        boardBuilder.setCharAt((BOARD_LENGTH * 7) + 7, WHITE_ROOK);

        boardData = boardBuilder.toString();
    }

    private void setupBoardListener() {
        gameRef.child(BOARD_DATA_KEY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boardData = dataSnapshot.getValue(String.class);
                updateDisplayer();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
