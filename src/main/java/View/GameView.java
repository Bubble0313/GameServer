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

import java.util.ArrayList;

@Getter
@Setter
public class GameView {
    private Scene firstScene;
    private Scene secondScene;
    private Text initialText;
    private Text numText;
    private ArrayList<Text> nameText = new ArrayList<>();
    private Text speedText;
    private ArrayList<Text> currentScoreText = new ArrayList<>();
    private Text bestScoreText;
    private Text bestPlayerText;
    private Button button;
    private ArrayList<TextField> nameTextField = new ArrayList<>();
    private ChoiceBox levelChoiceBox;
    private ChoiceBox playerChoiceBox;
    private Integer snakeNum = 1;

    public static Rectangle[][] whole;

    public GameView(Integer panelHeight, Integer panelWidth) {
        //first scene
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(panelHeight/20);
        gridPane.setHgap(panelWidth/30);
        //Instruction
        initialText = new Text("Welcome to the Game of Snake!\n" +
                "Start the game by choosing player number, game level and player name(s).\n " +
                "The higher the level you choose, the quicker the snake moves.\n " +
                "If there's no input, default player name is Bot, default game level is Level 1.");
        initialText.setWrappingWidth(panelWidth*0.8);
        initialText.setLineSpacing(10);
        initialText.setTextAlignment(TextAlignment.CENTER);
        initialText.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/30));
        //Ask for player number
        numText = new Text("Player number:");
        numText.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/40));
        playerChoiceBox = new ChoiceBox();
        playerChoiceBox.getItems().addAll("1", "2");
        //Ask for game speed
        speedText = new Text("Game Level:");
        speedText.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/40));
        levelChoiceBox = new ChoiceBox();
        levelChoiceBox.getItems().addAll("Level 1", "Level 2", "Level 3", "Level 4", "Level 5", "Level 6", "Level 7", "Level 8", "Level 9");
        //Ask for player name
        Text nameText1 = new Text("Player1 name:\n(Use keyboard arrows\nto control direction)");
        nameText1.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/40));
        nameText.add(nameText1);
        TextField nameTextField1 = new TextField();
        nameTextField.add(nameTextField1);
        //button to click to start the game
        button = new Button("Start");
        //layout
        gridPane.add(initialText,0,0);
        gridPane.add(numText, 0, 1);
        gridPane.add(playerChoiceBox,1,1);
        gridPane.add(speedText,0,2);
        gridPane.add(levelChoiceBox,1,2);
        gridPane.add(nameText.get(0),0,3);
        gridPane.add(nameTextField.get(0),1,3);
        gridPane.add(button,0,5);
        GridPane.setColumnSpan(initialText,2);
        GridPane.setColumnSpan(button,2);
        GridPane.setHalignment(button, HPos.CENTER);
        //set action listener to choiceBox
        playerChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
                (obs, oldV, newV) -> {
                    if (newV.equals(1)) {
                        Text nameText2 = new Text("Player2 name:\n(Use 4 keys WASD\nto control direction)");
                        nameText2.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/40));
                        nameText.add(nameText2);
                        gridPane.add(nameText.get(1), 0, 4);
                        TextField nameTextField2 = new TextField();
                        nameTextField.add(nameTextField2);
                        gridPane.add(nameTextField2, 1, 4);
                        snakeNum = 2;
                    }
                }
        );
        //setting the first scene
        firstScene = new Scene(gridPane);
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
        Text currentScoreText1 = new Text(grid*2, gameUpBorder/2, "");
        currentScoreText1.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/35));
        currentScoreText.add(currentScoreText1);
        //show best score and best player
        bestScoreText = new Text(panelWidth/2, gameUpBorder*0.25, "Best Score: N/A");
        bestScoreText.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/35));
        bestPlayerText = new Text(panelWidth/2, gameUpBorder*0.75, "Best Player: N/A");
        bestPlayerText.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/35));
        list.addAll(currentScoreText.get(0), bestScoreText, bestPlayerText);
        if (snakeNum.equals(2)){
            Text currentScoreText2 = new Text(grid*2, gameUpBorder*0.75, "");
            currentScoreText2.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/35));
            currentScoreText.add(currentScoreText2);
            list.add(currentScoreText.get(1));
        }
        //setting the second scene
        secondScene = new Scene(root);
    }

    public static void paintTail(int x, int y, int grid){
        whole[x/grid][y/grid].setFill(Color.BLACK);
    }

    public static void paintHead(int x, int y, int grid){
        whole[x/grid][y/grid].setFill(Color.YELLOW);
    }

    public static void paintBody(int x, int y, int grid){
        whole[x/grid][y/grid].setFill(Color.BLUE);
    }

    public static void paintFood(int x, int y, int grid) {
        whole[x/grid][y/grid].setFill(Color.RED);
    }

}
