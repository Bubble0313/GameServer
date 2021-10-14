package Model;

import Controller.Level;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class GameModelTest {

    private GameModel gameModel;

    @Before
    public void setup(){
        gameModel = new GameModel();
    }

    @Test
    public void testModelConstructor(){
        assertEquals(gameModel.getDirection(), Direction.RIGHT);
        assertEquals(gameModel.getLastDirection(), Direction.RIGHT);
        assertEquals(gameModel.getIsStart(),false);
        assertEquals(gameModel.getScore(), 0);
        assertEquals(gameModel.getSpeed(), Level.L1.speed);
        assertEquals(gameModel.getPlayer(), "Bot");
    }

    @Test
    public void testInitSnake()
    {
        gameModel.initialiseSnake(3, 10, 15);
        assertEquals(gameModel.getLength(), 3);
        assertNotNull(gameModel.getSnakeX().get(0));
        assertNotNull(gameModel.getSnakeX().get(1));
        assertNotNull(gameModel.getSnakeX().get(2));
        assertNotNull(gameModel.getSnakeY().get(0));
        assertNotNull(gameModel.getSnakeY().get(1));
        assertNotNull(gameModel.getSnakeY().get(2));
        assertNotNull(gameModel.getRan());
    }

    @Test
    public void testUpdateSnake(){
        ArrayList<Integer> snakeX = new ArrayList<>(Arrays.asList(Integer.valueOf(30),
                Integer.valueOf(20), Integer.valueOf(10)));
        ArrayList<Integer> snakeY = new ArrayList<>(Arrays.asList(Integer.valueOf(30),
                Integer.valueOf(30), Integer.valueOf(30)));
        gameModel.setSnakeX(snakeX);
        gameModel.setSnakeY(snakeY);
        gameModel.setLength(3);
        gameModel.updateSnake(10, 150, 150);
        assertEquals(gameModel.getSnakeX().get(2), Integer.valueOf(20));
        assertEquals(gameModel.getSnakeX().get(1), Integer.valueOf(30));
        assertEquals(gameModel.getSnakeX().get(0), Integer.valueOf(40));
        assertEquals(gameModel.getSnakeY().get(0), Integer.valueOf(30));
        assertEquals(gameModel.getSnakeY().get(1), Integer.valueOf(30));
        assertEquals(gameModel.getSnakeY().get(2), Integer.valueOf(30));
    }

}