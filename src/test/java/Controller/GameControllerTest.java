package Controller;

import de.saxsys.javafx.test.JfxRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

//@RunWith(JfxRunner.class)
public class GameControllerTest {
    private GameController gameController = new GameController();

    @Test
    public void test(){
        gameController.startGame();
        gameController.fileOperation("","");
        gameController.setHeadX(1);
        gameController.setHeadY(1);
        gameController.setBestScore(1);
        gameController.setBestPlayer("Amy");
        gameController.setFoodX(1);
        gameController.setFoodY(1);
        assertEquals(gameController.getHeadX(),1);
        assertEquals(gameController.getHeadY(),1);
    }
}