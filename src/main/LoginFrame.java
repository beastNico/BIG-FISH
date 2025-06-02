package main;

import javax.swing.*;
import java.awt.*;
import org.mindrot.jbcrypt.BCrypt;

public class LoginFrame {
    private JPanel loginPanel;
    private JFrame parentFrame;

    private JLabel userLabel = new JLabel("USERNAME");
    private JLabel passwordLabel = new JLabel("PASSWORD");
    private JTextField userTextField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JButton loginButton = new JButton("LOGIN");
    private JButton resetButton = new JButton("RESET");
    private JButton signUpButton = new JButton("SIGN UP");
    private JCheckBox showPassword = new JCheckBox("Show Password");

    final int screenWidth = 16 * 64;
    final int screenHeight = 12 * 64;

    public LoginFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        createLoginPanel();

        // Load stored credentials from CSV file
        SignUpFrame.loadCredentialsFromCSV("credentials.csv");

        // Save credentials on JVM shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            SignUpFrame.saveAllCredentialsToCSV("credentials.csv");
        }));
    }

    public JPanel getLoginPanel() {
        return loginPanel;
    }

    private void createLoginPanel() {
        loginPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("src/res/Tiles/bg.png");
                g.drawImage(background.getImage(), 0, 0, screenWidth, screenHeight, null);
            }
        };
        loginPanel.setLayout(null);
        loginPanel.setPreferredSize(new Dimension(screenWidth, screenHeight));

        // Title logo label (matches SignUpFrame positioning)
        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon("src/res/Tiles/title name.png");
        logoLabel.setIcon(logoIcon);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setBounds(0, 70, screenWidth, 145);
        loginPanel.add(logoLabel);

        // Form panel size and position (matches SignUpFrame)
        int panelWidth = 400;
        int panelHeight = 450; // Increased height to match SignUpFrame
        int xPosition = (screenWidth - panelWidth) / 2;
        int yPosition = (screenHeight - panelHeight) / 2;

        // Username label and text field
        userLabel.setBounds(xPosition + 50, yPosition + 30, 150, 30);
        userTextField.setBounds(xPosition + 200, yPosition + 30, 150, 30);

        // Password label and password field
        passwordLabel.setBounds(xPosition + 50, yPosition + 80, 150, 30);
        passwordField.setBounds(xPosition + 200, yPosition + 80, 150, 30);

        // Show password checkbox
        showPassword.setBounds(xPosition + 200, yPosition + 115, 150, 30);

        // Buttons positioning
        loginButton.setBounds(xPosition + 50, yPosition + 160, 100, 30);
        resetButton.setBounds(xPosition + 250, yPosition + 160, 100, 30);
        signUpButton.setBounds(xPosition + 125, yPosition + 210, 150, 30);
        signUpButton.setText("GO TO SIGN UP PAGE"); // Match SignUpFrame text

        // Styling colors
        Color textColor = Color.WHITE;
        Color bgColor = new Color(0, 0, 0, 150);

        // Set text colors
        userLabel.setForeground(textColor);
        passwordLabel.setForeground(textColor);
        showPassword.setForeground(textColor);

        // Text fields background and border
        userTextField.setBackground(bgColor);
        userTextField.setForeground(textColor);
        userTextField.setBorder(BorderFactory.createLineBorder(textColor));

        passwordField.setBackground(bgColor);
        passwordField.setForeground(textColor);
        passwordField.setBorder(BorderFactory.createLineBorder(textColor));

        // Show password checkbox background
        showPassword.setBackground(bgColor);
        showPassword.setOpaque(false);

        // Style buttons consistently
        styleButton(loginButton);
        styleButton(resetButton);
        styleButton(signUpButton);

        // Add all components to panel
        loginPanel.add(userLabel);
        loginPanel.add(passwordLabel);
        loginPanel.add(userTextField);
        loginPanel.add(passwordField);
        loginPanel.add(showPassword);
        loginPanel.add(loginButton);
        loginPanel.add(resetButton);
        loginPanel.add(signUpButton);

        addActionEvent();
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 0, 0, 150));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        button.setFocusPainted(false);
    }

    private void addActionEvent() {
        loginButton.addActionListener(e -> handleLogin());
        resetButton.addActionListener(e -> resetForm());
        signUpButton.addActionListener(e -> switchToSignUp());
        showPassword.addActionListener(e -> togglePasswordVisibility());
    }

    private void handleLogin() {
        String username = userTextField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (!SignUpFrame.userCredentials.containsKey(username)) {
            showError("Invalid username or password.");
            return;
        }

        String[] userData = SignUpFrame.userCredentials.get(username);
        String storedHash = userData[1]; // [0] = name, [1] = hashed password

        if (BCrypt.checkpw(password, storedHash)) {
            redirectToGame();
        } else {
            showError("Invalid username or password.");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(parentFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void resetForm() {
        userTextField.setText("");
        passwordField.setText("");
    }

    private void togglePasswordVisibility() {
        if (showPassword.isSelected()) {
            passwordField.setEchoChar((char) 0);
        } else {
            passwordField.setEchoChar('*');
        }
    }

    private void switchToSignUp() {
        SignUpFrame signUpFrame = new SignUpFrame(parentFrame);
        parentFrame.setContentPane(signUpFrame.getSignUpPanel());
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    private void redirectToGame() {
        GamePanel gamePanel = new GamePanel(parentFrame);
        parentFrame.getContentPane().removeAll();
        parentFrame.setContentPane(gamePanel);
        parentFrame.revalidate();
        parentFrame.repaint();

        gamePanel.requestFocusInWindow();
        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
}
