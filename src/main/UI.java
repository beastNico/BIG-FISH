package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class UI {
    
    GamePanel gp;
    Graphics2D g2;
    Font arial_20, arial_40;
    public int commandNum = 0;
    public boolean[] HighScoreSoundPlayed = new boolean[900];
    public boolean showHighScoreNotification = false;
    public long highScoreNotificationStartTime = 0; 
    public boolean isBlinkingVisible = true;
    public List<Integer> gameHistory = new ArrayList<>();
    public boolean historyDisplayed = false;
    public boolean isScoreAdded = false;
    
    private BufferedImage backgroundImage;
    private boolean bgImageLoaded = false;
    
    private BufferedImage logoImage;
    private boolean logoImageLoaded = false;
    
    // Button rectangles for mouse click detection
    private Rectangle playButton = new Rectangle();
    private Rectangle historyButton = new Rectangle();
    private Rectangle quitButton = new Rectangle();
    private Rectangle logoutButton = new Rectangle();
    private Rectangle backFromHistoryButton = new Rectangle();

    public List<Integer> getGameHistory() {
        return gameHistory;
    }
    
    public UI(GamePanel gp) {
        this.gp = gp;
        arial_20 = new Font("Arial", Font.PLAIN, 20);
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        loadHistoryFromFile();
        
        try {
            backgroundImage = ImageIO.read(new File("src/res/Tiles/bg.png")); 
            bgImageLoaded = true;
        } catch (IOException e) {
            System.out.println("Could not load background image: " + e.getMessage());
            bgImageLoaded = false;
        }
        
        try {
            logoImage = ImageIO.read(new File("src/res/Tiles/title name.png")); 
            logoImageLoaded = true;
        } catch (IOException e) {
            System.out.println("Could not load logo image: " + e.getMessage());
            logoImageLoaded = false;
        }
    }
    
    public void handleEscapeKey() {
        if (gp.gameState == gp.playState || gp.gameState == gp.pauseState) {
            gp.gameState = gp.titleState;
            commandNum = 0;
        }
    }
    
    public void checkMouseClick(int mouseX, int mouseY) {
        if (gp.gameState == gp.titleState) {
            checkTitleScreenClick(mouseX, mouseY);
        } else if (commandNum == 1 && gp.gameState == gp.titleState) {
            checkHistoryScreenClick(mouseX, mouseY);
        }
    }
    
    public void checkTitleScreenClick(int mouseX, int mouseY) {
        if (playButton.contains(mouseX, mouseY)) {
            commandNum = 0;
            gp.gameState = gp.playState;
        } else if (historyButton.contains(mouseX, mouseY)) {
            commandNum = 1;
        } else if (logoutButton.contains(mouseX, mouseY)) {
            commandNum = 2;
            gp.logout();
        } else if (quitButton.contains(mouseX, mouseY)) {
            commandNum = 3;
            System.exit(0);
        }
    }
    
    public void checkHistoryScreenClick(int mouseX, int mouseY) {
        if (backFromHistoryButton.contains(mouseX, mouseY)) {
            commandNum = 0;
        }
    }
    
    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(arial_20);
        g2.setColor(Color.WHITE);

        String scoreText = "Score: " + gp.getScore();
        String highScoreText = "HighScore: " + gp.getHighScore();

        g2.drawString(scoreText, 20, 20);

        int highScoreWidth = (int)g2.getFontMetrics().getStringBounds(highScoreText, g2).getWidth();
        g2.drawString(highScoreText, gp.screenWidth - highScoreWidth - 40, 20);
        
        if(gp.gameState == gp.titleState) {
            gp.resetScore();
            if(commandNum == 1) {
                drawGameHistory();
            } else {
                drawTitleScreen();
            }
        }
    
        if(gp.gameState == gp.playState) {
            drawGameNotifications();
            drawGameInstructions();
            newHighScore();
        }
    
        if(gp.gameState == gp.pauseState) {
            g2.setFont(arial_40);
            g2.setColor(Color.WHITE);
            drawPauseScreen();
            drawGameInstructions();
        }
    
        if(gp.gameState == gp.gameOverState) {
            drawGameOverScreen();
            gp.addScoreToHistory();
        }
    }
    
    public void drawGameInstructions() {
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.setColor(Color.WHITE);
    
        int yPos1 = gp.screenHeight - 65;
        int yPos2 = gp.screenHeight - 45;
    
        g2.drawString("P - Pause/Resume", 10, yPos1);
        g2.drawString("ESC - Main Menu", 10, yPos2);
    }
    
    public void drawTitleScreen() {
        if(bgImageLoaded && backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        } else {
            g2.setColor(new Color(10, 30, 50));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            
            for (int i = 0; i < gp.screenHeight; i++) {
                float ratio = (float) i / gp.screenHeight;
                int red = (int) (10 + ratio * 20);
                int green = (int) (30 + ratio * 40);
                int blue = (int) (50 + ratio * 80);
                g2.setColor(new Color(red, green, blue));
                g2.drawLine(0, i, gp.screenWidth, i);
            }
        }
        
        int x = gp.screenWidth/2;
        int y = (int) (gp.tileSize * 0.75);
        
        if(logoImageLoaded && logoImage != null) {
            int logoWidth = logoImage.getWidth();
            int logoHeight = logoImage.getHeight();
            x = (gp.screenWidth - logoWidth) / 2;
            g2.drawImage(logoImage, x, y, null);
        } else {
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 90f));
            String text = "BIG FISH";
            x = getXforCenteredText(text);
            
            g2.setColor(Color.BLACK);
            g2.drawString(text, x+5, y+5);
            g2.setColor(Color.WHITE);
            g2.drawString(text, x, y);
        }
        
        x = gp.screenWidth/2 - 100;
        y += gp.tileSize*1.5;
        g2.drawImage(gp.fish1.right, x ,y, 48*4, 20*4, null);
        
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 36f));
        
        String text = "Play";
        x = getXforCenteredText(text);
        y += gp.tileSize*3.5;
        
        int buttonWidth = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth() + 80;
        int buttonHeight = 50;
        playButton.setBounds(x - 40, y - 35, buttonWidth, buttonHeight);
        
        if(commandNum == 0) {
            g2.setColor(new Color(255, 255, 255, 50));
            g2.fillRoundRect(playButton.x, playButton.y, playButton.width, playButton.height, 10, 10);
            g2.setColor(Color.WHITE);
            g2.drawRoundRect(playButton.x, playButton.y, playButton.width, playButton.height, 10, 10);
        } else {
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRoundRect(playButton.x, playButton.y, playButton.width, playButton.height, 10, 10);
        }
        
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);
        if(commandNum == 0) {
            g2.drawString(">", x-40, y);
        }
        
        text = "Game History";
        x = getXforCenteredText(text);
        y += gp.tileSize-10;
        
        buttonWidth = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth() + 80;
        historyButton.setBounds(x - 40, y - 35, buttonWidth, buttonHeight);
        
        if(commandNum == 1) {
            g2.setColor(new Color(255, 255, 255, 50));
            g2.fillRoundRect(historyButton.x, historyButton.y, historyButton.width, historyButton.height, 10, 10);
            g2.setColor(Color.WHITE);
            g2.drawRoundRect(historyButton.x, historyButton.y, historyButton.width, historyButton.height, 10, 10);
        } else {
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRoundRect(historyButton.x, historyButton.y, historyButton.width, historyButton.height, 10, 10);
        }
        
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.drawString(">", x-40, y);
        }
        
        text = "Logout";
        x = getXforCenteredText(text);
        y += gp.tileSize - 10;

        buttonWidth = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth() + 80;
        logoutButton.setBounds(x - 40, y - 35, buttonWidth, buttonHeight);

        if(commandNum == 2) {
            g2.setColor(new Color(255, 255, 255, 50));
            g2.fillRoundRect(logoutButton.x, logoutButton.y, logoutButton.width, logoutButton.height, 10, 10);
            g2.setColor(Color.WHITE);
            g2.drawRoundRect(logoutButton.x, logoutButton.y, logoutButton.width, logoutButton.height, 10, 10);
        } else {
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRoundRect(logoutButton.x, logoutButton.y, logoutButton.width, logoutButton.height, 10, 10);
        }

        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);
        if(commandNum == 2) {
            g2.drawString(">", x - 40, y);
        }

        text = "Quit";
        x = getXforCenteredText(text);
        y += gp.tileSize - 10;

        buttonWidth = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth() + 80;
        quitButton.setBounds(x - 40, y - 35, buttonWidth, buttonHeight);

        if(commandNum == 3) {
            g2.setColor(new Color(255, 255, 255, 50));
            g2.fillRoundRect(quitButton.x, quitButton.y, quitButton.width, quitButton.height, 10, 10);
            g2.setColor(Color.WHITE);
            g2.drawRoundRect(quitButton.x, quitButton.y, quitButton.width, quitButton.height, 10, 10);
        } else {
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRoundRect(quitButton.x, quitButton.y, quitButton.width, quitButton.height, 10, 10);
        }

        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);
        if(commandNum == 3) {
            g2.drawString(">", x - 40, y);
        }
    }
    
    public void drawPauseScreen() {
        g2.setColor(new Color(0,0,0,150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight/2;
        
        g2.setColor(Color.black);
        g2.drawString(text, x, y);
        g2.setColor(Color.white);
        g2.drawString(text, x-4, y-4);    
    }
    
    public void drawGameNotifications() {
        if (gp.getScore() == 3) {
            String text = "Enemy level 2 has spawned!";
            int x = getXforCenteredText(text);
            int y = gp.screenHeight/2;
            g2.drawString(text, x, y); 
        }
        if (gp.getScore() == 5) {
            String text = "You can eat enemy level 2 now!";
            int x = getXforCenteredText(text);
            int y = gp.screenHeight/2;
            g2.drawString(text, x, y); 
        }
        if (gp.getScore() == 8) {
            String text = "Enemy level 3 has spawned!";
            int x = getXforCenteredText(text);
            int y = gp.screenHeight/2;
            g2.drawString(text, x, y); 
        }
        if (gp.getScore() == 10) {
            String text = "You can eat enemy level 3 now!";
            int x = getXforCenteredText(text);
            int y = gp.screenHeight/2;
            g2.drawString(text, x, y); 
        }
        if (gp.getScore() == 16) {
            g2.setColor(Color.RED);
            String text = "Enemy final level has spawned!";
            int x = getXforCenteredText(text);
            int y = gp.screenHeight/2;
            g2.drawString(text, x, y); 
        }
        if (gp.getScore() >= 0 && gp.getScore() < 5) {
            g2.setColor(Color.white);
            String text = "LEVEL 1";
            int x = getXforCenteredText(text);
            int y = 20;
            g2.drawString(text, x, y); 
        }
        if (gp.getScore() >= 5 && gp.getScore() < 10) {
            g2.setColor(Color.white);
            String text = "LEVEL 2";
            int x = getXforCenteredText(text);
            int y = 20;
            g2.drawString(text, x, y); 
        }
        if (gp.getScore() >= 10 && gp.getScore() <= 20) {
            g2.setColor(Color.white);
            String text = "LEVEL 3";
            int x = getXforCenteredText(text);
            int y = 20;
            g2.drawString(text, x, y); 
        }
        if (gp.getScore() >= 21) {
            g2.setColor(Color.white);
            String text = "FINAL LEVEL";
            int x = getXforCenteredText(text);
            int y = 20;
            g2.drawString(text, x, y); 
        }
    }
    
    public int getXforCenteredText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }
    
    public void drawGameOverScreen() {
        g2.setColor(new Color(0,0,0,225));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(20f));
        
        text = "Last score: " + gp.getLastGameScore();
        x = getXforCenteredText(text);
        y = gp.tileSize*4;
        g2.drawString(text,x,y);
        g2.setColor(Color.green);
        g2.drawString(text, x, y+65+50);
        
        int lastScore = gp.getLastGameScore();
        if (lastScore >= 0 && lastScore <= 5) {
            g2.setColor(Color.green);
            text = "Last level: 1";
            x = getXforCenteredText(text);
            y = gp.tileSize*4;
            g2.drawString(text, x, y+40+50); 
        }
        if (lastScore >= 6 && lastScore <= 10) {
            g2.setColor(Color.green);
            text = "Last level: 2";
            x = getXforCenteredText(text);
            y = gp.tileSize*4;
            g2.drawString(text, x, y+40+50); 
        }
        if (lastScore >= 11 && lastScore <= 20) {
            g2.setColor(Color.green);
            text = "Last level: 3";
            x = getXforCenteredText(text);
            y = gp.tileSize*4;
            g2.drawString(text, x, y+40+50); 
        }
        if (lastScore >= 21) {
            g2.setColor(Color.RED);
            text = "Last level: FINAL LEVEL";
            x = getXforCenteredText(text);
            y = gp.tileSize*4;
            g2.drawString(text, x, y+40+50); 
        }
        
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));
        text = "Game Over";
        g2.setColor(Color.black);
        x = getXforCenteredText(text);
        y = gp.tileSize*4;
        g2.drawString(text,x,y);
        g2.setColor(Color.white);
        g2.drawString(text, x-4, y-4);
        
        g2.setFont(g2.getFont().deriveFont(50f));
        text = "Retry";
        x = getXforCenteredText(text);
        y += gp.tileSize*4;
        g2.drawString(text,x,y);
        if (commandNum == 0) {
            g2.drawString(">", x-40, y);
        }
        
        text = "Back to Title Screen";
        x = getXforCenteredText(text);
        y += 65;
        g2.drawString(text,x,y);
        if (commandNum == 1) {
            g2.drawString(">", x-40, y);
        }
    }
    
    public void newHighScore() {
        if (gp.gameState != gp.playState) return;

        if ((gp.getScore()-1 == gp.getHighScore() && gp.getScore() > 10) && !HighScoreSoundPlayed[gp.getScore()]) {
            if (!showHighScoreNotification) {
                showHighScoreNotification = true;
                highScoreNotificationStartTime = System.currentTimeMillis();
                HighScoreSoundPlayed[gp.getScore()] = true;
            }
        }

        if (showHighScoreNotification) {
            long elapsedTime = System.currentTimeMillis() - highScoreNotificationStartTime;

            if (elapsedTime < 5000) {
                isBlinkingVisible = (elapsedTime / 500) % 2 == 0;

                if (isBlinkingVisible) {
                    g2.setFont(g2.getFont().deriveFont(30f));
                    g2.setColor(Color.white);
                    String text = "New HighScore Achieved!";
                    int x = getXforCenteredText(text);
                    int y = gp.screenHeight / 2;
                    g2.drawString(text, x, y - 100);
                }
            } else {
                showHighScoreNotification = false;
            }
        }
    }
        
    public void drawGameHistory() {
        g2.setColor(new Color(0,0,0,225));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
       
        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(20f));
        g2.setColor(Color.WHITE);
        
        text = "Last 5 Game History";
        x = getXforCenteredText(text);
        y = gp.screenHeight / 3;
        g2.drawString(text,x,y);
        
        text = "HighScore : " + gp.getHighScore();
        x = getXforCenteredText(text);
        y = gp.screenHeight / 3 + 30;
        g2.drawString(text,x,y); 

        y += 60;
        
        if(gameHistory.isEmpty()) {
            text = "No games played yet";
            x = getXforCenteredText(text);
            g2.drawString(text, x, y);
        } else {
            int startIndex = Math.max(0, gameHistory.size() - 5);
            int gameNumber = 1;
            
            for (int i = startIndex; i < gameHistory.size(); i++) {
                text = gameNumber + ". Score: " + gameHistory.get(i);
                x = getXforCenteredText(text);
                g2.drawString(text, x, y);
                y += 30;
                gameNumber++;
            }
        }
        
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24f));
        text = "← Back to Menu";
        int backButtonWidth = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth() + 40;
        int backButtonHeight = 40;
        x = getXforCenteredText(text);
        y += 60;
        
        backFromHistoryButton.setBounds(x - 20, y - 30, backButtonWidth, backButtonHeight);
        
        g2.setColor(new Color(0, 100, 0, 150));
        g2.fillRoundRect(backFromHistoryButton.x, backFromHistoryButton.y, 
                       backFromHistoryButton.width, backFromHistoryButton.height, 10, 10);
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(backFromHistoryButton.x, backFromHistoryButton.y, 
                       backFromHistoryButton.width, backFromHistoryButton.height, 10, 10);
        
        g2.drawString(text, x, y);
    }

    public void addScoreToHistory(int score) {
        gameHistory.add(score);
        
        while (gameHistory.size() > 5) {
            gameHistory.remove(0);
        }
        
        saveHistoryToFile();
    }
    
    private void saveHistoryToFile() {
        try (FileWriter writer = new FileWriter("game_history.txt")) {
            for (Integer score : gameHistory) {
                writer.write(score + "\n");
            }
        } catch (IOException e) {
            System.out.println("Could not save game history: " + e.getMessage());
        }
    }

    private void loadHistoryFromFile() {
        gameHistory.clear();
        File file = new File("game_history.txt");
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextInt()) {
                    gameHistory.add(scanner.nextInt());
                }
            } catch (IOException e) {
                System.out.println("Could not load game history: " + e.getMessage());
            }
        }
    }
}