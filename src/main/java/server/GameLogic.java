package server;

import shared.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameLogic {
    private static final int RIGHT_LIMIT = 380;
    private static final int LEFT_LIMIT = 10;
    private static final int SHIFT_SIZE = 5;
    private static final int PLAYER_Y = 50;
    private GameStateChange gameStateChange;
    private int idCountTracker;
    private Server server;
    private boolean gameOver;

    private int enemiesLeftX;
    private int enemiesTopY;
    private ArrayList<SmallInvader> invaders; // example
    private Map<ClientHandler, Player> playerMapping;
    private Map<Integer, Bullet> bulletMap;
    private boolean enemiesMovingRight;

    public GameLogic(Server server) {
        // gameStateChange keeps track of all changes since last network send
        this.gameStateChange = new GameStateChange();
        this.server = server;
        this.gameOver = false;
        this.playerMapping = new ConcurrentHashMap<>();
        this.bulletMap = new ConcurrentHashMap<>();
        this.idCountTracker = 0;
        // TODO TEST REMOVE
        this.invaders = new ArrayList<>();
        invaders.add(new SmallInvader(200, 250));
        invaders.add(new SmallInvader(180, 250));
        invaders.add(new SmallInvader(220, 250));

    }

    public void addPlayer(ClientHandler clientHandler) {
        Color playerColor = new Color(
                (int) (Math.random() * 256),
                (int) (Math.random() * 256),
                (int) (Math.random() * 256)
        );
        Player newPlayer = new Player(this.idCountTracker + 1, playerColor);
        this.idCountTracker++;
        playerMapping.put(clientHandler, newPlayer);
        // send this player information to the player, so they know who they are
        this.server.updateClient(clientHandler, newPlayer);
        // send all players now
        this.server.updateClients(new ArrayList<>(playerMapping.values()));
    }

    public void removePlayer(ClientHandler clientHandler) {
        playerMapping.remove(clientHandler);
        this.server.updateClients(new ArrayList<>(playerMapping.values()));
    }

    public void moveLeft(int playerId) {
        Player player = getPlayerById(playerId);
        if (player != null && player.getPos() != 0) {
            player.move(-SHIFT_SIZE);
            server.updateClients(player);
            System.out.println("Player " + player.getId() + " moved left");
        }
    }

    public void moveRight(int playerId) {
        Player player = getPlayerById(playerId);
        if (player != null && player.getPos() != 400) {
            player.move(SHIFT_SIZE);
            server.updateClients(player);
            System.out.println("Player " + player.getId() + " moved right");
        }
    }

    public void shoot(int playerId) {
        if (!bulletMap.containsKey(playerId)) {
            // spawn new bullet
            int x = getPlayerById(playerId).getPos();
            bulletMap.put(playerId, new Bullet(x, 50)); // default height at 50
            System.out.println("Shoot!");
        }
    }

    /** Move bullets further along their move
     */
    public void shiftBullets() {
        for (Bullet bullet: bulletMap.values()) {
            bullet.shift(SHIFT_SIZE);
        }
        //server.updateClients(bulletMap);
    }

    private Player getPlayerById(int playerId) {
        for (Player player: this.playerMapping.values()) {
            if (player.getId() == playerId) {
                return player;
            }
        }
        return null;
    }

    public void enemyHit() {

    }

    /** Shift them on their next move
     *
     */
    public void shiftEnemies() {
        // determine if sides were hit
        if (this.enemiesLeftX == LEFT_LIMIT) {
           this.enemiesMovingRight = true;
        } else if (this.enemiesLeftX == RIGHT_LIMIT) {
           this.enemiesMovingRight = false;
           this.enemiesTopY -= SHIFT_SIZE; // shift down, one left-right complete
            invaders.forEach(inv -> inv.moveY(-SHIFT_SIZE));
        }

        // move left and right
        if (this.enemiesMovingRight) {
            this.enemiesLeftX += SHIFT_SIZE;
            invaders.forEach(inv -> inv.moveX(SHIFT_SIZE));
        } else {
            this.enemiesLeftX -= SHIFT_SIZE;
            invaders.forEach(inv -> inv.moveX(-SHIFT_SIZE));
        }
        server.updateClients(new int[]{enemiesLeftX, enemiesTopY});
    }

    /**
     * Called when the game state has been sent to clients. Only keep track of changes onward
     */
    public void clearGameStateChange() {
        this.gameStateChange = new GameStateChange();
    }

    public void handleCollisions() {
        ArrayList<Integer> enemiesX = (ArrayList<Integer>) invaders.stream().map(BaseEnemy::getX).toList();// bro what why
        ArrayList<Integer> enemiesY = (ArrayList<Integer>) invaders.stream().map(BaseEnemy::getY).toList();

        for (Bullet bullet: bulletMap.values()) {
            int x = bullet.getX();
            int y = bullet.getY();
            for (int i = 0; i < enemiesX.size(); i++) {
                if (x == enemiesX.get(i) && y == enemiesY.get(i)) {
                    // colliding
                    invaders.get(i).getHit(); // TODO surely just remove them lol
                    System.out.println("Collision, enemy dead!");
                }
            }
        }
    }

    public Map<Integer, Bullet> getBulletMap() {
        return bulletMap;
    }
}
