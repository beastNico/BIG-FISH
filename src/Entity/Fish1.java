package Entity;

import enemy.Enemy1;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;
import java.awt.Rectangle;

public class Fish1 extends Entity {

    KeyHandler keyH;
    public boolean[] scoreSoundPlayed = new boolean[900];
    public boolean[] HighScoreSoundPlayed = new boolean[900];

    public Fish1(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;

        Enemy1 enemy1 = new Enemy1(gp); // Create one enemy instance

        Rectangle solidArea = new Rectangle(16, 16);
        solidArea.x = 48;
        solidArea.y = 20;

        int solidAreaDefaultX = solidArea.x;
        int solidAreaDefaultY = solidArea.y;

        attackArea.width = 48;
        attackArea.height = 20;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4;
        direction = "right";

        maxLife = 1;
        life = maxLife;

        attackArea.width = 48;
        attackArea.height = 20;
    }

    public void getPlayerImage() {
        try {
            left = ImageIO.read(getClass().getResourceAsStream("/Fish/Fish4Left.png"));
            right = ImageIO.read(getClass().getResourceAsStream("/Fish/Fish4Right.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (attacking = true) {
            attacking();
        }

        // Handle movement with boundary constraints
        if (keyH.upPressed && y - speed >= 30) {
            y -= speed;
        }

        if (keyH.leftPressed) {
            direction = "left";
            if (x - speed >= -5) {
                x -= speed;
            }
        }

        if (keyH.downPressed && y + speed + gp.tileSize <= gp.screenHeight + 45) {
            y += speed;
        }

        if (keyH.rightPressed) {
            direction = "right";
            if (x + speed + gp.tileSize <= gp.screenWidth + 20) {
                x += speed;
            }
        }

        collisionOn = false;

        // Trigger game over if life is depleted
        if (life <= 0) {
            gp.updateHighScore();
            gp.lastGameScore = gp.getScore();
            gp.playSE(2);
            gp.gameState = gp.gameOverState;
        }

        levelUp();
        newHighScore();
    }

    public void levelUp() {
        int currentScore = gp.getScore();

        if ((currentScore == 5 || currentScore == 10 || currentScore == 20) && !scoreSoundPlayed[currentScore]) {
            gp.playSE(3);
            scoreSoundPlayed[currentScore] = true;

            // Expand attack area on score milestones
            if (currentScore == 5) {
                attackArea.width = (int) (48 * 1.5);
                attackArea.height = (int) (20 * 1.5);
            } else if (currentScore == 10) {
                attackArea.width = (int) (48 * 1.8);
                attackArea.height = (int) (20 * 1.8);
            } else if (currentScore == 20) {
                attackArea.width = (int) (48 * 2);
                attackArea.height = (int) (20 * 2);
            }
        }
    }

    public void newHighScore() {
        int currentScore = gp.getScore();

        if ((currentScore - 1 == gp.getHighScore() && currentScore > 10) && !HighScoreSoundPlayed[currentScore]) {
            gp.playSE(4);
            HighScoreSoundPlayed[currentScore] = true;
        }
    }

    public void touchingEnemy(int i) {
        if (i != 999) {
            System.out.println("Touching!");
            attacking = true;
        }
    }

    public void contactEnemy(int i) {
        if (i != 999) {
            life -= 1;
            System.out.println(life);
        }
    }

    public void playerEnemy(boolean x) {
        if (x) {
            life -= 1;
            System.out.println(life);
        }
    }

    public void attacking() {
        int solidAreaWidth = solidArea.width;
        int solidAreaHeight = solidArea.height;

        // Temporarily expand collision area for attack
        solidArea.width = attackArea.width;
        solidArea.height = attackArea.height;

        int enemyIndex = gp.collisionChecker.checkEntity(this, gp.enemy);
        int hit2Index = gp.collisionChecker.checkEntity2(this, gp.enemy);
        int hit3Index = gp.collisionChecker.checkEntity3(this, gp.enemy);
        int hit4Index = gp.collisionChecker.checkEntity4(this, gp.enemy);

        if (gp.getScore() < 5) {
            damagePlayer(hit2Index);
        }

        if (gp.getScore() < 10) {
            damagePlayer(hit3Index);
        }

        if (gp.getScore() < 20) {
            damagePlayer(hit4Index);
        }

        damageEnemy(enemyIndex);
    }

    public void damageEnemy(int i) {
        if (i != 999) {
            System.out.println("HIT!");
            gp.increaseScore(1);
            gp.enemy[i].life -= 1;
            gp.playSE(1);

            if (gp.enemy[i].life <= 0) {
                gp.enemy[i] = null;

                // Respawn enemy using appropriate asset setter
                switch (i) {
                    case 0: gp.aSetter.setEnemy(); break;
                    case 1: gp.aSetter.setEnemy2(); break;
                    case 2: gp.aSetter.setEnemy3(); break;
                    case 3: gp.aSetter.setEnemy4(); break;
                    case 4: gp.aSetter.setEnemy5(); break;
                    case 5: gp.aSetter.setEnemy6(); break;
                }
            }
        }
    }

    public void damagePlayer(int i) {
        if (i != 999) {
            System.out.println("HIT!");
            life -= 1;
            gp.increaseScore(-1);
            gp.enemy[i].life -= 1;
            gp.playSE(2);
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "left":
                image = left;
                break;
            case "right":
                image = right;
                break;
        }

        if (image != null) {
            g2.drawImage(image, x, y, attackArea.width, attackArea.height, null);
        }
    }
}
