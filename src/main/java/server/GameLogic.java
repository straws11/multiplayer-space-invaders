package server;

import shared.GameStateChange;
import shared.Player;
import shared.SmallInvader;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameLogic {
    private GameStateChange gameStateChange;
    private int idCountTracker;
    private Server server;
    private boolean gameOver;

    private ArrayList<SmallInvader> invaders; // example
    private Map<ClientHandler, Player> playerMapping;

    public GameLogic(Server server) {
        // gameStateChange keeps track of all changes since last network send
        this.gameStateChange = new GameStateChange();
        this.server = server;
        this.gameOver = false;
        this.playerMapping = new HashMap<>();
        this.idCountTracker = 0;
    }

    public void addPlayer(ClientHandler clientHandler) {
        Player newPlayer = new Player(this.idCountTracker + 1, new Color(0, 0, 0)); // todo change color
        this.idCountTracker++;
        playerMapping.put(clientHandler, newPlayer);
        // send this player information to the player, so they know who they are
        this.server.updateClient(clientHandler, newPlayer);
        // send all players now
        this.server.updateClients(new ArrayList<>(playerMapping.values()));
    }

    public void removePlayer(ClientHandler clientHandler) {
        playerMapping.remove(clientHandler);
        this.server.updateClients(new ArrayList<>(playerMapping.values()));
    }

    public void moveLeft() {

    }

    public void moveRight() {
    }

    public void shoot() {
    }

    public void enemyHit() {

    }

    /**
     * Called when the game state has been sent to clients. Only keep track of changes onward
     */
    public void clearGameStateChange() {
        this.gameStateChange = new GameStateChange();
    }
}
