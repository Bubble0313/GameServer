package View;

import de.saxsys.javafx.test.JfxRunner;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

//@RunWith(JfxRunner.class)
public class GameViewTest {

    private GameView gameView;

    @Before
    public void setup(){
        gameView = new GameView( 165, 150);
    }

    @Test
    public void testViewConstructor(){
        gameView.createSecondScene(10, 150, 15, 15);
        assertEquals(gameView.getInitialText().getText(), "Welcome to the Game of Snake!\n" +
                "Start the game by choosing player mode (Local VS Network Server VS Network Client).");
        assertEquals(gameView.getModeText().getText(), "Player Mode:");
        assertEquals(gameView.getModeChoiceBox().getItems().get(0), "Local");
        assertEquals(gameView.getButton().getText(), "Start");
        assertNotNull(gameView.getFirstScene());
        assertNotNull(gameView.getSecondScene());
        assertEquals(gameView.getCurrentScoreText().getText(), "Current Score: 0");
        assertEquals(gameView.getBestScoreText().getText(), "Best Score: N/A");
        assertEquals(gameView.getBestPlayerText().getText(), "Best Player: N/A");
        gameView.setInitialText(new Text("Hello"));
        gameView.setModeText(new Text("Number"));
        gameView.setNameText(new Text("Bot"));
        gameView.setSpeedText(new Text("Speed"));
        gameView.setCurrentScoreText(new Text());
        gameView.setBestScoreText(new Text("best score"));
        gameView.setBestPlayerText(new Text("best player"));
        gameView.setButton(new Button());
        gameView.setNameTextField(new TextField());
        gameView.setLevelChoiceBox(new ChoiceBox());
        gameView.setModeChoiceBox(new ChoiceBox());
        Group root1 = new Group();
        Group root2 = new Group();
        gameView.setFirstScene(new Scene(root1));
        gameView.setSecondScene(new Scene(root2));
    }

    @Test
    public void testPainting(){
        gameView.createSecondScene(10, 150, 15, 15);
        GameView.paintSnake(3, 5, 5);
        assertEquals(GameView.whole[5][5].getFill(), Color.YELLOW);
        assertEquals(GameView.whole[4][5].getFill(), Color.BLUE);
        assertEquals(GameView.whole[3][5].getFill(), Color.BLUE);
        GameView.paintTail(5, 5);
        assertEquals(GameView.whole[5][5].getFill(), Color.BLACK);
        GameView.paintFood(5, 5);
        assertEquals(GameView.whole[5][5].getFill(), Color.RED);
    }

    @Test
    public void testAddInputView(){
        gameView.addInputView(150);
        assertEquals(gameView.getIntroText().getText(), "Please select game level and player name(s).\n" +
                "The higher the level you choose, the quicker the snake moves.\n" +
                "If there's no input, default player name is Bot, and default game level is Level 1.");
        assertEquals(gameView.getSpeedText().getText(), "Game Level:");
        assertEquals(gameView.getLevelChoiceBox().getItems().get(0), "Level 1");
        assertEquals(gameView.getNameText().getText(), "Player name:\n(Use keyboard arrows\nto control direction)");
        assertNotNull(gameView.getNameTextField());
        assertEquals(gameView.getPortText().getText(), "Client Port:");
        assertEquals(gameView.getPortChoiceBox().getItems().get(0), "1111");
        assertEquals(gameView.getMode(), "Server");
        assertNotNull(gameView.getGridPane());
        gameView.setPortText(new Text("Port"));
        gameView.setPortChoiceBox(new ChoiceBox());
        gameView.setIntroText(new Text("Intro"));
        gameView.setGridPane(new GridPane());
    }
}