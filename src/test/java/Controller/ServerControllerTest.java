package Controller;

import Model.Direction;
import Model.GameModel;
import View.GameView;
import de.saxsys.javafx.test.JfxRunner;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
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
        gameView.setMode("Local");
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
        assertEquals(serverController.getBestScore(), -1);
        assertEquals(serverController.getBestPlayer(), "N/A");
        assertNotNull(serverController.getKeyEventHandler());
        serverController.setKeyEventHandler(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

            }
        });
    }

    @Test
    public void testShowFood() {
        serverController.showFood();
        assertNotNull(serverController.getFoodX());
        assertNotNull(serverController.getFoodY());
    }

    @Test
    public void testFileOperations() {
        serverController.fileOperation("record.txt", "config.ini");
        assertTrue(serverController.getRecordFile().exists());
        assertTrue(serverController.getIniFile().exists());
        serverController.setRecordFile(serverController.createFile("record.txt"));
        assertTrue(serverController.getRecordFile().exists());
        assertEquals(serverController.getRecordFile().getName(), "record.txt");
        assertEquals(serverController.getRecordFile().length(), 0);
    }

    @Test
    public void testCreateNewModel(){
        serverController.createNewModel();
        assertNotNull(models.get(1));
        assertEquals(models.get(1).getIsStart(), true);
    }

    @Test
    public void testSetDirection(){
        serverController.setDirection(models.get(0), 0);
        assertEquals(models.get(0).getDirection(), Direction.UP);
        serverController.setDirection(models.get(0), 1);
        assertEquals(models.get(0).getDirection(), Direction.DOWN);
        serverController.setDirection(models.get(0), 2);
        assertEquals(models.get(0).getDirection(), Direction.LEFT);
        serverController.setDirection(models.get(0), 3);
        assertEquals(models.get(0).getDirection(), Direction.RIGHT);
    }

    @Test
    public void testDiffElement(){
        byte[] payload = new byte[10];
        payload[0] = 2;
        Packet packet = new Packet(MessageType.DIRECTION, payload);
        serverController.diffElement(models.get(0), packet);
        assertEquals(models.get(0).getDirection(), Direction.LEFT);
        packet.setType(MessageType.LEVEL);
        serverController.diffElement(models.get(0), packet);
        assertEquals(models.get(0).getSpeed(), Level.L2.speed);
        packet.setType(MessageType.NAME);
        packet.setPayload("Amy".getBytes());
        serverController.diffElement(models.get(0), packet);
        assertEquals(models.get(0).getPlayer(), "Amy");
    }

    @After
    public void cleanup() {
        Path path1 = FileSystems.getDefault().getPath("record.txt");
        Path path2 = FileSystems.getDefault().getPath("config.ini");
        try {
            Files.delete(path1);
            Files.delete(path2);
            System.out.println("deleted");
        }catch (IOException e) {
            System.err.println(e);
        }
    }
}