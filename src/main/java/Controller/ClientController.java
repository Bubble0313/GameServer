package Controller;

import Constants.GameConstants;
import Model.Direction;
import Model.GameModel;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.regex.Pattern;

@Getter
@Setter
public class ClientController extends GameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientController.class);
    //first byte: 1 means level, 2 means direction
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
                senDir[0] = 2;
                senDir[1] = 0;
                models.get(0).setDirection(Direction.UP);
            }
            if (keyCode == KeyCode.DOWN && direction != Direction.UP) {
                senDir[0] = 2;
                senDir[1] = 1;
                models.get(0).setDirection(Direction.DOWN);
            }
            if (keyCode == KeyCode.LEFT && direction != Direction.RIGHT) {
                senDir[0] = 2;
                senDir[1] = 2;
                models.get(0).setDirection(Direction.LEFT);
            }
            if (keyCode == KeyCode.RIGHT && direction != Direction.LEFT) {
                senDir[0] = 2;
                senDir[1] = 3;
                models.get(0).setDirection(Direction.RIGHT);
            }
            sendToServer(senDir);
        }
    };//change the direction of the snake based on the keyboard input

    public void sendToServer(byte[] senData){
        try {
            InetAddress address = InetAddress.getByName("localhost");
            DatagramPacket senPacket = new DatagramPacket(senData,senData.length, address, 8088);
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
                DatagramPacket recPacket = new DatagramPacket(new byte[GameConstants.shortPacketLength],GameConstants.shortPacketLength);
                socket.receive(recPacket);
                LOGGER.info("Client received: {}", recPacket);

                //get received data
                byte[] recData = recPacket.getData();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(recData);
                int element;
                ArrayList<Integer> list = new ArrayList<>();
                while ((element = byteArrayInputStream.read()) != -1){
                    list.add(element);
                }

                //differentiate elements
                diffElement(list);
                count++;

                //once receiving all data, initialise the snake on client side
                if (count == 5){
                    models.get(0).initialiseSnake(models.get(0).getLength(), headX, headY, grid);
                    paintSnake(models.get(0).getLength(), headX, headY);
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

    private void diffElement(ArrayList<Integer> list) {
        switch (list.get(0)){
            case 0:
                headX = list.get(1);
                LOGGER.info("snake head x: {}", list.get(1));
                break;
            case 1:
                headY = list.get(1);
                LOGGER.info("snake head y: {}", list.get(1));
                break;
            case 2:
                models.get(0).setLength(list.get(1));
                LOGGER.info("snake length: {}", list.get(1));
                break;
            case 3:
                foodX = list.get(1);
                LOGGER.info("foodX: {}", foodX);
                break;
            case 4:
                foodY = list.get(1);
                LOGGER.info("foodY: {}", foodY);
                break;
            case 5:
                increaseSnakeLength(models.get(0));
                view.getCurrentScoreText().setText("Current score: " + models.get(0).getScore());
                LOGGER.info("new snake length: {}", models.get(0).getLength());
                foodX = list.get(1);
                foodY = list.get(2);
                view.paintFood(foodX, foodY);
                LOGGER.info("new foodX: {}", foodX);
                LOGGER.info("new foodY: {}", foodY);
                break;
            case 6:
                models.get(0).setIsStart(false);
                LOGGER.info("hit body, game stop");
                break;
        }
    }

    public void sendNameLevelToServer(){
        //send player name to server
        String input = view.getNameTextField().getText();
        //ignoring complex characters, only presenting words and spaces, using regular expression
        String validInput = Pattern.compile("[\\W&&\\S]*", Pattern.CASE_INSENSITIVE).matcher(input).replaceAll("");
        if (!validInput.isEmpty()) {
            models.get(0).setPlayer(validInput);
            senName = validInput.getBytes();
        } else {
            senName = models.get(0).getPlayer().getBytes();
        }
        sendToServer(senName);

        //send game level to server
        senLevel[0] = 1;
        if (view.getLevelChoiceBox().getSelectionModel().isEmpty()){
            LOGGER.info("Default level 1");
            senLevel[1] = 1;
        } else {
            int selectedLevel = view.getLevelChoiceBox().getSelectionModel().getSelectedIndex() + 1;
            setModelSpeed(models.get(0), selectedLevel);
            senLevel[1] = (byte) (selectedLevel);
        }
        sendToServer(senLevel);
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