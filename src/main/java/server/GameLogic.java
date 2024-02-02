package server;

import shared.GameStateChange;
import shared.Player;
import shared.SmallInvader;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        this.playerMapping = new ConcurrentHashMap<>();
        this.idCountTracker = 0;
    }

    public void addPlayer(ClientHandler clientHandler) {
        Color playerColor = new Color(
                (int) (Math.random() * 256),
                (int) (Math.random() * 256),
                (int) (Math.random() * 256)
        );
        Player newPlayer = new Player(this.idCountTracker + 1, playerColor);
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

    public void moveLeft(int playerId) {
        Player player = getPlayerById(playerId);
        if (player != null && player.getPos() != 0) {
            player.move(-5);
            server.updateClients(player);
            System.out.println("Player " + player.getId() + " moved left");
        }
    }

    public void moveRight(int playerId) {
        Player player = getPlayerById(playerId);
        if (player != null && player.getPos() != 400) {
            player.move(5);
            server.updateClients(player);
            System.out.println("Player " + player.getId() + " moved right");
        }
    }

    public void shoot(int playerId) {
        System.out.println("Shoot!");
    }

    private Player getPlayerById(int playerId) {
        for (Player player: this.playerMapping.values()) {
            if (player.getId() == playerId) {
                return player;
            }
        }
        return null;
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
