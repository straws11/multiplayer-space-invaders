package client;

import java.io.*;
import java.net.Socket;

public class ClientNetworkManager {

    private Socket socket;
    private BufferedReader bufferedReader;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public ClientNetworkManager() {

    }

    public void connectToServer(String serverAddress, int port) {
        try {
            this.socket = new Socket(serverAddress, port);
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            // start listeners
            startReceiveDataListener();
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
     * Send object from client to server
     * @param object object to send, any type
     */
    public void sendObject(Object object) { // TODO keyCodes might be the only thing, so can change?
        try {
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
        } catch (IOException e) {
            closeConnection();
        }

    }

    public void closeConnection() {
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
}
