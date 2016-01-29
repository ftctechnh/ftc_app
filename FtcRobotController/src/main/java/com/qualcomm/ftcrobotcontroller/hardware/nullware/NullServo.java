package com.qualcomm.ftcrobotcontroller.hardware.nullware;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

/**
 * A null object form of {@link Servo}
 * This prevents having to null-check in case your motor is unplugged
 */
public class NullServo extends Servo implements NullHardware {

    public NullServo() {
        super(null, 0);
    }

    @Override
    public String getConnectionInfo() {
        return "Null Hardware";
    }

    @Override
    public String getDeviceName() {
        return "Null Hardware";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public ServoController getController() {
        return null;
    }

    @Override
    public void setDirection(Direction direction) {

    }

    @Override
    public Direction getDirection() {
        return Direction.FORWARD;
    }

    @Override
    public int getPortNumber() {
        return 0;
    }

    @Override
    public void setPosition(double position) {

    }

    @Override
    public double getPosition() {
        return 0;
    }

    @Override
    public void scaleRange(double min, double max) {

    }

}
