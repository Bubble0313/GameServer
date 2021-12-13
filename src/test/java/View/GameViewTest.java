package View;

import de.saxsys.javafx.test.JfxRunner;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;

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
                "Start the game by choosing player number, game level and player name(s).\n " +
                "The higher the level you choose, the quicker the snake moves.\n " +
                "If there's no input, default player name is Bot, default game level is Level 1.");
        assertEquals(gameView.getNumText().getText(), "Player number:");
        assertEquals(gameView.getPlayerChoiceBox().getItems().get(0), "1");
        assertEquals(gameView.getSpeedText().getText(), "Game Level:");
        assertEquals(gameView.getLevelChoiceBox().getItems().get(0), "Level 1");
        assertEquals(gameView.getNameText().get(0).getText(), "Player1 name:\n(Use keyboard arrows\n" +
                "to control direction)");
        assertNotNull(gameView.getNameTextField().get(0));
        assertEquals(gameView.getButton().getText(), "Start");
        assertNotNull(gameView.getFirstScene());
        assertNotNull(gameView.getSecondScene());
        assertEquals(gameView.getCurrentScoreText().get(0).getText(), "");
        assertEquals(gameView.getBestScoreText().getText(), "Best Score: N/A");
        assertEquals(gameView.getBestPlayerText().getText(), "Best Player: N/A");
        assertEquals(gameView.getSnakeNum().intValue(), 1);
        gameView.setInitialText(new Text("Hello"));
        gameView.setNumText(new Text("Number"));
        gameView.setNameText(new ArrayList<Text>());
        gameView.setSpeedText(new Text("Speed"));
        gameView.setCurrentScoreText(new ArrayList<Text>());
        gameView.setBestScoreText(new Text("best score"));
        gameView.setBestPlayerText(new Text("best player"));
        gameView.setButton(new Button());
        gameView.setNameTextField(new ArrayList<TextField>());
        gameView.setLevelChoiceBox(new ChoiceBox());
        gameView.setPlayerChoiceBox(new ChoiceBox());
        Group root1 = new Group();
        Group root2 = new Group();
        gameView.setFirstScene(new Scene(root1));
        gameView.setSecondScene(new Scene(root2));
    }

    @Test
    public void testSecondScene(){
        gameView.setSnakeNum(2);
        gameView.createSecondScene(10, 150, 15, 15);
        assertEquals(gameView.getCurrentScoreText().size(), 2);
    }

    @Test
    public void testPaintTail(){
        GameView.paintTail(50, 50, 10);
        assertEquals(GameView.whole[5][5].getFill(), Color.BLACK);
    }

    @Test
    public void testPaintHead(){
        GameView.paintHead(50, 50, 10);
        assertEquals(GameView.whole[5][5].getFill(), Color.YELLOW);
    }

    @Test
    public void testPaintBody(){
        GameView.paintBody(50, 50, 10);
        assertEquals(GameView.whole[5][5].getFill(), Color.BLUE);
    }

    @Test
    public void testPaintFood(){
        GameView.paintFood(50, 50, 10);
        assertEquals(GameView.whole[5][5].getFill(), Color.RED);
    }

}