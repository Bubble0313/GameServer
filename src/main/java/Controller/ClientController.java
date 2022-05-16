package Controller;

import Constants.GameConstants;
import Model.Direction;
import Model.GameModel;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.regex.Pattern;

@Getter
@Setter
public class ClientController extends GameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientController.class);
    private byte[] senName = new byte[GameConstants.longPacketLength];
    private byte[] senLevel = new byte[GameConstants.shortPacketLength];
    private byte[] senDir = new byte[GameConstants.shortPacketLength];
    private DatagramSocket socket;

    private EventHandler<KeyEvent> keyEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            KeyCode keyCode = event.getCode();
            Direction direction = models.get(0).getLastDirection();
            //user cannot directly change the direction to its opposite
            if (keyCode == KeyCode.UP && direction != Direction.DOWN) {
                senDir[0] = 0;
                models.get(0).setDirection(Direction.UP);
            }
            if (keyCode == KeyCode.DOWN && direction != Direction.UP) {
                senDir[0] = 1;
                models.get(0).setDirection(Direction.DOWN);
            }
            if (keyCode == KeyCode.LEFT && direction != Direction.RIGHT) {
                senDir[0] = 2;
                models.get(0).setDirection(Direction.LEFT);
            }
            if (keyCode == KeyCode.RIGHT && direction != Direction.LEFT) {
                senDir[0] = 3;
                models.get(0).setDirection(Direction.RIGHT);
            }
            sendToServer(MessageType.DIRECTION, senDir);
        }
    };//change the direction of the snake based on the keyboard input

    public void sendToServer(MessageType type, byte[] senData){
        try {
            Packet packet = new Packet(type, senData);
            byte[] data = SerializationUtils.serialize(packet);
            InetAddress address = InetAddress.getByName("localhost");
            DatagramPacket senPacket = new DatagramPacket(data, data.length, address, 8088);
            socket.send(senPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recFromServer(){
        try {
            int count = 0;
            while (true){
                //receive info from server
                DatagramPacket recPacket = new DatagramPacket(new byte[GameConstants.longPacketLength],GameConstants.longPacketLength);
                socket.receive(recPacket);
                LOGGER.info("Client received: {}", recPacket);

                //get received data
                byte[] recData = recPacket.getData();
                Packet packet = SerializationUtils.deserialize(recData);

                //differentiate elements
                diffElement(packet);
                count++;

                //once receiving all data, initialise the snake on client side
                if (count == 5){
                    models.get(0).initialiseSnake(models.get(0).getLength(), headX, headY, grid);
                    view.paintSnake(models.get(0).getLength(), headX, headY);
                    view.paintFood(foodX, foodY);
                    models.get(0).setIsStart(true);
                    Runnable startRun = new Runnable() {
                        @Override
                        public void run() {
                            startRunning();
                        }
                    };
                    Thread startThread = new Thread(startRun);
                    startThread.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startRunning() {
        while (models.get(0).getIsStart().equals(true)){
            view.paintTail(models.get(0).getSnakeX().get(models.get(0).getLength() - 1)/grid,
                    models.get(0).getSnakeY().get(models.get(0).getLength() - 1)/grid);
            models.get(0).updateSnake(grid, gameWidth, gameHeight);
            view.paintHead(models.get(0).getSnakeX().get(0)/grid, models.get(0).getSnakeY().get(0)/grid);//paint the new head
            view.paintBody(models.get(0).getSnakeX().get(1)/grid, models.get(0).getSnakeY().get(1)/grid);//paint the old head to the colour of body
            try {
                Thread.sleep(models.get(0).getSpeed());
            } catch (InterruptedException exception) {
                LOGGER.error(exception.getMessage());
            }
        }
    }

    private void diffElement(Packet packet) {
        switch (packet.getType()){
            case SNAKE_HEAD_X:
                headX = packet.getPayload()[0];
                LOGGER.info("snake head x: {}", headX);
                break;
            case SNAKE_HEAD_Y:
                headY = packet.getPayload()[0];
                LOGGER.info("snake head y: {}", headY);
                break;
            case SNAKE_LENGTH:
                int length = packet.getPayload()[0];
                models.get(0).setLength(length);
                LOGGER.info("snake length: {}", length);
                break;
            case FOOD_X:
                foodX = packet.getPayload()[0];
                LOGGER.info("foodX: {}", foodX);
                break;
            case FOOD_Y:
                foodY = packet.getPayload()[0];
                LOGGER.info("foodY: {}", foodY);
                break;
            case EATEN:
                models.get(0).increaseLength();
                view.getCurrentScoreText().setText("Current score: " + models.get(0).getScore());
                LOGGER.info("new snake length: {}", models.get(0).getLength());
                foodX = packet.getPayload()[0];
                foodY = packet.getPayload()[1];
                view.paintFood(foodX, foodY);
                LOGGER.info("new foodX: {}", foodX);
                LOGGER.info("new foodY: {}", foodY);
                break;
            case HIT:
                models.get(0).setIsStart(false);
                LOGGER.info("hit body, game stop");
                break;
        }
    }

    public void sendNameLevelToServer(){
        //send player name to server
        String input = view.getNameTextField().getText();
        //ignoring complex characters, only presenting words and spaces, using regular expression
        String validInput;
        validInput = Pattern.compile("[\\W&&\\S]*", Pattern.CASE_INSENSITIVE).matcher(input).replaceAll("");
        if (!validInput.isEmpty()) {
            models.get(0).setPlayer(validInput);
        } else {
            validInput = models.get(0).getPlayer();
        }
        senName = validInput.getBytes();
        sendToServer(MessageType.NAME, senName);

        //send game level to server
        int selectedLevel;
        if (view.getLevelChoiceBox().getSelectionModel().isEmpty()){
            selectedLevel = 1;
            LOGGER.info("Default level 1");
        } else {
            selectedLevel = view.getLevelChoiceBox().getSelectionModel().getSelectedIndex() + 1;
            models.get(0).setSnakeSpeed(selectedLevel);
        }
        senLevel[0] = (byte) selectedLevel;
        sendToServer(MessageType.LEVEL, senLevel);
    }


    @Override
    public void startGame() {
        //open a udp socket
        try {
            if (view.getPortChoiceBox().getSelectionModel().getSelectedIndex()==0){
                socket = new DatagramSocket(1111);
            }
            if (view.getPortChoiceBox().getSelectionModel().getSelectedIndex()==1){
                socket = new DatagramSocket(2222);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //create a new model, set values for models
        GameModel model = new GameModel();
        models.add(model);

        //create a new thread to send info to server
        Runnable sendRun = new Runnable() {
            @Override
            public void run() {
                sendNameLevelToServer();
            }
        };
        Thread sendThread = new Thread(sendRun);
        sendThread.start();

        //create a new thread to receive snake head location from server, and use it to print the initial snake
        Runnable recRun = new Runnable() {
            @Override
            public void run() {
                recFromServer();
            }
        };
        Thread recThread = new Thread(recRun);
        recThread.start();

        //enable key event handler
        view.getSecondScene().addEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);
    }
}