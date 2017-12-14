package edu.illinois.finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        final SoundPlayer soundPlayer = new SoundPlayer(this);

        Button createGameButton = (Button) findViewById(R.id.createGameButton);
        Button joinGameButton = (Button) findViewById(R.id.joinGameButton);
        Button rulesButton = (Button) findViewById(R.id.rulesButton);

        createGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPlayer.playButtonPressSound();
                final Context context = v.getContext();
                Intent createGameIntent = new Intent(context, CreateGameActivity.class);
                context.startActivity(createGameIntent);
            }
        });

        joinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPlayer.playButtonPressSound();
                final Context context = v.getContext();
                Intent lobbyListIntent = new Intent(context, LobbyListActivity.class);
                context.startActivity(lobbyListIntent);
            }
        });

        rulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPlayer.playButtonPressSound();
                final Context context = v.getContext();
                Intent rulesIntent = new Intent(context, RulesActivity.class);
                context.startActivity(rulesIntent);
            }
        });
    }
}
