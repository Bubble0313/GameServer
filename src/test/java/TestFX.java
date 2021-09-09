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

import static org.junit.Assert.assertEquals;

public class TestFX extends ApplicationTest {
    int grid = 10;
    GameModel snake1 = new GameModel(100, 100, grid, 8);
    ArrayList<GameModel> testModels = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        GameView testView = new GameView(grid, 550, 500, 50, 50);
        GameController testController = new GameController(testModels, testView);
        Scene scene = testView.getFirstScene();
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testButton(){
        FxAssert.verifyThat(".button", NodeMatchers.hasText("Start"));
    }

    @Before
    public void setup(){
        testModels.add(snake1);
    }

    @Test
    public void testModelUpdateSnake(){
        Integer x = 110;
        snake1.updateSnake(grid, 500, 500);
        assertEquals(x, snake1.getSnakeX().get(0));
    }

    @Test
    public void testViewPaint(){
        GameView.paintTail(20, 100, 5);
        assertEquals(Color.BLACK, GameView.whole[2][10].getFill());
    }

    @Test
    public void testControllerSetSpeed(){
        GameController.setModelSpeed(snake1, 2);
        assertEquals(Level.L2.speed, snake1.getSpeed());
    }
}
