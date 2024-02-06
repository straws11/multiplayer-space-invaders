package shared;

public abstract class BaseEnemy {

    private int x;
    private int y;
    private boolean alive;

    public BaseEnemy(int x, int y) {
        this.x = x;
        this.y = y;
        this.alive = true;
    }

    abstract void shoot();

    public void getHit() {
        this.alive = false;
    }

    public void moveX(int x) {
        this.x += x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public void moveY(int y) {
        this.y += y;
    }

    public int getY() {
        return this.y;
    }
}
