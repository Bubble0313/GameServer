import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertNotNull;

public class GameMVCTest {

    @Before
    public void setup() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(GameMVC.class);
        FxRobot robot = new FxRobot();
        robot.clickOn(GameMVC.view.getModeChoiceBox());
        robot.clickOn(new Point2D(550.0, 425.0));
        robot.clickOn(GameMVC.view.getButton());
        System.out.println("foodX: " + GameMVC.controller.getFoodX());
        System.out.println("foodY: " + GameMVC.controller.getFoodY());
        System.out.println("headX: " + GameMVC.controller.getHeadX());
        System.out.println("headY: " + GameMVC.controller.getHeadY());
        int gapX = GameMVC.controller.getFoodX() - GameMVC.controller.getHeadX();
        int gapY = GameMVC.controller.getFoodY() - GameMVC.controller.getHeadY();
        System.out.println("gapX: " + gapX);
        System.out.println("gapY: " + gapY);
        if (gapY > 0){
            robot.press(KeyCode.DOWN);
            robot.release(KeyCode.DOWN);
        }else {
            robot.press(KeyCode.UP);
            robot.release(KeyCode.UP);
        }
        robot.sleep(500 * Math.abs(gapY));
        if (gapX < 0){
            robot.press(KeyCode.LEFT);
            robot.release(KeyCode.LEFT);
        }else {
            robot.press(KeyCode.RIGHT);
            robot.release(KeyCode.RIGHT);
        }
        robot.sleep(500 * Math.abs(gapX));
        System.out.println(GameMVC.models.get(0).getScore());
        System.out.println(GameMVC.controller.bestScore);
        robot.sleep(20000);
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

    @Test
    public void testStart(){
        assertNotNull(GameMVC.models);
        assertNotNull(GameMVC.view);
        assertNotNull(GameMVC.mouseEventHandler);
        assertNotNull(GameMVC.controller);
    }
}