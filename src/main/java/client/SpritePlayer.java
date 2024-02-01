package client;

import shared.Player;

import java.awt.*;

public class SpritePlayer extends Player {

    private ClientGameGUI gui;

    public SpritePlayer(int id, Color playerColor, Player serverPlayer, ClientGameGUI gui) {
        super(id, playerColor);
        this.gui = gui;
        updatePlayerData(serverPlayer);
    }

    /**
     * Update existing SpritePlayer with any new data from the server
     * @param newPlayer The updated server player object
     */
    public void updatePlayerData(Player newPlayer) {
        this.pos = newPlayer.getPos();
        this.lives = newPlayer.getLives();
        drawPlayer(); // draw at new position
    }

    /**
     * Draw player on the GUI
     */
    private void drawPlayer() {
        gui.draw
    }

    /**
     * Remove sprite from the GUI, no longer in game / died
     */
    public void removeSprite() {
        this.gui.remove
    }
}
