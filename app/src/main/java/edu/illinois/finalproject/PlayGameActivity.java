package edu.illinois.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.TextView;

public class PlayGameActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_view);

        GridLayout gameGridLayout = (GridLayout) findViewById(R.id.gameGridLayout);
        TextView whoseTurnIsIt = (TextView) findViewById(R.id.whoseTurnIsItTextView);
        TextView hasGameStarted = (TextView) findViewById(R.id.hasGameStartedTextView);

        final Intent intent = getIntent();
        boolean gameWasJustCreated =
                !(intent.getBooleanExtra(ChessGameController.GAME_STARTED_KEY, true));

        Log.d("PlayGameActivity", "gameWasJustCreated:  " + gameWasJustCreated);
        String lobbyName = intent.getStringExtra(ChessGameController.LOBBY_NAME_KEY);
        boolean hostPlaysWhite =
                intent.getBooleanExtra(ChessGameController.HOST_PLAYS_WHITE_KEY, true);

        ChessGameDisplayer displayer;
        ChessGameController controller;

        if (gameWasJustCreated) {
            displayer = new ChessGameDisplayer(gameGridLayout, whoseTurnIsIt,
                    hasGameStarted, !hostPlaysWhite);
            controller = new ChessGameController(displayer, lobbyName, hostPlaysWhite);
        } else {
            displayer = new ChessGameDisplayer(gameGridLayout, whoseTurnIsIt,
                    hasGameStarted, hostPlaysWhite);

            String id = intent.getStringExtra(ChessGameController.ID_KEY);
            Log.d("PlayGameActivity", "id:  " + id);
            controller = new ChessGameController(id, displayer);
        }

    }
}
