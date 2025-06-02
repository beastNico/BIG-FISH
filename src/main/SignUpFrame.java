package main;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;

public class SignUpFrame {
    private JPanel signUpPanel;
    private JFrame parentFrame;

    // Store user credentials as Map<username, [name, hashedPassword]>
    public static Map<String, String[]> userCredentials = new HashMap<>();

    // UI Components
    private final JLabel nameLabel = new JLabel("NAME");
    private final JLabel userLabel = new JLabel("USERNAME");
    private final JLabel passwordLabel = new JLabel("PASSWORD");
    private final JLabel confirmPasswordLabel = new JLabel("CONFIRM PASSWORD");

    private final JTextField nameTextField = new JTextField();
    private final JTextField userTextField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JPasswordField confirmPasswordField = new JPasswordField();

    private final JButton signUpButton = new JButton("SIGN UP");
    private final JButton resetButton = new JButton("RESET");
    private final JButton loginButton = new JButton("GO TO LOGIN PAGE");

    private final int screenWidth = 16 * 64;
    private final int screenHeight = 12 * 64;

    public SignUpFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        createSignUpPanel();
    }

    public JPanel getSignUpPanel() {
        return signUpPanel;
    }

    // Load credentials from CSV file into userCredentials map
    public static void loadCredentialsFromCSV(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Credentials file not found, will create new one.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    userCredentials.put(parts[0].trim(), new String[]{parts[1].trim(), parts[2].trim()});
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading credentials: " + e.getMessage());
        }
    }

    // Save all user credentials to CSV file
    public static void saveAllCredentialsToCSV(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, String[]> entry : userCredentials.entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue()[0] + "," + entry.getValue()[1]);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving credentials: " + e.getMessage());
        }
    }

    // Setup the UI panel and components
    private void createSignUpPanel() {
        signUpPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("src/res/Tiles/bg.png");
                g.drawImage(background.getImage(), 0, 0, screenWidth, screenHeight, null);
            }
        };
        signUpPanel.setLayout(null);
        signUpPanel.setPreferredSize(new Dimension(screenWidth, screenHeight));

        // Title label with logo image
        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon("src/res/Tiles/title name.png");
        logoLabel.setIcon(logoIcon);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setBounds(0, 70, screenWidth, 145);
        signUpPanel.add(logoLabel);

        // Positioning form elements centered
        int panelWidth = 400;
        int panelHeight = 450;
        int xPosition = (screenWidth - panelWidth) / 2;
        int yPosition = (screenHeight - panelHeight) / 2;

        // Position labels and fields
        nameLabel.setBounds(xPosition + 50, yPosition + 30, 150, 30);
        nameTextField.setBounds(xPosition + 200, yPosition + 30, 150, 30);

        userLabel.setBounds(xPosition + 50, yPosition + 80, 150, 30);
        userTextField.setBounds(xPosition + 200, yPosition + 80, 150, 30);

        passwordLabel.setBounds(xPosition + 50, yPosition + 130, 150, 30);
        passwordField.setBounds(xPosition + 200, yPosition + 130, 150, 30);

        confirmPasswordLabel.setBounds(xPosition + 50, yPosition + 180, 150, 30);
        confirmPasswordField.setBounds(xPosition + 200, yPosition + 180, 150, 30);

        signUpButton.setBounds(xPosition + 50, yPosition + 230, 100, 30);
        resetButton.setBounds(xPosition + 250, yPosition + 230, 100, 30);
        loginButton.setBounds(xPosition + 150, yPosition + 280, 150, 30);

        // Style colors
        Color textColor = Color.WHITE;
        Color bgColor = new Color(0, 0, 0, 150);

        // Labels
        nameLabel.setForeground(textColor);
        userLabel.setForeground(textColor);
        passwordLabel.setForeground(textColor);
        confirmPasswordLabel.setForeground(textColor);

        // Text fields and password fields styling
        nameTextField.setBackground(bgColor);
        nameTextField.setForeground(textColor);
        nameTextField.setBorder(BorderFactory.createLineBorder(textColor));

        userTextField.setBackground(bgColor);
        userTextField.setForeground(textColor);
        userTextField.setBorder(BorderFactory.createLineBorder(textColor));

        passwordField.setBackground(bgColor);
        passwordField.setForeground(textColor);
        passwordField.setBorder(BorderFactory.createLineBorder(textColor));

        confirmPasswordField.setBackground(bgColor);
        confirmPasswordField.setForeground(textColor);
        confirmPasswordField.setBorder(BorderFactory.createLineBorder(textColor));

        // Buttons styling
        styleButton(signUpButton);
        styleButton(resetButton);
        styleButton(loginButton);

        // Add components to panel
        signUpPanel.add(nameLabel);
        signUpPanel.add(nameTextField);
        signUpPanel.add(userLabel);
        signUpPanel.add(userTextField);
        signUpPanel.add(passwordLabel);
        signUpPanel.add(passwordField);
        signUpPanel.add(confirmPasswordLabel);
        signUpPanel.add(confirmPasswordField);
        signUpPanel.add(signUpButton);
        signUpPanel.add(resetButton);
        signUpPanel.add(loginButton);

        addActionEvent();
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 0, 0, 150));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        button.setFocusPainted(false);
    }

    private void addActionEvent() {
        signUpButton.addActionListener(e -> handleSignUp());
        resetButton.addActionListener(e -> resetForm());
        loginButton.addActionListener(e -> switchToLogin());
    }

    private void handleSignUp() {
        String name = nameTextField.getText().trim();
        String username = userTextField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        // Validation
        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showError("All fields are required.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters.");
            return;
        }

        if (userCredentials.containsKey(username)) {
            showError("Username already exists.");
            return;
        }

        // Hash password using BCrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        userCredentials.put(username, new String[]{name, hashedPassword});

        JOptionPane.showMessageDialog(parentFrame, "Registration successful!");
        switchToLogin();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(parentFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void resetForm() {
        nameTextField.setText("");
        userTextField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }

    private void switchToLogin() {
        LoginFrame loginFrame = new LoginFrame(parentFrame);
        parentFrame.setContentPane(loginFrame.getLoginPanel());
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}
