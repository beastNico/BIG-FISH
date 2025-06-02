package enemy;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class Enemy3 extends Enemy1 {

    public Enemy3(GamePanel gp) {
        super(gp);

        name = "Enemy3";
        level = 3;
        speed = 8;
        maxLife = 1;
        life = maxLife;

        // Slightly scaled-up size for a different appearance
        width = (int) (32 * 1.5);
        height = (int) (25 * 1.5);

        getImage();
        updateCurrentImage();
    }

    @Override
    public void setAction() {
        // Simple horizontal movement
        if (movingLeft) {
            x -= speed;
        } else {
            x += speed;
        }

        // Reset if off-screen
        if (x < -width - 500 || x > gp.screenWidth + 500) {
            resetPosition();
        }
    }

    @Override
    public void getImage() {
        try {
            level3ImageLeft = ImageIO.read(getClass().getResourceAsStream("/Enemy/Fish3left.png"));
            level3ImageRight = ImageIO.read(getClass().getResourceAsStream("/Enemy/Fish3right.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCurrentImage() {
        // Update current image based on direction
        currentImage = movingLeft ? level3ImageLeft : level3ImageRight;
    }
}
