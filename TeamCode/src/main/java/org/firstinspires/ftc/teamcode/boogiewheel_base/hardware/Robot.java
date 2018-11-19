package org.firstinspires.ftc.teamcode.boogiewheel_base.hardware;

import org.firstinspires.ftc.teamcode.framework.AbstractOpMode;

public class Robot {

    private HardwareDevices hardware;

    public Robot(){
        hardware = new HardwareDevices();
    }

    public void setDriveY(double y){
        hardware.setDriveY(y);
    }

    public void setDriveZ(double z){
        hardware.setDriveZ(z);
    }

    public void setPower(double l, double r){
        hardware.setPower(l,r);
    }

    public void updateDrive(){
        hardware.updateDrive();
    }

    public void turnTo(double angle, double speed, double error, int period){
        hardware.turnTo(angle, speed, error, period);
    }

    public int[][] recordPath(int numSamples,int timeInterval) {
        return hardware.recordPath(numSamples, timeInterval);
    }

    public void runPath(int[] left, int[] right, int timeInterval) {
        hardware.runPath(left, right, timeInterval);
    }

    public void driveTo(double distance, double speed){
        hardware.driveTo(distance, speed);
    }

    public void setPosition(int position, double power) {
        hardware.setPosition(position, power);
    }

    public void stop(){
        hardware.stop();
    }

    public void delay(int time){
        AbstractOpMode.delay(time);
    }

    public boolean isGyroCalibrated() {
        return hardware.isGyroCalibrated();
    }

    public double GyroCalibrationTime() {
        return hardware.GyroCalibrationTime();
    }

    public double getHeading(){
        return hardware.getHeading();
    }
}
