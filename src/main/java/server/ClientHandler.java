package server;

import shared.Player;
import shared.PlayerInput;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private Socket socket;
    private Server server;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public ClientHandler(Socket socket, Server server) throws IOException {

        this.socket = socket;
        this.server = server;
        // for the game state data
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());

        // for text
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

    }

    /**
     * Send message to client using bufferedWriter
     * @param message text message so send
     */
    public void sendMessage(String message) {
        try {
            this.bufferedWriter.write(message);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
        } catch (IOException e) {
            closeConnection();
        }
    }

    public synchronized void updateClient(Object updates) {
        try {
            this.objectOutputStream.reset();
            this.objectOutputStream.writeUnshared(updates);
            this.objectOutputStream.flush();
        } catch (IOException e) {
            closeConnection("Failed to update client with latest updates");
        }
    }

    private void closeConnection(String reasonForClosing) {
        closeConnection();
        System.out.println("Reason for closing: " + reasonForClosing);
    }

    private void closeConnection() {
        try {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
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
            // remove from client handler list
            server.removeClientHandler(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            while (socket.isConnected() && !socket.isClosed()) {
                try {
                    Object receivedObject = objectInputStream.readUnshared();
                    // can process based on type:
                    // if (receivedObject instanceof GameState) doThis()
                    if (receivedObject instanceof PlayerInput) {
                        server.updateGameInput((PlayerInput) receivedObject);
                    }
                } catch (EOFException e) {
                    //System.out.println("Client Disconnected!"); correction: going to disconnect
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }
}
