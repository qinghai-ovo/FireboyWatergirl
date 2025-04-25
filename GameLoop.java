import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;

public class GameLoop extends AnimationTimer {
    private Player fireboy, watergirl;
    private GameMap gameMap;
    private Set<KeyCode> keysPressed = new HashSet<>();
    private long lastTime = System.nanoTime();
    private boolean doorUnlocked = false;
    private GameUI gameUI;
    private int keysCollected = 0;
    private FireboyWatergirl game;
    private boolean gameCompleted = false;

    public GameLoop(Player fireboy, Player watergirl, GameMap gameMap,Set<KeyCode> keysPressed,GameUI gameUI,FireboyWatergirl game) {
        this.fireboy = fireboy;
        this.watergirl = watergirl;
        this.gameMap = gameMap;
        this.keysPressed = keysPressed;
        this.gameUI = gameUI;
        this.game = game;
    }

    @Override
    public void handle(long now) {
        if (gameCompleted) return; // 避免重复触发关卡切换

        double deltaTime = (now - lastTime) / 1_000_000_000.0; // 计算时间差（秒）
        lastTime = now;

        if (keysPressed.contains(KeyCode.A)) {
            watergirl.moveLeft();
        }
        if (keysPressed.contains(KeyCode.D)) {
            watergirl.moveRight();
        }
        if (keysPressed.contains(KeyCode.W)) {
           watergirl.jump();
        }
        if (keysPressed.contains(KeyCode.LEFT)) {
            fireboy.moveLeft();
        }
        if (keysPressed.contains(KeyCode.RIGHT)) {
            fireboy.moveRight();
        }
        if (keysPressed.contains(KeyCode.UP)) {
            fireboy.jump();
        }
     
        
        // 处理重力
        fireboy.applyPhysics(deltaTime);
        watergirl.applyPhysics(deltaTime);

        fireboy.updateAnimation();
        watergirl.updateAnimation();

        // 碰撞检测
        checkCollisions(fireboy);
        checkCollisions(watergirl);
    }

