package Model;

import Controller.Level;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Random;

@Getter
@Setter
public class GameModel {
    private ArrayList<Integer> snakeX = new ArrayList<>();//X coordinates of each rectangle that makes up of the snake
    private ArrayList<Integer> snakeY = new ArrayList<>();//Y coordinates of each rectangle that makes up of the snake
    private Direction direction;//the direction for the snake to make next move
    private Direction lastDirection;//the direction when the snake last made a move
    private int length;//length of the snake
    private Boolean isStart;//whether the game has started or not
    private int speed;//the speed of the snake, i.e. the interval of timer running
    private String player;//player's name
    private int score;//score gained in the game
    private Random ran = new Random();

    public GameModel(){
        direction = Direction.RIGHT;//define the initial snake direction as right
        lastDirection = Direction.RIGHT;
        isStart = false;
        score = 0;
        speed = Level.L1.speed;
        player = "Bot";
    }

    public void initialiseSnake(int len, int headX, int headY, int grid){
        length = len;
        snakeX.add(0, headX*grid);
        snakeY.add(0, headY*grid);
        for(int i=1; i<length; i++){
            snakeX.add(i, (headX-i)*grid);
        }
        for(int i=1; i<length; i++){
            snakeY.add(i, headY*grid);
        }
    }//set snake body based on snake head

    public void updateSnake(int grid, int width, int height){
        //move the body
        for(int i = length-1; i>0; i--){
            snakeX.set(i, snakeX.get(i-1));
            snakeY.set(i, snakeY.get(i-1));
        }
        //move the head
        moveHead(grid, width, height);
    }

    public void moveHead(int grid, int width, int height) {
        //based on the direction of the snake, check if the next move will make the snake out of the game border
        //if the next move makes the snake still within the border, then move the snake
        //else the game is over as the snake is out of the game border

        //create local variable for gameModel.getDirection()
        //synchronisation issue
        int snakeHeadX = snakeX.get(0);
        int snakeHeadY = snakeY.get(0);
        if(direction.equals(Direction.RIGHT)){
            if(snakeHeadX + grid <= width-grid){
                snakeX.set(0, snakeHeadX + grid);
            }else{
                setIsStart(false);
            }
        }

        if(direction.equals(Direction.LEFT)){
            if(snakeHeadX - grid >= 0){
                snakeX.set(0, snakeHeadX - grid);
            }else{
                setIsStart(false);
            }
        }

        if(direction.equals(Direction.UP)){
            if(snakeHeadY - grid >= 0){
                snakeY.set(0, snakeHeadY - grid);
            }else{
                setIsStart(false);
            }
        }

        if(direction.equals(Direction.DOWN)){
            if(snakeHeadY + grid <= height-grid){
                snakeY.set(0, snakeHeadY + grid);
            }else{
                setIsStart(false);
            }
        }
        //after making a move, set the current direction to last direction
        setLastDirection(direction);
    }
}
