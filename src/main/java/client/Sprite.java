package client;

import shared.Player;

import java.awt.*;

public class Sprite {

    private static int spriteY = 100;
    private Player serverPlayer;
    private int pos;
    private Color color;

    public Sprite(Player serverPlayer) {
        this.serverPlayer = serverPlayer; // reference to be able to send attack data
        this.color = serverPlayer.getColor();
        updatePlayerData(serverPlayer);
        // place on some random position
    }

    /**
     * Update existing SpritePlayer with any new data from the server
     * @param newPlayer The updated server player object
     */
    public void updatePlayerData(Player newPlayer) {
        this.pos = newPlayer.getPos(); // TODO can change to just updatePlayerPos if that's all
        System.out.println(this.pos);
    }

    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRect(pos, spriteY, 20, 20);
    }


}
