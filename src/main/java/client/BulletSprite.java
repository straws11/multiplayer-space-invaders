package client;

import java.awt.*;

public class BulletSprite {
    private int x;
    private int y;

    public BulletSprite(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(this.x, this.y, 20, 20);
    }
}
