package client;

import shared.GameState;

public class Game implements Runnable {

    private GameState gameState;
    private boolean gameOver;
    private boolean running;


    public Game(GameState gameState) {
        this.gameState = gameState;
        this.gameOver = false;
        this.running = false;
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

    public void start() {
        if (!running) {
            running = true;
            new Thread(this).start();
        }
    }

    public void stop() {
        running = false;
    }

    /**
     * Handle game logic, key presses, movement etc
     */
    private void update() {
        if (keyPressed()) {
            handleKeyPress();
        }
        if (mouseClicked()) {
            handleMouseClicked();
        }
    }

    private void handleMouseClicked() {
    }

    private boolean mouseClicked() {
        return false;
    }

    private void handleKeyPress() {

    }

    private boolean keyPressed() {
        return false;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1_000_000_000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                update(); //update game logic, ie continue trying to play the game. runs about 60 times
                updates++;
                delta--;
            }
            //render() render game state
            frames++;

            if (System.currentTimeMillis() - timer > 1000) { // checks if a second has passed
                timer += 1000;
                //System.out.println("updates: " + updates + ", frames: " + frames);
                updates = 0;
                frames = 0;
            }
        }
        stop();
    }
}
