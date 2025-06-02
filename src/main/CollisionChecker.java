package main;

import Entity.Entity;
import Entity.Fish1;
import enemy.Enemy1;

public class CollisionChecker {
    
    GamePanel gp;  // Reference to the main game panel
    
    Enemy1 enemy1 = new Enemy1(gp);  // Instance of Enemy1 (not used in current methods)
    
    // Constructor to initialize with GamePanel reference
    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * Checks collision between an entity and an array of target entities.
     * Returns the index of the collided target, or 999 if none.
     * Checks full range of targets (all).
     */
    public int checkEntity(Entity entity, Entity[] target) {
        
        int index = 999;  // Default no-collision index
        
        for(int i = 0; i < target.length; i++) {
            
            if(target[i] != null) {
                // Update entity's solid area position to world coordinates
                entity.solidArea.x = entity.x + entity.solidArea.x;
                entity.solidArea.y = entity.y + entity.solidArea.y;
                // Update target's solid area position to world coordinates
                target[i].solidArea.x = target[i].x + target[i].solidArea.x;
                target[i].solidArea.y = target[i].y + target[i].solidArea.y;
                
                // Check collision based on entity's movement direction
                switch(entity.direction) {
                    case "left":
                        // Simulate entity moving left
                        entity.solidArea.x -= entity.speed;
                        // Check if solid areas intersect after movement
                        if(entity.solidArea.intersects(target[i].solidArea)) {
                            entity.collisionOn = true;  // Collision detected
                            index = i;  // Record index of collided target
                        }
                        break;
                    case "right":
                        // Simulate entity moving right
                        entity.solidArea.x += entity.speed;
                        if(entity.solidArea.intersects(target[i].solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                }
                
                // Reset solid area positions to default relative offsets
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }
        }
        
        return index;
    }
    
    /**
     * Checks collision between an entity and target entities from index 3 to 6 inclusive.
     * Used to check collisions in a specific enemy subset.
     */
    public int checkEntity2(Entity entity, Entity[] target) {
        
        int index = 999;
        
        for(int i = 3; i <= 6; i++) {
            if(target[i] != null) {
                // Update positions to world coordinates for collision checking
                entity.solidArea.x = entity.x + entity.solidArea.x;
                entity.solidArea.y = entity.y + entity.solidArea.y;
                target[i].solidArea.x = target[i].x + target[i].solidArea.x;
                target[i].solidArea.y = target[i].y + target[i].solidArea.y;
                
                switch(entity.direction) {
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if(entity.solidArea.intersects(target[i].solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if(entity.solidArea.intersects(target[i].solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                }
                
                // Reset solid area positions
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }
        }
        
        return index;
    }
    
    /**
     * Checks collision between an entity and target entities from index 7 to 10 inclusive.
     * Used for another subset of enemies.
     */
    public int checkEntity3(Entity entity, Entity[] target) {
        
        int index = 999;
        
        for(int i = 7; i <= 10 ; i++) {
            if(target[i] != null) {
                // Update positions for collision calculation
                entity.solidArea.x = entity.x + entity.solidArea.x;
                entity.solidArea.y = entity.y + entity.solidArea.y;
                target[i].solidArea.x = target[i].x + target[i].solidArea.x;
                target[i].solidArea.y = target[i].y + target[i].solidArea.y;
                
                switch(entity.direction) {
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if(entity.solidArea.intersects(target[i].solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if(entity.solidArea.intersects(target[i].solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                }
                
                // Reset solid areas to default offsets
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }
        }
        
        return index;
    }
    
    /**
     * Checks collision between an entity and target entities from index 11 to 13 inclusive.
     * Used for yet another subset of enemies.
     */
    public int checkEntity4(Entity entity, Entity[] target) {
        
        int index = 999;
        
        for(int i = 11; i <= 13 ; i++) {
            if(target[i] != null) {
                // Update solid area positions for collision check
                entity.solidArea.x = entity.x + entity.solidArea.x;
                entity.solidArea.y = entity.y + entity.solidArea.y;
                target[i].solidArea.x = target[i].x + target[i].solidArea.x;
                target[i].solidArea.y = target[i].y + target[i].solidArea.y;
                
                switch(entity.direction) {
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if(entity.solidArea.intersects(target[i].solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if(entity.solidArea.intersects(target[i].solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                }
                
                // Reset solid area positions to defaults
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }
        }
        
        return index;
    }
}
