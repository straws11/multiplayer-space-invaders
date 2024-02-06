package client;

import shared.Bullet;
import shared.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GamePanel extends JPanel {
    public Map<Integer, Sprite> playerSprites;
    public Map<Integer, BulletSprite> bulletSprites;
    public GamePanel() {
        this.playerSprites = new HashMap<>();
        this.bulletSprites = new HashMap<>();
    }

    /**
     * Remove and add sprite objects as necessary, depending on newly dis/connected players.
     * @param onlinePlayers all players that are still in the game
     */
    public void syncSpritesToPlayers(ArrayList<Player> onlinePlayers) {
        // remove sprites that are no longer present
        List<Integer> newPlayerIds = onlinePlayers.stream().map(Player::getId).toList();
        // remove from tracked list of SpritePlayers
        playerSprites.entrySet().removeIf(entry -> !newPlayerIds.contains(entry.getKey()));

        // add any new sprites from new players
        for (Player player: onlinePlayers) {
            if (!playerSprites.containsKey(player.getId())) {
                // add sprites for new player
                Sprite newSprite = new Sprite(player);
                playerSprites.put(player.getId(), newSprite);
            } else {
                // player does already exist, simply update their data
                syncSprite(player); // after this, drawing is handled in paintComponent
            }
        }
        // basically syncs new positions on graphics for each, i think! (maybe it even calls paintComponent)
        repaint();
    }

    /**
     * Sync the player's corresponding sprite's data
     * @param player
     */
    public void syncSprite(Player player) {
        Sprite sprite = playerSprites.get(player.getId());
        sprite.updatePlayerData(player);
        repaint();
    }

    /**
     * Remove and add sprite objects as necessary
     * @param bullets all bullets currently alive
     */
    public void syncSpritesToBullets(ConcurrentHashMap<Integer, Bullet> bullets) {
        // remove bullet sprites no longer on screen
        this.bulletSprites.entrySet().removeIf(entry -> !bullets.containsKey(entry.getKey()));
        // add new sprites
        for (ConcurrentHashMap.Entry<Integer, Bullet> entry: bullets.entrySet()) {
            if (!this.bulletSprites.containsKey(entry.getKey())) {
                Bullet bullet = entry.getValue();
                BulletSprite bulletSprite = new BulletSprite(bullet.getX(), bullet.getY());
                // add new sprite
                this.bulletSprites.put(entry.getKey(), bulletSprite);
            }
        }
        repaint(); // redraw all sprites, these that are gone should be gone
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ArrayList<Sprite> spriteList = new ArrayList<>(playerSprites.values());
        spriteList.forEach(sprite -> sprite.draw(g));

        ArrayList<BulletSprite> bulletSpriteList = new ArrayList<>(bulletSprites.values());
        bulletSpriteList.forEach(bullet -> bullet.draw(g));
    }
}
