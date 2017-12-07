package edu.illinois.finalproject;

public class GameLobby {
    private String lobbyName;
    private boolean hostPlaysWhite;
    private String id;

    public GameLobby() {

    }

    public GameLobby(String lobbyName, boolean hostPlaysWhite, String id) {
        this.lobbyName = lobbyName;
        this.hostPlaysWhite = hostPlaysWhite;
        this.id = id;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public boolean isHostPlaysWhite() {
        return hostPlaysWhite;
    }

    public String getId() {
        return id;
    }
}
