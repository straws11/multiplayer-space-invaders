package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import game.Game;
import game.GameState;

public class Server {

    private ArrayList<ClientHandler> connectedClients;
    ServerSocket serverSocket;
    public Server(ServerSocket serverSocket) {
       this.serverSocket = serverSocket;
       this.connectedClients = new ArrayList<>();
    }

    public void startServer() {
        System.out.println("Server starting...");
        GameState gameState = new GameState();
        Game game = new Game(gameState);
        game.start();
        try {
            // continuously accept new incoming connections and add client handlers for each
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();

                // Create new client handler and start a thread for it
                ClientHandler newClient = new ClientHandler(socket);
                connectedClients.add(newClient);
                new Thread(newClient).start();
                System.out.println("New client thread started");

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
}
