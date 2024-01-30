package client;

import shared.PlayerInput;

import javax.swing.*;
import java.awt.*;

public class ClientGameGUI extends JFrame {

    private Client client;
    private KeyboardInputHandler keyboardInputHandler;
    private JPanel mainPanel; // just the one that everything lives on
    private CardLayout cardLayout; // this allows switching between panels that live on mainpanel
    private JPanel startPanel;
    private JPanel gamePanel;

    public ClientGameGUI(Client client) {
        super("Game Client");

        this.client = client;
        // since I'm inheriting, setsize is like doing JFrame.setsize etc etc

        // init and setup gui stuff
        initializeComponents();
        setupLayout();
        keyboardInputHandler = new KeyboardInputHandler(this);
    }

    private void setupLayout() {
        // window size
        setSize(400, 300);
        //default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //center window
        setLocationRelativeTo(null);
        getContentPane().add(mainPanel);
    }

    private void initializeComponents() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // start panel
        startPanel = new JPanel();
        JButton startButton = new JButton("Start Game!");
        startPanel.add(startButton);
        startPanel.add(new JLabel("Welcome to Space Invaders!"));

        startButton.addActionListener(e -> {
            //handle button click event
            client.networkManager.connectToServer("localhost", 1234);
            cardLayout.show(mainPanel, "game");
        });

        // game panel
        gamePanel = new JPanel();
        JButton quitButton = new JButton("Quit Game");
        gamePanel.add(new JLabel("THIS IS THE GAME HI"));
        gamePanel.add(quitButton);

        quitButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "start");
            client.networkManager.closeConnection(); // disconnect from server
        });

        // add game and start panel to main
        mainPanel.add(startPanel, "start");
        mainPanel.add(gamePanel, "game");

    }

    public void sendKeyPressCode(int keyCode) {
        PlayerInput playerInput = new PlayerInput(keyCode);
        client.networkManager.sendObject(playerInput);
    }
}
