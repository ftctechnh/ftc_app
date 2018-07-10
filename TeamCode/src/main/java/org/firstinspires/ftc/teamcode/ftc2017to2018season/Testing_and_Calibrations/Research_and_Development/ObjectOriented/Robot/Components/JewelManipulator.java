package org.firstinspires.ftc.teamcode.ftc2017to2018season.Testing_and_Calibrations.Research_and_Development.ObjectOriented.Robot.Components;

import org.firstinspires.ftc.teamcode.ftc2017to2018season.Testing_and_Calibrations.Research_and_Development.ObjectOriented.Robot.Components.Subcomponents.Robot_Servo;
import org.firstinspires.ftc.teamcode.ftc2017to2018season.Testing_and_Calibrations.Research_and_Development.ObjectOriented.Robot.Components.Subcomponents.Sleep;

/**
 * Created by adityamavalankar on 3/20/18.
 */

enum manipulatorState {
    UP,
    DOWN,
    MIDDLE;
}

enum manipulatorRotateState {
    LEFT,
    RIGHT,
    MIDDLE;
}

enum direction {
    LEFT,
    RIGHT,
    MIDDLE;
}


public class JewelManipulator {

    Robot_Servo jewelMain = new Robot_Servo();
    Robot_Servo jewelRotate = new Robot_Servo();
    Sleep sleep = new Sleep();

    private void rotate(manipulatorRotateState inputRotation) {
        switch (inputRotation) {

            case LEFT:
                jewelRotate.setPosition(0.5);

            case RIGHT:
                jewelRotate.setPosition(1);

            case MIDDLE:
                jewelRotate.setPosition(0.74);

            default:
                jewelRotate.setPosition(0.74);
        }
    }

    private void manipulate(manipulatorState inputPosition) {
        switch (inputPosition) {

            case UP:
                jewelRotate.setPosition(0.95);

            case DOWN:
                jewelRotate.setPosition(0);

            case MIDDLE:
                jewelRotate.setPosition(0.65);

            default:
                jewelRotate.setPosition(0.95);
        }
    }

    public void jewelManipulate(direction rotateDirection) {
        JewelManipulator jewelManipulator = new JewelManipulator();

        jewelManipulator.rotate(manipulatorRotateState.MIDDLE);
        jewelManipulator.manipulate(manipulatorState.DOWN);

        sleep.sleep(75);
        switch (rotateDirection) {
            case RIGHT:
                jewelManipulator.rotate(manipulatorRotateState.RIGHT);
                break;
            case LEFT:
                jewelManipulator.rotate(manipulatorRotateState.LEFT);
                break;
            default:
                jewelManipulator.rotate(manipulatorRotateState.MIDDLE);
        }
        
        sleep.sleep(75);
        jewelManipulator.rotate(manipulatorRotateState.MIDDLE);
        sleep.sleep(75);
        jewelManipulator.manipulate(manipulatorState.UP);
        sleep.sleep(75);
    }
}
