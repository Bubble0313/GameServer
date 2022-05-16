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
import org.ini4j.Wini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;
import java.util.regex.Pattern;

@Getter
@Setter
public class ServerController extends GameController {
    private File recordFile;
    private File iniFile;
    private Random random = new Random();
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerController.class);
    private DatagramSocket mainSocket;
    private InetAddress clientAddress;
    private Integer clientPort;
    private byte[] fx = new byte[GameConstants.shortPacketLength];
    private byte[] fy = new byte[GameConstants.shortPacketLength];
    private byte[] hx = new byte[GameConstants.shortPacketLength];
    private byte[] hy = new byte[GameConstants.shortPacketLength];
    private byte[] len = new byte[GameConstants.shortPacketLength];
    private byte[] foo = new byte[GameConstants.shortPacketLength];
    private byte[] end = new byte[GameConstants.shortPacketLength];

    private EventHandler<KeyEvent> keyEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            KeyCode keyCode = event.getCode();
            Direction direction = models.get(0).getLastDirection();
            if (keyCode == KeyCode.UP && direction != Direction.DOWN) {
                models.get(0).setDirection(Direction.UP);//user cannot directly change the direction to its opposite
            }
            if (keyCode == KeyCode.DOWN && direction != Direction.UP) {
                models.get(0).setDirection(Direction.DOWN);
            }
            if (keyCode == KeyCode.LEFT && direction != Direction.RIGHT) {
                models.get(0).setDirection(Direction.LEFT);
            }
            if (keyCode == KeyCode.RIGHT && direction != Direction.LEFT) {
                models.get(0).setDirection(Direction.RIGHT);
            }
        }
    };//change the direction of the snake based on the keyboard input

    private void sendSnakeInfoToClient(DatagramSocket socket,GameModel model, InetAddress clientAddress, Integer clientPort) {
        hx[0] = (byte) headX;
        hy[0] = (byte) headY;
        len[0] = (byte) model.getLength();
        sendToClient(socket, clientAddress, clientPort, MessageType.SNAKE_HEAD_X, hx);
        sendToClient(socket, clientAddress, clientPort, MessageType.SNAKE_HEAD_Y, hy);
        sendToClient(socket, clientAddress, clientPort, MessageType.SNAKE_LENGTH, len);
        view.paintSnake(model.getLength(), headX, headY);
    }

    private void sendFoodToClient(DatagramSocket socket, InetAddress clientAddress, Integer clientPort) {
        showFood();
        fx[0] = (byte) foodX;
        fy[0] = (byte) foodY;
        sendToClient(socket, clientAddress, clientPort, MessageType.FOOD_X, fx);
        sendToClient(socket, clientAddress, clientPort, MessageType.FOOD_Y, fy);
    }

    private void diffElement(GameModel model, Packet packet) {
        switch (packet.getType()) {
            case LEVEL:
                int level = packet.getPayload()[0];
                model.setSnakeSpeed(level);
                LOGGER.info("level: {}", level);
                break;
            case DIRECTION:
                int dir = packet.getPayload()[0];
                setDirection(model, dir);
                LOGGER.info("direction: {}", dir);
                break;
            case NAME:
                String name = new String(packet.getPayload(), 0, packet.getPayload().length);
                model.setPlayer(name);
                LOGGER.info("name: {}", name);
        }
    }

    public void sendToClient(DatagramSocket socket, InetAddress clientAddress, Integer clientPort, MessageType type, byte[] senData) {
        try {
            Packet packet = new Packet(type, senData);
            byte[] data = SerializationUtils.serialize(packet);
            DatagramPacket response = new DatagramPacket(data, data.length, clientAddress, clientPort);
            socket.send(response);
            LOGGER.info("Sent {}", response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDirection(GameModel model, int dir) {
        switch (dir) {
            case 0:
                model.setDirection(Direction.UP);
                break;
            case 1:
                model.setDirection(Direction.DOWN);
                break;
            case 2:
                model.setDirection(Direction.LEFT);
                break;
            case 3:
                model.setDirection(Direction.RIGHT);
                break;
        }
    }

    public void tickUpdate(GameModel model, DatagramSocket socket, InetAddress clientAddress, Integer clientPort) {
        //override run function - while game is true, for each model, move it forward, t.sleep(game speed)
        //e.g. 4 ticks per second, use 1000/4

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    //only run when the game start status is true
                    while (model.getIsStart().equals(true)) {
                        startRunning(model, socket, clientAddress, clientPort);
                    }
                }
            };
            Thread t = new Thread(r);
            t.start();//run in a separate thread

    }

    public void startRunning(GameModel model, DatagramSocket socket, InetAddress clientAddress, Integer clientPort) {
        if (model.getSnakeX().get(0).equals(foodX * grid) &&
                model.getSnakeY().get(0).equals(foodY * grid)) {
            //if the positions are the same, then the snake length increases 1,
            //update the snake, and the current score increases 1
            model.increaseLength();
            view.getCurrentScoreText().setText("Current score: " + model.getScore());
            //tell client to increase its snake length, send new food
            showFood();
            LOGGER.info("new foodX: {}", foodX);
            LOGGER.info("new foodY: {}", foodY);
            foo[0] = (byte) foodX;
            foo[1] = (byte) foodY;
            if (view.getMode().equals("Server")) {
                sendToClient(socket, clientAddress, clientPort, MessageType.EATEN, foo);
            }
        } else {
            //if the positions are not the same, then paint the tail to black
            view.paintTail(model.getSnakeX().get(model.getLength() - 1) / grid,
                    model.getSnakeY().get(model.getLength() - 1) / grid);
        }
        model.updateSnake(grid, gameWidth, gameHeight);//change the coordinates of the snake in every move
        view.paintHead(model.getSnakeX().get(0) / grid, model.getSnakeY().get(0) / grid);//paint the new head
        view.paintBody(model.getSnakeX().get(1) / grid, model.getSnakeY().get(1) / grid);//paint the old head to the colour of body
        //after the move, check if snake head hits its own body
        for (int i = 1; i < model.getLength(); i++) {
            if (model.getSnakeX().get(0).equals(model.getSnakeX().get(i)) &&
                    model.getSnakeY().get(0).equals(model.getSnakeY().get(i))) {
                //if yes, then stop the game
                model.setIsStart(false);
                if (view.getMode().equals("Server")) {
                    sendToClient(socket, clientAddress, clientPort, MessageType.HIT, end);
                }
                //write to the file only when the new score is higher than the previous best score
                if (model.getScore() > bestScore) {
                    bestScore = model.getScore();
                    bestPlayer = model.getPlayer();
                    writeToRecord();
                }
                //save to ini file
                saveIni();
            }
        }
        try {
            Thread.sleep(model.getSpeed());
        } catch (InterruptedException exception) {
            LOGGER.error(exception.getMessage());
        }
    }

    public void showFood() {
        foodX = random.nextInt(num - 1);
        foodY = random.nextInt(num - 1);
        view.paintFood(foodX, foodY);
    }

    public File createFile(String path) {
        try {
            File file = new File(path);
            if (file.createNewFile()) {
                LOGGER.info("File {} is created successfully", file.getName());
            } else {
                LOGGER.info("File {} already exists", file.getName());
            }
            return file;
        } catch (IOException exception) {
            LOGGER.error(exception.getMessage());
            exception.printStackTrace();
            return null;
        }
    }

    public void writeToRecord() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(recordFile));
            bufferedWriter.write(bestPlayer + ":" + bestScore);
            bufferedWriter.flush();
            bufferedWriter.close();
            LOGGER.info("Content was successfully wrote to the file.");
        } catch (IOException exception) {
            LOGGER.error(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void readFromRecord() {
        String line;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(recordFile));
            if ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                bestPlayer = parts[0];
                bestScore = Integer.parseInt(parts[1]);
            } else {
                LOGGER.info("No record yet.");
            }
            bufferedReader.close();

        } catch (IOException exception) {
            LOGGER.error(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void getIni() {
        try {
            Wini ini = new Wini(iniFile);
            if (iniFile.length() == 0) {
                LOGGER.info("No content in ini yet.");
            } else {
                view.getFirstScene().getWindow().setX(ini.get("Screen", "Horizontal Location", double.class));
                view.getFirstScene().getWindow().setY(ini.get("Screen", "Vertical Location", double.class));
                view.getFirstScene().getWindow().setWidth(ini.get("Screen", "Width", double.class));
                view.getFirstScene().getWindow().setHeight(ini.get("Screen", "Height", double.class));
            }
        } catch (IOException exception) {
            LOGGER.error(exception.getMessage());
        }
    }

    public void saveIni() {
        try {
            Wini ini = new Wini(iniFile);
            ini.put("Screen", "Horizontal Location", view.getSecondScene().getWindow().getX());
            ini.put("Screen", "Vertical Location", view.getSecondScene().getWindow().getY());
            ini.put("Screen", "Width", view.getSecondScene().getWindow().getWidth());
            ini.put("Screen", "Height", view.getSecondScene().getWindow().getHeight());
            ini.store();
        } catch (IOException exception) {
            LOGGER.error(exception.getMessage());
        }
    }

    @Override
    public void startGame() {
        //set up model head
        headX = GameConstants.snakeLength + random.nextInt(num / 2);
        headY = GameConstants.snakeLength + random.nextInt(num / 2);

        if (view.getMode().equals("Server")) {
            networkOperation();
        } else {
            localOperation();
        }
    }

    public void localOperation() {
        GameModel model = createNewModel();
        //set the speed of snake and the player based on user input
        model.setSnakeSpeed(view.getLevelChoiceBox().getSelectionModel().getSelectedIndex());
        String input = view.getNameTextField().getText();
        //ignoring complex characters, only presenting words and spaces, using regular expression
        String validInput = Pattern.compile("[\\W&&\\S]*", Pattern.CASE_INSENSITIVE).matcher(input).replaceAll("");
        if (!input.isEmpty()) {
            model.setPlayer(validInput);
        }
        view.paintSnake(model.getLength(), headX, headY);
        showFood();
        tickUpdate(model, mainSocket, clientAddress, clientPort);
        //enable key event handler
        view.getSecondScene().addEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);
    }

    public GameModel createNewModel() {
        //create a new model
        GameModel model = new GameModel();
        models.add(model);
        model.initialiseSnake(GameConstants.snakeLength, headX, headY, grid);
        model.setIsStart(true);
        return model;
    }

    private void networkOperation() {
        //open a udp socket
        try {
            mainSocket = new DatagramSocket(8088);
            DatagramSocket socket1 = new DatagramSocket(8089);
            DatagramSocket socket2 = new DatagramSocket(8090);
            Set<String> clientSet = new LinkedHashSet<>();
            Runnable mainRun = new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            //receive info from client
                            DatagramPacket request = new DatagramPacket(new byte[GameConstants.longPacketLength], GameConstants.longPacketLength);
                            mainSocket.receive(request);
                            clientAddress = request.getAddress();
                            clientPort = request.getPort();
                            if (clientPort==1111){
                                DatagramPacket intermittentPacket1 = new DatagramPacket(request.getData(), request.getLength(), InetAddress.getByName("localhost"), 8089);
                                mainSocket.send(intermittentPacket1);
                            }
                            if(clientPort==2222){
                                DatagramPacket intermittentPacket2 = new DatagramPacket(request.getData(), request.getLength(), InetAddress.getByName("localhost"), 8090);
                                mainSocket.send(intermittentPacket2);
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            };
            Thread mainThread = new Thread(mainRun);
            mainThread.start();

            //create a new thread for every client
            Runnable interRun1 = new Runnable() {
                @Override
                public void run() {
                    GameModel model = createNewModel();

                    try {
                        int count = 0;
                        while (model.getIsStart().equals(true)) {
                            DatagramPacket intermittentRequest = new DatagramPacket(new byte[GameConstants.longPacketLength], GameConstants.longPacketLength);
                            socket1.receive(intermittentRequest);

                            //get received bytes
                            byte[] recData = intermittentRequest.getData();
                            Packet packet = SerializationUtils.deserialize(recData);

                            //differentiate received element
                            diffElement(model, packet);

                            count++;

                            //after receiving player and level, send info to client and start the tick
                            if (count == 2) {
                                sendSnakeInfoToClient(socket1, model, clientAddress, clientPort);
                                sendFoodToClient(socket1, clientAddress, clientPort);
                                tickUpdate(model, socket1,clientAddress, clientPort);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread interThread1 = new Thread(interRun1);
            interThread1.start();

            Runnable interRun2 = new Runnable() {
                @Override
                public void run() {
                    GameModel model = createNewModel();

                    try {
                        int count = 0;
                        while (model.getIsStart().equals(true)) {
                            DatagramPacket intermittentRequest = new DatagramPacket(new byte[GameConstants.longPacketLength], GameConstants.longPacketLength);
                            socket2.receive(intermittentRequest);

                            //get received bytes
                            byte[] recData = intermittentRequest.getData();
                            Packet packet = SerializationUtils.deserialize(recData);

                            //differentiate received element
                            diffElement(model, packet);

                            count++;

                            //after receiving player and level, send info to client and start the tick
                            if (count == 2) {
                                sendSnakeInfoToClient(socket2, model, clientAddress, clientPort);
                                sendFoodToClient(socket2, clientAddress, clientPort);
                                tickUpdate(model, socket2, clientAddress, clientPort);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread interThread2 = new Thread(interRun2);
            interThread2.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fileOperation(String record, String ini) {
        //create the file to store best player and best score when there is no such file
        setRecordFile(createFile(record));
        setIniFile(createFile(ini));
        //read the best play and best score from the file
        readFromRecord();
        getIni();
    }
}