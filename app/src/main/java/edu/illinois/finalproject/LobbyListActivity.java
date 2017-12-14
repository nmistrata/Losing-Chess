package edu.illinois.finalproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class LobbyListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby_list);

        RecyclerView lobbyList = (RecyclerView) findViewById(R.id.recyclerView);
        final GameLobbyAdapter gameLobbyAdapter = new GameLobbyAdapter(this);
        lobbyList.setAdapter(gameLobbyAdapter);
        lobbyList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        final FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference lobbyListRef = database.getReference(ChessGameController.LOBBY_LIST_KEY);
        lobbyListRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                GameLobby lobby = dataSnapshot.getValue(GameLobby.class);
                gameLobbyAdapter.addLobby(lobby);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                gameLobbyAdapter.removeLobby(dataSnapshot.getKey());

            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
