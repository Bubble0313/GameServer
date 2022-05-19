package Model;

import Controller.Level;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.*;

public class GameModelTest {

    private GameModel gameModel;

    @Before
    public void setup(){
        gameModel = new GameModel();
        gameModel.setIsStart(true);
        gameModel.setPlayer("Amy");
        gameModel.setSpeed(Level.L2.speed);
    }

    @Test
    public void testModelConstructor(){
        assertEquals(gameModel.getDirection(), Direction.RIGHT);
        assertEquals(gameModel.getLastDirection(), Direction.RIGHT);
        assertEquals(gameModel.getIsStart(),true);
        assertEquals(gameModel.getScore(), 0);
        assertEquals(gameModel.getSpeed(), Level.L2.speed);
        assertEquals(gameModel.getPlayer(), "Amy");
    }

    @Test
    public void testInitSnake()
    {
        gameModel.initialiseSnake(3, 10, 10, 10);
        gameModel.setScore(0);
        gameModel.setRan(new Random());
        assertEquals(gameModel.getLength(), 3);
        assertEquals(gameModel.getScore(),0);
        assertNotNull(gameModel.getSnakeX().get(0));
        assertNotNull(gameModel.getSnakeX().get(1));
        assertNotNull(gameModel.getSnakeX().get(2));
        assertNotNull(gameModel.getSnakeY().get(0));
        assertNotNull(gameModel.getSnakeY().get(1));
        assertNotNull(gameModel.getSnakeY().get(2));
        assertNotNull(gameModel.getRan());
    }

    @Test
    public void testUpdateSnakePositive(){
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
        gameModel.setDirection(Direction.UP);
        assertEquals(gameModel.getDirection(), Direction.UP);
        gameModel.updateSnake(10, 150, 150);
        assertEquals(gameModel.getSnakeX().get(2), Integer.valueOf(30));
        assertEquals(gameModel.getSnakeX().get(1), Integer.valueOf(40));
        assertEquals(gameModel.getSnakeX().get(0), Integer.valueOf(40));
        assertEquals(gameModel.getSnakeY().get(0), Integer.valueOf(20));
        assertEquals(gameModel.getSnakeY().get(1), Integer.valueOf(30));
        assertEquals(gameModel.getSnakeY().get(2), Integer.valueOf(30));
        gameModel.setDirection(Direction.LEFT);
        gameModel.updateSnake(10, 150, 150);
        assertEquals(gameModel.getSnakeX().get(2), Integer.valueOf(40));
        assertEquals(gameModel.getSnakeX().get(1), Integer.valueOf(40));
        assertEquals(gameModel.getSnakeX().get(0), Integer.valueOf(30));
        assertEquals(gameModel.getSnakeY().get(0), Integer.valueOf(20));
        assertEquals(gameModel.getSnakeY().get(1), Integer.valueOf(20));
        assertEquals(gameModel.getSnakeY().get(2), Integer.valueOf(30));
        gameModel.setDirection(Direction.DOWN);
        gameModel.updateSnake(10, 150, 150);
        assertEquals(gameModel.getSnakeX().get(2), Integer.valueOf(40));
        assertEquals(gameModel.getSnakeX().get(1), Integer.valueOf(30));
        assertEquals(gameModel.getSnakeX().get(0), Integer.valueOf(30));
        assertEquals(gameModel.getSnakeY().get(0), Integer.valueOf(30));
        assertEquals(gameModel.getSnakeY().get(1), Integer.valueOf(20));
        assertEquals(gameModel.getSnakeY().get(2), Integer.valueOf(20));
    }

    @Test
    public void testSetSnakeSpeed(){
        gameModel.setSnakeSpeed(1);
        assertEquals(gameModel.getSpeed(), Level.L1.speed);
        gameModel.setSnakeSpeed(2);
        assertEquals(gameModel.getSpeed(), Level.L2.speed);
        gameModel.setSnakeSpeed(3);
        assertEquals(gameModel.getSpeed(), Level.L3.speed);
        gameModel.setSnakeSpeed(4);
        assertEquals(gameModel.getSpeed(), Level.L4.speed);
        gameModel.setSnakeSpeed(5);
        assertEquals(gameModel.getSpeed(), Level.L5.speed);
        gameModel.setSnakeSpeed(6);
        assertEquals(gameModel.getSpeed(), Level.L6.speed);
        gameModel.setSnakeSpeed(7);
        assertEquals(gameModel.getSpeed(), Level.L7.speed);
        gameModel.setSnakeSpeed(8);
        assertEquals(gameModel.getSpeed(), Level.L8.speed);
        gameModel.setSnakeSpeed(9);
        assertEquals(gameModel.getSpeed(), Level.L9.speed);
    }

    @Test
    public void testIncreaseLength(){
        gameModel.increaseLength();
        assertEquals(gameModel.getLength(),1);
        assertEquals(gameModel.getScore(), 1);
    }

    @Test
    public void testUpdateSnakeNegative(){
        ArrayList<Integer> snakeX = new ArrayList<>(Arrays.asList(Integer.valueOf(100),
                Integer.valueOf(90), Integer.valueOf(80)));
        ArrayList<Integer> snakeY = new ArrayList<>(Arrays.asList(Integer.valueOf(30),
                Integer.valueOf(30), Integer.valueOf(30)));
        gameModel.setSnakeX(snakeX);
        gameModel.setSnakeY(snakeY);
        gameModel.setLength(3);
        gameModel.setIsStart(true);
        gameModel.updateSnake(10, 100, 100);
        assertEquals(gameModel.getIsStart(),false);
        gameModel.setIsStart(true);
        gameModel.setDirection(Direction.LEFT);
        snakeX = new ArrayList<>(Arrays.asList(Integer.valueOf(0),
                Integer.valueOf(10), Integer.valueOf(20)));
        gameModel.setSnakeX(snakeX);
        gameModel.updateSnake(10, 100, 100);
        assertEquals(gameModel.getIsStart(),false);
        gameModel.setIsStart(true);
        gameModel.setDirection(Direction.UP);
        snakeY = new ArrayList<>(Arrays.asList(Integer.valueOf(0),
                Integer.valueOf(10), Integer.valueOf(20)));
        gameModel.setSnakeY(snakeY);
        gameModel.updateSnake(10, 100, 100);
        assertEquals(gameModel.getIsStart(),false);
        gameModel.setIsStart(true);
        gameModel.setDirection(Direction.DOWN);
        snakeY = new ArrayList<>(Arrays.asList(Integer.valueOf(100),
                Integer.valueOf(90), Integer.valueOf(80)));
        gameModel.setSnakeY(snakeY);
        gameModel.updateSnake(10, 100, 100);
        assertEquals(gameModel.getIsStart(),false);
    }
}