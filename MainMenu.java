import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox menuLayout = new VBox(20);
        menuLayout.setStyle("-fx-background-color: #333333; -fx-alignment: center; -fx-padding: 50px;");

        // 游戏标题
        Label title = new Label("Fireboy And Watergirl");
        title.setStyle("-fx-font-size: 30px; -fx-text-fill: white;");

        // 操作说明
        Label instructions = new Label("Controls:\nFireboy - ← → to move, ↑ to jump\nWatergirl - A D to move, W to jump");
        instructions.setStyle("-fx-font-size: 16px; -fx-text-fill: lightgray;");

        Button startButton = new Button("Start Game");
        Button exitButton = new Button("Exit Game");

        startButton.setStyle("-fx-font-size: 20px;");
        exitButton.setStyle("-fx-font-size: 20px;");

        startButton.setOnAction(e -> {
            FireboyWatergirl game = new FireboyWatergirl();
            game.start(primaryStage);
        });

        exitButton.setOnAction(e -> System.exit(0));

        menuLayout.getChildren().addAll(title, instructions, startButton, exitButton);
        Scene menuScene = new Scene(menuLayout, 1600, 900);

        primaryStage.setTitle("Forest Ice Fire - Main Menu");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
