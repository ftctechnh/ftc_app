package org.firstinspires.ftc.teamcode.robot.peripherals;


/**
 * Created by Derek on 2/6/2018.
 */

public class ClawPositions {

    //public enum Positions {
    //   CLOSED,CENTER,OPEN;
    //}

    public Position CLOSED;
    public Position CENTER;
    public Position OPEN;

    public ClawPositions() {
        CLOSED = new Position(0);
        CENTER = new Position(0.5);
        OPEN = new Position(1);
    }
    /*
    //what was i thinking when i wrote this????
    public Position getPositionByEnum(Positions positions) {
        switch (positions) {
            case OPEN:
                return OPEN;
            case CENTER:
                return CENTER;
            case CLOSED:
                return CLOSED;
        }
        throw new IllegalStateException("Impossible enum state");
    }
    */

}
