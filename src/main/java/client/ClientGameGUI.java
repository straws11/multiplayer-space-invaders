package client;

import shared.Player;
import shared.PlayerInput;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ClientGameGUI extends JFrame {

    private Client client;
    private KeyboardInputHandler keyboardInputHandler;
    private JPanel mainPanel; // just the one that everything lives on
    private CardLayout cardLayout; // this allows switching between panels that live on mainpanel
    private JPanel startPanel;
    private JPanel gamePanel;

    private Map<Integer, SpritePlayer> playerSprites;

    public ClientGameGUI(Client client) {
        super("Game Client");

        this.client = client;
        this.playerSprites = new HashMap<>();
        setupLayout();
        keyboardInputHandler = new KeyboardInputHandler(this);
    }

    /**
     * Remove old player sprites, add new ones and call to draw on updated positions
     * @param updatedPlayers ArrayList from ClientGameLogic
     */
    public void processUpdatedPlayers(ArrayList<Player> updatedPlayers) {
        // remove players that are no longer present (not in updatedPlayers anymore)
        List<Integer> newPlayerIds = updatedPlayers.stream().map(Player::getId).toList();
        // using this iterator is because it doesn't skip over elements when they are deleted
        for (Iterator<Map.Entry<Integer, SpritePlayer>> iterator = playerSprites.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<Integer, SpritePlayer> entry = iterator.next();
            SpritePlayer sprite = entry.getValue();
            if (!newPlayerIds.contains(sprite.getId())) {
                sprite.removeSprite(); // delete the graphical sprite
                iterator.remove(); // remove from tracked list of SpritePlayers
            }
        }

        // add any new sprites from new players
        for (Player player: updatedPlayers) {
            if (!playerSprites.containsKey(player.getId())) {
                // add sprites for new player
                SpritePlayer newSprite = new SpritePlayer(player.getId(), player.getColor(), player, this);
                this.playerSprites.put(player.getId(), newSprite);
            }
        }

        // update each player's data and draw it
        for (Player updatedPlayer : updatedPlayers) { // both map and updatedPlayers should contain all the same clients now
            SpritePlayer sprite = playerSprites.get(updatedPlayer.getId());
            sprite.updatePlayerData(updatedPlayer); // drawing will happen in here
        }

        gamePanel.revalidate();
        gamePanel.repaint();
    }

    /**
     * Draw a single player's sprite
     * @param sprite
     */
    private void drawPlayer(SpritePlayer sprite) {

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
