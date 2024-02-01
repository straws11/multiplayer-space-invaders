package client;

import shared.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GamePanel extends JPanel {
    public Map<Integer, Sprite> sprites;
    public GamePanel() {
        sprites = new HashMap<>();
    }


    /**
     * Remove and add sprite objects as necessary, depending on newly dis/connected players.
     * @param onlinePlayers all players that are still in the game
     */
    public void syncSpritesToPlayers(ArrayList<Player> onlinePlayers) {
        // remove sprites that are no longer present
        List<Integer> newPlayerIds = onlinePlayers.stream().map(Player::getId).toList();
        // remove from tracked list of SpritePlayers
        sprites.entrySet().removeIf(entry -> !newPlayerIds.contains(entry.getKey()));

        // add any new sprites from new players
        for (Player player: onlinePlayers) {
            if (!sprites.containsKey(player.getId())) {
                // add sprites for new player
                Sprite newSprite = new Sprite(player);
                sprites.put(player.getId(), newSprite);
            } else {
                // player does already exist, simply update their data
                Sprite sprite = sprites.get(player.getId());
                sprite.updatePlayerData(player); // after this, drawing is handled in paintComponent
            }
        }

        // basically syncs new positions on graphics for each, i think! (maybe it even calls paintComponent)
        repaint();

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ArrayList<Sprite> spriteList = new ArrayList<>(sprites.values());
        spriteList.forEach(sprite -> sprite.draw(g));
    }
}
