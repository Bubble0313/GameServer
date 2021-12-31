import Constants.GameConstants;
import Controller.GameController;
import Model.GameModel;
import View.GameView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class ServerMVC extends Application {
    private ArrayList<GameModel> models = new ArrayList<>();
    private GameView view = new GameView(GameConstants.panelHeight, GameConstants.panelWidth);
    private GameController controller = new GameController();

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Game of Snake");
        primaryStage.setScene(view.getFirstScene());
        primaryStage.show();
        controller.setModels(models);
        controller.setView(view);
        controller.startGame("record.txt", "config.ini");
        controller.networkServer();
    }

    public static void main(String[] args) {
        launch(args);
    }
}