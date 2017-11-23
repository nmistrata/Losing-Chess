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

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        ChessGameController wrapper =new ChessGameController(null, "My game", 23);
//        DatabaseReference gameWrapper = database.getReference("1");
//        gameWrapper.setValue(wrapper);

        final GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        final Button joinButton = (Button) findViewById(R.id.joinGameButton);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChessGameDisplayer displayer = new ChessGameDisplayer(gridLayout);
                ChessGameController controller = new ChessGameController(1, displayer);
            }
        });

        final Button createButton = (Button) findViewById(R.id.createGameButton);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChessGameDisplayer displayer = new ChessGameDisplayer(gridLayout);
                ChessGameController controller = new ChessGameController(1, displayer, "my game");
            }
        });
    }
}
