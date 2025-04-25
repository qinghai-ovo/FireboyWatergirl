import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class GameUI {
    private Label timeLabel, keyLabel, doorLabel, levelLabel;
    private int secondsElapsed = 0;
    private int collectedKeys = 0;
    private boolean doorUnlocked = false;

    public GameUI(Pane root, int levelNumber) {
        setupUI(root, levelNumber);
    }

    private void setupUI(Pane root, int levelNumber) {
        // 添加背景矩形，提高可读性
        Rectangle bg = new Rectangle(200, 100);
        bg.setFill(Color.BLACK);
        bg.setOpacity(0.5);
        bg.setLayoutX(5);
        bg.setLayoutY(5);

        // 关卡提示（左上角）
        levelLabel = new Label("Level: " + levelNumber);
        levelLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
        levelLabel.setLayoutX(15);
        levelLabel.setLayoutY(10);

        // 挑战时长（屏幕中间正上方）
        timeLabel = new Label("Time: 0 S");
        timeLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-background-color: rgba(0, 0, 0, 0.5);");
        timeLabel.setLayoutX(750);
        timeLabel.setLayoutY(10);

        // 钥匙数量（左上角）
        keyLabel = new Label("Keys: 0");
        keyLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: yellow; -fx-background-color: rgba(0, 0, 0, 0.5);");
        keyLabel.setLayoutX(15);
        keyLabel.setLayoutY(40);

        // 门的状态（左上角）
        doorLabel = new Label("Door: Locked");
        doorLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: red; -fx-background-color: rgba(0, 0, 0, 0.5);");
        doorLabel.setLayoutX(15);
        doorLabel.setLayoutY(70);

        root.getChildren().addAll(bg, levelLabel, timeLabel, keyLabel, doorLabel);

        // 计时器，每秒更新挑战时长
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsElapsed++;
            timeLabel.setText("Time: " + secondsElapsed + " S");
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void updateKeys(int keys) {
        collectedKeys = keys;
        keyLabel.setText("Keys: " + collectedKeys);
    }

    public void updateDoorStatus(boolean unlocked) {
        doorUnlocked = unlocked;
        doorLabel.setText("Door: " + (doorUnlocked ? "Unlocked" : "Locked"));
        doorLabel.setStyle("-fx-font-size: 20px; " + "-fx-text-fill: " + (doorUnlocked ? "green" : "red") + "; -fx-background-color: rgba(0, 0, 0, 0.5);");
    }
}