package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by djfigs1 on 9/23/16.
 */
abstract class RobotAutoBecaons extends RobotHardware {

    enum VV_BEACON_COLOR {RED, BLUE, NONE}
    enum VV_LINE_COLOR {RED, BLUE, WHITE}
    enum ROBOT_LINE_FOLLOW_STATE {LEFT, RIGHT, BOTH, NONE}

    VV_BEACON_COLOR getBeaconColor() {
        int red = beaconColorSensor.red();
        int blue = beaconColorSensor.blue();

        if (red >= 15 && red > blue){
            return VV_BEACON_COLOR.RED;
        }else{
            if (blue >= 15 && blue > red) {
                return VV_BEACON_COLOR.BLUE;
            }else{
                return VV_BEACON_COLOR.NONE;
            }
        }
    }

    void pushBeaconButton() {
        //TODO Push Beacon Button
    }

    public ROBOT_LINE_FOLLOW_STATE getLineFollowState(VV_LINE_COLOR color, int threshold) {
        switch (color) {
            case RED:
                if (leftColorSensor != null || rightColorSensor != null) {
                    if (leftColorSensor.red() >= threshold && rightColorSensor.red() < threshold) {
                        return ROBOT_LINE_FOLLOW_STATE.LEFT;
                    }
                    if (leftColorSensor.red() < threshold && rightColorSensor.red() >= threshold) {
                        return ROBOT_LINE_FOLLOW_STATE.RIGHT;
                    }
                    if (leftColorSensor.red() >= threshold && rightColorSensor.red() >= threshold) {
                        return ROBOT_LINE_FOLLOW_STATE.BOTH;
                    }
                    if (leftColorSensor.red() < threshold && rightColorSensor.red() < threshold) {
                        return ROBOT_LINE_FOLLOW_STATE.NONE;
                    }
                }
            case BLUE:
                if (leftColorSensor != null || rightColorSensor != null) {
                    if (leftColorSensor.blue() >= threshold && rightColorSensor.blue() < threshold) {
                        return ROBOT_LINE_FOLLOW_STATE.LEFT;
                    }
                    if (leftColorSensor.blue() < threshold && rightColorSensor.blue() >= threshold) {
                        return ROBOT_LINE_FOLLOW_STATE.RIGHT;
                    }
                    if (leftColorSensor.blue() >= threshold && rightColorSensor.blue() >= threshold) {
                        return ROBOT_LINE_FOLLOW_STATE.BOTH;
                    }
                    if (leftColorSensor.blue() < threshold && rightColorSensor.blue() < threshold) {
                        return ROBOT_LINE_FOLLOW_STATE.NONE;
                    }
                }
            case WHITE:
                break;
        }

        return null;
    }

    void followLine(VV_LINE_COLOR color) {
        boolean trigger = false;
        boolean foundLine = false;
        boolean stopped = false;
        double slowSpeed = 0.07;
        double fastSpeed = 0.25;

        int threshold = 15;

        while (!foundLine) {
            switch (color) {
                case RED:
                    while (!trigger) {
                        if ((leftColorSensor.red() > threshold) || (rightColorSensor.red() > threshold)) {
                            foundLine = true;
                        } else {
                            set_drive_power(0.25, 0.25);
                        }
                    }
                case BLUE:
                    while (!trigger) {
                        if ((leftColorSensor.blue() > threshold) || (rightColorSensor.blue() > threshold)) {
                            foundLine = true;
                        } else {
                            set_drive_power(0.25, 0.25);
                        }
                    }
                case WHITE:
                    while (!trigger) {
                        if ((leftColorSensor.blue() > threshold && leftColorSensor.red() > threshold && leftColorSensor.green() > threshold) || (rightColorSensor.blue() > threshold && rightColorSensor.red() > threshold && rightColorSensor.green() > threshold)) {
                            foundLine = true;
                        } else {
                            set_drive_power(0.25, 0.25);
                        }
                    }
            }
        }

        //we found a line
        while (!stopped) {
            switch (getLineFollowState(color, threshold)) {
                case LEFT:
                    set_drive_power(fastSpeed, 0);
                    break;

                case RIGHT:
                    set_drive_power(0, fastSpeed);
                    break;

                case BOTH:
                    set_drive_power(slowSpeed, slowSpeed);
                    break;

                case NONE:
                    //Since we've found a line, stop.
                    set_drive_power(0,0);
                    break;
            }
        }
    }

    int threshold = 15;

    abstract void getColor();
    abstract void getBeacons();
    abstract void beaconAction();

}
