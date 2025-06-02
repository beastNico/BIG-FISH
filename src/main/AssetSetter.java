package main;

import enemy.Enemy1;
import enemy.Enemy2;
import enemy.Enemy3;
import enemy.Enemy4;
import java.util.Random;

public class AssetSetter {

    Random random = new Random();  // Random generator for spawn positions

    GamePanel gp;  // Reference to the main game panel

    // Flags to track if enemies for certain levels have been spawned already
    public boolean level2EnemiesSpawned = false;
    public boolean level3EnemiesSpawned = false;
    public boolean level4EnemiesSpawned = false;

    public AssetSetter(GamePanel gp){
        this.gp = gp;  // Initialize with game panel reference
    }

    // Getter and setter methods for level 2 enemies spawned flag
    public boolean isLevel2EnemiesSpawned() {
        return level2EnemiesSpawned;
    }
    public void setLevel2EnemiesSpawned(boolean level2EnemiesSpawned) {
        this.level2EnemiesSpawned = level2EnemiesSpawned;
    }

    // Getter and setter methods for level 3 enemies spawned flag
    public boolean isLevel3EnemiesSpawned() {
        return level3EnemiesSpawned;
    }
    public void setLevel3EnemiesSpawned(boolean level3EnemiesSpawned) {
        this.level3EnemiesSpawned = level3EnemiesSpawned;
    }

    // Getter and setter methods for level 4 enemies spawned flag
    public boolean isLevel4EnemiesSpawned() {
        return level4EnemiesSpawned;
    }
    public void setLevel4EnemiesSpawned(boolean level4EnemiesSpawned) {
        this.level4EnemiesSpawned = level4EnemiesSpawned;
    }

    /**
     * Returns a random spawn position.
     * x is either just off-screen to the left or right,
     * y is a random position within the screen height with a 30 pixel margin.
     * This is for the initial spawn; subsequent movement handled in Enemy1 class.
     */
    public int[] setSpawn() {                      
        int y = 30 + random.nextInt(gp.screenHeight - 30);  
        int x = random.nextInt(2);

        // Spawn enemy from either left (-50) or right (screenWidth + 50) side
        if (x == 0) {
            x = -50;  // Start off-screen left
        } else {
            x = gp.screenWidth + 50;  // Start off-screen right
        }
        int[] position = {x, y};
        return position;
    }

    // === LEVEL 1 ENEMIES ===

    public void setEnemy() {
        int[] spawnPosition = setSpawn();

        // Initialize enemy 0 as Enemy1 at random position
        gp.enemy[0] = new Enemy1(gp);
        gp.enemy[0].x = spawnPosition[0];  
        gp.enemy[0].y = spawnPosition[1];  
    }
    public void setEnemy2() {
        int[] spawnPosition = setSpawn();

        // Initialize enemy 1 as Enemy1 at random position
        gp.enemy[1] = new Enemy1(gp);
        gp.enemy[1].x = spawnPosition[0];  
        gp.enemy[1].y = spawnPosition[1];  
    }
    public void setEnemy3() {
        int[] spawnPosition = setSpawn();

        // Initialize enemy 2 as Enemy1 at random position
        gp.enemy[2] = new Enemy1(gp);
        gp.enemy[2].x = spawnPosition[0];  
        gp.enemy[2].y = spawnPosition[1];  
    }

    // === LEVEL 2 ENEMIES ===

