package Constants;

import javafx.stage.Screen;

import java.lang.Math;

//the class should be related to model, but not view
public class GameConstants {
    private GameConstants(){}
    //number of horizontal cells and vertical cells, there will be num*num cells in total
    public static final int num = 50;
    public static final int snakeLength = 8;//initial snake length
    //define the game screen size based on the screen size with a percentage, so the game fits both big and small screens
    public static final double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    //public static getWidth method, return default value
    //set default static non-final value here, static set method, 0,0, update in ini file
    //snake model should not care about view, independent of view
    public static final double screenHeight =  Screen.getPrimary().getVisualBounds().getHeight();
    public static final int grid = (int) (Math.min(screenWidth, screenHeight)*0.8/num);//size of each cell

    //define game border and panel border
    public static final int gameWidth = grid * num;
    public static final int gameHeight = gameWidth;
    public static final int gameUpBorder = (int) (gameWidth*0.1);//space to show current score and best score
    public static final int panelWidth = gameWidth;
    public static final int panelHeight = gameHeight + gameUpBorder;

    public static final int longPacketLength = 1000;
    public static final int shortPacketLength = 10;
}