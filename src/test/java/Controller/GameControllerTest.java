package Controller;

import Model.GameModel;
import View.GameView;
import de.saxsys.javafx.test.JfxRunner;
import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(JfxRunner.class)
public class GameControllerTest {

    private ArrayList<GameModel> gameModels;
    private GameView gameView;
    private GameController gameController;

    @Before
    public void setup() {
        GameModel model1 = new GameModel();
        model1.initialiseSnake(3, 10, 15);
        gameModels = new ArrayList<>();
        gameModels.add(model1);
        gameView = new GameView(10, 165, 150, 15, 15);
        gameController = new GameController(gameModels, gameView);
        gameController.setGrid(10);
        gameController.setNum(15);
    }

    @Test
    public void testControllerConstructor() {
        assertNotNull(gameController);
    }

    @Test
    public void testShowFood(){
        gameController.showFood();
        assertEquals(GameView.whole[gameController.getFoodX()/10][gameController.getFoodY()/10].getFill()
                , Color.RED);
    }
}