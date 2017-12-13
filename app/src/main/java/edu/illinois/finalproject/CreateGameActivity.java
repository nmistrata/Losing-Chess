package edu.illinois.finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class CreateGameActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_game_menu);

        final RadioButton blackPawnRadioButton = (RadioButton) findViewById(R.id.blackPawnRadioButton);
        final RadioButton whitePawnRadioButton = (RadioButton) findViewById(R.id.whitePawnRadioButton);
        final EditText lobbyNameEditText = (EditText) findViewById(R.id.lobbyNameEditText);
        final Button createButton = (Button) findViewById(R.id.createButton);

        whitePawnRadioButton.setChecked(true);

        blackPawnRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whitePawnRadioButton.setChecked(false);
                blackPawnRadioButton.setChecked(true);
            }
        });

        whitePawnRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whitePawnRadioButton.setChecked(true);
                blackPawnRadioButton.setChecked(false);
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean playingWhite = whitePawnRadioButton.isChecked();
                String lobbyName = lobbyNameEditText.getText().toString();
                if (lobbyName.isEmpty()) {
                    lobbyName = "My Game";
                }

                final Context context = v.getContext();
                Intent playGameIntent = new Intent(context, PlayGameActivity.class);
                playGameIntent.putExtra(ChessGameController.LOBBY_NAME_KEY, lobbyName);
                playGameIntent.putExtra(ChessGameController.HOST_PLAYS_WHITE_KEY, playingWhite);
                playGameIntent.putExtra(ChessGameController.GAME_STARTED_KEY, false);
                context.startActivity(playGameIntent);
                finish();
            }
        });

    }
}
