package shared;

import java.awt.*;
import java.io.Serializable;

public class Player implements Serializable {

    protected int pos;
    protected int id;
    protected int lives;
    protected Color color;

    public Player(int id, Color playerColor) {
        this.pos = (int) Math.floor(Math.random() * 100); // TODO change ofc
        this.lives = 3; // or some amount
        this.color = playerColor;
        this.id = id;
    }

    public void move(int x) {
        this.pos += x;
    }

    public void fire() {

    }

    public int getId() {
        return this.id;
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

    public int getLives() {
        return this.lives;
    }

    public int getPos() {
        return this.pos;
    }
}
