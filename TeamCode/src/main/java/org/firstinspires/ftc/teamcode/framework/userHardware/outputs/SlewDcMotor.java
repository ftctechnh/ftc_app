package org.firstinspires.ftc.teamcode.framework.userHardware.outputs;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.framework.AbstractOpMode;

public class SlewDcMotor implements DcMotor, DcMotorEx, Runnable{
    //Motor
    private DcMotor motor;
    private DcMotorEx motorEx;

    private double slewSpeed = 0.1;
    private double lastSpeed = 0;
    private double currentSpeed = 0;
    private double SetPower = 0;
    private int encoderOffset = 0;

    //Threading
    private boolean running = true;
    private Thread t;

    //Motor
    public SlewDcMotor(DcMotor motor){
        this.motor = motor;
        this.motorEx = (DcMotorEx) motor;

        //Threading
        t = new Thread(this,"motor");
        t.start();
    }

    //Threading
    @Override
    public void run() {
        while(running&& AbstractOpMode.isOpModeActive()){
            motorSlew(getSetPower());
            AbstractOpMode.delay(15);
        }
        shutDown();
    }

    public void stop() {
        running = false;
    }

    private void shutDown(){
        setMotorDisable();
        setVelocity(0);
        setSlewSpeed(0);
        setPower(0);
        setCurrentPosition(0);
        setTargetPosition(0);
        setMode(RunMode.STOP_AND_RESET_ENCODER);
    }

    //Motor
    public void setCurrentPosition(int position){
        motor.setMode(RunMode.STOP_AND_RESET_ENCODER);
        encoderOffset = position;
    }

    public void setSlewSpeed(double slewSpeed){
        this.slewSpeed = slewSpeed;
    }

    @Override
    public void setDirection(Direction direction) {
        motor.setDirection(direction);
    }

    @Override
    public Direction getDirection() {
        return motor.getDirection();
    }

    @Override
    public void setPower(double SetPower) {this.SetPower = SetPower;}

    @Override
    public double getPower() {return motor.getPower();}

    public double getSetPower() {return SetPower;}

    public void motorSlew(double newSpeed) {
        currentSpeed = lastSpeed;
        lastSpeed = newSpeed;
        //If no speed change stop function
        if(currentSpeed == newSpeed){return;}
        if(currentSpeed > newSpeed){
            //We are slowing down
            if(currentSpeed-newSpeed<slewSpeed){
                motor.setPower(newSpeed);
                return;
            }
            motor.setPower(currentSpeed-slewSpeed);
            lastSpeed = (currentSpeed-slewSpeed);
            return;
        }
        if(currentSpeed < newSpeed){
            //We are speeding up
            if(newSpeed-currentSpeed<slewSpeed){
                motor.setPower(newSpeed);
                return;
            }
            motor.setPower(currentSpeed+slewSpeed);
            lastSpeed = (currentSpeed+slewSpeed);
            return;
        }
    }

    @Override
    public MotorConfigurationType getMotorType() {
        return motor.getMotorType();
    }

    @Override
    public void setMotorType(MotorConfigurationType motorType) {
        motor.setMotorType(motorType);
    }

    @Override
    public DcMotorController getController() {
        return motor.getController();
    }

    @Override
    public int getPortNumber() {
        return motor.getPortNumber();
    }

    @Override
    public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        motor.setZeroPowerBehavior(zeroPowerBehavior);
    }

    @Override
    public ZeroPowerBehavior getZeroPowerBehavior() {
        return motor.getZeroPowerBehavior();
    }

    @Override
    public void setPowerFloat() {
        motor.setPowerFloat();
    }

    @Override
    public boolean getPowerFloat() {
        return motor.getPowerFloat();
    }

    @Override
    public void setTargetPosition(int position) {
        motor.setTargetPosition(position-encoderOffset);
    }

    @Override
    public int getTargetPosition() {
        return motor.getTargetPosition()+encoderOffset;
    }

    @Override
    public boolean isBusy() {
        return motor.isBusy();
    }

    @Override
    public int getCurrentPosition() {
        return motor.getCurrentPosition()+encoderOffset;
    }

    @Override
    public void setMode(RunMode mode) {
        if(mode == RunMode.STOP_AND_RESET_ENCODER){
            encoderOffset = 0;
        }
        motor.setMode(mode);
    }

    @Override
    public RunMode getMode() {
        return motor.getMode();
    }

    @Override
    public Manufacturer getManufacturer() {
        return motor.getManufacturer();
    }

    @Override
    public String getDeviceName() {
        return motor.getDeviceName();
    }

    @Override
    public String getConnectionInfo() {
        return motor.getConnectionInfo();
    }

    @Override
    public int getVersion() {
        return motor.getVersion();
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        motor.resetDeviceConfigurationForOpMode();
    }

    @Override
    public void close() {
        motor.close();
    }

    //MotorEx
    @Override
    public void setMotorEnable() {
        motorEx.setMotorEnable();
    }

    @Override
    public void setMotorDisable() {
        motorEx.setMotorDisable();
    }

    @Override
    public boolean isMotorEnabled() {
        return motorEx.isMotorEnabled();
    }

    @Override
    public void setVelocity(double angularRate) {
        motorEx.setVelocity(angularRate);
    }

    @Override
    public void setVelocity(double angularRate, AngleUnit unit) {
        motorEx.setVelocity(angularRate, unit);
    }

    @Override
    public double getVelocity() {
        return motorEx.getVelocity();
    }

    @Override
    public double getVelocity(AngleUnit unit) {
        return motorEx.getVelocity(unit);
    }

    @Override
    public void setPIDCoefficients(RunMode mode, PIDCoefficients pidCoefficients) {
        motorEx.setPIDCoefficients(mode, pidCoefficients);
    }

    @Override
    public void setPIDFCoefficients(RunMode mode, PIDFCoefficients pidfCoefficients) throws UnsupportedOperationException {
        motorEx.setPIDFCoefficients(mode, pidfCoefficients);
    }

    @Override
    public void setVelocityPIDFCoefficients(double p, double i, double d, double f) {
        motorEx.setVelocityPIDFCoefficients(p, i, d, f);
    }

    @Override
    public void setPositionPIDFCoefficients(double p) {
        motorEx.setPositionPIDFCoefficients(p);
    }

    @Override
    public PIDCoefficients getPIDCoefficients(RunMode mode) {
        return motorEx.getPIDCoefficients(mode);
    }

    @Override
    public PIDFCoefficients getPIDFCoefficients(RunMode mode) {
        return motorEx.getPIDFCoefficients(mode);
    }

    @Override
    public void setTargetPositionTolerance(int tolerance) {
        motorEx.setTargetPositionTolerance(tolerance);
    }

    @Override
    public int getTargetPositionTolerance() {
        return motorEx.getTargetPositionTolerance();
    }
}
