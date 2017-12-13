package edu.illinois.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;

public class PlayGameActivity extends AppCompatActivity{

    FrameLayout exitConfirmationFrame;
    ChessGameController controller;

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

    @Override
    public void onBackPressed() {

        //lot of info from http://android.pcsalt.com/create-alertdialog-with-custom-layout-using-xml-layout/

        LayoutInflater inflater = getLayoutInflater();
        View exitConfirmationAlertLayout = inflater.inflate(R.layout.exit_confirmation_menu, null);
        Button quitButton = (Button) exitConfirmationAlertLayout.findViewById(R.id.yesButton);
        Button cancelButton = (Button) exitConfirmationAlertLayout.findViewById(R.id.noButton);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(exitConfirmationAlertLayout);
        final AlertDialog alertDialog = alert.create();

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.exitGame();
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
