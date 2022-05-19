package Controller;

import Model.GameModel;
import View.GameView;
import de.saxsys.javafx.test.JfxRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

//@RunWith(JfxRunner.class)
public class ClientControllerTest {
    private ClientController clientController = new ClientController();

    @Test
    public void testDifFElement(){
        byte[] payload = new byte[10];
        payload[0] = 3;
        Packet packet = new Packet(MessageType.SNAKE_HEAD_X, payload);
        clientController.diffElement(packet);
        assertEquals(clientController.headX, 3);
        packet.setType(MessageType.SNAKE_HEAD_Y);
        clientController.diffElement(packet);
        assertEquals(clientController.headY, 3);
        GameModel model = new GameModel();
        clientController.models.add(model);
        packet.setType(MessageType.SNAKE_LENGTH);
        clientController.diffElement(packet);
        assertEquals(clientController.getModels().get(0).getLength(), 3);
        packet.setType(MessageType.FOOD_X);
        clientController.diffElement(packet);
        assertEquals(clientController.foodX, 3);
        packet.setType(MessageType.FOOD_Y);
        clientController.diffElement(packet);
        assertEquals(clientController.foodY, 3);
        packet.setType(MessageType.HIT);
        clientController.models.get(0).setIsStart(true);
        clientController.diffElement(packet);
        assertEquals(clientController.getModels().get(0).getIsStart(), false);
        GameView gameView = new GameView(165, 150);
        gameView.createSecondScene(10, 165, 15, 10);
        clientController.setView(gameView);
        packet.setType(MessageType.EATEN);
        clientController.diffElement(packet);
        assertEquals(clientController.getModels().get(0).getLength(), 4);
    }
}