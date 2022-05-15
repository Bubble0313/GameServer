package Controller;

public enum MessageType {
    LEVEL(1),
    DIRECTION(2),
    NAME(0),
    SNAKE_HEAD_X(3),
    SNAKE_HEAD_Y(4),
    SNAKE_LENGTH(5),
    FOOD_X(6),
    FOOD_Y(7),
    EATEN(8),
    HIT(9);

    public final int number;

    private MessageType(int number) {
        this.number = number;
    }
}
