import javafx.geometry.Point2D;
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
    }

    @Test
    public void testStart(){
        assertNotNull(GameMVC.models);
        assertNotNull(GameMVC.view);
        assertNotNull(GameMVC.mouseEventHandler);
        assertNotNull(GameMVC.controller);
    }
}