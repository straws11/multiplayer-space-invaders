package shared;

import java.io.Serializable;

public class PlayerInput implements Serializable {
    private int keyCode;

    public PlayerInput(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return this.keyCode;
    }
}
