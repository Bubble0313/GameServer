package Controller;

import Model.GameModel;
import View.GameView;
import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.util.*;

import static org.junit.Assert.*;

//@RunWith(JfxRunner.class)
public class ServerControllerTest {

    private ArrayList<GameModel> models;
    private GameView gameView;
    private ServerController serverController;

    private int grid = 10;
    private int num = 15;

    @Before
    public void setup() {
        models = new ArrayList<>();
        GameModel model = new GameModel();
        models.add(model);
        models.get(0).initialiseSnake(3, 50, 50, grid);
        gameView = new GameView(165, 150);
        gameView.getNameTextField().setText("Amy");
        gameView.createSecondScene(grid, 150, 15, num);
        serverController = new ServerController();
        serverController.setModels(models);
        serverController.setView(gameView);
        serverController.setGrid(grid);
        serverController.setNum(num);
        serverController.setGameHeight(150);
        serverController.setGameWidth(150);
    }

    @Test
    public void testControllerConstructor(){
        assertEquals(serverController.getModels(), models);
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
        assertEquals(GameView.whole[models.get(0).getSnakeX().get(0) / grid][models.get(0).getSnakeY().get(0) / grid].getFill()
                , Color.YELLOW);
        assertEquals(GameView.whole[models.get(0).getSnakeX().get(1) / grid][models.get(0).getSnakeY().get(1) / grid].getFill()
                , Color.BLUE);
        assertEquals(GameView.whole[models.get(0).getSnakeX().get(2) / grid][models.get(0).getSnakeY().get(2) / grid].getFill()
                , Color.BLUE);
    }

    @Test
    public void testSetModelSpeed() {
        GameController.setModelSpeed(models.get(0), 1);
        assertEquals(models.get(0).getSpeed(), Level.L1.speed);
        GameController.setModelSpeed(models.get(0), 2);
        assertEquals(models.get(0).getSpeed(), Level.L2.speed);
        GameController.setModelSpeed(models.get(0), 3);
        assertEquals(models.get(0).getSpeed(), Level.L3.speed);
        GameController.setModelSpeed(models.get(0), 4);
        assertEquals(models.get(0).getSpeed(), Level.L4.speed);
        GameController.setModelSpeed(models.get(0), 5);
        assertEquals(models.get(0).getSpeed(), Level.L5.speed);
        GameController.setModelSpeed(models.get(0), 6);
        assertEquals(models.get(0).getSpeed(), Level.L6.speed);
        GameController.setModelSpeed(models.get(0), 7);
        assertEquals(models.get(0).getSpeed(), Level.L7.speed);
        GameController.setModelSpeed(models.get(0), 8);
        assertEquals(models.get(0).getSpeed(), Level.L8.speed);
        GameController.setModelSpeed(models.get(0), 9);
        assertEquals(models.get(0).getSpeed(), Level.L9.speed);
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
        assertEquals(models.get(0).getPlayer(), "Amy");
        assertEquals(models.get(0).getIsStart(), true);
        assertEquals(gameView.getCurrentScoreText().getText(), "Amy's current score: 0");
        serverController.setFoodX(100);
        serverController.setFoodY(100);
        serverController.setBestPlayer("Lily");
        serverController.setBestScore(2);
        assertEquals(serverController.getFoodX(), 100);
        assertEquals(serverController.getFoodY(), 100);
        assertEquals(serverController.getBestPlayer(), "Lily");
        assertEquals(serverController.getBestScore(), 2);
//        serverController.tickUpdate(, );
    }
}