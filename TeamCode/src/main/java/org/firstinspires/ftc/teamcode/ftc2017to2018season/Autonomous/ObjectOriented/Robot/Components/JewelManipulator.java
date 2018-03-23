package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.ObjectOriented.Robot.Components;

import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.ObjectOriented.Robot.Components.Subcomponents.Jewel_Detector;
import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.ObjectOriented.Robot.Components.Subcomponents.Robot_Servo;

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


public class JewelManipulator {

    Robot_Servo jewelMain = new Robot_Servo();
    Robot_Servo jewelRotate = new Robot_Servo();

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

    public void knockBall(){
        Jewel_Detector jewelDetector = new Jewel_Detector();

        jewelDetector.getOrder();

    }
}
