package client;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    String username;

    public Client(String username) {
        this.username = username;
    }

    public void connectToServer(String serverAddress, int port) {
        try {
            this.socket = new Socket(serverAddress, port);
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            // start listeners
            startReceiveDataListener();
            startInputListener();
        } catch (IOException e) {
            System.out.println("Failed to connect, server address or port not right");
            e.printStackTrace();
        }
    }

    /**
     * Attempt to read objects from ObjectInputStream while socket connected.
     * Runs on a separate thread.
     */
    private void startReceiveDataListener() {
        new Thread( new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        Object receivedObject = objectInputStream.readObject();
                        System.out.println("Object received from server");
                        // TODO process object, update local display and GUI etc
                    } catch (IOException | ClassNotFoundException e) { // TODO i don't think ClassNotFound should go here
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * Listen for client game input, runs on a separate thread.
     */
    private void startInputListener() {
        new Thread( new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    // TODO implement listening for key presses etc
                    break; // TODO remove
                }

            }
        }).start();
    }


    private void closeConnection() {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("surely not");
        Client client = new Client("placeholder");
        System.out.println("problem?");

        // launch GUI
        SwingUtilities.invokeLater(() -> {
            GameClientGUI gameClientGui = new GameClientGUI(client);
            gameClientGui.setVisible(true);
        });
    }
}
