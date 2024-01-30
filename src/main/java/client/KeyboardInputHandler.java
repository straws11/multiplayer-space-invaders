package client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputHandler implements KeyListener {

    private GameClientGUI gameClientGui;

    public KeyboardInputHandler(GameClientGUI gameClientGui) {
        this.gameClientGui = gameClientGui;
        // TODO maybe move this to live in Client instead of clientgui?? bit weird
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // not necessary
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        gameClientGui.sendKeyPressCode(keyCode);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
