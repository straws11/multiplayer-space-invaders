package game;

import java.io.Serializable;

public class GameState implements Serializable {

    private int playerLives;
    private int playerPos;

    public GameState() {
        this.playerLives = 3;
    }

    public void setPlayerPos(int newPos) {
        this.playerPos = newPos;
    }

    public void removeLife() {
        this.playerLives--;
    }


}
