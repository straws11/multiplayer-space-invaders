package shared;

public class Bullet {
    private int x;
    private int y;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void shift(int shiftSize) {
        this.x += shiftSize;
    }
}
