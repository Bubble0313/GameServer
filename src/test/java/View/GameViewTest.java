package View;

import de.saxsys.javafx.test.JfxRunner;
import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(JfxRunner.class)
public class GameViewTest {

    private GameView gameView;

    @Before
    public void setup(){
        gameView = new GameView(10, 165, 150, 15, 15);
    }

    @Test
    public void testViewConstructor(){
        assertEquals(gameView.getInitialText().getText(), "Welcome to the Game of Snake!\n" +
                "Start the game by choosing player number, game level and player name(s).\n " +
                "The higher the level you choose, the quicker the snake moves.\n " +
                "If there's no input, default player name is Bot, default game level is Level 1.");
        assertEquals(gameView.getNumText().getText(), "Player number:");
        assertEquals(gameView.getPlayerChoiceBox().getItems().get(0), "1");
        assertEquals(gameView.getSpeedText().getText(), "Game Level:");
        assertEquals(gameView.getLevelChoiceBox().getItems().get(0), "Level 1");
        assertEquals(gameView.getName1Text().getText(), "Player1 name:\n(Use keyboard arrows\n" +
                "to control direction)");
        assertNotNull(gameView.getName1TextField());
        assertEquals(gameView.getName2Text().getText(), "Player2 name:\n(Use 4 keys WASD\n" +
                "to control direction)");
        assertNotNull(gameView.getName2TextField());
        assertEquals(gameView.getButton().getText(), "Start");
        assertNotNull(gameView.getFirstScene());
        assertNotNull(gameView.getSecondScene());
        assertEquals(gameView.getCurrentScore1Text().getText(), "");
        assertEquals(gameView.getCurrentScore2Text().getText(), "");
        assertEquals(gameView.getBestScoreText().getText(), "Best Score: N/A");
        assertEquals(gameView.getBestPlayerText().getText(), "Best Player: N/A");
        assertEquals(gameView.getSnakeNum().intValue(), 1);
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