import Constants.GameConstants;
import Controller.GameController;
import Model.Direction;
import Model.GameModel;
import View.GameView;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GameMVC extends Application {
    private ArrayList<GameModel> models = new ArrayList<>();
    private GameView view = new GameView(GameConstants.panelHeight, GameConstants.panelWidth);
    private GameController controller = new GameController();

    private EventHandler<KeyEvent> keyEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            KeyCode keyCode = event.getCode();
            Direction direction = models.get(0).getLastDirection();
            if (keyCode == KeyCode.UP && direction != Direction.DOWN) {
                models.get(0).setDirection(Direction.UP);//user cannot directly change the direction to its opposite
            }
            if (keyCode == KeyCode.DOWN && direction != Direction.UP) {
                models.get(0).setDirection(Direction.DOWN);
            }
            if (keyCode == KeyCode.LEFT && direction != Direction.RIGHT) {
                models.get(0).setDirection(Direction.LEFT);
            }
            if (keyCode == KeyCode.RIGHT && direction != Direction.LEFT) {
                models.get(0).setDirection(Direction.RIGHT);
            }
        }
    };//change the direction of the snake based on the keyboard input

    private EventHandler<KeyEvent> keyEventHandler2 = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            KeyCode keyCode = event.getCode();
            Direction direction = models.get(1).getLastDirection();
            if (keyCode == KeyCode.W && direction != Direction.DOWN) {
                models.get(1).setDirection(Direction.UP);//user cannot directly change the direction to its opposite
            }
            if (keyCode == KeyCode.S && direction != Direction.UP) {
                models.get(1).setDirection(Direction.DOWN);
            }
            if (keyCode == KeyCode.A && direction != Direction.RIGHT) {
                models.get(1).setDirection(Direction.LEFT);
            }
            if (keyCode == KeyCode.D && direction != Direction.LEFT) {
                models.get(1).setDirection(Direction.RIGHT);
            }
        }
    };

    private EventHandler<MouseEvent> mouseEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            //when game starts, create the second scene
            view.createSecondScene(GameConstants.grid, GameConstants.panelWidth,
                    GameConstants.gameUpBorder, GameConstants.num);
            view.getBestPlayerText().setText("Best Player: " + controller.getBestPlayer());
            view.getBestScoreText().setText("Best Score: " + controller.getBestScore());
            //Handling snake movement direction based on keyboard input
            view.getSecondScene().addEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);
            //create models
            GameModel model1 = new GameModel();
            controller.initSnake(model1, 1);
            //when there are 2 players
            if (view.getSnakeNum().equals(2)) {
                view.getSecondScene().addEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler2);
                GameModel model2 = new GameModel();
                controller.initSnake(model2, 2);
                controller.getModelTextMap().get(models.get(0)).setY(GameConstants.gameUpBorder * 0.25);
            }
            //switch from the first scene to the second scene
            Stage stage = (Stage) view.getFirstScene().getWindow();
            stage.setScene(view.getSecondScene());
            //paint the initial food
            controller.showFood();
            //start the game tick
            controller.tickUpdate();
        }
    };

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Game of Snake");
        primaryStage.setScene(view.getFirstScene());
        primaryStage.show();
        controller.setModels(models);
        controller.setView(view);
        //add mouse clicking event handler to all the buttons
        view.getButton().addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandler);
        controller.startGame("record.txt", "config.ini");
    }

    public static void main(String[] args) {
        launch(args);
    }
}