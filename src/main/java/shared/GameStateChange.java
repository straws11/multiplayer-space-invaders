package shared;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GameStateChange implements Serializable {

    private Map<String, Object> updatedStates;

    /**
     * Meant for sending across network, only containing variables client needs to update locally.
     */
    public GameStateChange() {
        this.updatedStates = new HashMap<>();
    }

    public void addChange(String varName, Object newValue) {
        updatedStates.put(varName, newValue);
    }

    public Map<String, Object> getUpdatedStates() {
        return updatedStates;
    }

}
