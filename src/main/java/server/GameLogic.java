package server;

import shared.GameState;

public class GameLogic {
    private GameState gameState;
    private boolean gameOver;

    public GameLogic(GameState gameState) {
        this.gameState = gameState;
        this.gameOver = false;
    }
    public void moveLeft() {
    }

    public void moveRight() {
    }

    public void shoot() {
    }

    public void enemyHit() {
        gameState.removeLife();
    }
}
