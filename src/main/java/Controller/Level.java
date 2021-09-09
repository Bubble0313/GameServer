package Controller;

public enum Level {
    L1(500),
    L2(400),
    L3(300),
    L4(200),
    L5(120),
    L6(90),
    L7(70),
    L8(50),
    L9(30);

    public final int speed;

    private Level(int speed){
        this.speed = speed;
    }
}
