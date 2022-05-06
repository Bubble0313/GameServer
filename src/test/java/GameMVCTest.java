import Controller.GameController;
import Controller.ServerController;
import Model.GameModel;
import View.GameView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class GameMVCTest extends ApplicationTest {
    GameModel model = new GameModel();

    @Override
    public void start(Stage stage) {
        GameView testView = new GameView(165, 150);
        testView.createSecondScene(10, 150, 15, 15);
        GameController testController = new ServerController();
        testController.setModel(model);
        testController.setView(testView);
        Scene scene = testView.getFirstScene();
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testStart(){
        GameMVC mvc = new GameMVC();
        assertNotNull(mvc.getModel());
        assertNotNull(mvc.getView());
        assertNull(mvc.getController());
    }

    @Test
    public void testButton(){
        FxAssert.verifyThat(".button", NodeMatchers.hasText("Start"));
    }
}