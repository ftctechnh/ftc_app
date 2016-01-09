package com.qualcomm.ftcrobotcontroller.hardware.nullware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * A null object form of {@link DcMotor}
 * This prevents having to null-check in case your motor is unplugged
 */
public class NullDcMotor extends DcMotor implements NullHardware {

    public NullDcMotor() {
        super(null, 0);
    }

    @Override
    public void close() {

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
    public DcMotorController getController() {
        return null;
    }


    @Override
    public void setDirection(Direction direction) {

    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public int getPortNumber() {
        return 0;
    }

    @Override
    public void setPower(double power) {

    }

    @Override
    public double getPower() {
        return 0;
    }

    @Override
    public void setPowerFloat() {

    }

    @Override
    public boolean getPowerFloat() {
        return false;
    }

    @Override
    public void setTargetPosition(int position) {

    }

    @Override
    public int getTargetPosition() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void setMode(DcMotorController.RunMode mode) {

    }

    @Override
    public void setChannelMode(DcMotorController.RunMode mode) {

    }


    @Override
    public DcMotorController.RunMode getChannelMode() {
        return DcMotorController.RunMode.RUN_WITHOUT_ENCODERS;
    }


}
