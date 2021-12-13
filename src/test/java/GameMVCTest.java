import static org.junit.Assert.*;

import Controller.GameController;
import Controller.Level;
import Model.GameModel;
import View.GameView;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import java.util.ArrayList;

public class GameMVCTest extends ApplicationTest {
    int grid = 10;
    GameModel snake1 = new GameModel();
    ArrayList<GameModel> testModels = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        GameView testView = new GameView(165, 150);
        testView.createSecondScene(10, 150, 15, 15);
        GameController testController = new GameController();
        testController.setModels(testModels);
        testController.setView(testView);
        Scene scene = testView.getFirstScene();
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testStart(){
        GameMVC mvc = new GameMVC();
        assertNotNull(mvc.getModels());
        assertNotNull(mvc.getView());
        assertNotNull(mvc.getController());
        assertNotNull(mvc.getKeyEventHandler());
        assertNotNull(mvc.getKeyEventHandler2());
        assertNotNull(mvc.getMouseEventHandler());
    }

    @Test
    public void testButton(){
        FxAssert.verifyThat(".button", NodeMatchers.hasText("Start"));
    }

    @Before
    public void setup(){
        snake1.initialiseSnake(8, grid, 50);
        testModels.add(snake1);
    }

    @Test
    public void testModelUpdateSnake(){
        int x = snake1.getSnakeX().get(0);
        snake1.updateSnake(grid, 500, 500);
        int y = snake1.getSnakeX().get(0);
        assertEquals(grid, y-x);
    }

    @Test
    public void testViewPaint(){
        GameView.paintTail(50, 50, grid);
        assertEquals(Color.BLACK, GameView.whole[5][5].getFill());
    }

    @Test
    public void testControllerSetSpeed(){
        GameController.setModelSpeed(snake1, 2);
        assertEquals(Level.L2.speed, snake1.getSpeed());
    }
}