    public void setEnemy4() {
        int[] spawnPosition = setSpawn();

        // Initialize enemy 3 as Enemy2 with adjusted solid area bounds
        gp.enemy[3] = new Enemy2(gp);
        gp.enemy[3].x = spawnPosition[0];
        gp.enemy[3].y = spawnPosition[1];
        gp.enemy[3].solidArea.setBounds(0, 0, 23*2, 11*2);
    }
    public void setEnemy5() {
        int[] spawnPosition = setSpawn();

        // Initialize enemy 4 as Enemy2 with adjusted solid area bounds
        gp.enemy[4] = new Enemy2(gp);
        gp.enemy[4].x = spawnPosition[0];
        gp.enemy[4].y = spawnPosition[1];
        gp.enemy[4].solidArea.setBounds(0, 0, 23*2, 11*2);
    }
    public void setEnemy6() {
        int[] spawnPosition = setSpawn();

        // Initialize enemy 5 as Enemy2 with adjusted solid area bounds
        gp.enemy[5] = new Enemy2(gp);
        gp.enemy[5].x = spawnPosition[0];
        gp.enemy[5].y = spawnPosition[1];
        gp.enemy[5].solidArea.setBounds(0, 0, 23*2, 11*2);
    }
    public void setEnemy7() {
        int[] spawnPosition = setSpawn();

        // Initialize enemy 6 as Enemy2 with adjusted solid area bounds
        gp.enemy[6] = new Enemy2(gp);
        gp.enemy[6].x = spawnPosition[0];
        gp.enemy[6].y = spawnPosition[1];
        gp.enemy[6].solidArea.setBounds(0, 0, 23*2, 11*2);
    }

    // === LEVEL 3 ENEMIES ===

    public void setEnemy8() {
        int[] spawnPosition = setSpawn();

        // Initialize enemy 7 as Enemy3 with adjusted solid area bounds
        gp.enemy[7] = new Enemy3(gp);
        gp.enemy[7].x = spawnPosition[0];
        gp.enemy[7].y = spawnPosition[1];
        gp.enemy[7].solidArea.setBounds(0, 0, (int) (32*1.5), (int) (25*1.5));
    }
    public void setEnemy9() {
        int[] spawnPosition = setSpawn();

        // Initialize enemy 8 as Enemy3 with adjusted solid area bounds
        gp.enemy[8] = new Enemy3(gp);
        gp.enemy[8].x = spawnPosition[0];
        gp.enemy[8].y = spawnPosition[1];
        gp.enemy[8].solidArea.setBounds(0, 0, (int) (32*1.5), (int) (25*1.5));
    }
    public void setEnemy10() {
        int[] spawnPosition = setSpawn();

        // Initialize enemy 9 as Enemy3 with adjusted solid area bounds
        gp.enemy[9] = new Enemy3(gp);
        gp.enemy[9].x = spawnPosition[0];
        gp.enemy[9].y = spawnPosition[1];
        gp.enemy[9].solidArea.setBounds(0, 0, (int) (32*1.5), (int) (25*1.5));
    }
    public void setEnemy11() {
        int[] spawnPosition = setSpawn();

        // Initialize enemy 10 as Enemy3 with adjusted solid area bounds
        gp.enemy[10] = new Enemy3(gp);
        gp.enemy[10].x = spawnPosition[0];
        gp.enemy[10].y = spawnPosition[1];
        gp.enemy[10].solidArea.setBounds(0, 0, (int) (32*1.5), (int) (25*1.5));
    }

    // === LEVEL 4 ENEMIES ===

    public void setEnemy12() {
        int[] spawnPosition = setSpawn();

        // Initialize enemy 11 as Enemy4 with adjusted solid area bounds
        gp.enemy[11] = new Enemy4(gp);
        gp.enemy[11].x = spawnPosition[0];
        gp.enemy[11].y = spawnPosition[1];
        gp.enemy[11].solidArea.setBounds(0, 0, 23*4, 11*4);
    }
    public void setEnemy13() {
        int[] spawnPosition = setSpawn();

        // Initialize enemy 12 as Enemy4 with adjusted solid area bounds
        gp.enemy[12] = new Enemy4(gp);
        gp.enemy[12].x = spawnPosition[0];
        gp.enemy[12].y = spawnPosition[1];
        gp.enemy[12].solidArea.setBounds(0, 0, 23*4, 11*4);
    }
    public void setEnemy14() {
        int[] spawnPosition = setSpawn();

        // Initialize enemy 13 as Enemy4 with adjusted solid area bounds
        gp.enemy[13] = new Enemy4(gp);
        gp.enemy[13].x = spawnPosition[0];
        gp.enemy[13].y = spawnPosition[1];
        gp.enemy[13].solidArea.setBounds(0, 0, 23*4, 11*4);
    }
}
