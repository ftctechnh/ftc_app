package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CompassSensor;

/**
 * Created by djfigs1 on 9/23/16.
 */
abstract class RobotAutoBeacons extends RobotHardware {

    //region functions with while (they need redesigining)
    //WHILE
    public void turnDegrees(int degrees, double speed) {
        int startingZ = gyroSensor.rawZ();
        int finishingZ = startingZ + degrees;

        if (degrees > 0) {
            while (gyroSensor.rawZ() < finishingZ) {
                set_drive_power(speed, -speed);
            }

        } else {
            while (gyroSensor.rawZ() > finishingZ) {
                set_drive_power(-speed, speed);
            }
        }
    }

    //WHILE
    public void forwardUntilDistance(double cm, double speed) {
        while (rangeSensor.cmUltrasonic() > cm) {
            set_drive_power(speed, speed);
        }
    }

    //WHILE
    public void moveForTime(long time, double speed, double speed2) {
        long endTime = System.currentTimeMillis() + time;

        boolean trigger = false;
        while (!trigger) {
            if (System.currentTimeMillis() >= endTime) {
                trigger = true;
                stopdrive();
            } else {
                set_drive_power(speed, speed2);
            }
        }
        trigger = false;
    }

    //WHILE
    public void moveUntilLine(VV_LINE_COLOR color, double speed) {
        boolean trigger = false;
        int threshold = 15;

        switch (color) {
            case RED:
                while (!trigger) {
                    if (leftColorSensor.red() > threshold) {
                        //Hey Emmanuel, guess who you don't have in your sights? Rachel.
                        trigger = true;
                        stopdrive();
                    } else {
                        set_drive_power(speed, speed);
                    }
                }
                break;
            case BLUE:
                while (!trigger) {
                    if (leftColorSensor.blue() > threshold) {
                        //Hey Emmanuel, guess who you don't have in your sights? Rachel.
                        trigger = true;
                        stopdrive();
                    } else {
                        set_drive_power(speed, speed);
                    }
                }
                break;
            case WHITE:
                while (!trigger) {
                    if (leftColorSensor.blue() > threshold && leftColorSensor.red() > threshold && leftColorSensor.green() > 15) {
                        //Hey Emmanuel, guess who you don't have in your sights? Rachel.
                        trigger = true;
                        stopdrive();
                    } else {
                        set_drive_power(speed, speed);
                    }
                }
                break;
        }
    }

    //WHILE
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
    //endregion

    int threshold = 15;

}
