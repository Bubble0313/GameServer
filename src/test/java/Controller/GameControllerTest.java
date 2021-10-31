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
    private GameModel model1;
    private GameView gameView;
    private GameController gameController;

    private int grid = 10;
    private int num = 15;

    @Before
    public void setup() {
        model1 = new GameModel();
        model1.initialiseSnake(3, grid, num);
        gameModels = new ArrayList<>();
        gameModels.add(model1);
        gameView = new GameView(165, 150);
        gameView.getNameTextField().get(0).setText("Amy");
        gameView.createSecondScene(grid, 150, 15, num);
        gameController = new GameController();
        gameController.setModels(gameModels);
        gameController.setView(gameView);
        gameController.setGrid(grid);
        gameController.setNum(num);
        gameController.setGameHeight(150);
        gameController.setGameWidth(150);
    }

    @Test
    public void testControllerConstructor(){
        assertEquals(gameController.getModels(), gameModels);
        assertEquals(gameController.getView(), gameView);
        assertEquals(gameController.getGrid(), grid);
        assertEquals(gameController.getNum(), num);
        assertEquals(gameController.getGameHeight(), 150);
        assertEquals(gameController.getGameWidth(), 150);
        assertNotNull(gameController.getRandom());
        assertEquals(gameController.getBestScore(), -1);
        assertEquals(gameController.getBestPlayer(), "N/A");
        assertNotNull(gameController.getModelTextMap());
    }

    @Test
    public void testShowFood() {
        gameController.showFood();
        assertEquals(GameView.whole[gameController.getFoodX() / grid][gameController.getFoodY() / grid].getFill()
                , Color.RED);
    }

    @Test
    public void testPaintSnake() {
        gameController.paintSnake(model1);
        assertEquals(GameView.whole[model1.getSnakeX().get(0) / grid][model1.getSnakeY().get(0) / grid].getFill()
                , Color.YELLOW);
        assertEquals(GameView.whole[model1.getSnakeX().get(1) / grid][model1.getSnakeY().get(1) / grid].getFill()
                , Color.BLUE);
        assertEquals(GameView.whole[model1.getSnakeX().get(2) / grid][model1.getSnakeY().get(2) / grid].getFill()
                , Color.BLUE);
    }

    @Test
    public void testSetModelSpeed() {
        GameController.setModelSpeed(model1, 1);
        assertEquals(model1.getSpeed(), Level.L1.speed);
        GameController.setModelSpeed(model1, 2);
        assertEquals(model1.getSpeed(), Level.L2.speed);
        GameController.setModelSpeed(model1, 3);
        assertEquals(model1.getSpeed(), Level.L3.speed);
        GameController.setModelSpeed(model1, 4);
        assertEquals(model1.getSpeed(), Level.L4.speed);
        GameController.setModelSpeed(model1, 5);
        assertEquals(model1.getSpeed(), Level.L5.speed);
        GameController.setModelSpeed(model1, 6);
        assertEquals(model1.getSpeed(), Level.L6.speed);
        GameController.setModelSpeed(model1, 7);
        assertEquals(model1.getSpeed(), Level.L7.speed);
        GameController.setModelSpeed(model1, 8);
        assertEquals(model1.getSpeed(), Level.L8.speed);
        GameController.setModelSpeed(model1, 9);
        assertEquals(model1.getSpeed(), Level.L9.speed);
    }

    @Test
    public void testFileOperations() {
        gameController.setRecordFile(gameController.createFile("test.txt"));
        assertTrue(gameController.getRecordFile().exists());
        assertEquals(gameController.getRecordFile().getName(), "test.txt");
        gameController.writeToFile();
        assertEquals(gameController.getRecordFile().length(), 6L);
        gameController.readFromFile();
        assertEquals(gameView.getBestPlayerText().getText(), "Best Player: N/A");
        assertEquals(gameView.getBestScoreText().getText(), "Best Score: N/A");
    }

    @Test
    public void testStartGame() {
        gameController.startGame("test.txt", "test.ini");
        assertTrue(gameController.getRecordFile().exists());
        assertTrue(gameController.getIniFile().exists());
    }

    @Test
    public void testInitSnake() {
        gameController.initSnake(model1, 1);
        assertEquals(gameModels.get(0).getPlayer(), "Amy");
        assertEquals(gameModels.get(0).getIsStart(), true);
        assertEquals(gameController.getModelTextMap().get(model1), gameView.getCurrentScoreText().get(0));
        assertEquals(gameView.getCurrentScoreText().get(0).getText(), "Amy's current score: 0");
        gameController.setFoodX(100);
        gameController.setFoodY(100);
        gameController.setBestPlayer("Lily");
        gameController.setBestScore(2);
        gameController.startRunning(model1);
    }
}