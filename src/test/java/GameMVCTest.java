import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

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
        robot.sleep(2000);
        robot.press(KeyCode.UP);
        robot.release(KeyCode.UP);
        robot.sleep(2000);
        robot.press(KeyCode.LEFT);
        robot.release(KeyCode.LEFT);
        robot.sleep(2000);
        robot.press(KeyCode.DOWN);
        robot.release(KeyCode.DOWN);
        robot.sleep(2000);
        robot.press(KeyCode.RIGHT);
        robot.release(KeyCode.RIGHT);
        robot.sleep(2000);
    }

    @Test
    public void testStart(){
        assertNotNull(GameMVC.models);
        assertNotNull(GameMVC.view);
        assertNotNull(GameMVC.mouseEventHandler);
        assertNotNull(GameMVC.controller);
    }
}