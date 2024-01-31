package client;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

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
        Client client = new Client();

        // launch GUI
        SwingUtilities.invokeLater(() -> {
            client.gameGui.setVisible(true);
        });
    }
}
