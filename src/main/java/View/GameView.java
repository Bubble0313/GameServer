package View;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameView {
    private Scene firstScene;
    private Scene secondScene;
    private Text initialText = new Text("Welcome to the Game of Snake!\n" +
            "Start the game by choosing player mode (Local VS Network Server VS Network Client).");
    private Text modeText = new Text("Player Mode:");
    private ChoiceBox modeChoiceBox = new ChoiceBox();
    private Text currentScoreText = new Text();
    private Button button;
    private GridPane gridPane = new GridPane();
    private Text nameText;
    private Text speedText;
    private Text portText;
    private TextField nameTextField;
    private ChoiceBox levelChoiceBox;
    private ChoiceBox portChoiceBox;
    private Text introText;
    private String mode = "Server";
    private Text bestScoreText;
    private Text bestPlayerText;

    public static Rectangle[][] whole;

    public GameView(Integer panelHeight, Integer panelWidth){
        createFirstScene(panelHeight, panelWidth);
    }

    public void createFirstScene(Integer panelHeight, Integer panelWidth){
        //first scene
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(panelHeight/20);
        gridPane.setHgap(panelWidth/30);
        //Instruction
        initialText.setWrappingWidth(panelWidth*0.8);
        initialText.setLineSpacing(10);
        initialText.setTextAlignment(TextAlignment.CENTER);
        initialText.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/30));
        //Ask for player mode
        modeText.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/40));
        modeChoiceBox.getItems().addAll("Local", "Network Server", "Network Client");
        //button to click to start the game
        button = new Button("Start");
        //layout
        gridPane.add(initialText,0,0);
        gridPane.add(modeText, 0, 1);
        gridPane.add(modeChoiceBox,1,1);
        gridPane.add(button,0,2);
        GridPane.setColumnSpan(initialText,2);
        GridPane.setColumnSpan(button,2);
        GridPane.setHalignment(button, HPos.CENTER);

        modeChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
                (obs, oldV, newV) -> {
                    if (newV.equals(2)) {
                        addInputView(panelWidth);
                        setMode("Client");
                    }
                    if (newV.equals(0)){
                        addInputView(panelWidth);
                        setMode("Local");
                    }
                }
        );

        //setting the first scene
        firstScene = new Scene(gridPane, panelWidth, panelHeight);
    }

    private void addInputView(Integer panelWidth) {
        //Give intro to game
        introText = new Text("Please select game level and player name(s).\n" +
                "The higher the level you choose, the quicker the snake moves.\n" +
                "If there's no input, default player name is Bot, and default game level is Level 1.");
        introText.setWrappingWidth(panelWidth*0.8);
        introText.setLineSpacing(10);
        introText.setTextAlignment(TextAlignment.CENTER);
        introText.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/40));
        //Ask for game speed
        speedText = new Text("Game Level:");
        speedText.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/40));
        levelChoiceBox = new ChoiceBox();
        levelChoiceBox.getItems().addAll("Level 1", "Level 2", "Level 3", "Level 4", "Level 5", "Level 6", "Level 7", "Level 8", "Level 9");
        //Ask for player name
        nameText = new Text("Player name:\n(Use keyboard arrows\nto control direction)");
        nameText.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/40));
        nameTextField = new TextField();
        //Ask for running port number
        portText = new Text("Client Port:");
        portText.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/40));
        portChoiceBox = new ChoiceBox();
        portChoiceBox.getItems().addAll("1111", "2222");

        gridPane.getChildren().remove(button);
        gridPane.add(introText, 0,2);
        gridPane.add(speedText,0,3);
        gridPane.add(levelChoiceBox,1,3);
        gridPane.add(nameText,0,4);
        gridPane.add(nameTextField,1,4);
        gridPane.add(portText, 0, 5);
        gridPane.add(portChoiceBox, 1, 5);
        gridPane.add(button,0,6);
        GridPane.setColumnSpan(introText,2);
    }

    public static void paintTail(int x, int y){
        whole[x][y].setFill(Color.BLACK);
    }

    public static void paintHead(int x, int y){
        whole[x][y].setFill(Color.YELLOW);
    }

    public static void paintBody(int x, int y){
        whole[x][y].setFill(Color.BLUE);
    }

    public static void paintFood(int x, int y) {
        whole[x][y].setFill(Color.RED);
    }

    public void createSecondScene(Integer grid, Integer panelWidth, Integer gameUpBorder, Integer num){
        //second scene
        Group root = new Group();
        ObservableList list = root.getChildren();
        //draw num*num squares with the length of grid, which forms the game area
        whole = new Rectangle[num][num];
        for(int i = 0; i<num ; i++){
            for(int j =0; j<num; j++){
                whole[i][j] = new Rectangle(i*grid,gameUpBorder+j*grid, grid, grid);
                list.add(whole[i][j]);
            }
        }
        //text to show current score
        currentScoreText = new Text(grid*2, gameUpBorder/2, "Current Score: 0");
        currentScoreText.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/35));
        //show best score and best player
        bestScoreText = new Text(panelWidth/2, gameUpBorder*0.25, "Best Score: N/A");
        bestScoreText.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/35));
        bestPlayerText = new Text(panelWidth/2, gameUpBorder*0.75, "Best Player: N/A");
        bestPlayerText.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/35));
        list.addAll(currentScoreText, bestScoreText, bestPlayerText);
        //setting the second scene
        secondScene = new Scene(root, panelWidth, panelWidth + gameUpBorder);
    }
}
