package Controller;

import Constants.GameConstants;
import Model.GameModel;
import View.GameView;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class GameController {
    public ArrayList<GameModel> models = new ArrayList<>();
    public GameView view;
    public int grid = GameConstants.grid;
    public int num = GameConstants.num;
    public int gameWidth = GameConstants.gameWidth;
    public int gameHeight = GameConstants.gameHeight;
    public int headX;
    public int headY;
    public int foodX;//X coordinate of the food
    public int foodY;//Y coordinate of the food
    public int bestScore = -1;//the highest score
    public String bestPlayer = "N/A";//player that scores the highest

    public void startGame(){
    }

    public void fileOperation(String record, String ini){
    }
}