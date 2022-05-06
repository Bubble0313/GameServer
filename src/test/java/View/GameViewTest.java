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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JfxRunner.class)
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
                "Start the game by choosing player mode (Server VS Client).");
        assertEquals(gameView.getModeText().getText(), "Player Mode:");
        assertEquals(gameView.getModeChoiceBox().getItems().get(0), "Server");
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
    public void testPaintTail(){
        GameView.paintTail(5, 5);
        assertEquals(GameView.whole[5][5].getFill(), Color.BLACK);
    }

    @Test
    public void testPaintHead(){
        GameView.paintHead(5, 5);
        assertEquals(GameView.whole[5][5].getFill(), Color.YELLOW);
    }

    @Test
    public void testPaintBody(){
        GameView.paintBody(5, 5);
        assertEquals(GameView.whole[5][5].getFill(), Color.BLUE);
    }

    @Test
    public void testPaintFood(){
        GameView.paintFood(5, 5);
        assertEquals(GameView.whole[5][5].getFill(), Color.RED);
    }

}