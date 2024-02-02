package client;

import javax.swing.*;
import java.io.*;

public class Client {

    public ClientGameLogic clientGameLogic;
    public ClientNetworkManager networkManager;
    public ClientGameGUI gameGui;

    public Client() {
        this.clientGameLogic = new ClientGameLogic(this);
        this.networkManager = new ClientNetworkManager(this);
        this.gameGui = new ClientGameGUI(this);
    }

    public static void main(String[] args) throws IOException {
        // launch GUI
        SwingUtilities.invokeLater(() -> {
            Client client = new Client();
            client.gameGui.setVisible(true);
        });
    }
}
