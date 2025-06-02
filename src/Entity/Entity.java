package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;


public class Entity {
    
    
    public GamePanel gp;
    
    public int x, y, width, height;
    public int speed;
    
    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    
    public BufferedImage left, right, up, down;
    public String direction;
    public boolean collisionOn;
    public int startX = 0;
    public int startY = 0;
    public int tileSizeWidth = 16;
    public int tileSizeHeight = 16;
    
    boolean attacking = false;
    
    //PLAYER STATUS
    public int maxLife;
    public int life;
    
    public String name;
    public boolean movingLeft;
        
    
    public Entity(GamePanel gp) {
        this.gp = gp;
        this.direction = "right";
        
        this.solidArea = new Rectangle(0, 0, 23, 11); // Default width and height
        
    }
    public void setAction() {
    }
    public void update() {
        setAction();
        gp.collisionChecker.checkEntity(this, gp.enemy);
//        gp.collisionChecker.checkPlayer(this);
        collisionOn = false;
    }

    public void draw(Graphics2D g2) {
    if (direction == null) {
        System.out.println("Direction is null. Cannot determine image.");
        return;
    }

    BufferedImage image = switch (direction) {
        case "left" -> left;
        case "right" -> right;
        default -> null;
    };

    if (image != null) {
        g2.drawImage(image, x, y, width, height, null);
    }
}
}
