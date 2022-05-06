package Controller;

import Constants.GameConstants;
import Model.Direction;
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
            Direction direction = model.getLastDirection();
            //user cannot directly change the direction to its opposite
            if (keyCode == KeyCode.UP && direction != Direction.DOWN) {
                senDir[0] = 2;
                senDir[1] = 0;
                model.setDirection(Direction.UP);
            }
            if (keyCode == KeyCode.DOWN && direction != Direction.UP) {
                senDir[0] = 2;
                senDir[1] = 1;
                model.setDirection(Direction.DOWN);
            }
            if (keyCode == KeyCode.LEFT && direction != Direction.RIGHT) {
                senDir[0] = 2;
                senDir[1] = 2;
                model.setDirection(Direction.LEFT);
            }
            if (keyCode == KeyCode.RIGHT && direction != Direction.LEFT) {
                senDir[0] = 2;
                senDir[1] = 3;
                model.setDirection(Direction.RIGHT);
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
                System.out.println("Client received: " + recPacket);

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
                    model.initialiseSnake(model.getLength(), headX, headY, grid);
                    paintSnake(model.getLength(), headX, headY);
                    view.paintFood(foodX, foodY);
                    model.setIsStart(true);
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
        while (model.getIsStart().equals(true)){
            view.paintTail(model.getSnakeX().get(model.getLength() - 1)/grid,
                    model.getSnakeY().get(model.getLength() - 1)/grid);
            model.updateSnake(grid, gameWidth, gameHeight);
            view.paintHead(model.getSnakeX().get(0)/grid, model.getSnakeY().get(0)/grid);//paint the new head
            view.paintBody(model.getSnakeX().get(1)/grid, model.getSnakeY().get(1)/grid);//paint the old head to the colour of body
            try {
                Thread.sleep(model.getSpeed());
            } catch (InterruptedException exception) {
                LOGGER.error(exception.getMessage());
            }
        }
    }

    private void diffElement(ArrayList<Integer> list) {
        switch (list.get(0)){
            case 0:
                headX = list.get(1);
                System.out.println("snake head x: " + list.get(1));
                break;
            case 1:
                headY = list.get(1);
                System.out.println("snake head y: " + list.get(1));
                break;
            case 2:
                model.setLength(list.get(1));
                System.out.println("snake length: " + list.get(1));
                break;
            case 3:
                foodX = list.get(1);
                System.out.println("foodX: " + foodX);
                break;
            case 4:
                foodY = list.get(1);
                System.out.println("foodY: " + foodY);
                break;
            case 5:
                increaseSnakeLength(model);
                view.getCurrentScoreText().setText("Current score: " + model.getScore());
                System.out.println("new snake length: " + model.getLength());
                foodX = list.get(1);
                foodY = list.get(2);
                view.paintFood(foodX, foodY);
                System.out.println("new foodX: " + foodX);
                System.out.println("new foodY: " + foodY);
                break;
            case 6:
                model.setIsStart(false);
                System.out.println("hit body, game stop");
                break;
        }
    }

    public void sendNameLevelToServer(){
        //send player name to server
        String input = view.getNameTextField().getText();
        //ignoring complex characters, only presenting words and spaces, using regular expression
        String validInput = Pattern.compile("[\\W&&\\S]*", Pattern.CASE_INSENSITIVE).matcher(input).replaceAll("");
        if (!validInput.isEmpty()) {
            model.setPlayer(validInput);
            senName = validInput.getBytes();
        } else {
            senName = model.getPlayer().getBytes();
        }
        sendToServer(senName);

        //send game level to server
        senLevel[0] = 1;
        if (view.getLevelChoiceBox().getSelectionModel().isEmpty()){
            LOGGER.info("Default level 1");
            senLevel[1] = 1;
        } else {
            int selectedLevel = view.getLevelChoiceBox().getSelectionModel().getSelectedIndex() + 1;
            setModelSpeed(model, selectedLevel);
            senLevel[1] = (byte) (selectedLevel);
        }
        sendToServer(senLevel);
    }


    @Override
    public void startGame() {
        //open a udp socket
        try {
            socket = new DatagramSocket();
        }catch (Exception e){
            e.printStackTrace();
        }

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