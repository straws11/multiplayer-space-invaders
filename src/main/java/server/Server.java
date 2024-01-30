package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import shared.GameState;

public class Server {

    private ArrayList<ClientHandler> connectedClients;
    private boolean gameStarted = false;

    GameState gameState;
    Game game;
    ServerSocket serverSocket;
    public Server(ServerSocket serverSocket) {
       this.serverSocket = serverSocket;
       this.connectedClients = new ArrayList<>();
    }

    public void startServer() {
        System.out.println("Server starting...");
        game = new Game(); // TODO move

        try {
            // continuously accept new incoming connections and add client handlers for each
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();

                // Create new client handler and start a thread for it
                ClientHandler newClient = new ClientHandler(socket, this);
                connectedClients.add(newClient);
                new Thread(newClient).start();
                System.out.println("SERVER: New client connected");
                // if at least one client, start a game
                if (!gameStarted && !connectedClients.isEmpty()) {// TODO move this somewhere else
                    game.start();
                    System.out.println("SERVER: Game started");
                    gameStarted = true;
                }
            }
        } catch (IOException e) {
            closeServerSocket();
        }
    }

    private void updateClients(Object stateUpdates) {
        for (ClientHandler handler: connectedClients) {
            handler.updateClient(stateUpdates);
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
                System.out.println("Closed Server Socket");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);

        server.startServer();
    }

    public void removeClientHandler(ClientHandler clientHandler) {
        connectedClients.remove(clientHandler);
        System.out.println("SERVER: Client disconnected");
        // end game if no more clients are connected
        if (connectedClients.isEmpty()) {
            game.stop();
            System.out.println("SERVER: Game ended, no players");
            gameStarted = false;
        }
    }

    /**
     * Send new update object received from client handler to Game object.
     * @param newObject The new update data
     */
    public void updateGameInput(Object newObject) {
        this.game.enqueueUpdate(newObject);
    }
}
