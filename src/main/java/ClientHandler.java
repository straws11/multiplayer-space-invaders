import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public ClientHandler(Socket socket) throws IOException {

        this.socket = socket;
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

    public void updateClient(Object updates) {
        try {
            this.objectOutputStream.writeObject(updates);
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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Client Disconnected!");
        }
    }


    @Override
    public void run() {
        try {
            while (socket.isConnected() && !socket.isClosed()) {
                try {
                    Object receivedUpdates = objectInputStream.readObject();
                    // can process based on type:
                    // if (receivedUpdates instanceof GameState) doThis()
                    // for now i'm assuming it's some tpe of gamestate
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
