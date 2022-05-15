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

    public void fileOperation(){
    }

    public void paintSnake(int length, int snakeX, int snakeY) {
        view.paintHead(snakeX, snakeY);
        for (int i = length - 1; i > 0; i--) {
            view.paintBody(snakeX - i, snakeY);
        }
    }

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

    public void increaseSnakeLength(GameModel model){
        model.setLength(model.getLength() + 1);
        ArrayList<Integer> arrayListX = model.getSnakeX();
        ArrayList<Integer> arrayListY = model.getSnakeY();
        arrayListX.add(foodX);//it doesn't matter what value is added as the tail
        arrayListY.add(foodY);//as the value will be overridden when the snake moves
        model.setSnakeX(arrayListX);
        model.setSnakeY(arrayListY);
        model.setScore(model.getScore() + 1);
    }
}