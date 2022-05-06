package Controller;

import Model.GameModel;
import View.GameView;
import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

//@RunWith(JfxRunner.class)
public class ServerControllerTest {

    private GameModel model;
    private GameView gameView;
    private ServerController serverController;

    private int grid = 10;
    private int num = 15;

    @Before
    public void setup() {
        model = new GameModel();
        model.initialiseSnake(3, 50, 50, grid);
        gameView = new GameView(165, 150);
        gameView.getNameTextField().setText("Amy");
        gameView.createSecondScene(grid, 150, 15, num);
        serverController = new ServerController();
        serverController.setModel(model);
        serverController.setView(gameView);
        serverController.setGrid(grid);
        serverController.setNum(num);
        serverController.setGameHeight(150);
        serverController.setGameWidth(150);
    }

    @Test
    public void testControllerConstructor(){
        assertEquals(serverController.getModel(), model);
        assertEquals(serverController.getView(), gameView);
        assertEquals(serverController.getGrid(), grid);
        assertEquals(serverController.getNum(), num);
        assertEquals(serverController.getGameHeight(), 150);
        assertEquals(serverController.getGameWidth(), 150);
        assertNotNull(serverController.getRandom());
        assertEquals(serverController.getBestScore(), -1);
        assertEquals(serverController.getBestPlayer(), "N/A");
        serverController.setRandom(new Random());
    }

    @Test
    public void testShowFood() {
        serverController.showFood();
        assertEquals(GameView.whole[serverController.getFoodX() / grid][serverController.getFoodY() / grid].getFill()
                , Color.RED);
    }

    @Test
    public void testPaintSnake() {
        serverController.paintSnake(3, 50, 50);
        assertEquals(GameView.whole[model.getSnakeX().get(0) / grid][model.getSnakeY().get(0) / grid].getFill()
                , Color.YELLOW);
        assertEquals(GameView.whole[model.getSnakeX().get(1) / grid][model.getSnakeY().get(1) / grid].getFill()
                , Color.BLUE);
        assertEquals(GameView.whole[model.getSnakeX().get(2) / grid][model.getSnakeY().get(2) / grid].getFill()
                , Color.BLUE);
    }

    @Test
    public void testSetModelSpeed() {
        GameController.setModelSpeed(model, 1);
        assertEquals(model.getSpeed(), Level.L1.speed);
        GameController.setModelSpeed(model, 2);
        assertEquals(model.getSpeed(), Level.L2.speed);
        GameController.setModelSpeed(model, 3);
        assertEquals(model.getSpeed(), Level.L3.speed);
        GameController.setModelSpeed(model, 4);
        assertEquals(model.getSpeed(), Level.L4.speed);
        GameController.setModelSpeed(model, 5);
        assertEquals(model.getSpeed(), Level.L5.speed);
        GameController.setModelSpeed(model, 6);
        assertEquals(model.getSpeed(), Level.L6.speed);
        GameController.setModelSpeed(model, 7);
        assertEquals(model.getSpeed(), Level.L7.speed);
        GameController.setModelSpeed(model, 8);
        assertEquals(model.getSpeed(), Level.L8.speed);
        GameController.setModelSpeed(model, 9);
        assertEquals(model.getSpeed(), Level.L9.speed);
    }

    @Test
    public void testFileOperations() {
        serverController.setRecordFile(serverController.createFile("test.txt"));
        assertTrue(serverController.getRecordFile().exists());
        assertEquals(serverController.getRecordFile().getName(), "test.txt");
        serverController.fileOperation();
        assertEquals(serverController.getRecordFile().length(), 6L);
    }

    @Test
    public void testStartGame() {
        serverController.startGame();
        assertTrue(serverController.getRecordFile().exists());
        assertTrue(serverController.getIniFile().exists());
    }

    @Test
    public void testInitSnake() {
        serverController.startGame();
        assertEquals(model.getPlayer(), "Amy");
        assertEquals(model.getIsStart(), true);
        assertEquals(gameView.getCurrentScoreText().getText(), "Amy's current score: 0");
        serverController.setFoodX(100);
        serverController.setFoodY(100);
        serverController.setBestPlayer("Lily");
        serverController.setBestScore(2);
        assertEquals(serverController.getFoodX(), 100);
        assertEquals(serverController.getFoodY(), 100);
        assertEquals(serverController.getBestPlayer(), "Lily");
        assertEquals(serverController.getBestScore(), 2);
        serverController.tickUpdate();
    }
}