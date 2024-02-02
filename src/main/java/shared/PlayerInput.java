package shared;

import java.io.Serializable;

public class PlayerInput implements Serializable {
    private int keyCode;
    private int playerId;

    public PlayerInput(int keyCode, int playerId) {
        this.keyCode = keyCode;
        this.playerId = playerId;
    }

    public int getKeyCode() {
        return this.keyCode;
    }

    public int getPlayerId() {
        return this.playerId;
    }
}
