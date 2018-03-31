package org.firstinspires.ftc.teamcode.ftc2017to2018season.Testing_and_Calibrations.Research_and_Development.ObjectOriented.Robot.Components.Subcomponents;


public class Robot_Servo {

    public Robot_Servo servo;

    public Robot_Servo() {
        this.servo = servo;
    }

    public void setPosition(double position) {
        servo.setPosition(position);
    }

    public void setDirection(Direction direction) {
        switch (direction) {
            case FORWARD:
                servo.setDirection(Direction.FORWARD);
                break;
            case REVERSE:
                servo.setDirection(Direction.REVERSE);
                break;
            default:
                servo.setDirection(Direction.FORWARD);
                break;
        }
    }

    private enum Direction {
        FORWARD,
        REVERSE;
    }
}