    private void checkCollisions(Player player) {
        boolean onGround = false;
        boolean hitWall = false;
        boolean hitCeiling = false;

        // 脚底
        double nextY = player.getY() + player.getVelocityY();
        for (Rectangle ground : gameMap.getGroundTiles()) {
            if (player.getBoundsBottom().intersects(ground.getBoundsInParent()) && !hitCeiling) {
                if (player.getVelocityY() > 0) { // 角色正在下降
                    onGround = true;
                    nextY = ground.getY() - player.getFitHeight(); // 让角色站在地面上
                    player.setVelocityY(0); // 停止掉落
                    //System.out.println("Onground");
                }
            }
        }
        player.setY(nextY);
        player.setOnGround(onGround);

        //头顶
        for (Rectangle ceiling : gameMap.getGroundTiles()) {
            if (player.getBoundsTop().intersects(ceiling.getBoundsInParent())) {
                if (player.getVelocityY() < 0) { // 角色向上移动时，检测天花板碰撞
                    hitCeiling = true;
                    player.setY(ceiling.getY() + ceiling.getHeight()); // 确保不会穿过天花板
                    player.setVelocityY(0); // 停止上升
                    //System.out.println("hitCeiling");
                }
            }
        }

        //左右
        double nextX = player.getX() + player.getVelocityX();
        for (Rectangle wall : gameMap.getGroundTiles()) {
            if (player.getBoundsRight().intersects(wall.getBoundsInParent()) && player.getVelocityX() > 0) {
                    //System.out.println("右碰壁");
                    hitWall = true;
                    nextX = wall.getX() - player.getFitWidth(); // 让角色停在墙的左侧
                } 
            if(player.getBoundsLeft().intersects(wall.getBoundsInParent()) && player.getVelocityX() < 0) { // 碰到左侧墙壁
                //System.out.println("左碰壁");
                hitWall = true;
                nextX = wall.getX() + wall.getWidth(); // 让角色停在墙的右侧
            }
        }
        
        player.setX(nextX);
        if (hitWall) player.setVelocityX(0);

        

        

        

        // 处理钥匙拾取
        List<Rectangle> keysToRemove = new ArrayList<>();
        List<ImageView> keyImagesToRemove = new ArrayList<>();

        for (int i = 0; i < gameMap.getKeys().size(); i++) {
            Rectangle keyCollider = gameMap.getKeys().get(i);
            ImageView keyImage = gameMap.getKeyImages().get(i);

            if (player.getBoundsInParent().intersects(keyCollider.getBoundsInParent())) {
                keysToRemove.add(keyCollider);
                keyImagesToRemove.add(keyImage);
                keysCollected++;
                gameUI.updateKeys(keysCollected);
            }
        }

        gameMap.getKeys().removeAll(keysToRemove);
        gameMap.getKeyImages().removeAll(keyImagesToRemove);
        gameMap.getMap().getChildren().removeAll(keysToRemove);
        gameMap.getMap().getChildren().removeAll(keyImagesToRemove);

        // 检测触发按钮
        for (Rectangle button : gameMap.getButtons()) {
            if (player.getBoundsBottom().intersects(button.getBoundsInParent())) {
                doorUnlocked = true;
                gameUI.updateDoorStatus(true);
                //System.out.println("踩下了按钮，门已解锁！");
                gameMap.openDoor(); // 更新门的贴图
            }
        }
    

        // 检测掉入水池（火娃会死）     
        for (Rectangle water : gameMap.getWaterTiles()) {
            if (player.getBoundsRight().intersects(water.getBoundsInParent()) && player.getVelocityX() > 0) {
                //System.out.println("右碰壁");
                hitWall = true;
                nextX = water.getX() - player.getFitWidth(); // 让角色停在墙的左侧
            } 
            if(player.getBoundsLeft().intersects(water.getBoundsInParent()) && player.getVelocityX() < 0) { // 碰到左侧墙壁
                //System.out.println("左碰壁");
                hitWall = true;
                nextX = water.getX() + water.getWidth(); // 让角色停在墙的右侧
            }
            if (player.getBoundsBottom().intersects(water.getBoundsInParent())) {
                if (player.getVelocityY() > 0) { // 角色正在下降
                    onGround = true;
                    nextY = water.getY() - player.getFitHeight(); // 让角色站在地面上
                    player.setVelocityY(0); // 停止掉落
                }
            }
            if (player == fireboy)
            if (player.getBoundsInParent().intersects(water.getBoundsInParent())) {
                //System.out.println("火娃掉进水里，失败！");
                resetPlayer(player);
                return;
            }
        }
        player.setY(nextY);
        player.setOnGround(onGround);
        player.setX(nextX);
        if (hitWall) player.setVelocityX(0);
        

        // 检测掉入熔岩（冰娃会死）
        for (Rectangle lava : gameMap.getLavaTiles()) {
            if (player.getBoundsRight().intersects(lava.getBoundsInParent()) && player.getVelocityX() > 0) {
                //System.out.println("右碰壁");
                hitWall = true;
                nextX = lava.getX() - player.getFitWidth(); // 让角色停在墙的左侧
            } 
            if(player.getBoundsLeft().intersects(lava.getBoundsInParent()) && player.getVelocityX() < 0) { // 碰到左侧墙壁
                //System.out.println("左碰壁");
                hitWall = true;
                nextX = lava.getX() + lava.getWidth(); // 让角色停在墙的右侧
            }
            if (player.getBoundsBottom().intersects(lava.getBoundsInParent())) {
                if (player.getVelocityY() > 0) { // 角色正在下降
                    onGround = true;
                    nextY = lava.getY() - player.getFitHeight(); // 让角色站在地面上
                    player.setVelocityY(0); // 停止掉落
                }
            }
            if (player == watergirl){
                if (player.getBoundsInParent().intersects(lava.getBoundsInParent())) {
                    //System.out.println("冰娃掉进熔岩，失败！");
                    resetPlayer(player);
                    return;
                }
            }
        }
        player.setY(nextY);
        player.setOnGround(onGround);
        player.setX(nextX);
        if (hitWall) player.setVelocityX(0);


        // 检测是否到达终点
        if (doorUnlocked && player.getBoundsInParent().intersects(gameMap.getExitDoor().getBoundsInParent())) {
            //System.out.println( " 到达终点！");
            checkWinCondition();
        }
    }

    private void resetPlayer(Player player) {
        player.setX(50); // 复位
        player.setY(850);
        player.setVelocityX(0);
        player.setVelocityY(0);
    }
    
    private void checkWinCondition() {
        if (gameCompleted) return;
        if (fireboy.getBoundsInParent().intersects(gameMap.getExitDoor().getBoundsInParent()) &&
            watergirl.getBoundsInParent().intersects(gameMap.getExitDoor().getBoundsInParent())) {
            System.out.println("🎉 关卡完成！🎉");
            stop(); // 停止当前游戏循环
            gameCompleted = true; // 防止重复触发
            game.loadNextLevel(); // 进入下一关
            }
    }
}