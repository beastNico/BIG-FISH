package enemy;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class Enemy4 extends Enemy1 {

    public Enemy4(GamePanel gp) {
        super(gp);

        name = "Enemy4";
        level = 4;
        speed = 10;
        maxLife = 1;
        life = maxLife;

        // Larger size for this enemy variant
        width = 23 * 4;
        height = 11 * 4;

        getImage();
        updateCurrentImage();
    }

    @Override
    public void setAction() {
        // Horizontal movement
        if (movingLeft) {
            x -= speed;
        } else {
            x += speed;
        }

        // Re-spawn if off-screen
        if (x < -width - 500 || x > gp.screenWidth + 500) {
            resetPosition();
        }
    }

    @Override
    public void getImage() {
        try {
            level4ImageLeft = ImageIO.read(getClass().getResourceAsStream("/Enemy/Enemy4left.png"));
            level4ImageRight = ImageIO.read(getClass().getResourceAsStream("/Enemy/Enemy4right.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCurrentImage() {
        currentImage = movingLeft ? level4ImageLeft : level4ImageRight;
    }
}
