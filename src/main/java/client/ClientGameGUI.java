package client;

import shared.Player;
import shared.PlayerInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class ClientGameGUI extends JFrame {

    private Client client;
    private JPanel mainPanel; // just the one that everything lives on
    private CardLayout cardLayout; // this allows switching between panels that live on mainpanel
    private JPanel startPanel;
    private JPanel mainGamePanel;
    public GamePanel gamePanel;



    public ClientGameGUI(Client client) {
        super("Game Client");
        this.client = client;
        setupLayout();
        setupKeyBindings();
    }

    private void setupKeyBindings() {
        JPanel contentPane = (JPanel) this.getContentPane();
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = contentPane.getInputMap(condition);
        ActionMap actionMap = contentPane.getActionMap();

        Map<String, Integer> keyBinds = Map.of(
                "left", KeyEvent.VK_LEFT,
                "right", KeyEvent.VK_RIGHT,
                "space", KeyEvent.VK_SPACE
        );

        // create a binding in the input map and action map for each of the above
        for (Map.Entry<String, Integer> entry: keyBinds.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();

            inputMap.put(KeyStroke.getKeyStroke(value, 0), key);
            actionMap.put(key, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sendKeyPressCode(value);
                }
            });
        }

    }

    private void setupLayout() {
        // window size
        setSize(400, 300);
        //default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //center window
        setLocationRelativeTo(null);
        initializeComponents();
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
            cardLayout.show(mainPanel, "mainGame");
        });

        // main Game panel where buttons and the actual game panel lives on
        mainGamePanel = new JPanel();
        JButton quitButton = new JButton("Quit Game");
        mainGamePanel.add(new JLabel("THIS IS THE GAME HI"));
        mainGamePanel.add(quitButton);
        // game lives on the gamePanel, all sprites
        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(400, 200));
        mainGamePanel.add(gamePanel, "game");

        quitButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "start");
            client.networkManager.closeConnection(); // disconnect from server
        });

        // add game and start panel to main (main being the essential window, afaiu);
        mainPanel.add(startPanel, "start");
        mainPanel.add(mainGamePanel, "mainGame");

    }

    public void sendKeyPressCode(int keyCode) {
        PlayerInput playerInput = new PlayerInput(keyCode, client.clientGameLogic.getPlayerId());
        client.networkManager.sendObject(playerInput);
    }
}
