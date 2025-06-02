package main;

import Entity.Entity;
import Entity.Fish1;
import Tile.TileManager;
import enemy.Enemy1;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GamePanel extends JPanel implements Runnable {
    
    // Screen settings
    final int originalTileSize = 16; // 16x16 pixels per tile
    final int scale = 4;
    public final int tileSize = originalTileSize * scale; // 64x64 tile size
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    private JFrame parentFrame;
    
    // Frames per second
    int fps = 60;
    
    // Core system components
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;
    Sound sound = new Sound();
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    
    // Entities
    public Enemy1 enemy1 = new Enemy1(this);
    public Fish1 fish1 = new Fish1(this, keyH);
    public Entity enemy[] = new Entity[900];
    ArrayList<Entity> entityList = new ArrayList<>();
    
    // Score tracking
    public int score = 0;
    public int highScore = 0;
    public int lastGameScore = 0;
    
    // Game states
    public int gameState;
    public final int playState = 1;
    public final int titleState = 0;
    public final int pauseState = 2;
    public final int gameOverState = 6;
    public final int optionState = 5;
    public final int loginState = 7; // Login screen state
    
    private String currentUsername = null;
    private String currentFullName = null;
    private boolean loggedIn = false;
    
    public GamePanel(JFrame parentFrame) {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.parentFrame = parentFrame;
        
        // Mouse listener for UI click handling
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ui.checkMouseClick(e.getX(), e.getY());
            }
        });
        
        System.out.println("CommandNum: " + ui.commandNum);
        playMusic(0);
        setupGame();
    }
    
    // Initialize the game entities and state
    public void setupGame() {
        aSetter.setEnemy();
        aSetter.setEnemy2();
        aSetter.setEnemy3();
        gameState = titleState;
    }
    
    // Restart the game and reset relevant data
    public void retry() {
        ui.isScoreAdded = false;
        ui.historyDisplayed = false;
        gameState = playState;
        ui.showHighScoreNotification = false;
        ui.highScoreNotificationStartTime = 0;
        ui.isBlinkingVisible = true;
        fish1.scoreSoundPlayed = new boolean[900];
        fish1.HighScoreSoundPlayed = new boolean[900];
        aSetter.setLevel2EnemiesSpawned(false);
        aSetter.setLevel3EnemiesSpawned(false);
        aSetter.setLevel4EnemiesSpawned(false);
        fish1.setDefaultValues();
        resetScore();
        enemy = new Entity[900];
        
        aSetter.setEnemy();
        aSetter.setEnemy2();
        aSetter.setEnemy3();
        update();
    }
    
    // Spawn enemies for level 2 if conditions met
    public void spawnLevel2Enemies() {
        if (getScore() >= 3 && !aSetter.isLevel2EnemiesSpawned()) {
            aSetter.setEnemy4();
            aSetter.setEnemy5();
            aSetter.setEnemy6();
            aSetter.setEnemy7();
            aSetter.setLevel2EnemiesSpawned(true);
        }
    }
    
    // Spawn enemies for level 3 if conditions met
    public void spawnLevel3Enemies() {
        if (getScore() >= 8 && !aSetter.isLevel3EnemiesSpawned()) {
            aSetter.setEnemy8();
            aSetter.setEnemy9();
            aSetter.setEnemy10();
            aSetter.setEnemy11();
            aSetter.setLevel3EnemiesSpawned(true);
        }
    }
    
    // Spawn enemies for level 4 if conditions met
    public void spawnLevel4Enemies() {
        if (getScore() >= 16 && !aSetter.isLevel4EnemiesSpawned()) {
            aSetter.setEnemy12();
            aSetter.setEnemy13();
            aSetter.setEnemy14();
            aSetter.setLevel4EnemiesSpawned(true);
        }
    }
    
    // Starts the main game loop thread
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    // Main game loop
    @Override
    public void run() {
        double drawInterval = 1_000_000_000 / fps; // Nanoseconds per frame
        double nextDrawTime = System.nanoTime() + drawInterval;
        long timer = 0;
        int drawCount = 0;
        long currentTime;
        long lastTime = System.nanoTime();
        
        while (gameThread != null) {
            update();
            repaint();
            
            try {
                currentTime = System.nanoTime();
                timer += (currentTime - lastTime);
                lastTime = currentTime;
                
                double remainingTime = (nextDrawTime - System.nanoTime()) / 1_000_000;
                if (remainingTime < 0) remainingTime = 0;
                
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
                drawCount++;
                
                if (timer >= 1_000_000_000) {
                    System.out.println("FPS: " + drawCount);
                    drawCount = 0;
                    timer = 0;
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    // Update game logic depending on game state
    public void update() {
        if (gameState == playState) {
            fish1.update();
            Enemy1.handleEnemies(enemy, screenWidth, screenHeight);
            spawnLevel2Enemies();
            spawnLevel3Enemies();
            spawnLevel4Enemies();
        } else if (gameState == pauseState) {
            // Game is paused, no updates
        }
    }
    
    // Add entity to the entity list
    public void addEntity(Entity entity) {
        entityList.add(entity);
    }
    
    // Draw all entities in the list
    private void drawEntities(Graphics2D g2) {
        for (Entity entity : entityList) {
            entity.draw(g2);
        }
    }
    
    // Score-related methods
    public int getScore() {
        return score;
    }
    
    public void resetScore() {
        score = 0;
    }
    
    public void increaseScore(int points) {
        score += points;
    }
    
    // Update high score if current score is higher
    public void updateHighScore() {
        lastGameScore = score;
        if (score > highScore) {
            highScore = score;
        }
    }
    
    public int getHighScore() {
        return highScore;
    }
    
    public int getLastGameScore() {
        return lastGameScore;
    }
    
    // Handle player logout and reset necessary state
    public void handleLogout() {
        gameState = loginState; // Switch to login screen
        resetScore();
        loggedIn = false;
        currentUsername = null;
    }
    
    // Add score to history CSV (only once)
    public void addScoreToHistory() {
        if (!ui.isScoreAdded) {
            ui.addScoreToHistory(score); // Saves to CSV automatically
            ui.isScoreAdded = true;
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        if (gameState == titleState) {
            ui.draw(g2);
        } else {
            tileM.draw(g2);
            fish1.draw(g2);
            
            for (Entity e : enemy) {
                if (e != null) {
                    e.draw(g2);
                }
            }
            
            ui.draw(g2);
        }
        
        g2.dispose();
    }
    
    // Sound controls
    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }
    
    public void stopMusic(int i) {
        sound.setFile(i);
        sound.stop();
    }
    
    public void playSE(int i) {
        sound.setFile(i);
        sound.play();
    }
    
    // Return to login screen replacing current content pane
    public void returnToLogin() {
        parentFrame.getContentPane().removeAll();
        LoginFrame loginPanel = new LoginFrame(parentFrame);
        parentFrame.setContentPane(loginPanel.getLoginPanel());
        parentFrame.revalidate();
        parentFrame.repaint();
    }
    
    // Logout and restart the app with login screen
    public void logout() {
        stopMusic(0); // Stop music
        
        // Close current window
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
        
        // Reset state
        resetScore();
        gameState = titleState;
        ui.commandNum = 0;
        
        // Launch login frame
        SwingUtilities.invokeLater(() -> {
            JFrame loginFrame = new JFrame();
            LoginFrame loginPanel = new LoginFrame(loginFrame);
            loginFrame.setContentPane(loginPanel.getLoginPanel());
            loginFrame.pack();
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setVisible(true);
        });
    }
    
    // Getters for current user info
    public String getCurrentUsername() {
        return currentUsername;
    }

    public String getCurrentFullName() {
        return currentFullName;
    }
}
