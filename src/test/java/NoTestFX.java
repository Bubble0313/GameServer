import Controller.GameController;
import Model.GameModel;
import View.GameView;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class NoTestFX {

    @Before
    public void setup(){

    }

    @Test
    public void testModelConstructor(){
        GameModel snake1 = new GameModel(100, 100, 5, 8);
        Integer x = 100;

        //snake1.updateSnake(5, 600, 600);
        assertEquals(x, snake1.getSnakeX().get(0));
    }

}
