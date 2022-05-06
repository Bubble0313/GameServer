import Constants.GameConstants;
import Controller.ClientController;
import Controller.GameController;
import Controller.ServerController;
import Model.GameModel;
import View.GameView;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class GameMVC extends Application {
    private GameModel model = new GameModel();
    private GameView view = new GameView(GameConstants.panelHeight, GameConstants.panelWidth);
    private GameController controller;
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMVC.class);
    private EventHandler<MouseEvent> mouseEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (view.getMode().equals("Client")){
                controller = new ClientController();
                LOGGER.info("client controller");
            }else {
                controller = new ServerController();
                if (view.getMode().equals("Local")){
                    LOGGER.info("local controller");
                }else {
                    LOGGER.info("server controller");
                }
            }
            controller.setModel(model);
            controller.setView(view);
            controller.fileOperation();
            view.createSecondScene(GameConstants.grid, GameConstants.panelWidth,
                    GameConstants.gameUpBorder, GameConstants.num);
            view.getBestPlayerText().setText("Best Player: " + controller.getBestPlayer());
            view.getBestScoreText().setText("Best Score: " + controller.getBestScore());
            Stage stage = (Stage) view.getFirstScene().getWindow();
            stage.setScene(view.getSecondScene());
            controller.startGame();
        }
    };

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Game of Snake");
        primaryStage.setScene(view.getFirstScene());
        primaryStage.show();
        view.getButton().addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandler);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

//write UDP,
//single project
//client connect pass the user keyboard, no need to have the logic,
//logic handle on server
//next step: client on a different laptop, ip address, lag network, same network
//next step: outside the network