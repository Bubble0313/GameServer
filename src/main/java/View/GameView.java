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
    private Text initialText;
    private Text numText;
    private Text name1Text;
    private Text name2Text;
    private Text speedText;
    private Text currentScore1Text;
    private Text currentScore2Text;
    private Text bestScoreText;
    private Text bestPlayerText;
    private Button button;
    private TextField name1TextField;
    private TextField name2TextField;
    private ChoiceBox levelChoiceBox;
    private ChoiceBox playerChoiceBox;
    private Integer snakeNum = 1;

    public static Rectangle[][] whole;

    public GameView(Integer grid, Integer panelHeight, Integer panelWidth, Integer gameUpBorder, Integer num) {
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
        name1Text = new Text("Player1 name:\n(Use keyboard arrows\nto control direction)");
        name1Text.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/40));
        name1TextField = new TextField();
        name2Text = new Text("Player2 name:\n(Use 4 keys WASD\nto control direction)");
        name2Text.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/40));
        name2Text.setVisible(false);
        name2TextField = new TextField();
        name2TextField.setVisible(false);
        //button to click to start the game
        button = new Button("Start");
        //layout
        gridPane.add(initialText,0,0);
        gridPane.add(numText, 0, 1);
        gridPane.add(playerChoiceBox,1,1);
        gridPane.add(speedText,0,2);
        gridPane.add(levelChoiceBox,1,2);
        gridPane.add(name1Text,0,3);
        gridPane.add(name1TextField,1,3);
        gridPane.add(name2Text, 0, 4);
        gridPane.add(name2TextField, 1, 4);
        gridPane.add(button,0,5);
        GridPane.setColumnSpan(initialText,2);
        GridPane.setColumnSpan(button,2);
        GridPane.setHalignment(button, HPos.CENTER);
        //setting the first scene
        //firstScene = new Scene(gridPane,panelWidth,panelHeight);
        firstScene = new Scene(gridPane);
        //set action listener to choiceBox
        playerChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
                (obs, oldV, newV) -> {
                    if (newV.equals(1)) {
                        name2Text.setVisible(true);
                        name2TextField.setVisible(true);
                        snakeNum = 2;
                    }
                    if (newV.equals(0)) {
                        name2Text.setVisible(false);
                        name2TextField.setVisible(false);
                        snakeNum = 1;
                    }
                }
        );

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
        currentScore1Text = new Text(grid*2, gameUpBorder/2, "");
        currentScore1Text.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/35));
        currentScore2Text = new Text(grid*2, gameUpBorder*0.75, "");
        currentScore2Text.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/35));
        currentScore2Text.setVisible(false);
        //show best score and best player
        bestScoreText = new Text(panelWidth/2, gameUpBorder*0.25, "Best Score: N/A");
        bestScoreText.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/35));
        bestPlayerText = new Text(panelWidth/2, gameUpBorder*0.75, "Best Player: N/A");
        bestPlayerText.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.REGULAR, panelWidth/35));

        list.addAll(currentScore1Text, currentScore2Text, bestScoreText, bestPlayerText);
        //setting the second scene
        //secondScene = new Scene(root, panelWidth, panelHeight);
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
