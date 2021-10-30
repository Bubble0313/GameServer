package Controller;

import Constants.GameConstants;
import Model.Direction;
import Model.GameModel;
import View.GameView;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.ini4j.Wini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

@Getter
@Setter
public class GameController {
    private ArrayList<GameModel> models;
    private GameView view;

    private int foodX;//X coordinate of the food
    private int foodY;//Y coordinate of the food

    private int bestScore = -1;//the highest score
    private String bestPlayer = "N/A";//player that scores the highest

    private File recordFile;
    private File iniFile;

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    private int grid = GameConstants.grid;
    private int num = GameConstants.num;
    private Random random = new Random();
    private Map<GameModel, Text> modelTextMap = new HashMap<>();

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

    private EventHandler<KeyEvent> keyEventHandler2 = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            KeyCode keyCode = event.getCode();
            Direction direction = models.get(1).getLastDirection();
            if (keyCode == KeyCode.W && direction != Direction.DOWN) {
                models.get(1).setDirection(Direction.UP);//user cannot directly change the direction to its opposite
            }
            if (keyCode == KeyCode.S && direction != Direction.UP) {
                models.get(1).setDirection(Direction.DOWN);
            }
            if (keyCode == KeyCode.A && direction != Direction.RIGHT) {
                models.get(1).setDirection(Direction.LEFT);
            }
            if (keyCode == KeyCode.D && direction != Direction.LEFT) {
                models.get(1).setDirection(Direction.RIGHT);
            }
        }
    };

    private EventHandler<MouseEvent> mouseEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            GameModel model1 = new GameModel();
            model1.initialiseSnake(GameConstants.snakeLength, grid, num);
            paintSnake(model1);
            models.add(model1);
            //once the button is clicked, start the game
            models.get(0).setIsStart(true);
            //set the speed of snake and the player based on user input
            setModelSpeed(models.get(0), view.getLevelChoiceBox().getSelectionModel().getSelectedIndex());
            String nameInput1 = view.getName1TextField().getText();
            //ignoring complex characters, only presenting words and spaces, using regular expression
            String validNameInput1 = Pattern.compile("[\\W&&\\S]*", Pattern.CASE_INSENSITIVE).matcher(nameInput1).replaceAll("");
            if (!nameInput1.isEmpty()) {
                models.get(0).setPlayer(validNameInput1);
            }
            //mapping models, texts
            modelTextMap.put(models.get(0), view.getCurrentScore1Text());
            //update the player name on screen
            modelTextMap.get(models.get(0)).setText(models.get(0).getPlayer() + "'s current score: 0");
            //when there are 2 players
            if (view.getSnakeNum().equals(2)) {
                GameModel model2 = new GameModel();
                model2.initialiseSnake(GameConstants.snakeLength, grid, num);
                paintSnake(model2);
                models.add(model2);
                models.get(1).setIsStart(true);
                setModelSpeed(models.get(1), view.getLevelChoiceBox().getSelectionModel().getSelectedIndex());
                String nameInput2 = view.getName2TextField().getText();
                String validNameInput2 = Pattern.compile("[\\W&&\\S]*", Pattern.CASE_INSENSITIVE).matcher(nameInput2).replaceAll("");
                if (!nameInput1.isEmpty()) {
                    models.get(1).setPlayer(validNameInput2);
                }
                modelTextMap.put(models.get(1), view.getCurrentScore2Text());
                modelTextMap.get(models.get(1)).setText(models.get(1).getPlayer() + "'s current score: 0");
                modelTextMap.get(models.get(0)).setY(GameConstants.gameUpBorder * 0.25);
                modelTextMap.get(models.get(1)).setVisible(true);
            }
            //switch from the first scene to the second scene
            Stage stage = (Stage) view.getFirstScene().getWindow();
            stage.setScene(view.getSecondScene());
            //paint the initial food
            showFood();
            //start the game tick
            tickUpdate();
            //Handling snake movement direction based on keyboard input
            view.getSecondScene().addEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);
            if (view.getSnakeNum().equals(2)) {
                view.getSecondScene().addEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler2);
            }
        }
    };

    public static void setModelSpeed(GameModel gameModel, int selection) {
        switch (selection) {
            case 1:
                gameModel.setSpeed(Level.L1.speed);
                break;
            case 2:
                gameModel.setSpeed(Level.L2.speed);
                break;
            case 3:
                gameModel.setSpeed(Level.L3.speed);
                break;
            case 4:
                gameModel.setSpeed(Level.L4.speed);
                break;
            case 5:
                gameModel.setSpeed(Level.L5.speed);
                break;
            case 6:
                gameModel.setSpeed(Level.L6.speed);
                break;
            case 7:
                gameModel.setSpeed(Level.L7.speed);
                break;
            case 8:
                gameModel.setSpeed(Level.L8.speed);
                break;
            case 9:
                gameModel.setSpeed(Level.L9.speed);
                break;
        }
    }

    public void paintSnake(GameModel gameModel) {
        int snakeX = gameModel.getSnakeX().get(0);
        int snakeY = gameModel.getSnakeY().get(0);
        view.paintHead(snakeX, snakeY, grid);
        for (int i = gameModel.getLength() - 1; i > 0; i--) {
            view.paintBody(snakeX - grid * i, snakeY, grid);
        }
    }

    public void showFood() {
        foodX = grid * random.nextInt(num - 1);
        foodY = grid * random.nextInt(num - 1);
        view.paintFood(foodX, foodY, grid);
    }

    public void tickUpdate() {
        //override run function - while game is true, for each model, move it forward, t.sleep(game speed)
        //e.g. 4 ticks per second, use 1000/4
        for (GameModel model : models) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    //only when the game start status is true
                    while (model.getIsStart().equals(true)) {
                        if (model.getSnakeX().get(0).equals(foodX) &&
                                model.getSnakeY().get(0).equals(foodY)) {
                            //if the positions are the same, then the snake length increases 1,
                            //update the snake, and the current score increases 1
                            model.setLength(model.getLength() + 1);
                            ArrayList<Integer> arrayListX = model.getSnakeX();
                            ArrayList<Integer> arrayListY = model.getSnakeY();
                            arrayListX.add(foodX);//it doesn't matter what value is added as the tail
                            arrayListY.add(foodY);//as the value will be overridden when the snake moves
                            model.setSnakeX(arrayListX);
                            model.setSnakeY(arrayListY);
                            model.setScore(model.getScore() + 1);
                            modelTextMap.get(model).setText(model.getPlayer() + "'s current score: " + model.getScore());
                            //then show another new food
                            showFood();
                        } else {
                            //if the positions are not the same, then paint the tail to black
                            view.paintTail(model.getSnakeX().get(model.getLength() - 1),
                                    model.getSnakeY().get(model.getLength() - 1), grid);
                        }
                        model.updateSnake(grid, GameConstants.gameWidth, GameConstants.gameHeight);//change the coordinates of the snake in every move
                        view.paintHead(model.getSnakeX().get(0), model.getSnakeY().get(0), grid);//paint the new head
                        view.paintBody(model.getSnakeX().get(1), model.getSnakeY().get(1), grid);//paint the old head to the colour of body
                        //after the move, check if snake head hits its own body
                        for (int i = 1; i < model.getLength(); i++) {
                            if (model.getSnakeX().get(0).equals(model.getSnakeX().get(i)) &&
                                    model.getSnakeY().get(0).equals(model.getSnakeY().get(i))) {
                                stopGame(model);
                            }
                        }
                        try {
                            Thread.sleep(model.getSpeed());
                        } catch (InterruptedException exception) {
                            LOGGER.error(exception.getMessage());
                        }
                    }
                }
            };
            Thread t = new Thread(r);
            t.start();//run in a separate thread
        };
    }

    public void stopGame(GameModel gameModel) {
        //set game start status to false
        gameModel.setIsStart(false);
        modelTextMap.get(gameModel).setFill(Color.GREY);
        //write to the file only when the new score is higher than the previous best score
        if (gameModel.getScore() > bestScore) {
            bestScore = gameModel.getScore();
            bestPlayer = gameModel.getPlayer();
            writeToFile();
        }
        saveIni();
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

    public void writeToFile() {
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

    public void readFromFile() {
        String line;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(recordFile));
            if ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                bestPlayer = parts[0];
                view.getBestPlayerText().setText("Best Player: " + bestPlayer);
                bestScore = Integer.parseInt(parts[1]);
                view.getBestScoreText().setText("Best Score: " + bestScore);
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
            if (iniFile.length() == 0){
                LOGGER.info("No content in ini yet.");
            } else {
                view.getFirstScene().getWindow().setX(ini.get("Screen", "Horizontal Location", double.class));
                view.getFirstScene().getWindow().setY(ini.get("Screen", "Vertical Location", double.class));
                view.getFirstScene().getWindow().setWidth(ini.get("Screen", "Width", double.class));
                view.getFirstScene().getWindow().setHeight(ini.get("Screen", "Height", double.class));
                view.getPlayerChoiceBox().setValue(ini.get("Player Number", "number"));
                view.getName1TextField().setText(ini.get("Player1", "name"));
                view.getLevelChoiceBox().setValue(ini.get("Player1", "level"));
                if (view.getSnakeNum().equals(2)) {
                    view.getName2TextField().setText(ini.get("Player2", "name"));
                }
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
            ini.put("Player Number", "number", view.getSnakeNum());
            ini.put("Player1", "name", models.get(0).getPlayer());
            ini.put("Player1", "level", view.getLevelChoiceBox().getValue());
            if (view.getSnakeNum().equals(2)) {
                ini.put("Player2", "name", models.get(1).getPlayer());
            }
            ini.store();
        } catch (IOException exception) {
            LOGGER.error(exception.getMessage());
        }
    }

    public void startGame(String recordPath, String iniPath) {
        //add mouse clicking event handler to all the buttons
        view.getButton().addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandler);
        //create the file to store best player and best score when there is no such file
        setRecordFile(createFile(recordPath));
        setIniFile(createFile(iniPath));
        //read the best play and best score from the file
        readFromFile();
        getIni();
    }

}