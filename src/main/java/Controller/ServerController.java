package Controller;

import Constants.GameConstants;
import Model.Direction;
import Model.GameModel;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.Getter;
import lombok.Setter;
import org.ini4j.Wini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

@Getter
@Setter
public class ServerController extends GameController {
    private ArrayList<GameModel> models = new ArrayList<>();
    private File recordFile;
    private File iniFile;
    private Random random = new Random();
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerController.class);
    private DatagramSocket socket;
    private InetAddress clientAddress;
    private Integer clientPort;

    private EventHandler<KeyEvent> keyEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            KeyCode keyCode = event.getCode();
            Direction direction = model.getLastDirection();
            if (keyCode == KeyCode.UP && direction != Direction.DOWN) {
                model.setDirection(Direction.UP);//user cannot directly change the direction to its opposite
            }
            if (keyCode == KeyCode.DOWN && direction != Direction.UP) {
                model.setDirection(Direction.DOWN);
            }
            if (keyCode == KeyCode.LEFT && direction != Direction.RIGHT) {
                model.setDirection(Direction.LEFT);
            }
            if (keyCode == KeyCode.RIGHT && direction != Direction.LEFT) {
                model.setDirection(Direction.RIGHT);
            }
        }
    };//change the direction of the snake based on the keyboard input


    public void recFromClient(GameModel model){
        try {
            int count = 0;
            while (model.getIsStart().equals(true)){
                //receive info from client
                DatagramPacket request = new DatagramPacket(new byte[GameConstants.longPacketLength],GameConstants.longPacketLength);
                socket.receive(request);
                clientAddress = request.getAddress();
                clientPort = request.getPort();

                //get received bytes
                byte[] recData = request.getData();
                if (request.getLength() != GameConstants.shortPacketLength){
                    String name = new String(recData, 0, request.getLength());
                    model.setPlayer(name);
                    LOGGER.info("name: {}", name);
                } else {
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(recData);
                    int element;
                    ArrayList<Integer> list = new ArrayList<>();
                    while ((element = byteArrayInputStream.read()) != -1){
                        list.add(element);
                    }

                    //differentiate received element
                    diffElement(model, list);
                }
                count++;

                //after receiving player and level, send info to client and start the tick
                if (count == 2){
                    sendSnakeInfoToClient(model);
                    sendFoodToClient();
                    tickUpdate();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendSnakeInfoToClient(GameModel model) {
        sendMethod(Arrays.asList(0, headX));
        sendMethod(Arrays.asList(1, headY));
        sendMethod(Arrays.asList(2, model.getLength()));
        paintSnake(model.getLength(), headX, headY);
    }

    private void sendFoodToClient(){
        showFood();
        sendMethod(Arrays.asList(3, foodX));
        sendMethod(Arrays.asList(4, foodY));
    }

    private void diffElement(GameModel model, ArrayList<Integer> list) {
        switch (list.get(0)){
            case 1:
                setModelSpeed(model, list.get(1));
                LOGGER.info("level: {}", list.get(1));
                break;
            case 2:
                setDirection(model, list.get(1));
                LOGGER.info("direction: {}", list.get(1));
                break;
            default:
                int zeroChar = list.get(0);
                int oneChar = list.get(1);
                String name = "" + (char) zeroChar + (char) oneChar;
                model.setPlayer(name);
                LOGGER.info("name: {}", name);
        }
    }

    public void sendMethod(List<Integer> list){
        byte[] newArray = new byte[GameConstants.shortPacketLength];
        for (int i = 0; i <list.size(); i++){
            int value = list.get(i);
            newArray[i] = (byte) value;
        }
        sendToClient(newArray);
    }

    public void sendToClient(byte[] buffer){
        try {
            DatagramPacket response = new DatagramPacket(buffer, GameConstants.shortPacketLength, clientAddress, clientPort);
            socket.send(response);
            LOGGER.info("Sent {}", response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setDirection(GameModel model, int dir){
        switch (dir){
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

    public void tickUpdate() {
        //override run function - while game is true, for each model, move it forward, t.sleep(game speed)
        //e.g. 4 ticks per second, use 1000/4
        for (GameModel model : models) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    //only run when the game start status is true
                    while (model.getIsStart().equals(true)) {
                        startRunning(model);
                    }
                }
            };
            Thread t = new Thread(r);
            t.start();//run in a separate thread
        }
    }

    public void startRunning(GameModel model) {
        if (model.getSnakeX().get(0).equals(foodX*grid) &&
                model.getSnakeY().get(0).equals(foodY*grid)) {
            //if the positions are the same, then the snake length increases 1,
            //update the snake, and the current score increases 1
            increaseSnakeLength(model);
            view.getCurrentScoreText().setText("Current score: " + model.getScore());
            //tell client to increase its snake length, send new food
            showFood();
            LOGGER.info("new foodX: {}", foodX);
            LOGGER.info("new foodY: {}", foodY);
            if (view.getMode().equals("Server")){
                sendMethod(Arrays.asList(5, foodX, foodY));
            }
        } else {
            //if the positions are not the same, then paint the tail to black
            view.paintTail(model.getSnakeX().get(model.getLength() - 1)/grid,
                    model.getSnakeY().get(model.getLength() - 1)/grid);
        }
        model.updateSnake(grid, gameWidth, gameHeight);//change the coordinates of the snake in every move
        view.paintHead(model.getSnakeX().get(0)/grid, model.getSnakeY().get(0)/grid);//paint the new head
        view.paintBody(model.getSnakeX().get(1)/grid, model.getSnakeY().get(1)/grid);//paint the old head to the colour of body
        //after the move, check if snake head hits its own body
        for (int i = 1; i < model.getLength(); i++) {
            if (model.getSnakeX().get(0).equals(model.getSnakeX().get(i)) &&
                    model.getSnakeY().get(0).equals(model.getSnakeY().get(i))) {
                //if yes, then stop the game
                model.setIsStart(false);
                if (view.getMode().equals("Server")) {
                    sendMethod(Arrays.asList(6));
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
        //set up model
        headX = GameConstants.snakeLength + random.nextInt(num/2);
        headY = GameConstants.snakeLength + random.nextInt(num/2);
        model.initialiseSnake(GameConstants.snakeLength, headX, headY, grid);
        models.add(model);
        model.setIsStart(true);

        if (view.getMode().equals("Server")){
            networkOperation();
        }else {
            localOperation();
        }
    }

    private void localOperation() {
        //set the speed of snake and the player based on user input
        setModelSpeed(model, view.getLevelChoiceBox().getSelectionModel().getSelectedIndex());
        String input = view.getNameTextField().getText();
        //ignoring complex characters, only presenting words and spaces, using regular expression
        String validInput = Pattern.compile("[\\W&&\\S]*", Pattern.CASE_INSENSITIVE).matcher(input).replaceAll("");
        if (!input.isEmpty()) {
            model.setPlayer(validInput);
        }
        paintSnake(model.getLength(), headX, headY);
        showFood();
        tickUpdate();
        //enable key event handler
        view.getSecondScene().addEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);
    }

    private void networkOperation() {
        //open a udp socket
        try {
            socket = new DatagramSocket(8088);
        }catch (Exception e){
            e.printStackTrace();
        }

        //create a new thread for receiving info from client
        Runnable recRun = new Runnable() {
            @Override
            public void run() {
                recFromClient(model);
            }
        };
        Thread recThread = new Thread(recRun);
        recThread.start();
    }

    @Override
    public void fileOperation(){
        //create the file to store best player and best score when there is no such file
        setRecordFile(createFile("record.txt"));
        setIniFile(createFile("config.ini"));
        //read the best play and best score from the file
        readFromRecord();
        getIni();
    }
}