package org.firstinspires.ftc.teamcode.robot.peripherals.gripper;


import org.firstinspires.ftc.teamcode.robot.peripherals.PeripheralPosition;

/**
 * Created by Derek on 2/6/2018.
 */

public class GripperClampPositions {

    //public enum Positions {
    //   CLOSED,CENTER,OPEN;
    //}

    public PeripheralPosition CLOSED;
    public PeripheralPosition CENTER;
    public PeripheralPosition OPEN;

    public GripperClampPositions() {
        CLOSED = new PeripheralPosition(0);
        CENTER = new PeripheralPosition(0.5);
        OPEN = new PeripheralPosition(1);
    }
    /*
    //what was i thinking when i wrote this????
    public PeripheralPosition getPositionByEnum(Positions positions) {
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
