import java.io.Serializable;

public class GameState implements Serializable {
    private String name;

    public GameState(String name) {
        this.name = name;
    }

    public String getData() {
        return this.name;
    }
}
