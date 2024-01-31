package server;

import shared.GameStateChange;
import shared.PlayerInput;

import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Queue;

public class Game implements Runnable {

    private GameStateChange gameStateChange;
    private GameLogic gameLogic;

    private Queue<PlayerInput> updateQueue;

    private boolean running;
    public Game(Server server) {
        this.gameStateChange = new GameStateChange();
        this.gameLogic = new GameLogic(server);

        this.updateQueue = new LinkedList<>();
        this.running = false;
    }

    /**
     * Add new received player input update to queue to process upon next update() call
     * @param inputObject New PlayerInput object received
     */
   public void enqueuePlayerInput(PlayerInput inputObject) {
        this.updateQueue.add(inputObject);
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

    // replace both below functions, unnecessary, make gameLogic public
    public void addPlayer(ClientHandler clientHandler) {
       // simply calls gameLogic to add new player
       this.gameLogic.addPlayer(clientHandler);
    }

    public void removePlayer(ClientHandler clientHandler) {
       this.gameLogic.removePlayer(clientHandler);
    }

    /**
     * Handle game logic, key presses, movement etc
     */
    private void update() {
        // process all new inputs received since last time update() was called
        processQueuedInput();
        // TODO enemies need to move again, bullets further travel etc??
    }

    /**
     * Call appropriate game logic method to handle player key press input for each update
     */
    private void processQueuedInput() {
        while (!updateQueue.isEmpty()) {
            PlayerInput nextInput = updateQueue.poll();

            int keyCode = nextInput.getKeyCode();

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
