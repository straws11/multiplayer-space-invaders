package server;

import shared.GameState;
import shared.PlayerInput;

import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Queue;

public class Game implements Runnable {

    private GameState gameState;
    private GameLogic gameLogic;

    private Queue<Object> updateQueue;

    private boolean running;
    public Game() {
        this.gameState = new GameState();
        this.gameLogic = new GameLogic(this.gameState);

        this.updateQueue = new LinkedList<>();
        this.running = false;
    }

    /**
     * Add new received update to Queue to process upon next update() call
     * @param updateObject New object received
     */
   public void enqueueUpdate(Object updateObject) {
        this.updateQueue.add(updateObject);
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
        // process all new inputs received since last time update() was called
        processQueuedInput();
    }

    private void processQueuedInput() {
        while (!updateQueue.isEmpty()) {
            Object nextUpdate = updateQueue.poll();

            // differentiate between input types
            // this is the first time since object received that we determine this
            if (nextUpdate instanceof PlayerInput) {
                PlayerInput newPlayerInput = (PlayerInput) nextUpdate;
                processPlayerInput(newPlayerInput.getKeyCode());

            } else if (nextUpdate instanceof  GameState) {
                GameState newGameState = (GameState) nextUpdate;
                processStateChanges(newGameState);

            }
        }
    }

    private void processStateChanges(GameState state) {

    }

    /**
     * Call appropriate game logic method to handle player key press input
     * @param keyCode KeyEvent int code corresponding to a pressed key.
     */
    private void processPlayerInput(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                gameLogic.moveLeft();
                break;
            case KeyEvent.VK_RIGHT:
                gameLogic.moveRight();
                break;
            case KeyEvent.VK_SPACE:
                gameLogic.shoot();
                break;
            default: // not a key relevant for this game
                break;
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
