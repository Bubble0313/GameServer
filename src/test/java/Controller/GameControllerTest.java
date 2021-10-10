package Controller;

import Model.GameModel;
import View.GameView;
import de.saxsys.javafx.test.JfxRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(JfxRunner.class)
public class GameControllerTest {

    private ArrayList<GameModel> gameModels;
    private GameView gameView;

    @Before
    public void setup() {
        GameModel model1 = new GameModel();
        gameModels = new ArrayList<>();
        gameModels.add(model1);
        gameView = new GameView(10, 165, 150, 15, 15);
    }

    @Test
    public void test() {

    }
}