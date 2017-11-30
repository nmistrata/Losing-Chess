package edu.illinois.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        final Button joinButton = (Button) findViewById(R.id.joinGameButton);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChessGameDisplayer displayer = new ChessGameDisplayer(gridLayout);
                ChessGameController controller = new ChessGameController("0", displayer);
            }
        });

        final Button createButton = (Button) findViewById(R.id.createGameButton);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChessGameDisplayer displayer = new ChessGameDisplayer(gridLayout);
                ChessGameController controller = new ChessGameController(displayer, "my game");
            }
        });
    }
}
