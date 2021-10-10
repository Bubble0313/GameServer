package Model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameModelTest {

    private GameModel snake1;

    @Before
    public void setup(){
        snake1 = new GameModel();
    }

    @Test
    public void testModelConstructor(){
        assertEquals(snake1.getDirection(), Direction.RIGHT);
    }

}