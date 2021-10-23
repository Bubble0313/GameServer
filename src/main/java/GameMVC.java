import Constants.GameConstants;
import Controller.GameController;
import Model.GameModel;
import View.GameView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GameMVC extends Application {
    private ArrayList<GameModel> models = new ArrayList<>();
    private GameView view = new GameView(GameConstants.grid, GameConstants.panelHeight,
            GameConstants.panelWidth, GameConstants.gameUpBorder, GameConstants.num);
    private GameController controller = new GameController(models, view);

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Game of Snake");
        primaryStage.setScene(view.getFirstScene());
        primaryStage.show();
        controller.startGame("record.txt", "config.ini");
    }

    public static void main(String[] args) {
        launch(args);
    }
}