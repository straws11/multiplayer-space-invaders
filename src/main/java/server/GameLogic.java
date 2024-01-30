package server;

import shared.GameStateChange;
import shared.Player;

public class GameLogic {
    private GameStateChange gameStateChange;
    private boolean gameOver;

    private Player player;

    public GameLogic() {
        // gameStateChange keeps track of all changes since last network send
        this.gameStateChange = new GameStateChange();
        this.gameOver = false;
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
