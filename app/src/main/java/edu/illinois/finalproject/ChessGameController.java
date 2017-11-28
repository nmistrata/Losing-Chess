package edu.illinois.finalproject;

import android.view.View;

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
    public static final String BOARD_DATA_KEY = "boardData";

    //Board squares a found using charAt((BOARD_LENGTH * ROW_INDEX) + COLUMN_INDEX)
    private ChessBoard board;

    private String lobbyName;
    private int id;
    private ChessGameDisplayer displayer;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference gameRef;
    private boolean myTurn;
    private boolean playingWhite;
    private Square curSelectedSquare = null;

    private Square[][] squares;


    //used for creating games
    public ChessGameController(int id, ChessGameDisplayer displayer, String lobbyName) {
        this.lobbyName = lobbyName;
        this.id = id;
        playingWhite = true;
        this.displayer = displayer;
        board = new ChessBoard();
        squares = displayer.getBoardDisplay();
        setUpSquareClickListeners();

        gameRef = database.getReference(""+ id);
        pushBoardToFirebase();
        gameRef.child(LOBBY_NAME_KEY).setValue(lobbyName);
        gameRef.child(WHITE_TO_MOVE_KEY).setValue(true);
        gameRef.child(GAME_STARTED_KEY).setValue(false);
        setupDatabaseListeners();
    }

    //Used for joining games
    public ChessGameController(int id, ChessGameDisplayer displayer) {
        this.id = id;
        playingWhite = false;
        this.displayer = displayer;
        board = new ChessBoard();
        squares = displayer.getBoardDisplay();
        setUpSquareClickListeners();

        gameRef = database.getReference("" + id);
        gameRef.child(GAME_STARTED_KEY).setValue(true);
        setupDatabaseListeners();
    }

    private void updateDisplayer() {
        displayer.renderBoard(board);
    }

    private void pushBoardToFirebase() {
        gameRef.child(BOARD_DATA_KEY).setValue(board.getBoardAsString());
    }

    private void setupDatabaseListeners() {
        gameRef.child(BOARD_DATA_KEY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                board.setBoardAsString(dataSnapshot.getValue(String.class));
                updateDisplayer();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        })
        ;

        gameRef.child(WHITE_TO_MOVE_KEY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean whiteToMove = dataSnapshot.getValue(Boolean.class);
                myTurn = (whiteToMove == playingWhite);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void setUpSquareClickListeners() {
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                final Square targetSquare = squares[i][j];
                targetSquare.getImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if ((curSelectedSquare == null &&
                            (board.getBoardAs2dArray()[targetSquare.getRow()][targetSquare.getColumn()])
                                    == EMPTY_SQUARE) ||
                            !myTurn) {
                            return;
                        }

                        if (curSelectedSquare == null) {
                            if(board.isWhitePiece(targetSquare.getRow(), targetSquare.getColumn())
                                    == playingWhite) {
                                targetSquare.highlight();
                                curSelectedSquare = targetSquare;
                            }
                        } else {
                            //if the move succeeds, update displayer and end my turn.
                            if (board.makeMove(curSelectedSquare.getRow(), curSelectedSquare.getColumn(),
                                         targetSquare.getRow(), targetSquare.getColumn())) {
                                endTurn();
                            }
                            curSelectedSquare.unhighlight();
                            curSelectedSquare = null;
                        }
                    }
                });
            }
        }
    }

    private void endTurn(){
        pushBoardToFirebase();
        updateDisplayer();
        gameRef.child(WHITE_TO_MOVE_KEY).setValue(!playingWhite);
    }

}
