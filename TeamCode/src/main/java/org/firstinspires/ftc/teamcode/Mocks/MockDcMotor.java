package org.firstinspires.ftc.teamcode.Mocks;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.configuration.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Created by guberti on 1/9/2018.
 */

public class MockDcMotor implements DcMotorEx {

    double speed;
    RunMode mode;
    int targetPos;
    int currentPos;

    public MockDcMotor() {
        double speed = 0.0;
        RunMode mode = RunMode.RUN_WITHOUT_ENCODER;
        int targetPos = 0;
        int currentPos = 0;
    }

    @Override
    public MotorConfigurationType getMotorType() {
        return null;
    }

    @Override
    public void setMotorType(MotorConfigurationType motorType) {

    }

    @Override
    public DcMotorController getController() {
        return null;
    }

    @Override
    public int getPortNumber() {
        return 0;
    }

    @Override
    public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {

    }

    @Override
    public ZeroPowerBehavior getZeroPowerBehavior() {
        return null;
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
        targetPos = position;
    }

    @Override
    public int getTargetPosition() {
        return targetPos;
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    @Override
    public int getCurrentPosition() {
        return currentPos;
    }

    public void setCurrentPosition(int m) { currentPos = m;}

    @Override
    public void setMode(RunMode mode) {
        this.mode = mode;
    }

    @Override
    public RunMode getMode() {
        return mode;
    }

    @Override
    public void setDirection(Direction direction) {

    }

    @Override
    public Direction getDirection() {
        return null;
    }

    @Override
    public void setPower(double power) {
        this.speed = power;
    }

    @Override
    public double getPower() {
        return speed;
    }

    @Override
    public Manufacturer getManufacturer() {
        return null;
    }

    @Override
    public String getDeviceName() {
        return null;
    }

    @Override
    public String getConnectionInfo() {
        return null;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {

    }

    @Override
    public void close() {}

    @Override
    public void setMotorEnable() {}

    @Override
    public void setMotorDisable() {}

    @Override
    public boolean isMotorEnabled() {return false;}

    @Override
    public void setVelocity(double angularRate, AngleUnit unit) {}

    @Override
    public double getVelocity(AngleUnit unit) {return 0;}

    @Override
    public void setPIDCoefficients(RunMode mode, PIDCoefficients pidCoefficients) {}

    @Override
    public PIDCoefficients getPIDCoefficients(RunMode mode) {return null;}

    @Override
    public void setTargetPositionTolerance(int tolerance) {}

    @Override
    public int getTargetPositionTolerance() {return 0;}
}
