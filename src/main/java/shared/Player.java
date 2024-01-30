package shared;

import java.awt.*;

public class Player {

    private int pos;
    private int lives;
    private Color color;

    public Player(Color playerColor) {
        this.pos = 0;
        this.lives = 3; // or some amount
        this.color = playerColor;
    }

    public void move(int x) {
        this.pos += x;
    }

    public void fire() {

    }

    public void getHit() {
        lives--;
    }

    public boolean isAlive() {
        return lives > 0;
    }

    public Color getColor() {
        return this.color;
    }
}
