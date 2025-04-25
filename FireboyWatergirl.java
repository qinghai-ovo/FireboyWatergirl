import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;



public class FireboyWatergirl extends Application {
    private Stage primaryStage;
    private Player fireboy, watergirl;
    private GameMap gameMap;
    private GameUI gameUI;
    private List<String> levels = Arrays.asList("levels/level1.txt", "levels/level2.txt", "levels/level3.txt");
    private int currentLevelIndex = 0;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        loadLevel(currentLevelIndex);
    }
    
    private void loadLevel(int levelIndex){
        if(levelIndex >= levels.size()){
            showWinScreen();
            return;
        }

        Pane root = new Pane();
        Scene scene = new Scene(root, 1600, 900);//32*18

        // 创建地图
        gameMap = new GameMap(levels.get(levelIndex));
        root.getChildren().add(gameMap.getMap());

        
        // 创建角色
        fireboy = new Player(true, 50,850);
        watergirl = new Player(false, 100,850);
        root.getChildren().addAll(fireboy, watergirl);

        //keyPressed or Released
        Set<KeyCode> keysPressed = new HashSet<>();
        scene.setOnKeyPressed(event -> keysPressed.add(event.getCode()));
        scene.setOnKeyReleased(event -> keysPressed.remove(event.getCode()));
        
        // 创建 UI
        gameUI = new GameUI(root, levelIndex + 1);

        // 启动游戏循环   
        GameLoop gameLoop = new GameLoop(fireboy, watergirl, gameMap, keysPressed,gameUI, this);
        gameLoop.start();
        

        primaryStage.setTitle("FireboyWatergirl");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void loadNextLevel() {
        System.out.println(currentLevelIndex);
        if (currentLevelIndex < levels.size()) {
            currentLevelIndex++;
            loadLevel(currentLevelIndex);
            System.out.println("load"+currentLevelIndex);
        } else {
            showWinScreen(); // 显示通关界面
        }
    }

    private void showWinScreen() {
        Pane winLayout = new Pane();
        winLayout.setStyle("-fx-background-color: #DCDCDC;");
    
        Scene winScene = new Scene(winLayout, 1600, 900);
    
        // 背景矩形（增加半透明层，提升可读性）
        javafx.scene.shape.Rectangle bg = new javafx.scene.shape.Rectangle(1600, 900);
        bg.setFill(javafx.scene.paint.Color.BLACK);
        bg.setOpacity(0.5);
    
        // 标题
        javafx.scene.control.Label winLabel = new javafx.scene.control.Label("Congratulations! ");
        winLabel.setStyle("-fx-font-size: 50px; -fx-text-fill: white; -fx-font-weight: bold;");
        winLabel.setLayoutX(600);
        winLabel.setLayoutY(250);
    
        // 返回主菜单按钮
        javafx.scene.control.Button backToMenu = new javafx.scene.control.Button("Back to Main Menu");
        backToMenu.setStyle("-fx-font-size: 24px; -fx-padding: 10px 20px; -fx-background-color: #555555; -fx-text-fill: white;");
        backToMenu.setLayoutX(700);
        backToMenu.setLayoutY(400);
        backToMenu.setOnAction(e -> {
            MainMenu menu = new MainMenu();
            menu.start(primaryStage);
        });
    
        // 添加到界面
        winLayout.getChildren().addAll(bg, winLabel, backToMenu);
        primaryStage.setScene(winScene);
    }
        

    public static void main(String[] args) {
        launch(args);
    }
}