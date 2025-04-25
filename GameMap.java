import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private Pane map;
    private LevelLoader levelLoader;
    private static final int TILE_SIZE = 50; // 每个格子的大小
    private List<Rectangle> groundTiles = new ArrayList<>();
    private List<Rectangle> waterTiles = new ArrayList<>();
    private List<Rectangle> lavaTiles = new ArrayList<>();
    private List<Rectangle> keys = new ArrayList<>();
    private List<ImageView> keyImages = new ArrayList<>();
    private List<Rectangle> buttons = new ArrayList<>();
    private Rectangle exitDoor;
    private ImageView exitDoorImage;

    private Image groundImg = new Image("assets/ground.png");
    private Image waterImg = new Image("assets/water.gif");
    private Image lavaImg = new Image("assets/lava.gif");
    private Image keyImg = new Image("assets/key.gif");
    private Image buttonImg = new Image("assets/button.png");
    private Image doorOpenImg = new Image("assets/dooro.png");
    private Image doorClosedImg = new Image("assets/doorc.png");
    

    public GameMap(String levelFile) {
        map = new Pane();
        levelLoader = new LevelLoader(levelFile);
        generateMap();
    }

    private void generateMap() {
        int[][] mapData = levelLoader.getMapData();
        for (int row = 0; row < levelLoader.getHeight(); row++) {
            for (int col = 0; col < levelLoader.getWidth(); col++) {
                int tile = mapData[row][col];
                ImageView tileImg = new ImageView();
                Rectangle rect = new Rectangle(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                rect.setOpacity(0); // 碰撞盒子透明化
                
                if (tile == 1) { // 地面
                    tileImg.setImage(groundImg);
                    groundTiles.add(rect);
                } else if (tile == 2) { // 水池
                    tileImg.setImage(waterImg);
                    waterTiles.add(rect);
                } else if (tile == 3) { // 熔岩
                    tileImg.setImage(lavaImg);
                    lavaTiles.add(rect);
                } else if (tile == 4) { // 门（默认关闭）
                    tileImg.setImage(doorClosedImg);
                    exitDoor = rect;
                    exitDoorImage = tileImg;
                } else if (tile == 5) { // 钥匙
                    tileImg.setImage(keyImg);
                    keys.add(rect);
                    keyImages.add(tileImg);
                } else if (tile == 6) { // 按钮
                    tileImg.setImage(buttonImg);
                    buttons.add(rect);
                } else {
                    continue; // 空地
                }

                tileImg.setFitWidth(TILE_SIZE);
                tileImg.setFitHeight(TILE_SIZE);
                tileImg.setX(col * TILE_SIZE);
                tileImg.setY(row * TILE_SIZE);


                map.getChildren().addAll(tileImg, rect);
            }
        }
    }

    public Pane getMap() {
        return map;
    }

    public List<Rectangle> getGroundTiles() {
        return groundTiles;
    }

    public List<Rectangle> getWaterTiles() {
        return waterTiles;
    }

    public List<Rectangle> getLavaTiles() {
        return lavaTiles;
    }

    public List<Rectangle> getKeys() {
        return keys;
    }
    public List<ImageView> getKeyImages() {
        return keyImages;
    }

    public List<Rectangle> getButtons() {
        return buttons;
    }

    public Rectangle getExitDoor() {
        return exitDoor;
    }

    public void openDoor() {
        if (exitDoorImage != null) {
            exitDoorImage.setImage(doorOpenImg); // 更新门的图片
        }
    }
}