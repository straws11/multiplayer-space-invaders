package client;

import javax.swing.*;
import java.awt.*;

public class GameClientGUI extends JFrame {

    public Client client;

    public GameClientGUI(Client client) {
        super("Game Client");

        this.client = client;
        // since I'm inheriting, setsize is like doing JFrame.setsize etc etc
        // window size
        setSize(400, 300);
        //default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // panel for components
        addComponents();
    }

    private void addComponents() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Welcome to the game!");
        JButton startButton = new JButton("Start Game!");
        startButton.addActionListener(e -> {
            //handle button click event
            client.connectToServer("localhost", 1234);
        });

        // add the components to the panel
        panel.add(label);
        panel.add(startButton);

        //layout manager?
        panel.setLayout(new FlowLayout());

        // add panel to frame (i.e. this class which inherits)
        add(panel);

        //center window
        setLocationRelativeTo(null);
        // make window visible
        setVisible(true);

    }
}
