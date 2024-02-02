package client;

import shared.Player;

import java.util.ArrayList;
import java.util.List;

public class ClientGameLogic {

    private Player player;
    private int playerId;
    private ArrayList<Player> onlinePlayers;
    private Client client;

    public ClientGameLogic(Client client) {
        this.client = client;
        this.playerId = -1;
        this.onlinePlayers = new ArrayList<>();
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
        client.gameGui.gamePanel.syncSpritesToPlayers(onlinePlayers);
    }

    /**
     * Update a single player's data, coming in from the server
     * @param player the update player's data
     */
    public void updatePlayerData(Player player) {
        client.gameGui.gamePanel.syncSprite(player);
        // TODO optimize
    }

    public int getPlayerId() {
        return this.playerId;
    }

}
