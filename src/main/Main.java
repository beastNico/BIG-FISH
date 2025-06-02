package main;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Single JFrame for the whole application
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("BIG FISH");

        // Game window dimensions
        final int screenWidth = 16 * 64; // 16 columns of 64x64 tiles
        final int screenHeight = 12 * 64; // 12 rows of 64x64 tiles

        LoginFrame loginFrame = new LoginFrame(window);

        window.setContentPane(loginFrame.getLoginPanel());
        window.setSize(new Dimension(screenWidth, screenHeight));
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
