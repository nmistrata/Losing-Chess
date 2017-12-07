package edu.illinois.finalproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class GameLobbyAdapter extends RecyclerView.Adapter<GameLobbyAdapter.ViewHolder>{
    private List<GameLobby> lobbies = new ArrayList<>();

    public void setLobbies(List<GameLobby> lobbies) {
        this.lobbies = lobbies;
    }

    public void addLobby(GameLobby lobby ){
        lobbies.add(lobby);
        notifyDataSetChanged();
    }

    public void removeLobby(String lobbyId) {
        for (int i = 0; i  < lobbies.size(); i++) {
            if (lobbies.get(i).getId().equals(lobbyId)) {
                lobbies.remove(i);
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View singleLobby = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.game_lobby, parent, false);

        return new ViewHolder(singleLobby);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GameLobby lobby = lobbies.get(position);
        holder.lobbyName.setText(lobby.getLobbyName());
        if (lobby.isHostPlaysWhite()) {
            holder.pawnOnButton.setImageResource(R.drawable.black_pawn);
        } else {
            holder.pawnOnButton.setImageResource(R.drawable.white_pawn);
        }

        holder.joinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = v.getContext();
                Intent joinGameIntent = new Intent(context, PlayGameActivity.class);
                joinGameIntent.putExtra(
                        ChessGameController.HOST_PLAYS_WHITE_KEY, lobby.isHostPlaysWhite());
                joinGameIntent.putExtra(ChessGameController.LOBBY_NAME_KEY, lobby.getLobbyName());
                joinGameIntent.putExtra(ChessGameController.ID_KEY, lobby.getId());
                context.startActivity(joinGameIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lobbies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView lobbyName;
        public ImageView pawnOnButton;
        public View joinGameButton;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.lobbyName = (TextView) itemView.findViewById(R.id.singleLobbyNameTextView);
            this.pawnOnButton = (ImageView) itemView.findViewById(R.id.pawnOnJoinButtonImageView);
            this.joinGameButton = itemView.findViewById(R.id.joinGameButton);
        }
    }

}
