import javafx.scene.input.MouseButton;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class GameMVCTest {

    @Before
    public void setup() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(GameMVC.class);
        new FxRobot().clickOn(GameMVC.view.getButton());
    }

    @Test
    public void testStart(){
        assertNotNull(GameMVC.models);
        assertNotNull(GameMVC.view);
        assertNotNull(GameMVC.mouseEventHandler);
        assertNull(GameMVC.controller);
    }
}