package edu.illinois.finalproject;

import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static final String LOBBY_LIST_KEY = "lobbyList";
    public static final String LOBBY_NAME_KEY = "lobbyName";
    public static final String WHITE_TO_MOVE_KEY = "whiteToMove";
    public static final String GAME_STARTED_KEY = "gameStarted";
    public static final String BOARD_DATA_KEY = "boardData";
    public static final String HOST_PLAYS_WHITE_KEY = "hostPlaysWhite";
    public static final String ID_KEY = "id";
    public static final String GAME_CONNECTED_KEY = "gameDisconnected";

    //Board squares a found using charAt((BOARD_LENGTH * ROW_INDEX) + COLUMN_INDEX)
    private ChessBoard board;

    private String id;
    private ChessGameDisplayer displayer;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference gameRef;
    private boolean myTurn = true;
    private boolean playingWhite;
    private boolean gameStarted;
    private boolean isHost;
    private Square curSelectedSquare = null;

    private Map<DatabaseReference, ValueEventListener> firebaseListeners = new HashMap<>();

    private Square[][] squares;


    //used for creating games
    public ChessGameController(ChessGameDisplayer displayer, String lobbyName, boolean hostPlaysWhite) {
        isHost = true;
        this.displayer = displayer;
        board = new ChessBoard();
        gameStarted = false;
        squares = displayer.getBoardDisplay();
        setUpSquareClickListeners();

        id = database.getReference().push().getKey();
        gameRef = database.getReference().child(id);
        DatabaseReference lobbyRef = database.getReference(LOBBY_LIST_KEY);

        GameLobby lobby = new GameLobby(lobbyName, hostPlaysWhite, id);
        lobbyRef.child(id).setValue(lobby);

        pushBoardToFirebase();

        gameRef.child(WHITE_TO_MOVE_KEY).setValue(true);
        gameRef.child(GAME_STARTED_KEY).setValue(false);
        gameRef.child(HOST_PLAYS_WHITE_KEY).setValue(hostPlaysWhite);
        setupDatabaseListeners();
    }

    //Used for joining games
    public ChessGameController(String id, ChessGameDisplayer displayer) {
        this.id = id;
        isHost = false;
        this.displayer = displayer;
        board = new ChessBoard();
        gameStarted = false;
        squares = displayer.getBoardDisplay();
        setUpSquareClickListeners();

        DatabaseReference lobbyRef = database.getReference().child(LOBBY_LIST_KEY).child(id);
        lobbyRef.setValue(null);

        gameRef = database.getReference(id);
        gameRef.child(GAME_STARTED_KEY).setValue(true);

        setupDatabaseListeners();
    }

    private void pushBoardToFirebase() {
        gameRef.child(BOARD_DATA_KEY).setValue(board.getBoardAsString());
    }



    private void setupDatabaseListeners() {
        ValueEventListener boardDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                board.setBoardAsString(dataSnapshot.getValue(String.class));
                displayer.renderBoard(board);

                checkForGameEnd();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        firebaseListeners.put(gameRef.child(BOARD_DATA_KEY), boardDataListener);
        gameRef.child(BOARD_DATA_KEY).addValueEventListener(boardDataListener);

        gameRef.child(HOST_PLAYS_WHITE_KEY).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                playingWhite = (dataSnapshot.getValue(Boolean.class) == isHost);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ValueEventListener whiteToMoveListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean whiteToMove = dataSnapshot.getValue(Boolean.class);
                displayer.setWhiteToMove(whiteToMove);
                //myTurn = (whiteToMove == playingWhite);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        firebaseListeners.put(gameRef.child(WHITE_TO_MOVE_KEY), whiteToMoveListener);
        gameRef.child(WHITE_TO_MOVE_KEY).addValueEventListener(whiteToMoveListener);

        ValueEventListener gameStartedListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gameStarted = dataSnapshot.getValue(Boolean.class);
                if (gameStarted) {
                    displayer.startGame();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        firebaseListeners.put(gameRef.child(GAME_STARTED_KEY), gameStartedListener);
        gameRef.child(GAME_STARTED_KEY).addValueEventListener(gameStartedListener);
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
                            !myTurn || !gameStarted) {
                            return;
                        }

                        if (curSelectedSquare == null) {
                            if((board.pieceColor(targetSquare.getRow(), targetSquare.getColumn()) == 1)
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
        gameRef.child(WHITE_TO_MOVE_KEY).setValue(!playingWhite);
        pushBoardToFirebase();
        displayer.renderBoard(board);
    }

    private void checkForGameEnd() {
        boolean whiteToMove = (playingWhite == myTurn);
        //if current player  has  no moves due to no pieces or stalemate
        if (!board.isMoveAvailable(whiteToMove)) {
            boolean whiteWon = board.whiteHasLessPieces();
            myTurn = false;
            displayer.endGame(whiteWon);
        }

    }

    private void removeListeners(){
        for (DatabaseReference reference : firebaseListeners.keySet()) {
            reference.removeEventListener(firebaseListeners.get(reference));
        }
    }

    public void exitGame () {
        if (gameStarted) {
            gameRef.child(GAME_STARTED_KEY).setValue(false);
        } else {
            removeListeners();
            database.getReference().child(LOBBY_LIST_KEY).child(id).setValue(null);
            gameRef.setValue(null);
        }
    }

}
