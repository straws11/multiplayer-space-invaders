package client;

import shared.Bullet;
import shared.Player;
import shared.SmallInvader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ClientGameLogic {

    private static final int GAP_X = 20; // gap between enemies horizontally
    private Player player;
    private int playerId;
    private ArrayList<Player> onlinePlayers;
    private Client client;
    private ArrayList<SmallInvader> invaders; // example
    private ConcurrentHashMap<Integer, Bullet> bulletMap;

    public ClientGameLogic(Client client) {
        this.client = client;
        this.playerId = -1;
        this.onlinePlayers = new ArrayList<>();
        this.invaders = new ArrayList<>();
        this.bulletMap = new ConcurrentHashMap<>();
    }

    /**
     * Set initial player data, received from server only upon connecting;
     * @param localPlayer Player object with starting data
     */
    public void setPlayer(Player localPlayer) {
        this.player = localPlayer;
        this.playerId = localPlayer.getId();
        this.onlinePlayers.add(localPlayer);
        //client.gameGui.syncSpritesToPlayers(); don't think this is necessary here
        // ^ should receive an arraylist with all players right after this method anyway
        // this also means there aren't any sprites for the player yet, which should also be fine
    }

    /**
     * Updates data of all currently online players, including the local player
     * @param players ArrayList of player objects
     */
    public void updateOnlinePlayers(ArrayList<Player> players) {
        // remove players no longer present
        List<Integer> playerIds = players.stream().map(Player::getId).toList();
        this.onlinePlayers.removeIf(p -> !playerIds.contains(p.getId()));

        for (Player player: players) {
            int id = player.getId();
            // check if already in onlinePlayer
            int indexToUpdate = -1;

            for (int i = 0; i < this.onlinePlayers.size() ; i++) {
                if (this.onlinePlayers.get(i).getId() == id) {
                    indexToUpdate = i;
                    break;
                }
            }
            // if already in list, replace with new updated one
            if (indexToUpdate != -1) {
                this.onlinePlayers.set(indexToUpdate, player);
            } else {  // else add to the players
                this.onlinePlayers.add(player);
            }
        }
        // remove, add, update sprites with new positional data etc, also redraws them
        client.gameGui.gamePanel.syncSpritesToPlayers(this.onlinePlayers);
    }

    /**
     * Update a single player's data, coming in from the server
     * @param player the update player's data
     */
    public void updatePlayerData(Player player) {
        client.gameGui.gamePanel.syncSprite(player);
        // TODO optimize
    }

    /**
     * Sync exact coords of all enemies client-side
     * @param coords top and left coordinates of the enemies, used to calculate all enemies' individual coordinates
     */
    public void updateEnemyPositions(int[] coords) {
        // TODO update for 2d
        for (int i = 0; i < invaders.size(); i++) {
            invaders.get(i).setX(i * GAP_X + 20); // using i to space out
        }
    }

    /**
     * Sync exact coords of all bullets client-side, removing and adding clients bullets as needed
     */
    public void updateBulletPositions(ConcurrentHashMap<Integer, Bullet> bullets) {
        // remove no longer existing bullets
        this.bulletMap.entrySet().removeIf(entry -> !bullets.contains(entry.getKey()));
        // add or update bullets
        this.bulletMap.putAll(bullets);
        // remove sprites of no longer existing bullets
        // add new sprites for new bullets
        client.gameGui.gamePanel.syncSpritesToBullets(this.bulletMap);
    }

    public int getPlayerId() {
        return this.playerId;
    }

}
