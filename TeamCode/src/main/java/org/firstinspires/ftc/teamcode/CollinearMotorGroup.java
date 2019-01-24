package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import java.util.ArrayList;

public class CollinearMotorGroup implements DcMotor {
    ArrayList<DcMotor> motors = new ArrayList<>();
    DcMotor motorPrime;

    CollinearMotorGroup(ArrayList<DcMotor> motors)
    {
        this.motors = motors;
        motorPrime = motors.get(0);
        this.setMotorType(motorPrime.getMotorType());
    }

    @Override
    public void setDirection(Direction direction) {
        for (DcMotor motor:motors) {
            motor.setDirection(direction);
        }
    }

    @Override public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior)
    {
        for (DcMotor motor:motors) {
            motor.setZeroPowerBehavior(zeroPowerBehavior);
        }
    }

    @Override public void setPower(double power)
    {
        for (DcMotor motor:motors) {
            motor.setPower(power);
        }
    }

    @Override public void setTargetPosition(int position)
    {
        for (DcMotor motor:motors) {
            motor.setTargetPosition(position);
        }
    }

    @Override public void setMode(RunMode mode)
    {
        for (DcMotor motor:motors) {
            motor.setMode(mode);
        }
    }

    @Override public boolean isBusy()
    {
        int sum = motors.size();
        for (DcMotor motor:motors) {
            sum += motor.isBusy()? 1 : 0;
        }

        return sum > motors.size() / 2;
    }

    @Override public int getCurrentPosition()
    {
        int sum = motors.size();
        for (DcMotor motor:motors) {
            sum += motor.getCurrentPosition();
        }

        return sum / motors.size();
    }

    @Override
    public void setMotorType(MotorConfigurationType motorType) {
        for (DcMotor motor:motors) {
            motor.setMotorType(motorType);
        }
    }

    @Override
    public DcMotorController getController() {
        return motorPrime.getController();
    }

    @Override
    @Deprecated
    public void setPowerFloat() {
        for (DcMotor motor:motors) {
            motor.setPowerFloat();
        }
    }

    @Override
    @Deprecated
    public boolean getPowerFloat() {
        return motorPrime.getPowerFloat();
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        for (DcMotor motor:motors) {
            motor.resetDeviceConfigurationForOpMode();
        }
    }

    @Override
    public void close() {
        for (DcMotor motor:motors) {
            motor.close();
        }
    }

    @Override
    public MotorConfigurationType getMotorType() {
        return motorPrime.getMotorType();
    }

    @Override
    public int getPortNumber() {
        return motorPrime.getPortNumber();
    }

    @Override
    public ZeroPowerBehavior getZeroPowerBehavior() {
        return motorPrime.getZeroPowerBehavior();
    }

    @Override
    public int getTargetPosition() {
        return motorPrime.getTargetPosition();
    }

    @Override
    public int getVersion() {
        return motorPrime.getVersion();
    }

    @Override
    public Manufacturer getManufacturer() {
        return motorPrime.getManufacturer();
    }

    @Override
    public String getConnectionInfo() {
        return motorPrime.getConnectionInfo();
    }

    @Override
    public Direction getDirection() {
        return motorPrime.getDirection();
    }

    @Override
    public String getDeviceName() {
        return motorPrime.getDeviceName();
    }

    @Override
    public RunMode getMode() {
        return motorPrime.getMode();
    }

    @Override
    public double getPower() {
        return motorPrime.getPower();
    }


}
